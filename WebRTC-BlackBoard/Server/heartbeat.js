// heartbeat.js — ping/pong termination
export function attachHeartbeat(wss, intervalMs = 30000) {
  return setInterval(() => {
    for (const client of wss.clients) {
      if (client.isAlive === false) {
        try { client.terminate(); } catch {}
        continue;
      }
      client.isAlive = false;
      try { client.ping(); } catch {}
    }
  }, intervalMs);
}
