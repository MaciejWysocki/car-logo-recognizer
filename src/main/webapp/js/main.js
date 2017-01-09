var colors = ['#000', '#1e8dcb', '#c70d1a', '#f0eef1'];

var chart = c3.generate({
    bindto: '#chart',
    data: {
        columns: [
            ['data', 30, 60, 8, 2]
        ],
        type: 'bar',
        colors: {
            data: function (d) {
                return colors[d.index];
            }
        }
    },
    axis: {
        rotated: true,
        x: {
            show: false
        },
        y: {
            max: 100,
            min: 0,
            padding: { top: 0, bottom: 0 }
        }
    },
    grid: {
        y: {
            show: true
        }
    },
    legend: {
        show: false
    }
});

var sketcher = atrament('#canvas');