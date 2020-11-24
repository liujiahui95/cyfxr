function createEcharts(id) {
    if (id == "con_one_1") {
        if (echart1 != null && echart1 != "" && echart1 != undefined) {
            echart1.dispose();
        }
        // 基于准备好的dom，初始化echarts实例
        echart1 = echarts.init(document.getElementById(id));
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
        echart1.setOption(option);
    }
    if (id == "con_one_2") {
        if (echart1 != null && echart1 != "" && echart1 != undefined) {
            echart1.dispose();
        }
        // 基于准备好的dom，初始化echarts实例
        echart1 = echarts.init(document.getElementById(id));
        var option = {
            baseOption: {
                title: {
                    text: '本年度计费重量、上年度计费重量',
                    subtext: ''
                },
                legend: {
                    data: ['本年度计费重量', '上年度计费重量']
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
                    name: '万吨',
                    // y轴数据,根据数据的最大最小之进行计算
                    scale: true
                },
                tooltip: {
                    show: true,
                    formatter: '{a}<br />日期:{b}<br />计费重量:{c}'
                },
                series: [{
                    name: '本年度计费重量',
                    type: 'line',
                    data: bjfzl_310
                }, {
                    name: '上年度计费重量',
                    type: 'line',
                    data: sjfzl_310
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
                            text: '集团年度计费重量分析'
                        }
                    }
                }
            ]
        };
        //每次窗口大小改变的时候都会触发onresize事件，
        //这个时候我们将echarts对象的尺寸赋值给窗口的大小这个属性，从而实现图表对象与窗口对象的尺寸一致的情况
        //window.onresize = echart1.resize;
        echart1.setOption(option);
    }
    if (id == "con_one_3") {
        if (echart1 != null && echart1 != "" && echart1 != undefined) {
            echart1.dispose();
        }
        // 基于准备好的dom，初始化echarts实例
        echart1 = echarts.init(document.getElementById(id));
        var option = {
            baseOption: {
                title: {
                    text: '本年度承运、上年度承运',
                    subtext: ''
                },
                legend: {
                    data: ['本年度承运', '上年度承运']
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
                    name: '万元',
                    // y轴数据,根据数据的最大最小之进行计算
                    scale: true
                },
                tooltip: {
                    show: true,
                    formatter: '{a}<br />日期:{b}<br />承运:{c}'
                },
                series: [{
                    name: '本年度承运',
                    type: 'line',
                    data: bcy_310
                }, {
                    name: '上年度承运',
                    type: 'line',
                    data: scy_310
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
                            text: '集团年度承运分析'
                        }
                    }
                }
            ]
        };
        //每次窗口大小改变的时候都会触发onresize事件，
        //这个时候我们将echarts对象的尺寸赋值给窗口的大小这个属性，从而实现图表对象与窗口对象的尺寸一致的情况
        window.onresize = echart1.resize;
        echart1.setOption(option);
    }
    if (id == "con_one_4") {
        if (echart1 != null && echart1 != "" && echart1 != undefined) {
            echart1.dispose();
        }
        // 基于准备好的dom，初始化echarts实例
        echart1 = echarts.init(document.getElementById(id));
        var option = {
            baseOption: {
                title: {
                    text: '本年度运费、上年度运费',
                    subtext: ''
                },
                legend: {
                    data: ['本年度运费', '上年度运费']
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
                    name: '万元',
                    // y轴数据,根据数据的最大最小之进行计算
                    scale: true
                },
                tooltip: {
                    show: true,
                    formatter: '{a}<br />日期:{b}<br />运费:{c}'
                },
                series: [{
                    name: '本年度运费',
                    type: 'line',
                    data: byf_310
                }, {
                    name: '上年度运费',
                    type: 'line',
                    data: syf_310
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
                            text: '集团年度运费分析'
                        }
                    }
                }
            ]
        };
        //每次窗口大小改变的时候都会触发onresize事件，
        //这个时候我们将echarts对象的尺寸赋值给窗口的大小这个属性，从而实现图表对象与窗口对象的尺寸一致的情况
        window.onresize = echart1.resize;
        echart1.setOption(option);
    }
    if (id == "con_one_5") {
        if (echart1 != null && echart1 != "" && echart1 != undefined) {
            echart1.dispose();
        }
        // 基于准备好的dom，初始化echarts实例
        echart1 = echarts.init(document.getElementById(id));
        var option = {
            baseOption: {
                title: {
                    text: '本年度盈余、上年度盈余',
                    subtext: ''
                },
                legend: {
                    data: ['本年度盈余', '上年度盈余']
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
                    name: '万元',
                    // y轴数据,根据数据的最大最小之进行计算
                    scale: true
                },
                tooltip: {
                    show: true,
                    formatter: '{a}<br />日期:{b}<br />盈余:{c}'
                },
                series: [{
                    name: '本年度盈余',
                    type: 'line',
                    data: byy_310
                }, {
                    name: '上年度盈余',
                    type: 'line',
                    data: syy_310
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
                            text: '集团年度盈余分析'
                        }
                    }
                }
            ]
        };
        //每次窗口大小改变的时候都会触发onresize事件，
        //这个时候我们将echarts对象的尺寸赋值给窗口的大小这个属性，从而实现图表对象与窗口对象的尺寸一致的情况
        window.onresize = echart1.resize;
        echart1.setOption(option);
    }
    if (id == "con_two_1") {
        if (echart2 != null && echart2 != "" && echart2 != undefined) {
            echart2.dispose();
        }
        echart2 = echarts.init(document.getElementById(id));
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
        window.onresize = echart2.resize;
        echart2.setOption(option);
    }
    if (id == "con_two_2") {
        if (echart2 != null && echart2 != "" && echart2 != undefined) {
            echart2.dispose();
        }
        // 基于准备好的dom，初始化echarts实例
        echart2 = echarts.init(document.getElementById(id));
        var option = {
            baseOption: {
                title: {
                    text: '本年度计费重量、上年度计费重量',
                    subtext: ''
                },
                legend: {
                    data: ['本年度计费重量', '上年度计费重量']
                },
                xAxis: {
                    data: mc_312,
                    axisLabel: {
                        interval: 0,
                        rotate: 40
                    }
                },
                yAxis: {
                    name: '万吨',
                    // y轴数据,根据数据的最大最小之进行计算
                    scale: true
                },
                tooltip: {
                    show: true,
                    formatter: '{a}<br />{b}<br />承运:{c}'
                },
                series: [{
                    name: '本年度计费重量',
                    type: 'bar',
                    data: bjfzl_312
                }, {
                    name: '上年度计费重量',
                    type: 'bar',
                    data: sjfzl_312
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
                            text: '集团年度计费重量分析'
                        }
                    }
                }
            ]
        };
        //每次窗口大小改变的时候都会触发onresize事件，
        //这个时候我们将echarts对象的尺寸赋值给窗口的大小这个属性，从而实现图表对象与窗口对象的尺寸一致的情况
        window.onresize = echart2.resize;
        echart2.setOption(option);
    }
    if (id == "con_two_3") {
        if (echart2 != null && echart2 != "" && echart2 != undefined) {
            echart2.dispose();
        }
        // 基于准备好的dom，初始化echarts实例
        echart2 = echarts.init(document.getElementById(id));
        var option = {
            baseOption: {
                title: {
                    text: '本年度计费吨公里、上年度计费吨公里',
                    subtext: ''
                },
                legend: {
                    data: ['本年度计费吨公里', '上年度计费吨公里']
                },
                xAxis: {
                    data: mc_312,
                    axisLabel: {
                        interval: 0,
                        rotate: 40
                    }
                },
                yAxis: {
                    name: '万吨公里',
                    // y轴数据,根据数据的最大最小之进行计算
                    scale: true
                },
                tooltip: {
                    show: true,
                    formatter: '{a}<br />{b}<br />计费吨公里:{c}'
                },
                series: [{
                    name: '本年度计费吨公里',
                    type: 'bar',
                    data: bjfdgl_312
                }, {
                    name: '上年度计费吨公里',
                    type: 'bar',
                    data: sjfdgl_312
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
                            text: '集团年度计费吨公里分析'
                        }
                    }
                }
            ]
        };
        //每次窗口大小改变的时候都会触发onresize事件，
        //这个时候我们将echarts对象的尺寸赋值给窗口的大小这个属性，从而实现图表对象与窗口对象的尺寸一致的情况
        window.onresize = echart2.resize;
        echart2.setOption(option);
    }

    if (id == "con_two_4") {
        if (echart2 != null && echart2 != "" && echart2 != undefined) {
            echart2.dispose();
        }
        // 基于准备好的dom，初始化echarts实例
        echart2 = echarts.init(document.getElementById(id));
        var option = {
            baseOption: {
                title: {
                    text: '本年度承运、上年度承运',
                    subtext: ''
                },
                legend: {
                    data: ['本年度承运', '上年度承运']
                },
                xAxis: {
                    data: mc_312,
                    axisLabel: {
                        interval: 0,
                        rotate: 40
                    }
                },
                yAxis: {
                    name: '万元',
                    // y轴数据,根据数据的最大最小之进行计算
                    scale: true
                },
                tooltip: {
                    show: true,
                    formatter: '{a}<br />{b}<br />承运:{c}'
                },
                series: [{
                    name: '本年度承运',
                    type: 'bar',
                    data: bcy_312
                }, {
                    name: '上年度承运',
                    type: 'bar',
                    data: scy_312
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
                            text: '集团年度承运分析'
                        }
                    }
                }
            ]
        };
        //每次窗口大小改变的时候都会触发onresize事件，
        //这个时候我们将echarts对象的尺寸赋值给窗口的大小这个属性，从而实现图表对象与窗口对象的尺寸一致的情况
        window.onresize = echart2.resize;
        echart2.setOption(option);
    }

    if (id == "con_two_5") {
        if (echart2 != null && echart2 != "" && echart2 != undefined) {
            echart2.dispose();
        }
        // 基于准备好的dom，初始化echarts实例
        echart2 = echarts.init(document.getElementById(id));
        var option = {
            baseOption: {
                title: {
                    text: '本年度运费、上年度运费',
                    subtext: ''
                },
                legend: {
                    data: ['本年度运费', '上年度运费']
                },
                xAxis: {
                    data: mc_312,
                    axisLabel: {
                        interval: 0,
                        rotate: 40
                    }
                },
                yAxis: {
                    name: '万元',
                    // y轴数据,根据数据的最大最小之进行计算
                    scale: true
                },
                tooltip: {
                    show: true,
                    formatter: '{a}<br />{b}<br />运费:{c}'
                },
                series: [{
                    name: '本年度运费',
                    type: 'bar',
                    data: bzyf_312
                }, {
                    name: '上年度运费',
                    type: 'bar',
                    data: szyf_312
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
                            text: '集团年度运费分析'
                        }
                    }
                }
            ]
        };
        //每次窗口大小改变的时候都会触发onresize事件，
        //这个时候我们将echarts对象的尺寸赋值给窗口的大小这个属性，从而实现图表对象与窗口对象的尺寸一致的情况
        window.onresize = echart2.resize;
        echart2.setOption(option);
    }
    if (id == "con_two_6") {
        if (echart2 != null && echart2 != "" && echart2 != undefined) {
            echart2.dispose();
        }
        // 基于准备好的dom，初始化echarts实例
        echart2 = echarts.init(document.getElementById(id));
        var option = {
            baseOption: {
                title: {
                    text: '本年度盈余、上年度盈余',
                    subtext: ''
                },
                legend: {
                    data: ['本年度盈余', '上年度盈余']
                },
                xAxis: {
                    data: mc_312,
                    axisLabel: {
                        interval: 0,
                        rotate: 40
                    }
                },
                yAxis: {
                    name: '万元',
                    // y轴数据,根据数据的最大最小之进行计算
                    scale: true
                },
                tooltip: {
                    show: true,
                    formatter: '{a}<br />{b}<br />盈余:{c}'
                },
                series: [{
                    name: '本年度盈余',
                    type: 'bar',
                    data: byy_312
                }, {
                    name: '上年度盈余',
                    type: 'bar',
                    data: syy_312
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
                            text: '集团年度盈余分析'
                        }
                    }
                }
            ]
        };
        //每次窗口大小改变的时候都会触发onresize事件，
        //这个时候我们将echarts对象的尺寸赋值给窗口的大小这个属性，从而实现图表对象与窗口对象的尺寸一致的情况
        window.onresize = echart2.resize;
        echart2.setOption(option);
    }
    if (id == "con_three_1") {
        if (echart3 != null && echart3 != "" && echart3 != undefined) {
            echart3.dispose();
        }
        echart3 = echarts.init(document.getElementById(id));
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
        window.onresize = echart3.resize;
        echart3.setOption(option);
    }
    if (id == "con_three_2") {
        if (echart3 != null && echart3 != "" && echart3 != undefined) {
            echart3.dispose();
        }
        // 基于准备好的dom，初始化echarts实例
        echart3 = echarts.init(document.getElementById(id));
        var option = {
            title: {
                text: '分公司计费重量统计(万吨)',
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
                    data: gsjfzl_311,
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
        window.onresize = echart3.resize;
        echart3.setOption(option);
    }
    if (id == "con_three_3") {
        if (echart3 != null && echart3 != "" && echart3 != undefined) {
            echart3.dispose();
        }
        // 基于准备好的dom，初始化echarts实例
        echart3 = echarts.init(document.getElementById(id));
        var option = {
            title: {
                text: '分公司计费吨公里统计(万吨公里)',
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
                    data: gsjfdgl_311,
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
        window.onresize = echart3.resize;
        echart3.setOption(option);
    }

    if (id == "con_three_4") {
        if (echart3 != null && echart3 != "" && echart3 != undefined) {
            echart3.dispose();
        }
        // 基于准备好的dom，初始化echarts实例
        echart3 = echarts.init(document.getElementById(id));
        var option = {
            title: {
                text: '分公司承运统计(万元)',
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
                    data: gscy_311,
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
        window.onresize = echart3.resize;
        echart3.setOption(option);
    }
    if (id == "con_three_5") {
        if (echart3 != null && echart3 != "" && echart3 != undefined) {
            echart3.dispose();
        }
        // 基于准备好的dom，初始化echarts实例
        echart3 = echarts.init(document.getElementById(id));
        var option = {
            title: {
                text: '分公司总运费统计(万元)',
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
                    data: gszyf_311,
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
        window.onresize = echart3.resize;
        echart3.setOption(option);
    }

};