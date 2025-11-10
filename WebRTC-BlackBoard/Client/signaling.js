// signaling.js — WebSocket signaling
import { SIGNAL_URL } from './config.js';

let ws = null;
let roomId = null;
let myId = null;
let statusSetter = (t)=>{};
let ensurePeerFn = null;
let onSignalFn = null;
let onPeerLeftFn = null;

export function initSignaling({ setStatus, ensurePeer, onSignal, onPeerLeft }) {
  statusSetter = setStatus;
  ensurePeerFn = ensurePeer;
  onSignalFn = onSignal;
  onPeerLeftFn = onPeerLeft;
}

export function connectSignaling(room, clientId) {
  roomId = room; myId = clientId;
  try { ws?.close(); } catch {}
  ws = new WebSocket(SIGNAL_URL);
  const t0 = Date.now();

  ws.onopen = () => {
    statusSetter(`ws connected (${SIGNAL_URL})`);
    ws.send(JSON.stringify({ type:'join', roomId, clientId: myId }));
  };
  ws.onerror = (e) => { statusSetter(`ws error (check server/port/firewall)`); console.error('WS error', e); };
  ws.onclose = () => { statusSetter(`ws closed (${Date.now()-t0}ms)`); };
  ws.onmessage = async (ev) => {
    const msg = JSON.parse(ev.data);
    if (msg.type === 'peers') { for (const peerId of msg.peers) ensurePeerFn(peerId, myId > peerId); }
    if (msg.type === 'peer-joined') { ensurePeerFn(msg.clientId, myId > msg.clientId); }
    if (msg.type === 'peer-left') { onPeerLeftFn && onPeerLeftFn(msg.clientId); }
    if (msg.type === 'signal') { await onSignalFn(msg.from, msg.data); }
  };

  return (to, data) => { // sendSignal
    try { ws?.send(JSON.stringify({ type:'signal', roomId, to, from: myId, data })); } catch (e) { console.warn('sendSignal fail', e); }
  };
}
