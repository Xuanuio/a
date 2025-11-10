// canvas.js — Whiteboard drawing + CRDT (strokes/tombstones)
const ERASER_RADIUS = 10;
const strokes = new Map(); // id -> stroke
const tombstones = new Set();
let localCounter = 0;
let canvas, ctx;
let tool = 'pen';
let currentStroke = null;
let eraserPath = [];

export function initCanvas(canvasEl) {
  canvas = canvasEl;
  ctx = canvas.getContext('2d');
  const resize = () => {
    const dpr = window.devicePixelRatio || 1;
    canvas.width = Math.floor(canvas.clientWidth * dpr);
    canvas.height = Math.floor(canvas.clientHeight * dpr);
    ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
    redraw();
  };
  window.addEventListener('resize', resize);
  resize();
}

export function setTool(next) { tool = next; }
export function getTool() { return tool; }
export function getEraserRadius(){ return ERASER_RADIUS; }

export function redraw() {
  if (!ctx) return;
  ctx.clearRect(0, 0, canvas.width, canvas.height);
  for (const s of strokes.values()) drawStroke(ctx, s);
}

export function drawStroke(context, s) {
  if (!s?.points?.length) return;
  context.strokeStyle = s.color;
  context.lineWidth = s.width;
  context.lineJoin = 'round';
  context.lineCap = 'round';
  context.beginPath();
  context.moveTo(s.points[0].x, s.points[0].y);
  for (let i = 1; i < s.points.length; i++) context.lineTo(s.points[i].x, s.points[i].y);
  context.stroke();
}

export function addStrokeCRDT(s) {
  if (tombstones.has(s.id)) return;
  if (!strokes.has(s.id)) strokes.set(s.id, s);
  else if ((s.points?.length||0) > (strokes.get(s.id).points?.length||0)) strokes.set(s.id, s);
}

export function delStrokeCRDT(id) {
  tombstones.add(id);
  strokes.delete(id);
}

export function strokesHitByPath(pathPoints, radius = ERASER_RADIUS) {
  if (!pathPoints?.length) return [];
  const r2 = radius * radius;
  const hitIds = new Set();
  for (const s of strokes.values()) {
    if (!s.points?.length) continue;
    // quick bbox check
    let minx=Infinity,miny=Infinity,maxx=-Infinity,maxy=-Infinity;
    for (const p of s.points) { if (p.x<minx) minx=p.x; if (p.y<miny) miny=p.y; if (p.x>maxx) maxx=p.x; if (p.y>maxy) maxy=p.y; }
    let pminx=Infinity,pminy=Infinity,pmaxx=-Infinity,pmaxy=-Infinity;
    for (const q of pathPoints) { if (q.x<pminx) pminx=q.x; if (q.y<pminy) pminy=q.y; if (q.x>pmaxx) pmaxx=q.x; if (q.y>pmaxy) pmaxy=q.y; }
    pminx -= radius; pminy -= radius; pmaxx += radius; pmaxy += radius;
    if (maxx < pminx || maxy < pminy || minx > pmaxx || miny > pmaxy) continue;
    // detailed proximity
    let hit = false;
    outer: for (const q of pathPoints) {
      for (const p of s.points) {
        const dx = p.x - q.x, dy = p.y - q.y;
        if (dx*dx + dy*dy <= r2) { hit = true; break outer; }
      }
    }
    if (hit) hitIds.add(s.id);
  }
  return Array.from(hitIds);
}

export function drawEraserDot(x, y) {
  if (!ctx) return;
  ctx.save();
  ctx.beginPath();
  ctx.arc(x, y, ERASER_RADIUS, 0, Math.PI * 2);
  ctx.strokeStyle = '#c9d1d980';
  ctx.lineWidth = 1.5;
  ctx.setLineDash([4, 3]);
  ctx.stroke();
  ctx.restore();
}

export function beginStroke({ myId, color, width, x, y }) {
  currentStroke = {
    id: `${myId}:${++localCounter}`,
    color, width, points: [{ x, y }], author: myId,
  };
  addStrokeCRDT(currentStroke);
  redraw();
  return currentStroke;
}
export function extendStroke({ x, y }) {
  if (!currentStroke) return;
  currentStroke.points.push({ x, y });
  drawStroke(ctx, currentStroke);
}
export function endStroke() {
  const s = currentStroke;
  currentStroke = null;
  redraw();
  return s;
}

export function beginErasePath(x, y) {
  eraserPath = [{ x, y }];
  drawEraserDot(x, y);
}
export function extendErasePath(x, y) {
  eraserPath.push({ x, y });
  drawEraserDot(x, y);
}
export function endErasePath() {
  const path = eraserPath;
  eraserPath = [];
  return path;
}

export function exportFullState() {
  return { strokes: Array.from(strokes.values()), tombstones: Array.from(tombstones) };
}
