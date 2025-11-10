// index.js — compose http + ws + heartbeat
import { createHttpServer } from './httpServer.js';
import { attachWebSocket } from './wsServer.js';
import { attachHeartbeat } from './heartbeat.js';
import { PORT, HOST, HEARTBEAT_MS } from './config.js';
import { log } from './logger.js';

const server = createHttpServer();
const wss = attachWebSocket(server);
attachHeartbeat(wss, HEARTBEAT_MS);

server.listen(PORT, HOST, () => log(`[signaling] http/ws on ${HOST}:${PORT}`));
