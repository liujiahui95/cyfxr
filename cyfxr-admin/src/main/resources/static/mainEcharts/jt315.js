//310	
var rq_310 = new Array();
var syf_310 = new Array();//上年度运费
var byf_310 = new Array();//本年度运费
var scy_310 = new Array();//上年度承运
var bcy_310 = new Array();//本年度承运
var syy_310 = new Array();//上年度盈余
var byy_310 = new Array();//本年度盈余
var scs_310 = new Array();//上年度车数
var bcs_310 = new Array();//本年度车数
var sjfzl_310 = new Array();//上年度计费重量
var bjfzl_310 = new Array();//本年度计费重量
var syyl_310 = new Array();//上年度盈余率
var byyl_310 = new Array();//本年度盈余率
//311
var gsmc_311 = new Array();//公司名称
var gszyf_311 = new Array();//总运费
var gscy_311 = new Array();//承运
var gscs_311 = new Array();//车数
var gsjfzl_311 = new Array();//计费重量
var gsjfdgl_311 = new Array();//计费吨公里
//312
var mc_312 = new Array();//货运中心名称
var szyf_312 = new Array();//总运费
var scy_312 = new Array();//承运
var syy_312 = new Array();//盈余
var sbl_312 = new Array();//比例
var scs_312 = new Array();//车数
var sjfzl_312 = new Array();//计费重量
var sjfdgl_312 = new Array();//计费吨公里
var bzyf_312 = new Array();//总运费
var bcy_312 = new Array();//承运
var byy_312 = new Array();//盈余
var bbl_312 = new Array();//比例
var bcs_312 = new Array();//车数
var bjfzl_312 = new Array();//计费重量
var bjfdgl_312 = new Array();//计费吨公里
$.ajax({
    url: "/hp/sy",
    type: "post",
    contentType: "application/json; charset=utf-8",
    dataType: 'text',
    data: "",
    success: function (value) {
        var obj = JSON.parse(value);//格式｛310:[{}],311:｝
        for (var i in obj) {
            if (i == "line") {//集团
                for (var zgs in obj[i]) {
                    if (obj[i][zgs][0] == "本年度") {
                        rq_310.push(obj[i][zgs][1]);
                        byf_310.push(obj[i][zgs][2]);
                        bcy_310.push(obj[i][zgs][3]);
                        byy_310.push(obj[i][zgs][4]);
                        bcs_310.push(obj[i][zgs][5]);
                        bjfzl_310.push(obj[i][zgs][6]);
                        byyl_310.push(obj[i][zgs][7]);
                    } else {
                        syf_310.push(obj[i][zgs][2]);
                        scy_310.push(obj[i][zgs][3]);
                        syy_310.push(obj[i][zgs][4]);
                        scs_310.push(obj[i][zgs][5]);
                        sjfzl_310.push(obj[i][zgs][6]);
                        syyl_310.push(obj[i][zgs][7]);
                    }
                }
            }
            if (i == "src") {//公司饼图
                for (var gs in obj[i]) {
                    gsmc_311.push(obj[i][gs][1]);
                    gszyf_311.push({name: obj[i][gs][1], value: obj[i][gs][2]});
                    gscy_311.push({name: obj[i][gs][1], value: obj[i][gs][3]});
                    gscs_311.push({name: obj[i][gs][1], value: obj[i][gs][4]});
                    gsjfzl_311.push({name: obj[i][gs][1], value: obj[i][gs][5]});
                    gsjfdgl_311.push({name: obj[i][gs][1], value: obj[i][gs][6]});
                }
            }
            if (i == "bar") {//货运中心
                for (var hyzx in obj[i]) {
                    if (obj[i][hyzx][0] == "本年度") {
                        mc_312.push(obj[i][hyzx][2]);
                        bzyf_312.push(obj[i][hyzx][3]);
                        bcy_312.push(obj[i][hyzx][4]);
                        byy_312.push(obj[i][hyzx][5]);
                        bbl_312.push(obj[i][hyzx][6]);
                        bcs_312.push(obj[i][hyzx][7]);
                        bjfzl_312.push(obj[i][hyzx][8]);
                        bjfdgl_312.push(obj[i][hyzx][9]);
                    } else {
                        szyf_312.push(obj[i][hyzx][3]);
                        scy_312.push(obj[i][hyzx][4]);
                        syy_312.push(obj[i][hyzx][5]);
                        sbl_312.push(obj[i][hyzx][6]);
                        scs_312.push(obj[i][hyzx][7]);
                        sjfzl_312.push(obj[i][hyzx][8]);
                        sjfdgl_312.push(obj[i][hyzx][9]);
                    }
                }
            }
        }
        echart1 = echarts.init(document.getElementById('con_one_1'));
        var option = {
            baseOption: {
                title: {
                    text: '本年度车数、上年度车数',
                    subtext: ''
                },
                legend: {
                    data: ['本年度车数', '上年度车数']
                },
                xAxis: {
                    data: rq_310,
                    name: '月_日',
                    axisLabel: {
                        interval: 10,
                        rotate: 0
                    }
                },
                yAxis: {
                    name: '车数',
                    // y轴数据,根据数据的最大最小之进行计算
                    scale: true
                },
                tooltip: {
                    show: true,
                    formatter: '{a}<br />日期:{b}<br />车数:{c}'
                },
                series: [{
                    name: '本年度车数',
                    type: 'line',
                    data: bcs_310
                }, {
                    name: '上年度车数',
                    type: 'line',
                    data: scs_310
                }
                ]
            },
            media: [
                {
                    //小与1000像素时候响应
                    query: {
                        maxWidth: 1000
                    },
                    option: {
                        title: {
                            show: true,
                            text: '集团年度车数分析'
                        }
                    }
                }
            ]
        };
        //每次窗口大小改变的时候都会触发onresize事件，
        //这个时候我们将echarts对象的尺寸赋值给窗口的大小这个属性，从而实现图表对象与窗口对象的尺寸一致的情况
        //window.onresize = echart1.resize;
        echart1.setOption(option);

        echart2 = echarts.init(document.getElementById('con_two_1'));
        var option = {
            baseOption: {
                title: {
                    text: '分货运中心车数分析',
                    subtext: ''
                },
                legend: {
                    data: ['上年度车数', '本年度车数']
                },
                xAxis: {
                    data: mc_312,
                    axisLabel: {
                        interval: 0,
                        /* formatter:function(value)  
                            {  
                                return value.split("").join("\n");  
                            } 文字竖向排列 */
                        rotate: 40
                    }
                },
                yAxis: {
                    name: '车数'
                },
                tooltip: {
                    show: true,
                    formatter: '{a}<br />{b}<br />{c}'
                },
                series: [{
                    name: '上年度车数',
                    type: 'bar',
                    data: scs_312
                },
                    {
                        name: '本年度车数',
                        type: 'bar',
                        data: bcs_312
                    }
                ]
            },
            media: [
                {
                    //小与1000像素时候响应
                    query: {
                        maxWidth: 1000
                    },
                    option: {
                        title: {
                            show: true,
                            text: '货运中心年度车数分析'
                        }
                    }
                }
            ]
        };
        //每次窗口大小改变的时候都会触发onresize事件，
        //这个时候我们将echarts对象的尺寸赋值给窗口的大小这个属性，从而实现图表对象与窗口对象的尺寸一致的情况
        //window.onresize = echart2.resize;
        echart2.setOption(option);

        echart3 = echarts.init(document.getElementById('con_three_1'));
        var option = {
            title: {
                text: '分公司车数统计(车)',
                subtext: ''
                //x:'center'
            },
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                type: 'scroll',
                orient: 'vertical',
                right: 0,
                top: 40,
                bottom: 10,
                data: gsmc_311
                //selected: gszyf
            },
            //调色盘颜色列表共11种。如果系列没有设置颜色，则会依次循环从该列表中取颜色作为系列颜色
            color: ['#c23531', '#2f4554', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570', '#c4ccd3', '#3399CC'],
            series: [
                {
                    name: '公司名称',
                    type: 'pie',
                    radius: '70%',
                    center: ['50%', '50%'],
                    data: gscs_311,
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };
        //每次窗口大小改变的时候都会触发onresize事件，这个时候我们将echarts对象的尺寸赋值给窗口的大小这个属性，
        //从而实现图表对象与窗口对象的尺寸一致的情况
        //window.onresize = echart3.resize;
        echart3.setOption(option);

        window.addEventListener("resize", function () {
            echart1.resize();
            echart2.resize();
            echart3.resize();
        });
    },
    error: function (XMLHttpResponse, textStatus, errorThrown) {
        console.log("error: " + XMLHttpResponse.responseText);
        console.log("1 异步调用返回失败,XMLHttpResponse.readyState:" + XMLHttpResponse.readyState);
        console.log("2 异步调用返回失败,XMLHttpResponse.status:" + XMLHttpResponse.status);
        console.log("3 异步调用返回失败,textStatus:" + textStatus);
        console.log("4 异步调用返回失败,errorThrown:" + errorThrown);
    },
    complete: function (XMLHttpRequest, textStatus) {
        var returnText = XMLHttpRequest.responseText;
        //$("#satable").html(returnText);	
    }

});