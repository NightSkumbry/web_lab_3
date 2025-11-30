export class Background {
    draw(R) {
        let canvas = document.getElementById('background')
        const ctx = canvas.getContext('2d');
        ctx.imageSmoothingEnabled = true;

        const width = canvas.width;
        const height = canvas.height;
        const centerX = width / 2;
        const centerY = height / 2;
        const RMax = 250;

        ctx.fillStyle = '#DA5869';
        ctx.strokeStyle = '#CB152B';

        ctx.clearRect(0, 0, canvas.width, canvas.height);


        // rectangle
        ctx.beginPath();
        ctx.rect(centerX - R / 2, centerY, R / 2, R);
        ctx.closePath();
        ctx.strokeWidth = 0;
        ctx.fill();

        ctx.beginPath();
        ctx.moveTo(centerX - R / 2, centerY);
        ctx.lineTo(centerX - R / 2, centerY + R);
        ctx.lineTo(centerX, centerY + R);
        ctx.lineWidth = 2.5;
        ctx.stroke();

        // quarter circle
        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.arc(centerX, centerY, R / 2, 3 * Math.PI / 2, 2 * Math.PI, false);
        ctx.lineTo(centerX, centerY);
        ctx.closePath();
        ctx.strokeWidth = 0;
        ctx.fill();

        ctx.beginPath();
        ctx.moveTo(centerX, centerY - R / 2);
        ctx.arc(centerX, centerY, R / 2, 3 * Math.PI / 2, 2 * Math.PI, false);
        ctx.lineWidth = 2.5;
        ctx.stroke();

        // triangle
        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.lineTo(centerX - R / 2, centerY);
        ctx.lineTo(centerX, centerY - R);
        ctx.closePath();
        ctx.strokeWidth = 0;
        ctx.fill();

        ctx.beginPath();
        ctx.moveTo(centerX, centerY - R);
        ctx.lineTo(centerX - R / 2, centerY);
        ctx.lineWidth = 2.5;
        ctx.stroke();


        ctx.lineWidth = 2;

        ctx.beginPath();
        ctx.moveTo(centerX, 0);
        ctx.lineTo(centerX, height);
        ctx.moveTo(0, centerY);
        ctx.lineTo(width, centerY);
        ctx.strokeStyle = "black";
        ctx.moveTo(width, centerY);
        ctx.lineTo(width - 10, centerY - 5);
        ctx.moveTo(width, centerY);
        ctx.lineTo(width - 10, centerY + 5);
        ctx.moveTo(centerX, 0);
        ctx.lineTo(centerX - 5, 10);
        ctx.moveTo(centerX, 0);
        ctx.lineTo(centerX + 5, 10);
        ctx.stroke();

        ctx.font = "12px monospace";
        ctx.lineWidth = 1;
        ctx.textAlign = 'center';
        ctx.textBaseline = 'top';

        ctx.beginPath();
        ctx.moveTo(centerX - RMax/4, centerY - 4); ctx.lineTo(centerX - RMax/4, centerY + 4);
        ctx.stroke();
        ctx.strokeText("-1", centerX - RMax/4, centerY + 6);

        ctx.beginPath();
        ctx.moveTo(centerX + RMax/4, centerY - 4); ctx.lineTo(centerX + RMax/4, centerY + 4);
        ctx.stroke();
        ctx.strokeText("1", centerX + RMax/4, centerY + 6);

        ctx.beginPath();
        ctx.moveTo(centerX - 2*RMax/4, centerY - 4); ctx.lineTo(centerX - 2*RMax/4, centerY + 4);
        ctx.stroke();
        ctx.strokeText("-2", centerX - 2*RMax/4, centerY + 6);

        ctx.beginPath();
        ctx.moveTo(centerX + 2*RMax/4, centerY - 4); ctx.lineTo(centerX + 2*RMax/4, centerY + 4);
        ctx.stroke();
        ctx.strokeText("2", centerX + 2*RMax/4, centerY + 6);

        ctx.beginPath();
        ctx.moveTo(centerX - 3*RMax/4, centerY - 4); ctx.lineTo(centerX - 3*RMax/4, centerY + 4);
        ctx.stroke();
        ctx.strokeText("-3", centerX - 3*RMax/4, centerY + 6);

        ctx.beginPath();
        ctx.moveTo(centerX + 3*RMax/4, centerY - 4); ctx.lineTo(centerX + 3*RMax/4, centerY + 4);
        ctx.stroke();
        ctx.strokeText("3", centerX + 3*RMax/4, centerY + 6);

        ctx.beginPath();
        ctx.moveTo(centerX - RMax, centerY - 4); ctx.lineTo(centerX - RMax, centerY + 4);
        ctx.stroke();
        ctx.strokeText("-4", centerX - RMax, centerY + 6);

        ctx.beginPath();
        ctx.moveTo(centerX + RMax, centerY - 4); ctx.lineTo(centerX + RMax, centerY + 4);
        ctx.stroke();
        ctx.strokeText("4", centerX + RMax, centerY + 6);

        ctx.textAlign = 'left';
        ctx.textBaseline = 'middle';

        ctx.beginPath();
        ctx.moveTo(centerX - 4, centerY - RMax/4); ctx.lineTo(centerX + 4, centerY - RMax/4);
        ctx.stroke();
        ctx.strokeText("1", centerX + 7, centerY - RMax/4);

        ctx.beginPath();
        ctx.moveTo(centerX - 4, centerY + RMax/4); ctx.lineTo(centerX + 4, centerY + RMax/4);
        ctx.stroke();
        ctx.strokeText("-1", centerX + 7, centerY + RMax/4);

        ctx.beginPath();
        ctx.moveTo(centerX - 4, centerY - 2*RMax/4); ctx.lineTo(centerX + 4, centerY - 2*RMax/4);
        ctx.stroke();
        ctx.strokeText("2", centerX + 7, centerY - 2*RMax/4);

        ctx.beginPath();
        ctx.moveTo(centerX - 4, centerY + 2*RMax/4); ctx.lineTo(centerX + 4, centerY + 2*RMax/4);
        ctx.stroke();
        ctx.strokeText("-2", centerX + 7, centerY + 2*RMax/4);

        ctx.beginPath();
        ctx.moveTo(centerX - 4, centerY - 3*RMax/4); ctx.lineTo(centerX + 4, centerY - 3*RMax/4);
        ctx.stroke();
        ctx.strokeText("3", centerX + 7, centerY - 3*RMax/4);

        ctx.beginPath();
        ctx.moveTo(centerX - 4, centerY + 3*RMax/4); ctx.lineTo(centerX + 4, centerY + 3*RMax/4);
        ctx.stroke();
        ctx.strokeText("-3", centerX + 7, centerY + 3*RMax/4);

        ctx.beginPath();
        ctx.moveTo(centerX - 4, centerY - RMax); ctx.lineTo(centerX + 4, centerY - RMax);
        ctx.stroke();
        ctx.strokeText("4", centerX + 7, centerY - RMax);

        ctx.beginPath();
        ctx.moveTo(centerX - 4, centerY + RMax); ctx.lineTo(centerX + 4, centerY + RMax);
        ctx.stroke();
        ctx.strokeText("-4", centerX + 7, centerY + RMax);
    }
    
}