import { StatsCanvas } from "./canvas/StatsCanvas.js";


const statsCanvas = new StatsCanvas(600, 600, 65);


const parts = [
            {
                type: "line",
                params: [0.5, 0],
                start: [0, 0]
            },
            {
                type: "arc",
                params: [
                    0,   0,
                    0.5,
                    0,   Math.PI/2
                ]
            },
            {
                type: "line",
                params: [0, 1],
                start: [0, 0.5]
            },
            {
                type: "line",
                params: [-0.5, 0],
                start: [0, 1]
            },
            {
                type: "line",
                params: [-0.5, -1],
                start: [-0.5, 0]
            },
            {
                type: "line",
                params: [0, -1],
                start: [-0.5, -1]
            },
            {
                type: "line",
                params: [0, 0],
                start: [0, -1],
            }
        ];


const animationDuration = 2000;

let radius = null;
let st1 = -1;

let r = null;
let st2 = -1;

function showRadius(rad) {
    radius = rad;
    st1 = null;

    requestAnimationFrame(animation);
}

function showR(rad) {
    r = rad;
    st2 = null;

    requestAnimationFrame(animation);
}

function animation(currentTime) {
    if (st1 == null) st1 = currentTime;
    if (st2 == null) st2 = currentTime;

    let k1, k2;

    if (st1 != -1) {
        const elapsed1 = currentTime - st1;
        if (elapsed1 > animationDuration) {
            st1 = -1;
        } else {
            k1 = elapsed1 / animationDuration;
        }
    }
    if (st1 == -1) k1 = null;

    if (st2 != -1) {
        const elapsed2 = currentTime - st2;
        if (elapsed2 > animationDuration) {
            st2 = -1;
        } else {
            k2 = elapsed2 / animationDuration;
        }
    }
    if (st2 == -1) k2= null;

    statsCanvas.drawAF(radius, k1, r, k2, parts);

    if (k1 != null || k2 != null) requestAnimationFrame(animation);
}


window.showRadius = showRadius;
window.showR = showR;
window.statsCanvas = statsCanvas;