let colors = ['#000', '#1e8dcb', '#c70d1a', '#f0eef1'];

let chart = c3.generate({
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

let sketcher = atrament('#canvas', 400, 400);
sketcher.weight = 20;
sketcher.smoothing = false;

let tool = document.getElementById("tool-marker-black");
tool.addEventListener("click", function () {
    sketcher.color = "#000000";
    sketcher.mode = "draw";
});
tool = document.getElementById("tool-marker-blue");
tool.addEventListener("click", function () {
    sketcher.color = "#0000ff";
    sketcher.mode = "draw";
});
tool = document.getElementById("tool-marker-white");
tool.addEventListener("click", function () {
    sketcher.mode = "erase";
});
tool = document.getElementById("tool-eraser");
tool.addEventListener("click", function () {
    sketcher.clear();
    updateJudgement([0, 0, 0, 0]);
    drawing = true;
});

let canvas = document.getElementById("canvas");
let drawing = false;
let timeout;

let timeoutFunction = function () {
    if (drawing) {
        timeout = setTimeout(function () {
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

let sendImage = function () {
    let request = new XMLHttpRequest();
    request.open('POST', '/recognize', true);

    request.onload = function () {
        if (this.status >= 200 && this.status < 400) {
            updateJudgement(JSON.parse(this.response).data);
        } 
    };

    request.send(canvas.toDataURL());
};

let carMap = ['BMW', 'Ford', 'Mazda', 'Mercedes'];

let updateJudgement = function(data) {
    chart.load({
        columns: [['data', 100*data[0], 100*data[1], 100*data[2], 100*data[3]]]
    });

    let description = "I am not sure what is it.";

    let maxData = 0;
    let maxIndex = -1;
    for(let i = 0; i < data.length; ++i) {
        if(data[i] > maxData) {
            maxData = data[i];
            maxIndex = i;
            if(maxData > 0.7) {
                description = "I am sure it is ";
            } else if (maxData > 0.5) {
                description = "I think it is ";
            } else if (maxData > 0.3) {
                description = "It mimght be ";
            }
        }
    }
    if(maxData > 0.3) {
        description += carMap[maxIndex];
    }
    document.getElementById('answer').innerText = description;
};