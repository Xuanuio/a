// config.js — ports & timings
export const PORT = process.env.PORT || 8090;
export const HOST = process.env.HOST || '0.0.0.0';
export const HEARTBEAT_MS = Number(process.env.HEARTBEAT_MS || 30000);
