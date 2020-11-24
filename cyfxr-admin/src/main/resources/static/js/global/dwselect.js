function getSelectDw() {
    //单位公共部分
    var select = $("#dw");
    $("#dw").find("option:not(:first)").remove();
    select.clear;
    select.append("<option disabled='disabled'>-----车务段-----</option>" +
        "<option value='0'>全部</option>" +
        "<option value='601'>阿克苏车务段</option>" +
        "<option value='602'>阿勒泰基础设施段</option>" +
        "<option value='603'>哈密车务段</option>" +
        "<option value='604'>喀什车务段</option>" +
        "<option value='605'>库尔勒车务段</option>" +
        "<option value='606'>库尔勒客运段</option>" +
        "<option value='607'>奎屯车务段</option>" +
        "<option value='608'>乌鲁木齐车务段</option>" +
        "<option value='609'>乌鲁木齐客运段</option>" +
        "<option value='610'>乌鲁木齐站</option>" +
        "<option value='611'>阿拉山口站</option>" +
        "<option value='612'>霍尔果斯站</option>" +
        "<option disabled='disabled'>-----公司-----</option>" +
        "<option value='700'>乌鲁木齐局</option>" +
        "<option value='701'>北阿公司</option>" +
        "<option value='702'>甘青新疆公司乌</option>" +
        "<option value='703'>哈罗公司</option>" +
        "<option value='704'>库俄公司</option>" +
        "<option value='705'>奎北公司</option>" +
        "<option value='706'>兰新新疆公司乌</option>" +
        "<option value='707'>临哈公司</option>" +
        "<option value='708'>乌准公司</option>" +
        "<option disabled='disabled'>-----货运中心-----</option>" +
        "<option value='801'>阿拉山口站</option>" +
        "<option value='802'>哈密货运中心</option>" +
        "<option value='803'>霍尔果斯站</option>" +
        "<option value='804'>喀什货运中心</option>" +
        "<option value='805'>库尔勒货运中心</option>" +
        "<option value='806'>奎屯货运中心</option>" +
        "<option value='807'>乌鲁木齐货运中心</option>");
}

function getSelectZm() {
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
                //$("#cz").append(new Option(dbm,temp));
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