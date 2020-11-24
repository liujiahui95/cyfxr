package com.cyfxr.web.controller.page;

import com.cyfxr.common.config.Global;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cyfxPage")
public class CyfxPageController {
    /*
    承运收益汇总
     */
    @GetMapping("/fb1")
    public String getFb1(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/fb1";
    }

    @GetMapping("/fb2_pl")
    public String getFb2_pl(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/fb2_pl";
    }

    @GetMapping("/fb2_cx")
    public String getFb2_cx(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/fb2_cx";
    }

    @GetMapping("/fb2_zz")
    public String getFb2_zz(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/fb2_zz";
    }

    @GetMapping("/fb2_yj")
    public String getFb2_yj(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/fb2_yj";
    }

    @GetMapping("/fb2_yjh")
    public String getFb2_yjh(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/fb2_yjh";
    }

    @GetMapping("/fb2_ysfs")
    public String getFb2_ysfs(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/fb2_ysfs";
    }

    /*
    承运收益明细
     */
    @GetMapping("/fb3_pl")
    public String getFb3_pl(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/fb3_pl";
    }

    @GetMapping("/fb3_dj")
    public String getFb3_dj(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/fb3_dj";
    }

    @GetMapping("/fb3_fz")
    public String getFb3_fz(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/fb3_fz";
    }

    @GetMapping("/fb3_yj")
    public String getFb3_yj(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/fb3_yj";
    }

    @GetMapping("/fb3_yjh")
    public String getFb3_yjh(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/fb3_yjh";
    }

    @GetMapping("/fb3_zz")
    public String getFb3_zz(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/fb3_zz";
    }

    /*
    承运清算台账表
     */
    @GetMapping("/fb7_dj")
    public String getFb7_dj(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/fb7_dj";
    }

    @GetMapping("/fb7_pl")
    public String getFb7_pl(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/fb7_pl";
    }

    @GetMapping("/fb7_zz")
    public String getFb7_zz(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/fb7_zz";
    }

    @GetMapping("/fb7_fz")
    public String getFb7_fz(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/fb7_fz";
    }

    @GetMapping("/fb7_yj")
    public String getFb7_yj(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/fb7_yj";
    }

    @GetMapping("/fb7_yjh")
    public String getFb7_yjh(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/fb7_yjh";
    }

    @GetMapping("/fb7_hz")
    public String getFb7_hz(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/fb7_hz";
    }

    /*
    运输收入
     */
    @GetMapping("/yssr_hz")
    public String getYssr_hz(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/yssr_hz";
    }

    @GetMapping("/yssr_dw")
    public String getYssr_dw(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/yssr_dw";
    }

    @GetMapping("/yssr_hyz")
    public String getYssr_hyz(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/yssr_hyz";
    }

    @GetMapping("/yssr_kyz")
    public String getYssr_kyz(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/yssr_kyz";
    }

    @GetMapping("/yssr_kyzmx")
    public String getYssr_kyzmx(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/yssr_kyzmx";
    }

    @GetMapping("/yssr_lkpj")
    public String getYssr_lkpj(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "cyfx/yssr_lkpj";
    }

}
