async function postData(elem) {

    var response = await fetch(elem.action, {
        method: elem.method,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams(new FormData(elem))

    });

    elem.reset()
    var characterElem = document.getElementById("character");
    characterElem.options.length = 0;

    if (response.ok) {
        var data = await response.json();

        show_chart1(data.chart1.SP, data.chart1.FTR, data.chart1.TTG, data.chart1.MLP, data.chart1.WTP);
        show_chart2(data.chart2);
        show_chart3(data.chart3);

        var hide = document.getElementsByClassName('control');
        for (var i in hide) {
            hide[i].hidden = true;
        }
        //TO HIDE THE INPUT 
        document.getElementById('chart1').hidden = false;
        document.getElementById('chart2').hidden = false;
        document.getElementById('chart3').hidden = false;
        document.getElementById('jump').style = "display:block";
    } else {
        alert(response.status + " " + response.statusCode);

    }
}

