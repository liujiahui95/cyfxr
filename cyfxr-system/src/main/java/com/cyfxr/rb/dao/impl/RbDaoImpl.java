package com.cyfxr.rb.dao.impl;

import com.cyfxr.common.utils.DateUtil;
import com.cyfxr.rb.dao.RbDao;
import com.cyfxr.system.domain.CyfxSearchModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("rbDao")
public class RbDaoImpl implements RbDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getRb1(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String cz = "";
        String skzdbm = "";
        String dw = searchInfo.getDw();
        String czs = searchInfo.getCz();
        int flag = 0;
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            int id = Integer.parseInt(dw);
            if (id >= 600 && id <= 699) {//查询车务段，只有客运
                flag = 1;
            }
            if (id >= 800 && id <= 899) {//查询货运中心，只有货运
                flag = 2;
            }
            if (czs != null && !czs.equals("") && !czs.equals("0")) {//选择单个站
                cz = "and czid ='" + searchInfo.getCz() + "'";
                skzdbm = "and skzdbm ='" + searchInfo.getCz() + "'";
            } else {//选择单位、未选择车站、查询单位下面所有站
                cz = "and instr((select fwdbm from srfx_zmk where id =" + dw + "),czid )>0";
                skzdbm = "and instr((select fwdbm from srfx_zmk where id =" + dw + "),skzdbm )>0";
            }
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String nd = ksrq.substring(0, 4);//nd
        //上年年度yyyy
        int year = Integer.parseInt(nd) - 1;
        String lastYear = "" + year;
        String lasttime = lastYear + jsrq.substring(4, 8);//yyyymmdd 上年结束
        String lasttime1 = lastYear + ksrq.substring(4, 8);//yyyymmdd 上年开始
        //获取起始天数的集合
        List<String> dayList = DateUtil.getDayList(searchInfo.getKSRQ().replace("-", ""), searchInfo.getJSRQ().replace("-", ""));
        int yearDays = DateUtil.getDayList(nd + "0101", nd + "1231").size();
        List<String> lastDayList = DateUtil.getDayList(lastYear + "0101", lasttime);
        //获取天数
        int day = dayList.size();
        int lastDay = lastDayList.size();
        String days = day + "";
        String lastDays = lastDay + "";
        //调用方法查询本年和上年结果
        insertRb1_tem(nd, days, jsrq, ksrq, cz, skzdbm, flag);//本年度
        insertRb1_tem(lastYear, lastDays, lasttime, lasttime1, cz, skzdbm, flag);//上年度同期
        //形成结果
        String sql = "select ID,MC,DW,\r\n" +
                "DRRS,DRKYSR,DRSRL,LJRS,LJKYSR,LJSRL,RJRS,RJKYSR,RJSRL,\r\n" +
                "gzl_jh,je_jh,srl_jh,ljgzl_jh,ljje_jh,ljsrl_jh,rjgzl_jh,rjje_jh,rjsrl_jh, \r\n" +
                "DRRS_s,DRKYSR_s,DRSRL_s,LJRS_s,LJKYSR_s,LJSRL_s,RJRS_s,RJKYSR_s,RJSRL_s,\r\n" +
                "--年度计划增减\r\n" +
                "(drrs-rjgzl_jh) as drgzl_ndzj,(drkysr-rjje_jh) as drje_ndzj,(case when drrs-rjgzl_jh>0 then round((drkysr-rjje_jh)/(drrs-rjgzl_jh),2) else 0 end) as drsrl_ndzj,\r\n" +
                "(ljrs-ljgzl_jh) as ljgzl_ndzj,(ljkysr-ljje_jh) as ljje_ndzj,(case when ljrs-ljgzl_jh>0 then round((ljkysr-ljje_jh)/(ljrs-ljgzl_jh),2) else 0 end) as ljsrl_ndzj,\r\n" +
                "(rjrs-rjgzl_jh) as rjgzl_ndzj,(rjkysr-rjje_jh) as rjje_ndzj,(case when rjrs-rjgzl_jh>0 then round((rjkysr-rjje_jh)/(rjrs-rjgzl_jh),2) else 0 end) as rjsrl_ndzj,\r\n" +
                "--同比增减\r\n" +
                "(drrs-drrs_s) as drgzl_tbzj,(drkysr-drkysr_s) as drkysr_tbzj,(case when drrs-drrs_s>0 then round((drkysr-drkysr_s)/(drrs-drrs_s),2) else 0 end) as drsrl_tbzj,\r\n" +
                "(ljrs-ljrs_s) as ljgzl_tbzj,(ljkysr-ljkysr_s) as ljkysr_tbzj,(case when ljrs-ljrs_s>0 then round((ljkysr-ljkysr_s)/(ljrs-ljrs_s),2) else 0 end) as ljsrl_tbzj,\r\n" +
                "(rjrs-rjrs_s) as rjgzl_tbzj,(rjkysr-rjkysr_s) as rjkysr_tbzj,(case when rjrs-rjrs_s>0 then round((rjkysr-rjkysr_s)/(rjrs-rjrs_s),2) else 0 end) as rjsrl_tbzj\r\n" +
                "from\r\n" +
                "(\r\n" +
                "  select a.ID,a.MC,a.DW,\r\n" +
                "  round(b.DRRS/" + jldw + ",2) drrs,round(b.DRKYSR/" + jldw + ",2) DRKYSR,b.DRSRL,round(b.LJRS/" + jldw + ",2) ljrs,round(b.LJKYSR/" + jldw + ",2) ljkysr,b.LJSRL,round(b.RJRS/" + jldw + ",2) rjrs,round(b.RJKYSR/" + jldw + ",2) rjkysr,b.RJSRL,\r\n" +
                "  c.gzl as gzl_jh,c.je as je_jh,c.srl as srl_jh,c.ljgzl as ljgzl_jh,c.ljje as ljje_jh,c.ljsrl as ljsrl_jh,c.rjgzl as rjgzl_jh,c.rjje as rjje_jh,c.rjsrl as rjsrl_jh, \r\n" +
                "  round(d.DRRS_s/" + jldw + ",2) DRRS_s,round(d.DRKYSR_s/" + jldw + ",2) DRKYSR_s,d.DRSRL_s,round(d.LJRS_s/" + jldw + ",2) LJRS_s,round(d.LJKYSR_s/" + jldw + ",2)LJKYSR_s,d.LJSRL_s,round(d.RJRS_s/" + jldw + ",2)RJRS_s,round(d.RJKYSR_s/" + jldw + ",2)RJKYSR_s,d.RJSRL_s\r\n" +
                "  from hdy_rb1 a\r\n" +
                "  left join\r\n" +
                "  (\r\n" +
                "    select nd,xh,\r\n" +
                "    sum(drrs) drrs,sum(drkysr) drkysr,(case when abs(sum(drrs))>0 then round(sum(drkysr)/sum(drrs),2) else 0 end) drsrl,\r\n" +
                "    sum(ljrs) ljrs,sum(ljkysr) ljkysr,(case when abs(sum(ljrs))>0 then round(sum(ljkysr)/sum(ljrs),2) else 0 end) ljsrl,\r\n" +
                "    sum(rjrs) rjrs,sum(rjkysr) rjkysr,(case when abs(sum(rjrs))>0 then round(sum(rjkysr)/sum(rjrs),2) else 0 end) rjsrl\r\n" +
                "    from cyfx_rb1_ky_temp \r\n" +
                "    where nd=" + nd + "\r\n" +
                "    group by nd,xh\r\n" +
                "    union all\r\n" +
                "    select nd,xh,\r\n" +
                "    sum(drjfzl) drjfzl,sum(drhysr) drhysr,(case when abs(sum(drjfzl))>0 then round(sum(drhysr)/sum(drjfzl),2) else 0 end) drsrl,\r\n" +
                "    sum(ljjfzl) ljjfzl,sum(ljhysr) ljhysr,(case when abs(sum(ljjfzl))>0 then round(sum(ljhysr)/sum(ljjfzl),2) else 0 end) ljsrl,\r\n" +
                "    sum(rjjfzl) rjjfzl,sum(rjhysr) rjhysr,(case when abs(sum(rjjfzl))>0 then round(sum(rjhysr)/sum(rjjfzl),2) else 0 end) rjsrl\r\n" +
                "    from cyfx_rb1_hy_temp \r\n" +
                "    where nd=" + nd + "\r\n" +
                "    group by nd,xh\r\n" +
                "  ) b\r\n" +
                "  on a.id=b.xh\r\n" +
                "  left join\r\n" +
                "  (\r\n" +
                "    select id,gzl,je,srl,\r\n" +
                "    round(gzl/" + yearDays + "*" + days + ",2) as ljgzl,round(je/" + yearDays + "*" + days + ",2) as ljje,srl as ljsrl,\r\n" +
                "    round(gzl/" + yearDays + ",2) as rjgzl,round(je/" + yearDays + ",2) as rjje,srl as rjsrl\r\n" +
                "    from hdy_rb1_ndjh\r\n" +
                "    where nd=" + nd + "\r\n" +
                "  ) c\r\n" +
                "  on a.id=c.id\r\n" +
                "  left join\r\n" +
                "  (\r\n" +
                "    select nd,xh,\r\n" +
                "    sum(drrs) drrs_s,sum(drkysr) drkysr_s,(case when abs(sum(drrs))>0 then round(sum(drkysr)/sum(drrs),2) else 0 end) drsrl_s,\r\n" +
                "    sum(ljrs) ljrs_s,sum(ljkysr) ljkysr_s,(case when abs(sum(ljrs))>0 then round(sum(ljkysr)/sum(ljrs),2) else 0 end) ljsrl_s,\r\n" +
                "    sum(rjrs) rjrs_s,sum(rjkysr) rjkysr_s,(case when abs(sum(rjrs))>0 then round(sum(rjkysr)/sum(rjrs),2) else 0 end) rjsrl_s\r\n" +
                "    from cyfx_rb1_ky_temp \r\n" +
                "    where nd=" + lastYear + "\r\n" +
                "    group by nd,xh\r\n" +
                "    union all\r\n" +
                "    select nd,xh,\r\n" +
                "    sum(drjfzl) drjfzl,sum(drhysr) drhysr,(case when abs(sum(drjfzl))>0 then round(sum(drhysr)/sum(drjfzl),2) else 0 end) drsrl,\r\n" +
                "    sum(ljjfzl) ljjfzl,sum(ljhysr) ljhysr,(case when abs(sum(ljjfzl))>0 then round(sum(ljhysr)/sum(ljjfzl),2) else 0 end) ljsrl,\r\n" +
                "    sum(rjjfzl) rjjfzl,sum(rjhysr) rjhysr,(case when abs(sum(rjjfzl))>0 then round(sum(rjhysr)/sum(rjjfzl),2) else 0 end) rjsrl\r\n" +
                "    from cyfx_rb1_hy_temp \r\n" +
                "    where nd=" + lastYear + "\r\n" +
                "    group by nd,xh\r\n" +
                "  ) d\r\n" +
                "  on a.id=d.xh\r\n" +
                "  where a.id<=20\r\n" +
                "  union all\r\n" +
                "  select a.ID,a.MC,a.DW,\r\n" +
                "  b.DRRS,b.DRKYSR,b.DRSRL,b.LJRS,b.LJKYSR,b.LJSRL,b.RJRS,b.RJKYSR,b.RJSRL,\r\n" +
                "  c.gzl as gzl_jh,c.je as je_jh,c.srl as srl_jh,c.ljgzl as ljgzl_jh,c.ljje as ljje_jh,c.ljsrl as ljsrl_jh,c.rjgzl as rjgzl_jh,c.rjje as rjje_jh,c.rjsrl as rjsrl_jh, \r\n" +
                "  d.DRRS_s,d.DRKYSR_s,d.DRSRL_s,d.LJRS_s,d.LJKYSR_s,d.LJSRL_s,d.RJRS_s,d.RJKYSR_s,d.RJSRL_s\r\n" +
                "  from hdy_rb1 a\r\n" +
                "  left join\r\n" +
                "  (\r\n" +
                "    select nd,xh,drrs,drkysr,drsrl,ljrs,ljkysr,ljsrl,rjrs,rjkysr,rjsrl from cyfx_rb1_ky_temp where nd=" + nd + "\r\n" +
                "    union all\r\n" +
                "    select nd,xh,drjfzl,drhysr,drsrl,ljjfzl,ljhysr,ljsrl,rjjfzl,rjhysr,rjsrl from cyfx_rb1_hy_temp where nd=" + nd + "\r\n" +
                "  ) b\r\n" +
                "  on a.id=b.xh\r\n" +
                "  left join\r\n" +
                "  (\r\n" +
                "    select id,gzl,je,srl,\r\n" +
                "    round(gzl/" + yearDays + "*" + days + ",2) as ljgzl,round(je/" + yearDays + "*" + days + ",2) as ljje,srl as ljsrl,\r\n" +
                "    round(gzl/" + yearDays + ",2) as rjgzl,round(je/" + yearDays + ",2) as rjje,srl as rjsrl\r\n" +
                "    from hdy_rb1_ndjh\r\n" +
                "    where nd=" + nd + "\r\n" +
                "  ) c\r\n" +
                "  on a.id=c.id\r\n" +
                "  left join\r\n" +
                "  (\r\n" +
                "    select nd,xh,drrs drrs_s,drkysr drkysr_s,drsrl drsrl_s,\r\n" +
                "    ljrs ljrs_s,ljkysr ljkysr_s,ljsrl ljsrl_s,\r\n" +
                "    rjrs rjrs_s,rjkysr rjkysr_s,rjsrl rjsrl_s from cyfx_rb1_ky_temp where nd=" + lastYear + "\r\n" +
                "    union all\r\n" +
                "    select nd,xh,drjfzl,drhysr,drsrl,ljjfzl,ljhysr,ljsrl,rjjfzl,rjhysr,rjsrl from cyfx_rb1_hy_temp where nd=" + lastYear + "\r\n" +
                "  ) d\r\n" +
                "  on a.id=d.xh\r\n" +
                "  where a.id>20\r\n" +
                ")\r\n" +
                "order by id";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getRb2(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String cz = "";
        String skzdbm = "";
        String dw = searchInfo.getDw();
        String czs = searchInfo.getCz();
        int flag = 0;
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            int id = Integer.parseInt(dw);
            if (id >= 600 && id <= 699) {//查询车务段，只有客运
                flag = 1;
            }
            if (id >= 800 && id <= 899) {//查询货运中心，只有货运
                flag = 2;
            }
            if (czs != null && !czs.equals("") && !czs.equals("0")) {//选择单个站
                cz = " and czid ='" + searchInfo.getCz() + "'";
                skzdbm = " and skzdbm ='" + searchInfo.getCz() + "'";
            } else {//选择单位、未选择车站、查询单位下面所有站
                cz = " and instr((select fwdbm from srfx_zmk where id =" + dw + "),czid )>0";
                skzdbm = " and instr((select fwdbm from srfx_zmk where id =" + dw + "),skzdbm )>0";
            }
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String nd = ksrq.substring(0, 4);//nd
        //上年年度yyyy
        int year = Integer.parseInt(nd) - 1;
        String lastYear = "" + year;
        String lasttime = lastYear + jsrq.substring(4, 8);//yyyymmdd 上年结束
        String lasttime1 = lastYear + ksrq.substring(4, 8);//yyyymmdd 上年开始
        //获取起始天数的集合
        List<String> dayList = DateUtil.getDayList(searchInfo.getKSRQ().replace("-", ""), searchInfo.getJSRQ().replace("-", ""));
        int yearDays = DateUtil.getDayList(nd + "0101", nd + "1231").size();
        List<String> lastDayList = DateUtil.getDayList(lastYear + "0101", lasttime);
        //获取天数
        int day = dayList.size();
        int lastDay = lastDayList.size();
        String days = day + "";
        String lastDays = lastDay + "";
        //调用方法查询本年和上年结果
        insertRb2_tem(nd, days, jsrq, ksrq, cz, skzdbm, flag);//本年度
        insertRb2_tem(lastYear, lastDays, lasttime, lasttime1, cz, skzdbm, flag);//上年度同期
        String sql = "select ID,MC,DW,\r\n" +
                "DRRS,DRKYSR,DRSRL,LJRS,LJKYSR,LJSRL,RJRS,RJKYSR,RJSRL,\r\n" +
                "gzl_jh,je_jh,srl_jh,ljgzl_jh,ljje_jh,ljsrl_jh,rjgzl_jh,rjje_jh,rjsrl_jh, \r\n" +
                "DRRS_s,DRKYSR_s,DRSRL_s,LJRS_s,LJKYSR_s,LJSRL_s,RJRS_s,RJKYSR_s,RJSRL_s,\r\n" +
                "--年度计划增减\r\n" +
                "(drrs-rjgzl_jh) as drgzl_ndzj,(drkysr-rjje_jh) as drje_ndzj,(case when drrs-rjgzl_jh>0 then round((drkysr-rjje_jh)/(drrs-rjgzl_jh),2) else 0 end) as drsrl_ndzj,\r\n" +
                "(ljrs-ljgzl_jh) as ljgzl_ndzj,(ljkysr-ljje_jh) as ljje_ndzj,(case when ljrs-ljgzl_jh>0 then round((ljkysr-ljje_jh)/(ljrs-ljgzl_jh),2) else 0 end) as ljsrl_ndzj,\r\n" +
                "(rjrs-rjgzl_jh) as rjgzl_ndzj,(rjkysr-rjje_jh) as rjje_ndzj,(case when rjrs-rjgzl_jh>0 then round((rjkysr-rjje_jh)/(rjrs-rjgzl_jh),2) else 0 end) as rjsrl_ndzj,\r\n" +
                "--同比增减\r\n" +
                "(drrs-drrs_s) as drgzl_tbzj,(drkysr-drkysr_s) as drkysr_tbzj,(case when drrs-drrs_s>0 then round((drkysr-drkysr_s)/(drrs-drrs_s),2) else 0 end) as drsrl_tbzj,\r\n" +
                "(ljrs-ljrs_s) as ljgzl_tbzj,(ljkysr-ljkysr_s) as ljkysr_tbzj,(case when ljrs-ljrs_s>0 then round((ljkysr-ljkysr_s)/(ljrs-ljrs_s),2) else 0 end) as ljsrl_tbzj,\r\n" +
                "(rjrs-rjrs_s) as rjgzl_tbzj,(rjkysr-rjkysr_s) as rjkysr_tbzj,(case when rjrs-rjrs_s>0 then round((rjkysr-rjkysr_s)/(rjrs-rjrs_s),2) else 0 end) as rjsrl_tbzj\r\n" +
                "from\r\n" +
                "(\r\n" +
                "  select a.ID,a.MC,a.DW,\r\n" +
                "  round(b.DRRS/" + jldw + ",2) drrs,round(b.DRKYSR/" + jldw + ",2) DRKYSR,b.DRSRL,round(b.LJRS/" + jldw + ",2) ljrs,round(b.LJKYSR/" + jldw + ",2) ljkysr,b.LJSRL,round(b.RJRS/" + jldw + ",2) rjrs,round(b.RJKYSR/" + jldw + ",2) rjkysr,b.RJSRL,\r\n" +
                "  c.gzl as gzl_jh,c.je as je_jh,c.srl as srl_jh,c.ljgzl as ljgzl_jh,c.ljje as ljje_jh,c.ljsrl as ljsrl_jh,c.rjgzl as rjgzl_jh,c.rjje as rjje_jh,c.rjsrl as rjsrl_jh, \r\n" +
                "  round(d.DRRS_s/" + jldw + ",2) DRRS_s,round(d.DRKYSR_s/" + jldw + ",2) DRKYSR_s,d.DRSRL_s,round(d.LJRS_s/" + jldw + ",2) LJRS_s,round(d.LJKYSR_s/" + jldw + ",2)LJKYSR_s,d.LJSRL_s,round(d.RJRS_s/" + jldw + ",2)RJRS_s,round(d.RJKYSR_s/" + jldw + ",2)RJKYSR_s,d.RJSRL_s\r\n" +
                "  from hdy_rb2 a\r\n" +
                "  left join\r\n" +
                "  (\r\n" +
                "    select nd,xh,\r\n" +
                "    sum(drrs) drrs,sum(drkysr) drkysr,(case when abs(sum(drrs))>0 then round(sum(drkysr)/sum(drrs),2) else 0 end) drsrl,\r\n" +
                "    sum(ljrs) ljrs,sum(ljkysr) ljkysr,(case when abs(sum(ljrs))>0 then round(sum(ljkysr)/sum(ljrs),2) else 0 end) ljsrl,\r\n" +
                "    sum(rjrs) rjrs,sum(rjkysr) rjkysr,(case when abs(sum(rjrs))>0 then round(sum(rjkysr)/sum(rjrs),2) else 0 end) rjsrl \r\n" +
                "    from cyfx_rb2_ky_temp \r\n" +
                "    where nd=" + nd + "\r\n" +
                "    group by nd,xh\r\n" +
                "    union all\r\n" +
                "    select * from cyfx_rb2_hy_temp where nd=" + nd + "\r\n" +
                "  ) b\r\n" +
                "  on a.id=b.xh \r\n" +
                "  left join\r\n" +
                "  (\r\n" +
                "    select id,gzl,je,srl,\r\n" +
                "    round(gzl/" + yearDays + "*" + days + ",2) as ljgzl,round(je/" + yearDays + "*" + days + ",2) as ljje,srl as ljsrl,\r\n" +
                "    round(gzl/" + yearDays + ",2) as rjgzl,round(je/" + yearDays + ",2) as rjje,srl as rjsrl\r\n" +
                "    from hdy_rb2_ndjh\r\n" +
                "    where nd=" + nd + "\r\n" +
                "  ) c\r\n" +
                "  on a.id=c.id\r\n" +
                "  left join\r\n" +
                "  (\r\n" +
                " select nd,xh,\r\n" +
                "    sum(drrs) drrs_s,sum(drkysr) drkysr_s,(case when abs(sum(drrs))>0 then round(sum(drkysr)/sum(drrs),2) else 0 end) drsrl_s,\r\n" +
                "    sum(ljrs) ljrs_s,sum(ljkysr) ljkysr_s,(case when abs(sum(ljrs))>0 then round(sum(ljkysr)/sum(ljrs),2) else 0 end) ljsrl_s,\r\n" +
                "    sum(rjrs) rjrs_s,sum(rjkysr) rjkysr_s,(case when abs(sum(rjrs))>0 then round(sum(rjkysr)/sum(rjrs),2) else 0 end) rjsrl_s\r\n" +
                "    from cyfx_rb2_ky_temp \r\n" +
                "    where nd=" + lastYear + "\r\n" +
                "    group by nd,xh\r\n" +
                "    union all\r\n" +
                "    select * from cyfx_rb2_hy_temp where nd=" + lastYear + "\r\n" +
                "  ) d\r\n" +
                "  on a.id=d.xh\r\n" +
                ") order by id";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getRb3(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String cz = "";
        String dw = searchInfo.getDw();
        String czs = searchInfo.getCz();
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            if (czs != null && !czs.equals("") && !czs.equals("0")) {//选择单个站
                cz = "and 电报码 ='" + searchInfo.getCz() + "'";
            } else {//选择单位、未选择车站、查询单位下面所有站
                cz = "and instr((select fwdbm from srfx_zmk where id =" + dw + "),电报码 )>0";
            }
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String year = ksrq.substring(0, 4);
        String tableName = "srfx_b561_" + year;
        String sql = "delete from CYFX_RB3";
        jdbcTemplate.update(sql);
        sql = "insert into CYFX_RB3\r\n" +
                "select a.hh,a.pm,\r\n" +
                "sum(b.jfzl_gn) as jfzl_gn,sum(b.jfzl_zt) as jfzl_zt,sum(b.jfzl) as jfzl,\r\n" +
                "sum(b.zzl_gn) as zzl_gn,sum(b.zzl_zt) as zzl_zt,sum(b.zzl) as zzl,\r\n" +
                "sum(b.hysr_gn) as hysr_gn,sum(b.hysr_zt) as hysr_zt,sum(b.hysr) as hysr,\r\n" +
                "sum(b.yf_gn) as yf_gn,sum(b.yf_zt) as yf_zt,sum(b.yf) as yf,\r\n" +
                "sum(b.zf_gn) as zf_gn,sum(b.zf_zt) as zf_zt,sum(b.zf) as zf,\r\n" +
                "sum(b.jsjj_gn) as jsjj_gn,sum(b.jsjj_zt) as jsjj_zt,sum(b.jsjj) as jsjj\r\n" +
                "from HDY_PL a\r\n" +
                "left join\r\n" +
                "(\r\n" +
                "  select c.hh,\r\n" +
                "  (case when c.fj=c.dj then jfzl end) jfzl_gn, (case when c.fj<>c.dj then jfzl end) jfzl_zt,jfzl,\r\n" +
                "  (case when c.fj=c.dj then zzl end) zzl_gn, (case when c.fj<>c.dj then zzl end) zzl_zt,zzl,\r\n" +
                "  (case when c.fj=c.dj then hysr end) hysr_gn, (case when c.fj<>c.dj then hysr end) hysr_zt,hysr,\r\n" +
                "  (case when c.fj=c.dj then yf end) yf_gn, (case when c.fj<>c.dj then yf end) yf_zt,yf,\r\n" +
                "  (case when c.fj=c.dj then zf end) zf_gn, (case when c.fj<>c.dj then zf end) zf_zt,zf,\r\n" +
                "  (case when c.fj=c.dj then jsjj end) jsjj_gn, (case when c.fj<>c.dj then jsjj end) jsjj_zt,jsjj\r\n" +
                "  from \r\n" +
                "  (\r\n" +
                "    select a.*,b.ssljdm as dj from \r\n" +
                "    (\r\n" +
                "      select (case when instr(票据种类,'特货') > 0 then '31' when instr(运价号,'箱') >0 then '29' when (substr(品类代码,1,2)='99' or substr(品类代码,1,2)='W') then '26' else substr(品类代码,1,2) end) hh,\r\n" +
                "      t.发站企业代码 as fzqy,t.到站企业代码 as dzqy,\r\n" +
                "      round(sum(t.计费重量)/1000,2) as jfzl,\r\n" +
                "      round(sum(t.站间计费吨公里)/1000,2) as zzl,\r\n" +
                "      sum(t.全程运费) as hysr,\r\n" +
                "      sum(t.国铁运费+t.股改公司运费+t.控股公司运费+t.地铁运费+t.电气化附加费) as yf,\r\n" +
                "      sum(t.建设基金) as jsjj,\r\n" +
                "      sum(t.货运杂费+t.装卸费+t.印花费+t.保价款) as zf,\r\n" +
                "      t1.ssljdm as fj\r\n" +
                "      from " + tableName + " t,SRFX_QYDM t1\n" +
                "      where t.发站企业代码=t1.qydm\r\n" +
                "      and 日期>=" + ksrq + " and 日期 <= " + jsrq + cz + " \n" +
                "      group by (case when instr(票据种类,'特货') > 0 then '31' when instr(运价号,'箱') >0 then '29' when (substr(品类代码,1,2)='99' or substr(品类代码,1,2)='W') then '26' else substr(品类代码,1,2) end),\r\n" +
                "      t.发站企业代码,t.到站企业代码,t1.ssljdm\r\n" +
                "    ) a,SRFX_QYDM b\r\n" +
                "    where a.dzqy = b.qydm\r\n" +
                "  ) c\r\n" +
                ") b\r\n" +
                "on a.pmdm=b.hh\r\n" +
                "where a.hh<>30\r\n" +
                "group by a.hh,a.pm";
        jdbcTemplate.update(sql);
        sql = "insert into CYFX_RB3\r\n" +
                "select 30,'合计',sum(JFZL_GN),sum(JFZL_ZT),sum(JFZL),sum(ZZL_GN),sum(ZZL_ZT),sum(ZZL),sum(HYSR_GN),sum(HYSR_ZT),sum(HYSR),sum(YF_GN),sum(YF_ZT),sum(YF),\r\n" +
                "sum(ZF_GN),sum(ZF_ZT),sum(ZF),sum(JSJJ_GN),sum(JSJJ_ZT),sum(JSJJ)\r\n" +
                "from CYFX_RB3";
        jdbcTemplate.update(sql);
        sql = "select hh,pm,round(jfzl_gn/" + jldw + ",2) jfzl_gn,round(jfzl_zt/" + jldw + ",2)jfzl_zt,round(jfzl/" + jldw + ",2)jfzl,round(zzl_gn/" + jldw + ",2)zzl_gn,round(zzl_zt/" + jldw + ",2)zzl_zt,round(zzl/" + jldw + ",2)zzl,\r\n" +
                "(case when jfzl_gn > 0 then round(zzl_gn/jfzl_gn,0) else 0 end) as pjyj_gn,\r\n" +
                "(case when jfzl_zt > 0 then round(zzl_zt/jfzl_zt,0) else 0 end) as pjyj_zt,\r\n" +
                "(case when jfzl > 0 then round(zzl/jfzl,0) else 0 end) as pjyj,\r\n" +
                "round(hysr_gn/" + jldw + ",2)hysr_gn,round(hysr_zt/" + jldw + ",2)hysr_zt,round(hysr/" + jldw + ",2)hysr,\r\n" +
                "round(yf_gn/" + jldw + ",2)yf_gn,round(yf_zt/" + jldw + ",2)yf_zt,round(yf/" + jldw + ",2)yf,\r\n" +
                "round(zf_gn/" + jldw + ",2)zf_gn,round(zf_zt/" + jldw + ",2)zf_zt,round(zf/" + jldw + ",2)zf,\r\n" +
                "round(jsjj_gn/" + jldw + ",2)jsjj_gn,round(jsjj_zt/" + jldw + ",2)jsjj_zt,round(jsjj/" + jldw + ",2)jsjj,\r\n" +
                "--吨收入率、吨公里收入率,使用的是运费\r\n" +
                "(case when jfzl_gn > 0 then round(yf_gn/jfzl_gn,0) else 0 end) as dsrl_gn,\r\n" +
                "(case when jfzl_zt > 0 then round(yf_zt/jfzl_zt,0) else 0 end) as dsrl_zt,\r\n" +
                "(case when jfzl > 0 then round(yf/jfzl,0) else 0 end) as dsrl,\r\n" +
                "(case when zzl_gn > 0 then round(yf_gn/zzl_gn,2) else 0 end) as dglsrl_gn,\r\n" +
                "(case when zzl_zt > 0 then round(yf_zt/zzl_zt,2) else 0 end) as dglsrl_zt,\r\n" +
                "(case when zzl > 0 then round(yf/zzl,2) else 0 end) as dglsrl\r\n" +
                "from CYFX_RB3 \r\n" +
                "order by hh";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getRb4(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String nd = ksrq.substring(0, 4);//nd
        //上年年度yyyy
        int year = Integer.parseInt(nd) - 1;
        String lastYear = "" + year;
        String lasttime = lastYear + jsrq.substring(4, 8);//yyyymmdd 上年结束
        String lasttime1 = lastYear + ksrq.substring(4, 8);//yyyymmdd 上年开始
        //获取起始天数的集合
        List<String> dayList = DateUtil.getDayList(searchInfo.getKSRQ().replace("-", ""), searchInfo.getJSRQ().replace("-", ""));
        int yearDays = DateUtil.getDayList(nd + "0101", nd + "1231").size();
        List<String> lastDayList = DateUtil.getDayList(lastYear + "0101", lasttime);
        //获取天数
        int day = dayList.size();
        int lastDay = lastDayList.size();
        String days = day + "";
        String lastDays = lastDay + "";
        //调用方法查询本年和上年结果
        insertRb4_tem(nd, days, jsrq, ksrq);//本年度
        insertRb4_tem(lastYear, lastDays, lasttime, lasttime1);//上年度同期
        String sql = "select a.xh,a.mc,a.dw,\r\n" +
                "b.DRGZL,b.DRJE,b.DRSRL,b.LJGZL,b.LJJE,b.LJSRL,b.RJGZL,b.RJJE,b.RJSRL,"
                + "b.GZL,b.JE,b.SRL,b.LJGZL_JH,b.LJJE_JH,b.LJSRL_JH,b.RJGZL_JH,b.RJJE_JH,"
                + "b.RJSRL_JH,b.DRGZL_S,b.DRJE_S,b.DRSRL_S,b.LJGZL_S,b.LJJE_S,b.LJSRL_S,b.RJGZL_S,b.RJJE_S,b.RJSRL_S\r\n" +
                "from hdy_rb4 a\r\n" +
                "left join" +
                "(select a.xh,a.mc,round(a.drgzl/" + jldw + ",2)drgzl,round(a.drje/" + jldw + ",2)drje,a.drsrl,round(a.ljgzl/" + jldw + ",2)ljgzl,round(a.ljje/" + jldw + ",2)ljje,a.ljsrl,round(a.rjgzl/" + jldw + ",2)rjgzl,round(a.rjje/" + jldw + ",2)rjje,a.rjsrl,\r\n" +
                "b.GZL,b.JE,b.SRL,b.LJGZL_jh,b.LJJE_jh,b.LJSRL_jh,b.RJGZL_jh,b.RJJE_jh,b.RJSRL_jh,\r\n" +
                "round(c.DRGZL_S/" + jldw + ",2)DRGZL_S,round(c.DRJE_S/" + jldw + ",2)DRJE_S,c.DRSRL_S,round(c.LJGZL_S/" + jldw + ",2)LJGZL_S,round(c.LJJE_S/" + jldw + ",2)LJJE_S,c.LJSRL_S,round(c.RJGZL_S/" + jldw + ",2)RJGZL_S,round(c.RJJE_S/" + jldw + ",2)RJJE_S,c.RJSRL_S\r\n" +
                "from\r\n" +
                "(\r\n" +
                "select xh,mc,sum(drrs) drgzl,sum(drpjhj) drje,sum(drsrl) drsrl,sum(ljrs) ljgzl,sum(ljpjhj) ljje,sum(ljsrl) ljsrl,sum(rjrs) rjgzl,sum(rjpjhj) rjje,sum(rjsrl) rjsrl from cyfx_rb4_temp\r\n" +
                "where nd='" + nd + "'\r\n" +
                "group by xh,mc\r\n" +
                ") a\r\n" +
                "left join\r\n" +
                "(\r\n" +
                "select xh,gzl,je,srl,\r\n" +
                "round(gzl/" + yearDays + "*" + days + ",2) as ljgzl_jh,round(je/" + yearDays + "*" + days + ",2) as ljje_jh,srl as ljsrl_jh,\r\n" +
                "round(gzl/" + yearDays + ",2) as rjgzl_jh,round(je/" + yearDays + ",2) as rjje_jh,srl as rjsrl_jh\r\n" +
                "from hdy_rb4_ndjh\r\n" +
                "where nd=" + nd + "\r\n" +
                ") b\r\n" +
                "on a.xh=b.xh\r\n" +
                "left join\r\n" +
                "(\r\n" +
                "select xh,sum(drrs) drgzl_s,sum(drpjhj) drje_s,sum(drsrl) drsrl_s,sum(ljrs) ljgzl_s,sum(ljpjhj) ljje_s,sum(ljsrl) ljsrl_s,sum(rjrs) rjgzl_s,sum(rjpjhj) rjje_s,sum(rjsrl) rjsrl_s from cyfx_rb4_temp\r\n" +
                "where nd='" + lastYear + "'\r\n" +
                "group by xh\r\n" +
                ") c\r\n" +
                "on a.xh=c.xh) b\r\n" +
                "on a.xh = b.xh\r\n" +
                "order by a.xh";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public void insertRb1_tem(String nd, String days, String jsrq, String ksrq, String cz, String skzdbm, int flag) {
        //根据年度删除
        String sql = "delete from cyfx_rb1_ky_temp where nd=" + nd;
        jdbcTemplate.update(sql);
        //无论选择车务段与否、货运数据都要删除
        sql = "delete from cyfx_rb1_hy_temp where nd=" + nd;
        jdbcTemplate.update(sql);
        if (flag == 1) {
            insertRb1_KYtem(nd, days, jsrq, ksrq, cz, skzdbm);
        } else if (flag == 2) {
            insertRb1_HYtem(nd, days, jsrq, ksrq, cz, skzdbm);
        } else {
            insertRb1_KYtem(nd, days, jsrq, ksrq, cz, skzdbm);
            insertRb1_HYtem(nd, days, jsrq, ksrq, cz, skzdbm);
        }
    }

    @Override
    public void insertRb2_tem(String nd, String days, String jsrq, String ksrq, String cz, String skzdbm, int flag) {
        //根据年度删除
        String sql = "delete from cyfx_rb2_ky_temp where nd=" + nd;
        jdbcTemplate.update(sql);
        sql = "delete from cyfx_rb2_hy_temp where nd=" + nd;
        jdbcTemplate.update(sql);
        if (flag == 1) {
            insertRb2_KYtem(nd, days, jsrq, ksrq, cz, skzdbm);
        } else if (flag == 2) {
            insertRb2_HYtem(nd, days, jsrq, ksrq, cz, skzdbm);
        } else {
            insertRb2_KYtem(nd, days, jsrq, ksrq, cz, skzdbm);
            insertRb2_HYtem(nd, days, jsrq, ksrq, cz, skzdbm);
        }
    }

    @Override
    public void insertRb4_tem(String nd, String days, String jsrq, String ksrq) {
        String sql = "delete from cyfx_rb4_temp where nd=" + nd;
        jdbcTemplate.update(sql);
        sql = "insert into cyfx_rb4_temp\r\n" +
                "--create table cyfx_rb4_temp as\r\n" +
                "select '" + nd + "'as nd,a.xh,a.mc,b.drrs,b.drpjhj,b.drsrl,c.ljrs,c.ljpjhj,c.ljsrl,d.rjrs,d.rjpjhj,d.rjsrl from hdy_rb4 a\r\n" +
                "left join\r\n" +
                "(\r\n" +
                "  select kyd,sum(rs) drrs,sum(pjhj) drpjhj,round(sum(rs)/sum(pjhj),2) drsrl from cyfx_rb_ky\r\n" +
                "  where ccrq='" + jsrq + "'\r\n" +
                "  group by kyd\r\n" +
                ") b\r\n" +
                "on a.dzmc=b.kyd\r\n" +
                "left join\r\n" +
                "(\r\n" +
                "  select kyd,sum(rs) ljrs,sum(pjhj) ljpjhj,round(sum(rs)/sum(pjhj),2) ljsrl from cyfx_rb_ky\r\n" +
                "  where ccrq>='" + ksrq + "' and ccrq<'" + jsrq + "'\r\n" +
                "  group by kyd\r\n" +
                ") c\r\n" +
                "on a.dzmc=c.kyd\r\n" +
                "left join\r\n" +
                "(\r\n" +
                "  select kyd,round(sum(rs)/" + days + ",2) rjrs,round(sum(pjhj)/" + days + ",2) rjpjhj,round(sum(rs)/sum(pjhj),2) rjsrl from cyfx_rb_ky\r\n" +
                "  where ccrq>='" + ksrq + "' and ccrq<'" + jsrq + "'\r\n" +
                "  group by kyd\r\n" +
                ") d\r\n" +
                "on a.dzmc=d.kyd\r\n" +
                "union all\r\n" +
                "select '" + nd + "'as nd,a.xh,a.mc,\r\n" +
                "b.drrs,b.drpjhj,b.drsrl,b.ljrs,b.ljpjhj,b.ljsrl,b.rjrs,b.rjpjhj,b.rjsrl from\r\n" +
                "hdy_rb4 a\r\n" +
                "left join \r\n" +
                "(\r\n" +
                "  select * from \r\n" +
                "  (select '异地票' as kyd,sum(rs) drrs,sum(pjhj) drpjhj,round(sum(rs)/sum(pjhj),2) drsrl from cyfx_rb_ky where ccrq='" + ksrq + "' and fzj<>'R') a\r\n" +
                "  left join\r\n" +
                "  (select sum(rs) ljrs,sum(pjhj) ljpjhj,round(sum(rs)/sum(pjhj),2) ljsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<'" + ksrq + "' and fzj<>'R') b\r\n" +
                "  on 1=1\r\n" +
                "  left join\r\n" +
                "  (select round(sum(rs)/" + days + ",2) rjrs,round(sum(pjhj)/" + days + ",2) rjpjhj,round(sum(rs)/sum(pjhj),2) rjsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<'" + jsrq + "' and fzj<>'R')c\r\n" +
                "  on 1=1\r\n" +
                ") b\r\n" +
                "on a.dzmc=b.kyd\r\n" +
                "where a.xh=22";
        jdbcTemplate.update(sql);
        sql = "insert into cyfx_rb4_temp\r\n" +
                "select '" + nd + "'as nd,a.xh,a.mc,b.drgzl,b.drje,b.drsrl,c.ljgzl,c.ljje,c.ljsrl,d.rjgzl,d.rjje,d.rjsrl from hdy_rb4 a\r\n" +
                "left join\r\n" +
                "(\r\n" +
                "select mc,sum(gzl) drgzl,sum(je) drje,round(sum(je)/sum(gzl),2) drsrl from cyfx_rb4_hy\r\n" +
                "where rq=" + jsrq + "\r\n" +
                "group by mc\r\n" +
                ") b\r\n" +
                "on a.dzmc=b.mc\r\n" +
                "left join\r\n" +
                "(\r\n" +
                "select mc,sum(gzl) ljgzl,sum(je) ljje,round(sum(je)/sum(gzl),2) ljsrl from cyfx_rb4_hy\r\n" +
                "where rq>=" + ksrq + " and rq<" + jsrq + "\r\n" +
                "group by mc\r\n" +
                ") c\r\n" +
                "on a.dzmc=c.mc\r\n" +
                "left join\r\n" +
                "(\r\n" +
                "select mc,round(sum(gzl)/" + days + ",2) rjgzl,round(sum(je)/" + days + ",2) rjje,round(sum(je)/sum(gzl),2) rjsrl from cyfx_rb4_hy\r\n" +
                "where rq>=" + ksrq + "  and rq<" + jsrq + " \r\n" +
                "group by mc\r\n" +
                ") d\r\n" +
                "on a.dzmc=d.mc";
        jdbcTemplate.update(sql);
        sql = "insert into cyfx_rb4_temp\r\n" +
                "select '" + nd + "'nd,11 xh,'阿拉山口站' mc,'',sum(drpjhj),'','',sum(ljpjhj),'','',sum(rjpjhj),'' from cyfx_rb4_temp\r\n" +
                "where nd='" + nd + "'\r\n" +
                "and (xh=12 or xh=13)\r\n" +
                "union all\r\n" +
                "select '" + nd + "'nd,14 xh,'霍尔果斯站' mc,'',sum(drpjhj),'','',sum(ljpjhj),'','',sum(rjpjhj),'' from cyfx_rb4_temp\r\n" +
                "where nd='" + nd + "'\r\n" +
                "and (xh=15 or xh=16)\r\n" +
                "union all\r\n" +
                "select '" + nd + "'nd,23 xh,'集团公司合计' mc,'',sum(drpjhj),'','',sum(ljpjhj),'','',sum(rjpjhj),'' from cyfx_rb4_temp\r\n" +
                "where nd='" + nd + "'\r\n" +
                "and (xh<>11 or xh<>14)";
        jdbcTemplate.update(sql);
    }

    /**
     * 日报一客运货运
     *
     * @param nd
     * @param days
     * @param jsrq
     * @param ksrq
     * @param cz
     * @param skzdbm
     */
    public void insertRb1_KYtem(String nd, String days, String jsrq, String ksrq, String cz, String skzdbm) {
        String sql = "insert into cyfx_rb1_ky_temp\n" +
                "--create table cyfx_rb1_ky_temp as\n" +
                "select " + nd + " as nd,t.* from \n" +
                "(\n" +
                "  select 2 as xh,a.*,b.*,c.* from\n" +
                "  (select sum(rs) drrs,sum(pjhj) drkysr,(case when sum(rs)>0 then round(sum(pjhj)/sum(rs),2) else 0 end) drsrl from cyfx_rb_ky where ccrq='" + jsrq + "' " + cz + ") a\n" +
                "  left join\n" +
                "  (select sum(rs) ljrs,sum(pjhj) ljkysr,(case when sum(rs)>0 then round(sum(pjhj)/sum(rs),2) else 0 end) ljsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' " + cz + ") b\n" +
                "  on 1=1\n" +
                "  left join\n" +
                "  (select round(sum(rs)/" + days + ",2) rjrs,round(sum(pjhj)/" + days + ") rjkysr,(case when sum(rs)>0 then round(sum(pjhj)/sum(rs),2) else 0 end) rjsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' " + cz + ") c\n" +
                "  on 1=1\n" +
                "  union all\n" +
                "  --  1.旅客票价收入\n" +
                "  select 3,a.*,b.*,c.*from\n" +
                "  (select sum(rs) drrs,sum(jcpj) drjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) drsrl from cyfx_rb_ky where ccrq='" + jsrq + "' " + cz + ") a\n" +
                "  left join\n" +
                "  (select sum(rs) ljrs,sum(jcpj) ljjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) ljsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' " + cz + ") b\n" +
                "  on 1=1\n" +
                "  left join\n" +
                "  (select round(sum(rs)/" + days + ",2) rjrs,round(sum(jcpj)/" + days + ") rjjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) rjsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' " + cz + ") c\n" +
                "  on 1=1\n" +
                "  union all\n" +
                "  --（1）发售客票收入\n" +
                "  --其中：管内\n" +
                "  --      直通\n" +
                "  select 4,a.*,b.*,c.*from\n" +
                "  (select sum(rs) drrs,sum(jcpj) drjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) drsrl from cyfx_rb_ky where ccrq='" + jsrq + "' and fzj='R' " + cz + ") a\n" +
                "  left join\n" +
                "  (select sum(rs) ljrs,sum(jcpj) ljjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) ljsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' and fzj='R'  " + cz + ") b\n" +
                "  on 1=1\n" +
                "  left join\n" +
                "  (select round(sum(rs)/" + days + ",2) rjrs,round(sum(jcpj)/" + days + ") rjjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) rjsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' and fzj='R'  " + cz + ") c\n" +
                "  on 1=1\n" +
                "  union all\n" +
                "  select 5,a.*,b.*,c.*from\n" +
                "  (select sum(rs) drrs,sum(jcpj) drjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) drsrl from cyfx_rb_ky where ccrq='" + jsrq + "' and fzj=dzj and fzj='R' " + cz + ") a\n" +
                "  left join\n" +
                "  (select sum(rs) ljrs,sum(jcpj) ljjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) ljsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' and fzj=dzj and fzj='R' " + cz + ") b\n" +
                "  on 1=1\n" +
                "  left join\n" +
                "  (select round(sum(rs)/" + days + ",2) rjrs,round(sum(jcpj)/" + days + ") rjjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) rjsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' and fzj=dzj and fzj='R' " + cz + ") c\n" +
                "  on 1=1\n" +
                "  union all\n" +
                "  select 6,a.*,b.*,c.*from\n" +
                "  (select sum(rs) drrs,sum(jcpj) drjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) drsrl from cyfx_rb_ky where ccrq='" + jsrq + "' and fzj<>dzj and fzj='R' " + cz + ") a\n" +
                "  left join\n" +
                "  (select sum(rs) ljrs,sum(jcpj) ljjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) ljsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' and fzj<>dzj and fzj='R' " + cz + ") b\n" +
                "  on 1=1\n" +
                "  left join\n" +
                "  (select round(sum(rs)/" + days + ",2) rjrs,round(sum(jcpj)/" + days + ") rjjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) rjsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' and fzj<>dzj and fzj='R' " + cz + ") c\n" +
                "  on 1=1\n" +
                "  union all\n" +
                "  --（2）异地票\n" +
                "  select 7,a.*,b.*,c.*from\n" +
                "  (select sum(rs) drrs,sum(jcpj) drjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) drsrl from cyfx_rb_ky where ccrq='" + jsrq + "' and fzj<>'R' " + cz + ") a\n" +
                "  left join\n" +
                "  (select sum(rs) ljrs,sum(jcpj) ljjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) ljsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' and fzj<>'R' " + cz + ") b\n" +
                "  on 1=1\n" +
                "  left join\n" +
                "  (select round(sum(rs)/" + days + ",2) rjrs,round(sum(jcpj)/" + days + ") rjjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) rjsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' and fzj<>'R' " + cz + ") c\n" +
                "  on 1=1\n" +
                "  union all\n" +
                "  -- 2.客运其他收入\n" +
                "  select 8,a.*,b.*,c.*from\n" +
                "  (select sum(rs) drrs,sum(wpdpf7+wpdpf3+czktf+rpf) kqtsr,(case when sum(rs)>0 then round(sum(wpdpf7+wpdpf3+czktf+rpf)/sum(rs),2) else 0 end) drsrl from cyfx_rb_ky where ccrq='" + jsrq + "' " + cz + ") a\n" +
                "  left join\n" +
                "  (select sum(rs) ljrs,sum(wpdpf7+wpdpf3+czktf+rpf) ljwpdpf7,(case when sum(rs)>0 then round(sum(wpdpf7+wpdpf3+czktf+rpf)/sum(rs),2) else 0 end) ljsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' " + cz + ") b\n" +
                "  on 1=1\n" +
                "  left join\n" +
                "  (select round(sum(rs)/" + days + ",2) rjrs,round(sum(wpdpf7+wpdpf3+czktf+rpf)/" + days + ") rjwpdpf7,(case when sum(rs)>0 then round(sum(wpdpf7+wpdpf3+czktf+rpf)/sum(rs),2) else 0 end) rjsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' " + cz + ") c\n" +
                "  on 1=1\n" +
                ") t";
        jdbcTemplate.update(sql);
        sql = "insert into cyfx_rb1_ky_temp\n" +
                "select " + nd + " as nd,t.* from \n" +
                "(\n" +
                "  --客杂、进客运总收入\r\n" +
                "select xh,sum(drrs) drrs,sum(drsr) drsr,(case when sum(drrs)>0 then round(sum(drsr)/sum(drrs),2) else 0 end) drsrl,\r\n" +
                "sum(ljrs) ljrs,sum(lj) ljsr,(case when sum(ljrs)>0 then round(sum(lj)/sum(ljrs),2) else 0 end) ljsrl,\r\n" +
                "sum(rjrs) rjrs,sum(rj) rjsr,(case when sum(rjrs)>0 then round(sum(rj)/sum(rjrs),2) else 0 end) rjsrl\r\n" +
                "from\r\n" +
                "(\r\n" +
                "  select 2 as xh,a.*,b.*,c.* from\r\n" +
                "  (select sum(车补人数+站补人数) drrs,sum(车补税后+站补税后) drsr from srfx_b542_" + nd + "d where 日期=" + jsrq + cz.replace("czid", "电报码") + ") a\n" +
                "  left join\r\n" +
                "  (select sum(车补人数+站补人数) ljrs,sum(车补税后+站补税后) lj from srfx_b542_" + nd + "d where 日期>=" + ksrq + " and 日期<=" + jsrq + cz.replace("czid", "电报码") + ") b\n" +
                "  on 1=1\r\n" +
                "  left join\r\n" +
                "  (select round(sum(车补人数+站补人数)/" + days + ",2) rjrs,round(sum(车补税后+站补税后)/" + days + ") rj from srfx_b542_" + nd + "d where 日期>=" + ksrq + "and 日期<=" + jsrq + cz.replace("czid", "电报码") + ") c\n" +
                "  on 1=1\r\n" +
                ")\r\n" +
                "group by xh" +
                ") t";
        jdbcTemplate.update(sql);
        sql = "insert into cyfx_rb1_ky_temp\n" +
                "select " + nd + " as nd,t.* from \n" +
                "(\n" +
                "  --客杂、客其他收入\r\n" +
                "select xh,sum(drrs) drrs,sum(drsr) drsr,(case when sum(drrs)>0 then round(sum(drsr)/sum(drrs),2) else 0 end) drsrl,\r\n" +
                "sum(ljrs) ljrs,sum(lj) ljsr,(case when sum(ljrs)>0 then round(sum(lj)/sum(ljrs),2) else 0 end) ljsrl,\r\n" +
                "sum(rjrs) rjrs,sum(rj) rjsr,(case when sum(rjrs)>0 then round(sum(rj)/sum(rjrs),2) else 0 end) rjsrl\r\n" +
                "from\r\n" +
                "(\r\n" +
                "  select 8 as xh,a.*,b.*,c.* from\r\n" +
                "  (select sum(车补人数+站补人数) drrs,sum(车补税后+站补税后) drsr from srfx_b542_" + nd + "d where 日期=" + jsrq + cz.replace("czid", "电报码") + ") a\n" +
                "  left join\r\n" +
                "  (select sum(车补人数+站补人数) ljrs,sum(车补税后+站补税后) lj from srfx_b542_" + nd + "d where 日期>=" + ksrq + " and 日期<=" + jsrq + cz.replace("czid", "电报码") + ") b\n" +
                "  on 1=1\r\n" +
                "  left join\r\n" +
                "  (select round(sum(车补人数+站补人数)/" + days + ",2) rjrs,round(sum(车补税后+站补税后)/" + days + ") rj from srfx_b542_" + nd + "d where 日期>=" + ksrq + "and 日期<=" + jsrq + cz.replace("czid", "电报码") + ") c\n" +
                "  on 1=1\r\n" +
                ")\r\n" +
                "group by xh" +
                ") t";
        jdbcTemplate.update(sql);
        //形成客运管内、直通占比
        sql = "insert into cyfx_rb1_ky_temp(nd,xh,drrs,drkysr,ljrs,ljkysr,rjrs,rjkysr) \n" +
                "select " + nd + ",23,\n" +
                "round((select  drrs from cyfx_rb1_ky_temp where xh=5 and nd=" + nd + ")/(select drrs from cyfx_rb1_ky_temp where xh=4 and nd=" + nd + "),2),\n" +
                "round((select  drkysr from cyfx_rb1_ky_temp where xh=5 and nd=" + nd + ")/(select drkysr from cyfx_rb1_ky_temp where xh=4 and nd=" + nd + "),2),\n" +
                "round((select  ljrs from cyfx_rb1_ky_temp where xh=5 and nd=" + nd + ")/(select ljrs from cyfx_rb1_ky_temp where xh=4 and nd=" + nd + "),2),\n" +
                "round((select  ljkysr from cyfx_rb1_ky_temp where xh=5 and nd=" + nd + ")/(select ljkysr from cyfx_rb1_ky_temp where xh=4 and nd=" + nd + "),2),\n" +
                "round((select  rjrs from cyfx_rb1_ky_temp where xh=5 and nd=" + nd + ")/(select rjrs from cyfx_rb1_ky_temp where xh=4 and nd=" + nd + "),2),\n" +
                "round((select  rjkysr from cyfx_rb1_ky_temp where xh=5 and nd=" + nd + ")/(select rjkysr from cyfx_rb1_ky_temp where xh=4 and nd=" + nd + "),2)\n" +
                "from dual\r\n" +
                "union all\r\n" +
                "select " + nd + ",24,\n" +
                "round((select  drrs from cyfx_rb1_ky_temp where xh=6 and nd=" + nd + ")/(select drrs from cyfx_rb1_ky_temp where xh=4 and nd=" + nd + "),2),\n" +
                "round((select  drkysr from cyfx_rb1_ky_temp where xh=6 and nd=" + nd + ")/(select drkysr from cyfx_rb1_ky_temp where xh=4 and nd=" + nd + "),2),\n" +
                "round((select  ljrs from cyfx_rb1_ky_temp where xh=6 and nd=" + nd + ")/(select ljrs from cyfx_rb1_ky_temp where xh=4 and nd=" + nd + "),2),\n" +
                "round((select  ljkysr from cyfx_rb1_ky_temp where xh=6 and nd=" + nd + ")/(select ljkysr from cyfx_rb1_ky_temp where xh=4 and nd=" + nd + "),2),\n" +
                "round((select  rjrs from cyfx_rb1_ky_temp where xh=6 and nd=" + nd + ")/(select rjrs from cyfx_rb1_ky_temp where xh=4 and nd=" + nd + "),2),\n" +
                "round((select  rjkysr from cyfx_rb1_ky_temp where xh=6 and nd=" + nd + ")/(select rjkysr from cyfx_rb1_ky_temp where xh=4 and nd=" + nd + "),2)\n" +
                "from dual";
        jdbcTemplate.update(sql);
    }

    public void insertRb1_HYtem(String nd, String days, String jsrq, String ksrq, String cz, String skzdbm) {
        //形成货运汇总结果表、车数单列出来计算
        String sql = "insert into cyfx_rb1_hy_temp\r\n" +
                "select " + nd + "  as nd,d.id,a.drjfzl,a.drhysr,a.drsrl,b.ljjfzl,b.ljhysr,b.ljsrl,c.rjjfzl,c.rjhysr,c.rjsrl from hdy_rb1 d\r\n" +
                "left join\r\n" +
                "(select xh,sum(jfzl) drjfzl,sum(hysr) drhysr,(case when sum(jfzl)>0 then round(sum(hysr)/sum(jfzl),2) else 0 end) drsrl from cyfx_rb1_hy where rq='" + jsrq + "' and xh<>31" + skzdbm + "group by xh) a\n" +
                "on d.id=a.xh\r\n" +
                "left join\r\n" +
                "(select xh,sum(jfzl) ljjfzl,sum(hysr) ljhysr,(case when sum(jfzl)>0 then round(sum(hysr)/sum(jfzl),2) else 0 end) ljsrl from cyfx_rb1_hy where rq>='" + ksrq + "' and rq<='" + jsrq + "' and xh<>31 " + skzdbm + " group by xh) b\n" +
                "on d.id=b.xh\r\n" +
                "left join\r\n" +
                "(select xh,round(sum(jfzl)/" + days + ",2) rjjfzl,round(sum(hysr)/" + days + ",2) rjhysr,(case when sum(jfzl)>0 then round(sum(hysr)/sum(jfzl),2) else 0 end) rjsrl from cyfx_rb1_hy where rq>='" + ksrq + "' and rq<='" + jsrq + "' and xh<>31 " + skzdbm + " group by xh) c\n" +
                "on d.id=c.xh\r\n" +
                "where d.id>=9 and d.id<=20";
        jdbcTemplate.update(sql);
        //处理车数、车收入率
        sql = "insert into cyfx_rb1_hy_temp\r\n" +
                "select " + nd + " as nd,d.id,a.drjfzl,a.drsr,a.drsrl,b.ljjfzl,b.ljhysr,b.ljsrl,c.rjjfzl,c.rjhysr,c.rjsrl from hdy_rb1 d\r\n" +
                "left join\r\n" +
                "(select xh,sum(jfzl) drjfzl,sum(hysr) drsr,(case when sum(jfzl)>0 then round(sum(hysr)/sum(jfzl),2) else 0 end) drsrl from cyfx_rb1_hy where rq='" + jsrq + "' and xh=31 " + skzdbm + " group by xh) a\r\n" +
                "on d.id=a.xh\r\n" +
                "left join\r\n" +
                "(select xh,sum(jfzl) ljjfzl,sum(hysr) ljhysr,(case when sum(jfzl)>0 then round(sum(hysr)/sum(jfzl),2) else 0 end) ljsrl from cyfx_rb1_hy where rq>='" + ksrq + "' and rq<='" + jsrq + "' and xh=31 " + skzdbm + " group by xh) b\r\n" +
                "on d.id=b.xh\r\n" +
                "left join\r\n" +
                "(select xh,round(sum(jfzl)/" + days + ",2) rjjfzl,round(sum(hysr)/" + days + ",2) rjhysr,(case when sum(jfzl)>0 then round(sum(hysr)/sum(jfzl),2) else 0 end) rjsrl from cyfx_rb1_hy where rq>='" + ksrq + "' and rq<='" + jsrq + "' and xh=31 " + skzdbm + " group by xh) c\r\n" +
                "on d.id=c.xh\r\n" +
                "where d.id=31";
        jdbcTemplate.update(sql);
        //处理静载重8行/25行
        sql = "insert into cyfx_rb1_hy_temp(nd,xh,DRJFZL,LJJFZL,RJJFZL) \r\n" +
                "select " + nd + ",32,\r\n" +
                "(case when (select drjfzl from cyfx_rb1_hy_temp where nd=" + nd + " and xh=31 )<>0 then round((select sum(drjfzl) from cyfx_rb1_hy_temp where nd=" + nd + " and xh=9 )/(select drjfzl from cyfx_rb1_hy_temp where nd=" + nd + " and xh=31 ),2) else 0 end) dr,\r\n" +
                "(case when (select ljjfzl from cyfx_rb1_hy_temp where nd=" + nd + " and xh=31 )<>0 then round((select sum(ljjfzl) from cyfx_rb1_hy_temp where nd=" + nd + " and xh=9 )/(select ljjfzl from cyfx_rb1_hy_temp where nd=" + nd + " and xh=31 ),2) else 0 end) lj,\r\n" +
                "(case when (select rjjfzl from cyfx_rb1_hy_temp where nd=" + nd + " and xh=31 )<>0 then round((select sum(rjjfzl) from cyfx_rb1_hy_temp where nd=" + nd + " and xh=9 )/(select rjjfzl from cyfx_rb1_hy_temp where nd=" + nd + " and xh=31 ),2) else 0 end) rj\r\n" +
                "from dual";
        jdbcTemplate.update(sql);
        //货运管内占比
        sql = "insert into cyfx_rb1_hy_temp(nd,xh,DRJFZL,DRHYSR,LJJFZL,LJHYSR,RJJFZL,RJHYSR) \r\n" +
                "select " + nd + ",26,\r\n" +
                "round((select  drjfzl from cyfx_rb1_hy_temp where xh=11 and nd=" + nd + ")/(select drjfzl from cyfx_rb1_hy_temp where xh=10 and nd=" + nd + "),2),\r\n" +
                "round((select  drhysr from cyfx_rb1_hy_temp where xh=11 and nd=" + nd + ")/(select drhysr from cyfx_rb1_hy_temp where xh=10 and nd=" + nd + "),2),\r\n" +
                "round((select  ljjfzl from cyfx_rb1_hy_temp where xh=11 and nd=" + nd + ")/(select ljjfzl from cyfx_rb1_hy_temp where xh=10 and nd=" + nd + "),2),\r\n" +
                "round((select  ljhysr from cyfx_rb1_hy_temp where xh=11 and nd=" + nd + ")/(select ljhysr from cyfx_rb1_hy_temp where xh=10 and nd=" + nd + "),2),\r\n" +
                "round((select  rjjfzl from cyfx_rb1_hy_temp where xh=11 and nd=" + nd + ")/(select rjjfzl from cyfx_rb1_hy_temp where xh=10 and nd=" + nd + "),2),\r\n" +
                "round((select  rjhysr from cyfx_rb1_hy_temp where xh=11 and nd=" + nd + ")/(select rjhysr from cyfx_rb1_hy_temp where xh=10 and nd=" + nd + "),2)\r\n" +
                "from dual\r\n" +
                "union all\r\n" +
                "select " + nd + ",27,\r\n" +
                "round((select  drjfzl from cyfx_rb1_hy_temp where xh=12 and nd=" + nd + ")/(select drjfzl from cyfx_rb1_hy_temp where xh=10 and nd=" + nd + "),2),\r\n" +
                "round((select  drhysr from cyfx_rb1_hy_temp where xh=12 and nd=" + nd + ")/(select drhysr from cyfx_rb1_hy_temp where xh=10 and nd=" + nd + "),2),\r\n" +
                "round((select  ljjfzl from cyfx_rb1_hy_temp where xh=12 and nd=" + nd + ")/(select ljjfzl from cyfx_rb1_hy_temp where xh=10 and nd=" + nd + "),2),\r\n" +
                "round((select  ljhysr from cyfx_rb1_hy_temp where xh=12 and nd=" + nd + ")/(select ljhysr from cyfx_rb1_hy_temp where xh=10 and nd=" + nd + "),2),\r\n" +
                "round((select  rjjfzl from cyfx_rb1_hy_temp where xh=12 and nd=" + nd + ")/(select rjjfzl from cyfx_rb1_hy_temp where xh=10 and nd=" + nd + "),2),\r\n" +
                "round((select  rjhysr from cyfx_rb1_hy_temp where xh=12 and nd=" + nd + ")/(select rjhysr from cyfx_rb1_hy_temp where xh=10 and nd=" + nd + "),2)\r\n" +
                "from dual";
        jdbcTemplate.update(sql);
        //平均运距管内
        sql = "insert into cyfx_rb1_hy_temp(nd,xh,DRJFZL,LJJFZL,RJJFZL) \r\n" +
                "select " + nd + ",29,\r\n" +
                "round((select  drjfzl from cyfx_rb1_hy_temp where xh=17 and nd=" + nd + ")/(select drjfzl from cyfx_rb1_hy_temp where xh=11 and nd=" + nd + "),2),\r\n" +
                "round((select  ljjfzl from cyfx_rb1_hy_temp where xh=17 and nd=" + nd + ")/(select ljjfzl from cyfx_rb1_hy_temp where xh=11 and nd=" + nd + "),2),\r\n" +
                "round((select  rjjfzl from cyfx_rb1_hy_temp where xh=17 and nd=" + nd + ")/(select rjjfzl from cyfx_rb1_hy_temp where xh=11 and nd=" + nd + "),2)\r\n" +
                "from dual\r\n" +
                "union all\r\n" +
                "select " + nd + ",30,\r\n" +
                "round((select  drjfzl from cyfx_rb1_hy_temp where xh=18 and nd=" + nd + ")/(select drjfzl from cyfx_rb1_hy_temp where xh=12 and nd=" + nd + "),2),\r\n" +
                "round((select  ljjfzl from cyfx_rb1_hy_temp where xh=18 and nd=" + nd + ")/(select ljjfzl from cyfx_rb1_hy_temp where xh=12 and nd=" + nd + "),2),\r\n" +
                "round((select  rjjfzl from cyfx_rb1_hy_temp where xh=18 and nd=" + nd + ")/(select rjjfzl from cyfx_rb1_hy_temp where xh=12 and nd=" + nd + "),2)\r\n" +
                "from dual";
        jdbcTemplate.update(sql);
    }

    /**
     * 日报二客运货运
     *
     * @param nd
     * @param days
     * @param jsrq
     * @param ksrq
     * @param cz
     * @param skzdbm
     */
    public void insertRb2_HYtem(String nd, String days, String jsrq, String ksrq, String cz, String skzdbm) {
        String sql = "insert into cyfx_rb2_hy_temp\r\n" +
                "select " + nd + "  as nd,d.id,a.drjfzl,a.drhysr,a.drsrl,b.ljjfzl,b.ljhysr,b.ljsrl,c.rjjfzl,c.rjhysr,c.rjsrl from hdy_rb2 d\r\n" +
                "left join\r\n" +
                "(select xh,sum(jfzl) drjfzl,sum(hysr) drhysr,(case when sum(jfzl)>0 then round(sum(hysr)/sum(jfzl),2) else 0 end) drsrl from cyfx_rb2_hy where rq='" + jsrq + "'" + skzdbm + " group by xh ) a\r\n" +
                "on d.id=a.xh \r\n" +
                "left join\r\n" +
                "(select xh,sum(jfzl) ljjfzl,sum(hysr) ljhysr,(case when sum(jfzl)>0 then round(sum(hysr)/sum(jfzl),2) else 0 end) ljsrl from cyfx_rb2_hy where rq>='" + ksrq + "' and rq<='" + jsrq + "'" + skzdbm + " group by xh) b\r\n" +
                "on d.id=b.xh \r\n" +
                "left join\r\n" +
                "(select xh,round(sum(jfzl)/" + days + ",2) rjjfzl,round(sum(hysr)/" + days + ",2) rjhysr,(case when sum(jfzl)>0 then round(sum(hysr)/sum(jfzl),2) else 0 end) rjsrl from cyfx_rb2_hy where rq>='" + ksrq + "' and rq<='" + jsrq + "'" + skzdbm + " group by xh) c\r\n" +
                "on d.id=c.xh \r\n" +
                "where d.id>=18";
        jdbcTemplate.update(sql);
    }

    public void insertRb2_KYtem(String nd, String days, String jsrq, String ksrq, String cz, String skzdbm) {
        String sql = "insert into cyfx_rb2_ky_temp\r\n" +
                "select " + nd + " as nd,t.xh,t.DRRS,t.DRKYSR,t.DRSRL,t.LJRS,t.LJKYSR,t.LJSRL,t.RJRS,t.RJKYSR,t.RJSRL from \r\n" +
                "(\r\n" +
                "  select 2 as xh,a.*,b.*,c.*from\r\n" +
                "  (select sum(rs) drrs,sum(pjhj) drkysr,(case when sum(rs)>0 then round(sum(pjhj)/sum(rs),2) else 0 end) drsrl from cyfx_rb_ky where ccrq='" + jsrq + "'" + cz + ") a\r\n" +
                "  left join\r\n" +
                "  (select sum(rs) ljrs,sum(pjhj) ljkysr,(case when sum(rs)>0 then round(sum(pjhj)/sum(rs),2) else 0 end) ljsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "'" + cz + ") b\r\n" +
                "  on 1=1\r\n" +
                "  left join\r\n" +
                "  (select round(sum(rs)/" + days + ",2) rjrs,round(sum(pjhj)/" + days + ") rjkysr,(case when sum(rs)>0 then round(sum(pjhj)/sum(rs),2) else 0 end) rjsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "'" + cz + ") c\r\n" +
                "  on 1=1\r\n" +
                "  union all\r\n" +
                "  --  1.旅客票价收入\r\n" +
                "  select 3,a.*,b.*,c.*from\r\n" +
                "  (select sum(rs) drrs,sum(jcpj) drjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) drsrl from cyfx_rb_ky where ccrq='" + jsrq + "'" + cz + ") a\r\n" +
                "  left join\r\n" +
                "  (select sum(rs) ljrs,sum(jcpj) ljjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) ljsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "'" + cz + ") b\r\n" +
                "  on 1=1\r\n" +
                "  left join\r\n" +
                "  (select round(sum(rs)/" + days + ",2) rjrs,round(sum(jcpj)/" + days + ") rjjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) rjsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "'" + cz + ") c\r\n" +
                "  on 1=1\r\n" +
                "  union all\r\n" +
                "  --（1）发售客票收入\r\n" +
                "  --其中：管内\r\n" +
                "  --      直通\r\n" +
                "  select 4,a.*,b.*,c.*from\r\n" +
                "  (select sum(rs) drrs,sum(jcpj) drjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) drsrl from cyfx_rb_ky where ccrq='" + jsrq + "' and fzj='R' " + cz + ") a\r\n" +
                "  left join\r\n" +
                "  (select sum(rs) ljrs,sum(jcpj) ljjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) ljsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' and fzj='R' " + cz + ") b\r\n" +
                "  on 1=1\r\n" +
                "  left join\r\n" +
                "  (select round(sum(rs)/" + days + ",2) rjrs,round(sum(jcpj)/" + days + ") rjjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) rjsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' and fzj='R' " + cz + ") c\r\n" +
                "  on 1=1\r\n" +
                "  union all\r\n" +
                "  select 5,a.*,b.*,c.*from\r\n" +
                "  (select sum(rs) drrs,sum(jcpj) drjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) drsrl from cyfx_rb_ky where ccrq='" + jsrq + "' and fzj=dzj and fzj='R' " + cz + ") a\r\n" +
                "  left join\r\n" +
                "  (select sum(rs) ljrs,sum(jcpj) ljjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) ljsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' and fzj=dzj and fzj='R' " + cz + ") b\r\n" +
                "  on 1=1\r\n" +
                "  left join\r\n" +
                "  (select round(sum(rs)/" + days + ",2) rjrs,round(sum(jcpj)/" + days + ") rjjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) rjsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' and fzj=dzj and fzj='R' " + cz + ") c\r\n" +
                "  on 1=1\r\n" +
                "  union all\r\n" +
                "  select 6,a.*,b.*,c.*from\r\n" +
                "  (select sum(rs) drrs,sum(jcpj) drjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) drsrl from cyfx_rb_ky where ccrq='" + jsrq + "' and fzj<>dzj and fzj='R' " + cz + ") a\r\n" +
                "  left join\r\n" +
                "  (select sum(rs) ljrs,sum(jcpj) ljjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) ljsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' and fzj<>dzj and fzj='R' " + cz + ") b\r\n" +
                "  on 1=1\r\n" +
                "  left join\r\n" +
                "  (select round(sum(rs)/" + days + ",2) rjrs,round(sum(jcpj)/" + days + ") rjjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) rjsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' and fzj<>dzj and fzj='R' " + cz + ") c\r\n" +
                "  on 1=1\r\n" +
                "  union all\r\n" +
                "  --（2）异地票\r\n" +
                "  select 7,a.*,b.*,c.*from\r\n" +
                "  (select sum(rs) drrs,sum(jcpj) drjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) drsrl from cyfx_rb_ky where ccrq='" + jsrq + "' and fzj<>'R' " + cz + ") a\r\n" +
                "  left join\r\n" +
                "  (select sum(rs) ljrs,sum(jcpj) ljjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) ljsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' and fzj<>'R'" + cz + ") b\r\n" +
                "  on 1=1\r\n" +
                "  left join\r\n" +
                "  (select round(sum(rs)/" + days + ",2) rjrs,round(sum(jcpj)/" + days + ") rjjcpj,(case when sum(rs)>0 then round(sum(jcpj)/sum(rs),2) else 0 end) rjsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' and fzj<>'R' " + cz + ") c\r\n" +
                "  on 1=1\r\n" +
                "  union all\n" +
                "  -- 2.客运其他收入\n" +
                "  select 8,a.*,b.*,c.*from\n" +
                "  (select sum(rs) drrs,sum(wpdpf7+wpdpf3+czktf+rpf) kqtsr,(case when sum(rs)>0 then round(sum(wpdpf7+wpdpf3+czktf+rpf)/sum(rs),2) else 0 end) drsrl from cyfx_rb_ky where ccrq='" + jsrq + "' " + cz + ") a\n" +
                "  left join\n" +
                "  (select sum(rs) ljrs,sum(wpdpf7+wpdpf3+czktf+rpf) ljwpdpf7,(case when sum(rs)>0 then round(sum(wpdpf7+wpdpf3+czktf+rpf)/sum(rs),2) else 0 end) ljsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' " + cz + ") b\n" +
                "  on 1=1\n" +
                "  left join\n" +
                "  (select round(sum(rs)/" + days + ",2) rjrs,round(sum(wpdpf7+wpdpf3+czktf+rpf)/" + days + ") rjwpdpf7,(case when sum(rs)>0 then round(sum(wpdpf7+wpdpf3+czktf+rpf)/sum(rs),2) else 0 end) rjsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "' " + cz + ") c\n" +
                "  on 1=1\n" +
                "  union all\r\n" +
                "  --其中：卧铺订票费70%\r\n" +
                "  select 9,a.*,b.*,c.*from\r\n" +
                "  (select sum(rs) drrs,sum(wpdpf7) drwpdpf7,(case when sum(rs)>0 then round(sum(wpdpf7)/sum(rs),2) else 0 end) drsrl from cyfx_rb_ky where ccrq='" + jsrq + "'" + cz + ") a\r\n" +
                "  left join\r\n" +
                "  (select sum(rs) ljrs,sum(wpdpf7) ljwpdpf7,(case when sum(rs)>0 then round(sum(wpdpf7)/sum(rs),2) else 0 end) ljsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "'" + cz + ") b\r\n" +
                "  on 1=1\r\n" +
                "  left join\r\n" +
                "  (select round(sum(rs)/" + days + ",2) rjrs,round(sum(wpdpf7)/" + days + ") rjwpdpf7,(case when sum(rs)>0 then round(sum(wpdpf7)/sum(rs),2) else 0 end) rjsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "'" + cz + ") c\r\n" +
                "  on 1=1\r\n" +
                "  union all\r\n" +
                "  --候车室空调费\r\n" +
                "  select 12,a.*,b.*,c.*from\r\n" +
                "  (select sum(rs) drrs,sum(czktf) drczktf,(case when sum(rs)>0 then round(sum(czktf)/sum(rs),2) else 0 end) drsrl from cyfx_rb_ky where ccrq='" + jsrq + "'" + cz + ") a\r\n" +
                "  left join\r\n" +
                "  (select sum(rs) ljrs,sum(czktf) ljczktf,(case when sum(rs)>0 then round(sum(czktf)/sum(rs),2) else 0 end) ljsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "'" + cz + ") b\r\n" +
                "  on 1=1\r\n" +
                "  left join\r\n" +
                "  (select round(sum(rs)/" + days + ",2) rjrs,round(sum(czktf)/" + days + ") rjczktf,(case when sum(rs)>0 then round(sum(czktf)/sum(rs),2) else 0 end) rjsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "'" + cz + ") c\r\n" +
                "  on 1=1\r\n" +
                "  union all\r\n" +
                "  --客票系统发展金\r\n" +
                "  select 13,a.*,b.*,c.*from\r\n" +
                "  (select sum(rs) drrs,sum(rpf) drrpf,(case when sum(rs)>0 then round(sum(rpf)/sum(rs),2) else 0 end) drsrl from cyfx_rb_ky where ccrq='" + jsrq + "'" + cz + ") a\r\n" +
                "  left join\r\n" +
                "  (select sum(rs) ljrs,sum(rpf) ljrpf,(case when sum(rs)>0 then round(sum(rpf)/sum(rs),2) else 0 end) ljsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "'" + cz + ") b\r\n" +
                "  on 1=1\r\n" +
                "  left join\r\n" +
                "  (select round(sum(rs)/" + days + ",2) rjrs,round(sum(rpf)/" + days + ") rjrpf,(case when sum(rs)>0 then round(sum(rpf)/sum(rs),2) else 0 end) rjsrl from cyfx_rb_ky where ccrq>='" + ksrq + "' and ccrq<='" + jsrq + "'" + cz + ") c\r\n" +
                "  on 1=1\r\n" +
                ") t";
        jdbcTemplate.update(sql);
        sql = "insert into cyfx_rb2_ky_temp\n" +
                "select " + nd + " as nd,t.* from \n" +
                "(\n" +
                "  --客杂、进客运总收入\r\n" +
                "select xh,sum(drrs) drrs,sum(drsr) drsr,(case when sum(drrs)>0 then round(sum(drsr)/sum(drrs),2) else 0 end) drsrl,\r\n" +
                "sum(ljrs) ljrs,sum(lj) ljsr,(case when sum(ljrs)>0 then round(sum(lj)/sum(ljrs),2) else 0 end) ljsrl,\r\n" +
                "sum(rjrs) rjrs,sum(rj) rjsr,(case when sum(rjrs)>0 then round(sum(rj)/sum(rjrs),2) else 0 end) rjsrl\r\n" +
                "from\r\n" +
                "(\r\n" +
                "  select 2 as xh,a.*,b.*,c.* from\r\n" +
                "  (select sum(车补人数+站补人数) drrs,sum(车补税后+站补税后) drsr from srfx_b542_" + nd + "d where 日期=" + jsrq + cz.replace("czid", "电报码") + ") a\n" +
                "  left join\r\n" +
                "  (select sum(车补人数+站补人数) ljrs,sum(车补税后+站补税后) lj from srfx_b542_" + nd + "d where 日期>=" + ksrq + " and 日期<=" + jsrq + cz.replace("czid", "电报码") + ") b\n" +
                "  on 1=1\r\n" +
                "  left join\r\n" +
                "  (select round(sum(车补人数+站补人数)/" + days + ",2) rjrs,round(sum(车补税后+站补税后)/" + days + ") rj from srfx_b542_" + nd + "d where 日期>=" + ksrq + "and 日期<=" + jsrq + cz.replace("czid", "电报码") + ") c\n" +
                "  on 1=1\r\n" +
                ")\r\n" +
                "group by xh" +
                ") t";
        jdbcTemplate.update(sql);
        sql = "insert into cyfx_rb2_ky_temp\n" +
                "select " + nd + " as nd,t.* from \n" +
                "(\n" +
                "  --客杂、进客运总收入\r\n" +
                "select xh,sum(drrs) drrs,sum(drsr) drsr,(case when sum(drrs)>0 then round(sum(drsr)/sum(drrs),2) else 0 end) drsrl,\r\n" +
                "sum(ljrs) ljrs,sum(lj) ljsr,(case when sum(ljrs)>0 then round(sum(lj)/sum(ljrs),2) else 0 end) ljsrl,\r\n" +
                "sum(rjrs) rjrs,sum(rj) rjsr,(case when sum(rjrs)>0 then round(sum(rj)/sum(rjrs),2) else 0 end) rjsrl\r\n" +
                "from\r\n" +
                "(\r\n" +
                "  select 8 as xh,a.*,b.*,c.* from\r\n" +
                "  (select sum(车补人数+站补人数) drrs,sum(车补税后+站补税后) drsr from srfx_b542_" + nd + "d where 日期=" + jsrq + cz.replace("czid", "电报码") + ") a\n" +
                "  left join\r\n" +
                "  (select sum(车补人数+站补人数) ljrs,sum(车补税后+站补税后) lj from srfx_b542_" + nd + "d where 日期>=" + ksrq + " and 日期<=" + jsrq + cz.replace("czid", "电报码") + ") b\n" +
                "  on 1=1\r\n" +
                "  left join\r\n" +
                "  (select round(sum(车补人数+站补人数)/" + days + ",2) rjrs,round(sum(车补税后+站补税后)/" + days + ") rj from srfx_b542_" + nd + "d where 日期>=" + ksrq + "and 日期<=" + jsrq + cz.replace("czid", "电报码") + ") c\n" +
                "  on 1=1\r\n" +
                ")\r\n" +
                "group by xh" +
                ") t";
        jdbcTemplate.update(sql);
        sql = "insert into cyfx_rb2_ky_temp\n" +
                "select " + nd + " as nd,t.* from \n" +
                "(\n" +
                "  --客杂、进客运总收入\r\n" +
                "select xh,sum(drrs) drrs,sum(drsr) drsr,(case when sum(drrs)>0 then round(sum(drsr)/sum(drrs),2) else 0 end) drsrl,\r\n" +
                "sum(ljrs) ljrs,sum(lj) ljsr,(case when sum(ljrs)>0 then round(sum(lj)/sum(ljrs),2) else 0 end) ljsrl,\r\n" +
                "sum(rjrs) rjrs,sum(rj) rjsr,(case when sum(rjrs)>0 then round(sum(rj)/sum(rjrs),2) else 0 end) rjsrl\r\n" +
                "from\r\n" +
                "(\r\n" +
                "  select 10 as xh,a.*,b.*,c.* from\r\n" +
                "  (select sum(车补人数) drrs,sum(车补税后) drsr from srfx_b542_" + nd + "d where 日期=" + jsrq + cz.replace("czid", "电报码") + "  and 车补人数>0) a\n" +
                "  left join\r\n" +
                "  (select sum(车补人数) ljrs,sum(车补税后) lj from srfx_b542_" + nd + "d where 日期>=" + ksrq + " and 日期<=" + jsrq + cz.replace("czid", "电报码") + "  and 车补人数>0) b\n" +
                "  on 1=1\r\n" +
                "  left join\r\n" +
                "  (select round(sum(车补人数)/" + days + ",2) rjrs,round(sum(车补税后)/" + days + ") rj from srfx_b542_" + nd + "d where 日期>=" + ksrq + "and 日期<=" + jsrq + cz.replace("czid", "电报码") + "  and 车补人数>0) c\n" +
                "  on 1=1\r\n" +
                ")\r\n" +
                "group by xh" +
                ") t";
        jdbcTemplate.update(sql);
        sql = "insert into cyfx_rb2_ky_temp\n" +
                "select " + nd + " as nd,t.* from \n" +
                "(\n" +
                "  --客杂、进客运总收入\r\n" +
                "select xh,sum(drrs) drrs,sum(drsr) drsr,(case when sum(drrs)>0 then round(sum(drsr)/sum(drrs),2) else 0 end) drsrl,\r\n" +
                "sum(ljrs) ljrs,sum(lj) ljsr,(case when sum(ljrs)>0 then round(sum(lj)/sum(ljrs),2) else 0 end) ljsrl,\r\n" +
                "sum(rjrs) rjrs,sum(rj) rjsr,(case when sum(rjrs)>0 then round(sum(rj)/sum(rjrs),2) else 0 end) rjsrl\r\n" +
                "from\r\n" +
                "(\r\n" +
                "  select 11 as xh,a.*,b.*,c.* from\r\n" +
                "  (select sum(站补人数) drrs,sum(站补税后) drsr from srfx_b542_" + nd + "d where 日期=" + jsrq + cz.replace("czid", "电报码") + " and 站补人数>0) a\n" +
                "  left join\r\n" +
                "  (select sum(站补人数) ljrs,sum(站补税后) lj from srfx_b542_" + nd + "d where 日期>=" + ksrq + " and 日期<=" + jsrq + cz.replace("czid", "电报码") + " and 站补人数>0) b\n" +
                "  on 1=1\r\n" +
                "  left join\r\n" +
                "  (select round(sum(站补人数)/" + days + ",2) rjrs,round(sum(站补税后)/" + days + ") rj from srfx_b542_" + nd + "d where 日期>=" + ksrq + "and 日期<=" + jsrq + cz.replace("czid", "电报码") + " and 站补人数>0) c\n" +
                "  on 1=1\r\n" +
                ")\r\n" +
                "group by xh" +
                ") t";
        jdbcTemplate.update(sql);
        sql = "insert into cyfx_rb2_ky_temp\r\n" +
                "select " + nd + " as nd,xh,sum(drrs) drrs,sum(drsr) drsr,0 drsrl,\r\n" +
                "sum(ljrs) ljrs,sum(lj) kqtsr,0 ljsrl,\r\n" +
                "sum(rjrs) rjrs,sum(rj) kqtsr,0 rjsrl\r\n" +
                "from\r\n" +
                "(\r\n" +
                "  select 2 as xh,a.*,b.*,c.* from\r\n" +
                "  (select 0 drrs,sum(行李运费+包裹运费-行李税-包裹税+行包发站装卸费+行包到站装卸费-发站装卸费税-到站装卸费税+保价款-保价费税) drsr from srfx_b546_" + nd + " where 日期=" + jsrq + cz.replace("czid", "电报码") + ") a\n" +
                "  left join\r\n" +
                "  (select 0 ljrs,sum(行李运费+包裹运费-行李税-包裹税+行包发站装卸费+行包到站装卸费-发站装卸费税-到站装卸费税+保价款-保价费税) lj from srfx_b546_" + nd + " where 日期>=" + ksrq + " and 日期<=" + jsrq + cz.replace("czid", "电报码") + " ) b\n" +
                "  on 1=1\r\n" +
                "  left join\r\n" +
                "  (select 0 rjrs,round(sum(行李运费+包裹运费-行李税-包裹税+行包发站装卸费+行包到站装卸费-发站装卸费税-到站装卸费税+保价款-保价费税)/" + days + ") rj from srfx_b546_" + nd + " where 日期>=" + ksrq + "and 日期<=" + jsrq + cz.replace("czid", "电报码") + ") c\n" +
                "  on 1=1\r\n" +
                ")\r\n" +
                "group by xh";
        jdbcTemplate.update(sql);
        sql = "insert into cyfx_rb2_ky_temp\r\n" +
                "select " + nd + " as nd,xh,sum(drrs) drrs,sum(drsr) drsr,0 drsrl,\r\n" +
                "sum(ljrs) ljrs,sum(lj) kqtsr,0 ljsrl,\r\n" +
                "sum(rjrs) rjrs,sum(rj) kqtsr,0 rjsrl\r\n" +
                "from\r\n" +
                "(\r\n" +
                "  select 8 as xh,a.*,b.*,c.* from\r\n" +
                "  (select 0 drrs,sum(行李运费+包裹运费-行李税-包裹税+行包发站装卸费+行包到站装卸费-发站装卸费税-到站装卸费税+保价款-保价费税) drsr from srfx_b546_" + nd + " where 日期=" + jsrq + cz.replace("czid", "电报码") + ") a\n" +
                "  left join\r\n" +
                "  (select 0 ljrs,sum(行李运费+包裹运费-行李税-包裹税+行包发站装卸费+行包到站装卸费-发站装卸费税-到站装卸费税+保价款-保价费税) lj from srfx_b546_" + nd + " where 日期>=" + ksrq + " and 日期<=" + jsrq + cz.replace("czid", "电报码") + " ) b\n" +
                "  on 1=1\r\n" +
                "  left join\r\n" +
                "  (select 0 rjrs,round(sum(行李运费+包裹运费-行李税-包裹税+行包发站装卸费+行包到站装卸费-发站装卸费税-到站装卸费税+保价款-保价费税)/" + days + ") rj from srfx_b546_" + nd + " where 日期>=" + ksrq + "and 日期<=" + jsrq + cz.replace("czid", "电报码") + ") c\n" +
                "  on 1=1\r\n" +
                ")\r\n" +
                "group by xh";
        jdbcTemplate.update(sql);
        sql = "insert into cyfx_rb2_ky_temp\r\n" +
                "select " + nd + " as nd,xh,sum(drrs) drrs,sum(drsr) drsr,0 drsrl,\r\n" +
                "sum(ljrs) ljrs,sum(lj) kqtsr,0 ljsrl,\r\n" +
                "sum(rjrs) rjrs,sum(rj) kqtsr,0 rjsrl\r\n" +
                "from\r\n" +
                "(\r\n" +
                "  select 14 as xh,a.*,b.*,c.* from\r\n" +
                "  (select 0 drrs,sum(行李运费+包裹运费-行李税-包裹税) drsr from srfx_b546_" + nd + " where 日期=" + jsrq + cz.replace("czid", "电报码") + ") a\n" +
                "  left join\r\n" +
                "  (select 0 ljrs,sum(行李运费+包裹运费-行李税-包裹税) lj from srfx_b546_" + nd + " where 日期>=" + ksrq + " and 日期<=" + jsrq + cz.replace("czid", "电报码") + " ) b\n" +
                "  on 1=1\r\n" +
                "  left join\r\n" +
                "  (select 0 rjrs,round(sum(行李运费+包裹运费-行李税-包裹税)/" + days + ") rj from srfx_b546_" + nd + " where 日期>=" + ksrq + "and 日期<=" + jsrq + cz.replace("czid", "电报码") + ") c\n" +
                "  on 1=1\r\n" +
                ")\r\n" +
                "group by xh";
        jdbcTemplate.update(sql);
        sql = "insert into cyfx_rb2_ky_temp\r\n" +
                "select " + nd + " as nd,xh,sum(drrs) drrs,sum(drsr) drsr,0 drsrl,\r\n" +
                "sum(ljrs) ljrs,sum(lj) kqtsr,0 ljsrl,\r\n" +
                "sum(rjrs) rjrs,sum(rj) kqtsr,0 rjsrl\r\n" +
                "from\r\n" +
                "(\r\n" +
                "  select 16 as xh,a.*,b.*,c.* from\r\n" +
                "  (select 0 drrs,sum(行包发站装卸费+行包到站装卸费-发站装卸费税-到站装卸费税) drsr from srfx_b546_" + nd + " where 日期=" + jsrq + cz.replace("czid", "电报码") + ") a\n" +
                "  left join\r\n" +
                "  (select 0 ljrs,sum(行包发站装卸费+行包到站装卸费-发站装卸费税-到站装卸费税) lj from srfx_b546_" + nd + " where 日期>=" + ksrq + " and 日期<=" + jsrq + cz.replace("czid", "电报码") + " ) b\n" +
                "  on 1=1\r\n" +
                "  left join\r\n" +
                "  (select 0 rjrs,round(sum(行包发站装卸费+行包到站装卸费-发站装卸费税-到站装卸费税)/" + days + ") rj from srfx_b546_" + nd + " where 日期>=" + ksrq + "and 日期<=" + jsrq + cz.replace("czid", "电报码") + ") c\n" +
                "  on 1=1\r\n" +
                ")\r\n" +
                "group by xh";
        jdbcTemplate.update(sql);
        sql = "insert into cyfx_rb2_ky_temp\r\n" +
                "select " + nd + " as nd,xh,sum(drrs) drrs,sum(drsr) drsr,0 drsrl,\r\n" +
                "sum(ljrs) ljrs,sum(lj) kqtsr,0 ljsrl,\r\n" +
                "sum(rjrs) rjrs,sum(rj) kqtsr,0 rjsrl\r\n" +
                "from\r\n" +
                "(\r\n" +
                "  select 17 as xh,a.*,b.*,c.* from\r\n" +
                "  (select 0 drrs,sum(保价款-保价费税) drsr from srfx_b546_" + nd + " where 日期=" + jsrq + cz.replace("czid", "电报码") + ") a\n" +
                "  left join\r\n" +
                "  (select 0 ljrs,sum(保价款-保价费税) lj from srfx_b546_" + nd + " where 日期>=" + ksrq + " and 日期<=" + jsrq + cz.replace("czid", "电报码") + " ) b\n" +
                "  on 1=1\r\n" +
                "  left join\r\n" +
                "  (select 0 rjrs,round(sum(保价款-保价费税)/" + days + ") rj from srfx_b546_" + nd + " where 日期>=" + ksrq + "and 日期<=" + jsrq + cz.replace("czid", "电报码") + ") c\n" +
                "  on 1=1\r\n" +
                ")\r\n" +
                "group by xh";
        jdbcTemplate.update(sql);
    }
}
