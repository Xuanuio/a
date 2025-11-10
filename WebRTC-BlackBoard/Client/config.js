// config.js — Networking & feature limits (patched)
export const WS_HOST = (location.hostname && location.hostname !== '' && location.hostname !== '0.0.0.0')
  ? location.hostname
  : '127.0.0.1';

export const SIGNAL_URL = `${location.protocol === 'https:' ? 'wss' : 'ws'}://${WS_HOST}:8090`;

export const ICE_SERVERS = [{ urls: 'stun:stun.l.google.com:19302' }];
export const MAX_PEERS_PER_ROOM = 6;
