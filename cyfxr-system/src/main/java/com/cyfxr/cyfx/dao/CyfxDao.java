package com.cyfxr.cyfx.dao;

import com.cyfxr.system.domain.CyfxSearchModel;

import java.util.List;
import java.util.Map;

public interface CyfxDao {
    /**
     * 附表
     *
     * @param searchInfo
     * @return
     */
    public List<Map<String, Object>> getFb1(CyfxSearchModel searchInfo);

    public List<Map<String, Object>> getFb1_1(CyfxSearchModel searchInfo);

    public List<Map<String, Object>> getFb2_cx(CyfxSearchModel searchInfo);//车型

    public List<Map<String, Object>> getFb2_pl(CyfxSearchModel searchInfo);//品类

    public List<Map<String, Object>> getFb2_zz(CyfxSearchModel searchInfo);//静载重

    public List<Map<String, Object>> getFb2_yj(CyfxSearchModel searchInfo);//静运距

    public List<Map<String, Object>> getFb2_yjh(CyfxSearchModel searchInfo);//运价号

    public List<Map<String, Object>> getFb2_ysfs(CyfxSearchModel searchInfo);//运输方式

    //表三
    public List<Map<String, Object>> getFb3_pl(CyfxSearchModel searchInfo);//品类

    public List<Map<String, Object>> getFb3_dj(CyfxSearchModel searchInfo);//到局

    public List<Map<String, Object>> getFb3_zz(CyfxSearchModel searchInfo);//静载重

    public List<Map<String, Object>> getFb3_yjh(CyfxSearchModel searchInfo);//运价号

    public List<Map<String, Object>> getFb3_yj(CyfxSearchModel searchInfo);//运距

    public List<Map<String, Object>> getFb3_fz(CyfxSearchModel searchInfo);//发站

    //表七
    public List<Map<String, Object>> getFb7_yjh(CyfxSearchModel searchInfo);//运价号

    public List<Map<String, Object>> getFb7_yj(CyfxSearchModel searchInfo);//运距

    public List<Map<String, Object>> getFb7_pl(CyfxSearchModel searchInfo);//品类

    public List<Map<String, Object>> getFb7_dj(CyfxSearchModel searchInfo);//到局

    public List<Map<String, Object>> getFb7_fz(CyfxSearchModel searchInfo);//发站

    public List<Map<String, Object>> getFb7_zz(CyfxSearchModel searchInfo);//静载重

    public List<Map<String, Object>> getFb7_xm(CyfxSearchModel searchInfo);//项目

    //各单位运输收入情况
    public List<Map<String, Object>> getYssr_dw(CyfxSearchModel searchInfo);

    //客运运输收入完成情况表-按客运站
    public List<Map<String, Object>> getYssr_kyz(CyfxSearchModel searchInfo);

    //客运运输收入完成情况表-按客运明细
    public List<Map<String, Object>> getYssr_kyzmx(CyfxSearchModel searchInfo);

    public List<Map<String, Object>> getCYFX_ysss(CyfxSearchModel searchInfo);

    public List<Map<String, Object>> getYssr_hyz(CyfxSearchModel searchInfo);

    //旅客票价收入-分车次
    public List<Map<String, Object>> getPjsr_cc(CyfxSearchModel searchInfo);
}
