// wsServer.js — WebSocket signaling
import { WebSocketServer } from 'ws';
import { v4 as uuidv4 } from 'uuid';
import { log } from './logger.js';
import { joinRoom, leaveRoom, listPeers, getSocket, broadcast } from './rooms.js';

function send(ws, msg) {
  if (ws.readyState === ws.OPEN) {
    try { ws.send(JSON.stringify(msg)); } catch {}
  }
}

export function attachWebSocket(server) {
  const wss = new WebSocketServer({ server });

  wss.on('connection', (ws, req) => {
    ws.id = uuidv4();
    ws.isAlive = true;
    const ip = req.socket.remoteAddress;
    log(`[+] conn ${ws.id} from ${ip}`);

    ws.on('pong', () => (ws.isAlive = true));

    ws.on('message', (raw) => {
      let msg;
      try {
        msg = JSON.parse(raw.toString());
      } catch {
        return;
      }
      const { type } = msg || {};

      if (type === 'join') {
        const { roomId, clientId } = msg;
        if (!roomId || !clientId) return;
        ws.roomId = roomId;
        ws.clientId = clientId;

        joinRoom(roomId, clientId, ws);
        log(`[join] room=${roomId} client=${clientId}`);

        send(ws, { type: 'peers', peers: listPeers(roomId, clientId) });
        broadcast(roomId, clientId, { type: 'peer-joined', clientId });
        return;
      }

      if (type === 'signal') {
        const { roomId, to, from, data } = msg;
        const target = getSocket(roomId, to);
        if (!target) return;
        send(target, { type: 'signal', from, data });
        return;
      }
    });

    ws.on('close', () => {
      const { roomId, clientId } = ws;
      log(`[-] close ${ws.id} (${clientId || 'n/a'})`);
      if (!roomId || !clientId) return;
      leaveRoom(roomId, clientId);
      if (roomId) broadcast(roomId, clientId, { type: 'peer-left', clientId });
    });
  });

  return wss;
}
