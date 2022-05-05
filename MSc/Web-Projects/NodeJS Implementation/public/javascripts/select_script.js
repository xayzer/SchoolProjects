var wtp_ch = ["Pooh", "Piglet", "Owl", "Tiger", "Eeyore"];
var sp_ch = ["Eric", "Kenny", "Stan", "Kyle"];
var ftr_ch = ["Leela", "Bender", "Fry", "Amy", "Zoidberg", "Farnsworth"];
var ttg_ch = ["Starfire", "Raven", "Robin", "Beastboy", "Cyborg"];
var mlp_ch = ["Sparkle", "Fluttershy", "Pinky", "Rainbow"];

function cartoonSelected(cartoon) {
    var characterElem = document.getElementById("character");
    characterElem.options.length = 0;
    if (cartoon == "WTP") {
        ch = wtp_ch;
    } else if (cartoon == "SP") {
        ch = sp_ch;
    } else if (cartoon == "FTR") {
        ch = ftr_ch;
    } else if (cartoon == "TTG") {
        ch = ttg_ch
    } else if (cartoon == "MLP") {
        ch = mlp_ch
    } else {
        characterElem.disabled = true;
    }

    ch.forEach(function (item) {
        var option = document.createElement("option");
        option.text = item;
        option.value = item;
        characterElem.add(option);
    });
    characterElem.disabled = false;
}
