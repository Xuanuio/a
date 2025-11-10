// app.js — Entry point (patched order to avoid race)
import { setupUI } from './ui.js';
import { initSignaling, connectSignaling } from './signaling.js';
import { initRTC, ensurePeer, onSignal, removePeer } from './rtc.js';
import { SIGNAL_URL } from './config.js';

const ui = setupUI({
  onJoin: ({ roomId, displayName, myId }) => {
    // Initialize signaling callbacks BEFORE opening WebSocket to avoid race
    initSignaling({
      setStatus: ui.setStatus,
      ensurePeer: (peerId, initiator) => ensurePeer(peerId, initiator),
      onSignal: (from, data) => onSignal(from, data),
      onPeerLeft: (peerId) => removePeer(peerId),
    });

    // Connect WS signaling, get sendSignal, then init RTC
    const sendSignal = connectSignaling(roomId, myId);
    initRTC({
      sendSignal,
      peersAudioEl: ui.peersAudioEl,
      setRTT: ui.setRTT,
      setStatus: ui.setStatus,
    });

    ui.setStatus(`ready – signaling ${SIGNAL_URL}`);
    window.__APP__ = { roomId, displayName, myId };
  }
});
