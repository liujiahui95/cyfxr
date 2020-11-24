var echart1;
var echart2;
var echart3;
var cid1 = "con_one_1";
var cid2 = "con_two_1";
var cid3 = "con_three_1";

//var tabs =  $(".tabs li");
function setTab(name, cursel) {
    //tabs.removeClass("off");
    var bqs;//标签个数
    if (name == "one") {
        bqs = 5;
    }
    if (name == "two") {
        bqs = 6;
    }
    if (name == "three") {
        bqs = 5;
    }
    for (var i = 1; i <= bqs; i++) {
        var menu = document.getElementById(name + i);
        var id = "con_" + name + "_" + i;

        var menudiv = document.getElementById("con_" + name + "_" + i);
        if (i == cursel) {
            menu.className = "off";
            menudiv.style.display = "block";
            createEcharts(id);
            if (name == "one") {
                cid1 = id;
            }
            if (name == "two") {
                cid2 = id;
            }
            if (name == "three") {
                cid3 = id;
            }
        } else {
            menu.className = "";
            menudiv.style.display = "none";
        }
    }
}