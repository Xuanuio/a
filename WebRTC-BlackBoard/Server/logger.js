// logger.js — small timestamped logger
export const now = () => new Date().toISOString();
export const log = (...args) => console.log(`[${now()}]`, ...args);
export const warn = (...args) => console.warn(`[${now()}]`, ...args);
export const error = (...args) => console.error(`[${now()}]`, ...args);
