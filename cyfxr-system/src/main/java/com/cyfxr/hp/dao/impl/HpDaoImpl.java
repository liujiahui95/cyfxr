package com.cyfxr.hp.dao.impl;

import com.cyfxr.hp.dao.HpDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("hpDao")
public class HpDaoImpl implements HpDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getCYFXBB_SY_Line() {
        String sql = "select * from \r\n" +
                "(\r\n" +
                "  select '本年度' as 年度, to_number(substr(日期,5,2))||'_'||to_number(substr(日期,7,2)) as 日期 , \r\n" +
                "  round(sum(票面运费)/10000,2) as 总运费,\r\n" +
                "  round(sum(承运)/10000,2) as 承运,\r\n" +
                "  round(sum(盈余)/10000,2) as 盈余,\r\n" +
                "  sum(车数) as 车数,\r\n" +
                "  round(sum(计费重量)/10000,2) as 计费重量,\r\n" +
                "  (case when round(sum(承运)/10000,2) > 0 then round(round(sum(盈余)/10000,2)/round(sum(承运)/10000,2)*100,2) else 0 end) as 盈余率  \r\n" +
                "   from fb_sy_2019\r\n" +
                "   where 段代码 < 8 and 日期 >=20190101 and 日期 <= 20191231\r\n" +
                "   group by to_number(substr(日期,5,2))||'_'||to_number(substr(日期,7,2))\r\n" +
                "   union all\r\n" +
                "  select '上年度' as 年度, to_number(substr(日期,5,2))||'_'||to_number(substr(日期,7,2)) as 日期 , \r\n" +
                "  round(sum(票面运费)/10000,2) as 总运费,\r\n" +
                "  round(sum(承运)/10000,2) as 承运,\r\n" +
                "  round(sum(盈余)/10000,2) as 盈余,\r\n" +
                "  sum(车数) as 车数,\r\n" +
                "  round(sum(计费重量)/10000,2) as 计费重量,\r\n" +
                "  (case when round(sum(承运)/10000,2) > 0 then round(round(sum(盈余)/10000,2)/round(sum(承运)/10000,2)*100,2) else 0 end) as 盈余率 \r\n" +
                "   from fb_sy_2018\r\n" +
                "   where 段代码 < 8 and 日期 >=20180101 and 日期 <= 20181231\r\n" +
                "   group by to_number(substr(日期,5,2))||'_'||to_number(substr(日期,7,2))\r\n" +
                ") \r\n" +
                "order by to_number(substr(日期,1,instr(日期,'_') -1)),to_number(substr(日期,instr(日期,'_') +1,2))";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getCYFXBB_SY_Bar() {
        String sql = "select * from(\r\n" +
                "select '本年度' as 年度, a.代码,a.名称,\r\n" +
                "round(a.承运/10000,2)+ round(a.高出归己/10000,2) + round(a.高出付出/10000,2) as 总运费,\r\n" +
                "round(a.承运/10000,2) 承运,\r\n" +
                "round(a.承运/10000,2)+round(高出归己/10000,2) - \r\n" +
                "(round(a.牵引费/10000,2)+round(a.线路使用费/10000,2)+round(a.车辆服务费/10000,2)+round(a.到达服务费/10000,2)+round(a.综合服务费/10000,2)) as 盈余,\r\n" +
                "(case when a.承运 > 0 then round((a.承运+a.高出归己-(a.牵引费+a.线路使用费+a.车辆服务费+a.到达服务费+a.综合服务费)) /(a.承运+a.高出归己),4) * 100 else 0 end) as 比例,\r\n" +
                "a.车数,round(a.计费重量/10000,2) as 计费重量,round(a.计费吨公里/10000,2) as 计费吨公里\r\n" +
                "from\r\n" +
                "(\r\n" +
                "    select t.段代码 as 代码,a.名称, \r\n" +
                "    sum(t.承运运费小计+t.使用费小计) as 承运,\r\n" +
                "    sum(t.高出部分归己小计) as 高出归己,\r\n" +
                "    sum(t.高出部分付出小计) as 高出付出,\r\n" +
                "    sum(机车牵引费) as 牵引费,\r\n" +
                "    sum(线路使用费) as 线路使用费,\r\n" +
                "    sum(车辆服务费) as 车辆服务费,\r\n" +
                "    sum(到达服务费) as 到达服务费,\r\n" +
                "    sum(综合服务费) as 综合服务费,\r\n" +
                "    sum(t.本企业牵引) as 本企业牵引,\r\n" +
                "    sum(t.本企业线路) as 本企业线路,\r\n" +
                "    sum(t.本企业车辆) as 本企业车辆,\r\n" +
                "    sum(t.本企业到达) as 本企业到达,\r\n" +
                "    sum(0) as 本企业综合,\r\n" +
                "    sum(票数) as 票数,sum(t.车数) as 车数,\r\n" +
                "    sum(t.计费重量/1000) as 计费重量,\r\n" +
                "    sum(t.全程计费吨公里/1000) as 计费吨公里\r\n" +
                "    from FB_汇总_2019 t,SRFX_货运中心定义 a \r\n" +
                "    where t.日期 <=20191231 and t.段代码=a.编号 and a.类型='合计'\r\n" +
                "    group by t.段代码,a.名称\r\n" +
                ") a \r\n" +
                "union all\r\n" +
                "select '上年度' as 年度, a.代码,a.名称,\r\n" +
                "round(a.承运/10000,2)+ round(a.高出归己/10000,2) + round(a.高出付出/10000,2) as 总运费,\r\n" +
                "round(a.承运/10000,2) 承运,\r\n" +
                "round(a.承运/10000,2)+round(高出归己/10000,2) - \r\n" +
                "(round(a.牵引费/10000,2)+round(a.线路使用费/10000,2)+round(a.车辆服务费/10000,2)+round(a.到达服务费/10000,2)+round(a.综合服务费/10000,2)) as 盈余,\r\n" +
                "(case when a.承运 > 0 then round((a.承运+a.高出归己-(a.牵引费+a.线路使用费+a.车辆服务费+a.到达服务费+a.综合服务费)) /(a.承运+a.高出归己),4) * 100 else 0 end) as 比例,\r\n" +
                "a.车数,round(a.计费重量/10000,2) as 计费重量,round(a.计费吨公里/10000,2) as 计费吨公里\r\n" +
                "from\r\n" +
                "(\r\n" +
                "    select t.段代码 as 代码,a.名称, \r\n" +
                "    sum(t.承运运费小计+t.使用费小计) as 承运,\r\n" +
                "    sum(t.高出部分归己小计) as 高出归己,\r\n" +
                "    sum(t.高出部分付出小计) as 高出付出,\r\n" +
                "    sum(机车牵引费) as 牵引费,\r\n" +
                "    sum(线路使用费) as 线路使用费,\r\n" +
                "    sum(车辆服务费) as 车辆服务费,\r\n" +
                "    sum(到达服务费) as 到达服务费,\r\n" +
                "    sum(综合服务费) as 综合服务费,\r\n" +
                "    sum(t.本企业牵引) as 本企业牵引,\r\n" +
                "    sum(t.本企业线路) as 本企业线路,\r\n" +
                "    sum(t.本企业车辆) as 本企业车辆,\r\n" +
                "    sum(t.本企业到达) as 本企业到达,\r\n" +
                "    sum(0) as 本企业综合,\r\n" +
                "    sum(票数) as 票数,sum(t.车数) as 车数,\r\n" +
                "    sum(t.计费重量/1000) as 计费重量,\r\n" +
                "    sum(t.全程计费吨公里/1000) as 计费吨公里\r\n" +
                "    from FB_汇总_2018 t,SRFX_货运中心定义 a \r\n" +
                "    where t.日期 <=20181231 and t.段代码=a.编号 and a.类型='合计'\r\n" +
                "    group by t.段代码,a.名称\r\n" +
                ") a\r\n" +
                ")\r\n" +
                "order by 代码,年度";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getCYFXBB_SY_Scroll() {
        String sql = "select a.代码,a.名称,\r\n" +
                "round(a.承运/10000,2)+ round(a.高出归己/10000,2) + round(a.高出付出/10000,2) as 总运费,\r\n" +
                "round(a.承运/10000,2) 承运,\r\n" +
                "a.车数,round(a.计费重量/10000,2) as 计费重量,round(a.计费吨公里/10000,2) as 计费吨公里\r\n" +
                "from\r\n" +
                "(\r\n" +
                "    select t.企业代码 as 代码,a.名称, \r\n" +
                "    sum(t.承运运费小计+t.使用费小计) as 承运,\r\n" +
                "    sum(t.高出部分归己小计) as 高出归己,\r\n" +
                "    sum(t.高出部分付出小计) as 高出付出,\r\n" +
                "    sum(机车牵引费) as 牵引费,\r\n" +
                "    sum(线路使用费) as 线路使用费,\r\n" +
                "    sum(车辆服务费) as 车辆服务费,\r\n" +
                "    sum(到达服务费) as 到达服务费,\r\n" +
                "    sum(综合服务费) as 综合服务费,\r\n" +
                "    sum(t.本企业牵引) as 本企业牵引,\r\n" +
                "    sum(t.本企业线路) as 本企业线路,\r\n" +
                "    sum(t.本企业车辆) as 本企业车辆,\r\n" +
                "    sum(t.本企业到达) as 本企业到达,\r\n" +
                "    sum(0) as 本企业综合,\r\n" +
                "    sum(票数) as 票数,sum(t.车数) as 车数,\r\n" +
                "    sum(t.计费重量/1000) as 计费重量,\r\n" +
                "    sum(t.全程计费吨公里/1000) as 计费吨公里\r\n" +
                "    from FB_汇总_2019 t,SRFX_公司定义 a \r\n" +
                "    where t.日期 <=20191231 and t.企业代码=a.代码 and a.类型='合计'\r\n" +
                "    group by t.企业代码,a.名称\r\n" +
                ") a";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }
}
