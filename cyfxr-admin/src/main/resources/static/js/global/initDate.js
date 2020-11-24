function initDate() {
    var startdate = new Date(new Date().getTime() - (1000 * 60 * 60 * 24 * 1));
    var endTime = new Date(new Date().getTime() - (1000 * 60 * 60 * 24 * 1));
    $("#endTime").val(formatdateYYMMDD(endTime))
    $("#startTime").val(formatdateYYMMDD(startdate))
}

function formatdateYYMMDD(date) {
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();
    if (month < 10) {
        month = "0" + month;
    }
    if (day < 10) {
        day = "0" + day;
    }
    return (year + "-" + month + "-" + day);
}

function initDateYYMM() {
    var startdate = new Date(new Date().getTime() - (1000 * 60 * 60 * 24 * 1));
    var endTime = new Date(new Date().getTime() - (1000 * 60 * 60 * 24 * 1));
    $("#endTime1").val(formatdateYYMM(endTime))
    $("#startTime1").val(formatdateYYMM(startdate))

}

function formatdateYYMM(date) {
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    if (month < 10) {
        month = "0" + month;
    }
    return (year + "-" + month);
}