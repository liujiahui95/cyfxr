function formatYyyyMm() {
    $("#startTime1").datetimepicker({
        weekStart: 0, //一周从哪一天开始
        todayBtn: 1, //
        todayHighlight: 1,
        startView: 3,//选择月
        forceParse: 0,
        showMeridian: 1,
        format: 'yyyy-mm',
        minView: '3',
        autoclose: true,
        endDate: new Date()
    }).on('changeDate', function (event) {
        event.preventDefault();
        event.stopPropagation();
        var startTime = event.date;
        $('#endTime1').datetimepicker('setStartDate', startTime);
    });

    $("#endTime1").datetimepicker({
        weekStart: 0, //一周从哪一天开始
        todayBtn: 1, //
        todayHighlight: 1,
        startView: 3,//选择月
        forceParse: 0,
        showMeridian: 1,
        format: 'yyyy-mm',
        minView: '3',
        autoclose: true,
        endDate: new Date()
    }).on('changeDate', function (event) {
        event.preventDefault();
        event.stopPropagation();
        var endTime = event.date;
        $("#startTime1").datetimepicker('setEndDate', endTime);
    });
}

function initDatetimepicker() {
    var startTime1 = new Date(new Date().getTime() - (1000 * 60 * 60 * 24 * 1));
    var endTime1 = new Date(new Date().getTime() - (1000 * 60 * 60 * 24 * 1));
    $("#startTime").datetimepicker('setDate', startTime1);
    $("#endTime").datetimepicker('setDate', endTime1);
}