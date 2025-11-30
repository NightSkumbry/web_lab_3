
let canvas = document.getElementById('background');
const width = canvas.width;
const height = canvas.height;
const centerX = width / 2;
const centerY = height / 2;
const clockRadius = 240;
const clockThickness = 5;
const clockRadiusSmall = clockRadius - clockThickness;
const majorMarkLength = clockRadius / 7;
const minorMarkLength = majorMarkLength / 3;
const textOffset = 10;
const textOffsetY = 4;
const hourLength = clockRadius / 7 * 4;
const hourWidth = 9;
const minuteLength = clockRadius / 14 * 11;
const minuteWidth = 6;
const secondLength = clockRadius / 7 * 6;
const secondWidth = 4;


function drawRotatedArrow(ctx, topLeftX, topLeftY, pivotX, pivotY, w, h, angle) {
    const corners = [
        { x: topLeftX, y: topLeftY },                           // левый верхний
        { x: topLeftX + w, y: topLeftY },                       // правый верхний
        { x: topLeftX + w, y: topLeftY + h },                   // правый нижний
        { x: topLeftX, y: topLeftY + h }                        // левый нижний
    ];

    const rotatedCorners = corners.map(corner => {
        const xRel = corner.x - pivotX;
        const yRel = corner.y - pivotY;

        const xRot = xRel * Math.cos(angle) - yRel * Math.sin(angle);
        const yRot = xRel * Math.sin(angle) + yRel * Math.cos(angle);
        return {
            x: xRot + pivotX,
            y: yRot + pivotY
        };
    });

    ctx.beginPath();
    ctx.moveTo(rotatedCorners[0].x, rotatedCorners[0].y);
    for (let i = 1; i < rotatedCorners.length; i++) {
        ctx.lineTo(rotatedCorners[i].x, rotatedCorners[i].y);
    }
    ctx.closePath();
}


function draw_clock(hour, minute, second, day, month, year) {
    const canvas = document.getElementById('background');
    const ctx = canvas.getContext('2d');
    ctx.imageSmoothingEnabled = true;

    ctx.fillStyle = '#1BD5EE';
    ctx.strokeStyle = '#1BD5EE';
    // ctx.strokeStyle = '#0096b8';
    ctx.strokeWidth = 1;
    ctx.lineWidth = 1;

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // draw clock oval
    ctx.beginPath();
    ctx.arc(centerX, centerY, clockRadius, 0, 2*Math.PI, false);
    ctx.lineTo(
        centerX + clockRadiusSmall,
        centerY + 0
    );
    ctx.arc(centerX, centerY, clockRadiusSmall, 2*Math.PI, 0, true);
    ctx.closePath();
    ctx.fill();
    ctx.stroke();

    ctx.strokeStyle = '#0096b8';
    ctx.beginPath();
    ctx.arc(centerX, centerY, clockRadiusSmall, 2*Math.PI, 0, true);
    ctx.stroke();
    ctx.beginPath();
    ctx.arc(centerX, centerY, clockRadius, 0, 2*Math.PI, false);
    ctx.stroke();

    ctx.beginPath();
    ctx.fillStyle = '#FFD270';
    ctx.arc(centerX, centerY, clockRadiusSmall, 2*Math.PI, 0, true);
    ctx.closePath();
    ctx.fill();


    // draw marks
    ctx.strokeStyle = "black";
    ctx.font = "12px monospace";
    ctx.textAlign = 'center';
    const phis = [
        270, 300, 330,
        0, 30, 60,
        90, 120, 150,
        180, 210, 240
    ];
    for (let i = 0; i < phis.length; i++) {
        let phi = phis[i];
        let markLength = minorMarkLength;
        if (phi%90 == 0) {
            markLength = majorMarkLength;
        }
        let k = phi/360 * 2*Math.PI;
        let num = i == 0 ? 12 : i;

        ctx.beginPath();
        ctx.lineWidth = 2;
        ctx.moveTo(centerX + clockRadiusSmall * Math.cos(k), centerY + clockRadiusSmall * Math.sin(k));
        ctx.lineTo(centerX + (clockRadiusSmall - markLength) * Math.cos(k), centerY + (clockRadiusSmall - markLength) * Math.sin(k));
        ctx.stroke();
        ctx.lineWidth = 1;
        ctx.strokeText(num, centerX + (clockRadiusSmall - markLength - textOffset) * Math.cos(k), centerY + textOffsetY + (clockRadiusSmall - markLength - textOffset) * Math.sin(k));

    }


    // draw texts
    ctx.font = "25px supercell-magic";
    ctx.lineWidth = 1.5;
    ctx.fillStyle = '#AAAAAA';
    ctx.strokeStyle = '#333333';
    ctx.fillText(`${day} ${month}`, centerX, centerY + textOffsetY*2 - (clockRadiusSmall/2))
    ctx.strokeText(`${day} ${month}`, centerX, centerY + textOffsetY*2 - (clockRadiusSmall/2));
    ctx.fillText(`${year} г.`, centerX, centerY + textOffsetY*2 + (clockRadiusSmall/2))
    ctx.strokeText(`${year} г.`, centerX, centerY + textOffsetY*2 + (clockRadiusSmall/2));
    ctx.textAlign = 'right';
    ctx.fillText(`Night`, centerX - (clockRadiusSmall/26*3), centerY + textOffsetY*2)
    ctx.strokeText(`Night`, centerX - (clockRadiusSmall/26*3), centerY + textOffsetY*2);
    ctx.textAlign = 'left';
    ctx.fillText(`Skumbry`, centerX + (clockRadiusSmall/26*3), centerY + textOffsetY*2)
    ctx.strokeText(`Skumbry`, centerX + (clockRadiusSmall/26*3), centerY + textOffsetY*2);
    

    // draw arrows
    const hour_num = parseInt(hour);
    const minute_num = parseInt(minute);
    const second_num = parseInt(second);
    const hour_phi = ((hour_num%12)*60*60 + minute_num*60 + second_num)/12/60/60 * 2*Math.PI - Math.PI/2;
    const minute_phi = (minute_num*60 + second_num)/60/60 * 2*Math.PI - Math.PI/2;
    const second_phi = (second_num)/60 * 2*Math.PI - Math.PI/2;
    // console.log(((hour_num%12)*60*60 + minute_num*60 + second_num)/12/60/60);
    // console.log(hour_phi, minute_phi, second_phi)

    ctx.fillStyle = '#DA5869';
    ctx.strokeStyle = '#CB152B';
    drawRotatedArrow(ctx, centerX, centerY - hourWidth/2, centerX, centerY, hourLength, hourWidth, hour_phi);
    ctx.fill();
    ctx.stroke();

    ctx.fillStyle = '#8D74B8';
    ctx.strokeStyle = '#684C94';
    drawRotatedArrow(ctx, centerX, centerY - minuteWidth/2, centerX, centerY, minuteLength, minuteWidth, minute_phi);
    ctx.fill();
    ctx.stroke();

    ctx.fillStyle = '#53CA61';
    ctx.strokeStyle = '#628D3F';
    drawRotatedArrow(ctx, centerX, centerY - secondWidth/2, centerX, centerY, secondLength, secondWidth, second_phi);
    ctx.fill();
    ctx.stroke();
    

    // draw clock center
    ctx.strokeStyle = '#0096b8';
    ctx.fillStyle = '#1BD5EE';
    ctx.lineWidth = 1;
    ctx.beginPath();
    ctx.arc(centerX, centerY, 7, 0, Math.PI * 2);
    ctx.fill();
    ctx.stroke();
}


function updateLocalTime() {
    const serverTimeISO = document.getElementById('serverTime').value;
    if (serverTimeISO) {
        const serverTime = new Date(serverTimeISO);
        
        const options = {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
            weekday: 'short',
            hour12: false
        };

        const userLocalTime = serverTime.toLocaleString('ru-RU', options);
        document.getElementById('localTimeDisplay').textContent = userLocalTime;

        const year = serverTime.toLocaleDateString('ru-RU', { year: 'numeric' });

        const month = serverTime.toLocaleDateString('ru-RU', { month: 'short' });

        const day = serverTime.toLocaleDateString('ru-RU', { day: 'numeric' });

        const weekday = serverTime.toLocaleDateString('ru-RU', { weekday: 'short' });

        const hour = serverTime.toLocaleTimeString('ru-RU', { hour: '2-digit', hour12: false }).substring(0, 2);
        const minute = serverTime.toLocaleTimeString('ru-RU', { minute: '2-digit' }).substring(0, 2);
        const second = serverTime.toLocaleTimeString('ru-RU', { second: '2-digit' }).substring(0, 2);

        // console.log({
        //     year: year,
        //     month: month,
        //     day: day,
        //     weekday: weekday,
        //     hour: hour,
        //     minute: minute,
        //     second: second
        // });
        draw_clock(hour, minute, second, day, month, year);
    }



}

window.onload = async function() {
    try {
        await document.fonts.load(`20px supercell-magic`);

        updateLocalTime();
        document.getElementById('localTimeDisplay').setAttribute("style", "display:none;");

    } catch (e) {
        console.error("Ошибка загрузки шрифта:", e);

        console.warn("Шрифт не загрузился, вызываем updateLocalTime() с fallback шрифтом");
        updateLocalTime();
        document.getElementById('localTimeDisplay').setAttribute("style", "display:none;");
    }
};
