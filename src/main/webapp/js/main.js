var colors = ['#000', '#1e8dcb', '#c70d1a', '#f0eef1'];

var chart = c3.generate({
    bindto: '#chart',
    data: {
        columns: [
            ['data', 0, 0, 0, 0]
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

var sketcher = atrament('#canvas', 400, 400);
sketcher.weight = 20;
sketcher.smoothing = false;

var tool = document.getElementById("tool-marker-black");
tool.addEventListener("click", function () {
    sketcher.color = "#000000";
    // sketcher.mode = "draw";
});
var tool = document.getElementById("tool-marker-blue");
tool.addEventListener("click", function () {
    sketcher.color = "#0000ff";
    // sketcher.mode = "draw";
});
var tool = document.getElementById("tool-eraser");
tool.addEventListener("click", function () {
    sketcher.clear();
    drawing = true;
    timeoutFunction();
    // sketcher.mode = "erase";
});

var canvas = document.getElementById("canvas");
var drawing = false;
var timeout;

var timeoutFunction = function () {
    if (drawing) {
        timeout = setTimeout(function () {
            // alert(canvas.toDataURL());
            if(drawing) {
                sendImage();
                drawing = false;
            }
        }, 300);
    }
};

canvas.addEventListener("mouseup", timeoutFunction);
canvas.addEventListener("mouseout", timeoutFunction);
canvas.addEventListener("mousedown", function () {
    drawing = true;
    clearTimeout(timeout);
});

var sendImage = function () {
    var request = new XMLHttpRequest();
    request.open('POST', '/recognize', true);

    request.onload = function () {
        if (this.status >= 200 && this.status < 400) {
            // Success!
            let data = JSON.parse(this.response);
            // chart.unload();
            chart.load({
                columns: [['data', 100*data.data[0], 100*data.data[1], 100*data.data[2], 100*data.data[3]]]
            });
        } else {
            // We reached our target server, but it returned an error

        }
    };

    request.onerror = function () {
        // There was a connection error of some sort
    };

    request.send(canvas.toDataURL());
};