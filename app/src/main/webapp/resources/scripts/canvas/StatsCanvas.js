import { Drawer } from "./drawer.js";
import { Colors } from "./colors.js";


export class StatsCanvas {
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


        let canvas = document.getElementById('statground');
        let ctx = canvas.getContext('2d');
        ctx.transform(1, 0, 0, -1, this.maxX, this.maxY);
    }

    getStatgroundCanvas() {
        return document.getElementById('statground');
    }


    newStatgroundDrawer() {
        let canvas = document.getElementById('statground');
        const ctx = canvas.getContext('2d');
        
        return new Drawer(ctx, this.maxX, this.maxY);
    }


    drawAF(radius, k1, r, k2, parts) {
        const drawer = this.newStatgroundDrawer();
        drawer.setCoordinateScale(this.gridScale);
        drawer.clear();
        drawer.setStyle(
            this.colors.getColor("blue", "light"),
            "#31579a",
            4
        );

        if (radius > 0 && k1 != null) {
            let smooth1 = 1 - Math.cos(Math.PI * k1);
            let n = smooth1 <= 1 ? Math.PI * smooth1 : 3 * Math.PI * smooth1 - 4 * Math.PI;
            let m = smooth1 <= 1 ? 3 * Math.PI * smooth1 : Math.PI * smooth1;
            drawer.beginPath();
            drawer.drawArc_PRAA(0, 0, radius, n, m, false);
            // drawer.closePath();
            drawer.draw(false, true);
        }

        if (r > 0 && k2 != null) {
            drawer.setCoordinateScale(this.gridScale * r);
            const smooth2 = 1 - Math.cos(Math.PI * k2);

            let n = smooth2 <= 1 ? Math.PI * smooth2 : 3 * Math.PI * smooth2 - 4 * Math.PI;
            let m = smooth2 <= 1 ? 3 * Math.PI * smooth2 : Math.PI * smooth2;

            n /= Math.PI*2;
            m /= Math.PI*2;
            n *= parts.length;
            m *= parts.length;


            drawer.beginPath();
            for (let i = -parts.length; i < 2*parts.length; i++) {
                if (Math.floor(n) <= i && i <= Math.floor(m)) {
                    let index = i < 0 ? i + parts.length : i;
                    index = index >= parts.length ? index - parts.length : index
                    const p = parts[index];

                    let strt = Math.max(n-i, 0);
                    let end = Math.min(m-i, 1);
                    if (p.type == "arc") {
                        drawer.drawArc_PRAA(
                            p.params[0],
                            p.params[1],
                            p.params[2],
                            p.params[3] + (p.params[4]-p.params[3])*strt,
                            p.params[4] - (p.params[4]-p.params[3])*(1-end)
                        )
                    }
                    else if (p.type == "line") {
                        if (strt != 0) {
                            drawer.drawLine_2PCA(
                                p.start[0] + (p.params[0]-p.start[0])*strt,
                                p.start[1] + (p.params[1]-p.start[1])*strt,
                                p.params[0] - (p.params[0]-p.start[0])*(1-end),
                                p.params[1] - (p.params[1]-p.start[1])*(1-end)
                            )
                        }
                        else {
                            drawer.drawLine_PCA(
                                p.params[0] - (p.params[0]-p.start[0])*(1-end),
                                p.params[1] - (p.params[1]-p.start[1])*(1-end)
                            )
                        }
                    }
                }
            }
            drawer.draw(false, true);
        }

    }

    /**
     * @param {Drawer} drawer
     */
    drawBorders(drawer, parts) {
        
        for (const part of parts) {
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
        drawer.draw(false, true);
    }

}

