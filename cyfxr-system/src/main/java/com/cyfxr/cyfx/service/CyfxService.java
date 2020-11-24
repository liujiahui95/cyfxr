package com.cyfxr.cyfx.service;

import com.cyfxr.system.domain.CyfxSearchModel;

import java.util.List;

public interface CyfxService {
    public List<Object[]> getFb1(CyfxSearchModel searchInfo);

    public List<Object[]> getFb2_pl(CyfxSearchModel searchInfo);

    public List<Object[]> getFb2_cx(CyfxSearchModel searchInfo);

    public List<Object[]> getFb2_zz(CyfxSearchModel searchInfo);

    public List<Object[]> getFb2_yj(CyfxSearchModel searchInfo);

    public List<Object[]> getFb2_yjh(CyfxSearchModel searchInfo);

    public List<Object[]> getFb2_ysfs(CyfxSearchModel searchInfo);

    public List<Object[]> getFb3_yjh(CyfxSearchModel searchInfo);

    public List<Object[]> getFb3_pl(CyfxSearchModel searchInfo);

    public List<Object[]> getFb3_dj(CyfxSearchModel searchInfo);

    public List<Object[]> getFb3_zz(CyfxSearchModel searchInfo);

    public List<Object[]> getFb3_yj(CyfxSearchModel searchInfo);

    public List<Object[]> getFb3_fz(CyfxSearchModel searchInfo);

    public List<Object[]> getFb7_pl(CyfxSearchModel searchInfo);

    public List<Object[]> getFb7_dj(CyfxSearchModel searchInfo);

    public List<Object[]> getFb7_fz(CyfxSearchModel searchInfo);

    public List<Object[]> getFb7_zz(CyfxSearchModel searchInfo);

    public List<Object[]> getFb7_yjh(CyfxSearchModel searchInfo);

    public List<Object[]> getFb7_yj(CyfxSearchModel searchInfo);

    public List<Object[]> getFb7_xm(CyfxSearchModel searchInfo);

    //各单位运输收入情况
    public List<Object[]> getYssr_dw(CyfxSearchModel searchInfo);

    public List<Object[]> getYssr_kyz(CyfxSearchModel searchInfo);

    public List<Object[]> getYssr_kyzmx(CyfxSearchModel searchInfo);

    public List<Object[]> getCYFX_ysss(CyfxSearchModel searchInfo);

    public List<Object[]> getYssr_hyz(CyfxSearchModel searchInfo);

    public List<Object[]> getPjsr_cc(CyfxSearchModel searchInfo);
}
