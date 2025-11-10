// rtc.js — Peer connections, DataChannels, Call controls
import { ICE_SERVERS } from './config.js';
import { addStrokeCRDT, delStrokeCRDT, redraw, exportFullState } from './canvas.js';

const peers = new Map(); // peerId -> { pc, dc, dcChat, queue, rttEMA, remoteAudio }
let sendSignalFn = null;
let peersAudioContainer = null;
let rttLabelSetter = (val)=>{};
let statusSetter = (txt)=>{};

// Audio
let localStream = null;
let isMuted = false;

export function initRTC({ sendSignal, peersAudioEl, setRTT, setStatus }) {
  sendSignalFn = sendSignal;
  peersAudioContainer = peersAudioEl;
  rttLabelSetter = setRTT;
  statusSetter = setStatus;
}

export function ensurePeer(peerId, initiator) {
  if (peers.has(peerId)) return;
  const pc = new RTCPeerConnection({ iceServers: ICE_SERVERS, iceCandidatePoolSize: 2 });
  const st = { pc, dc: null, dcChat: null, queue: [], rttEMA: null, remoteAudio: null };
  peers.set(peerId, st);

  pc.onicecandidate = (e) => { if (e.candidate) sendSignalFn(peerId, { type:'candidate', candidate: e.candidate }); };
  pc.onicecandidateerror = (e) => { console.warn('ICE error', e.errorText || e); };
  pc.onconnectionstatechange = () => statusSetter(`rtc ${peerId}: ${pc.connectionState}`);
  pc.ontrack = (ev) => {
    const st = peers.get(peerId); if (!st) return;
    if (!st.remoteAudio) {
      const audio = document.createElement('audio'); audio.autoplay = true; audio.playsInline = true; audio.dataset.peer = peerId;
      peersAudioContainer && peersAudioContainer.appendChild(audio);
      st.remoteAudio = audio;
    }
    st.remoteAudio.srcObject = ev.streams[0];
  };

  if (initiator) {
    const dcDraw = pc.createDataChannel('draw', { ordered:true });
    prepareDataChannel(peerId, dcDraw);
    const dcChat = pc.createDataChannel('chat', { ordered:true });
    prepareDataChannel(peerId, dcChat);
    negotiate(peerId);
  } else {
    pc.ondatachannel = (e) => prepareDataChannel(peerId, e.channel);
  }
}

async function negotiate(peerId) {
  const { pc } = peers.get(peerId)||{}; if (!pc) return;
  const offer = await pc.createOffer();
  await pc.setLocalDescription(offer);
  sendSignalFn(peerId, { type:'offer', sdp: offer.sdp });
}

export async function onSignal(fromPeerId, data) {
  let st = peers.get(fromPeerId);
  if (!st) { ensurePeer(fromPeerId, false); st = peers.get(fromPeerId); }
  const { pc } = st; if (!pc) return;
  if (data.type === 'offer') {
    await pc.setRemoteDescription({ type:'offer', sdp: data.sdp });
    const answer = await pc.createAnswer();
    await pc.setLocalDescription(answer);
    sendSignalFn(fromPeerId, { type:'answer', sdp: answer.sdp });
  } else if (data.type === 'answer') {
    await pc.setRemoteDescription({ type:'answer', sdp: data.sdp });
  } else if (data.type === 'candidate') {
    try { await pc.addIceCandidate(data.candidate); } catch (e) { console.warn('addIceCandidate fail', e); }
  }
}

function prepareDataChannel(peerId, dc) {
  const st = peers.get(peerId); if (!st) return;
  if (dc.label === 'draw') st.dc = dc;
  if (dc.label === 'chat') st.dcChat = dc;

  dc.onopen = () => {
    statusSetter(`${dc.label} dc open ↔ ${peerId}`);
    if (dc.label === 'draw') {
      flushQueue(peerId);
      const full = exportFullState();
      sendDC(peerId, { kind:'fullState', ...full });
      schedulePing(peerId);
    }
  };
  dc.onmessage = (e) => {
    if (dc.label === 'chat') {
      try {
        const m = JSON.parse(e.data);
        if (m.kind === 'chat') onChatMessage(m);
      } catch {}
      return;
    }
    onDCMessage(peerId, e.data);
  };
  dc.onclose = () => statusSetter(`${dc.label} dc closed ↔ ${peerId}`);
}

function onChatMessage(m) {
  const ev = new CustomEvent('chat:incoming', { detail: m });
  window.dispatchEvent(ev);
}

function onDCMessage(peerId, raw) {
  let msg; try { msg = JSON.parse(raw); } catch { return; }
  if (msg.kind === 'chat') { onChatMessage(msg); return; }
  if (msg.kind === 'fullState') {
    for (const s of msg.strokes||[]) addStrokeCRDT(s);
    for (const id of msg.tombstones||[]) delStrokeCRDT(id); // ensure CRDT removal reflected
    redraw(); return;
  }
  if (msg.kind === 'op') {
    const { opType, payload } = msg;
    if (opType === 'stroke:add') addStrokeCRDT(payload);
    if (opType === 'stroke:del') delStrokeCRDT(payload.id);
    redraw(); return;
  }
  if (msg.kind === 'ping') { sendDC(peerId, { kind:'pong', t: msg.t }); return; }
  if (msg.kind === 'pong') {
    const rtt = Date.now() - msg.t;
    const st = peers.get(peerId);
    st.rttEMA = st.rttEMA == null ? rtt : 0.6*st.rttEMA + 0.4*rtt;
    const v = Math.round(avgRTT());
    rttLabelSetter(Number.isFinite(v) ? v : '–');
  }
}

function sendDC(peerId, payload) {
  const st = peers.get(peerId); if (!st) return;
  if (!st.dc || st.dc.readyState !== 'open') { st.queue.push(payload); return; }
  try { st.dc.send(JSON.stringify(payload)); } catch { st.queue.push(payload); }
}

function flushQueue(peerId) {
  const st = peers.get(peerId);
  if (!st || !st.dc || st.dc.readyState !== 'open') return;
  while (st.queue.length) {
    const p = st.queue.shift();
    try { st.dc.send(JSON.stringify(p)); } catch { st.queue.unshift(p); break; }
  }
}

export function removePeer(peerId) {
  const st = peers.get(peerId); if (!st) return;
  try { st.dc?.close(); } catch {}
  try { st.dcChat?.close(); } catch {}
  try { st.pc?.close(); } catch {}
  if (st.remoteAudio && st.remoteAudio.srcObject) {
    try { st.remoteAudio.srcObject.getTracks().forEach(t=>t.stop()); } catch {}
  }
  peers.delete(peerId);
}

export function broadcastOp(op) {
  if (op.opType === 'stroke:add') addStrokeCRDT(op.payload);
  if (op.opType === 'stroke:del') delStrokeCRDT(op.payload.id);
  for (const pid of peers.keys()) sendDC(pid, op);
}

export function broadcastChat(myId, text) {
  const payload = { kind: 'chat', from: myId, text, ts: Date.now() };
  for (const pid of peers.keys()) { try { sendDC(pid, payload); } catch {} }
}

function schedulePing(peerId) {
  const st = peers.get(peerId); if (!st) return;
  const sendPing = () => {
    if (!peers.has(peerId) || !st.dc || st.dc.readyState !== 'open') return;
    sendDC(peerId, { kind:'ping', t: Date.now() });
    setTimeout(sendPing, 1500 + Math.random()*500);
  };
  setTimeout(sendPing, 500);
}

export function avgRTT() {
  const arr = Array.from(peers.values()).map(p => p.rttEMA).filter(v => v != null);
  if (!arr.length) return NaN;
  return arr.reduce((a,b)=>a+b,0)/arr.length;
}

// ===== Audio Call helpers =====
export async function startCall() {
  try {
    if (!localStream) {
      localStream = await navigator.mediaDevices.getUserMedia({ audio: { echoCancellation: true, noiseSuppression: true, autoGainControl: true } });
    }
    for (const [peerId, st] of peers) {
      for (const track of localStream.getTracks()) st.pc.addTrack(track, localStream);
      await renegotiate(peerId);
    }
    window.dispatchEvent(new CustomEvent('call:state', { detail: { started: true } }));
  } catch (e) {
    console.error('startCall error', e);
    alert('Không thể truy cập micro: ' + e.message);
  }
}

export function toggleMute() {
  if (!localStream) return;
  isMuted = !isMuted;
  for (const tr of localStream.getAudioTracks()) tr.enabled = !isMuted;
  window.dispatchEvent(new CustomEvent('call:muted', { detail: { muted: isMuted } }));
}

export async function hangupCall() {
  if (localStream) {
    for (const tr of localStream.getTracks()) { try { tr.stop(); } catch {} }
  }
  localStream = null; isMuted = false;
  peersAudioContainer && (peersAudioContainer.innerHTML = '');
  window.dispatchEvent(new CustomEvent('call:state', { detail: { started: false } }));
}

async function renegotiate(peerId) {
  const st = peers.get(peerId); if (!st) return;
  const offer = await st.pc.createOffer();
  await st.pc.setLocalDescription(offer);
  sendSignalFn(peerId, { type: 'offer', sdp: offer.sdp });
}
