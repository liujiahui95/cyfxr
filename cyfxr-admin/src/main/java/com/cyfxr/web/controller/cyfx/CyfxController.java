package com.cyfxr.web.controller.cyfx;

import com.cyfxr.common.core.controller.BaseController;
import com.cyfxr.common.core.page.TableDataInfo;
import com.cyfxr.cyfx.service.CyfxService;
import com.cyfxr.system.domain.CyfxSearchModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/cyfx")
public class CyfxController extends BaseController {
    @Autowired
    private CyfxService cyfxService;

    @PostMapping("/fb1")
    @ResponseBody
    public TableDataInfo getFb1(CyfxSearchModel searchInfo) {
        List<Object[]> fb1 = cyfxService.getFb1(searchInfo);
        return getDataTable(fb1);
    }

    @PostMapping("/fb2_pl")
    @ResponseBody
    public TableDataInfo getFb2_pl(CyfxSearchModel searchInfo) {
        List<Object[]> fb2_pl = cyfxService.getFb2_pl(searchInfo);
        return getDataTable(fb2_pl);
    }

    @PostMapping("/fb2_cx")
    @ResponseBody
    public TableDataInfo getFb2_cx(CyfxSearchModel searchInfo) {
        List<Object[]> fb2_cx = cyfxService.getFb2_cx(searchInfo);
        return getDataTable(fb2_cx);
    }

    @PostMapping("/fb2_zz")
    @ResponseBody
    public TableDataInfo getFb2_zz(CyfxSearchModel searchInfo) {
        List<Object[]> fb2_zz = cyfxService.getFb2_zz(searchInfo);
        return getDataTable(fb2_zz);
    }

    @PostMapping("/fb2_yj")
    @ResponseBody
    public TableDataInfo getFb2_yj(CyfxSearchModel searchInfo) {
        List<Object[]> fb2_yj = cyfxService.getFb2_yj(searchInfo);
        return getDataTable(fb2_yj);
    }

    @PostMapping("/fb2_yjh")
    @ResponseBody
    public TableDataInfo getFb2_yjh(CyfxSearchModel searchInfo) {
        List<Object[]> fb2_yjh = cyfxService.getFb2_yjh(searchInfo);
        return getDataTable(fb2_yjh);
    }

    @PostMapping("/fb2_ysfs")
    @ResponseBody
    public TableDataInfo getFb2_ysfs(CyfxSearchModel searchInfo) {
        List<Object[]> fb2_ysfs = cyfxService.getFb2_ysfs(searchInfo);
        return getDataTable(fb2_ysfs);
    }

    @PostMapping("/fb3_yjh")
    @ResponseBody
    public TableDataInfo getFb3_yjh(CyfxSearchModel searchInfo) {
        List<Object[]> fb3_yjh = cyfxService.getFb3_yjh(searchInfo);
        return getDataTable(fb3_yjh);
    }

    @PostMapping("/fb3_pl")
    @ResponseBody
    public TableDataInfo getFb3_pl(CyfxSearchModel searchInfo) {
        List<Object[]> fb3_pl = cyfxService.getFb3_pl(searchInfo);
        return getDataTable(fb3_pl);
    }

    @PostMapping("/fb3_dj")
    @ResponseBody
    public TableDataInfo getFb3_dj(CyfxSearchModel searchInfo) {
        List<Object[]> fb3_dj = cyfxService.getFb3_dj(searchInfo);
        return getDataTable(fb3_dj);
    }

    @PostMapping("/fb3_zz")
    @ResponseBody
    public TableDataInfo getFb3_zz(CyfxSearchModel searchInfo) {
        List<Object[]> fb3_zz = cyfxService.getFb3_zz(searchInfo);
        return getDataTable(fb3_zz);
    }

    @PostMapping("/fb3_yj")
    @ResponseBody
    public TableDataInfo getFb3_yj(CyfxSearchModel searchInfo) {
        List<Object[]> fb3_yj = cyfxService.getFb3_yj(searchInfo);
        return getDataTable(fb3_yj);
    }

    @PostMapping("/fb3_fz")
    @ResponseBody
    public TableDataInfo getFb3_fz(CyfxSearchModel searchInfo) {
        List<Object[]> fb3_fz = cyfxService.getFb3_fz(searchInfo);
        return getDataTable(fb3_fz);
    }

    @PostMapping("/fb7_pl")
    @ResponseBody
    public TableDataInfo getFb7_pl(CyfxSearchModel searchInfo) {
        List<Object[]> fb7_pl = cyfxService.getFb7_pl(searchInfo);
        return getDataTable(fb7_pl);
    }

    @PostMapping("/fb7_dj")
    @ResponseBody
    public TableDataInfo getFb7_dj(CyfxSearchModel searchInfo) {
        List<Object[]> fb7_dj = cyfxService.getFb7_dj(searchInfo);
        return getDataTable(fb7_dj);
    }

    @PostMapping("/fb7_fz")
    @ResponseBody
    public TableDataInfo getFb7_fz(CyfxSearchModel searchInfo) {
        List<Object[]> fb7_fz = cyfxService.getFb7_fz(searchInfo);
        return getDataTable(fb7_fz);
    }

    @PostMapping("/fb7_zz")
    @ResponseBody
    public TableDataInfo getFb7_zz(CyfxSearchModel searchInfo) {
        List<Object[]> fb7_zz = cyfxService.getFb7_zz(searchInfo);
        return getDataTable(fb7_zz);
    }

    @PostMapping("/fb7_yjh")
    @ResponseBody
    public TableDataInfo getFb7_yjh(CyfxSearchModel searchInfo) {
        List<Object[]> fb7_yjh = cyfxService.getFb7_yjh(searchInfo);
        return getDataTable(fb7_yjh);
    }

    @PostMapping("/fb7_yj")
    @ResponseBody
    public TableDataInfo getFb7_yj(CyfxSearchModel searchInfo) {
        List<Object[]> fb7_yj = cyfxService.getFb7_yj(searchInfo);
        return getDataTable(fb7_yj);
    }

    @PostMapping("/fb7_hz")
    @ResponseBody
    public TableDataInfo getFb7_hz(CyfxSearchModel searchInfo) {
        List<Object[]> fb7_xm = cyfxService.getFb7_xm(searchInfo);
        return getDataTable(fb7_xm);
    }

    @PostMapping("/yssr_dw")
    @ResponseBody
    public TableDataInfo getYssr_dw(CyfxSearchModel searchInfo) {
        List<Object[]> yssr_dw = cyfxService.getYssr_dw(searchInfo);
        return getDataTable(yssr_dw);
    }

    @PostMapping("/yssr_kyz")
    @ResponseBody
    public TableDataInfo getYssr_kyz(CyfxSearchModel searchInfo) {
        List<Object[]> yssr_kyz = cyfxService.getYssr_kyz(searchInfo);
        return getDataTable(yssr_kyz);
    }

    @PostMapping("/yssr_kyzmx")
    @ResponseBody
    public TableDataInfo getYssr_kyzmx(CyfxSearchModel searchInfo) {
        List<Object[]> yssr_kyzmx = cyfxService.getYssr_kyzmx(searchInfo);
        return getDataTable(yssr_kyzmx);
    }

    @PostMapping("/yssr_hz")
    @ResponseBody
    public TableDataInfo getCYFX_ysss(CyfxSearchModel searchInfo) {
        List<Object[]> cyfx_ysss = cyfxService.getCYFX_ysss(searchInfo);
        return getDataTable(cyfx_ysss);
    }

    @PostMapping("/yssr_hyz")
    @ResponseBody
    public TableDataInfo getYssr_hyz(CyfxSearchModel searchInfo) {
        List<Object[]> yssr_hyz = cyfxService.getYssr_hyz(searchInfo);
        return getDataTable(yssr_hyz);
    }

    @PostMapping("/pjsr_cc")
    @ResponseBody
    public TableDataInfo getPjsr_cc(CyfxSearchModel searchInfo) {
        List<Object[]> pjsr_cc = cyfxService.getPjsr_cc(searchInfo);
        return getDataTable(pjsr_cc);
    }
}
