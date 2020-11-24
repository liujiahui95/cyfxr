(function () {
    //单位公共部分
    var select = $("#dw");
    $("#dw").find("option:not(:first)").remove();
    select.clear;
    select.append("<option disabled='disabled'>-----车务段-----</option>" +
        "<option value='0'>全部</option>" +
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
    $('#dw').selectpicker('refresh');

});