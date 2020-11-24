function getHyqy() {
    //单位公共部分
    var select = $("#dw");
    $("#dw").find("option:not(:first)").remove();
    select.clear;
    select.append("<option value='0'>全部</option>" +
        "<option value='a17'>乌鲁木齐局</option>" +
        "<option value='195'>奎北公司</option>" +
        "<option value='196'>乌准公司</option>" +
        "<option value='197'>库俄公司</option>" +
        "<option value='217'>哈罗公司</option>" +
        "<option value='256'>临哈公司</option>" +
        "<option value='350'>北阿公司</option>");
}

function getYjh() {
    //单位公共部分
    var select = $("#yjh");
    $("#yjh").find("option:not(:first)").remove();
    select.clear;
    select.append("<option value='0'>全部</option>" +
        "<option value='1'>1号运价</option>" +
        "<option value='2'>2号运价</option>" +
        "<option value='3'>3号运价</option>" +
        "<option value='4'>4号运价</option>" +
        "<option value='5'>5号运价</option>" +
        "<option value='6'>6号运价</option>" +
        "<option value='20英尺箱'>20英尺箱</option>" +
        "<option value='40英尺箱'>40英尺箱</option>" +
        "<option value='敞顶箱'>敞顶箱</option>" +
        "<option value='机械冷藏车'>机械冷藏车</option>");
}

function getCz() {
    // 选中单位代码
    var dwdm = $("#dw").val();
    $("#cz").find("option:not(:first)").remove();
    if (dwdm == 0 || dwdm == "") {//单位选择全部时，不进行加载站名
        $("#cz").clear;
        return;
    }
    //承运清算表使用到此企业
    var gs = new Array("195", "196", "197", "217", "256", "350", "a17", "232", "225");//公司站
    var lx = "";
    for (var i = 0; i < gs.length; i++) {
        if (gs[i] == dwdm) {
            lx = "2";
        }
    }
    $.ajax({
        url: "/basicdata/getCZ",
        async: false,
        cache: false,
        type: "post",
        data: {
            "lx": lx, "dm": dwdm
        },
        success: function (data) {
            var select = $("#cz");
            select.clear;
            select.append("<option value='0'>全部</option>");//全部，传值为空
            var option = "";
            for (var i in data) {
                var dbm = data[i].dbm;//区分大小写
                var mc = data[i].mc;
                option = option + "<option value='" + dbm + "'>" + mc + "</option>"
            }
            select.append(option);
        },
        error: function (XMLHttpResponse, textStatus, errorThrown) {
            console.log("error: " + XMLHttpResponse.responseText);
            console.log("1 异步调用返回失败,XMLHttpResponse.readyState:" + XMLHttpResponse.readyState);
            console.log("2 异步调用返回失败,XMLHttpResponse.status:" + XMLHttpResponse.status);
            console.log("3 异步调用返回失败,textStatus:" + textStatus);
            console.log("4 异步调用返回失败,errorThrown:" + errorThrown);
        }
    })
}

function getJm() {
    var select = $("#jm");
    $("#jm").find("option:not(:first)").remove();
    select.clear;
    select.append("<option value='0'>全部</option>" +
        "<option value='1'>哈尔滨局</option>" +
        "<option value='2'>沈阳局</option>" +
        "<option value='3'>北京局</option>" +
        "<option value='4'>太原局</option>" +
        "<option value='5'>呼和浩特局</option>" +
        "<option value='6'>郑州局</option>" +
        "<option value='7'>武汉局</option>" +
        "<option value='8'>西安局</option>" +
        "<option value='9'>济南局</option>" +
        "<option value='10'>上海局</option>" +
        "<option value='11'>南昌局</option>" +
        "<option value='12'>广州局</option>" +
        "<option value='13'>南宁局</option>" +
        "<option value='14'>成都局</option>" +
        "<option value='15'>昆明局</option>" +
        "<option value='16'>兰州局</option>" +
        "<option value='17'>乌鲁木齐局</option>" +
        "<option value='18'>青藏公司</option>");
}