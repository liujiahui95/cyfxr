package com.cyfxr.cyfx.dao.impl;

import com.cyfxr.cyfx.dao.CyfxDao;
import com.cyfxr.system.domain.CyfxSearchModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("cyfxDao")
public class CyfxDaoImpl implements CyfxDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getFb1(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String sql = "select a.hh,a.mc,b.车数,b.计费重量,b.计费吨公里,b.车流吨公里,b.运费收入,b.承运收入,b.承运付费,b.本企业承运服务收入,\r\n" +
                "b.本企业货票盈余,b.本企业承运盈余,b.计费吨公里运费,b.本企业货票盈余率,b.本企业货票运费盈余率,b.本企业承运盈余率,\r\n" +
                "b.本企业承运运费盈余率,b.本月企业单车货票盈余,b.本月企业单车承运盈余\r\n" +
                "from \r\n" +
                "hdy_cyhz_fb1 a\r\n" +
                "left join\r\n" +
                "(select 企业代码,\r\n" +
                "sum(车数) as 车数, \r\n" +
                "sum(round(计费重量/" + jldw + ",2)) as 计费重量,\r\n" +
                "sum(round(计费吨公里/" + jldw + ",2)) as 计费吨公里,\r\n" +
                "sum(round(车流吨公里/" + jldw + ",2)) as 车流吨公里,\r\n" +
                "sum(round(运费收入/" + jldw + ",2)) as 运费收入,\r\n" +
                "sum(round(承运收入/" + jldw + ",2)) as 承运收入,\r\n" +
                "sum(round(承运付费/" + jldw + ",2)) as 承运付费,\r\n" +
                "sum(round(本企业取得服务/" + jldw + ",2)) as 本企业承运服务收入,\r\n" +
                "sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业货票盈余,\r\n" +
                "sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业承运盈余,\r\n" +
                "(case when abs( sum(round(计费吨公里/" + jldw + ",2))) > 0 then round (sum(round(运费收入/" + jldw + ",2))/sum(round(计费吨公里/" + jldw + ",2)),2) else 0 end ) as 计费吨公里运费,\r\n" +
                "(case when abs( sum(round(承运收入/" + jldw + ",2))) > 0 then round (sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2))/sum(round(承运收入/" + jldw + ",2)),2) else 0 end ) as 本企业货票盈余率,\r\n" +
                "(case when abs( sum(round(运费收入/" + jldw + ",2))) > 0 then round (sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2))/sum(round(运费收入/" + jldw + ",2)),2) else 0 end ) as 本企业货票运费盈余率,\r\n" +
                "(case when abs( sum(round(承运收入/" + jldw + ",2))) > 0 then round (sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2))/sum(round(承运收入/" + jldw + ",2)),2) else 0 end ) as 本企业承运盈余率,\r\n" +
                "(case when abs( sum(round(承运收入/" + jldw + ",2))) > 0 then round (sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2))/sum(round(运费收入/" + jldw + ",2)),2) else 0 end ) as 本企业承运运费盈余率,\r\n" +
                "(case when abs( sum(round(计费重量/" + jldw + ",2))) > 0 then round (sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2))/sum(round(计费重量/" + jldw + ",2)),2) else 0 end ) as 本月企业单车货票盈余,\r\n" +
                "(case when abs( sum(round(计费重量/" + jldw + ",2))) > 0 then round (sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end ) as 本月企业单车承运盈余\r\n" +
                "from huizong_承运汇总\r\n" +
                "where 日期>=" + ksrq + " and 日期<=" + jsrq + "\r\n" +
                "group by 企业代码\r\n" +
                "union all\r\n" +
                "select 'HJ'  企业代码,\r\n" +
                "sum(车数) as 车数, \r\n" +
                "sum(round(计费重量/" + jldw + ",2)) as 计费重量,\r\n" +
                "sum(round(计费吨公里/" + jldw + ",2)) as 计费吨公里,\r\n" +
                "sum(round(车流吨公里/" + jldw + ",2)) as 车流吨公里,\r\n" +
                "sum(round(运费收入/" + jldw + ",2)) as 运费收入,\r\n" +
                "sum(round(承运收入/" + jldw + ",2)) as 承运收入,\r\n" +
                "sum(round(承运付费/" + jldw + ",2)) as 承运付费,\r\n" +
                "sum(round(本企业取得服务/" + jldw + ",2)) as 本企业承运服务收入,\r\n" +
                "sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业货票盈余,\r\n" +
                "sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业承运盈余,\r\n" +
                "(case when abs( sum(round(计费吨公里/" + jldw + ",2))) > 0 then round (sum(round(运费收入/" + jldw + ",2))/sum(round(计费吨公里/" + jldw + ",2)),2) else 0 end ) as 计费吨公里运费,\r\n" +
                "(case when abs( sum(round(承运收入/" + jldw + ",2))) > 0 then round (sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2))/sum(round(承运收入/" + jldw + ",2)),2) else 0 end ) as 本企业货票盈余率,\r\n" +
                "(case when abs( sum(round(运费收入/" + jldw + ",2))) > 0 then round (sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2))/sum(round(运费收入/" + jldw + ",2)),2) else 0 end ) as 本企业货票运费盈余率,\r\n" +
                "(case when abs( sum(round(承运收入/" + jldw + ",2))) > 0 then round (sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2))/sum(round(承运收入/" + jldw + ",2)),2) else 0 end ) as 本企业承运盈余率,\r\n" +
                "(case when abs( sum(round(承运收入/" + jldw + ",2))) > 0 then round (sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2))/sum(round(运费收入/" + jldw + ",2)),2) else 0 end ) as 本企业承运运费盈余率,\r\n" +
                "(case when abs( sum(round(计费重量/" + jldw + ",2))) > 0 then round (sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2))/sum(round(计费重量/" + jldw + ",2)),2) else 0 end ) as 本月企业单车货票盈余,\r\n" +
                "(case when abs( sum(round(计费重量/" + jldw + ",2))) > 0 then round (sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end ) as 本月企业单车承运盈余\r\n" +
                "from huizong_承运汇总\r\n" +
                "where 日期>=" + ksrq + " and 日期<=" + jsrq + "\r\n" +
                " ) b \r\n" +
                "on a.qydm = b.企业代码  order by hh";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getFb1_1(CyfxSearchModel searchInfo) {
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String sql = "select\n" +
                "HH,MC,sum(发送量) 发送量,sum(车数) 车数,sum(运费收入) 运费收入,sum(承运收入) 承运收入,sum(本企业发送基准运费) 本企业发送基准运费,sum(本企业发送高出归己) 本企业发送高出归己,\n" +
                "sum(收本局其他企业发送高出运费) 收本局其他企业发送高出运费,sum(收外局通过高出运费) 收外局通过高出运费,sum(收外局到达高出运费) 收外局到达高出运费,sum(本企业承运服务收入) 本企业承运服务收入,\n" +
                "sum(自收自) 自收自,sum(收本局其他企业服务收入) 收本局其他企业服务收入,sum(收外局通过货物服务费) 收外局通过货物服务费,sum(收外局到达货物服务费) 收外局到达货物服务费,\n" +
                "sum(本企业非票服务收入) 本企业非票服务收入,sum(空车补偿费) 空车补偿费,sum(中转服务费) 中转服务费,sum(集装箱使用费) 集装箱使用费,sum(篷布使用费) 篷布使用费,\n" +
                "sum(承运付费) 承运付费,sum(本企业发送货票盈余) 本企业发送货票盈余,sum(本企业发送承运盈余) 本企业发送承运盈余,sum(本企业承运总盈余) 本企业承运总盈余\n" +
                "from cyfx_fb1_1\n" +
                "where qsrq>=" + ksrq + " and qsrq<=" + jsrq + "\n" +
                "group by hh,mc\n" +
                "order by hh";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getFb2_cx(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String tj = "";//添加所有条件
        String dw = searchInfo.getDw();//单位（企业）
        String cz = searchInfo.getCz();//车站
        String jm = searchInfo.getJm();//局码
        String yjh = searchInfo.getYjh();//运价号

        if (cz != null && !cz.equals("") && !cz.equals("0")) {
            tj += " and 电报码 ='" + cz + "'";
        }
        if (jm != null && !jm.equals("") && !jm.equals("0")) {
            tj += " and 到局码 ='" + jm + "'";
        }
        if (yjh != null && !yjh.equals("") && !yjh.equals("0")) {
            tj += " and 运价号 ='" + yjh + "'";
        }
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            tj += " and 企业代码 ='" + dw + "'";
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String sql = "delete from temp_承运汇总_车型";
        jdbcTemplate.update(sql);
        sql = "insert into temp_承运汇总_车型\r\n" +
                "select 车型,  \r\n" +
                "sum(车数) as 车数, \r\n" +
                "sum(箱数) as 箱数,\r\n" +
                "sum(计费重量) as 计费重量,\r\n" +
                "sum(计费吨公里) as 计费吨公里,\r\n" +
                "sum(车流吨公里) as 车流吨公里,\r\n" +
                "sum(运费收入) as 运费收入,\r\n" +
                "sum(基准) as 基准,\r\n" +
                "sum(高出归己) as 高出归己,\r\n" +
                "sum(付本局其他高出) as 付本局其他高出,\r\n" +
                "sum(付外局高出) as 付外局高出,\r\n" +
                "sum(承运收入) as 承运收入,\r\n" +
                "sum(承运付费) as 承运付费,\r\n" +
                "sum(付国铁) as 付国铁,\r\n" +
                "sum(付乌准) as 付乌准,\r\n" +
                "sum(付奎北) as 付奎北,\r\n" +
                "sum(付库俄) as 付库俄,\r\n" +
                "sum(付哈罗) as 付哈罗,\r\n" +
                "sum(付外局) as 付外局,\r\n" +
                "sum(本企业取得服务) as 本企业取得服务,\r\n" +
                "sum(本局取得服务) as 本局取得服务\r\n" +
                "from HUIZONG_承运汇总 t \r\n" +
                "where t.日期 >=" + ksrq + " and t.日期 <=" + jsrq + tj + "\r\n" +
                "group by 车型";
        jdbcTemplate.update(sql);
        sql = "select hh,mc,\r\n" +
                "车数,计费重量,计费吨公里,车流吨公里,运费收入,基准,高出归己,付本局其他高出,付外局高出,承运收入,承运付费,\r\n" +
                "付国铁,付乌准,付奎北,付库俄,付哈罗,付外局,本企业取得服务,本局取得服务,本企业货票盈余,本企业承运盈余,全局承运盈余,计费吨公里运费,\r\n" +
                "本企业货票盈余率,本企业货票运费盈余率,全局承运盈余率,全局承运运费盈余率,本企业单车货票盈余,全局单车承运盈余\r\n" +
                "from hdy_cyhz_fb2_chex a\r\n" +
                "left join\r\n" +
                "(\r\n" +
                "  select 车型,\r\n" +
                "  sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数,\r\n" +
                "  sum(round(计费重量/" + jldw + ",2)) as 计费重量,\r\n" +
                "  sum(round(计费吨公里/" + jldw + ",2)) as 计费吨公里,\r\n" +
                "  sum(round(车流吨公里/" + jldw + ",2)) as 车流吨公里,\r\n" +
                "  sum(round(运费收入/" + jldw + ",2)) as 运费收入,\r\n" +
                "  sum(round(基准/" + jldw + ",2)) as 基准,\r\n" +
                "  sum(round(高出归己/" + jldw + ",2)) as 高出归己,\r\n" +
                "  sum(round(付本局其他高出/" + jldw + ",2)) as 付本局其他高出,\r\n" +
                "  sum(round(付外局高出/" + jldw + ",2)) as 付外局高出,\r\n" +
                "  sum(round(承运收入/" + jldw + ",2)) as 承运收入,\r\n" +
                "  sum(round(承运付费/" + jldw + ",2)) as 承运付费,\r\n" +
                "  sum(round(付国铁/" + jldw + ",2)) as 付国铁,\r\n" +
                "  sum(round(付乌准/" + jldw + ",2)) as 付乌准,\r\n" +
                "  sum(round(付奎北/" + jldw + ",2)) as 付奎北,\r\n" +
                "  sum(round(付库俄/" + jldw + ",2)) as 付库俄,\r\n" +
                "  sum(round(付哈罗/" + jldw + ",2)) as 付哈罗,\r\n" +
                "  sum(round(付外局/" + jldw + ",2)) as 付外局,\r\n" +
                "  sum(round(本企业取得服务/" + jldw + ",2)) as 本企业取得服务,\r\n" +
                "  sum(round(本局取得服务/" + jldw + ",2)) as 本局取得服务,\r\n" +
                "  sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业货票盈余,\r\n" +
                "  sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业承运盈余,\r\n" +
                "  sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 全局承运盈余,\r\n" +
                "  (case when abs(sum(round(计费吨公里/" + jldw + ",2))) > 0 then round(sum(round(运费收入/" + jldw + ",2))/sum(round(计费吨公里/" + jldw + ",2)),2) else 0 end) as 计费吨公里运费,\r\n" +
                "  (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 本企业货票盈余率,\r\n" +
                "  (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)),2) else 0 end) as 本企业货票运费盈余率,\r\n" +
                "  (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 全局承运盈余率,\r\n" +
                "  (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)) ,2) else 0 end) as 全局承运运费盈余率,\r\n" +
                "  (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 本企业单车货票盈余,\r\n" +
                "  (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 全局单车承运盈余\r\n" +
                "  from temp_承运汇总_车型 \r\n" +
                "  group by 车型\r\n" +
                "  union all\r\n" +
                "  select '合计' 车型,\r\n" +
                "  sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数,\r\n" +
                "  sum(round(计费重量/" + jldw + ",2)) as 计费重量,\r\n" +
                "  sum(round(计费吨公里/" + jldw + ",2)) as 计费吨公里,\r\n" +
                "  sum(round(车流吨公里/" + jldw + ",2)) as 车流吨公里,\r\n" +
                "  sum(round(运费收入/" + jldw + ",2)) as 运费收入,\r\n" +
                "  sum(round(基准/" + jldw + ",2)) as 基准,\r\n" +
                "  sum(round(高出归己/" + jldw + ",2)) as 高出归己,\r\n" +
                "  sum(round(付本局其他高出/" + jldw + ",2)) as 付本局其他高出,\r\n" +
                "  sum(round(付外局高出/" + jldw + ",2)) as 付外局高出,\r\n" +
                "  sum(round(承运收入/" + jldw + ",2)) as 承运收入,\r\n" +
                "  sum(round(承运付费/" + jldw + ",2)) as 承运付费,\r\n" +
                "  sum(round(付国铁/" + jldw + ",2)) as 付国铁,\r\n" +
                "  sum(round(付乌准/" + jldw + ",2)) as 付乌准,\r\n" +
                "  sum(round(付奎北/" + jldw + ",2)) as 付奎北,\r\n" +
                "  sum(round(付库俄/" + jldw + ",2)) as 付库俄,\r\n" +
                "  sum(round(付哈罗/" + jldw + ",2)) as 付哈罗,\r\n" +
                "  sum(round(付外局/" + jldw + ",2)) as 付外局,\r\n" +
                "  sum(round(本企业取得服务/" + jldw + ",2)) as 本企业取得服务,\r\n" +
                "  sum(round(本局取得服务/" + jldw + ",2)) as 本局取得服务,\r\n" +
                "  sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业货票盈余,\r\n" +
                "  sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业承运盈余,\r\n" +
                "  sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 全局承运盈余,\r\n" +
                "  (case when abs(sum(round(计费吨公里/" + jldw + ",2))) > 0 then round(sum(round(运费收入/" + jldw + ",2))/sum(round(计费吨公里/" + jldw + ",2)),2) else 0 end) as 计费吨公里运费,\r\n" +
                "  (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 本企业货票盈余率,\r\n" +
                "  (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)),2) else 0 end) as 本企业货票运费盈余率,\r\n" +
                "  (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 全局承运盈余率,\r\n" +
                "  (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)) ,2) else 0 end) as 全局承运运费盈余率,\r\n" +
                "  (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 本企业单车货票盈余,\r\n" +
                "  (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 全局单车承运盈余\r\n" +
                "  from temp_承运汇总_车型 \r\n" +
                ")b\r\n" +
                "on a.mc=b.车型\r\n" +
                "order by a.hh";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getFb2_pl(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String tj = "";//添加所有条件
        String dw = searchInfo.getDw();//单位（企业）
        String cz = searchInfo.getCz();//车站
        String jm = searchInfo.getJm();//局码
        String yjh = searchInfo.getYjh();//运价号
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            tj += " and 企业代码 ='" + dw + "'";
        }
        if (cz != null && !cz.equals("") && !cz.equals("0")) {
            tj += " and 电报码 ='" + cz + "'";
        }
        if (jm != null && !jm.equals("") && !jm.equals("0")) {
            tj += " and 到局码 ='" + jm + "'";
        }
        if (yjh != null && !yjh.equals("") && !yjh.equals("0")) {
            tj += " and 运价号 ='" + yjh + "'";
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String sql = "delete from temp_承运汇总_品类";
        jdbcTemplate.update(sql);
        sql = "insert into temp_承运汇总_品类\n" +
                "--create table temp_承运汇总_品类 as\n" +
                "select 品类,  \n" +
                "sum(车数) as 车数, \n" +
                "sum(箱数) as 箱数,\n" +
                "sum(计费重量) as 计费重量,\n" +
                "sum(计费吨公里) as 计费吨公里,\n" +
                "sum(车流吨公里) as 车流吨公里,\n" +
                "sum(运费收入) as 运费收入,\n" +
                "sum(基准) as 基准,\n" +
                "sum(高出归己) as 高出归己,\n" +
                "sum(付本局其他高出) as 付本局其他高出,\n" +
                "sum(付外局高出) as 付外局高出,\n" +
                "sum(承运收入) as 承运收入,\n" +
                "sum(承运付费) as 承运付费,\n" +
                "sum(付国铁) as 付国铁,\n" +
                "sum(付乌准) as 付乌准,\n" +
                "sum(付奎北) as 付奎北,\n" +
                "sum(付库俄) as 付库俄,\n" +
                "sum(付哈罗) as 付哈罗,\n" +
                "sum(付外局) as 付外局,\n" +
                "sum(本企业取得服务) as 本企业取得服务,\n" +
                "sum(本局取得服务) as 本局取得服务\n" +
                "from HUIZONG_承运汇总 t \n" +
                "where t.日期 >=" + ksrq + " and t.日期 <=" + jsrq + " \n" + tj +
                "group by 品类";
        jdbcTemplate.update(sql);
        sql = "select a.HH,a.PM,b.车数,b.计费重量,b.计费吨公里,b.车流吨公里,b.运费收入,b.基准,b.高出归己,b.付本局其他高出,b.付外局高出,\n" +
                "b.承运收入,b.承运付费,b.付国铁,b.付乌准,b.付奎北,b.付库俄,b.付哈罗,b.付外局,b.本企业取得服务,b.本局取得服务,b.本企业货票盈余,b.本企业承运盈余,\n" +
                "b.全局承运盈余,b.计费吨公里运费,b.本企业货票盈余率,b.本企业货票运费盈余率,b.全局承运盈余率,b.全局承运运费盈余率,b.本企业单车货票盈余,b.全局单车承运盈余 from hdy_pl a\n" +
                "left join\n" +
                "(\n" +
                "  select 品类,\n" +
                "  sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数,\r\n" +
                "  sum(round(计费重量/" + jldw + ",2)) as 计费重量,\n" +
                "  sum(round(计费吨公里/" + jldw + ",2)) as 计费吨公里,\n" +
                "  sum(round(车流吨公里/" + jldw + ",2)) as 车流吨公里,\n" +
                "  sum(round(运费收入/" + jldw + ",2)) as 运费收入,\n" +
                "  sum(round(基准/" + jldw + ",2)) as 基准,\n" +
                "  sum(round(高出归己/" + jldw + ",2)) as 高出归己,\n" +
                "  sum(round(付本局其他高出/" + jldw + ",2)) as 付本局其他高出,\n" +
                "  sum(round(付外局高出/" + jldw + ",2)) as 付外局高出,\n" +
                "  sum(round(承运收入/" + jldw + ",2)) as 承运收入,\n" +
                "  sum(round(承运付费/" + jldw + ",2)) as 承运付费,\n" +
                "  sum(round(付国铁/" + jldw + ",2)) as 付国铁,\n" +
                "  sum(round(付乌准/" + jldw + ",2)) as 付乌准,\n" +
                "  sum(round(付奎北/" + jldw + ",2)) as 付奎北,\n" +
                "  sum(round(付库俄/" + jldw + ",2)) as 付库俄,\n" +
                "  sum(round(付哈罗/" + jldw + ",2)) as 付哈罗,\n" +
                "  sum(round(付外局/" + jldw + ",2)) as 付外局,\n" +
                "  sum(round(本企业取得服务/" + jldw + ",2)) as 本企业取得服务,\n" +
                "  sum(round(本局取得服务/" + jldw + ",2)) as 本局取得服务,\n" +
                "  sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业货票盈余,\n" +
                "  sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业承运盈余,\n" +
                "  sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 全局承运盈余,\n" +
                "  (case when abs(sum(round(计费吨公里/" + jldw + ",2))) > 0 then round(sum(round(运费收入/" + jldw + ",2))/sum(round(计费吨公里/" + jldw + ",2)),2) else 0 end) as 计费吨公里运费,\n" +
                "  (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 本企业货票盈余率,\n" +
                "  (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)),2) else 0 end) as 本企业货票运费盈余率,\n" +
                "  (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 全局承运盈余率,\n" +
                "  (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)) ,2) else 0 end) as 全局承运运费盈余率,\n" +
                "  (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 本企业单车货票盈余,\n" +
                "  (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 全局单车承运盈余\n" +
                "  from temp_承运汇总_品类 \n" +
                "  group by 品类\n" +
                "  union all\n" +
                "  select '30' 品类,\n" +
                "  sum(车数) as 车数,\n" +
                "  sum(round(计费重量/" + jldw + ",2)) as 计费重量,\n" +
                "  sum(round(计费吨公里/" + jldw + ",2)) as 计费吨公里,\n" +
                "  sum(round(车流吨公里/" + jldw + ",2)) as 车流吨公里,\n" +
                "  sum(round(运费收入/" + jldw + ",2)) as 运费收入,\n" +
                "  sum(round(基准/" + jldw + ",2)) as 基准,\n" +
                "  sum(round(高出归己/" + jldw + ",2)) as 高出归己,\n" +
                "  sum(round(付本局其他高出/" + jldw + ",2)) as 付本局其他高出,\n" +
                "  sum(round(付外局高出/" + jldw + ",2)) as 付外局高出,\n" +
                "  sum(round(承运收入/" + jldw + ",2)) as 承运收入,\n" +
                "  sum(round(承运付费/" + jldw + ",2)) as 承运付费,\n" +
                "  sum(round(付国铁/" + jldw + ",2)) as 付国铁,\n" +
                "  sum(round(付乌准/" + jldw + ",2)) as 付乌准,\n" +
                "  sum(round(付奎北/" + jldw + ",2)) as 付奎北,\n" +
                "  sum(round(付库俄/" + jldw + ",2)) as 付库俄,\n" +
                "  sum(round(付哈罗/" + jldw + ",2)) as 付哈罗,\n" +
                "  sum(round(付外局/" + jldw + ",2)) as 付外局,\n" +
                "  sum(round(本企业取得服务/" + jldw + ",2)) as 本企业取得服务,\n" +
                "  sum(round(本局取得服务/" + jldw + ",2)) as 本局取得服务,\n" +
                "  sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业货票盈余,\n" +
                "  sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业承运盈余,\n" +
                "  sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 全局承运盈余,\n" +
                "  (case when abs(sum(round(计费吨公里/" + jldw + ",2))) > 0 then round(sum(round(运费收入/" + jldw + ",2))/sum(round(计费吨公里/" + jldw + ",2)),2) else 0 end) as 计费吨公里运费,\n" +
                "  (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 本企业货票盈余率,\n" +
                "  (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)),2) else 0 end) as 本企业货票运费盈余率,\n" +
                "  (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 全局承运盈余率,\n" +
                "  (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)) ,2) else 0 end) as 全局承运运费盈余率,\n" +
                "  (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 本企业单车货票盈余,\n" +
                "  (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 全局单车承运盈余\n" +
                "  from temp_承运汇总_品类 \n" +
                ") b\n" +
                "on a.PMDM=b.品类\n" +
                "order by a.hh";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getFb2_zz(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String tj = "";//添加所有条件
        String dw = searchInfo.getDw();//单位（企业）
        String cz = searchInfo.getCz();//车站
        String jm = searchInfo.getJm();//局码
        String yjh = searchInfo.getYjh();//运价号
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            tj += " and 企业代码 ='" + dw + "'";
        }
        if (cz != null && !cz.equals("") && !cz.equals("0")) {
            tj += " and 电报码 ='" + cz + "'";
        }
        if (jm != null && !jm.equals("") && !jm.equals("0")) {
            tj += " and 到局码 ='" + jm + "'";
        }
        if (yjh != null && !yjh.equals("") && !yjh.equals("0")) {
            tj += " and 运价号 ='" + yjh + "'";
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String sql = "delete from temp_承运汇总_载重";
        jdbcTemplate.update(sql);
        sql = "insert into temp_承运汇总_载重\n" +
                "select 静载重,  \n" +
                "sum(车数) as 车数, \n" +
                "sum(箱数) as 箱数,\n" +
                "sum(计费重量) as 计费重量,\n" +
                "sum(计费吨公里) as 计费吨公里,\n" +
                "sum(车流吨公里) as 车流吨公里,\n" +
                "sum(运费收入) as 运费收入,\n" +
                "sum(基准) as 基准,\n" +
                "sum(高出归己) as 高出归己,\n" +
                "sum(付本局其他高出) as 付本局其他高出,\n" +
                "sum(付外局高出) as 付外局高出,\n" +
                "sum(承运收入) as 承运收入,\n" +
                "sum(承运付费) as 承运付费,\n" +
                "sum(付国铁) as 付国铁,\n" +
                "sum(付乌准) as 付乌准,\n" +
                "sum(付奎北) as 付奎北,\n" +
                "sum(付库俄) as 付库俄,\n" +
                "sum(付哈罗) as 付哈罗,\n" +
                "sum(付外局) as 付外局,\n" +
                "sum(本企业取得服务) as 本企业取得服务,\n" +
                "sum(本局取得服务) as 本局取得服务\n" +
                "from HUIZONG_承运汇总 t \n" +
                "where t.日期 >=" + ksrq + " and t.日期 <=" + jsrq + " \n" + tj +
                "group by 静载重";
        jdbcTemplate.update(sql);
        sql = "select a.HH,a.MC,b.车数,b.计费重量,b.计费吨公里,b.车流吨公里,b.运费收入,b.基准,b.高出归己,b.付本局其他高出,\r\n" +
                "b.付外局高出,b.承运收入,b.承运付费,b.付国铁,b.付乌准,b.付奎北,b.付库俄,b.付哈罗,b.付外局,b.本企业取得服务,b.本局取得服务,\r\n" +
                "b.本企业货票盈余,b.本企业承运盈余,b.全局承运盈余,b.计费吨公里运费,b.本企业货票盈余率,b.本企业货票运费盈余率,b.全局承运盈余率\r\n" +
                ",b.全局承运运费盈余率,b.本企业单车货票盈余,b.全局单车承运盈余\r\n" +
                " from hdy_cyhz_fb2_jzz a\r\n" +
                "left join\r\n" +
                "(\r\n" +
                "  select 静载重,\r\n" +
                "  sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数,\r\n" +
                "  sum(round(计费重量/" + jldw + ",2)) as 计费重量,\r\n" +
                "  sum(round(计费吨公里/" + jldw + ",2)) as 计费吨公里,\r\n" +
                "  sum(round(车流吨公里/" + jldw + ",2)) as 车流吨公里,\r\n" +
                "  sum(round(运费收入/" + jldw + ",2)) as 运费收入,\r\n" +
                "  sum(round(基准/" + jldw + ",2)) as 基准,\r\n" +
                "  sum(round(高出归己/" + jldw + ",2)) as 高出归己,\r\n" +
                "  sum(round(付本局其他高出/" + jldw + ",2)) as 付本局其他高出,\r\n" +
                "  sum(round(付外局高出/" + jldw + ",2)) as 付外局高出,\r\n" +
                "  sum(round(承运收入/" + jldw + ",2)) as 承运收入,\r\n" +
                "  sum(round(承运付费/" + jldw + ",2)) as 承运付费,\r\n" +
                "  sum(round(付国铁/" + jldw + ",2)) as 付国铁,\r\n" +
                "  sum(round(付乌准/" + jldw + ",2)) as 付乌准,\r\n" +
                "  sum(round(付奎北/" + jldw + ",2)) as 付奎北,\r\n" +
                "  sum(round(付库俄/" + jldw + ",2)) as 付库俄,\r\n" +
                "  sum(round(付哈罗/" + jldw + ",2)) as 付哈罗,\r\n" +
                "  sum(round(付外局/" + jldw + ",2)) as 付外局,\r\n" +
                "  sum(round(本企业取得服务/" + jldw + ",2)) as 本企业取得服务,\r\n" +
                "  sum(round(本局取得服务/" + jldw + ",2)) as 本局取得服务,\r\n" +
                "  sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业货票盈余,\r\n" +
                "  sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业承运盈余,\r\n" +
                "  sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 全局承运盈余,\r\n" +
                "  (case when abs(sum(round(计费吨公里/" + jldw + ",2))) > 0 then round(sum(round(运费收入/" + jldw + ",2))/sum(round(计费吨公里/" + jldw + ",2)),2) else 0 end) as 计费吨公里运费,\r\n" +
                "  (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 本企业货票盈余率,\r\n" +
                "  (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)),2) else 0 end) as 本企业货票运费盈余率,\r\n" +
                "  (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 全局承运盈余率,\r\n" +
                "  (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)) ,2) else 0 end) as 全局承运运费盈余率,\r\n" +
                "  (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 本企业单车货票盈余,\r\n" +
                "  (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 全局单车承运盈余\r\n" +
                "  from temp_承运汇总_载重 \r\n" +
                "  group by 静载重\r\n" +
                "  union all\r\n" +
                "  select '1' 静载重,\r\n" +
                "  sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数,\r\n" +
                "  sum(round(计费重量/" + jldw + ",2)) as 计费重量,\r\n" +
                "  sum(round(计费吨公里/" + jldw + ",2)) as 计费吨公里,\r\n" +
                "  sum(round(车流吨公里/" + jldw + ",2)) as 车流吨公里,\r\n" +
                "  sum(round(运费收入/" + jldw + ",2)) as 运费收入,\r\n" +
                "  sum(round(基准/" + jldw + ",2)) as 基准,\r\n" +
                "  sum(round(高出归己/" + jldw + ",2)) as 高出归己,\r\n" +
                "  sum(round(付本局其他高出/" + jldw + ",2)) as 付本局其他高出,\r\n" +
                "  sum(round(付外局高出/" + jldw + ",2)) as 付外局高出,\r\n" +
                "  sum(round(承运收入/" + jldw + ",2)) as 承运收入,\r\n" +
                "  sum(round(承运付费/" + jldw + ",2)) as 承运付费,\r\n" +
                "  sum(round(付国铁/" + jldw + ",2)) as 付国铁,\r\n" +
                "  sum(round(付乌准/" + jldw + ",2)) as 付乌准,\r\n" +
                "  sum(round(付奎北/" + jldw + ",2)) as 付奎北,\r\n" +
                "  sum(round(付库俄/" + jldw + ",2)) as 付库俄,\r\n" +
                "  sum(round(付哈罗/" + jldw + ",2)) as 付哈罗,\r\n" +
                "  sum(round(付外局/" + jldw + ",2)) as 付外局,\r\n" +
                "  sum(round(本企业取得服务/" + jldw + ",2)) as 本企业取得服务,\r\n" +
                "  sum(round(本局取得服务/" + jldw + ",2)) as 本局取得服务,\r\n" +
                "  sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业货票盈余,\r\n" +
                "  sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业承运盈余,\r\n" +
                "  sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 全局承运盈余,\r\n" +
                "  (case when abs(sum(round(计费吨公里/" + jldw + ",2))) > 0 then round(sum(round(运费收入/" + jldw + ",2))/sum(round(计费吨公里/" + jldw + ",2)),2) else 0 end) as 计费吨公里运费,\r\n" +
                "  (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 本企业货票盈余率,\r\n" +
                "  (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)),2) else 0 end) as 本企业货票运费盈余率,\r\n" +
                "  (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 全局承运盈余率,\r\n" +
                "  (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)) ,2) else 0 end) as 全局承运运费盈余率,\r\n" +
                "  (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 本企业单车货票盈余,\r\n" +
                "  (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 全局单车承运盈余\r\n" +
                "  from temp_承运汇总_载重 \r\n" +
                ")b\r\n" +
                "on a.hh=b.静载重\r\n" +
                "order by a.hh";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getFb2_yj(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String tj = "";//添加所有条件
        String dw = searchInfo.getDw();//单位（企业）
        String cz = searchInfo.getCz();//车站
        String jm = searchInfo.getJm();//局码
        String yjh = searchInfo.getYjh();//运价号
        if (cz != null && !cz.equals("") && !cz.equals("0")) {
            tj += " and 电报码 ='" + cz + "'";
        }
        if (jm != null && !jm.equals("") && !jm.equals("0")) {
            tj += " and 到局码 ='" + jm + "'";
        }
        if (yjh != null && !yjh.equals("") && !yjh.equals("0")) {
            tj += " and 运价号 ='" + yjh + "'";
        }
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            tj += " and 企业代码 ='" + dw + "'";
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String sql = "delete from temp_承运汇总_运距";
        jdbcTemplate.update(sql);
        sql = "insert into temp_承运汇总_运距\r\n" +
                "  select 运距,\r\n" +
                "    sum(车数) as 车数, \r\n" +
                "    sum(箱数) as 箱数,\r\n" +
                "    sum(计费重量) as 计费重量,\r\n" +
                "    sum(计费吨公里) as 计费吨公里,\r\n" +
                "    sum(车流吨公里) as 车流吨公里,\r\n" +
                "    sum(运费收入) as 运费收入,\r\n" +
                "    sum(基准) as 基准,\r\n" +
                "    sum(高出归己) as 高出归己,\r\n" +
                "    sum(付本局其他高出) as 付本局其他高出,\r\n" +
                "    sum(付外局高出) as 付外局高出,\r\n" +
                "    sum(承运收入) as 承运收入,\r\n" +
                "    sum(承运付费) as 承运付费,\r\n" +
                "    sum(付国铁) as 付国铁,\r\n" +
                "    sum(付乌准) as 付乌准,\r\n" +
                "    sum(付奎北) as 付奎北,\r\n" +
                "    sum(付库俄) as 付库俄,\r\n" +
                "    sum(付哈罗) as 付哈罗,\r\n" +
                "    sum(付外局) as 付外局,\r\n" +
                "    sum(本企业取得服务) as 本企业取得服务,\r\n" +
                "    sum(本局取得服务) as 本局取得服务\r\n" +
                "  from\r\n" +
                "    HUIZONG_承运汇总 t\r\n" +
                "  where \r\n" +
                "    t.日期 >= " + ksrq + " and t.日期 <=" + jsrq + tj + "\r\n" +
                "  group by 运距";
        jdbcTemplate.update(sql);
        sql = "select \r\n" +
                "    a.HH,a.MC,b.车数,b.计费重量,b.计费吨公里,b.车流吨公里,b.运费收入,b.基准,b.高出归己,b.付本局其他高出,b.付外局高出,\r\n" +
                "    b.承运收入,b.承运付费,b.付国铁,b.付乌准,b.付奎北,b.付库俄,b.付哈罗,b.付外局,b.本企业取得服务,b.本局取得服务,b.本企业货票盈余,\r\n" +
                "    b.本企业承运盈余,b.全局承运盈余,b.计费吨公里运费,b.本企业货票盈余率,b.本企业货票运费盈余率,b.全局承运盈余率,b.全局承运运费盈余率,\r\n" +
                "    b.本企业单车货票盈余,b.全局单车承运盈余\r\n" +
                "  from \r\n" +
                "    hdy_cyhz_fb2_yunju a\r\n" +
                "    left join\r\n" +
                "      (\r\n" +
                "        select \r\n" +
                "          运距,\r\n" +
                "          sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数,\r\n" +
                "          sum(round(计费重量/" + jldw + ",2)) as 计费重量,\r\n" +
                "          sum(round(计费吨公里/" + jldw + ",2)) as 计费吨公里,\r\n" +
                "          sum(round(车流吨公里/" + jldw + ",2)) as 车流吨公里,\r\n" +
                "          sum(round(运费收入/" + jldw + ",2)) as 运费收入,\r\n" +
                "          sum(round(基准/" + jldw + ",2)) as 基准,\r\n" +
                "          sum(round(高出归己/" + jldw + ",2)) as 高出归己,\r\n" +
                "          sum(round(付本局其他高出/" + jldw + ",2)) as 付本局其他高出,\r\n" +
                "          sum(round(付外局高出/" + jldw + ",2)) as 付外局高出,\r\n" +
                "          sum(round(承运收入/" + jldw + ",2)) as 承运收入,\r\n" +
                "          sum(round(承运付费/" + jldw + ",2)) as 承运付费,\r\n" +
                "          sum(round(付国铁/" + jldw + ",2)) as 付国铁,\r\n" +
                "          sum(round(付乌准/" + jldw + ",2)) as 付乌准,\r\n" +
                "          sum(round(付奎北/" + jldw + ",2)) as 付奎北,\r\n" +
                "          sum(round(付库俄/" + jldw + ",2)) as 付库俄,\r\n" +
                "          sum(round(付哈罗/" + jldw + ",2)) as 付哈罗,\r\n" +
                "          sum(round(付外局/" + jldw + ",2)) as 付外局,\r\n" +
                "          sum(round(本企业取得服务/" + jldw + ",2)) as 本企业取得服务,\r\n" +
                "          sum(round(本局取得服务/" + jldw + ",2)) as 本局取得服务,\r\n" +
                "          sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业货票盈余,\r\n" +
                "          sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业承运盈余,\r\n" +
                "          sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 全局承运盈余,\r\n" +
                "          (case when abs(sum(round(计费吨公里/" + jldw + ",2))) > 0 then round(sum(round(运费收入/" + jldw + ",2))/sum(round(计费吨公里/" + jldw + ",2)),2) else 0 end) as 计费吨公里运费,\r\n" +
                "          (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 本企业货票盈余率,\r\n" +
                "          (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)),2) else 0 end) as 本企业货票运费盈余率,\r\n" +
                "          (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 全局承运盈余率,\r\n" +
                "          (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)) ,2) else 0 end) as 全局承运运费盈余率,\r\n" +
                "          (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 本企业单车货票盈余,\r\n" +
                "          (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 全局单车承运盈余\r\n" +
                "          from temp_承运汇总_运距 \r\n" +
                "          group by 运距 \r\n" +
                "          union all\r\n" +
                "          select '1' 运距,\r\n" +
                "          sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数,\r\n" +
                "          sum(round(计费重量/" + jldw + ",2)) as 计费重量,\r\n" +
                "          sum(round(计费吨公里/" + jldw + ",2)) as 计费吨公里,\r\n" +
                "          sum(round(车流吨公里/" + jldw + ",2)) as 车流吨公里,\r\n" +
                "          sum(round(运费收入/" + jldw + ",2)) as 运费收入,\r\n" +
                "          sum(round(基准/" + jldw + ",2)) as 基准,\r\n" +
                "          sum(round(高出归己/" + jldw + ",2)) as 高出归己,\r\n" +
                "          sum(round(付本局其他高出/" + jldw + ",2)) as 付本局其他高出,\r\n" +
                "          sum(round(付外局高出/" + jldw + ",2)) as 付外局高出,\r\n" +
                "          sum(round(承运收入/" + jldw + ",2)) as 承运收入,\r\n" +
                "          sum(round(承运付费/" + jldw + ",2)) as 承运付费,\r\n" +
                "          sum(round(付国铁/" + jldw + ",2)) as 付国铁,\r\n" +
                "          sum(round(付乌准/" + jldw + ",2)) as 付乌准,\r\n" +
                "          sum(round(付奎北/" + jldw + ",2)) as 付奎北,\r\n" +
                "          sum(round(付库俄/" + jldw + ",2)) as 付库俄,\r\n" +
                "          sum(round(付哈罗/" + jldw + ",2)) as 付哈罗,\r\n" +
                "          sum(round(付外局/" + jldw + ",2)) as 付外局,\r\n" +
                "          sum(round(本企业取得服务/" + jldw + ",2)) as 本企业取得服务,\r\n" +
                "          sum(round(本局取得服务/" + jldw + ",2)) as 本局取得服务,\r\n" +
                "          sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业货票盈余,\r\n" +
                "          sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业承运盈余,\r\n" +
                "          sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 全局承运盈余,\r\n" +
                "          (case when abs(sum(round(计费吨公里/" + jldw + ",2))) > 0 then round(sum(round(运费收入/" + jldw + ",2))/sum(round(计费吨公里/" + jldw + ",2)),2) else 0 end) as 计费吨公里运费,\r\n" +
                "          (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 本企业货票盈余率,\r\n" +
                "          (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)),2) else 0 end) as 本企业货票运费盈余率,\r\n" +
                "          (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 全局承运盈余率,\r\n" +
                "          (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)) ,2) else 0 end) as 全局承运运费盈余率,\r\n" +
                "          (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 本企业单车货票盈余,\r\n" +
                "          (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 全局单车承运盈余\r\n" +
                "          from temp_承运汇总_运距 \r\n" +
                "      )b\r\n" +
                "      on a.hh = b.运距\r\n" +
                "    order by a.hh";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getFb2_yjh(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String tj = "";//添加所有条件
        String dw = searchInfo.getDw();//单位（企业）
        String cz = searchInfo.getCz();//车站
        String jm = searchInfo.getJm();//局码
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            tj += " and 企业代码 ='" + dw + "'";
        }
        if (cz != null && !cz.equals("") && !cz.equals("0")) {
            tj += " and 电报码 ='" + cz + "'";
        }
        if (jm != null && !jm.equals("") && !jm.equals("0")) {
            tj += " and 到局码 ='" + jm + "'";
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String sql = "delete from temp_承运汇总_运价号";
        jdbcTemplate.update(sql);
        sql = "insert into temp_承运汇总_运价号\r\n" +
                "  select 运价号,\r\n" +
                "    sum(车数) as 车数, \r\n" +
                "    sum(箱数) as 箱数,\r\n" +
                "    sum(计费重量) as 计费重量,\r\n" +
                "    sum(计费吨公里) as 计费吨公里,\r\n" +
                "    sum(车流吨公里) as 车流吨公里,\r\n" +
                "    sum(运费收入) as 运费收入,\r\n" +
                "    sum(基准) as 基准,\r\n" +
                "    sum(高出归己) as 高出归己,\r\n" +
                "    sum(付本局其他高出) as 付本局其他高出,\r\n" +
                "    sum(付外局高出) as 付外局高出,\r\n" +
                "    sum(承运收入) as 承运收入,\r\n" +
                "    sum(承运付费) as 承运付费,\r\n" +
                "    sum(付国铁) as 付国铁,\r\n" +
                "    sum(付乌准) as 付乌准,\r\n" +
                "    sum(付奎北) as 付奎北,\r\n" +
                "    sum(付库俄) as 付库俄,\r\n" +
                "    sum(付哈罗) as 付哈罗,\r\n" +
                "    sum(付外局) as 付外局,\r\n" +
                "    sum(本企业取得服务) as 本企业取得服务,\r\n" +
                "    sum(本局取得服务) as 本局取得服务\r\n" +
                "  from\r\n" +
                "    HUIZONG_承运汇总 t\r\n" +
                "  where \r\n" +
                "    t.日期 >= " + ksrq + " and t.日期 <=" + jsrq + tj + "\r\n" +
                "  group by 运价号";
        jdbcTemplate.update(sql);
        sql = "select \r\n" +
                "    a.HH,a.MC,b.车数,b.计费重量,b.计费吨公里,b.车流吨公里,b.运费收入,b.基准,b.高出归己,b.付本局其他高出,b.付外局高出,\r\n" +
                "    b.承运收入,b.承运付费,b.付国铁,b.付乌准,b.付奎北,b.付库俄,b.付哈罗,b.付外局,b.本企业取得服务,b.本局取得服务,b.本企业货票盈余,\r\n" +
                "    b.本企业承运盈余,b.全局承运盈余,b.计费吨公里运费,b.本企业货票盈余率,b.本企业货票运费盈余率,b.全局承运盈余率,b.全局承运运费盈余率,\r\n" +
                "    b.本企业单车货票盈余,b.全局单车承运盈余\r\n" +
                "from \r\n" +
                "    hdy_cyhz_fb2_yjh a\r\n" +
                "left join\r\n" +
                "      (\r\n" +
                "        select \r\n" +
                "          运价号,\r\n" +
                "          sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数,\r\n" +
                "          sum(round(计费重量/" + jldw + ",2)) as 计费重量,\r\n" +
                "          sum(round(计费吨公里/" + jldw + ",2)) as 计费吨公里,\r\n" +
                "          sum(round(车流吨公里/" + jldw + ",2)) as 车流吨公里,\r\n" +
                "          sum(round(运费收入/" + jldw + ",2)) as 运费收入,\r\n" +
                "          sum(round(基准/" + jldw + ",2)) as 基准,\r\n" +
                "          sum(round(高出归己/" + jldw + ",2)) as 高出归己,\r\n" +
                "          sum(round(付本局其他高出/" + jldw + ",2)) as 付本局其他高出,\r\n" +
                "          sum(round(付外局高出/" + jldw + ",2)) as 付外局高出,\r\n" +
                "          sum(round(承运收入/" + jldw + ",2)) as 承运收入,\r\n" +
                "          sum(round(承运付费/" + jldw + ",2)) as 承运付费,\r\n" +
                "          sum(round(付国铁/" + jldw + ",2)) as 付国铁,\r\n" +
                "          sum(round(付乌准/" + jldw + ",2)) as 付乌准,\r\n" +
                "          sum(round(付奎北/" + jldw + ",2)) as 付奎北,\r\n" +
                "          sum(round(付库俄/" + jldw + ",2)) as 付库俄,\r\n" +
                "          sum(round(付哈罗/" + jldw + ",2)) as 付哈罗,\r\n" +
                "          sum(round(付外局/" + jldw + ",2)) as 付外局,\r\n" +
                "          sum(round(本企业取得服务/" + jldw + ",2)) as 本企业取得服务,\r\n" +
                "          sum(round(本局取得服务/" + jldw + ",2)) as 本局取得服务,\r\n" +
                "          sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业货票盈余,\r\n" +
                "          sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业承运盈余,\r\n" +
                "          sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 全局承运盈余,\r\n" +
                "          (case when abs(sum(round(计费吨公里/" + jldw + ",2))) > 0 then round(sum(round(运费收入/" + jldw + ",2))/sum(round(计费吨公里/" + jldw + ",2)),2) else 0 end) as 计费吨公里运费,\r\n" +
                "          (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 本企业货票盈余率,\r\n" +
                "          (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)),2) else 0 end) as 本企业货票运费盈余率,\r\n" +
                "          (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 全局承运盈余率,\r\n" +
                "          (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)) ,2) else 0 end) as 全局承运运费盈余率,\r\n" +
                "          (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 本企业单车货票盈余,\r\n" +
                "          (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 全局单车承运盈余\r\n" +
                "          from temp_承运汇总_运价号 \r\n" +
                "          group by 运价号 \r\n" +
                "          union all\r\n" +
                "          select '合计' 运价号,\r\n" +
                "          sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数,\r\n" +
                "          sum(round(计费重量/" + jldw + ",2)) as 计费重量,\r\n" +
                "          sum(round(计费吨公里/" + jldw + ",2)) as 计费吨公里,\r\n" +
                "          sum(round(车流吨公里/" + jldw + ",2)) as 车流吨公里,\r\n" +
                "          sum(round(运费收入/" + jldw + ",2)) as 运费收入,\r\n" +
                "          sum(round(基准/" + jldw + ",2)) as 基准,\r\n" +
                "          sum(round(高出归己/" + jldw + ",2)) as 高出归己,\r\n" +
                "          sum(round(付本局其他高出/" + jldw + ",2)) as 付本局其他高出,\r\n" +
                "          sum(round(付外局高出/" + jldw + ",2)) as 付外局高出,\r\n" +
                "          sum(round(承运收入/" + jldw + ",2)) as 承运收入,\r\n" +
                "          sum(round(承运付费/" + jldw + ",2)) as 承运付费,\r\n" +
                "          sum(round(付国铁/" + jldw + ",2)) as 付国铁,\r\n" +
                "          sum(round(付乌准/" + jldw + ",2)) as 付乌准,\r\n" +
                "          sum(round(付奎北/" + jldw + ",2)) as 付奎北,\r\n" +
                "          sum(round(付库俄/" + jldw + ",2)) as 付库俄,\r\n" +
                "          sum(round(付哈罗/" + jldw + ",2)) as 付哈罗,\r\n" +
                "          sum(round(付外局/" + jldw + ",2)) as 付外局,\r\n" +
                "          sum(round(本企业取得服务/" + jldw + ",2)) as 本企业取得服务,\r\n" +
                "          sum(round(本局取得服务/" + jldw + ",2)) as 本局取得服务,\r\n" +
                "          sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业货票盈余,\r\n" +
                "          sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业承运盈余,\r\n" +
                "          sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 全局承运盈余,\r\n" +
                "          (case when abs(sum(round(计费吨公里/" + jldw + ",2))) > 0 then round(sum(round(运费收入/" + jldw + ",2))/sum(round(计费吨公里/" + jldw + ",2)),2) else 0 end) as 计费吨公里运费,\r\n" +
                "          (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 本企业货票盈余率,\r\n" +
                "          (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)),2) else 0 end) as 本企业货票运费盈余率,\r\n" +
                "          (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 全局承运盈余率,\r\n" +
                "          (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)) ,2) else 0 end) as 全局承运运费盈余率,\r\n" +
                "          (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 本企业单车货票盈余,\r\n" +
                "          (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 全局单车承运盈余\r\n" +
                "          from temp_承运汇总_运价号 \r\n" +
                "      )b\r\n" +
                "on a.yjh = b.运价号\r\n" +
                "order by a.hh";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getFb2_ysfs(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String tj = "";//添加所有条件
        String dw = searchInfo.getDw();//单位（企业）
        String cz = searchInfo.getCz();//车站
        String jm = searchInfo.getJm();//局码
        String yjh = searchInfo.getYjh();//运价号
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            tj += " and 企业代码 ='" + dw + "'";
        }
        if (cz != null && !cz.equals("") && !cz.equals("0")) {
            tj += " and 电报码 ='" + cz + "'";
        }
        if (jm != null && !jm.equals("") && !jm.equals("0")) {
            tj += " and 到局码 ='" + jm + "'";
        }
        if (yjh != null && !yjh.equals("") && !yjh.equals("0")) {
            tj += " and 运价号 ='" + yjh + "'";
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String sql = "delete from temp_承运汇总_运输方式";
        jdbcTemplate.update(sql);
        sql = "insert into temp_承运汇总_运输方式\r\n" +
                "select 运输方式,  \r\n" +
                "sum(车数) as 车数, \r\n" +
                "sum(箱数) as 箱数,\r\n" +
                "sum(计费重量) as 计费重量,\r\n" +
                "sum(计费吨公里) as 计费吨公里,\r\n" +
                "sum(车流吨公里) as 车流吨公里,\r\n" +
                "sum(运费收入) as 运费收入,\r\n" +
                "sum(基准) as 基准,\r\n" +
                "sum(高出归己) as 高出归己,\r\n" +
                "sum(付本局其他高出) as 付本局其他高出,\r\n" +
                "sum(付外局高出) as 付外局高出,\r\n" +
                "sum(承运收入) as 承运收入,\r\n" +
                "sum(承运付费) as 承运付费,\r\n" +
                "sum(付国铁) as 付国铁,\r\n" +
                "sum(付乌准) as 付乌准,\r\n" +
                "sum(付奎北) as 付奎北,\r\n" +
                "sum(付库俄) as 付库俄,\r\n" +
                "sum(付哈罗) as 付哈罗,\r\n" +
                "sum(付外局) as 付外局,\r\n" +
                "sum(本企业取得服务) as 本企业取得服务,\r\n" +
                "sum(本局取得服务) as 本局取得服务\r\n" +
                "from HUIZONG_承运汇总 t \r\n" +
                "where t.日期 >=" + ksrq + " and t.日期 <=" + jsrq + tj +
                "group by 运输方式";
        jdbcTemplate.update(sql);
        sql = "select a.HH,a.MC,b.车数,b.计费重量,b.计费吨公里,b.车流吨公里,b.运费收入,b.基准,b.高出归己,b.付本局其他高出,\r\n" +
                "b.付外局高出,b.承运收入,b.承运付费,b.付国铁,b.付乌准,b.付奎北,b.付库俄,b.付哈罗,b.付外局,b.本企业取得服务,b.本局取得服务,\r\n" +
                "b.本企业货票盈余,b.本企业承运盈余,b.全局承运盈余,b.计费吨公里运费,b.本企业货票盈余率,b.本企业货票运费盈余率,b.全局承运盈余率,\r\n" +
                "b.全局承运运费盈余率,b.本企业单车货票盈余,b.全局单车承运盈余\r\n" +
                " from hdy_cyhz_fb2_ysfs a\r\n" +
                "left join\r\n" +
                "(\r\n" +
                "  select 运输方式,\r\n" +
                "  sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数,\r\n" +
                "  sum(round(计费重量/" + jldw + ",2)) as 计费重量,\r\n" +
                "  sum(round(计费吨公里/" + jldw + ",2)) as 计费吨公里,\r\n" +
                "  sum(round(车流吨公里/" + jldw + ",2)) as 车流吨公里,\r\n" +
                "  sum(round(运费收入/" + jldw + ",2)) as 运费收入,\r\n" +
                "  sum(round(基准/" + jldw + ",2)) as 基准,\r\n" +
                "  sum(round(高出归己/" + jldw + ",2)) as 高出归己,\r\n" +
                "  sum(round(付本局其他高出/" + jldw + ",2)) as 付本局其他高出,\r\n" +
                "  sum(round(付外局高出/" + jldw + ",2)) as 付外局高出,\r\n" +
                "  sum(round(承运收入/" + jldw + ",2)) as 承运收入,\r\n" +
                "  sum(round(承运付费/" + jldw + ",2)) as 承运付费,\r\n" +
                "  sum(round(付国铁/" + jldw + ",2)) as 付国铁,\r\n" +
                "  sum(round(付乌准/" + jldw + ",2)) as 付乌准,\r\n" +
                "  sum(round(付奎北/" + jldw + ",2)) as 付奎北,\r\n" +
                "  sum(round(付库俄/" + jldw + ",2)) as 付库俄,\r\n" +
                "  sum(round(付哈罗/" + jldw + ",2)) as 付哈罗,\r\n" +
                "  sum(round(付外局/" + jldw + ",2)) as 付外局,\r\n" +
                "  sum(round(本企业取得服务/" + jldw + ",2)) as 本企业取得服务,\r\n" +
                "  sum(round(本局取得服务/" + jldw + ",2)) as 本局取得服务,\r\n" +
                "  sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业货票盈余,\r\n" +
                "  sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业承运盈余,\r\n" +
                "  sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 全局承运盈余,\r\n" +
                "  (case when abs(sum(round(计费吨公里/" + jldw + ",2))) > 0 then round(sum(round(运费收入/" + jldw + ",2))/sum(round(计费吨公里/" + jldw + ",2)),2) else 0 end) as 计费吨公里运费,\r\n" +
                "  (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 本企业货票盈余率,\r\n" +
                "  (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)),2) else 0 end) as 本企业货票运费盈余率,\r\n" +
                "  (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 全局承运盈余率,\r\n" +
                "  (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)) ,2) else 0 end) as 全局承运运费盈余率,\r\n" +
                "  (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 本企业单车货票盈余,\r\n" +
                "  (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 全局单车承运盈余\r\n" +
                "  from temp_承运汇总_运输方式 \r\n" +
                "  group by 运输方式\r\n" +
                "  union all\r\n" +
                "  select '合计' 运输方式,\r\n" +
                "  sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数,\r\n" +
                "  sum(round(计费重量/" + jldw + ",2)) as 计费重量,\r\n" +
                "  sum(round(计费吨公里/" + jldw + ",2)) as 计费吨公里,\r\n" +
                "  sum(round(车流吨公里/" + jldw + ",2)) as 车流吨公里,\r\n" +
                "  sum(round(运费收入/" + jldw + ",2)) as 运费收入,\r\n" +
                "  sum(round(基准/" + jldw + ",2)) as 基准,\r\n" +
                "  sum(round(高出归己/" + jldw + ",2)) as 高出归己,\r\n" +
                "  sum(round(付本局其他高出/" + jldw + ",2)) as 付本局其他高出,\r\n" +
                "  sum(round(付外局高出/" + jldw + ",2)) as 付外局高出,\r\n" +
                "  sum(round(承运收入/" + jldw + ",2)) as 承运收入,\r\n" +
                "  sum(round(承运付费/" + jldw + ",2)) as 承运付费,\r\n" +
                "  sum(round(付国铁/" + jldw + ",2)) as 付国铁,\r\n" +
                "  sum(round(付乌准/" + jldw + ",2)) as 付乌准,\r\n" +
                "  sum(round(付奎北/" + jldw + ",2)) as 付奎北,\r\n" +
                "  sum(round(付库俄/" + jldw + ",2)) as 付库俄,\r\n" +
                "  sum(round(付哈罗/" + jldw + ",2)) as 付哈罗,\r\n" +
                "  sum(round(付外局/" + jldw + ",2)) as 付外局,\r\n" +
                "  sum(round(本企业取得服务/" + jldw + ",2)) as 本企业取得服务,\r\n" +
                "  sum(round(本局取得服务/" + jldw + ",2)) as 本局取得服务,\r\n" +
                "  sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业货票盈余,\r\n" +
                "  sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 本企业承运盈余,\r\n" +
                "  sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) as 全局承运盈余,\r\n" +
                "  (case when abs(sum(round(计费吨公里/" + jldw + ",2))) > 0 then round(sum(round(运费收入/" + jldw + ",2))/sum(round(计费吨公里/" + jldw + ",2)),2) else 0 end) as 计费吨公里运费,\r\n" +
                "  (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 本企业货票盈余率,\r\n" +
                "  (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)),2) else 0 end) as 本企业货票运费盈余率,\r\n" +
                "  (case when abs(sum(round(承运收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(承运收入/" + jldw + ",2)),2) else 0 end) as 全局承运盈余率,\r\n" +
                "  (case when abs(sum(round(运费收入/" + jldw + ",2))) > 0 then round(sum(round(付本局其他高出/" + jldw + ",2)) + sum(round(本局取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(运费收入/" + jldw + ",2)) ,2) else 0 end) as 全局承运运费盈余率,\r\n" +
                "  (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 本企业单车货票盈余,\r\n" +
                "  (case when abs(sum(round(计费重量/" + jldw + ",2))) > 0 then round(sum(round(本企业取得服务/" + jldw + ",2)) + sum(round(承运收入/" + jldw + ",2)) - sum(round(承运付费/" + jldw + ",2)) /sum(round(计费重量/" + jldw + ",2)),2) else 0 end) as 全局单车承运盈余\r\n" +
                "  from temp_承运汇总_运输方式 \r\n" +
                ")b\r\n" +
                "on a.zs=b.运输方式\r\n" +
                "order by a.hh";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getFb3_pl(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String tj = "";//添加所有条件
        String dw = searchInfo.getDw();//单位（企业）
        String cz = searchInfo.getCz();//车站
        String jm = searchInfo.getJm();//局码
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            tj += " and 企业代码 ='" + dw + "'";
        }
        if (cz != null && !cz.equals("") && !cz.equals("0")) {
            tj += " and 电报码 ='" + cz + "'";
        }
        if (jm != null && !jm.equals("") && !jm.equals("0")) {
            tj += " and 到局码 ='" + jm + "'";
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String sql = "delete from temp_服务收入明细";
        jdbcTemplate.update(sql);
        sql = "insert into temp_服务收入明细\r\n" +
                "--create table temp_服务收入明细 as\r\n" +
                "select  (case when 品类='99' then '26' else 品类 end)  类型,sum(计费重量) 计费重量,sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数,\r\n" +
                "sum(基准+高出归己+高出付出) 运费,sum(计费吨公里) 计费吨公里,sum(本企业吨公里) 本企业吨公里,sum(本局吨公里) 本局吨公里,''实际吨公里,''本企业实际吨公里,''本局实际吨公里,\r\n" +
                "sum(付牵引费+付线路费+付车辆费+付到达费+综合费) 本企业承运付费,sum(付牵引费) 付牵引费,sum(付牵引费_内) 付牵引费_内,sum(付牵引费_电) 付牵引费_电,sum(付线路费) 付线路费,\r\n" +
                "sum(付线路费_特) 付线路费_特,sum(付线路费_1) 付线路费_1,sum(付线路费_2) 付线路费_2,sum(付线路费_3) 付线路费_3,sum(付车辆费) 付车辆费,sum(付到达费) 付到达费,sum(付到达费整) 付到达费整,\r\n" +
                "sum(付到达费批) 付到达费批,sum(付到达费零) 付到达费零,sum(付到达费集) 付到达费集,sum(综合费) 综合费,sum(收牵引费+收线路费+收车辆费+收到达费) 本企业承运收入,sum(收牵引费) 收牵引费,\r\n" +
                "sum(收牵引费_电) 收牵引费_电,sum(收牵引费_内) 收牵引费_内,sum(收线路费) 收线路费,sum(收线路费_特) 收线路费_特,sum(收线路费_1) 收线路费_1,sum(收线路费_2) 收线路费_2,sum(收线路费_3) 收线路费_3,\r\n" +
                "sum(收车辆费) 收车辆费,sum(收到达费) 收到达费,sum(收到达费整) 收到达费整,sum(收到达费批) 收到达费批,sum(收到达费零) 收到达费零,sum(收到达费集)收到达费集,''中转服务费,''有调,''无调,\r\n" +
                "sum(集装箱使用费) 集装箱使用费,sum(使用费40) 使用费40,sum(使用费35) 使用费35,sum(使用费其他) 使用费其他,''篷布使用费,''空车\r\n" +
                "from HUIZONG_承运付费服务明细 t\r\n" +
                " where 日期 >= " + ksrq + " and 日期 <=" + jsrq + tj + "\r\n" +
                "group by 品类\r\n" +
                "union all\r\n" +
                "select '30' 类型,sum(计费重量) 计费重量,sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数,\r\n" +
                "sum(基准+高出归己+高出付出) 运费,sum(计费吨公里) 计费吨公里,sum(本企业吨公里) 本企业吨公里,sum(本局吨公里) 本局吨公里,''实际吨公里,''本企业实际吨公里,''本局实际吨公里,\r\n" +
                "sum(付牵引费+付线路费+付车辆费+付到达费+综合费) 本企业承运付费,sum(付牵引费) 付牵引费,sum(付牵引费_内) 付牵引费_内,sum(付牵引费_电) 付牵引费_电,sum(付线路费) 付线路费,\r\n" +
                "sum(付线路费_特) 付线路费_特,sum(付线路费_1) 付线路费_1,sum(付线路费_2) 付线路费_2,sum(付线路费_3) 付线路费_3,sum(付车辆费) 付车辆费,sum(付到达费) 付到达费,sum(付到达费整) 付到达费整,\r\n" +
                "sum(付到达费批) 付到达费批,sum(付到达费零) 付到达费零,sum(付到达费集) 付到达费集,sum(综合费) 综合费,sum(收牵引费+收线路费+收车辆费+收到达费) 本企业承运收入,sum(收牵引费) 收牵引费,\r\n" +
                "sum(收牵引费_电) 收牵引费_电,sum(收牵引费_内) 收牵引费_内,sum(收线路费) 收线路费,sum(收线路费_特) 收线路费_特,sum(收线路费_1) 收线路费_1,sum(收线路费_2) 收线路费_2,sum(收线路费_3) 收线路费_3,\r\n" +
                "sum(收车辆费) 收车辆费,sum(收到达费) 收到达费,sum(收到达费整) 收到达费整,sum(收到达费批) 收到达费批,sum(收到达费零) 收到达费零,sum(收到达费集)收到达费集,''中转服务费,''有调,''无调,\r\n" +
                "sum(集装箱使用费) 集装箱使用费,sum(使用费40) 使用费40,sum(使用费35) 使用费35,sum(使用费其他) 使用费其他,''篷布使用费,''空车\r\n" +
                "from HUIZONG_承运付费服务明细 t\r\n" +
                " where 日期 >= " + ksrq + " and 日期 <=" + jsrq + tj;
        jdbcTemplate.update(sql);
        sql = "select  b.hh,b.pm,\r\n" +
                "round(a.计费重量/" + jldw + ",2) as 计费重量,a.车数 ,\r\n" +
                "round(a.运费/" + jldw + ",2) as 运费收入,\r\n" +
                "round(a.计费吨公里/" + jldw + ",2) as 计费吨公里,\r\n" +
                "round(a.本企业吨公里/" + jldw + ",2) as 本企业吨公里,\r\n" +
                "round(a.本局吨公里/" + jldw + ",2) as 本局吨公里,\r\n" +
                "round(a.实际吨公里/" + jldw + ",2) as 实际吨公里,\r\n" +
                "round(a.本企业实际吨公里/" + jldw + ",2) as 本企业实际吨公里,\r\n" +
                "round(a.本局实际吨公里/" + jldw + ",2) as 本局实际吨公里,\r\n" +
                "round(a.本企业承运付费/" + jldw + ",2) as 本企业承运付费,\r\n" +
                "round(a.付牵引费/" + jldw + ",2) as 付牵引费,\r\n" +
                "round(a.付牵引费_内/" + jldw + ",2) as 付牵引费_内,\r\n" +
                "round(a.付牵引费_电/" + jldw + ",2) as 付牵引费_电,\r\n" +
                "round(a.付线路费/" + jldw + ",2) as 付线路费,\r\n" +
                "round(a.付线路费_特/" + jldw + ",2) as 付线路费_特,\r\n" +
                "round(a.付线路费_1/" + jldw + ",2) as 付线路费_1,\r\n" +
                "round(a.付线路费_2/" + jldw + ",2) as 付线路费_2,\r\n" +
                "round(a.付线路费_3/" + jldw + ",2) as 付线路费_3,\r\n" +
                "round(a.付车辆费/" + jldw + ",2) as 付车辆费,\r\n" +
                "round(a.付到达费/" + jldw + ",2) as 付到达费,\r\n" +
                "round(a.付到达费整/" + jldw + ",2) as 付到达费整,\r\n" +
                "round(a.付到达费批/" + jldw + ",2) as 付到达费批,\r\n" +
                "round(a.付到达费零/" + jldw + ",2) as 付到达费零,\r\n" +
                "round(a.付到达费集/" + jldw + ",2) as 付到达费集,\r\n" +
                "round(a.综合费/" + jldw + ",2) as 综合费,\r\n" +
                "round(a.本企业承运收入/" + jldw + ",2) as 本企业承运收入,\r\n" +
                "round(a.收牵引费/" + jldw + ",2) as 收牵引费,\r\n" +
                "round(a.收牵引费_内/" + jldw + ",2) as 收牵引费_内,\r\n" +
                "round(a.收牵引费_电/" + jldw + ",2) as 收牵引费_电,\r\n" +
                "round(a.收线路费/" + jldw + ",2) as 收线路费,\r\n" +
                "round(a.收线路费_特/" + jldw + ",2) as 收线路费_特,\r\n" +
                "round(a.收线路费_1/" + jldw + ",2) as 收线路费_1,\r\n" +
                "round(a.收线路费_2/" + jldw + ",2) as 收线路费_2,\r\n" +
                "round(a.收线路费_3/" + jldw + ",2) as 收线路费_3,\r\n" +
                "round(a.收车辆费/" + jldw + ",2) as 收车辆费,\r\n" +
                "round(a.收到达费/" + jldw + ",2) as 收到达费,\r\n" +
                "round(a.收到达费整/" + jldw + ",2) as 收到达费整,\r\n" +
                "round(a.收到达费批/" + jldw + ",2) as 收到达费批,\r\n" +
                "round(a.收到达费零/" + jldw + ",2) as 收到达费零,\r\n" +
                "round(a.收到达费集/" + jldw + ",2) as 收到达费集,\r\n" +
                "round(a.中转服务费/" + jldw + ",2) as 中转服务费,\r\n" +
                "round(a.有调/" + jldw + ",2) as 有调,\r\n" +
                "round(a.无调/" + jldw + ",2) as 无调,\r\n" +
                "round(a.集装箱使用费/" + jldw + ",2) as 集装箱使用费,\r\n" +
                "round(a.使用费40/" + jldw + ",2) as 使用费40,\r\n" +
                "round(a.使用费35/" + jldw + ",2) as 使用费35,\r\n" +
                "round(a.使用费其他/" + jldw + ",2) as 使用费其他,\r\n" +
                "round(a.篷布使用费/" + jldw + ",2) as 篷布使用费,\r\n" +
                "round(a.空车/" + jldw + ",2) as 空车,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付牵引费_内/a.计费吨公里,2) else 0 end) as 吨公里内燃机车牵引费,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付牵引费_电/a.计费吨公里,2) else 0 end) as 吨公里电力机车牵引费,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付线路费/a.计费吨公里,2) else 0 end) as 吨公里线路使用费,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付车辆费/a.计费吨公里,2) else 0 end) as 吨公里车辆服务费,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付到达费/a.计费吨公里,2) else 0 end) as 吨公里到达服务费,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付牵引费_内/a.运费,2) else 0 end) as 运费内燃机车牵引费,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付牵引费_电/a.运费,2) else 0 end) as 运费电力机车牵引费,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付线路费/a.运费,2) else 0 end) as 运费线路使用费,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付车辆费/a.运费,2) else 0 end) as 运费车辆服务费,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付到达费/a.运费,2) else 0 end) as 运费到达服务费,\r\n" +
                "(case when abs(a.本企业吨公里) > 0 then round(a.收牵引费_内/a.本企业吨公里,2) else 0 end) as 服务内燃机车牵引费,\r\n" +
                "(case when abs(a.本企业吨公里) > 0 then round(a.收牵引费_电/a.本企业吨公里,2) else 0 end) as 服务电力机车牵引费,\r\n" +
                "(case when abs(a.本企业吨公里) > 0 then round(a.收线路费/a.本企业吨公里,2) else 0 end) as 服务线路使用费,\r\n" +
                "(case when abs(a.本企业吨公里) > 0 then round(a.收车辆费/a.本企业吨公里,2) else 0 end) as 服务车辆服务费,\r\n" +
                "(case when abs(a.本企业吨公里) > 0 then round(a.收到达费/a.本企业吨公里,2) else 0 end) as 服务到达服务费\r\n" +
                "from  hdy_pl b\r\n" +
                "left join temp_服务收入明细 a   \r\n" +
                "on b.pmdm = a.类型 \r\n" +
                "order by hh";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getFb3_dj(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String tj = "";//添加所有条件
        String dw = searchInfo.getDw();//单位（企业）
        String cz = searchInfo.getCz();//车站
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            tj += " and 企业代码 ='" + dw + "'";
        }
        if (cz != null && !cz.equals("") && !cz.equals("0")) {
            tj += " and 电报码 ='" + cz + "'";
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String sql = "delete from temp_服务收入明细";
        jdbcTemplate.update(sql);
        sql = "insert into temp_服务收入明细\r\n" +
                "--create table temp_服务收入明细 as\r\n" +
                "select 到局名 类型,sum(计费重量) 计费重量,sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数,\r\n" +
                "sum(基准+高出归己+高出付出) 运费,sum(计费吨公里) 计费吨公里,sum(本企业吨公里) 本企业吨公里,sum(本局吨公里) 本局吨公里,''实际吨公里,''本企业实际吨公里,''本局实际吨公里,\r\n" +
                "sum(付牵引费+付线路费+付车辆费+付到达费+综合费) 本企业承运付费,sum(付牵引费) 付牵引费,sum(付牵引费_内) 付牵引费_内,sum(付牵引费_电) 付牵引费_电,sum(付线路费) 付线路费,\r\n" +
                "sum(付线路费_特) 付线路费_特,sum(付线路费_1) 付线路费_1,sum(付线路费_2) 付线路费_2,sum(付线路费_3) 付线路费_3,sum(付车辆费) 付车辆费,sum(付到达费) 付到达费,sum(付到达费整) 付到达费整,\r\n" +
                "sum(付到达费批) 付到达费批,sum(付到达费零) 付到达费零,sum(付到达费集) 付到达费集,sum(综合费) 综合费,sum(收牵引费+收线路费+收车辆费+收到达费) 本企业承运收入,sum(收牵引费) 收牵引费,\r\n" +
                "sum(收牵引费_电) 收牵引费_电,sum(收牵引费_内) 收牵引费_内,sum(收线路费) 收线路费,sum(收线路费_特) 收线路费_特,sum(收线路费_1) 收线路费_1,sum(收线路费_2) 收线路费_2,sum(收线路费_3) 收线路费_3,\r\n" +
                "sum(收车辆费) 收车辆费,sum(收到达费) 收到达费,sum(收到达费整) 收到达费整,sum(收到达费批) 收到达费批,sum(收到达费零) 收到达费零,sum(收到达费集)收到达费集,''中转服务费,''有调,''无调,\r\n" +
                "sum(集装箱使用费) 集装箱使用费,sum(使用费40) 使用费40,sum(使用费35) 使用费35,sum(使用费其他) 使用费其他,''篷布使用费,''空车\r\n" +
                "from HUIZONG_承运付费服务明细 t\r\n" +
                " where 日期 >= " + ksrq + " and 日期 <=" + jsrq + tj + "\r\n" +
                "group by 到局名\r\n";
        jdbcTemplate.update(sql);
        sql = "select  b.序号,b.名称,\r\n" +
                "round(a.计费重量/" + jldw + ",2) as 计费重量,a.车数 ,\r\n" +
                "round(a.运费/" + jldw + ",2) as 运费收入,\r\n" +
                "round(a.计费吨公里/" + jldw + ",2) as 计费吨公里,\r\n" +
                "round(a.本企业吨公里/" + jldw + ",2) as 本企业吨公里,\r\n" +
                "round(a.本局吨公里/" + jldw + ",2) as 本局吨公里,\r\n" +
                "round(a.实际吨公里/" + jldw + ",2) as 实际吨公里,\r\n" +
                "round(a.本企业实际吨公里/" + jldw + ",2) as 本企业实际吨公里,\r\n" +
                "round(a.本局实际吨公里/" + jldw + ",2) as 本局实际吨公里,\r\n" +
                "round(a.本企业承运付费/" + jldw + ",2) as 本企业承运付费,\r\n" +
                "round(a.付牵引费/" + jldw + ",2) as 付牵引费,\r\n" +
                "round(a.付牵引费_内/" + jldw + ",2) as 付牵引费_内,\r\n" +
                "round(a.付牵引费_电/" + jldw + ",2) as 付牵引费_电,\r\n" +
                "round(a.付线路费/" + jldw + ",2) as 付线路费,\r\n" +
                "round(a.付线路费_特/" + jldw + ",2) as 付线路费_特,\r\n" +
                "round(a.付线路费_1/" + jldw + ",2) as 付线路费_1,\r\n" +
                "round(a.付线路费_2/" + jldw + ",2) as 付线路费_2,\r\n" +
                "round(a.付线路费_3/" + jldw + ",2) as 付线路费_3,\r\n" +
                "round(a.付车辆费/" + jldw + ",2) as 付车辆费,\r\n" +
                "round(a.付到达费/" + jldw + ",2) as 付到达费,\r\n" +
                "round(a.付到达费整/" + jldw + ",2) as 付到达费整,\r\n" +
                "round(a.付到达费批/" + jldw + ",2) as 付到达费批,\r\n" +
                "round(a.付到达费零/" + jldw + ",2) as 付到达费零,\r\n" +
                "round(a.付到达费集/" + jldw + ",2) as 付到达费集,\r\n" +
                "round(a.综合费/" + jldw + ",2) as 综合费,\r\n" +
                "round(a.本企业承运收入/" + jldw + ",2) as 本企业承运收入,\r\n" +
                "round(a.收牵引费/" + jldw + ",2) as 收牵引费,\r\n" +
                "round(a.收牵引费_内/" + jldw + ",2) as 收牵引费_内,\r\n" +
                "round(a.收牵引费_电/" + jldw + ",2) as 收牵引费_电,\r\n" +
                "round(a.收线路费/" + jldw + ",2) as 收线路费,\r\n" +
                "round(a.收线路费_特/" + jldw + ",2) as 收线路费_特,\r\n" +
                "round(a.收线路费_1/" + jldw + ",2) as 收线路费_1,\r\n" +
                "round(a.收线路费_2/" + jldw + ",2) as 收线路费_2,\r\n" +
                "round(a.收线路费_3/" + jldw + ",2) as 收线路费_3,\r\n" +
                "round(a.收车辆费/" + jldw + ",2) as 收车辆费,\r\n" +
                "round(a.收到达费/" + jldw + ",2) as 收到达费,\r\n" +
                "round(a.收到达费整/" + jldw + ",2) as 收到达费整,\r\n" +
                "round(a.收到达费批/" + jldw + ",2) as 收到达费批,\r\n" +
                "round(a.收到达费零/" + jldw + ",2) as 收到达费零,\r\n" +
                "round(a.收到达费集/" + jldw + ",2) as 收到达费集,\r\n" +
                "round(a.中转服务费/" + jldw + ",2) as 中转服务费,\r\n" +
                "round(a.有调/" + jldw + ",2) as 有调,\r\n" +
                "round(a.无调/" + jldw + ",2) as 无调,\r\n" +
                "round(a.集装箱使用费/" + jldw + ",2) as 集装箱使用费,\r\n" +
                "round(a.使用费40/" + jldw + ",2) as 使用费40,\r\n" +
                "round(a.使用费35/" + jldw + ",2) as 使用费35,\r\n" +
                "round(a.使用费其他/" + jldw + ",2) as 使用费其他,\r\n" +
                "round(a.篷布使用费/" + jldw + ",2) as 篷布使用费,\r\n" +
                "round(a.空车/" + jldw + ",2) as 空车,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付牵引费_内/a.计费吨公里,2) else 0 end) as 吨公里内燃机车牵引费,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付牵引费_电/a.计费吨公里,2) else 0 end) as 吨公里电力机车牵引费,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付线路费/a.计费吨公里,2) else 0 end) as 吨公里线路使用费,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付车辆费/a.计费吨公里,2) else 0 end) as 吨公里车辆服务费,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付到达费/a.计费吨公里,2) else 0 end) as 吨公里到达服务费,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付牵引费_内/a.运费,2) else 0 end) as 运费内燃机车牵引费,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付牵引费_电/a.运费,2) else 0 end) as 运费电力机车牵引费,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付线路费/a.运费,2) else 0 end) as 运费线路使用费,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付车辆费/a.运费,2) else 0 end) as 运费车辆服务费,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付到达费/a.运费,2) else 0 end) as 运费到达服务费,\r\n" +
                "(case when abs(a.本企业吨公里) > 0 then round(a.收牵引费_内/a.本企业吨公里,2) else 0 end) as 服务内燃机车牵引费,\r\n" +
                "(case when abs(a.本企业吨公里) > 0 then round(a.收牵引费_电/a.本企业吨公里,2) else 0 end) as 服务电力机车牵引费,\r\n" +
                "(case when abs(a.本企业吨公里) > 0 then round(a.收线路费/a.本企业吨公里,2) else 0 end) as 服务线路使用费,\r\n" +
                "(case when abs(a.本企业吨公里) > 0 then round(a.收车辆费/a.本企业吨公里,2) else 0 end) as 服务车辆服务费,\r\n" +
                "(case when abs(a.本企业吨公里) > 0 then round(a.收到达费/a.本企业吨公里,2) else 0 end) as 服务到达服务费\r\n" +
                "from  SRFX_路局定义 b\r\n" +
                "left join temp_服务收入明细 a  \r\n" +
                "on b.名称 = a.类型 \r\n" +
                "order by 序号";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getFb3_zz(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String tj = "";//添加所有条件
        String dw = searchInfo.getDw();//单位（企业）
        String cz = searchInfo.getCz();//车站
        String jm = searchInfo.getJm();//局码
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            tj += " and 企业代码 ='" + dw + "'";
        }
        if (cz != null && !cz.equals("") && !cz.equals("0")) {
            tj += " and 电报码 ='" + cz + "'";
        }
        if (jm != null && !jm.equals("") && !jm.equals("0")) {
            tj += " and 到局码 ='" + jm + "'";
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String sql = "";
        sql = "delete from temp_服务收入明细";
        jdbcTemplate.update(sql);
        sql = "insert into temp_服务收入明细\r\n" +
                "--create table temp_承运台账\r\n" +
                "select \r\n" +
                "    静载重  类型,\r\n" +
                "     sum( 计费重量)  as 计费重量,sum((case when  箱数>0 then  箱数 else  车数 end)) as 车数, sum(基准+高出归己+高出付出)  as 运费收入,sum( 计费吨公里)  as 计费吨公里,\r\n" +
                "     sum( 本企业吨公里)  as 本企业吨公里, sum( 本局吨公里)  as 本局吨公里, 0 实际吨公里, 0 本企业实际吨公里,0 本局实际吨公里,sum(付牵引费+付线路费+付车辆费+付到达费+综合费)  as 本企业承运付费,\r\n" +
                "     sum( 付牵引费)  as 付牵引费,sum( 付牵引费_内)  as 付牵引费_内,sum( 付牵引费_电)  as 付牵引费_电, sum( 付线路费)  as 付线路费, sum( 付线路费_特)  as 付线路费_特,sum( 付线路费_1)  as 付线路费_1,\r\n" +
                "     sum( 付线路费_2)  as 付线路费_2,sum( 付线路费_3)  as 付线路费_3, sum( 付车辆费)  as 付车辆费,sum( 付到达费)  as 付到达费,sum( 付到达费整)  as 付到达费整,sum( 付到达费批)  as 付到达费批,\r\n" +
                "     sum( 付到达费零)  as 付到达费零, sum( 付到达费集)  as 付到达费集, sum( 综合费)  as 综合费, sum(收牵引费+收线路费+收车辆费+收到达费)  as 本企业承运收入,sum( 收牵引费)  as 收牵引费,\r\n" +
                "     sum( 收牵引费_内)  as 收牵引费_内,sum( 收牵引费_电)  as 收牵引费_电, sum( 收线路费)  as 收线路费, sum( 收线路费_特)  as 收线路费_特, sum( 收线路费_1)  as 收线路费_1, sum( 收线路费_2)  as 收线路费_2,\r\n" +
                "     sum( 收线路费_3)  as 收线路费_3,sum( 收车辆费)  as 收车辆费,sum( 收到达费)  as 收到达费,sum( 收到达费整)  as 收到达费整,sum( 收到达费批)  as 收到达费批,sum( 收到达费零)  as 收到达费零,\r\n" +
                "     sum( 收到达费集)  as 收到达费集,0 中转服务费,0 有调,0 无调,sum( 集装箱使用费)  as 集装箱使用费,sum( 使用费40)  as 使用费40,sum( 使用费35)  as 使用费35,sum( 使用费其他)  as 使用费其他,0 篷布使用费,0 空车\r\n" +
                "from\r\n" +
                "    HUIZONG_承运付费服务明细\r\n" +
                "  where 日期 >= " + ksrq + " and 日期 <=" + jsrq + tj + "\r\n" +
                "group by 静载重\r\n" +
                "union all\r\n" +
                "select \r\n" +
                "    1  类型,\r\n" +
                "     sum( 计费重量)  as 计费重量,sum((case when  箱数>0 then  箱数 else  车数 end)) as 车数, sum(基准+高出归己+高出付出)  as 运费收入,sum( 计费吨公里)  as 计费吨公里,\r\n" +
                "     sum( 本企业吨公里)  as 本企业吨公里, sum( 本局吨公里)  as 本局吨公里, 0 实际吨公里, 0 本企业实际吨公里,0 本局实际吨公里,sum(付牵引费+付线路费+付车辆费+付到达费+综合费)  as 本企业承运付费,\r\n" +
                "     sum( 付牵引费)  as 付牵引费,sum( 付牵引费_内)  as 付牵引费_内,sum( 付牵引费_电)  as 付牵引费_电, sum( 付线路费)  as 付线路费, sum( 付线路费_特)  as 付线路费_特,sum( 付线路费_1)  as 付线路费_1,\r\n" +
                "     sum( 付线路费_2)  as 付线路费_2,sum( 付线路费_3)  as 付线路费_3, sum( 付车辆费)  as 付车辆费,sum( 付到达费)  as 付到达费,sum( 付到达费整)  as 付到达费整,sum( 付到达费批)  as 付到达费批,\r\n" +
                "     sum( 付到达费零)  as 付到达费零, sum( 付到达费集)  as 付到达费集, sum( 综合费)  as 综合费, sum(收牵引费+收线路费+收车辆费+收到达费)  as 本企业承运收入,sum( 收牵引费)  as 收牵引费,\r\n" +
                "     sum( 收牵引费_内)  as 收牵引费_内,sum( 收牵引费_电)  as 收牵引费_电, sum( 收线路费)  as 收线路费, sum( 收线路费_特)  as 收线路费_特, sum( 收线路费_1)  as 收线路费_1, sum( 收线路费_2)  as 收线路费_2,\r\n" +
                "     sum( 收线路费_3)  as 收线路费_3,sum( 收车辆费)  as 收车辆费,sum( 收到达费)  as 收到达费,sum( 收到达费整)  as 收到达费整,sum( 收到达费批)  as 收到达费批,sum( 收到达费零)  as 收到达费零,\r\n" +
                "     sum( 收到达费集)  as 收到达费集,0 中转服务费,0 有调,0 无调,sum( 集装箱使用费)  as 集装箱使用费,sum( 使用费40)  as 使用费40,sum( 使用费35)  as 使用费35,sum( 使用费其他)  as 使用费其他,0 篷布使用费,0 空车\r\n" +
                "from\r\n" +
                "    HUIZONG_承运付费服务明细\r\n" +
                "  where 日期 >= " + ksrq + " and 日期 <=" + jsrq + tj;
        jdbcTemplate.update(sql);
        sql = "select \r\n" +
                "    a.hh,a.mc,\r\n" +
                "    round( t.计费重量/" + jldw + ",2) as 计费重量,\r\n" +
                "    t.车数,\r\n" +
                "    round( t.运费/" + jldw + ",2) as 运费收入,\r\n" +
                "    round( t.计费吨公里/" + jldw + ",2) as 计费吨公里,\r\n" +
                "    round( t.本企业吨公里/" + jldw + ",2) as 本企业吨公里,\r\n" +
                "    round( t.本局吨公里/" + jldw + ",2) as 本局吨公里,\r\n" +
                "    round( t.实际吨公里/" + jldw + ",2) as 实际吨公里,\r\n" +
                "    round( t.本企业实际吨公里/" + jldw + ",2) as 本企业实际吨公里,\r\n" +
                "    round( t.本局实际吨公里/" + jldw + ",2) as 本局实际吨公里,\r\n" +
                "    round( t.本企业承运付费/" + jldw + ",2) as 本企业承运付费,\r\n" +
                "    round( t.付牵引费/" + jldw + ",2) as 付牵引费,\r\n" +
                "    round( t.付牵引费_内/" + jldw + ",2) as 付牵引费_内,\r\n" +
                "    round( t.付牵引费_电/" + jldw + ",2) as 付牵引费_电,\r\n" +
                "    round( t.付线路费/" + jldw + ",2) as 付线路费,\r\n" +
                "    round( t.付线路费_特/" + jldw + ",2) as 付线路费_特,\r\n" +
                "    round( t.付线路费_1/" + jldw + ",2) as 付线路费_1,\r\n" +
                "    round( t.付线路费_2/" + jldw + ",2) as 付线路费_2,\r\n" +
                "    round( t.付线路费_3/" + jldw + ",2) as 付线路费_3,\r\n" +
                "    round( t.付车辆费/" + jldw + ",2) as 付车辆费,\r\n" +
                "    round( t.付到达费/" + jldw + ",2) as 付到达费,\r\n" +
                "    round( t.付到达费整/" + jldw + ",2) as 付到达费整,\r\n" +
                "    round( t.收到达费批/" + jldw + ",2) as 付到达费批,\r\n" +
                "    round( t.收到达费零/" + jldw + ",2) as 付到达费零,\r\n" +
                "    round( t.付到达费集/" + jldw + ",2) as 付到达费集, \r\n" +
                "    round( t.综合费/" + jldw + ",2) as 综合费,\r\n" +
                "    round( t.本企业承运收入/" + jldw + ",2) as 本企业承运收入,\r\n" +
                "    round( t.收牵引费/" + jldw + ",2) as 收牵引费,\r\n" +
                "    round( t.收牵引费_内/" + jldw + ",2) as 收牵引费_内,\r\n" +
                "    round( t.收牵引费_电/" + jldw + ",2) as 收牵引费_电,\r\n" +
                "    round( t.收线路费/" + jldw + ",2) as 收线路费,\r\n" +
                "    round( t.收线路费_特/" + jldw + ",2) as 收线路费_特,\r\n" +
                "    round( t.收线路费_1/" + jldw + ",2) as 收线路费_1,\r\n" +
                "    round( t.收线路费_2/" + jldw + ",2) as 收线路费_2,\r\n" +
                "    round( t.收线路费_3/" + jldw + ",2) as 收线路费_3,\r\n" +
                "    round( t.收车辆费/" + jldw + ",2) as 收车辆费,\r\n" +
                "    round( t.收到达费/" + jldw + ",2) as 收到达费,\r\n" +
                "    round( t.收到达费整/" + jldw + ",2) as 收到达费整,\r\n" +
                "    round( t.收到达费批/" + jldw + ",2) as 收到达费批,\r\n" +
                "    round( t.收到达费零/" + jldw + ",2) as 收到达费零,\r\n" +
                "    round( t.收到达费集/" + jldw + ",2) as 收到达费集,\r\n" +
                "    round( t.中转服务费/" + jldw + ",2) as 中转服务费,\r\n" +
                "    round( t.有调/" + jldw + ",2) as 有调,\r\n" +
                "    round( t.无调/" + jldw + ",2) as 无调,\r\n" +
                "    round( t.集装箱使用费/" + jldw + ",2) as 集装箱使用费,\r\n" +
                "    round( t.使用费40/" + jldw + ",2) as 使用费40,\r\n" +
                "    round( t.使用费35/" + jldw + ",2) as 使用费35,\r\n" +
                "    round( t.使用费其他/" + jldw + ",2) as 使用费其他,\r\n" +
                "    round( t.篷布使用费/" + jldw + ",2) as 篷布使用费,\r\n" +
                "    round( t.空车/" + jldw + ",2) as 空车,\r\n" +
                "    --每万公里承运付费率\r\n" +
                "    (case when  t.计费吨公里 >0 then round( t.付牵引费_内 /  t.计费吨公里,2) else 0 end) as 内燃机车牵引费,\r\n" +
                "    (case when  t.计费吨公里 >0 then round( t.付牵引费_电 /  t.计费吨公里,2) else 0 end) as 电力机车牵引费,\r\n" +
                "    (case when  t.计费吨公里 >0 then round( t.付线路费 /  t.计费吨公里,2) else 0 end) as 线路使用费,\r\n" +
                "    (case when  t.计费吨公里 >0 then round( t.付车辆费 /  t.计费吨公里,2) else 0 end) as 车辆服务费,\r\n" +
                "    (case when  t.计费吨公里 >0 then round( t.付到达费 /  t.计费吨公里,2) else 0 end) as 到达服务费,\r\n" +
                "    --每运费收入付费率\r\n" +
                "    (case when  t.运费 >0 then round( t.付牵引费_内 /  t.运费,2) else 0 end) as 内燃机车牵引费_s,\r\n" +
                "    (case when  t.运费 >0 then round( t.付牵引费_电 /  t.运费,2) else 0 end) as 电力机车牵引费_s,\r\n" +
                "    (case when  t.运费 >0 then round( t.付线路费 /  t.运费,2) else 0 end) as 线路使用费_s,\r\n" +
                "    (case when  t.运费 >0 then round( t.付车辆费 /  t.运费,2) else 0 end) as 车辆服务费_s,\r\n" +
                "    (case when  t.运费 >0 then round( t.付到达费 /  t.运费,2) else 0 end) as 到达服务费_s,\r\n" +
                "    --每万吨公里服务收入率\r\n" +
                "    (case when  t.本企业吨公里 >0 then round( t.收牵引费_内 /  t.本企业吨公里,2) else 0 end) as 内燃机车牵引费_f,\r\n" +
                "    (case when  t.本企业吨公里 >0 then round( t.收牵引费_电 /  t.本企业吨公里,2) else 0 end) as 电力机车牵引费_f,\r\n" +
                "    (case when  t.本企业吨公里 >0 then round( t.收线路费 /  t.本企业吨公里,2) else 0 end) as 线路使用费_f,\r\n" +
                "    (case when  t.本企业吨公里 >0 then round( t.收车辆费 /  t.本企业吨公里,2) else 0 end) as 车辆服务费_f,\r\n" +
                "    (case when  t.本企业吨公里 >0 then round( t.收到达费 /  t.本企业吨公里,2) else 0 end) as 到达服务费_f \r\n" +
                "from hdy_cyhz_fb2_jzz a\r\n" +
                "left join\r\n" +
                "    temp_服务收入明细 t\r\n" +
                "on a.hh = t.类型\r\n" +
                "order by hh";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getFb3_yjh(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String tj = "";//添加所有条件
        String dw = searchInfo.getDw();//单位（企业）
        String cz = searchInfo.getCz();//车站
        String jm = searchInfo.getJm();//局码
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            tj += " and 企业代码 ='" + dw + "'";
        }
        if (cz != null && !cz.equals("") && !cz.equals("0")) {
            tj += " and 电报码 ='" + cz + "'";
        }
        if (jm != null && !jm.equals("") && !jm.equals("0")) {
            tj += " and 到局码 ='" + jm + "'";
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String sql = "delete from temp_服务收入明细";
        jdbcTemplate.update(sql);
        sql = "insert into temp_服务收入明细\r\n" +
                "--create table temp_承运台账\r\n" +
                "select \r\n" +
                "    运价号  类型,\r\n" +
                "     sum( 计费重量)  as 计费重量,sum((case when  箱数>0 then  箱数 else  车数 end)) as 车数, sum(基准+高出归己+高出付出)  as 运费收入,sum( 计费吨公里)  as 计费吨公里,\r\n" +
                "     sum( 本企业吨公里)  as 本企业吨公里, sum( 本局吨公里)  as 本局吨公里, 0 实际吨公里, 0 本企业实际吨公里,0 本局实际吨公里,sum(付牵引费+付线路费+付车辆费+付到达费+综合费)  as 本企业承运付费,\r\n" +
                "     sum( 付牵引费)  as 付牵引费,sum( 付牵引费_内)  as 付牵引费_内,sum( 付牵引费_电)  as 付牵引费_电, sum( 付线路费)  as 付线路费, sum( 付线路费_特)  as 付线路费_特,sum( 付线路费_1)  as 付线路费_1,\r\n" +
                "     sum( 付线路费_2)  as 付线路费_2,sum( 付线路费_3)  as 付线路费_3, sum( 付车辆费)  as 付车辆费,sum( 付到达费)  as 付到达费,sum( 付到达费整)  as 付到达费整,sum( 付到达费批)  as 付到达费批,\r\n" +
                "     sum( 付到达费零)  as 付到达费零, sum( 付到达费集)  as 付到达费集, sum( 综合费)  as 综合费, sum(收牵引费+收线路费+收车辆费+收到达费)  as 本企业承运收入,sum( 收牵引费)  as 收牵引费,\r\n" +
                "     sum( 收牵引费_内)  as 收牵引费_内,sum( 收牵引费_电)  as 收牵引费_电, sum( 收线路费)  as 收线路费, sum( 收线路费_特)  as 收线路费_特, sum( 收线路费_1)  as 收线路费_1, sum( 收线路费_2)  as 收线路费_2,\r\n" +
                "     sum( 收线路费_3)  as 收线路费_3,sum( 收车辆费)  as 收车辆费,sum( 收到达费)  as 收到达费,sum( 收到达费整)  as 收到达费整,sum( 收到达费批)  as 收到达费批,sum( 收到达费零)  as 收到达费零,\r\n" +
                "     sum( 收到达费集)  as 收到达费集,0 中转服务费,0 有调,0 无调,sum( 集装箱使用费)  as 集装箱使用费,sum( 使用费40)  as 使用费40,sum( 使用费35)  as 使用费35,sum( 使用费其他)  as 使用费其他,0 篷布使用费,0 空车\r\n" +
                "from\r\n" +
                "    HUIZONG_承运付费服务明细\r\n" +
                "  where 日期 >= " + ksrq + " and 日期 <=" + jsrq + tj + "\r\n" +
                "group by 运价号\r\n" +
                "union all\r\n" +
                "select \r\n" +
                "    '合计'  类型,\r\n" +
                "     sum( 计费重量)  as 计费重量,sum((case when  箱数>0 then  箱数 else  车数 end)) as 车数, sum(基准+高出归己+高出付出)  as 运费收入,sum( 计费吨公里)  as 计费吨公里,\r\n" +
                "     sum( 本企业吨公里)  as 本企业吨公里, sum( 本局吨公里)  as 本局吨公里, 0 实际吨公里, 0 本企业实际吨公里,0 本局实际吨公里,sum(付牵引费+付线路费+付车辆费+付到达费+综合费)  as 本企业承运付费,\r\n" +
                "     sum( 付牵引费)  as 付牵引费,sum( 付牵引费_内)  as 付牵引费_内,sum( 付牵引费_电)  as 付牵引费_电, sum( 付线路费)  as 付线路费, sum( 付线路费_特)  as 付线路费_特,sum( 付线路费_1)  as 付线路费_1,\r\n" +
                "     sum( 付线路费_2)  as 付线路费_2,sum( 付线路费_3)  as 付线路费_3, sum( 付车辆费)  as 付车辆费,sum( 付到达费)  as 付到达费,sum( 付到达费整)  as 付到达费整,sum( 付到达费批)  as 付到达费批,\r\n" +
                "     sum( 付到达费零)  as 付到达费零, sum( 付到达费集)  as 付到达费集, sum( 综合费)  as 综合费, sum(收牵引费+收线路费+收车辆费+收到达费)  as 本企业承运收入,sum( 收牵引费)  as 收牵引费,\r\n" +
                "     sum( 收牵引费_内)  as 收牵引费_内,sum( 收牵引费_电)  as 收牵引费_电, sum( 收线路费)  as 收线路费, sum( 收线路费_特)  as 收线路费_特, sum( 收线路费_1)  as 收线路费_1, sum( 收线路费_2)  as 收线路费_2,\r\n" +
                "     sum( 收线路费_3)  as 收线路费_3,sum( 收车辆费)  as 收车辆费,sum( 收到达费)  as 收到达费,sum( 收到达费整)  as 收到达费整,sum( 收到达费批)  as 收到达费批,sum( 收到达费零)  as 收到达费零,\r\n" +
                "     sum( 收到达费集)  as 收到达费集,0 中转服务费,0 有调,0 无调,sum( 集装箱使用费)  as 集装箱使用费,sum( 使用费40)  as 使用费40,sum( 使用费35)  as 使用费35,sum( 使用费其他)  as 使用费其他,0 篷布使用费,0 空车\r\n" +
                "from\r\n" +
                "    HUIZONG_承运付费服务明细\r\n" +
                "  where 日期 >= " + ksrq + " and 日期 <=" + jsrq + tj;
        jdbcTemplate.update(sql);
        sql = "select \r\n" +
                "    a.hh,a.mc,\r\n" +
                "    round( t.计费重量/" + jldw + ",2) as 计费重量,\r\n" +
                "    t.车数,\r\n" +
                "    round( t.运费/" + jldw + ",2) as 运费收入,\r\n" +
                "    round( t.计费吨公里/" + jldw + ",2) as 计费吨公里,\r\n" +
                "    round( t.本企业吨公里/" + jldw + ",2) as 本企业吨公里,\r\n" +
                "    round( t.本局吨公里/" + jldw + ",2) as 本局吨公里,\r\n" +
                "    round( t.实际吨公里/" + jldw + ",2) as 实际吨公里,\r\n" +
                "    round( t.本企业实际吨公里/" + jldw + ",2) as 本企业实际吨公里,\r\n" +
                "    round( t.本局实际吨公里/" + jldw + ",2) as 本局实际吨公里,\r\n" +
                "    round( t.本企业承运付费/" + jldw + ",2) as 本企业承运付费,\r\n" +
                "    round( t.付牵引费/" + jldw + ",2) as 付牵引费,\r\n" +
                "    round( t.付牵引费_内/" + jldw + ",2) as 付牵引费_内,\r\n" +
                "    round( t.付牵引费_电/" + jldw + ",2) as 付牵引费_电,\r\n" +
                "    round( t.付线路费/" + jldw + ",2) as 付线路费,\r\n" +
                "    round( t.付线路费_特/" + jldw + ",2) as 付线路费_特,\r\n" +
                "    round( t.付线路费_1/" + jldw + ",2) as 付线路费_1,\r\n" +
                "    round( t.付线路费_2/" + jldw + ",2) as 付线路费_2,\r\n" +
                "    round( t.付线路费_3/" + jldw + ",2) as 付线路费_3,\r\n" +
                "    round( t.付车辆费/" + jldw + ",2) as 付车辆费,\r\n" +
                "    round( t.付到达费/" + jldw + ",2) as 付到达费,\r\n" +
                "    round( t.付到达费整/" + jldw + ",2) as 付到达费整,\r\n" +
                "    round( t.收到达费批/" + jldw + ",2) as 付到达费批,\r\n" +
                "    round( t.收到达费零/" + jldw + ",2) as 付到达费零,\r\n" +
                "    round( t.付到达费集/" + jldw + ",2) as 付到达费集, \r\n" +
                "    round( t.综合费/" + jldw + ",2) as 综合费,\r\n" +
                "    round( t.本企业承运收入/" + jldw + ",2) as 本企业承运收入,\r\n" +
                "    round( t.收牵引费/" + jldw + ",2) as 收牵引费,\r\n" +
                "    round( t.收牵引费_内/" + jldw + ",2) as 收牵引费_内,\r\n" +
                "    round( t.收牵引费_电/" + jldw + ",2) as 收牵引费_电,\r\n" +
                "    round( t.收线路费/" + jldw + ",2) as 收线路费,\r\n" +
                "    round( t.收线路费_特/" + jldw + ",2) as 收线路费_特,\r\n" +
                "    round( t.收线路费_1/" + jldw + ",2) as 收线路费_1,\r\n" +
                "    round( t.收线路费_2/" + jldw + ",2) as 收线路费_2,\r\n" +
                "    round( t.收线路费_3/" + jldw + ",2) as 收线路费_3,\r\n" +
                "    round( t.收车辆费/" + jldw + ",2) as 收车辆费,\r\n" +
                "    round( t.收到达费/" + jldw + ",2) as 收到达费,\r\n" +
                "    round( t.收到达费整/" + jldw + ",2) as 收到达费整,\r\n" +
                "    round( t.收到达费批/" + jldw + ",2) as 收到达费批,\r\n" +
                "    round( t.收到达费零/" + jldw + ",2) as 收到达费零,\r\n" +
                "    round( t.收到达费集/" + jldw + ",2) as 收到达费集,\r\n" +
                "    round( t.中转服务费/" + jldw + ",2) as 中转服务费,\r\n" +
                "    round( t.有调/" + jldw + ",2) as 有调,\r\n" +
                "    round( t.无调/" + jldw + ",2) as 无调,\r\n" +
                "    round( t.集装箱使用费/" + jldw + ",2) as 集装箱使用费,\r\n" +
                "    round( t.使用费40/" + jldw + ",2) as 使用费40,\r\n" +
                "    round( t.使用费35/" + jldw + ",2) as 使用费35,\r\n" +
                "    round( t.使用费其他/" + jldw + ",2) as 使用费其他,\r\n" +
                "    round( t.篷布使用费/" + jldw + ",2) as 篷布使用费,\r\n" +
                "    round( t.空车/" + jldw + ",2) as 空车,\r\n" +
                "    --每万公里承运付费率\r\n" +
                "    (case when  t.计费吨公里 >0 then round( t.付牵引费_内 /  t.计费吨公里,2) else 0 end) as 内燃机车牵引费,\r\n" +
                "    (case when  t.计费吨公里 >0 then round( t.付牵引费_电 /  t.计费吨公里,2) else 0 end) as 电力机车牵引费,\r\n" +
                "    (case when  t.计费吨公里 >0 then round( t.付线路费 /  t.计费吨公里,2) else 0 end) as 线路使用费,\r\n" +
                "    (case when  t.计费吨公里 >0 then round( t.付车辆费 /  t.计费吨公里,2) else 0 end) as 车辆服务费,\r\n" +
                "    (case when  t.计费吨公里 >0 then round( t.付到达费 /  t.计费吨公里,2) else 0 end) as 到达服务费,\r\n" +
                "    --每运费收入付费率\r\n" +
                "    (case when  t.运费 >0 then round( t.付牵引费_内 /  t.运费,2) else 0 end) as 内燃机车牵引费_s,\r\n" +
                "    (case when  t.运费 >0 then round( t.付牵引费_电 /  t.运费,2) else 0 end) as 电力机车牵引费_s,\r\n" +
                "    (case when  t.运费 >0 then round( t.付线路费 /  t.运费,2) else 0 end) as 线路使用费_s,\r\n" +
                "    (case when  t.运费 >0 then round( t.付车辆费 /  t.运费,2) else 0 end) as 车辆服务费_s,\r\n" +
                "    (case when  t.运费 >0 then round( t.付到达费 /  t.运费,2) else 0 end) as 到达服务费_s,\r\n" +
                "    --每万吨公里服务收入率\r\n" +
                "    (case when  t.本企业吨公里 >0 then round( t.收牵引费_内 /  t.本企业吨公里,2) else 0 end) as 内燃机车牵引费_f,\r\n" +
                "    (case when  t.本企业吨公里 >0 then round( t.收牵引费_电 /  t.本企业吨公里,2) else 0 end) as 电力机车牵引费_f,\r\n" +
                "    (case when  t.本企业吨公里 >0 then round( t.收线路费 /  t.本企业吨公里,2) else 0 end) as 线路使用费_f,\r\n" +
                "    (case when  t.本企业吨公里 >0 then round( t.收车辆费 /  t.本企业吨公里,2) else 0 end) as 车辆服务费_f,\r\n" +
                "    (case when  t.本企业吨公里 >0 then round( t.收到达费 /  t.本企业吨公里,2) else 0 end) as 到达服务费_f \r\n" +
                "from hdy_cyhz_fb2_YJH a\r\n" +
                "left join\r\n" +
                "    temp_服务收入明细 t\r\n" +
                "on a.yjh = t.类型\r\n" +
                "order by hh";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getFb3_yj(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String tj = "";//添加所有条件
        String dw = searchInfo.getDw();//单位（企业）
        String cz = searchInfo.getCz();//车站
        String jm = searchInfo.getJm();
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            tj += " and 企业代码 ='" + dw + "'";
        }
        if (cz != null && !cz.equals("") && !cz.equals("0")) {
            tj += " and 电报码 ='" + cz + "'";
        }
        if (jm != null && !jm.equals("") && !jm.equals("0")) {
            tj += " and 到局码 ='" + jm + "'";
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String sql = "delete from temp_服务收入明细";
        jdbcTemplate.update(sql);
        sql = "insert into temp_服务收入明细\r\n" +
                "--create table temp_承运台账\r\n" +
                "select \r\n" +
                "    运距  类型,\r\n" +
                "     sum( 计费重量)  as 计费重量,sum((case when  箱数>0 then  箱数 else  车数 end)) as 车数, sum(基准+高出归己+高出付出)  as 运费收入,sum( 计费吨公里)  as 计费吨公里,\r\n" +
                "     sum( 本企业吨公里)  as 本企业吨公里, sum( 本局吨公里)  as 本局吨公里, 0 实际吨公里, 0 本企业实际吨公里,0 本局实际吨公里,sum(付牵引费+付线路费+付车辆费+付到达费+综合费)  as 本企业承运付费,\r\n" +
                "     sum( 付牵引费)  as 付牵引费,sum( 付牵引费_内)  as 付牵引费_内,sum( 付牵引费_电)  as 付牵引费_电, sum( 付线路费)  as 付线路费, sum( 付线路费_特)  as 付线路费_特,sum( 付线路费_1)  as 付线路费_1,\r\n" +
                "     sum( 付线路费_2)  as 付线路费_2,sum( 付线路费_3)  as 付线路费_3, sum( 付车辆费)  as 付车辆费,sum( 付到达费)  as 付到达费,sum( 付到达费整)  as 付到达费整,sum( 付到达费批)  as 付到达费批,\r\n" +
                "     sum( 付到达费零)  as 付到达费零, sum( 付到达费集)  as 付到达费集, sum( 综合费)  as 综合费, sum(收牵引费+收线路费+收车辆费+收到达费)  as 本企业承运收入,sum( 收牵引费)  as 收牵引费,\r\n" +
                "     sum( 收牵引费_内)  as 收牵引费_内,sum( 收牵引费_电)  as 收牵引费_电, sum( 收线路费)  as 收线路费, sum( 收线路费_特)  as 收线路费_特, sum( 收线路费_1)  as 收线路费_1, sum( 收线路费_2)  as 收线路费_2,\r\n" +
                "     sum( 收线路费_3)  as 收线路费_3,sum( 收车辆费)  as 收车辆费,sum( 收到达费)  as 收到达费,sum( 收到达费整)  as 收到达费整,sum( 收到达费批)  as 收到达费批,sum( 收到达费零)  as 收到达费零,\r\n" +
                "     sum( 收到达费集)  as 收到达费集,0 中转服务费,0 有调,0 无调,sum( 集装箱使用费)  as 集装箱使用费,sum( 使用费40)  as 使用费40,sum( 使用费35)  as 使用费35,sum( 使用费其他)  as 使用费其他,0 篷布使用费,0 空车\r\n" +
                "from\r\n" +
                "    HUIZONG_承运付费服务明细\r\n" +
                "where 日期 >= " + ksrq + " and 日期 <=" + jsrq + tj + "\r\n" +
                "group by 运距\r\n" +
                "union all\r\n" +
                "select \r\n" +
                "    1   类型,\r\n" +
                "     sum( 计费重量)  as 计费重量,sum((case when  箱数>0 then  箱数 else  车数 end)) as 车数, sum(基准+高出归己+高出付出)  as 运费收入,sum( 计费吨公里)  as 计费吨公里,\r\n" +
                "     sum( 本企业吨公里)  as 本企业吨公里, sum( 本局吨公里)  as 本局吨公里, 0 实际吨公里, 0 本企业实际吨公里,0 本局实际吨公里,sum(付牵引费+付线路费+付车辆费+付到达费+综合费)  as 本企业承运付费,\r\n" +
                "     sum( 付牵引费)  as 付牵引费,sum( 付牵引费_内)  as 付牵引费_内,sum( 付牵引费_电)  as 付牵引费_电, sum( 付线路费)  as 付线路费, sum( 付线路费_特)  as 付线路费_特,sum( 付线路费_1)  as 付线路费_1,\r\n" +
                "     sum( 付线路费_2)  as 付线路费_2,sum( 付线路费_3)  as 付线路费_3, sum( 付车辆费)  as 付车辆费,sum( 付到达费)  as 付到达费,sum( 付到达费整)  as 付到达费整,sum( 付到达费批)  as 付到达费批,\r\n" +
                "     sum( 付到达费零)  as 付到达费零, sum( 付到达费集)  as 付到达费集, sum( 综合费)  as 综合费, sum(收牵引费+收线路费+收车辆费+收到达费)  as 本企业承运收入,sum( 收牵引费)  as 收牵引费,\r\n" +
                "     sum( 收牵引费_内)  as 收牵引费_内,sum( 收牵引费_电)  as 收牵引费_电, sum( 收线路费)  as 收线路费, sum( 收线路费_特)  as 收线路费_特, sum( 收线路费_1)  as 收线路费_1, sum( 收线路费_2)  as 收线路费_2,\r\n" +
                "     sum( 收线路费_3)  as 收线路费_3,sum( 收车辆费)  as 收车辆费,sum( 收到达费)  as 收到达费,sum( 收到达费整)  as 收到达费整,sum( 收到达费批)  as 收到达费批,sum( 收到达费零)  as 收到达费零,\r\n" +
                "     sum( 收到达费集)  as 收到达费集,0 中转服务费,0 有调,0 无调,sum( 集装箱使用费)  as 集装箱使用费,sum( 使用费40)  as 使用费40,sum( 使用费35)  as 使用费35,sum( 使用费其他)  as 使用费其他,0 篷布使用费,0 空车\r\n" +
                "from\r\n" +
                "    HUIZONG_承运付费服务明细\r\n" +
                "  where 日期 >= " + ksrq + " and 日期 <=" + jsrq + tj;
        jdbcTemplate.update(sql);
        sql = "select \r\n" +
                "    a.hh,a.mc,\r\n" +
                "    round( t.计费重量/" + jldw + ",2) as 计费重量,\r\n" +
                "    t.车数,\r\n" +
                "    round( t.运费/" + jldw + ",2) as 运费收入,\r\n" +
                "    round( t.计费吨公里/" + jldw + ",2) as 计费吨公里,\r\n" +
                "    round( t.本企业吨公里/" + jldw + ",2) as 本企业吨公里,\r\n" +
                "    round( t.本局吨公里/" + jldw + ",2) as 本局吨公里,\r\n" +
                "    round( t.实际吨公里/" + jldw + ",2) as 实际吨公里,\r\n" +
                "    round( t.本企业实际吨公里/" + jldw + ",2) as 本企业实际吨公里,\r\n" +
                "    round( t.本局实际吨公里/" + jldw + ",2) as 本局实际吨公里,\r\n" +
                "    round( t.本企业承运付费/" + jldw + ",2) as 本企业承运付费,\r\n" +
                "    round( t.付牵引费/" + jldw + ",2) as 付牵引费,\r\n" +
                "    round( t.付牵引费_内/" + jldw + ",2) as 付牵引费_内,\r\n" +
                "    round( t.付牵引费_电/" + jldw + ",2) as 付牵引费_电,\r\n" +
                "    round( t.付线路费/" + jldw + ",2) as 付线路费,\r\n" +
                "    round( t.付线路费_特/" + jldw + ",2) as 付线路费_特,\r\n" +
                "    round( t.付线路费_1/" + jldw + ",2) as 付线路费_1,\r\n" +
                "    round( t.付线路费_2/" + jldw + ",2) as 付线路费_2,\r\n" +
                "    round( t.付线路费_3/" + jldw + ",2) as 付线路费_3,\r\n" +
                "    round( t.付车辆费/" + jldw + ",2) as 付车辆费,\r\n" +
                "    round( t.付到达费/" + jldw + ",2) as 付到达费,\r\n" +
                "    round( t.付到达费整/" + jldw + ",2) as 付到达费整,\r\n" +
                "    round( t.收到达费批/" + jldw + ",2) as 付到达费批,\r\n" +
                "    round( t.收到达费零/" + jldw + ",2) as 付到达费零,\r\n" +
                "    round( t.付到达费集/" + jldw + ",2) as 付到达费集, \r\n" +
                "    round( t.综合费/" + jldw + ",2) as 综合费,\r\n" +
                "    round( t.本企业承运收入/" + jldw + ",2) as 本企业承运收入,\r\n" +
                "    round( t.收牵引费/" + jldw + ",2) as 收牵引费,\r\n" +
                "    round( t.收牵引费_内/" + jldw + ",2) as 收牵引费_内,\r\n" +
                "    round( t.收牵引费_电/" + jldw + ",2) as 收牵引费_电,\r\n" +
                "    round( t.收线路费/" + jldw + ",2) as 收线路费,\r\n" +
                "    round( t.收线路费_特/" + jldw + ",2) as 收线路费_特,\r\n" +
                "    round( t.收线路费_1/" + jldw + ",2) as 收线路费_1,\r\n" +
                "    round( t.收线路费_2/" + jldw + ",2) as 收线路费_2,\r\n" +
                "    round( t.收线路费_3/" + jldw + ",2) as 收线路费_3,\r\n" +
                "    round( t.收车辆费/" + jldw + ",2) as 收车辆费,\r\n" +
                "    round( t.收到达费/" + jldw + ",2) as 收到达费,\r\n" +
                "    round( t.收到达费整/" + jldw + ",2) as 收到达费整,\r\n" +
                "    round( t.收到达费批/" + jldw + ",2) as 收到达费批,\r\n" +
                "    round( t.收到达费零/" + jldw + ",2) as 收到达费零,\r\n" +
                "    round( t.收到达费集/" + jldw + ",2) as 收到达费集,\r\n" +
                "    round( t.中转服务费/" + jldw + ",2) as 中转服务费,\r\n" +
                "    round( t.有调/" + jldw + ",2) as 有调,\r\n" +
                "    round( t.无调/" + jldw + ",2) as 无调,\r\n" +
                "    round( t.集装箱使用费/" + jldw + ",2) as 集装箱使用费,\r\n" +
                "    round( t.使用费40/" + jldw + ",2) as 使用费40,\r\n" +
                "    round( t.使用费35/" + jldw + ",2) as 使用费35,\r\n" +
                "    round( t.使用费其他/" + jldw + ",2) as 使用费其他,\r\n" +
                "    round( t.篷布使用费/" + jldw + ",2) as 篷布使用费,\r\n" +
                "    round( t.空车/" + jldw + ",2) as 空车,\r\n" +
                "    --每万公里承运付费率\r\n" +
                "    (case when  t.计费吨公里 >0 then round( t.付牵引费_内 /  t.计费吨公里,2) else 0 end) as 内燃机车牵引费,\r\n" +
                "    (case when  t.计费吨公里 >0 then round( t.付牵引费_电 /  t.计费吨公里,2) else 0 end) as 电力机车牵引费,\r\n" +
                "    (case when  t.计费吨公里 >0 then round( t.付线路费 /  t.计费吨公里,2) else 0 end) as 线路使用费,\r\n" +
                "    (case when  t.计费吨公里 >0 then round( t.付车辆费 /  t.计费吨公里,2) else 0 end) as 车辆服务费,\r\n" +
                "    (case when  t.计费吨公里 >0 then round( t.付到达费 /  t.计费吨公里,2) else 0 end) as 到达服务费,\r\n" +
                "    --每运费收入付费率\r\n" +
                "    (case when  t.运费 >0 then round( t.付牵引费_内 /  t.运费,2) else 0 end) as 内燃机车牵引费_s,\r\n" +
                "    (case when  t.运费 >0 then round( t.付牵引费_电 /  t.运费,2) else 0 end) as 电力机车牵引费_s,\r\n" +
                "    (case when  t.运费 >0 then round( t.付线路费 /  t.运费,2) else 0 end) as 线路使用费_s,\r\n" +
                "    (case when  t.运费 >0 then round( t.付车辆费 /  t.运费,2) else 0 end) as 车辆服务费_s,\r\n" +
                "    (case when  t.运费 >0 then round( t.付到达费 /  t.运费,2) else 0 end) as 到达服务费_s,\r\n" +
                "    --每万吨公里服务收入率\r\n" +
                "    (case when  t.本企业吨公里 >0 then round( t.收牵引费_内 /  t.本企业吨公里,2) else 0 end) as 内燃机车牵引费_f,\r\n" +
                "    (case when  t.本企业吨公里 >0 then round( t.收牵引费_电 /  t.本企业吨公里,2) else 0 end) as 电力机车牵引费_f,\r\n" +
                "    (case when  t.本企业吨公里 >0 then round( t.收线路费 /  t.本企业吨公里,2) else 0 end) as 线路使用费_f,\r\n" +
                "    (case when  t.本企业吨公里 >0 then round( t.收车辆费 /  t.本企业吨公里,2) else 0 end) as 车辆服务费_f,\r\n" +
                "    (case when  t.本企业吨公里 >0 then round( t.收到达费 /  t.本企业吨公里,2) else 0 end) as 到达服务费_f \r\n" +
                "from hdy_cyhz_fb2_yunju a\r\n" +
                "left join\r\n" +
                "    temp_服务收入明细 t\r\n" +
                "on a.hh = t.类型\r\n" +
                "order by hh";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getFb3_fz(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String tj = "";//添加所有条件
        String dw = searchInfo.getDw();//单位（企业）
        String cz = searchInfo.getCz();//车站
        String jm = searchInfo.getJm();//局码
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            tj += " and 企业代码 ='" + dw + "'";
        }
        if (cz != null && !cz.equals("") && !cz.equals("0")) {
            tj += " and 电报码 ='" + cz + "'";
        }
        if (jm != null && !jm.equals("") && !jm.equals("0")) {
            tj += " and 到局码 ='" + jm + "'";
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String sql = "delete from temp_服务收入明细";
        jdbcTemplate.update(sql);
        sql = "insert into temp_服务收入明细\r\n" +
                "--create table temp_服务收入明细 as\r\n" +
                "select 电报码 类型,sum(计费重量) 计费重量,sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数,\r\n" +
                "sum(基准+高出归己+高出付出) 运费,sum(计费吨公里) 计费吨公里,sum(本企业吨公里) 本企业吨公里,sum(本局吨公里) 本局吨公里,''实际吨公里,''本企业实际吨公里,''本局实际吨公里,\r\n" +
                "sum(付牵引费+付线路费+付车辆费+付到达费+综合费) 本企业承运付费,sum(付牵引费) 付牵引费,sum(付牵引费_内) 付牵引费_内,sum(付牵引费_电) 付牵引费_电,sum(付线路费) 付线路费,\r\n" +
                "sum(付线路费_特) 付线路费_特,sum(付线路费_1) 付线路费_1,sum(付线路费_2) 付线路费_2,sum(付线路费_3) 付线路费_3,sum(付车辆费) 付车辆费,sum(付到达费) 付到达费,sum(付到达费整) 付到达费整,\r\n" +
                "sum(付到达费批) 付到达费批,sum(付到达费零) 付到达费零,sum(付到达费集) 付到达费集,sum(综合费) 综合费,sum(收牵引费+收线路费+收车辆费+收到达费) 本企业承运收入,sum(收牵引费) 收牵引费,\r\n" +
                "sum(收牵引费_电) 收牵引费_电,sum(收牵引费_内) 收牵引费_内,sum(收线路费) 收线路费,sum(收线路费_特) 收线路费_特,sum(收线路费_1) 收线路费_1,sum(收线路费_2) 收线路费_2,sum(收线路费_3) 收线路费_3,\r\n" +
                "sum(收车辆费) 收车辆费,sum(收到达费) 收到达费,sum(收到达费整) 收到达费整,sum(收到达费批) 收到达费批,sum(收到达费零) 收到达费零,sum(收到达费集)收到达费集,''中转服务费,''有调,''无调,\r\n" +
                "sum(集装箱使用费) 集装箱使用费,sum(使用费40) 使用费40,sum(使用费35) 使用费35,sum(使用费其他) 使用费其他,''篷布使用费,''空车\r\n" +
                "from HUIZONG_承运付费服务明细 t\r\n" +
                " where 日期 >= " + ksrq + " and 日期 <=" + jsrq + tj + "\r\n" +
                "group by 电报码\r\n";
        jdbcTemplate.update(sql);
        sql = "select  b.id,b.mc,\r\n" +
                "round(a.计费重量/" + jldw + ",2) as 计费重量,a.车数 ,\r\n" +
                "round(a.运费/" + jldw + ",2) as 运费收入,\r\n" +
                "round(a.计费吨公里/" + jldw + ",2) as 计费吨公里,\r\n" +
                "round(a.本企业吨公里/" + jldw + ",2) as 本企业吨公里,\r\n" +
                "round(a.本局吨公里/" + jldw + ",2) as 本局吨公里,\r\n" +
                "round(a.实际吨公里/" + jldw + ",2) as 实际吨公里,\r\n" +
                "round(a.本企业实际吨公里/" + jldw + ",2) as 本企业实际吨公里,\r\n" +
                "round(a.本局实际吨公里/" + jldw + ",2) as 本局实际吨公里,\r\n" +
                "round(a.本企业承运付费/" + jldw + ",2) as 本企业承运付费,\r\n" +
                "round(a.付牵引费/" + jldw + ",2) as 付牵引费,\r\n" +
                "round(a.付牵引费_内/" + jldw + ",2) as 付牵引费_内,\r\n" +
                "round(a.付牵引费_电/" + jldw + ",2) as 付牵引费_电,\r\n" +
                "round(a.付线路费/" + jldw + ",2) as 付线路费,\r\n" +
                "round(a.付线路费_特/" + jldw + ",2) as 付线路费_特,\r\n" +
                "round(a.付线路费_1/" + jldw + ",2) as 付线路费_1,\r\n" +
                "round(a.付线路费_2/" + jldw + ",2) as 付线路费_2,\r\n" +
                "round(a.付线路费_3/" + jldw + ",2) as 付线路费_3,\r\n" +
                "round(a.付车辆费/" + jldw + ",2) as 付车辆费,\r\n" +
                "round(a.付到达费/" + jldw + ",2) as 付到达费,\r\n" +
                "round(a.付到达费整/" + jldw + ",2) as 付到达费整,\r\n" +
                "round(a.付到达费批/" + jldw + ",2) as 付到达费批,\r\n" +
                "round(a.付到达费零/" + jldw + ",2) as 付到达费零,\r\n" +
                "round(a.付到达费集/" + jldw + ",2) as 付到达费集,\r\n" +
                "round(a.综合费/" + jldw + ",2) as 综合费,\r\n" +
                "round(a.本企业承运收入/" + jldw + ",2) as 本企业承运收入,\r\n" +
                "round(a.收牵引费/" + jldw + ",2) as 收牵引费,\r\n" +
                "round(a.收牵引费_内/" + jldw + ",2) as 收牵引费_内,\r\n" +
                "round(a.收牵引费_电/" + jldw + ",2) as 收牵引费_电,\r\n" +
                "round(a.收线路费/" + jldw + ",2) as 收线路费,\r\n" +
                "round(a.收线路费_特/" + jldw + ",2) as 收线路费_特,\r\n" +
                "round(a.收线路费_1/" + jldw + ",2) as 收线路费_1,\r\n" +
                "round(a.收线路费_2/" + jldw + ",2) as 收线路费_2,\r\n" +
                "round(a.收线路费_3/" + jldw + ",2) as 收线路费_3,\r\n" +
                "round(a.收车辆费/" + jldw + ",2) as 收车辆费,\r\n" +
                "round(a.收到达费/" + jldw + ",2) as 收到达费,\r\n" +
                "round(a.收到达费整/" + jldw + ",2) as 收到达费整,\r\n" +
                "round(a.收到达费批/" + jldw + ",2) as 收到达费批,\r\n" +
                "round(a.收到达费零/" + jldw + ",2) as 收到达费零,\r\n" +
                "round(a.收到达费集/" + jldw + ",2) as 收到达费集,\r\n" +
                "round(a.中转服务费/" + jldw + ",2) as 中转服务费,\r\n" +
                "round(a.有调/" + jldw + ",2) as 有调,\r\n" +
                "round(a.无调/" + jldw + ",2) as 无调,\r\n" +
                "round(a.集装箱使用费/" + jldw + ",2) as 集装箱使用费,\r\n" +
                "round(a.使用费40/" + jldw + ",2) as 使用费40,\r\n" +
                "round(a.使用费35/" + jldw + ",2) as 使用费35,\r\n" +
                "round(a.使用费其他/" + jldw + ",2) as 使用费其他,\r\n" +
                "round(a.篷布使用费/" + jldw + ",2) as 篷布使用费,\r\n" +
                "round(a.空车/" + jldw + ",2) as 空车,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付牵引费_内/a.计费吨公里,2) else 0 end) as 吨公里内燃机车牵引费,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付牵引费_电/a.计费吨公里,2) else 0 end) as 吨公里电力机车牵引费,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付线路费/a.计费吨公里,2) else 0 end) as 吨公里线路使用费,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付车辆费/a.计费吨公里,2) else 0 end) as 吨公里车辆服务费,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付到达费/a.计费吨公里,2) else 0 end) as 吨公里到达服务费,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付牵引费_内/a.运费,2) else 0 end) as 运费内燃机车牵引费,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付牵引费_电/a.运费,2) else 0 end) as 运费电力机车牵引费,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付线路费/a.运费,2) else 0 end) as 运费线路使用费,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付车辆费/a.运费,2) else 0 end) as 运费车辆服务费,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付到达费/a.运费,2) else 0 end) as 运费到达服务费,\r\n" +
                "(case when abs(a.本企业吨公里) > 0 then round(a.收牵引费_内/a.本企业吨公里,2) else 0 end) as 服务内燃机车牵引费,\r\n" +
                "(case when abs(a.本企业吨公里) > 0 then round(a.收牵引费_电/a.本企业吨公里,2) else 0 end) as 服务电力机车牵引费,\r\n" +
                "(case when abs(a.本企业吨公里) > 0 then round(a.收线路费/a.本企业吨公里,2) else 0 end) as 服务线路使用费,\r\n" +
                "(case when abs(a.本企业吨公里) > 0 then round(a.收车辆费/a.本企业吨公里,2) else 0 end) as 服务车辆服务费,\r\n" +
                "(case when abs(a.本企业吨公里) > 0 then round(a.收到达费/a.本企业吨公里,2) else 0 end) as 服务到达服务费\r\n" +
                "from  temp_服务收入明细 a\r\n" +
                "left join srfx_zmk b  \r\n" +
                "on b.cs4dbm = a.类型 and b.cztype = 1\r\n" +
                "order by id";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

        return list;
    }

    @Override
    public List<Map<String, Object>> getFb7_yjh(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String tj = "";//添加所有条件
        String dw = searchInfo.getDw();//单位（企业）
        String cz = searchInfo.getCz();//车站
        String jm = searchInfo.getJm();//局码
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            tj += " and 企业代码 ='" + dw + "'";
        }
        if (cz != null && !cz.equals("") && !cz.equals("0")) {
            tj += " and 电报码 ='" + cz + "'";
        }
        if (jm != null && !jm.equals("") && !jm.equals("0")) {
            tj += " and 到局码 ='" + jm + "'";
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String sql = "delete from temp_承运台账";
        jdbcTemplate.update(sql);
        sql = "insert into temp_承运台账\r\n" +
                "--create table temp_承运台账 as\r\n" +
                "select 运价号  类型,\r\n" +
                "sum(计费重量) 计费重量,sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数 ,sum(基准+高出归己+高出付出) 运费,sum(基准) 基准,sum(高出归己) 高出归己,sum(高出付出) 高出付出,\r\n" +
                "sum(付国铁高出) 付国铁高出,sum(付乌准高出) 付乌准高出,sum(付奎北高出) 付奎北高出,sum(付库俄高出) 付库俄高出,sum(付哈罗高出) 付哈罗高出,sum(付外局高出) 付外局高出,\r\n" +
                "sum(计费吨公里) 计费吨公里,sum(国铁吨公里) 国铁吨公里,sum(乌准吨公里) 乌准吨公里,sum(奎北吨公里) 奎北吨公里,sum(库俄吨公里) 库俄吨公里,sum(哈罗吨公里) 哈罗吨公里,\r\n" +
                "sum(计费吨公里-国铁吨公里-乌准吨公里-奎北吨公里-库俄吨公里-哈罗吨公里) 经外局吨公里,\r\n" +
                "sum(付牵引费_内+付牵引费_电+付线路费+付车辆费+付到达服务费+综合服务费) 承运付费,\r\n" +
                "sum(付牵引费_内) 付牵引费_内,sum(付牵引费_电) 付牵引费_电,sum(付线路费) 付线路费,sum(付车辆费) 付车辆费,sum(付到达服务费) 付到达服务费,sum(综合服务费) 综合服务费,\r\n" +
                "sum(付国铁牵引电) 付国铁牵引电,sum(付国铁牵引内) 付国铁牵引内,sum(付国铁线路) 付国铁线路,sum(付国铁车辆) 付国铁车辆,sum(付国铁到达) 付国铁到达,\r\n" +
                "sum(付乌准牵引电) 付乌准牵引电,sum(付乌准牵引内) 付乌准牵引内,sum(付乌准线路) 付乌准线路,sum(付乌准车辆) 付乌准车辆,sum(付乌准到达) 付乌准到达,\r\n" +
                "sum(付奎北牵引电) 付奎北牵引电,sum(付奎北牵引内) 付奎北牵引内,sum(付奎北线路) 付奎北线路,sum(付奎北车辆) 付奎北车辆,sum(付奎北到达) 付奎北到达,\r\n" +
                "sum(付库俄牵引电) 付库俄牵引电,sum(付库俄牵引内) 付库俄牵引内,sum(付库俄线路) 付库俄线路,sum(付库俄车辆) 付库俄车辆,sum(付库俄到达) 付库俄到达,\r\n" +
                "sum(付哈罗牵引电) 付哈罗牵引电,sum(付哈罗牵引内) 付哈罗牵引内,sum(付哈罗线路) 付哈罗线路,sum(付哈罗车辆) 付哈罗车辆,sum(付哈罗到达) 付哈罗到达,\r\n" +
                "sum(付其他牵引电) 付其他牵引电,sum(付其他牵引内) 付其他牵引内,sum(付其他线路) 付其他线路,sum(付其他车辆) 付其他车辆,sum(付其他到达) 付其他到达\r\n" +
                "from huizong_承运清算台账\r\n" +
                " where 日期 >= " + ksrq + " and 日期 <=" + jsrq + tj +
                "  group by 运价号 \r\n" +
                "union all\r\n" +
                "select '合计' 类型,\r\n" +
                "sum(计费重量) 计费重量,sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数 ,sum(基准+高出归己+高出付出) 运费,sum(基准) 基准,sum(高出归己) 高出归己,sum(高出付出) 高出付出,\r\n" +
                "sum(付国铁高出) 付国铁高出,sum(付乌准高出) 付乌准高出,sum(付奎北高出) 付奎北高出,sum(付库俄高出) 付库俄高出,sum(付哈罗高出) 付哈罗高出,sum(付外局高出) 付外局高出,\r\n" +
                "sum(计费吨公里) 计费吨公里,sum(国铁吨公里) 国铁吨公里,sum(乌准吨公里) 乌准吨公里,sum(奎北吨公里) 奎北吨公里,sum(库俄吨公里) 库俄吨公里,sum(哈罗吨公里) 哈罗吨公里,\r\n" +
                "sum(计费吨公里-国铁吨公里-乌准吨公里-奎北吨公里-库俄吨公里-哈罗吨公里) 经外局吨公里,\r\n" +
                "sum(付牵引费_内+付牵引费_电+付线路费+付车辆费+付到达服务费+综合服务费) 承运付费,\r\n" +
                "sum(付牵引费_内) 付牵引费_内,sum(付牵引费_电) 付牵引费_电,sum(付线路费) 付线路费,sum(付车辆费) 付车辆费,sum(付到达服务费) 付到达服务费,sum(综合服务费) 综合服务费,\r\n" +
                "sum(付国铁牵引电) 付国铁牵引电,sum(付国铁牵引内) 付国铁牵引内,sum(付国铁线路) 付国铁线路,sum(付国铁车辆) 付国铁车辆,sum(付国铁到达) 付国铁到达,\r\n" +
                "sum(付乌准牵引电) 付乌准牵引电,sum(付乌准牵引内) 付乌准牵引内,sum(付乌准线路) 付乌准线路,sum(付乌准车辆) 付乌准车辆,sum(付乌准到达) 付乌准到达,\r\n" +
                "sum(付奎北牵引电) 付奎北牵引电,sum(付奎北牵引内) 付奎北牵引内,sum(付奎北线路) 付奎北线路,sum(付奎北车辆) 付奎北车辆,sum(付奎北到达) 付奎北到达,\r\n" +
                "sum(付库俄牵引电) 付库俄牵引电,sum(付库俄牵引内) 付库俄牵引内,sum(付库俄线路) 付库俄线路,sum(付库俄车辆) 付库俄车辆,sum(付库俄到达) 付库俄到达,\r\n" +
                "sum(付哈罗牵引电) 付哈罗牵引电,sum(付哈罗牵引内) 付哈罗牵引内,sum(付哈罗线路) 付哈罗线路,sum(付哈罗车辆) 付哈罗车辆,sum(付哈罗到达) 付哈罗到达,\r\n" +
                "sum(付其他牵引电) 付其他牵引电,sum(付其他牵引内) 付其他牵引内,sum(付其他线路) 付其他线路,sum(付其他车辆) 付其他车辆,sum(付其他到达) 付其他到达\r\n" +
                "from huizong_承运清算台账\r\n" +
                " where 日期 >= " + ksrq + " and 日期 <=" + jsrq + tj;
        jdbcTemplate.update(sql);
        sql = "select b.hh,b.mc,round(a.计费重量/" + jldw + ",2) 计费重量,a.车数,round(a.运费/" + jldw + ",2) 运费,round(a.基准/" + jldw + ",2) 基准,round(a.高出归己/" + jldw + ",2) 高出归己,\r\n" +
                "round(a.高出付出/" + jldw + ",2) 高出付出,round(a.付国铁高出/" + jldw + ",2) 付国铁高出,round(a.付乌准高出/" + jldw + ",2) 付乌准高出,round(a.付奎北高出/" + jldw + ",2) 付奎北高出,\r\n" +
                "round(a.付库俄高出/" + jldw + ",2) 付库俄高出,round(a.付哈罗高出/" + jldw + ",2) 付哈罗高出,round(a.付外局高出/" + jldw + ",2) 付外局高出,round(a.计费吨公里/" + jldw + ",2) 计费吨公里,\r\n" +
                "round(a.国铁吨公里/" + jldw + ",2) 国铁吨公里,round(a.乌准吨公里/" + jldw + ",2) 乌准吨公里,round(a.奎北吨公里/" + jldw + ",2) 奎北吨公里s,round(a.库俄吨公里/" + jldw + ",2) 库俄吨公里,\r\n" +
                "round(a.哈罗吨公里/" + jldw + ",2) 哈罗吨公里,round(a.经外局吨公里/" + jldw + ",2) 经外局吨公里,round(a.承运付费/" + jldw + ",2) 承运付费,round(a.付牵引费_内/" + jldw + ",2) 奎北吨公里,\r\n" +
                "round(a.付牵引费_电/" + jldw + ",2) 付牵引费_电,round(a.付线路费/" + jldw + ",2) 付线路费,round(a.付车辆费/" + jldw + ",2) 付车辆费,round(a.付到达服务费/" + jldw + ",2) 付到达服务费,\r\n" +
                "round(a.综合服务费/" + jldw + ",2) 综合服务费,round(a.付国铁牵引电/" + jldw + ",2) 付国铁牵引电,round(a.付国铁牵引内/" + jldw + ",2) 付国铁牵引内,round(a.付国铁线路/" + jldw + ",2) 付国铁线路,\r\n" +
                "round(a.付国铁车辆/" + jldw + ",2) 付国铁车辆,round(a.付国铁到达/" + jldw + ",2) 付国铁到达,round(a.付乌准牵引电/" + jldw + ",2) 付乌准牵引电,round(a.付乌准牵引内/" + jldw + ",2) 付乌准牵引内,\r\n" +
                "round(a.付乌准线路/" + jldw + ",2) 付乌准线路,round(a.付乌准车辆/" + jldw + ",2) 付乌准车辆,round(a.付乌准到达/" + jldw + ",2) 付乌准到达,round(a.付奎北牵引内/" + jldw + ",2) 付奎北牵引内,\r\n" +
                "round(a.付奎北牵引电/" + jldw + ",2) 付奎北牵引电,round(a.付奎北线路/" + jldw + ",2) 付奎北线路,round(a.付奎北车辆/" + jldw + ",2) 付奎北车辆,round(a.付奎北到达/" + jldw + ",2) 付奎北到达,\r\n" +
                "round(a.付库俄牵引内/" + jldw + ",2) 付库俄牵引内,round(a.付库俄牵引电/" + jldw + ",2) 付库俄牵引电,round(a.付库俄线路/" + jldw + ",2) 付库俄线路,round(a.付库俄车辆/" + jldw + ",2) 付库俄车辆,\r\n" +
                "round(a.付库俄到达/" + jldw + ",2) 付库俄到达,round(a.付哈罗牵引内/" + jldw + ",2) 付哈罗牵引内,round(a.付哈罗牵引电/" + jldw + ",2) 付哈罗牵引电,round(a.付哈罗线路/" + jldw + ",2) 付哈罗线路,\r\n" +
                "round(a.付哈罗车辆/" + jldw + ",2) 付哈罗车辆,round(a.付哈罗到达/" + jldw + ",2) 付哈罗到达,round(a.付其他牵引内/" + jldw + ",2) 付其他牵引内,round(a.付其他牵引电/" + jldw + ",2) 付其他牵引电,\r\n" +
                "round(a.付其他线路/" + jldw + ",2) 付其他线路,round(a.付其他车辆/" + jldw + ",2) 付其他车辆,round(a.付其他到达/" + jldw + ",2) 付其他到达,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round((a.付牵引费_内 + a.付牵引费_电)/a.计费吨公里,2) else 0 end) as 计费机车牵引费,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付牵引费_内 + a.付牵引费_电 - a.付其他牵引电 - a.付其他牵引内)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局机车牵引费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round((a.付其他牵引电 + a.付其他牵引内)/a.经外局吨公里,2) else 0 end) as 付外局机车牵引费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付线路费/a.计费吨公里,2) else 0 end) as 计费线路使用费a,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付线路费 - 付其他线路)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局线路使用费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round(a.付其他线路/a.经外局吨公里,2) else 0 end) as 付外局线路使用费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付车辆费/a.计费吨公里,2) else 0 end) as 计费车辆服务费a,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付车辆费 - a.付其他车辆)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局车辆服务费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round((a.付车辆费)/(a.经外局吨公里),2) else 0 end) as 付外局车辆服务费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付到达服务费/a.计费吨公里,2) else 0 end) as 计费到达服务费a,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付到达服务费 - a.付其他到达)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局到达服务费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round(a.付其他到达/(a.经外局吨公里),2) else 0 end) as 付外局到达服务费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.综合服务费/a.计费吨公里,2) else 0 end) as 计费综合服务费a,\r\n" +
                "(case when abs(a.运费) > 0 then round((a.付牵引费_内 + a.付牵引费_电)/a.运费,2) else 0 end) as 运费收入机车牵引费b,\r\n" +
                "(case when abs(a.运费) > 0 then round((a.付其他牵引电 + a.付其他牵引内)/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round((a.付牵引费_内 + a.付牵引费_电)/a.运费,2) else 0 end) as 付本局机车牵引费b,\r\n" +
                "(case when abs(a.运费) > 0 then round((a.付其他牵引电 + a.付其他牵引内)/a.运费,2) else 0 end) as 付外局机车牵引费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付线路费/a.运费,2) else 0 end) as 运费收入线路使用费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他线路/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round(a.付线路费/a.运费,2) else 0 end) as 付本局线路使用费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他线路/a.运费,2) else 0 end) as 付外局线路使用费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付车辆费/a.运费,2) else 0 end) as 运费收入车辆服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他车辆/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round(a.付车辆费/a.运费,2) else 0 end) as 付本局车辆服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他车辆/a.运费,2) else 0 end) as 付外局车辆服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付到达服务费/a.运费,2) else 0 end) as 运费收入到达服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他到达/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round(a.付到达服务费/a.运费,2) else 0 end) as 付本局到达服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他到达/a.运费,2) else 0 end) as 付外局到达服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.综合服务费/a.运费,2) else 0 end) as 运费收入综合服务费b\r\n" +
                "from hdy_cyhz_fb2_YJH b\r\n" +
                "left join\r\n" +
                "temp_承运台账 a  \r\n" +
                "on b.yjh = a.类型\r\n" +
                "order by hh";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getFb7_yj(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String tj = "";//添加所有条件
        String dw = searchInfo.getDw();//单位（企业）
        String cz = searchInfo.getCz();//车站
        String jm = searchInfo.getJm();//局码
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            tj += " and 企业代码 ='" + dw + "'";
        }
        if (cz != null && !cz.equals("") && !cz.equals("0")) {
            tj += " and 电报码 ='" + cz + "'";
        }
        if (jm != null && !jm.equals("") && !jm.equals("0")) {
            tj += " and 到局码 ='" + jm + "'";
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String sql = "delete from temp_承运台账";
        jdbcTemplate.update(sql);
        sql = "insert into temp_承运台账\r\n" +
                "--create table temp_承运台账 as\r\n" +
                "select 运距  类型,\r\n" +
                "sum(计费重量) 计费重量,sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数 ,sum(基准+高出归己+高出付出) 运费,sum(基准) 基准,sum(高出归己) 高出归己,sum(高出付出) 高出付出,\r\n" +
                "sum(付国铁高出) 付国铁高出,sum(付乌准高出) 付乌准高出,sum(付奎北高出) 付奎北高出,sum(付库俄高出) 付库俄高出,sum(付哈罗高出) 付哈罗高出,sum(付外局高出) 付外局高出,\r\n" +
                "sum(计费吨公里) 计费吨公里,sum(国铁吨公里) 国铁吨公里,sum(乌准吨公里) 乌准吨公里,sum(奎北吨公里) 奎北吨公里,sum(库俄吨公里) 库俄吨公里,sum(哈罗吨公里) 哈罗吨公里,\r\n" +
                "sum(计费吨公里-国铁吨公里-乌准吨公里-奎北吨公里-库俄吨公里-哈罗吨公里) 经外局吨公里,\r\n" +
                "sum(付牵引费_内+付牵引费_电+付线路费+付车辆费+付到达服务费+综合服务费) 承运付费,\r\n" +
                "sum(付牵引费_内) 付牵引费_内,sum(付牵引费_电) 付牵引费_电,sum(付线路费) 付线路费,sum(付车辆费) 付车辆费,sum(付到达服务费) 付到达服务费,sum(综合服务费) 综合服务费,\r\n" +
                "sum(付国铁牵引电) 付国铁牵引电,sum(付国铁牵引内) 付国铁牵引内,sum(付国铁线路) 付国铁线路,sum(付国铁车辆) 付国铁车辆,sum(付国铁到达) 付国铁到达,\r\n" +
                "sum(付乌准牵引电) 付乌准牵引电,sum(付乌准牵引内) 付乌准牵引内,sum(付乌准线路) 付乌准线路,sum(付乌准车辆) 付乌准车辆,sum(付乌准到达) 付乌准到达,\r\n" +
                "sum(付奎北牵引电) 付奎北牵引电,sum(付奎北牵引内) 付奎北牵引内,sum(付奎北线路) 付奎北线路,sum(付奎北车辆) 付奎北车辆,sum(付奎北到达) 付奎北到达,\r\n" +
                "sum(付库俄牵引电) 付库俄牵引电,sum(付库俄牵引内) 付库俄牵引内,sum(付库俄线路) 付库俄线路,sum(付库俄车辆) 付库俄车辆,sum(付库俄到达) 付库俄到达,\r\n" +
                "sum(付哈罗牵引电) 付哈罗牵引电,sum(付哈罗牵引内) 付哈罗牵引内,sum(付哈罗线路) 付哈罗线路,sum(付哈罗车辆) 付哈罗车辆,sum(付哈罗到达) 付哈罗到达,\r\n" +
                "sum(付其他牵引电) 付其他牵引电,sum(付其他牵引内) 付其他牵引内,sum(付其他线路) 付其他线路,sum(付其他车辆) 付其他车辆,sum(付其他到达) 付其他到达\r\n" +
                "from huizong_承运清算台账\r\n" +
                " where 日期 >= " + ksrq + " and 日期 <=" + jsrq + tj +
                "  group by 运距\r\n" +
                "union all\r\n" +
                "select 1 类型,\r\n" +
                "sum(计费重量) 计费重量,sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数 ,sum(基准+高出归己+高出付出) 运费,sum(基准) 基准,sum(高出归己) 高出归己,sum(高出付出) 高出付出,\r\n" +
                "sum(付国铁高出) 付国铁高出,sum(付乌准高出) 付乌准高出,sum(付奎北高出) 付奎北高出,sum(付库俄高出) 付库俄高出,sum(付哈罗高出) 付哈罗高出,sum(付外局高出) 付外局高出,\r\n" +
                "sum(计费吨公里) 计费吨公里,sum(国铁吨公里) 国铁吨公里,sum(乌准吨公里) 乌准吨公里,sum(奎北吨公里) 奎北吨公里,sum(库俄吨公里) 库俄吨公里,sum(哈罗吨公里) 哈罗吨公里,\r\n" +
                "sum(计费吨公里-国铁吨公里-乌准吨公里-奎北吨公里-库俄吨公里-哈罗吨公里) 经外局吨公里,\r\n" +
                "sum(付牵引费_内+付牵引费_电+付线路费+付车辆费+付到达服务费+综合服务费) 承运付费,\r\n" +
                "sum(付牵引费_内) 付牵引费_内,sum(付牵引费_电) 付牵引费_电,sum(付线路费) 付线路费,sum(付车辆费) 付车辆费,sum(付到达服务费) 付到达服务费,sum(综合服务费) 综合服务费,\r\n" +
                "sum(付国铁牵引电) 付国铁牵引电,sum(付国铁牵引内) 付国铁牵引内,sum(付国铁线路) 付国铁线路,sum(付国铁车辆) 付国铁车辆,sum(付国铁到达) 付国铁到达,\r\n" +
                "sum(付乌准牵引电) 付乌准牵引电,sum(付乌准牵引内) 付乌准牵引内,sum(付乌准线路) 付乌准线路,sum(付乌准车辆) 付乌准车辆,sum(付乌准到达) 付乌准到达,\r\n" +
                "sum(付奎北牵引电) 付奎北牵引电,sum(付奎北牵引内) 付奎北牵引内,sum(付奎北线路) 付奎北线路,sum(付奎北车辆) 付奎北车辆,sum(付奎北到达) 付奎北到达,\r\n" +
                "sum(付库俄牵引电) 付库俄牵引电,sum(付库俄牵引内) 付库俄牵引内,sum(付库俄线路) 付库俄线路,sum(付库俄车辆) 付库俄车辆,sum(付库俄到达) 付库俄到达,\r\n" +
                "sum(付哈罗牵引电) 付哈罗牵引电,sum(付哈罗牵引内) 付哈罗牵引内,sum(付哈罗线路) 付哈罗线路,sum(付哈罗车辆) 付哈罗车辆,sum(付哈罗到达) 付哈罗到达,\r\n" +
                "sum(付其他牵引电) 付其他牵引电,sum(付其他牵引内) 付其他牵引内,sum(付其他线路) 付其他线路,sum(付其他车辆) 付其他车辆,sum(付其他到达) 付其他到达\r\n" +
                "from huizong_承运清算台账\r\n" +
                " where 日期 >= " + ksrq + " and 日期 <=" + jsrq + tj;
        jdbcTemplate.update(sql);
        sql = "select b.hh,b.mc,round(a.计费重量/" + jldw + ",2) 计费重量,a.车数,round(a.运费/" + jldw + ",2) 运费,round(a.基准/" + jldw + ",2) 基准,round(a.高出归己/" + jldw + ",2) 高出归己,\r\n" +
                "round(a.高出付出/" + jldw + ",2) 高出付出,round(a.付国铁高出/" + jldw + ",2) 付国铁高出,round(a.付乌准高出/" + jldw + ",2) 付乌准高出,round(a.付奎北高出/" + jldw + ",2) 付奎北高出,\r\n" +
                "round(a.付库俄高出/" + jldw + ",2) 付库俄高出,round(a.付哈罗高出/" + jldw + ",2) 付哈罗高出,round(a.付外局高出/" + jldw + ",2) 付外局高出,round(a.计费吨公里/" + jldw + ",2) 计费吨公里,\r\n" +
                "round(a.国铁吨公里/" + jldw + ",2) 国铁吨公里,round(a.乌准吨公里/" + jldw + ",2) 乌准吨公里,round(a.奎北吨公里/" + jldw + ",2) 奎北吨公里s,round(a.库俄吨公里/" + jldw + ",2) 库俄吨公里,\r\n" +
                "round(a.哈罗吨公里/" + jldw + ",2) 哈罗吨公里,round(a.经外局吨公里/" + jldw + ",2) 经外局吨公里,round(a.承运付费/" + jldw + ",2) 承运付费,round(a.付牵引费_内/" + jldw + ",2) 奎北吨公里,\r\n" +
                "round(a.付牵引费_电/" + jldw + ",2) 付牵引费_电,round(a.付线路费/" + jldw + ",2) 付线路费,round(a.付车辆费/" + jldw + ",2) 付车辆费,round(a.付到达服务费/" + jldw + ",2) 付到达服务费,\r\n" +
                "round(a.综合服务费/" + jldw + ",2) 综合服务费,round(a.付国铁牵引电/" + jldw + ",2) 付国铁牵引电,round(a.付国铁牵引内/" + jldw + ",2) 付国铁牵引内,round(a.付国铁线路/" + jldw + ",2) 付国铁线路,\r\n" +
                "round(a.付国铁车辆/" + jldw + ",2) 付国铁车辆,round(a.付国铁到达/" + jldw + ",2) 付国铁到达,round(a.付乌准牵引电/" + jldw + ",2) 付乌准牵引电,round(a.付乌准牵引内/" + jldw + ",2) 付乌准牵引内,\r\n" +
                "round(a.付乌准线路/" + jldw + ",2) 付乌准线路,round(a.付乌准车辆/" + jldw + ",2) 付乌准车辆,round(a.付乌准到达/" + jldw + ",2) 付乌准到达,round(a.付奎北牵引内/" + jldw + ",2) 付奎北牵引内,\r\n" +
                "round(a.付奎北牵引电/" + jldw + ",2) 付奎北牵引电,round(a.付奎北线路/" + jldw + ",2) 付奎北线路,round(a.付奎北车辆/" + jldw + ",2) 付奎北车辆,round(a.付奎北到达/" + jldw + ",2) 付奎北到达,\r\n" +
                "round(a.付库俄牵引内/" + jldw + ",2) 付库俄牵引内,round(a.付库俄牵引电/" + jldw + ",2) 付库俄牵引电,round(a.付库俄线路/" + jldw + ",2) 付库俄线路,round(a.付库俄车辆/" + jldw + ",2) 付库俄车辆,\r\n" +
                "round(a.付库俄到达/" + jldw + ",2) 付库俄到达,round(a.付哈罗牵引内/" + jldw + ",2) 付哈罗牵引内,round(a.付哈罗牵引电/" + jldw + ",2) 付哈罗牵引电,round(a.付哈罗线路/" + jldw + ",2) 付哈罗线路,\r\n" +
                "round(a.付哈罗车辆/" + jldw + ",2) 付哈罗车辆,round(a.付哈罗到达/" + jldw + ",2) 付哈罗到达,round(a.付其他牵引内/" + jldw + ",2) 付其他牵引内,round(a.付其他牵引电/" + jldw + ",2) 付其他牵引电,\r\n" +
                "round(a.付其他线路/" + jldw + ",2) 付其他线路,round(a.付其他车辆/" + jldw + ",2) 付其他车辆,round(a.付其他到达/" + jldw + ",2) 付其他到达,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round((a.付牵引费_内 + a.付牵引费_电)/a.计费吨公里,2) else 0 end) as 计费机车牵引费,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付牵引费_内 + a.付牵引费_电 - a.付其他牵引电 - a.付其他牵引内)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局机车牵引费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round((a.付其他牵引电 + a.付其他牵引内)/a.经外局吨公里,2) else 0 end) as 付外局机车牵引费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付线路费/a.计费吨公里,2) else 0 end) as 计费线路使用费a,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付线路费 - 付其他线路)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局线路使用费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round(a.付其他线路/a.经外局吨公里,2) else 0 end) as 付外局线路使用费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付车辆费/a.计费吨公里,2) else 0 end) as 计费车辆服务费a,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付车辆费 - a.付其他车辆)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局车辆服务费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round((a.付车辆费)/(a.经外局吨公里),2) else 0 end) as 付外局车辆服务费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付到达服务费/a.计费吨公里,2) else 0 end) as 计费到达服务费a,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付到达服务费 - a.付其他到达)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局到达服务费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round(a.付其他到达/(a.经外局吨公里),2) else 0 end) as 付外局到达服务费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.综合服务费/a.计费吨公里,2) else 0 end) as 计费综合服务费a,\r\n" +
                "(case when abs(a.运费) > 0 then round((a.付牵引费_内 + a.付牵引费_电)/a.运费,2) else 0 end) as 运费收入机车牵引费b,\r\n" +
                "(case when abs(a.运费) > 0 then round((a.付其他牵引电 + a.付其他牵引内)/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round((a.付牵引费_内 + a.付牵引费_电)/a.运费,2) else 0 end) as 付本局机车牵引费b,\r\n" +
                "(case when abs(a.运费) > 0 then round((a.付其他牵引电 + a.付其他牵引内)/a.运费,2) else 0 end) as 付外局机车牵引费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付线路费/a.运费,2) else 0 end) as 运费收入线路使用费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他线路/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round(a.付线路费/a.运费,2) else 0 end) as 付本局线路使用费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他线路/a.运费,2) else 0 end) as 付外局线路使用费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付车辆费/a.运费,2) else 0 end) as 运费收入车辆服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他车辆/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round(a.付车辆费/a.运费,2) else 0 end) as 付本局车辆服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他车辆/a.运费,2) else 0 end) as 付外局车辆服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付到达服务费/a.运费,2) else 0 end) as 运费收入到达服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他到达/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round(a.付到达服务费/a.运费,2) else 0 end) as 付本局到达服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他到达/a.运费,2) else 0 end) as 付外局到达服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.综合服务费/a.运费,2) else 0 end) as 运费收入综合服务费b\r\n" +
                "from hdy_cyhz_fb2_yunju b\r\n" +
                "left join\r\n" +
                "temp_承运台账 a  \r\n" +
                "on b.hh = a.类型\r\n" +
                "order by hh";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getFb7_pl(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String tj = "";//添加所有条件
        String dw = searchInfo.getDw();//单位（企业）
        String cz = searchInfo.getCz();//车站
        String jm = searchInfo.getJm();//局码
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            tj += " and 企业代码 ='" + dw + "'";
        }
        if (cz != null && !cz.equals("") && !cz.equals("0")) {
            tj += " and 电报码 ='" + cz + "'";
        }
        if (jm != null && !jm.equals("") && !jm.equals("0")) {
            tj += " and 到局码 ='" + jm + "'";
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String sql = "delete from temp_承运台账";
        jdbcTemplate.update(sql);
        sql = "insert into temp_承运台账\r\n" +
                "--create table temp_承运台账 as\r\n" +
                "select (case when 品类='99' then '26' else 品类 end) 类型,\r\n" +
                "sum(计费重量) 计费重量,sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数 ,sum(基准+高出归己+高出付出) 运费,sum(基准) 基准,sum(高出归己) 高出归己,sum(高出付出) 高出付出,\r\n" +
                "sum(付国铁高出) 付国铁高出,sum(付乌准高出) 付乌准高出,sum(付奎北高出) 付奎北高出,sum(付库俄高出) 付库俄高出,sum(付哈罗高出) 付哈罗高出,sum(付外局高出) 付外局高出,\r\n" +
                "sum(计费吨公里) 计费吨公里,sum(国铁吨公里) 国铁吨公里,sum(乌准吨公里) 乌准吨公里,sum(奎北吨公里) 奎北吨公里,sum(库俄吨公里) 库俄吨公里,sum(哈罗吨公里) 哈罗吨公里,\r\n" +
                "sum(计费吨公里-国铁吨公里-乌准吨公里-奎北吨公里-库俄吨公里-哈罗吨公里) 经外局吨公里,\r\n" +
                "sum(付牵引费_内+付牵引费_电+付线路费+付车辆费+付到达服务费+综合服务费) 承运付费,\r\n" +
                "sum(付牵引费_内) 付牵引费_内,sum(付牵引费_电) 付牵引费_电,sum(付线路费) 付线路费,sum(付车辆费) 付车辆费,sum(付到达服务费) 付到达服务费,sum(综合服务费) 综合服务费,\r\n" +
                "sum(付国铁牵引电) 付国铁牵引电,sum(付国铁牵引内) 付国铁牵引内,sum(付国铁线路) 付国铁线路,sum(付国铁车辆) 付国铁车辆,sum(付国铁到达) 付国铁到达,\r\n" +
                "sum(付乌准牵引电) 付乌准牵引电,sum(付乌准牵引内) 付乌准牵引内,sum(付乌准线路) 付乌准线路,sum(付乌准车辆) 付乌准车辆,sum(付乌准到达) 付乌准到达,\r\n" +
                "sum(付奎北牵引电) 付奎北牵引电,sum(付奎北牵引内) 付奎北牵引内,sum(付奎北线路) 付奎北线路,sum(付奎北车辆) 付奎北车辆,sum(付奎北到达) 付奎北到达,\r\n" +
                "sum(付库俄牵引电) 付库俄牵引电,sum(付库俄牵引内) 付库俄牵引内,sum(付库俄线路) 付库俄线路,sum(付库俄车辆) 付库俄车辆,sum(付库俄到达) 付库俄到达,\r\n" +
                "sum(付哈罗牵引电) 付哈罗牵引电,sum(付哈罗牵引内) 付哈罗牵引内,sum(付哈罗线路) 付哈罗线路,sum(付哈罗车辆) 付哈罗车辆,sum(付哈罗到达) 付哈罗到达,\r\n" +
                "sum(付其他牵引电) 付其他牵引电,sum(付其他牵引内) 付其他牵引内,sum(付其他线路) 付其他线路,sum(付其他车辆) 付其他车辆,sum(付其他到达) 付其他到达\r\n" +
                "from huizong_承运清算台账\r\n" +
                " where 日期 >= " + ksrq + " and 日期 <=" + jsrq + tj + "\r\n" +
                "group by 品类\r\n" +
                "union all\r\n" +
                "select '30' 类型,\r\n" +
                "sum(计费重量) 计费重量,sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数 ,sum(基准+高出归己+高出付出) 运费,sum(基准) 基准,sum(高出归己) 高出归己,sum(高出付出) 高出付出,\r\n" +
                "sum(付国铁高出) 付国铁高出,sum(付乌准高出) 付乌准高出,sum(付奎北高出) 付奎北高出,sum(付库俄高出) 付库俄高出,sum(付哈罗高出) 付哈罗高出,sum(付外局高出) 付外局高出,\r\n" +
                "sum(计费吨公里) 计费吨公里,sum(国铁吨公里) 国铁吨公里,sum(乌准吨公里) 乌准吨公里,sum(奎北吨公里) 奎北吨公里,sum(库俄吨公里) 库俄吨公里,sum(哈罗吨公里) 哈罗吨公里,\r\n" +
                "sum(计费吨公里-国铁吨公里-乌准吨公里-奎北吨公里-库俄吨公里-哈罗吨公里) 经外局吨公里,\r\n" +
                "sum(付牵引费_内+付牵引费_电+付线路费+付车辆费+付到达服务费+综合服务费) 承运付费,\r\n" +
                "sum(付牵引费_内) 付牵引费_内,sum(付牵引费_电) 付牵引费_电,sum(付线路费) 付线路费,sum(付车辆费) 付车辆费,sum(付到达服务费) 付到达服务费,sum(综合服务费) 综合服务费,\r\n" +
                "sum(付国铁牵引电) 付国铁牵引电,sum(付国铁牵引内) 付国铁牵引内,sum(付国铁线路) 付国铁线路,sum(付国铁车辆) 付国铁车辆,sum(付国铁到达) 付国铁到达,\r\n" +
                "sum(付乌准牵引电) 付乌准牵引电,sum(付乌准牵引内) 付乌准牵引内,sum(付乌准线路) 付乌准线路,sum(付乌准车辆) 付乌准车辆,sum(付乌准到达) 付乌准到达,\r\n" +
                "sum(付奎北牵引电) 付奎北牵引电,sum(付奎北牵引内) 付奎北牵引内,sum(付奎北线路) 付奎北线路,sum(付奎北车辆) 付奎北车辆,sum(付奎北到达) 付奎北到达,\r\n" +
                "sum(付库俄牵引电) 付库俄牵引电,sum(付库俄牵引内) 付库俄牵引内,sum(付库俄线路) 付库俄线路,sum(付库俄车辆) 付库俄车辆,sum(付库俄到达) 付库俄到达,\r\n" +
                "sum(付哈罗牵引电) 付哈罗牵引电,sum(付哈罗牵引内) 付哈罗牵引内,sum(付哈罗线路) 付哈罗线路,sum(付哈罗车辆) 付哈罗车辆,sum(付哈罗到达) 付哈罗到达,\r\n" +
                "sum(付其他牵引电) 付其他牵引电,sum(付其他牵引内) 付其他牵引内,sum(付其他线路) 付其他线路,sum(付其他车辆) 付其他车辆,sum(付其他到达) 付其他到达\r\n" +
                "from huizong_承运清算台账\r\n" +
                "  where 日期 >= " + ksrq + " and 日期 <=" + jsrq + tj;
        jdbcTemplate.update(sql);
        sql = "select b.hh,b.pm,round(a.计费重量/" + jldw + ",2) 计费重量,a.车数,round(a.运费/" + jldw + ",2) 运费,round(a.基准/" + jldw + ",2) 基准,round(a.高出归己/" + jldw + ",2) 高出归己,\r\n" +
                "round(a.高出付出/" + jldw + ",2) 高出付出,round(a.付国铁高出/" + jldw + ",2) 付国铁高出,round(a.付乌准高出/" + jldw + ",2) 付乌准高出,round(a.付奎北高出/" + jldw + ",2) 付奎北高出,\r\n" +
                "round(a.付库俄高出/" + jldw + ",2) 付库俄高出,round(a.付哈罗高出/" + jldw + ",2) 付哈罗高出,round(a.付外局高出/" + jldw + ",2) 付外局高出,round(a.计费吨公里/" + jldw + ",2) 计费吨公里,\r\n" +
                "round(a.国铁吨公里/" + jldw + ",2) 国铁吨公里,round(a.乌准吨公里/" + jldw + ",2) 乌准吨公里,round(a.奎北吨公里/" + jldw + ",2) 奎北吨公里s,round(a.库俄吨公里/" + jldw + ",2) 库俄吨公里,\r\n" +
                "round(a.哈罗吨公里/" + jldw + ",2) 哈罗吨公里,round(a.经外局吨公里/" + jldw + ",2) 经外局吨公里,round(a.承运付费/" + jldw + ",2) 承运付费,round(a.付牵引费_内/" + jldw + ",2) 奎北吨公里,\r\n" +
                "round(a.付牵引费_电/" + jldw + ",2) 付牵引费_电,round(a.付线路费/" + jldw + ",2) 付线路费,round(a.付车辆费/" + jldw + ",2) 付车辆费,round(a.付到达服务费/" + jldw + ",2) 付到达服务费,\r\n" +
                "round(a.综合服务费/" + jldw + ",2) 综合服务费,round(a.付国铁牵引电/" + jldw + ",2) 付国铁牵引电,round(a.付国铁牵引内/" + jldw + ",2) 付国铁牵引内,round(a.付国铁线路/" + jldw + ",2) 付国铁线路,\r\n" +
                "round(a.付国铁车辆/" + jldw + ",2) 付国铁车辆,round(a.付国铁到达/" + jldw + ",2) 付国铁到达,round(a.付乌准牵引电/" + jldw + ",2) 付乌准牵引电,round(a.付乌准牵引内/" + jldw + ",2) 付乌准牵引内,\r\n" +
                "round(a.付乌准线路/" + jldw + ",2) 付乌准线路,round(a.付乌准车辆/" + jldw + ",2) 付乌准车辆,round(a.付乌准到达/" + jldw + ",2) 付乌准到达,round(a.付奎北牵引内/" + jldw + ",2) 付奎北牵引内,\r\n" +
                "round(a.付奎北牵引电/" + jldw + ",2) 付奎北牵引电,round(a.付奎北线路/" + jldw + ",2) 付奎北线路,round(a.付奎北车辆/" + jldw + ",2) 付奎北车辆,round(a.付奎北到达/" + jldw + ",2) 付奎北到达,\r\n" +
                "round(a.付库俄牵引内/" + jldw + ",2) 付库俄牵引内,round(a.付库俄牵引电/" + jldw + ",2) 付库俄牵引电,round(a.付库俄线路/" + jldw + ",2) 付库俄线路,round(a.付库俄车辆/" + jldw + ",2) 付库俄车辆,\r\n" +
                "round(a.付库俄到达/" + jldw + ",2) 付库俄到达,round(a.付哈罗牵引内/" + jldw + ",2) 付哈罗牵引内,round(a.付哈罗牵引电/" + jldw + ",2) 付哈罗牵引电,round(a.付哈罗线路/" + jldw + ",2) 付哈罗线路,\r\n" +
                "round(a.付哈罗车辆/" + jldw + ",2) 付哈罗车辆,round(a.付哈罗到达/" + jldw + ",2) 付哈罗到达,round(a.付其他牵引内/" + jldw + ",2) 付其他牵引内,round(a.付其他牵引电/" + jldw + ",2) 付其他牵引电,\r\n" +
                "round(a.付其他线路/" + jldw + ",2) 付其他线路,round(a.付其他车辆/" + jldw + ",2) 付其他车辆,round(a.付其他到达/" + jldw + ",2) 付其他到达,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round((a.付牵引费_内 + a.付牵引费_电)/a.计费吨公里,2) else 0 end) as 计费机车牵引费,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付牵引费_内 + a.付牵引费_电 - a.付其他牵引电 - a.付其他牵引内)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局机车牵引费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round((a.付其他牵引电 + a.付其他牵引内)/a.经外局吨公里,2) else 0 end) as 付外局机车牵引费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付线路费/a.计费吨公里,2) else 0 end) as 计费线路使用费a,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付线路费 - 付其他线路)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局线路使用费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round(a.付其他线路/a.经外局吨公里,2) else 0 end) as 付外局线路使用费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付车辆费/a.计费吨公里,2) else 0 end) as 计费车辆服务费a,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付车辆费 - a.付其他车辆)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局车辆服务费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round((a.付车辆费)/(a.经外局吨公里),2) else 0 end) as 付外局车辆服务费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付到达服务费/a.计费吨公里,2) else 0 end) as 计费到达服务费a,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付到达服务费 - a.付其他到达)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局到达服务费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round(a.付其他到达/(a.经外局吨公里),2) else 0 end) as 付外局到达服务费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.综合服务费/a.计费吨公里,2) else 0 end) as 计费综合服务费a,\r\n" +
                "(case when abs(a.运费) > 0 then round((a.付牵引费_内 + a.付牵引费_电)/a.运费,2) else 0 end) as 运费收入机车牵引费b,\r\n" +
                "(case when abs(a.运费) > 0 then round((a.付其他牵引电 + a.付其他牵引内)/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round((a.付牵引费_内 + a.付牵引费_电)/a.运费,2) else 0 end) as 付本局机车牵引费b,\r\n" +
                "(case when abs(a.运费) > 0 then round((a.付其他牵引电 + a.付其他牵引内)/a.运费,2) else 0 end) as 付外局机车牵引费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付线路费/a.运费,2) else 0 end) as 运费收入线路使用费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他线路/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round(a.付线路费/a.运费,2) else 0 end) as 付本局线路使用费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他线路/a.运费,2) else 0 end) as 付外局线路使用费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付车辆费/a.运费,2) else 0 end) as 运费收入车辆服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他车辆/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round(a.付车辆费/a.运费,2) else 0 end) as 付本局车辆服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他车辆/a.运费,2) else 0 end) as 付外局车辆服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付到达服务费/a.运费,2) else 0 end) as 运费收入到达服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他到达/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round(a.付到达服务费/a.运费,2) else 0 end) as 付本局到达服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他到达/a.运费,2) else 0 end) as 付外局到达服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.综合服务费/a.运费,2) else 0 end) as 运费收入综合服务费b\r\n" +
                "from hdy_pl b\r\n" +
                "left join\r\n" +
                "temp_承运台账 a  \r\n" +
                "on b.pmdm = a.类型\r\n" +
                "order by hh";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getFb7_dj(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String tj = "";//添加所有条件
        String dw = searchInfo.getDw();//单位（企业）
        String cz = searchInfo.getCz();//车站
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            tj += " and 企业代码 ='" + dw + "'";
        }
        if (cz != null && !cz.equals("") && !cz.equals("0")) {
            tj += " and 电报码 ='" + cz + "'";
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String sql = "delete from temp_承运台账";
        jdbcTemplate.update(sql);
        sql = "insert into temp_承运台账\r\n" +
                "--create table temp_承运台账 as\r\n" +
                "select 到局码  类型,\r\n" +
                "sum(计费重量) 计费重量,sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数 ,sum(基准+高出归己+高出付出) 运费,sum(基准) 基准,sum(高出归己) 高出归己,sum(高出付出) 高出付出,\r\n" +
                "sum(付国铁高出) 付国铁高出,sum(付乌准高出) 付乌准高出,sum(付奎北高出) 付奎北高出,sum(付库俄高出) 付库俄高出,sum(付哈罗高出) 付哈罗高出,sum(付外局高出) 付外局高出,\r\n" +
                "sum(计费吨公里) 计费吨公里,sum(国铁吨公里) 国铁吨公里,sum(乌准吨公里) 乌准吨公里,sum(奎北吨公里) 奎北吨公里,sum(库俄吨公里) 库俄吨公里,sum(哈罗吨公里) 哈罗吨公里,\r\n" +
                "sum(计费吨公里-国铁吨公里-乌准吨公里-奎北吨公里-库俄吨公里-哈罗吨公里) 经外局吨公里,\r\n" +
                "sum(付牵引费_内+付牵引费_电+付线路费+付车辆费+付到达服务费+综合服务费) 承运付费,\r\n" +
                "sum(付牵引费_内) 付牵引费_内,sum(付牵引费_电) 付牵引费_电,sum(付线路费) 付线路费,sum(付车辆费) 付车辆费,sum(付到达服务费) 付到达服务费,sum(综合服务费) 综合服务费,\r\n" +
                "sum(付国铁牵引电) 付国铁牵引电,sum(付国铁牵引内) 付国铁牵引内,sum(付国铁线路) 付国铁线路,sum(付国铁车辆) 付国铁车辆,sum(付国铁到达) 付国铁到达,\r\n" +
                "sum(付乌准牵引电) 付乌准牵引电,sum(付乌准牵引内) 付乌准牵引内,sum(付乌准线路) 付乌准线路,sum(付乌准车辆) 付乌准车辆,sum(付乌准到达) 付乌准到达,\r\n" +
                "sum(付奎北牵引电) 付奎北牵引电,sum(付奎北牵引内) 付奎北牵引内,sum(付奎北线路) 付奎北线路,sum(付奎北车辆) 付奎北车辆,sum(付奎北到达) 付奎北到达,\r\n" +
                "sum(付库俄牵引电) 付库俄牵引电,sum(付库俄牵引内) 付库俄牵引内,sum(付库俄线路) 付库俄线路,sum(付库俄车辆) 付库俄车辆,sum(付库俄到达) 付库俄到达,\r\n" +
                "sum(付哈罗牵引电) 付哈罗牵引电,sum(付哈罗牵引内) 付哈罗牵引内,sum(付哈罗线路) 付哈罗线路,sum(付哈罗车辆) 付哈罗车辆,sum(付哈罗到达) 付哈罗到达,\r\n" +
                "sum(付其他牵引电) 付其他牵引电,sum(付其他牵引内) 付其他牵引内,sum(付其他线路) 付其他线路,sum(付其他车辆) 付其他车辆,sum(付其他到达) 付其他到达\r\n" +
                "from huizong_承运清算台账\r\n" +
                " where 日期 >= " + ksrq + " and 日期 <=" + jsrq + tj +
                " group by 到局码\r\n" +
                "union all\r\n" +
                "select 19 类型,\r\n" +
                "sum(计费重量) 计费重量,sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数 ,sum(基准+高出归己+高出付出) 运费,sum(基准) 基准,sum(高出归己) 高出归己,sum(高出付出) 高出付出,\r\n" +
                "sum(付国铁高出) 付国铁高出,sum(付乌准高出) 付乌准高出,sum(付奎北高出) 付奎北高出,sum(付库俄高出) 付库俄高出,sum(付哈罗高出) 付哈罗高出,sum(付外局高出) 付外局高出,\r\n" +
                "sum(计费吨公里) 计费吨公里,sum(国铁吨公里) 国铁吨公里,sum(乌准吨公里) 乌准吨公里,sum(奎北吨公里) 奎北吨公里,sum(库俄吨公里) 库俄吨公里,sum(哈罗吨公里) 哈罗吨公里,\r\n" +
                "sum(计费吨公里-国铁吨公里-乌准吨公里-奎北吨公里-库俄吨公里-哈罗吨公里) 经外局吨公里,\r\n" +
                "sum(付牵引费_内+付牵引费_电+付线路费+付车辆费+付到达服务费+综合服务费) 承运付费,\r\n" +
                "sum(付牵引费_内) 付牵引费_内,sum(付牵引费_电) 付牵引费_电,sum(付线路费) 付线路费,sum(付车辆费) 付车辆费,sum(付到达服务费) 付到达服务费,sum(综合服务费) 综合服务费,\r\n" +
                "sum(付国铁牵引电) 付国铁牵引电,sum(付国铁牵引内) 付国铁牵引内,sum(付国铁线路) 付国铁线路,sum(付国铁车辆) 付国铁车辆,sum(付国铁到达) 付国铁到达,\r\n" +
                "sum(付乌准牵引电) 付乌准牵引电,sum(付乌准牵引内) 付乌准牵引内,sum(付乌准线路) 付乌准线路,sum(付乌准车辆) 付乌准车辆,sum(付乌准到达) 付乌准到达,\r\n" +
                "sum(付奎北牵引电) 付奎北牵引电,sum(付奎北牵引内) 付奎北牵引内,sum(付奎北线路) 付奎北线路,sum(付奎北车辆) 付奎北车辆,sum(付奎北到达) 付奎北到达,\r\n" +
                "sum(付库俄牵引电) 付库俄牵引电,sum(付库俄牵引内) 付库俄牵引内,sum(付库俄线路) 付库俄线路,sum(付库俄车辆) 付库俄车辆,sum(付库俄到达) 付库俄到达,\r\n" +
                "sum(付哈罗牵引电) 付哈罗牵引电,sum(付哈罗牵引内) 付哈罗牵引内,sum(付哈罗线路) 付哈罗线路,sum(付哈罗车辆) 付哈罗车辆,sum(付哈罗到达) 付哈罗到达,\r\n" +
                "sum(付其他牵引电) 付其他牵引电,sum(付其他牵引内) 付其他牵引内,sum(付其他线路) 付其他线路,sum(付其他车辆) 付其他车辆,sum(付其他到达) 付其他到达\r\n" +
                "from huizong_承运清算台账\r\n" +
                " where 日期 >= " + ksrq + " and 日期 <=" + jsrq + tj;
        jdbcTemplate.update(sql);
        sql = "select b.序号,b.名称,round(a.计费重量/" + jldw + ",2) 计费重量,a.车数,round(a.运费/" + jldw + ",2) 运费,round(a.基准/" + jldw + ",2) 基准,round(a.高出归己/" + jldw + ",2) 高出归己,\r\n" +
                "round(a.高出付出/" + jldw + ",2) 高出付出,round(a.付国铁高出/" + jldw + ",2) 付国铁高出,round(a.付乌准高出/" + jldw + ",2) 付乌准高出,round(a.付奎北高出/" + jldw + ",2) 付奎北高出,\r\n" +
                "round(a.付库俄高出/" + jldw + ",2) 付库俄高出,round(a.付哈罗高出/" + jldw + ",2) 付哈罗高出,round(a.付外局高出/" + jldw + ",2) 付外局高出,round(a.计费吨公里/" + jldw + ",2) 计费吨公里,\r\n" +
                "round(a.国铁吨公里/" + jldw + ",2) 国铁吨公里,round(a.乌准吨公里/" + jldw + ",2) 乌准吨公里,round(a.奎北吨公里/" + jldw + ",2) 奎北吨公里s,round(a.库俄吨公里/" + jldw + ",2) 库俄吨公里,\r\n" +
                "round(a.哈罗吨公里/" + jldw + ",2) 哈罗吨公里,round(a.经外局吨公里/" + jldw + ",2) 经外局吨公里,round(a.承运付费/" + jldw + ",2) 承运付费,round(a.付牵引费_内/" + jldw + ",2) 奎北吨公里,\r\n" +
                "round(a.付牵引费_电/" + jldw + ",2) 付牵引费_电,round(a.付线路费/" + jldw + ",2) 付线路费,round(a.付车辆费/" + jldw + ",2) 付车辆费,round(a.付到达服务费/" + jldw + ",2) 付到达服务费,\r\n" +
                "round(a.综合服务费/" + jldw + ",2) 综合服务费,round(a.付国铁牵引电/" + jldw + ",2) 付国铁牵引电,round(a.付国铁牵引内/" + jldw + ",2) 付国铁牵引内,round(a.付国铁线路/" + jldw + ",2) 付国铁线路,\r\n" +
                "round(a.付国铁车辆/" + jldw + ",2) 付国铁车辆,round(a.付国铁到达/" + jldw + ",2) 付国铁到达,round(a.付乌准牵引电/" + jldw + ",2) 付乌准牵引电,round(a.付乌准牵引内/" + jldw + ",2) 付乌准牵引内,\r\n" +
                "round(a.付乌准线路/" + jldw + ",2) 付乌准线路,round(a.付乌准车辆/" + jldw + ",2) 付乌准车辆,round(a.付乌准到达/" + jldw + ",2) 付乌准到达,round(a.付奎北牵引内/" + jldw + ",2) 付奎北牵引内,\r\n" +
                "round(a.付奎北牵引电/" + jldw + ",2) 付奎北牵引电,round(a.付奎北线路/" + jldw + ",2) 付奎北线路,round(a.付奎北车辆/" + jldw + ",2) 付奎北车辆,round(a.付奎北到达/" + jldw + ",2) 付奎北到达,\r\n" +
                "round(a.付库俄牵引内/" + jldw + ",2) 付库俄牵引内,round(a.付库俄牵引电/" + jldw + ",2) 付库俄牵引电,round(a.付库俄线路/" + jldw + ",2) 付库俄线路,round(a.付库俄车辆/" + jldw + ",2) 付库俄车辆,\r\n" +
                "round(a.付库俄到达/" + jldw + ",2) 付库俄到达,round(a.付哈罗牵引内/" + jldw + ",2) 付哈罗牵引内,round(a.付哈罗牵引电/" + jldw + ",2) 付哈罗牵引电,round(a.付哈罗线路/" + jldw + ",2) 付哈罗线路,\r\n" +
                "round(a.付哈罗车辆/" + jldw + ",2) 付哈罗车辆,round(a.付哈罗到达/" + jldw + ",2) 付哈罗到达,round(a.付其他牵引内/" + jldw + ",2) 付其他牵引内,round(a.付其他牵引电/" + jldw + ",2) 付其他牵引电,\r\n" +
                "round(a.付其他线路/" + jldw + ",2) 付其他线路,round(a.付其他车辆/" + jldw + ",2) 付其他车辆,round(a.付其他到达/" + jldw + ",2) 付其他到达,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round((a.付牵引费_内 + a.付牵引费_电)/a.计费吨公里,2) else 0 end) as 计费机车牵引费,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付牵引费_内 + a.付牵引费_电 - a.付其他牵引电 - a.付其他牵引内)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局机车牵引费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round((a.付其他牵引电 + a.付其他牵引内)/a.经外局吨公里,2) else 0 end) as 付外局机车牵引费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付线路费/a.计费吨公里,2) else 0 end) as 计费线路使用费a,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付线路费 - 付其他线路)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局线路使用费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round(a.付其他线路/a.经外局吨公里,2) else 0 end) as 付外局线路使用费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付车辆费/a.计费吨公里,2) else 0 end) as 计费车辆服务费a,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付车辆费 - a.付其他车辆)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局车辆服务费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round((a.付车辆费)/(a.经外局吨公里),2) else 0 end) as 付外局车辆服务费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付到达服务费/a.计费吨公里,2) else 0 end) as 计费到达服务费a,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付到达服务费 - a.付其他到达)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局到达服务费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round(a.付其他到达/(a.经外局吨公里),2) else 0 end) as 付外局到达服务费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.综合服务费/a.计费吨公里,2) else 0 end) as 计费综合服务费a,\r\n" +
                "(case when abs(a.运费) > 0 then round((a.付牵引费_内 + a.付牵引费_电)/a.运费,2) else 0 end) as 运费收入机车牵引费b,\r\n" +
                "(case when abs(a.运费) > 0 then round((a.付其他牵引电 + a.付其他牵引内)/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round((a.付牵引费_内 + a.付牵引费_电)/a.运费,2) else 0 end) as 付本局机车牵引费b,\r\n" +
                "(case when abs(a.运费) > 0 then round((a.付其他牵引电 + a.付其他牵引内)/a.运费,2) else 0 end) as 付外局机车牵引费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付线路费/a.运费,2) else 0 end) as 运费收入线路使用费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他线路/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round(a.付线路费/a.运费,2) else 0 end) as 付本局线路使用费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他线路/a.运费,2) else 0 end) as 付外局线路使用费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付车辆费/a.运费,2) else 0 end) as 运费收入车辆服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他车辆/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round(a.付车辆费/a.运费,2) else 0 end) as 付本局车辆服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他车辆/a.运费,2) else 0 end) as 付外局车辆服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付到达服务费/a.运费,2) else 0 end) as 运费收入到达服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他到达/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round(a.付到达服务费/a.运费,2) else 0 end) as 付本局到达服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他到达/a.运费,2) else 0 end) as 付外局到达服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.综合服务费/a.运费,2) else 0 end) as 运费收入综合服务费b\r\n" +
                "from srfx_行定义_路局 b\r\n" +
                "left join\r\n" +
                "temp_承运台账 a  \r\n" +
                "on b.序号 = a.类型\r\n" +
                "order by 序号";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getFb7_fz(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String tj = "";//添加所有条件
        String dw = searchInfo.getDw();//单位（企业）
        String cz = searchInfo.getCz();//车站
        String jm = searchInfo.getJm();//局码
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            tj += " and 企业代码 ='" + dw + "'";
        }
        if (cz != null && !cz.equals("") && !cz.equals("0")) {
            tj += " and 电报码 ='" + cz + "'";
        }
        if (jm != null && !jm.equals("") && !jm.equals("0")) {
            tj += " and 到局码 ='" + jm + "'";
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String sql = "delete from temp_承运台账";
        jdbcTemplate.update(sql);
        sql = "insert into temp_承运台账\r\n" +
                "--create table temp_承运台账 as\r\n" +
                "select 电报码  类型,\r\n" +
                "sum(计费重量) 计费重量,sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数 ,sum(基准+高出归己+高出付出) 运费,sum(基准) 基准,sum(高出归己) 高出归己,sum(高出付出) 高出付出,\r\n" +
                "sum(付国铁高出) 付国铁高出,sum(付乌准高出) 付乌准高出,sum(付奎北高出) 付奎北高出,sum(付库俄高出) 付库俄高出,sum(付哈罗高出) 付哈罗高出,sum(付外局高出) 付外局高出,\r\n" +
                "sum(计费吨公里) 计费吨公里,sum(国铁吨公里) 国铁吨公里,sum(乌准吨公里) 乌准吨公里,sum(奎北吨公里) 奎北吨公里,sum(库俄吨公里) 库俄吨公里,sum(哈罗吨公里) 哈罗吨公里,\r\n" +
                "sum(计费吨公里-国铁吨公里-乌准吨公里-奎北吨公里-库俄吨公里-哈罗吨公里) 经外局吨公里,\r\n" +
                "sum(付牵引费_内+付牵引费_电+付线路费+付车辆费+付到达服务费+综合服务费) 承运付费,\r\n" +
                "sum(付牵引费_内) 付牵引费_内,sum(付牵引费_电) 付牵引费_电,sum(付线路费) 付线路费,sum(付车辆费) 付车辆费,sum(付到达服务费) 付到达服务费,sum(综合服务费) 综合服务费,\r\n" +
                "sum(付国铁牵引电) 付国铁牵引电,sum(付国铁牵引内) 付国铁牵引内,sum(付国铁线路) 付国铁线路,sum(付国铁车辆) 付国铁车辆,sum(付国铁到达) 付国铁到达,\r\n" +
                "sum(付乌准牵引电) 付乌准牵引电,sum(付乌准牵引内) 付乌准牵引内,sum(付乌准线路) 付乌准线路,sum(付乌准车辆) 付乌准车辆,sum(付乌准到达) 付乌准到达,\r\n" +
                "sum(付奎北牵引电) 付奎北牵引电,sum(付奎北牵引内) 付奎北牵引内,sum(付奎北线路) 付奎北线路,sum(付奎北车辆) 付奎北车辆,sum(付奎北到达) 付奎北到达,\r\n" +
                "sum(付库俄牵引电) 付库俄牵引电,sum(付库俄牵引内) 付库俄牵引内,sum(付库俄线路) 付库俄线路,sum(付库俄车辆) 付库俄车辆,sum(付库俄到达) 付库俄到达,\r\n" +
                "sum(付哈罗牵引电) 付哈罗牵引电,sum(付哈罗牵引内) 付哈罗牵引内,sum(付哈罗线路) 付哈罗线路,sum(付哈罗车辆) 付哈罗车辆,sum(付哈罗到达) 付哈罗到达,\r\n" +
                "sum(付其他牵引电) 付其他牵引电,sum(付其他牵引内) 付其他牵引内,sum(付其他线路) 付其他线路,sum(付其他车辆) 付其他车辆,sum(付其他到达) 付其他到达\r\n" +
                "from huizong_承运清算台账\r\n" +
                " where 日期 >= " + ksrq + " and 日期 <=" + jsrq + tj + "\r\n" +
                "group by 电报码";
        jdbcTemplate.update(sql);
        sql = "select b.id,b.mc,round(a.计费重量/" + jldw + ",2) 计费重量,a.车数,round(a.运费/" + jldw + ",2) 运费,round(a.基准/" + jldw + ",2) 基准,round(a.高出归己/" + jldw + ",2) 高出归己,\r\n" +
                "round(a.高出付出/" + jldw + ",2) 高出付出,round(a.付国铁高出/" + jldw + ",2) 付国铁高出,round(a.付乌准高出/" + jldw + ",2) 付乌准高出,round(a.付奎北高出/" + jldw + ",2) 付奎北高出,\r\n" +
                "round(a.付库俄高出/" + jldw + ",2) 付库俄高出,round(a.付哈罗高出/" + jldw + ",2) 付哈罗高出,round(a.付外局高出/" + jldw + ",2) 付外局高出,round(a.计费吨公里/" + jldw + ",2) 计费吨公里,\r\n" +
                "round(a.国铁吨公里/" + jldw + ",2) 国铁吨公里,round(a.乌准吨公里/" + jldw + ",2) 乌准吨公里,round(a.奎北吨公里/" + jldw + ",2) 奎北吨公里s,round(a.库俄吨公里/" + jldw + ",2) 库俄吨公里,\r\n" +
                "round(a.哈罗吨公里/" + jldw + ",2) 哈罗吨公里,round(a.经外局吨公里/" + jldw + ",2) 经外局吨公里,round(a.承运付费/" + jldw + ",2) 承运付费,round(a.付牵引费_内/" + jldw + ",2) 奎北吨公里,\r\n" +
                "round(a.付牵引费_电/" + jldw + ",2) 付牵引费_电,round(a.付线路费/" + jldw + ",2) 付线路费,round(a.付车辆费/" + jldw + ",2) 付车辆费,round(a.付到达服务费/" + jldw + ",2) 付到达服务费,\r\n" +
                "round(a.综合服务费/" + jldw + ",2) 综合服务费,round(a.付国铁牵引电/" + jldw + ",2) 付国铁牵引电,round(a.付国铁牵引内/" + jldw + ",2) 付国铁牵引内,round(a.付国铁线路/" + jldw + ",2) 付国铁线路,\r\n" +
                "round(a.付国铁车辆/" + jldw + ",2) 付国铁车辆,round(a.付国铁到达/" + jldw + ",2) 付国铁到达,round(a.付乌准牵引电/" + jldw + ",2) 付乌准牵引电,round(a.付乌准牵引内/" + jldw + ",2) 付乌准牵引内,\r\n" +
                "round(a.付乌准线路/" + jldw + ",2) 付乌准线路,round(a.付乌准车辆/" + jldw + ",2) 付乌准车辆,round(a.付乌准到达/" + jldw + ",2) 付乌准到达,round(a.付奎北牵引内/" + jldw + ",2) 付奎北牵引内,\r\n" +
                "round(a.付奎北牵引电/" + jldw + ",2) 付奎北牵引电,round(a.付奎北线路/" + jldw + ",2) 付奎北线路,round(a.付奎北车辆/" + jldw + ",2) 付奎北车辆,round(a.付奎北到达/" + jldw + ",2) 付奎北到达,\r\n" +
                "round(a.付库俄牵引内/" + jldw + ",2) 付库俄牵引内,round(a.付库俄牵引电/" + jldw + ",2) 付库俄牵引电,round(a.付库俄线路/" + jldw + ",2) 付库俄线路,round(a.付库俄车辆/" + jldw + ",2) 付库俄车辆,\r\n" +
                "round(a.付库俄到达/" + jldw + ",2) 付库俄到达,round(a.付哈罗牵引内/" + jldw + ",2) 付哈罗牵引内,round(a.付哈罗牵引电/" + jldw + ",2) 付哈罗牵引电,round(a.付哈罗线路/" + jldw + ",2) 付哈罗线路,\r\n" +
                "round(a.付哈罗车辆/" + jldw + ",2) 付哈罗车辆,round(a.付哈罗到达/" + jldw + ",2) 付哈罗到达,round(a.付其他牵引内/" + jldw + ",2) 付其他牵引内,round(a.付其他牵引电/" + jldw + ",2) 付其他牵引电,\r\n" +
                "round(a.付其他线路/" + jldw + ",2) 付其他线路,round(a.付其他车辆/" + jldw + ",2) 付其他车辆,round(a.付其他到达/" + jldw + ",2) 付其他到达,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round((a.付牵引费_内 + a.付牵引费_电)/a.计费吨公里,2) else 0 end) as 计费机车牵引费,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付牵引费_内 + a.付牵引费_电 - a.付其他牵引电 - a.付其他牵引内)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局机车牵引费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round((a.付其他牵引电 + a.付其他牵引内)/a.经外局吨公里,2) else 0 end) as 付外局机车牵引费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付线路费/a.计费吨公里,2) else 0 end) as 计费线路使用费a,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付线路费 - 付其他线路)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局线路使用费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round(a.付其他线路/a.经外局吨公里,2) else 0 end) as 付外局线路使用费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付车辆费/a.计费吨公里,2) else 0 end) as 计费车辆服务费a,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付车辆费 - a.付其他车辆)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局车辆服务费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round((a.付车辆费)/(a.经外局吨公里),2) else 0 end) as 付外局车辆服务费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付到达服务费/a.计费吨公里,2) else 0 end) as 计费到达服务费a,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付到达服务费 - a.付其他到达)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局到达服务费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round(a.付其他到达/(a.经外局吨公里),2) else 0 end) as 付外局到达服务费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.综合服务费/a.计费吨公里,2) else 0 end) as 计费综合服务费a,\r\n" +
                "(case when abs(a.运费) > 0 then round((a.付牵引费_内 + a.付牵引费_电)/a.运费,2) else 0 end) as 运费收入机车牵引费b,\r\n" +
                "(case when abs(a.运费) > 0 then round((a.付其他牵引电 + a.付其他牵引内)/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round((a.付牵引费_内 + a.付牵引费_电)/a.运费,2) else 0 end) as 付本局机车牵引费b,\r\n" +
                "(case when abs(a.运费) > 0 then round((a.付其他牵引电 + a.付其他牵引内)/a.运费,2) else 0 end) as 付外局机车牵引费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付线路费/a.运费,2) else 0 end) as 运费收入线路使用费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他线路/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round(a.付线路费/a.运费,2) else 0 end) as 付本局线路使用费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他线路/a.运费,2) else 0 end) as 付外局线路使用费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付车辆费/a.运费,2) else 0 end) as 运费收入车辆服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他车辆/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round(a.付车辆费/a.运费,2) else 0 end) as 付本局车辆服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他车辆/a.运费,2) else 0 end) as 付外局车辆服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付到达服务费/a.运费,2) else 0 end) as 运费收入到达服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他到达/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round(a.付到达服务费/a.运费,2) else 0 end) as 付本局到达服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他到达/a.运费,2) else 0 end) as 付外局到达服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.综合服务费/a.运费,2) else 0 end) as 运费收入综合服务费b\r\n" +
                "from temp_承运台账 a\r\n" +
                "left join\r\n" +
                "srfx_zmk b \r\n" +
                "on b.cs4dbm = a.类型 and b.cztype = 1 \r\n" +
                " order by id";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getFb7_zz(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String tj = "";//添加所有条件
        String dw = searchInfo.getDw();//单位（企业）
        String cz = searchInfo.getCz();//车站
        String jm = searchInfo.getJm();//局码
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            tj += " and 企业代码 ='" + dw + "'";
        }
        if (cz != null && !cz.equals("") && !cz.equals("0")) {
            tj += " and 电报码 ='" + cz + "'";
        }
        if (jm != null && !jm.equals("") && !jm.equals("0")) {
            tj += " and 到局码 ='" + jm + "'";
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String sql = "delete from temp_承运台账";
        jdbcTemplate.update(sql);
        sql = "insert into temp_承运台账\r\n" +
                "--create table temp_承运台账 as\r\n" +
                "select 静载重  类型,\r\n" +
                "sum(计费重量) 计费重量,sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数 ,sum(基准+高出归己+高出付出) 运费,sum(基准) 基准,sum(高出归己) 高出归己,sum(高出付出) 高出付出,\r\n" +
                "sum(付国铁高出) 付国铁高出,sum(付乌准高出) 付乌准高出,sum(付奎北高出) 付奎北高出,sum(付库俄高出) 付库俄高出,sum(付哈罗高出) 付哈罗高出,sum(付外局高出) 付外局高出,\r\n" +
                "sum(计费吨公里) 计费吨公里,sum(国铁吨公里) 国铁吨公里,sum(乌准吨公里) 乌准吨公里,sum(奎北吨公里) 奎北吨公里,sum(库俄吨公里) 库俄吨公里,sum(哈罗吨公里) 哈罗吨公里,\r\n" +
                "sum(计费吨公里-国铁吨公里-乌准吨公里-奎北吨公里-库俄吨公里-哈罗吨公里) 经外局吨公里,\r\n" +
                "sum(付牵引费_内+付牵引费_电+付线路费+付车辆费+付到达服务费+综合服务费) 承运付费,\r\n" +
                "sum(付牵引费_内) 付牵引费_内,sum(付牵引费_电) 付牵引费_电,sum(付线路费) 付线路费,sum(付车辆费) 付车辆费,sum(付到达服务费) 付到达服务费,sum(综合服务费) 综合服务费,\r\n" +
                "sum(付国铁牵引电) 付国铁牵引电,sum(付国铁牵引内) 付国铁牵引内,sum(付国铁线路) 付国铁线路,sum(付国铁车辆) 付国铁车辆,sum(付国铁到达) 付国铁到达,\r\n" +
                "sum(付乌准牵引电) 付乌准牵引电,sum(付乌准牵引内) 付乌准牵引内,sum(付乌准线路) 付乌准线路,sum(付乌准车辆) 付乌准车辆,sum(付乌准到达) 付乌准到达,\r\n" +
                "sum(付奎北牵引电) 付奎北牵引电,sum(付奎北牵引内) 付奎北牵引内,sum(付奎北线路) 付奎北线路,sum(付奎北车辆) 付奎北车辆,sum(付奎北到达) 付奎北到达,\r\n" +
                "sum(付库俄牵引电) 付库俄牵引电,sum(付库俄牵引内) 付库俄牵引内,sum(付库俄线路) 付库俄线路,sum(付库俄车辆) 付库俄车辆,sum(付库俄到达) 付库俄到达,\r\n" +
                "sum(付哈罗牵引电) 付哈罗牵引电,sum(付哈罗牵引内) 付哈罗牵引内,sum(付哈罗线路) 付哈罗线路,sum(付哈罗车辆) 付哈罗车辆,sum(付哈罗到达) 付哈罗到达,\r\n" +
                "sum(付其他牵引电) 付其他牵引电,sum(付其他牵引内) 付其他牵引内,sum(付其他线路) 付其他线路,sum(付其他车辆) 付其他车辆,sum(付其他到达) 付其他到达\r\n" +
                "from huizong_承运清算台账\r\n" +
                " where 日期 >= " + ksrq + " and 日期 <=" + jsrq + tj +
                "  group by 静载重\r\n" +
                "union all\r\n" +
                "select 1 类型,\r\n" +
                "sum(计费重量) 计费重量,sum((case when 箱数>0 then 箱数 else 车数 end)) as 车数 ,sum(基准+高出归己+高出付出) 运费,sum(基准) 基准,sum(高出归己) 高出归己,sum(高出付出) 高出付出,\r\n" +
                "sum(付国铁高出) 付国铁高出,sum(付乌准高出) 付乌准高出,sum(付奎北高出) 付奎北高出,sum(付库俄高出) 付库俄高出,sum(付哈罗高出) 付哈罗高出,sum(付外局高出) 付外局高出,\r\n" +
                "sum(计费吨公里) 计费吨公里,sum(国铁吨公里) 国铁吨公里,sum(乌准吨公里) 乌准吨公里,sum(奎北吨公里) 奎北吨公里,sum(库俄吨公里) 库俄吨公里,sum(哈罗吨公里) 哈罗吨公里,\r\n" +
                "sum(计费吨公里-国铁吨公里-乌准吨公里-奎北吨公里-库俄吨公里-哈罗吨公里) 经外局吨公里,\r\n" +
                "sum(付牵引费_内+付牵引费_电+付线路费+付车辆费+付到达服务费+综合服务费) 承运付费,\r\n" +
                "sum(付牵引费_内) 付牵引费_内,sum(付牵引费_电) 付牵引费_电,sum(付线路费) 付线路费,sum(付车辆费) 付车辆费,sum(付到达服务费) 付到达服务费,sum(综合服务费) 综合服务费,\r\n" +
                "sum(付国铁牵引电) 付国铁牵引电,sum(付国铁牵引内) 付国铁牵引内,sum(付国铁线路) 付国铁线路,sum(付国铁车辆) 付国铁车辆,sum(付国铁到达) 付国铁到达,\r\n" +
                "sum(付乌准牵引电) 付乌准牵引电,sum(付乌准牵引内) 付乌准牵引内,sum(付乌准线路) 付乌准线路,sum(付乌准车辆) 付乌准车辆,sum(付乌准到达) 付乌准到达,\r\n" +
                "sum(付奎北牵引电) 付奎北牵引电,sum(付奎北牵引内) 付奎北牵引内,sum(付奎北线路) 付奎北线路,sum(付奎北车辆) 付奎北车辆,sum(付奎北到达) 付奎北到达,\r\n" +
                "sum(付库俄牵引电) 付库俄牵引电,sum(付库俄牵引内) 付库俄牵引内,sum(付库俄线路) 付库俄线路,sum(付库俄车辆) 付库俄车辆,sum(付库俄到达) 付库俄到达,\r\n" +
                "sum(付哈罗牵引电) 付哈罗牵引电,sum(付哈罗牵引内) 付哈罗牵引内,sum(付哈罗线路) 付哈罗线路,sum(付哈罗车辆) 付哈罗车辆,sum(付哈罗到达) 付哈罗到达,\r\n" +
                "sum(付其他牵引电) 付其他牵引电,sum(付其他牵引内) 付其他牵引内,sum(付其他线路) 付其他线路,sum(付其他车辆) 付其他车辆,sum(付其他到达) 付其他到达\r\n" +
                "from huizong_承运清算台账\r\n" +
                " where 日期 >= " + ksrq + " and 日期 <=" + jsrq + tj;
        jdbcTemplate.update(sql);
        sql = "select b.hh,b.mc,round(a.计费重量/" + jldw + ",2) 计费重量,a.车数,round(a.运费/" + jldw + ",2) 运费,round(a.基准/" + jldw + ",2) 基准,round(a.高出归己/" + jldw + ",2) 高出归己,\r\n" +
                "round(a.高出付出/" + jldw + ",2) 高出付出,round(a.付国铁高出/" + jldw + ",2) 付国铁高出,round(a.付乌准高出/" + jldw + ",2) 付乌准高出,round(a.付奎北高出/" + jldw + ",2) 付奎北高出,\r\n" +
                "round(a.付库俄高出/" + jldw + ",2) 付库俄高出,round(a.付哈罗高出/" + jldw + ",2) 付哈罗高出,round(a.付外局高出/" + jldw + ",2) 付外局高出,round(a.计费吨公里/" + jldw + ",2) 计费吨公里,\r\n" +
                "round(a.国铁吨公里/" + jldw + ",2) 国铁吨公里,round(a.乌准吨公里/" + jldw + ",2) 乌准吨公里,round(a.奎北吨公里/" + jldw + ",2) 奎北吨公里s,round(a.库俄吨公里/" + jldw + ",2) 库俄吨公里,\r\n" +
                "round(a.哈罗吨公里/" + jldw + ",2) 哈罗吨公里,round(a.经外局吨公里/" + jldw + ",2) 经外局吨公里,round(a.承运付费/" + jldw + ",2) 承运付费,round(a.付牵引费_内/" + jldw + ",2) 奎北吨公里,\r\n" +
                "round(a.付牵引费_电/" + jldw + ",2) 付牵引费_电,round(a.付线路费/" + jldw + ",2) 付线路费,round(a.付车辆费/" + jldw + ",2) 付车辆费,round(a.付到达服务费/" + jldw + ",2) 付到达服务费,\r\n" +
                "round(a.综合服务费/" + jldw + ",2) 综合服务费,round(a.付国铁牵引电/" + jldw + ",2) 付国铁牵引电,round(a.付国铁牵引内/" + jldw + ",2) 付国铁牵引内,round(a.付国铁线路/" + jldw + ",2) 付国铁线路,\r\n" +
                "round(a.付国铁车辆/" + jldw + ",2) 付国铁车辆,round(a.付国铁到达/" + jldw + ",2) 付国铁到达,round(a.付乌准牵引电/" + jldw + ",2) 付乌准牵引电,round(a.付乌准牵引内/" + jldw + ",2) 付乌准牵引内,\r\n" +
                "round(a.付乌准线路/" + jldw + ",2) 付乌准线路,round(a.付乌准车辆/" + jldw + ",2) 付乌准车辆,round(a.付乌准到达/" + jldw + ",2) 付乌准到达,round(a.付奎北牵引内/" + jldw + ",2) 付奎北牵引内,\r\n" +
                "round(a.付奎北牵引电/" + jldw + ",2) 付奎北牵引电,round(a.付奎北线路/" + jldw + ",2) 付奎北线路,round(a.付奎北车辆/" + jldw + ",2) 付奎北车辆,round(a.付奎北到达/" + jldw + ",2) 付奎北到达,\r\n" +
                "round(a.付库俄牵引内/" + jldw + ",2) 付库俄牵引内,round(a.付库俄牵引电/" + jldw + ",2) 付库俄牵引电,round(a.付库俄线路/" + jldw + ",2) 付库俄线路,round(a.付库俄车辆/" + jldw + ",2) 付库俄车辆,\r\n" +
                "round(a.付库俄到达/" + jldw + ",2) 付库俄到达,round(a.付哈罗牵引内/" + jldw + ",2) 付哈罗牵引内,round(a.付哈罗牵引电/" + jldw + ",2) 付哈罗牵引电,round(a.付哈罗线路/" + jldw + ",2) 付哈罗线路,\r\n" +
                "round(a.付哈罗车辆/" + jldw + ",2) 付哈罗车辆,round(a.付哈罗到达/" + jldw + ",2) 付哈罗到达,round(a.付其他牵引内/" + jldw + ",2) 付其他牵引内,round(a.付其他牵引电/" + jldw + ",2) 付其他牵引电,\r\n" +
                "round(a.付其他线路/" + jldw + ",2) 付其他线路,round(a.付其他车辆/" + jldw + ",2) 付其他车辆,round(a.付其他到达/" + jldw + ",2) 付其他到达,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round((a.付牵引费_内 + a.付牵引费_电)/a.计费吨公里,2) else 0 end) as 计费机车牵引费,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付牵引费_内 + a.付牵引费_电 - a.付其他牵引电 - a.付其他牵引内)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局机车牵引费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round((a.付其他牵引电 + a.付其他牵引内)/a.经外局吨公里,2) else 0 end) as 付外局机车牵引费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付线路费/a.计费吨公里,2) else 0 end) as 计费线路使用费a,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付线路费 - 付其他线路)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局线路使用费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round(a.付其他线路/a.经外局吨公里,2) else 0 end) as 付外局线路使用费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付车辆费/a.计费吨公里,2) else 0 end) as 计费车辆服务费a,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付车辆费 - a.付其他车辆)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局车辆服务费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round((a.付车辆费)/(a.经外局吨公里),2) else 0 end) as 付外局车辆服务费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.付到达服务费/a.计费吨公里,2) else 0 end) as 计费到达服务费a,\r\n" +
                "(case when abs(a.计费吨公里 - a.经外局吨公里) > 0 then round((a.付到达服务费 - a.付其他到达)/(a.计费吨公里 - a.经外局吨公里),2) else 0 end) as 付本局到达服务费a,\r\n" +
                "(case when abs(a.经外局吨公里) > 0 then round(a.付其他到达/(a.经外局吨公里),2) else 0 end) as 付外局到达服务费a,\r\n" +
                "(case when abs(a.计费吨公里) > 0 then round(a.综合服务费/a.计费吨公里,2) else 0 end) as 计费综合服务费a,\r\n" +
                "(case when abs(a.运费) > 0 then round((a.付牵引费_内 + a.付牵引费_电)/a.运费,2) else 0 end) as 运费收入机车牵引费b,\r\n" +
                "(case when abs(a.运费) > 0 then round((a.付其他牵引电 + a.付其他牵引内)/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round((a.付牵引费_内 + a.付牵引费_电)/a.运费,2) else 0 end) as 付本局机车牵引费b,\r\n" +
                "(case when abs(a.运费) > 0 then round((a.付其他牵引电 + a.付其他牵引内)/a.运费,2) else 0 end) as 付外局机车牵引费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付线路费/a.运费,2) else 0 end) as 运费收入线路使用费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他线路/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round(a.付线路费/a.运费,2) else 0 end) as 付本局线路使用费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他线路/a.运费,2) else 0 end) as 付外局线路使用费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付车辆费/a.运费,2) else 0 end) as 运费收入车辆服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他车辆/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round(a.付车辆费/a.运费,2) else 0 end) as 付本局车辆服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他车辆/a.运费,2) else 0 end) as 付外局车辆服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付到达服务费/a.运费,2) else 0 end) as 运费收入到达服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他到达/a.运费,2) else 0 end) - (case when abs(a.运费) > 0 then round(a.付到达服务费/a.运费,2) else 0 end) as 付本局到达服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.付其他到达/a.运费,2) else 0 end) as 付外局到达服务费b,\r\n" +
                "(case when abs(a.运费) > 0 then round(a.综合服务费/a.运费,2) else 0 end) as 运费收入综合服务费b\r\n" +
                "from hdy_cyhz_fb2_jzz b\r\n" +
                "left join\r\n" +
                "temp_承运台账 a  \r\n" +
                "on b.hh = a.类型\r\n" +
                "order by hh";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getFb7_xm(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String sql = "delete from temp_承运清算台账";
        jdbcTemplate.update(sql);
        sql = "insert into temp_承运清算台账\r\n" +
                "select a.hh,a.mc,c.运费,c.基准,c.高出归己,c.付本局其他企业,c.付外局高出,b.收本局其他高出,b.收外局高出,\r\n" +
                "b.收牵引费,b.自收自牵引,b.收本局其他牵引,b.收外局牵引,\r\n" +
                "b.收线路费,b.自收自线路,b.收本局其他线路,b.收外局线路,\r\n" +
                "b.收车辆费,b.自收自车辆,b.收本局其他车辆,b.收外局车辆,\r\n" +
                "b.收到达服务费,b.自收自到达,b.收本局其他到达,b.收外局到达,\r\n" +
                "b.收接触网,b.自收自接触网,b.收本局其他接触网,b.收外局接触网,b.收长交路,\r\n" +
                "c.机车牵引,c.自付自牵引,c.付本局其他牵引,c.付外局牵引,\r\n" +
                "c.付线路费,c.自付自线路,c.付本局其他线路,c.付外局线路,\r\n" +
                "c.付车辆费,c.自付自车辆,c.付本局其他车辆,c.付外局车辆,"
                + " c.付到达费,c.自付自到达,c.付本局其他到达,c.付外局到达,\r\n" +
                "c.付接触网,c.自付自接触网,c.付本局其他企业接触网,c.付外局接触网,\r\n" +
                "c.付长交路\r\n" +
                "from HDY_CYHZ_FB1 a\r\n" +
                "left join\r\n" +
                "(\r\n" +
                "  select 清算企业,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)>0 and 清算企业<>制票企业 then 高出归己 else 0 end) 收本局其他高出,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)<=0 then 高出归己 else 0 end) 收外局高出,\r\n" +
                "  sum(收牵引费) 收牵引费,sum(case when 清算企业=制票企业 then 收牵引费 else 0 end) 自收自牵引,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)>0 and 清算企业<>制票企业 then 收牵引费 else 0 end) 收本局其他牵引,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)<=0 then 收牵引费 else 0 end) 收外局牵引,\r\n" +
                "  sum(收线路费) 收线路费,\r\n" +
                "  sum(case when 清算企业=制票企业 then 收线路费 else 0 end) 自收自线路,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)>0 and 清算企业<>制票企业 then 收线路费 else 0 end) 收本局其他线路,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)<=0 then 收线路费 else 0 end) 收外局线路,\r\n" +
                "  sum(收车辆费) 收车辆费,\r\n" +
                "  sum(case when 清算企业=制票企业 then 收车辆费 else 0 end) 自收自车辆,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)>0 and 清算企业<>制票企业 then 收车辆费 else 0 end) 收本局其他车辆,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)<=0 then 收车辆费 else 0 end) 收外局车辆,\r\n" +
                "  sum(收到达服务费) 收到达服务费,\r\n" +
                "  sum(case when 清算企业=制票企业 then 收到达服务费 else 0 end) 自收自到达,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)>0 and 清算企业<>制票企业 then 收到达服务费 else 0 end) 收本局其他到达,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)<=0 then 收到达服务费 else 0 end) 收外局到达,\r\n" +
                "  sum(收接触网) 收接触网,\r\n" +
                "  sum(case when 清算企业=制票企业 then 收接触网 else 0 end) 自收自接触网,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)>0 and 清算企业<>制票企业 then 收接触网 else 0 end) 收本局其他接触网,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)<=0 then 收接触网 else 0 end) 收外局接触网,\r\n" +
                "  sum(收长交路轮乘费) 收长交路\r\n" +
                "  from QSJG_GT_2018 t\r\n" +
                "  where 清算日期>=" + ksrq + " and 清算日期<=" + jsrq +
                "  group by 清算企业\r\n" +
                "  union all\r\n" +
                "  select '合计'清算企业,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)>0 and 清算企业<>制票企业 then 高出归己 else 0 end) 收本局其他高出,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)<=0 then 高出归己 else 0 end) 收外局高出,\r\n" +
                "  sum(收牵引费) 收牵引费,sum(case when 清算企业=制票企业 then 收牵引费 else 0 end) 自收自牵引,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)>0 and 清算企业<>制票企业 then 收牵引费 else 0 end) 收本局其他牵引,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)<=0 then 收牵引费 else 0 end) 收外局牵引,\r\n" +
                "  sum(收线路费) 收线路费,\r\n" +
                "  sum(case when 清算企业=制票企业 then 收线路费 else 0 end) 自收自线路,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)>0 and 清算企业<>制票企业 then 收线路费 else 0 end) 收本局其他线路,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)<=0 then 收线路费 else 0 end) 收外局线路,\r\n" +
                "  sum(收车辆费) 收车辆费,\r\n" +
                "  sum(case when 清算企业=制票企业 then 收车辆费 else 0 end) 自收自车辆,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)>0 and 清算企业<>制票企业 then 收车辆费 else 0 end) 收本局其他车辆,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)<=0 then 收车辆费 else 0 end) 收外局车辆,\r\n" +
                "  sum(收到达服务费) 收到达服务费,\r\n" +
                "  sum(case when 清算企业=制票企业 then 收到达服务费 else 0 end) 自收自到达,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)>0 and 清算企业<>制票企业 then 收到达服务费 else 0 end) 收本局其他到达,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)<=0 then 收到达服务费 else 0 end) 收外局到达,\r\n" +
                "  sum(收接触网) 收接触网,\r\n" +
                "  sum(case when 清算企业=制票企业 then 收接触网 else 0 end) 自收自接触网,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)>0 and 清算企业<>制票企业 then 收接触网 else 0 end) 收本局其他接触网,\r\n" +
                "  sum(case when instr('乌鲁木齐局,乌准公司,奎北公司,库俄公司,哈罗公司',制票企业)<=0 then 收接触网 else 0 end) 收外局接触网,\r\n" +
                "  sum(收长交路轮乘费) 收长交路\r\n" +
                "  from QSJG_GT_2018 t\r\n" +
                "  where 清算日期>=" + ksrq + " and 清算日期<=" + jsrq +
                "  \r\n" +
                ") b\r\n" +
                "on a.mc=b.清算企业\r\n" +
                "left join \r\n" +
                "(\r\n" +
                "   select 企业代码,\r\n" +
                "   sum(基准+高出归己+高出付出) 运费,sum(基准) 基准,sum(高出归己) 高出归己,\r\n" +
                "   sum(高出付出-付外局高出) 付本局其他企业,sum(付外局高出) 付外局高出,\r\n" +
                "   sum(付牵引费_内+付牵引费_电+付线路费+付车辆费+付到达服务费+综合服务费) 承运付费,\r\n" +
                "   sum(付牵引费_内+付牵引费_电) 机车牵引,\r\n" +
                "   sum(自付自牵引) 自付自牵引,\r\n" +
                "   sum(付牵引费_内+付牵引费_电-自付自牵引-付其他牵引内-付其他牵引电) 付本局其他牵引,\r\n" +
                "   sum(付其他牵引内+付其他牵引电) 付外局牵引,\r\n" +
                "   sum(付线路费) 付线路费,sum(自付自线路) 自付自线路,sum(付线路费-自付自线路-付其他线路) 付本局其他线路,sum(付其他线路) 付外局线路,\r\n" +
                "   sum(付车辆费) 付车辆费,sum(自付自车辆) 自付自车辆,sum(付车辆费-自付自车辆-付其他车辆) 付本局其他车辆,sum(付其他车辆) 付外局车辆,\r\n" +
                "   sum(付到达服务费) 付到达费,sum(自付自到达) 自付自到达,sum(付到达服务费-自付自到达-付其他到达) 付本局其他到达,sum(付其他到达) 付外局到达,\r\n" +
                "   sum(付接触网) 付接触网,sum(自付自接触网) 自付自接触网,sum(付本局其他企业接触网) 付本局其他企业接触网,sum(付外局接触网) 付外局接触网,\r\n" +
                "   sum(付长交路) 付长交路\r\n" +
                "   from huizong_承运清算台账\r\n" +
                "  where 日期>=" + ksrq + " and 日期<=" + jsrq +
                "   group by 企业代码\r\n" +
                "   union all\r\n" +
                "   select 'HJ'企业代码,\r\n" +
                "   sum(基准+高出归己+高出付出) 运费,sum(基准) 基准,sum(高出归己) 高出归己,\r\n" +
                "   sum(高出付出-付外局高出) 付本局其他企业,sum(付外局高出) 付外局高出,\r\n" +
                "   sum(付牵引费_内+付牵引费_电+付线路费+付车辆费+付到达服务费+综合服务费) 承运付费,\r\n" +
                "   sum(付牵引费_内+付牵引费_电) 机车牵引,\r\n" +
                "   sum(自付自牵引) 自付自牵引,\r\n" +
                "   sum(付牵引费_内+付牵引费_电-自付自牵引-付其他牵引内-付其他牵引电) 付本局其他牵引,\r\n" +
                "   sum(付其他牵引内+付其他牵引电) 付外局牵引,\r\n" +
                "   sum(付线路费) 付线路费,sum(自付自线路) 自付自线路,sum(付线路费-自付自线路-付其他线路) 付本局其他线路,sum(付其他线路) 付外局线路,\r\n" +
                "   sum(付车辆费) 付车辆费,sum(自付自车辆) 自付自车辆,sum(付车辆费-自付自车辆-付其他车辆) 付本局其他车辆,sum(付其他车辆) 付外局车辆,\r\n" +
                "   sum(付到达服务费) 付到达费,sum(自付自到达) 自付自到达,sum(付到达服务费-自付自到达-付其他到达) 付本局其他到达,sum(付其他到达) 付外局到达,\r\n" +
                "   sum(付接触网) 付接触网,sum(自付自接触网) 自付自接触网,sum(付本局其他企业接触网) 付本局其他企业接触网,sum(付外局接触网) 付外局接触网,\r\n" +
                "   sum(付长交路) 付长交路\r\n" +
                "   from huizong_承运清算台账\r\n" +
                "  where 日期>=" + ksrq + " and 日期<=" + jsrq +
                ")c\r\n" +
                "on a.qydm=c.企业代码\r\n" +
                "order by hh";
        jdbcTemplate.update(sql);
        sql = "select hh,mc,round(b.运费/ " + jldw + ",2) 运费,  round(b.基准 /" + jldw + ",2) 基准,  round(b.高出归己 /" + jldw + ",2) 高出归己,  round(b.付本局其他企业/ " + jldw + ",2) 付本局其他企业,  round(b.付外局高出/ " + jldw + ",2) 付外局高出,  round(b.收本局其他高出/ " + jldw + ",2) 收本局其他高出,  round(b.收外局高出/ " + jldw + ",2) 收外局高出,\r\n" +
                "  round(b.收牵引费 /" + jldw + ",2) 收牵引费,  round(b.自收自牵引/ " + jldw + ",2) 自收自牵引,  round(b.收本局其他牵引 /" + jldw + ",2) 收本局其他牵引,  round(b.收外局牵引/ " + jldw + ",2) 收外局牵引,\r\n" +
                "  round(b.收线路费 /" + jldw + ",2) 收线路费,  round(b.自收自线路/ " + jldw + ",2) 自收自线路,  round(b.收本局其他线路/ " + jldw + ",2) 收本局其他线路,  round(b.收外局线路/ " + jldw + ",2) 收外局线路,\r\n" +
                "  round(b.收车辆费 /" + jldw + ",2) 收车辆费,  round(b.自收自车辆 /" + jldw + ",2) 自收自车辆,  round(b.收本局其他车辆/ " + jldw + ",2) 收本局其他车辆,  round(b.收外局车辆/ " + jldw + ",2) 收外局车辆,\r\n" +
                "  round(b.收到达服务费/ " + jldw + ",2) 收到达服务费,  round(b.自收自到达 /" + jldw + ",2) 自收自到达,  round(b.收本局其他到达/ " + jldw + ",2) 收本局其他到达,  round(b.收外局到达/ " + jldw + ",2) 收外局到达,\r\n" +
                "  round(b.收接触网/ " + jldw + ",2) 收接触网,  round(b.自收自接触网/ " + jldw + ",2) 自收自接触网,  round(b.收本局其他接触网/ " + jldw + ",2) 收本局其他接触网,  round(b.收外局接触网/ " + jldw + ",2) 收外局接触网,  round(b.收长交路/ " + jldw + ",2) 收长交路,\r\n" +
                "  round(b.机车牵引 /" + jldw + ",2) 机车牵引,  round(b.自付自牵引/ " + jldw + ",2) 自付自牵引,  round(b.付本局其他牵引/ " + jldw + ",2) 付本局其他牵引,  round(b.付外局牵引/ " + jldw + ",2) 付外局牵引 ,\r\n" +
                "  round(b.付线路费 /" + jldw + ",2) 付线路费,  round(b.自付自线路 /" + jldw + ",2) 自付自线路,  round(b.付本局其他线路 /" + jldw + ",2) 付本局其他线路,  round(b.付外局线路/ " + jldw + ",2) 付外局线路,\r\n" +
                "  round(b.付车辆费/ " + jldw + ",2) 付车辆费,  round(b.自付自车辆/ " + jldw + ",2) 自付自车辆,  round(b.付本局其他车辆 /" + jldw + ",2) 付本局其他车辆,  round(b.付外局车辆 /" + jldw + ",2)付外局车辆 ,  "
                + " round(b.付到达费/ " + jldw + ",2) 付到达费,round(b.自付自到达/ " + jldw + ",2) 自付自到达,round(b.付本局其他到达/ " + jldw + ",2) 付本局其他到达,round(b.付外局到达/ " + jldw + ",2) 付外局到达,\r\n" +
                "  round(b.付接触网 /" + jldw + ",2) 付接触网,  round(b.自付自接触网 /" + jldw + ",2) 自付自接触网,  round(b.付本局其他企业接触网 /" + jldw + ",2) 付本局其他企业接触网,  round(b.付外局接触网 /" + jldw + ",2) 付外局接触网,\r\n" +
                "  round(b.付长交路 /" + jldw + ",2) 付长交路\r\n" +
                "from temp_承运清算台账 b";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getYssr_dw(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String year = ksrq.substring(0, 4);
        String lastyear = Integer.parseInt(year) - 1 + "";
        String lastksrq = lastyear + ksrq.substring(4, 6);
        String lastjsrq = lastyear + jsrq.substring(4, 6);
        //起始月份数
        int months = Integer.parseInt(jsrq.substring(4, 6)) - Integer.parseInt(ksrq.substring(4, 6)) + 1;
        String sql = "delete from tem_dwyssr";
        jdbcTemplate.update(sql);
        sql = "insert into tem_dwyssr\r\n" +
                "--create table tem_dwyssr as\r\n" +
                "select a.xh,a.mc,c.sr_s,a.ysmb,b.sr\r\n" +
                "from HDY_YSSR_DW_NDJH a\r\n" +
                "left join\r\n" +
                "(\r\n" +
                "  --上年客\r\n" +
                "  select t2.mc,sum(t1.全额净) sr_s from srfx_b510_" + lastyear + " t1,srfx_zmk t2\r\n" +
                "  where t1.日期>=" + lastksrq + "01 and t1.日期<=" + lastjsrq + "31\r\n" +
                "  and t2.rxybz=0\r\n" +
                "  and instr(t2.fwdbm,t1.电报码)>0\r\n" +
                "  and t1.行号 in (1,15,110,115,281,325)\r\n" +
                "  group by t2.mc\r\n" +
                "  union all\r\n" +
                "  --上年\r\n" +
                "  select t2.mc,sum(t1.全额净) sr_s from srfx_b510_" + lastyear + " t1,srfx_zmk t2\r\n" +
                "  where t1.日期>=" + lastksrq + "01 and t1.日期<=" + lastjsrq + "31\r\n" +
                "  and t2.rxybz=4\r\n" +
                "  and instr(t2.fwdbm,t1.电报码)>0\r\n" +
                "  and t1.行号 in (25,50,65,75,77,80,85,175,265,282,283,284,285,286,287,295,320)\r\n" +
                "  group by t2.mc\r\n" +
                ") c\r\n" +
                "on a.mc = c.mc \r\n" +
                "left join\r\n" +
                "(\r\n" +
                "  --客\r\n" +
                "  select t2.mc,sum(t1.全额净) sr from srfx_b510_" + year + " t1,srfx_zmk t2\r\n" +
                "  where t1.日期>=" + ksrq + "01 and t1.日期<=" + jsrq + "01\r\n" +
                "  and t2.rxybz=0\r\n" +
                "  and instr(t2.fwdbm,t1.电报码)>0\r\n" +
                "  and t1.行号 in (1,15,110,115,281,325)\r\n" +
                "  group by t2.mc\r\n" +
                "  union all\r\n" +
                "  --货\r\n" +
                "  select t2.mc,sum(t1.全额净) sr from srfx_b510_" + year + " t1,srfx_zmk t2\r\n" +
                "  where t1.日期>=" + ksrq + "01 and t1.日期<=" + jsrq + "01\r\n" +
                "  and t2.rxybz=4\r\n" +
                "  and instr(t2.fwdbm,t1.电报码)>0\r\n" +
                "  and t1.行号 in (25,50,65,75,77,80,85,175,265,282,283,284,285,286,287,295,320)\r\n" +
                "  group by t2.mc\r\n" +
                "  \r\n" +
                ") b\r\n" +
                "on a.mc = b.mc and nd = " + year + "\r\n" +
                "order by xh";
        jdbcTemplate.update(sql);
        //插入合计
        sql = "insert into tem_dwyssr\r\n" +
                "select 19 xh,'合计' mc,sum(sr_s) sr_s,sum(ysmb) ysmb,sum(sr) sr from tem_dwyssr";
        jdbcTemplate.update(sql);
        sql = "select xh,mc,round(sr_s/" + jldw + ",2) sr_s,round(ysmb/" + jldw + ",2) ysmb,round(ysmb/366*91/" + jldw + ",2) ysmbjd,\r\n" +
                "round(sr/" + jldw + ",2) sr,round((sr - round(ysmb/12*" + months + ",2))/" + jldw + ",2) zj,\r\n" +
                "(case when abs(ysmb)>0 then round((sr - round(ysmb/12*" + months + ",2))/ysmb,2) else 0 end) zjb\r\n" +
                "from tem_dwyssr";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getYssr_kyz(CyfxSearchModel searchInfo) {
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String tj = "";//添加所有条件
        String dw = searchInfo.getDw();//单位（企业）
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            tj += " and t2.id ='" + dw + "'";
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String year = ksrq.substring(0, 4);
        String lastyear = Integer.parseInt(year) - 1 + "";
        String lastksrq = lastyear + ksrq.substring(4, 6);
        String lastjsrq = lastyear + jsrq.substring(4, 6);
        //起始月份数
        int months = Integer.parseInt(jsrq.substring(4, 6)) - Integer.parseInt(ksrq.substring(4, 6)) + 1;
        String sql = "delete from tem_yssr_ky";
        jdbcTemplate.update(sql);
        sql = "insert into tem_yssr_ky\r\n" +
                "--create table tem_yssr_ky as\r\n" +
                "select t1.dbm,rs_s,pj_s,zf_s,rs,pj,zf\r\n" +
                "from \r\n" +
                "(\r\n" +
                "select  (case when a.dbm is null then b.dbm else a.dbm end) dbm,\r\n" +
                "(case when a.rs is null then b.rs else a.rs+nvl(b.rs,0) end) rs,\r\n" +
                "(case when pj is null then 0 else a.pj end) pj,\r\n" +
                "(case when b.zf is null then 0 else b.zf end) zf \r\n" +
                "from\r\n" +
                "  (\r\n" +
                "    select 电报码 dbm,sum(人数) rs,\r\n" +
                "    sum(t.旅客票价+t.青藏线格拉段加价款+t.高出国铁票价款+t.拉日段加价-t.旅客票价税-t.青藏线格拉段加价款税-t.高出国铁票价款税-t.拉日段加价税) pj\r\n" +
                "    from SRFX_B545_" + year + " t\r\n" +
                "    where 日期>=" + ksrq + "01 and 日期<=" + jsrq + "31\r\n" +
                "    group by 电报码\r\n" +
                "  ) a \r\n" +
                "  full outer join\r\n" +
                "  (\r\n" +
                "	 select dbm,sum(rs) rs,sum(zf) zf from\r\n" +
                "    (--客票杂费\r\n" +
                "    select 电报码 dbm,sum(人数) rs,\r\n" +
                "    sum(t.卧铺订票费70+t.卧铺订票费30+t.候车室空调费+t.客票发展金-t.卧铺订票费70税-t.卧铺订票费30税-t.候车室空调费税-t.客票发展金税) zf\r\n" +
                "    from SRFX_B545_" + year + " t\r\n" +
                "  	 where 日期>=" + ksrq + "01 and 日期<=" + jsrq + "31\r\n" +
                "    group by 电报码\r\n" +
                "    union all\r\n" +
                "    --客杂\r\n" +
                "    select 电报码 dbm,sum(车补人数+站补人数) rs,sum(车补税后+站补税后) zf\r\n" +
                "    from SRFX_B542_" + year + " t\r\n" +
                "  	 where 日期>=" + ksrq + "01 and 日期<=" + jsrq + "31\r\n" +
                "    group by 电报码\r\n" +
                "    union all\r\n" +
                "    --行包\r\n" +
                "    select 电报码 dbm,0 rs,\r\n" +
                "    sum(行李运费+包裹运费-行李税-包裹税+行包发站装卸费+行包到站装卸费-发站装卸费税-到站装卸费税+保价款-保价费税) zf\r\n" +
                "    from SRFX_B546_" + year + " t\r\n" +
                "  	 where 日期>=" + ksrq + "01 and 日期<=" + jsrq + "31\r\n" +
                "    group by 电报码\r\n" +
                "	) group by dbm\r\n" +
                "  ) b\r\n" +
                "  on a.dbm=b.dbm\r\n" +
                ") t1\r\n" +
                " full outer join\r\n" +
                "(\r\n" +
                "select (case when c.dbm is null then d.dbm else c.dbm end) dbm,\r\n" +
                "(case when c.rs_s is null then d.rs_s else c.rs_s+nvl(d.rs_s,0) end) rs_s,\r\n" +
                "(case when pj_s is null then 0 else c.pj_s end) pj_s,\r\n" +
                "(case when d.zf_s is null then 0 else d.zf_s end) zf_s from\r\n" +
                "  (\r\n" +
                "    select 电报码 dbm,sum(人数) rs_s,\r\n" +
                "    sum(t.旅客票价+t.青藏线格拉段加价款+t.高出国铁票价款+t.拉日段加价-t.旅客票价税-t.青藏线格拉段加价款税-t.高出国铁票价款税-t.拉日段加价税) pj_s\r\n" +
                "    from SRFX_B545_" + lastyear + " t\r\n" +
                "  	 where 日期>=" + lastksrq + "01 and 日期<=" + lastjsrq + "31\r\n" +
                "    group by 电报码\r\n" +
                "  ) c\r\n" +
                "  full outer join\r\n" +
                "  (\r\n" +
                "	 select dbm,sum(rs_s) rs_s,sum(zf_s) zf_s from" +
                "    (--客票杂费\r\n" +
                "    select 电报码 dbm,sum(人数) rs_s,\r\n" +
                "    sum(t.卧铺订票费70+t.卧铺订票费30+t.候车室空调费+t.客票发展金-t.卧铺订票费70税-t.卧铺订票费30税-t.候车室空调费税-t.客票发展金税) zf_s\r\n" +
                "    from SRFX_B545_" + lastyear + " t\r\n" +
                "     where 日期>=" + lastksrq + "01 and 日期<=" + lastjsrq + "31\r\n" +
                "    group by 电报码\r\n" +
                "    union all\r\n" +
                "    --客杂\r\n" +
                "    select 电报码 dbm,sum(车补人数+站补人数) rs_s,sum(车补税后+站补税后) zf_s\r\n" +
                "    from SRFX_B542_" + lastyear + " t\r\n" +
                "  	 where 日期>=" + lastksrq + "01 and 日期<=" + lastjsrq + "31\r\n" +
                "    group by 电报码\r\n" +
                "    union all\r\n" +
                "    --行包\r\n" +
                "    select 电报码 dbm,0 rs_s,\r\n" +
                "    sum(行李运费+包裹运费-行李税-包裹税+行包发站装卸费+行包到站装卸费-发站装卸费税-到站装卸费税+保价款-保价费税) zf_s\r\n" +
                "    from SRFX_B546_" + lastyear + " t\r\n" +
                "  	 where 日期>=" + lastksrq + "01 and 日期<=" + lastjsrq + "31\r\n" +
                "    group by 电报码\r\n" +
                " ) group by dbm\r\n" +
                "  ) d\r\n" +
                "  on c.dbm=d.dbm\r\n" +
                ")t2 \r\n" +
                "on t1.dbm = t2.dbm";
        jdbcTemplate.update(sql);
        sql = "select t1.DBM,t1.MC,t1.RS_S,t1.PJ_S,t1.ZF_S,t1.HJ1,t1.NDRS,t1.NDPJ,t1.NDZF,t1.HJ2,t1.RSJN,t1.PJJN,t1.ZFJN,t1.HJ3,t1.RS,t1.PJ,t1.ZF,t1.HJ4 \r\n" +
                "from \r\n" +
                "(" +
                "select a.dbm,a.mc,round(b.rs_s/" + jldw + ",2) rs_s,round(b.pj_s/" + jldw + ",2) pj_s,round(b.zf_s/" + jldw + ",2) zf_s,round((b.pj_s+b.zf_s)/" + jldw + ",2) hj1,\r\n" +
                "round(a.rs/" + jldw + ",2) ndrs,round(a.pj/" + jldw + ",2) ndpj,round(a.zf/" + jldw + ",2) ndzf,round((a.pj+a.zf)/" + jldw + ",2) hj2,\r\n" +
                "round(a.rs/12*" + months + "/" + jldw + ",2) rsjn,round(a.pj/12*" + months + "/" + jldw + ") pjjn,round(a.zf/12*" + months + "/" + jldw + ",2) zfjn,round((a.pj+a.zf)/12*" + months + "/" + jldw + ",2) hj3,\r\n" +
                "round(b.rs/" + jldw + ",2) rs,round(b.pj/" + jldw + ",2) pj,round(b.zf/" + jldw + ",2) zf,round((b.pj+b.zf)/" + jldw + ",2) hj4\r\n" +
                "from \r\n" +
                "hdy_yssr_ky_ndjh a \r\n" +
                "left join\r\n" +
                "(\r\n" +
                "select dbm,sum(rs_s) rs_s,sum(pj_s) pj_s,sum(zf_s) zf_s,sum(rs) rs,sum(pj) pj,sum(zf) zf\r\n" +
                "from TEM_YSSR_KY \r\n" +
                "group by dbm\r\n" +
                ") b\r\n" +
                "on a.dbm = b.dbm\r\n" +
                "order by a.dbm" +
                ") t1,srfx_zmk t2\r\n" +
                "where  instr(t2.fwdbm,t1.dbm)>0\r\n" +
                "and t2.rxybz=0 " + tj;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getYssr_kyzmx(CyfxSearchModel searchInfo) {
        String tj = "";//添加所有条件
        String dw = searchInfo.getDw();//单位（企业）
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            tj += " and t2.id ='" + dw + "'";
        }
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String year = ksrq.substring(0, 4);
        String sql = "delete from tem_yssr_kyz";
        jdbcTemplate.update(sql);
        sql = "insert into tem_yssr_kyz\r\n" +
                "--create table tem_yssr_kyz as\r\n" +
                "select czid,t2.mc zm,\r\n" +
                "sum(rs) rs,\r\n" +
                "sum(case when fzj=dzj and fzj='R' then rs else 0 end) rs_gn,\r\n" +
                "sum(jcpj) jcpj,\r\n" +
                "sum(case when ddqydm='R' then jcpj else 0 end) jcpj_bj,\r\n" +
                "sum(case when fzj=dzj and fzj='R' and ddqydm='R' then jcpj else 0 end) jcpj_bjgn,\r\n" +
                "sum(case when ddqydm<>'R' then jcpj else 0 end) jcpj_wj,\r\n" +
                "sum(case when fzj=dzj and fzj='R' and ddqydm<>'R' then jcpj else 0 end) jcpj_wjgn,\r\n" +
                "sum(wpdpf7+wpdpf3+czktf+rpf) kqt,\r\n" +
                "sum(case when fzj=dzj and fzj='R' then (wpdpf7+wpdpf3+czktf+rpf) else 0 end) kqt_gn\r\n" +
                "from cyfx_rb_ky t1,srfx_zmk t2\r\n" +
                "where  t1.czid=t2.dbm\r\n" +
                "and t2.cztype=1\r\n" +
                "group by czid,t2.mc\r\n" +
                "union all\r\n" +
                "select 电报码 dbm,补票站段 cz,\r\n" +
                "sum(车补人数+站补人数) rs,\r\n" +
                "sum(case when fzdbm=dzdbm and FZDBM='乌'  then (车补人数+站补人数) else 0 end) rs_gn,\r\n" +
                "0,0,0,0,0,\r\n" +
                "sum(车补税后+站补税后) zf,\r\n" +
                "sum(case when fzdbm=dzdbm and fzdbm='乌' then (车补税后+站补税后) else 0 end) zf_gn\r\n" +
                "from SRFX_B542_" + year + "D\r\n" +
                "where 日期>=" + ksrq + " and 日期<=" + jsrq + "\r\n" +
                "group by 电报码,补票站段\r\n" +
                "union all\r\n" +
                "select 电报码 dbm,收款车站 zm,\r\n" +
                "0,0,\r\n" +
                "0,0,0,0,0,\r\n" +
                "sum(行李运费+包裹运费-行李税-包裹税+行包发站装卸费+行包到站装卸费-发站装卸费税-到站装卸费税+保价款-保价费税) zf,\r\n" +
                "sum(case when 段代码=企业代码 and 段代码='R' then (行李运费+包裹运费-行李税-包裹税+行包发站装卸费+行包到站装卸费-发站装卸费税-到站装卸费税+保价款-保价费税) else 0 end) zf_gn\r\n" +
                "from SRFX_B546_" + year + "\r\n" +
                "where 日期>=" + ksrq + " and 日期<=" + jsrq + "\r\n" +
                "group by 电报码,收款车站";
        jdbcTemplate.update(sql);
        sql = "select t1.* from\r\n" +
                "(\r\n" +
                "  select czid,zm,sum(rs) rs,sum(rs_gn) rs_gn,sum(rs-rs_gn) rs_zt,\r\n" +
                "  sum(jcpj) jcpj,\r\n" +
                "  sum(jcpj_bjgn) jcpj_bjgn,sum(jcpj_bj-jcpj_bjgn) jcpj_bjzt,\r\n" +
                "  sum(jcpj_wjgn) jcpj_wjgn,sum(jcpj_wj-jcpj_wjgn) jcpj_wjzt,\r\n" +
                "  sum(kqt) zf,\r\n" +
                "  sum(kqt_gn) zf_gn,sum(kqt-kqt_gn) zf_zt\r\n" +
                "  from tem_yssr_kyz\r\n" +
                "  group by zm,czid\r\n" +
                "  order by czid\r\n" +
                ") t1 ,srfx_zmk t2 \r\n" +
                "where  instr(t2.fwdbm,t1.czid) > 0\r\n" +
                "and t2.rxybz=0 " + tj;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getCYFX_ysss(CyfxSearchModel searchInfo) {
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String nf = ksrq.substring(0, 4);
        String lertksrq = ksrq.substring(4, 6);
        String lertjsrq = jsrq.substring(4, 6);
        String yf = ksrq.substring(4, 6);
        int nf1 = Integer.valueOf(nf) - 1;

        String yearnf = nf1 + "";
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String sql = "delete from tem_yszsr";
        jdbcTemplate.update(sql);
        insertYsss(nf, ksrq, jsrq);
        insertYsss(yearnf, yearnf + lertksrq, yearnf + lertjsrq);
        sql = "select a.id,a.mc,a.dw,\r\n" +
                "\r\n" +
                "round(b.rs_s/" + jldw + ",2) rs_s,round(b.kysr_s/" + jldw + ",2) kysr_s,b.srl_s,\r\n" +
                "round(d.gzl/" + jldw + ",2) gzl,round(d.sr/" + jldw + ",2) sr,d.srl,\r\n" +
                "round(d.gzl/3/" + jldw + ",2) gzl_jd,round(d.sr/3/" + jldw + ",2) sr_jd,(case when abs(d.gzl)>0 then round(d.sr/d.gzl,2) else 0 end) srl_jd,\r\n" +
                "round(c.rs/" + jldw + ",2) rs_j,round(c.kysr/" + jldw + ",2) kysr_j,c.srl srl_j,\r\n" +
                "round((c.rs-round(d.gzl/12*" + yf + ",2))/" + jldw + ",2) gzl_zj,round((c.kysr-round(d.sr/12*" + yf + ",2))/" + jldw + ",2) srzj,\r\n" +
                "(c.srl-(case when abs(d.gzl)>0 then round(d.sr/d.gzl,2) else 0 end)) srl_zj\r\n" +
                "from \r\n" +
                "HDY_YSZSR a\r\n" +
                "left join\r\n" +
                "(\r\n" +
                "  select xh,rs as rs_s,kysr as kysr_s,(case when abs(rs)>0 then round(kysr/rs,2) else 0 end) srl_s from tem_yszsr\r\n" +
                "  where nd=" + yearnf + "\r\n" +
                ") b\r\n" +
                "on a.xh=b.xh\r\n" +
                "left join\r\n" +
                "(\r\n" +
                "  select xh,rs,kysr,(case when abs(rs)>0 then round(kysr/rs,2) else 0 end) srl from tem_yszsr\r\n" +
                "  where nd=" + nf + "\r\n" +
                ") c\r\n" +
                "on a.xh=c.xh\r\n" +
                "left join\r\n" +
                "(\r\n" +
                "  select * from HDY_YSZSR_NDJH t\r\n" +
                "  where nd=" + nf + "\r\n" +
                ") d\r\n" +
                "on a.id=d.id\r\n" +
                "order by a.id";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getYssr_hyz(CyfxSearchModel searchInfo) {
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String nf = ksrq.substring(0, 4);
        int nf1 = Integer.valueOf(nf) - 1;
        String yearnf = nf1 + "";
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String tj = "";
        String dw = searchInfo.getDw();//单位（企业）
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            tj += " and t2.id ='" + dw + "'";
        }
        String lasttime1 = yearnf + ksrq.substring(4, 6);//yyyymmdd 上年开始
        String lasttime = yearnf + jsrq.substring(4, 6);//yyyymmdd 上年结束
        //起始月份数
        int months = Integer.parseInt(jsrq.substring(4, 6)) - Integer.parseInt(ksrq.substring(4, 6)) + 1;
        String sql = "delete from tem_b573";
        jdbcTemplate.update(sql);
        insertHyz(nf, ksrq, jsrq, tj);
        insertHyz(yearnf, lasttime1, lasttime, tj);
        sql = "select nd,mc,zm,\r\n" +
                "    sum(jfzl)jfzl,sum(yf)yf,sum(zf)zf,sum(jsjj)jsjj,sum(hj)hj,\r\n" +
                "    sum(jfzl_n)jfzl_n,sum(yf_n)yf_n,sum(zf_n)zf_n,sum(jsjj_n)jsjj_n,sum(hj_n)hj_n,\r\n" +
                "    sum(jfzl_jd),sum(yf_jd),sum(zf_jd),sum(jsjj_jd),sum(hj_jd),\r\n" +
                "    sum(jfzl_j)jfzl_j,sum(yf_j)yf_j,sum(zf_j)zf_j,sum(jsjj_j)jsjj_j,sum(hj_j)hj_j\r\n" +
                "from\r\n" +
                "("
                + "select a.nd,a.mc,a.zm,\r\n" +
                "round(a.jfzl/" + jldw + ",2) jfzl,round(a.yf/" + jldw + ",2) yf,round(a.zf/" + jldw + ",2) zf,round(a.jsjj/" + jldw + ",2) jsjj, round(a.yf/" + jldw + ",2)+round(a.zf/" + jldw + ",2)+round(a.jsjj/" + jldw + ",2) hj,--2019\r\n" +
                "0 jfzl_n,0 yf_n,0 zf_n,0 jsjj_n,0 hj_n,--2020预算\r\n" +
                "round(b.jfzl/12*" + months + ",2) jfzl_jd,round(b.yf/12*" + months + ",2) yf_jd,round(b.zf/12*" + months + ",2) zf_jd,round(b.jsjj/12*" + months + ",2) jsjj_jd,round(b.yf/12*" + months + ",2)+round(b.zf/months*" + months + ",2)+round(b.jsjj/12*" + months + ",2) hj_jd,--2020预算进度\r\n" +
                "round(b.jfzl/" + jldw + ",2) jfzl_j,round(b.yf/" + jldw + ",2) yf_j,round(b.zf/" + jldw + ",2) zf_j,round(b.jsjj/" + jldw + ",2) jsjj_j,round(b.yf/" + jldw + ",2)+round(b.zf/" + jldw + ",2)+round(b.jsjj/" + jldw + ",2) hj_j--2020实际\r\n" +
                "from \r\n" +
                "(\r\n" +
                "    --2019年实际\r\n" +
                "    select nd,mc,zm,jfzl,yf,zf,jsjj,hj\r\n" +
                "    from tem_b573\r\n" +
                "    where nd = " + yearnf + "\r\n" +
                ") a\r\n" +
                "left join\r\n" +
                "(\r\n" +
                "  --2020年实际\r\n" +
                "  select nd,mc,zm,jfzl,yf,zf,jsjj,hj\r\n" +
                "  from tem_b573\r\n" +
                "  where nd = " + nf + "\r\n" +
                ") b\r\n" +
                "on a.mc=b.mc\r\n" +
                "order by a.mc"
                + ")\r\n" +
                "group by nd,zm,mc";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getPjsr_cc(CyfxSearchModel searchInfo) {
        String ksrq = searchInfo.getKSRQ().replace("-", "");
        String jsrq = searchInfo.getJSRQ().replace("-", "");
        String nf = ksrq.substring(0, 4);
        int nf1 = Integer.valueOf(nf) - 1;
        String yearnf = nf1 + "";
        String tj = " ";
        String dw = searchInfo.getDw();//单位（企业）
        if (dw != null && !dw.equals("") && !dw.equals("0")) {
            tj += " and t2.担当企业代码 ='" + dw + "'";
        }
        int jldw = 10000;
        if (searchInfo.getJldw().equals("1")) {//默认：0、万元，1、元
            jldw = 1;
        }
        String sql = "select 0 序号,t2.列车始发车次 as cc,t2.列车始发站||'-'||t2.列车终到站 as yxqy,t2.列车担当企业 qy,\r\n" +
                "       round(sum(t1.定员人数)/" + jldw + ",2) rs,round(sum(t1.定员票价)/" + jldw + ",2) pj,\r\n" +
                "       round(sum(t2.合计人数)/" + jldw + ",2) as 管内,round(sum(t2.合计税后-t2.合计人数)/" + jldw + ",2) as 直通,round(sum(t2.合计税后)/" + jldw + ",2) as 小计,\r\n" +
                "       round(sum(t2.乌鲁木齐局人数)/" + jldw + ",2) as 管内_w,round(sum(t2.乌鲁木齐局税后 - t2.乌鲁木齐局人数)/" + jldw + ",2) as 直通_w,round(sum(t2.乌鲁木齐局税后)/" + jldw + ",2) as 小计_w\r\n" +
                "from srfx_lcjcxx_" + nf + "new t1,srfx_b541_" + yearnf + " t2\r\n" +
                "where t1.日期>=" + ksrq + " and t1.日期<=" + jsrq + tj +
                "and t1.始发车次=t2.列车始发车次 and t1.电报码 = '017'\r\n" +
                "group by t2.列车始发车次,t2.列车始发站,t2.列车终到站,t2.列车担当企业";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

        return list;
    }

    public void insertYsss(String nf, String ksrq, String jsrq) {
        String sql = "insert into tem_yszsr\r\n" +
                "--create table tem_yszsr as\r\n" +
                "select " + nf + " nd,xh,sum(rs) rs,sum(pj) kysr from\r\n" +
                "(\r\n" +
                "  select 3 as xh,sum(人数) rs,\r\n" +
                "  sum(t.旅客票价+t.青藏线格拉段加价款+t.高出国铁票价款+t.拉日段加价-t.旅客票价税-t.青藏线格拉段加价款税-t.高出国铁票价款税-t.拉日段加价税) pj\r\n" +
                "  from SRFX_B545_" + nf + " t\r\n" +
                "  where 日期>=" + ksrq + "01 and 日期<=" + jsrq + "31 \r\n" +
                "  union all\r\n" +
                "  select 4 as xh,sum(乌鲁木齐局人数) rs,sum(乌鲁木齐局税后) pj from SRFX_B541_" + nf + "\r\n" +
                "  where 日期>=" + ksrq + "01 and 日期<=" + jsrq + "31 \r\n" +
                "  union all\r\n" +
                "  --客票杂费\r\n" +
                "  select 6 as xh,sum(人数) rs,\r\n" +
                "  sum(t.卧铺订票费70+t.卧铺订票费30+t.候车室空调费+t.客票发展金-t.卧铺订票费70税-t.卧铺订票费30税-t.候车室空调费税-t.客票发展金税) zf\r\n" +
                "  from SRFX_B545_" + nf + " t\r\n" +
                "  where 日期>=" + ksrq + "01 and 日期<=" + jsrq + "31 \r\n" +
                "  union all\r\n" +
                "  --客杂\r\n" +
                "  select 6 as xh,sum(车补人数+站补人数) rs,sum(车补税后+站补税后) zf\r\n" +
                "  from SRFX_B542_" + nf + " t\r\n" +
                "  where 日期>=" + ksrq + "01 and 日期<=" + jsrq + "31 \r\n" +
                "  union all\r\n" +
                "  --行包\r\n" +
                "  select 6 as xh,0 rs,\r\n" +
                "  sum(行李运费+包裹运费-行李税-包裹税+行包发站装卸费+行包到站装卸费-发站装卸费税-到站装卸费税+保价款-保价费税) zf\r\n" +
                "  from SRFX_B546_" + nf + " t\r\n" +
                "  where 日期>=" + ksrq + "01 and 日期<=" + jsrq + "31 \r\n" +
                "  group by 日期,电报码\r\n" +
                ")\r\n" +
                "group by xh";
        jdbcTemplate.update(sql);
        sql = "--计算客运收入\r\n" +
                "insert into tem_yszsr\r\n" +
                "select nd,2 xh,sum(rs),sum(kysr) from tem_yszsr where (xh=3 or xh=6) and nd= " + nf + "  group by nd";
        jdbcTemplate.update(sql);
        sql = "insert into tem_yszsr\r\n" +
                "select " + nf + " nd,5 xh,\r\n" +
                "((select rs from tem_yszsr where xh=3 and nd=" + nf + ")-(select rs from tem_yszsr where xh=4 and nd=" + nf + ")) rs,\r\n" +
                "((select kysr from tem_yszsr where xh=3 and nd=" + nf + ")-(select kysr from tem_yszsr where xh=4 and nd=" + nf + ")) kysr\r\n" +
                "from dual";
        jdbcTemplate.update(sql);
        sql = "--货运收入\r\n" +
                "insert into tem_yszsr\r\n" +
                "select " + nf + " nd,xh,sum(jfzl) gzl,sum(hysr) sr from CYFX_RB2_HY t\r\n" +
                "where xh in (18,21,22,23,41,48,51,54,55,56,74,81)\r\n" +
                "and rq>=" + ksrq + "01 and rq<=" + jsrq + "31 \r\n" +
                "group by xh";
        System.err.println("sql===========" + sql);
        jdbcTemplate.update(sql);
    }

    public void insertHyz(String nf, String ksrq, String jsrq, String tj) {
        String sql = "insert into tem_b573\r\n" +
                "--create table tem_b573 as\r\n" +
                "select \r\n" +
                "    nd,mc,zm,sum(jfzl) jfzl,sum(yf) yf,sum(zf) zf,sum(jsjj) jsjj,sum(yf+zf+jsjj) hj\r\n" +
                "from\r\n" +
                "(\r\n" +
                "    select " + nf + " nd,t2.mc mc,收款车站 zm,sum(计费重量) jfzl, sum(实收费用小计) yf,sum(实收杂费小计) as zf,sum(实收建设基金) as jsjj,0 hj \r\n" +
                "    from srfx_b573_" + nf + " t1,srfx_zmk t2\r\n" +
                "    where 日期>=" + ksrq + "01 and 日期<=" + jsrq + "31 " + tj + "\r\n" +
                "    and instr(t2.fwdbm,t1.电报码)>0\r\n" +
                "    and t2.rxybz=4\r\n" +
                "    group by t2.mc,收款车站\r\n" +
                "    union all\r\n" +
                "    select " + nf + " nd,t2.mc,收款车站 zm,sum(计费重量) jfzl, 0 yf,sum(税后费用合计金额) as zf,sum(税后建设基金) as jsjj,0 hj \r\n" +
                "    from srfx_b572_" + nf + " t1,srfx_zmk t2\r\n" +
                "    where 日期>=" + ksrq + "01 and 日期<=" + jsrq + "31 " + tj + "\r\n" +
                "    and instr(t2.fwdbm,t1.电报码)>0\r\n" +
                "    and t2.rxybz=4\r\n" +
                "    group by t2.mc,收款车站\r\n" +
                ")\r\n" +
                "group by nd,mc,zm";
        jdbcTemplate.update(sql);
    }
}
