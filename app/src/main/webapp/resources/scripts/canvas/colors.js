
export class Colors {
    red = {
        dark: '#CB152B',
        light: '#DA5869'
    };
    
    green = {
        dark: '#628D3F',
        light: '#53CA61'
    }

    yellow = {
        dark: '#FA791F',
        light: '#FFBE33'
    }

    blue = {
        dark: '#0096B8',
        light: '#1BD5EE'
    }

    purple = {
        dark: '#684C94',
        light: '#8D74B8'
    }

    grey = {
        dark: '#333333',
        light: '#AAAAAA'
    }

    getColor(color, shade) {
        let c = this.grey;
        switch (color) {
            case 'red':
                c = this.red;
                break;
            case 'green':
                c = this.green;
                break;
            case 'yellow':
                c = this.yellow;
                break;
            case 'blue':
                c = this.blue;
                break;
            case 'purple':
                c = this.purple;
                break;
            default:
                break;
        }

        if (shade === "dark") return c.dark;
        return c.light;
        
    }
}
