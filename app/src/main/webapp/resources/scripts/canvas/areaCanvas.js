import { Drawer } from "./drawer.js";
import { Colors } from "./colors.js";


export class AreaCanvas {
    width;
    height;
    maxX;
    maxY;
    gridScale;
    colors;
    shapeBorderThickness;
    dotRadius;
    ringRadius;
    R;

    
    constructor(width, height, gridScale, R=1, shapeBorderThickness=2, dotRadius=5, ringRadius=3) {
        this.width = width;
        this.height = height;
        this.maxX = width/2;
        this.maxY = height/2;
        this.gridScale = gridScale;
        this.shapeBorderThickness = shapeBorderThickness;
        this.dotRadius = dotRadius;
        this.ringRadius = ringRadius;
        this.R = R;
        this.colors = new Colors();

        let canvas1 = document.getElementById('background');
        let ctx1 = canvas1.getContext('2d');
        ctx1.transform(1, 0, 0, -1, this.maxX, this.maxY);

        let canvas2 = document.getElementById('foreground');
        let ctx2 = canvas2.getContext('2d');
        ctx2.transform(1, 0, 0, -1, this.maxX, this.maxY);

        let canvas3 = document.getElementById('forethestground');
        let ctx3 = canvas3.getContext('2d');
        ctx3.transform(1, 0, 0, -1, this.maxX, this.maxY);

    }

    setR(R) {
        this.R = R;
    }

    setGridScale(gridScale) {
        this.gridScale = gridScale;
    }

    setShapeBorderThickness(shapeBorderThickness) {
        this.shapeBorderThickness = shapeBorderThickness;
    }

    setDotRadius(dotRadius) {
        this.dotRadius = dotRadius;
    }

    setRingRadius(ringRadius) {
        this.ringRadius = ringRadius;
    }


    getBackgroundCanvas() {
        return document.getElementById('background');
    }

    getForegroundCanvas() {
        return document.getElementById('foreground');
    }

    getForethestgroundCanvas() {
        return document.getElementById('forethestground');
    }
    

    newBackgroundDrawer() {
        let canvas = document.getElementById('background');
        const ctx = canvas.getContext('2d');
        
        return new Drawer(ctx, this.maxX, this.maxY);
    }

    newForegroundDrawer() {
        let canvas = document.getElementById('foreground');
        const ctx = canvas.getContext('2d');
        
        return new Drawer(ctx, this.maxX, this.maxY);
    }

    newForethestgroundDrawer() {
        let canvas = document.getElementById('forethestground');
        const ctx = canvas.getContext('2d');
        
        return new Drawer(ctx, this.maxX, this.maxY);
    }


    /*
    state = [
        {
            k = n,
            x = n,
            y = n
        }
    ]
    */
    drawForethestground(state) {
        const drawer = this.newForethestgroundDrawer();
        drawer.clear();
        drawer.setStyle(
            this.colors.getColor("blue", "light"),
            this.colors.getColor("blue", "dark"),
            1
        );
        for (const dot of state) {
            const x = dot.x;
            const y = dot.y;
            const k = dot.k;

            const smooth = 1 - Math.cos(Math.PI * k);

            let n = smooth <= 1 ? Math.PI * smooth : 3 * Math.PI * smooth - 4 * Math.PI;
            let m = smooth <= 1 ? 3 * Math.PI * smooth : Math.PI * smooth;

            // inner
            drawer.beginPath();
            drawer.drawArc_PRAA(x, y, this.dotRadius + this.ringRadius, n, m, false);
            drawer.drawArc_PRAA(x, y, this.dotRadius + 2*this.ringRadius, m, n, true);
            drawer.closePath();
            drawer.draw(true, true);

            // outer
            drawer.beginPath();
            drawer.drawArc_PRAA(x, y, this.dotRadius + 3*this.ringRadius, -m, -n, false);
            drawer.drawArc_PRAA(x, y, this.dotRadius + 4*this.ringRadius, -n, -m, true);
            drawer.closePath();
            drawer.draw(true, true);
        }

    }


    /*
    dots = {
        id = {
            x = n,
            y = n,
            maxMissR = n
        }
    }
    */
    drawForeground(dots) {
        const drawer = this.newForegroundDrawer();
        drawer.clear();
        drawer.setLineWidth(1);
        const margin = this.dotRadius/2;
        // drawer.closePath();

        for (const dot of Object.values(dots)) {
            const hit = dot.maxMissR < this.R;
            const x = dot.x * this.gridScale;
            const y = dot.y * this.gridScale;

            drawer.setStyle(
                hit ? this.colors.getColor("green", "light") : this.colors.getColor("yellow", "light"),
                hit ? this.colors.getColor("green", "dark") : this.colors.getColor("yellow", "dark"),
                1
            );
            if (dot.maxMissR == -1) {
                drawer.setStyle(
                    this.colors.getColor("purple", "light"),
                    this.colors.getColor("purple", "dark"),
                    1
                )
            }

            if (Math.abs(x) < this.maxX - margin && Math.abs(y) < this.maxY - margin) {
                drawer.beginPath();
                drawer.drawArc_PRAA(x, y, this.dotRadius, 0, Math.PI * 2);
                drawer.draw(true, true);
                continue;
            }

            const mx = this.maxX - this.dotRadius;
            const my = this.maxY - this.dotRadius;

            const xb = Math.max(-mx, Math.min(x, mx));
            const yb = Math.max(-my, Math.min(y, my));

            const dx = x - xb;
            const dy = y - yb;

            const vx = dx / Math.sqrt(dx*dx + dy*dy);
            const vy = dy / Math.sqrt(dx*dx + dy*dy);

            const tipX = xb + this.dotRadius * vx;
            const tipY = yb + this.dotRadius * vy;
            const angle = Math.atan2(dy, dx);

            drawer.drawTriangle_3PCA(
                0, 0,
                -this.dotRadius*2, this.dotRadius,
                -this.dotRadius*2, -this.dotRadius,
                tipX, tipY, angle
            );
            drawer.draw(true, true);
        }
    }


    /*
    shape / border = [
        {
            color = "",
            startPoint = [x, y], (optional)
            closed = bool (only for border),
            parts = [
                {
                    type = "", (
                        border{line2[2PAC], line[PAC], arc[PRAA]},
                        shape{line[PAC], arc[PRAA], rectangle[2PAC], triangle[3PAC], quadrangle[4PAC]}
                    )
                    params = [...]
                }
            ]
        }
    ]
    */
    drawBackground(shapes, borders) {
        const drawer = this.newBackgroundDrawer();
        drawer.clear();

        drawer.setCoordinateScale(this.gridScale * this.R);
        this.drawShapes(drawer, shapes);
        this.drawBorders(drawer, borders);

        drawer.setCoordinateScale(1);
        this.drawCoordinates(drawer);
    }

    /**
     * @param {Drawer} drawer
     */
    drawShapes(drawer, shapes) {
        for (const shape of shapes) {

            drawer.setFillStyle(this.colors.getColor(shape.color, "light"));
            if (shape.startPoint !== undefined) {
                drawer.beginPath(...shape.startPoint);
            }
            for (const part of shape.parts) {
                switch (part.type) {
                    case "line":
                        drawer.drawLine_PCA(...part.params);
                        break;
                    case "rectangle":
                        drawer.drawRectangle_2PCA(...part.params);
                        break;
                    case "triangle":
                        drawer.drawTriangle_3PCA(...part.params);
                        break;
                    case "quadrangle":
                        drawer.drawQuadrangle_4PCA(...part.params);
                        break;
                    case "arc":
                        drawer.drawArc_PRAA(...part.params);
                        break;
                    default:
                        break;
                }
            }
            drawer.draw(true);
        }
    }

    /**
     * @param {Drawer} drawer
     */
    drawBorders(drawer, borders) {
        drawer.setLineWidth(this.shapeBorderThickness);
        for (const border of borders) {

            drawer.setStrokeStyle(this.colors.getColor(border.color, "dark"));
            if (border.startPoint !== undefined) {
                drawer.beginPath(...border.startPoint);
            } else {
                drawer.beginPath();
            }
            for (const part of border.parts) {
                switch (part.type) {
                    case "line":
                        drawer.drawLine_PCA(...part.params);
                        break;
                    case "line2":
                        drawer.drawLine_2PCA(...part.params);
                        break;
                    case "arc":
                        drawer.drawArc_PRAA(...part.params);
                        break;
                    default:
                        break;
                }
            }
            if (border.closed) drawer.closePath();
            drawer.draw(false, true);
        }
    }

    /**
     * @param {Drawer} drawer
     */
    drawCoordinates(drawer) {
        drawer.setStyle("black", "black", 2);
        drawer.beginPath();
        // lines
        drawer.drawLine_2PCA(-this.maxX, 0, this.maxX, 0);
        drawer.drawLine_2PCA(0, -this.maxY, 0, this.maxY);
        // tips
        drawer.drawLine_2PCA(this.maxX, 0, this.maxX - 10, 5);
        drawer.drawLine_2PCA(this.maxX, 0, this.maxX - 10, -5);
        drawer.drawLine_2PCA(0, this.maxY, 5, this.maxY - 10);
        drawer.drawLine_2PCA(0, this.maxY, -5, this.maxY - 10);
        drawer.draw(false, true);

        drawer.setLineWidth(1);
        // 0, X, Y
        drawer.setTextPosition("left", "top");
        drawer.drawText_TPFCA("0", 2, -2, false, true);
        drawer.setTextAlign("right");
        drawer.drawText_TPFCA("Y", -7, this.maxY-2, false, true);
        drawer.setTextBaseline("bottom");
        drawer.drawText_TPFCA("X", this.maxX-2, 7, false, true);

        for (let i = 1; i*this.gridScale < Math.max(this.maxX, this.maxY)*2; i++) {
            drawer.beginPath();
            drawer.setTextPosition("center", "top");
            // X+
            drawer.drawLine_2PCA(0, 4*(2 - i%2), 0, -4*(2 - i%2), i*this.gridScale/2);
            if (i%2 == 0) drawer.drawText_TPFCA(i/2, 0, -2, false, true, i*this.gridScale/2, -4*(2 - i%2));
            // X-
            drawer.drawLine_2PCA(0, 4*(2 - i%2), 0, -4*(2 - i%2), -i*this.gridScale/2);
            if (i%2 == 0) drawer.drawText_TPFCA(-i/2, 0, -2, false, true, -i*this.gridScale/2, -4*(2 - i%2));
            
            drawer.setTextPosition("left", "middle");
            // Y+
            drawer.drawLine_2PCA(4*(2 - i%2), 0, -4*(2 - i%2), 0, 0, i*this.gridScale/2);
            if (i%2 == 0) drawer.drawText_TPFCA(i/2, 2, 0, false, true, 4*(2 - i%2), i*this.gridScale/2);
            // Y-
            drawer.drawLine_2PCA(4*(2 - i%2), 0, -4*(2 - i%2), 0, 0, -i*this.gridScale/2);
            if (i%2 == 0) drawer.drawText_TPFCA(-i/2, 2, 0, false, true, 4*(2 - i%2), -i*this.gridScale/2);
            drawer.draw(false, true);
        }
    }

}

