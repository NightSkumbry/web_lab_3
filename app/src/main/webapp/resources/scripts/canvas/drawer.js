
export class Drawer {
    #ctx;
    #sizeX;
    #sizeY;
    #coordinateScale;

    constructor (ctx, centerX, centerY) {
        if (ctx === null || ctx === undefined) {
            throw new Error("Canvas context (ctx) cannot be null or undefined");
        }
        ctx.imageSmoothingEnabled = true;

        this.#ctx = ctx;
        this.#sizeX = centerX;
        this.#sizeY = centerY;
        this.#coordinateScale = 1;

        this.setStyle("black", "black", 1);
        this.setTextStyle("12px monospace", "center", "middle");
    }

    get ctx() {return this.#ctx}

    setCoordinateScale(scale) {
        this.#coordinateScale = scale;
    }

    clear() {
        this.#ctx.clearRect(-this.#sizeX, -this.#sizeY, 2*this.#sizeX, 2*this.#sizeY);
    }


    beginPath(x=null, y=null) {
        
        this.#ctx.beginPath();
        if (x !== null && x !== undefined && y !== null && y !== undefined) {
            x *= this.#coordinateScale;
            y *= this.#coordinateScale;
            this.#ctx.moveTo(x, y);
        }
    }

    setStyle(fillStyle, lineStyle, lineWidth) {
        this.setFillStyle(fillStyle);
        this.setStrokeStyle(lineStyle);
        this.setLineWidth(lineWidth);
    }

    setFillStyle(fillStyle) {
        this.#ctx.fillStyle = fillStyle;
    }

    setStrokeStyle(strokeStyle) {
        this.#ctx.strokeStyle = strokeStyle;
    }

    setLineWidth(lineWidth) {
        this.#ctx.lineWidth = lineWidth;
    }

    draw (fill=false, stroke=false) {
        if (fill) this.#ctx.fill();
        if (stroke) this.#ctx.stroke();
    }

    closePath() {
        this.#ctx.closePath();
    }


    setFont(font) {
        this.#ctx.font = font;
    }

    setTextAlign(textAlign) {
        this.#ctx.textAlign = textAlign;
    }

    setTextBaseline(textBaseline) {
        this.#ctx.textBaseline = textBaseline;
    }

    setDirection(direction) {
        this.#ctx.direction = direction;
    }

    setTextStyle(font, textAlign, textBaseline, direction = undefined) {
        this.setFont(font);
        this.setTextAlign(textAlign);
        this.setTextBaseline(textBaseline);
        if (direction !== undefined && direction !== null) {
            this.setDirection(direction);
        }
    }

    setTextPosition(textAlign, textBaseline) {
        this.setTextAlign(textAlign);
        this.setTextBaseline(textBaseline);
    }

    transform(angle, x, y) {
        x *= this.#coordinateScale;
        y *= this.#coordinateScale;

        this.#ctx.save();

        this.#ctx.transform(Math.cos(angle), Math.sin(angle), -Math.sin(angle), Math.cos(angle), x, y);
    }

    restore() {
        this.#ctx.restore();
    }
    
    /*
    P - point as offset from center of rotation [x, y]
    A - angle [a]
    R - radius [r]
    C - center of rotation [x, y] (0, 0 by default)
    S - size [width, height]
    T - text [t]
    F - fill [fill, stroke]
    */

    //texts
    drawText_TPFCA(text, x, y, fill=false, stroke=false, xc=0, yc=0, angle=0) {
        x *= this.#coordinateScale;
        y *= -this.#coordinateScale;

        this.transform(angle, xc, yc);
        this.#ctx.transform(1, 0, 0, -1, 0, 0);

        if (fill) this.#ctx.fillText(text, x, y);
        if (stroke) this.#ctx.strokeText(text, x, y);

        this.restore();
        
    }

    // lines
    drawLine_2PCA(x1, y1, x2, y2, xc=0, yc=0, angle=0) {
        x1 *= this.#coordinateScale;
        x2 *= this.#coordinateScale;
        y1 *= this.#coordinateScale;
        y2 *= this.#coordinateScale;

        this.transform(angle, xc, yc);

        this.#ctx.moveTo(x1, y1);
        this.#ctx.lineTo(x2, y2);

        this.restore();
    }

    drawLine_PCA(x, y, xc=0, yc=0, angle=0) {
        x *= this.#coordinateScale;
        y *= this.#coordinateScale;

        this.transform(angle, xc, yc);

        this.#ctx.lineTo(x, y);

        this.restore();
    }
    

    // triangles
    drawTriangle_3PCA(x1, y1, x2, y2, x3, y3, xc=0, yc=0, angle=0) {
        x1 *= this.#coordinateScale;
        x2 *= this.#coordinateScale;
        x3 *= this.#coordinateScale;
        y1 *= this.#coordinateScale;
        y2 *= this.#coordinateScale;
        y3 *= this.#coordinateScale;

        this.transform(angle, xc, yc);

        this.beginPath(x1, y1);
        this.#ctx.lineTo(x2, y2);
        this.#ctx.lineTo(x3, y3);
        this.closePath();

        this.restore();
    }

    // quadrangles
    drawQuadrangle_4PCA(x1, y1, x2, y2, x3, y3, x4, y4, xc=0, yc=0, angle=0) {
        x1 *= this.#coordinateScale;
        x2 *= this.#coordinateScale;
        x3 *= this.#coordinateScale;
        x4 *= this.#coordinateScale;
        y1 *= this.#coordinateScale;
        y2 *= this.#coordinateScale;
        y3 *= this.#coordinateScale;
        y4 *= this.#coordinateScale;

        this.transform(angle, xc, yc);

        this.beginPath(x1, y1);
        this.#ctx.lineTo(x2, y2);
        this.#ctx.lineTo(x3, y3);
        this.#ctx.lineTo(x4, y4);
        this.closePath();

        this.restore();
    }

    // rectangles
    drawRectangle_2PCA(x1, y1, x2, y2, xc=0, yc=0, angle=0) {
        x1 *= this.#coordinateScale;
        x2 *= this.#coordinateScale;
        y1 *= this.#coordinateScale;
        y2 *= this.#coordinateScale;

        this.transform(angle, xc, yc);
        
        this.beginPath(x1, y1);
        this.#ctx.lineTo(x2, y1);
        this.#ctx.lineTo(x2, y2);
        this.#ctx.lineTo(x1, y2);
        this.closePath();

        this.restore();
    }

    drawRectangle_PSCA(x, y, width, height, xc=0, yc=0, angle=0) {
        this.drawRectangle_2PCA(x, y, x+width, y+height, xc, yc, angle);
    }

    // arcs
    drawArc_PRAA(x, y, radius, angle1, angle2, counterclockwise=false) {
        x *= this.#coordinateScale;
        y *= this.#coordinateScale;
        radius *= this.#coordinateScale;
        
        this.#ctx.arc(x, y, radius, angle1, angle2, counterclockwise);
    }
}

