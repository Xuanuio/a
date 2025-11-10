// ui.js — DOM wiring, toolbar/chat/call, pointer events
import { getTool, setTool, initCanvas, beginStroke, extendStroke, endStroke, beginErasePath, extendErasePath, endErasePath, strokesHitByPath, redraw, getEraserRadius } from './canvas.js';
import { broadcastOp, broadcastChat, startCall, toggleMute, hangupCall } from './rtc.js';

let dom = {};
let myId = null;

export function setupUI({ onJoin }) {
  dom.color = document.getElementById('color');
  dom.width = document.getElementById('width');
  dom.rtt = document.getElementById('rtt');
  dom.status = document.getElementById('status');
  dom.penBtn = document.getElementById('penBtn');
  dom.eraserBtn = document.getElementById('eraserBtn');

  dom.chatInput = document.getElementById('chatInput');
  dom.chatSend = document.getElementById('chatSend');
  dom.chatLog = document.getElementById('chatLog');

  dom.callStart = document.getElementById('callStart');
  dom.callMute = document.getElementById('callMute');
  dom.callHang = document.getElementById('callHangup');
  dom.peersAudio = document.getElementById('peersAudio');

  dom.room = document.getElementById('room');
  dom.name = document.getElementById('name');
  dom.joinBtn = document.getElementById('joinBtn');

  const canvas = document.getElementById('board');
  initCanvas(canvas);

  // tools
  dom.penBtn.classList.add('active');
  dom.penBtn.onclick = () => { setTool('pen'); dom.penBtn.classList.add('active'); dom.eraserBtn.classList.remove('active'); };
  dom.eraserBtn.onclick = () => { setTool('eraser'); dom.eraserBtn.classList.add('active'); dom.penBtn.classList.remove('active'); };

  dom.joinBtn.onclick = () => {
    const room = dom.room.value.trim();
    const name = dom.name.value.trim();
    if (!room || !name) return alert('Nhập room & name');
    myId = `${name}-${Math.random().toString(36).slice(2,8)}`;
    onJoin({ roomId: room, displayName: name, myId });
  };

  // chat
  dom.chatSend.onclick = () => {
    const txt = (dom.chatInput.value || '').trim();
    if (!txt) return;
    appendChat('me', txt);
    broadcastChat(myId, txt);
    dom.chatInput.value = '';
  };
  dom.chatInput.addEventListener('keydown', (e) => { if (e.key === 'Enter') dom.chatSend.click(); });
  window.addEventListener('chat:incoming', (ev) => {
    const m = ev.detail;
    appendChat(m.from || 'peer', m.text);
  });

  // call
  dom.callMute.disabled = true;
  dom.callHang.disabled = true;
  dom.callStart.onclick = async () => {
    await startCall();
    dom.callStart.disabled = true;
    dom.callMute.disabled = false;
    dom.callHang.disabled = false;
  };
  dom.callMute.onclick = () => {
    toggleMute();
    dom.callMute.textContent = dom.callMute.textContent === 'Tắt tiếng' ? 'Bật tiếng' : 'Tắt tiếng';
  };
  dom.callHang.onclick = async () => {
    await hangupCall();
    dom.callStart.disabled = false;
    dom.callMute.disabled = true;
    dom.callHang.disabled = true;
  };

  // drawing interactions
  let drawing = false;
  canvas.addEventListener('pointerdown', (e) => {
    if (!myId) { alert('Join room trước đã'); return; }
    drawing = true;
    if (getTool() === 'eraser') {
      beginErasePath(e.offsetX, e.offsetY);
      return;
    }
    beginStroke({ myId, color: dom.color.value, width: Number(dom.width.value), x: e.offsetX, y: e.offsetY });
  });
  canvas.addEventListener('pointermove', (e) => {
    if (!drawing) return;
    if (getTool() === 'eraser') {
      extendErasePath(e.offsetX, e.offsetY);
      return;
    }
    extendStroke({ x: e.offsetX, y: e.offsetY });
  });
  window.addEventListener('pointerup', () => {
    if (!drawing) return; drawing = false;
    if (getTool() === 'eraser') {
      const hits = strokesHitByPath(endErasePath(), getEraserRadius());
      if (hits.length) {
        for (const id of hits) {
          broadcastOp({ kind: 'op', opType: 'stroke:del', payload: { id }, ts: Date.now() });
        }
        redraw();
      }
      return;
    }
    const s = endStroke();
    if (s) broadcastOp({ kind:'op', opType:'stroke:add', payload: s, ts: Date.now() });
  });

  // expose a few helpers
  return {
    setStatus: (txt) => { dom.status.textContent = txt; console.log('[status]', txt); },
    setRTT: (v) => { dom.rtt.textContent = v; },
    peersAudioEl: dom.peersAudio,
    appendChat,
    myIdGetter: () => myId,
  };
}

function appendChat(who, text) {
  const li = document.createElement('li');
  li.textContent = `[${who}] ${text}`;
  dom.chatLog.appendChild(li);
  dom.chatLog.scrollTop = dom.chatLog.scrollHeight;
}
