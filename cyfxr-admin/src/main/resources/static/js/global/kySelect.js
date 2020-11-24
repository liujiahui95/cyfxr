function getKyz() {
    //单位公共部分
    var select = $("#dw");
    $("#dw").find("option:not(:first)").remove();
    select.clear;
    select.append("<option value='0'>全部</option>" +
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
        "<option value='612'>霍尔果斯站</option>");
}