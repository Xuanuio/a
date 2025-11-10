// httpServer.js — small HTTP endpoints (health/metrics/rooms)
import http from 'http';
import { stats, listRooms } from './rooms.js';
import { now } from './logger.js';

export function createHttpServer() {
  return http.createServer((req, res) => {
    if (req.url === '/health') {
      res.writeHead(200, { 'Content-Type': 'text/plain' });
      res.end('ok');
      return;
    }
    if (req.url === '/metrics') {
      const s = stats();
      const payload = { ts: now(), ...s };
      res.writeHead(200, { 'Content-Type': 'application/json' });
      res.end(JSON.stringify(payload));
      return;
    }
    if (req.url === '/rooms') {
      res.writeHead(200, { 'Content-Type': 'application/json' });
      res.end(JSON.stringify(listRooms()));
      return;
    }
    res.writeHead(200, { 'Content-Type': 'text/plain' });
    res.end('whiteboard-signaling');
  });
}
