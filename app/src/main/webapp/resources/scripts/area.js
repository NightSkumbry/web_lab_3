import { AreaCanvas } from "./canvas/areaCanvas.js";


const areaCanvas = new AreaCanvas(600, 600, 65);
const forethestground = areaCanvas.getForethestgroundCanvas();

let animations = []
const animationDuration = 1500;

const shapes = [
    {
        color: "red",
        parts: [
            {
                type: "rectangle",
                params: [
                    0,    0,
                    -0.5, -1
                ]
            }
        ]
    },
    {
        color: "red",
        parts: [
            {
                type: "triangle",
                params: [
                    0,    0,
                    -0.5, 0,
                    0,    1
                ]
            }
        ]
    },
    {
        color: "red",
        startPoint: [0, 0],
        parts: [
            {
                type: "arc",
                params: [
                    0,   0,
                    0.5,
                    0,   Math.PI/2
                ]
            }
        ]
    }
];
const borders = [
    {
        color: "red",
        startPoint: [0, 0],
        closed: true,
        parts: [
            {
                type: "line",
                params: [0.5, 0]
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
                params: [0, 1]
            },
            {
                type: "line",
                params: [-0.5, 0]
            },
            {
                type: "line",
                params: [-0.5, -1]
            },
            {
                type: "line",
                params: [0, -1]
            },
            {
                type: "line",
                params: [0, 0]
            }
        ]
    }
]


forethestground.addEventListener("click", function(event) {
    const rect = forethestground.getBoundingClientRect();

    const clickX = event.clientX - rect.left;
    const clickY = event.clientY - rect.top;
    const dx = clickX - areaCanvas.maxX;
    const dy = areaCanvas.maxY - clickY;

    const x = dx / areaCanvas.gridScale;
    const y = dy / areaCanvas.gridScale;

    document.getElementById('data-form:x-click').value = x;
    document.getElementById('data-form:y-click').value = y;

    document.getElementById('data-form:check-click').click();
});


function navigateAvPoint(x, y) {
    const existingIndex = animations.findIndex(anim => anim.id === -1);

    let xv = x * areaCanvas.gridScale;
    if (xv < -areaCanvas.maxX + areaCanvas.dotRadius/2) {xv = 6 - areaCanvas.maxX}
    else if (xv > areaCanvas.maxX - areaCanvas.dotRadius/2) {xv = areaCanvas.maxX - 6}

    let yv = y * areaCanvas.gridScale;
    if (yv < -areaCanvas.maxY + areaCanvas.dotRadius/2) {yv = 6 - areaCanvas.maxY}
    else if (yv > areaCanvas.maxY - areaCanvas.dotRadius/2) {yv = areaCanvas.maxY - 6}

    const newAnimation = {
        id: -1,
        x: xv,
        y: yv,
        startTime: null
    };

    if (existingIndex !== -1) {
        animations[existingIndex] = newAnimation;
    } else {
        animations.push(newAnimation);
    }

    requestAnimationFrame(navigation_animation);
}


function navigateAllPoint(hit) {

    let resultTable = document.querySelector('#results-table table');

    let x_ind = 1;
    let y_ind = 2;
    let r_ind = 3;
    let nav_ind = 0;
    let hit_ind = 4;
    for (let i = 0; i < resultTable.rows[0].cells.length; i++) {
        if (resultTable.rows[0].cells[i].id == 'results-table:x-column') x_ind = i;
        if (resultTable.rows[0].cells[i].id == 'results-table:y-column') y_ind = i;
        if (resultTable.rows[0].cells[i].id == 'results-table:nav-column') nav_ind = i;
        if (resultTable.rows[0].cells[i].id == 'results-table:r-column') r_ind = i;
        if (resultTable.rows[0].cells[i].id == 'results-table:result-column') hit_ind = i;
    }

    for (let i = 1; i < resultTable.rows.length; i++) {
        const row = resultTable.rows[i];
        if (row.cells[1] && row.cells[hit_ind].innerText === (hit ? "Попадание" : "Промах")) {
            navigatePoint(row.cells[nav_ind].children[0].dataset.id, true);
        }
    }
}


function navigateAlwaysPoint(hit) {
    let resultTable = document.querySelector('#results-table table');

    let x_ind = 1;
    let y_ind = 2;
    let r_ind = 3;
    let nav_ind = 0;
    let hit_ind = 4;
    for (let i = 0; i < resultTable.rows[0].cells.length; i++) {
        if (resultTable.rows[0].cells[i].id == 'results-table:x-column') x_ind = i;
        if (resultTable.rows[0].cells[i].id == 'results-table:y-column') y_ind = i;
        if (resultTable.rows[0].cells[i].id == 'results-table:nav-column') nav_ind = i;
        if (resultTable.rows[0].cells[i].id == 'results-table:r-column') r_ind = i;
        if (resultTable.rows[0].cells[i].id == 'results-table:result-column') hit_ind = i;
    }

    for (let i = 1; i < resultTable.rows.length; i++) {
        const row = resultTable.rows[i];
        
        if (row.cells[1]) {
            let mmr = parseFloat(row.cells[hit_ind].children[0].dataset.maxmissr);
            let r = parseFloat(row.cells[y_ind].innerText);
            if (mmr == (hit ? 0 : 4)) {
                navigatePoint(row.cells[nav_ind].children[0].dataset.id, true);
            }
        }
    }
}


function navigatePoint(pointId, nChangeR) {

    let resultTable = document.querySelector('#results-table table');

    let x_ind = 1;
    let y_ind = 2;
    let r_ind = 3;
    let nav_ind = 0;
    for (let i = 0; i < resultTable.rows[0].cells.length; i++) {
        if (resultTable.rows[0].cells[i].id == 'results-table:x-column') x_ind = i;
        if (resultTable.rows[0].cells[i].id == 'results-table:y-column') y_ind = i;
        if (resultTable.rows[0].cells[i].id == 'results-table:nav-column') nav_ind = i;
        if (resultTable.rows[0].cells[i].id == 'results-table:r-column') r_ind = i;
        
    }

    for (let i = 1; i < resultTable.rows.length; i++) {
        const row = resultTable.rows[i];
        if (row.cells[1] && row.cells[nav_ind].children[0].dataset.id == pointId) {
            const id = row.cells[nav_ind].children[0].dataset.id;
            const existingIndex = animations.findIndex(anim => anim.id === id);

            let xv = parseFloat(row.cells[x_ind].innerText) * areaCanvas.gridScale;
            if (xv < -areaCanvas.maxX + areaCanvas.dotRadius/2) {xv = 6 - areaCanvas.maxX}
            else if (xv > areaCanvas.maxX - areaCanvas.dotRadius/2) {xv = areaCanvas.maxX - 6}

            let yv = parseFloat(row.cells[y_ind].innerText) * areaCanvas.gridScale;
            if (yv < -areaCanvas.maxY + areaCanvas.dotRadius/2) {yv = 6 - areaCanvas.maxY}
            else if (yv > areaCanvas.maxY - areaCanvas.dotRadius/2) {yv = areaCanvas.maxY - 6}

            const newAnimation = {
                id: id,
                x: xv,
                y: yv,
                startTime: null
            };

            if (existingIndex !== -1) {
                animations[existingIndex] = newAnimation;
            } else {
                animations.push(newAnimation);
            }
            
            requestAnimationFrame(navigation_animation);
            if (nChangeR) return;
            const new_r = parseFloat(row.cells[r_ind].innerText);
            PF('r-slider').setValue(new_r);
            document.getElementById('data-form:r-display').innerText = new_r;
            changeR();

        }
    }
}

function navigation_animation(currentTime) {
    let toRemove = []
    let toDraw = []

    for (let i = 0; i < animations.length; i++) {
        let r = animations[i];
        if (!r.startTime) r.startTime = currentTime;

        const elapsed = currentTime - r.startTime;
        if (elapsed > animationDuration) {
            toRemove.push(i);
            continue;
        }
        const k = elapsed / animationDuration;
        
        toDraw.push({x: r.x, y: r.y, k: k});
    }

    areaCanvas.drawForethestground(toDraw);

    toRemove.sort((a, b) => b - a);
    toRemove.forEach(index => {
        if (index >= 0 && index < animations.length) {
            animations.splice(index, 1);
        }
    });

    if (animations.length > 0) requestAnimationFrame(navigation_animation)
}


function showPoints() {
    let resultTable = document.querySelector('#results-table table');

    let x_ind = 1;
    let y_ind = 2;
    let hit_ind = 4;
    let nav_ind = 0;
    for (let i = 0; i < resultTable.rows[0].cells.length; i++) {
        if (resultTable.rows[0].cells[i].id == 'results-table:x-column') x_ind = i;
        if (resultTable.rows[0].cells[i].id == 'results-table:y-column') y_ind = i;
        if (resultTable.rows[0].cells[i].id == 'results-table:result-column') hit_ind = i;
        if (resultTable.rows[0].cells[i].id == 'results-table:nav-column') nav_ind = i;
        
    }

    let dots = {};

    for (let i = 1; i < resultTable.rows.length; i++) {
        const row = resultTable.rows[i];
        
        if (row.cells[1]) {
            const x = parseFloat(row.cells[x_ind].innerText);
            const y = parseFloat(row.cells[y_ind].innerText);
            const maxMissR = parseFloat(row.cells[hit_ind].children[0].dataset.maxmissr);
            const id = parseInt(row.cells[nav_ind].children[0].dataset.id);
            dots[id] = {x: x, y: y, maxMissR: maxMissR};
        }
    }

    let t = document.getElementById("averagePoint").innerText;
    let xy = t.slice(1, t.length-1).split(", ");
    dots[-1] = {x: parseFloat(xy[0]), y: parseFloat(xy[1]), maxMissR: -1}

    areaCanvas.drawForeground(dots);
}



// function showOnePoint(tableRow, x_ind, y_ind, hit_ind, ind) {
//     const x_value = parseFloat(tableRow.cells[x_ind].innerText);
//     const y_value = parseFloat(tableRow.cells[y_ind].innerText);
//     const hit = parseFloat(tableRow.cells[hit_ind].children[0].dataset.maxmissr) < RVal;

//     const x = centerX + (x_value * (RMax / 4));
//     const y = centerY - (y_value * (RMax / 4));

//     const canvas = document.getElementById('foreground');
//     const ctx = canvas.getContext('2d');
//     ctx.imageSmoothingEnabled = true;
//     ctx.fillStyle = hit ? '#53CA61' : '#FFBE33';
//     ctx.strokeStyle = hit ? '#628D3F' : '#FA791F';
//     ctx.strokeWidth = 1;
//     ctx.lineWidth = 1;

//     if (x < 0 + 2 || x > canvas.width - 2 || y < 0 + 2 || y > canvas.height - 2) {
//         if (x > canvas.width - 2 && y > 0 + 2 && y < canvas.height - 2) {
//             ctx.beginPath();
//             ctx.moveTo(canvas.width - 10, y - 5);
//             ctx.lineTo(canvas.width - 10, y + 5);
//             ctx.lineTo(canvas.width, y);
//             ctx.closePath();
//             ctx.fill();
//             ctx.stroke();
//             return;
//         }
//         if (x < 0 + 2 && y > 0 + 2 && y < canvas.height - 2) {
//             ctx.beginPath();
//             ctx.moveTo(10, y - 5);
//             ctx.lineTo(10, y + 5);
//             ctx.lineTo(0, y);
//             ctx.closePath();
//             ctx.fill();
//             ctx.stroke();
//             return;
//         }
//         if (y < 0 + 2 && x > 0 + 2 && x < canvas.width) {
//             ctx.beginPath();
//             ctx.moveTo(x - 5, 10);
//             ctx.lineTo(x + 5, 10);
//             ctx.lineTo(x, 0);
//             ctx.closePath();
//             ctx.fill();
//             ctx.stroke();
//             return;
//         }
//         if (y > canvas.height - 2 && x > 0 + 2 && x < canvas.width) {
//             ctx.beginPath();
//             ctx.moveTo(x - 5, canvas.height - 10);
//             ctx.lineTo(x + 5, canvas.height - 10);
//             ctx.lineTo(x, canvas.height);
//             ctx.closePath();
//             ctx.fill();
//             ctx.stroke();
//             return;
//         }
//         if (x < 0 + 2 && y < 0) {
//             ctx.beginPath();
//             ctx.moveTo(0, 0);
//             ctx.lineTo(10, 5);
//             ctx.lineTo(5, 10);
//             ctx.closePath();
//             ctx.fill();
//             ctx.stroke();
//             return;
//         }
//         if (x < 0 + 2 && y > canvas.height - 2) {
//             ctx.beginPath();
//             ctx.moveTo(0, canvas.height);
//             ctx.lineTo(10, canvas.height - 5);
//             ctx.lineTo(5, canvas.height - 10);
//             ctx.closePath();
//             ctx.fill();
//             ctx.stroke();
//             return;
//         }
//         if (x > canvas.width - 2 && y < 0) {
//             ctx.beginPath();
//             ctx.moveTo(canvas.width, 0);
//             ctx.lineTo(canvas.width - 10, 5);
//             ctx.lineTo(canvas.width - 5, 10);
//             ctx.closePath();
//             ctx.fill();
//             ctx.stroke();
//             return;
//         }
//         if (x > canvas.width - 2 && y > canvas.height - 2) {
//             ctx.beginPath();
//             ctx.moveTo(canvas.width, canvas.height);
//             ctx.lineTo(canvas.width - 10, canvas.height - 5);
//             ctx.lineTo(canvas.width - 5, canvas.height - 10);
//             ctx.closePath();
//             ctx.fill();
//             ctx.stroke();
//             return;
//         }
//     }
//     ctx.beginPath();
//     ctx.arc(x, y, 5, 0, Math.PI * 2);
//     ctx.fill();
//     ctx.stroke();
// }

function changeR() {
    areaCanvas.setR(PF('r-slider').getValue());
    areaCanvas.drawBackground(shapes, borders);
    showPoints();
}


window.onload = function() {
    changeR();
};


window.showPoints = showPoints;
window.changeR = changeR;
window.navigatePoint = navigatePoint;
window.navigateAvPoint = navigateAvPoint;
window.navigateAllPoint = navigateAllPoint;
window.navigateAlwaysPoint = navigateAlwaysPoint;
// window.areaCanvas = areaCanvas;
// window.borders = borders;
// window.shapes = shapes;