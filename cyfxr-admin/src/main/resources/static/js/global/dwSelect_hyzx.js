function getHyzx() {
    //单位公共部分
    var select = $("#dw");
    $("#dw").find("option:not(:first)").remove();
    select.clear;
    select.append("<option value='801'>阿拉山口站</option>" +
        "<option value='802'>哈密货运中心</option>" +
        "<option value='803'>霍尔果斯站</option>" +
        "<option value='804'>喀什货运中心</option>" +
        "<option value='805'>库尔勒货运中心</option>" +
        "<option value='806'>奎屯货运中心</option>" +
        "<option value='807'>乌鲁木齐货运中心</option>");
}

