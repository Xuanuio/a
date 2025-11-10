// rooms.js — room registry & helpers
// Rooms: Map<roomId, Map<clientId, WebSocket>>
const rooms = new Map();

export function ensureRoom(roomId) {
  if (!rooms.has(roomId)) rooms.set(roomId, new Map());
  return rooms.get(roomId);
}

export function joinRoom(roomId, clientId, ws) {
  const room = ensureRoom(roomId);
  room.set(clientId, ws);
  return room;
}

export function leaveRoom(roomId, clientId) {
  const room = rooms.get(roomId);
  if (!room) return;
  room.delete(clientId);
  if (room.size === 0) rooms.delete(roomId);
}

export function listPeers(roomId, exceptClientId) {
  const room = rooms.get(roomId);
  if (!room) return [];
  return Array.from(room.keys()).filter((id) => id !== exceptClientId);
}

export function getSocket(roomId, clientId) {
  return rooms.get(roomId)?.get(clientId) || null;
}

export function broadcast(roomId, exceptClientId, msg) {
  const room = rooms.get(roomId);
  if (!room) return;
  const raw = JSON.stringify(msg);
  for (const [cid, sock] of room) {
    if (cid === exceptClientId) continue;
    if (sock.readyState === sock.OPEN) {
      try { sock.send(raw); } catch {}
    }
  }
}

export function stats() {
  const detail = Object.fromEntries(Array.from(rooms.entries()).map(([k, v]) => [k, v.size]));
  const peers_total = Array.from(rooms.values()).reduce((a, m) => a + m.size, 0);
  return { rooms: rooms.size, peers_total, detail };
}

export function listRooms() {
  return Array.from(rooms.keys());
}
