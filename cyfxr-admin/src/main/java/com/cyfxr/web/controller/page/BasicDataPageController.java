package com.cyfxr.web.controller.page;

import com.cyfxr.common.config.Global;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 基础数据维护页面
 */
@Controller
@RequestMapping("/basicdatapage")
public class BasicDataPageController {
    private String prefix = "basicdata";

    @GetMapping("/zmdata")
    public String ZMData(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return prefix + "/zmk/zmk";
    }

    @GetMapping("/addzm")
    public String addZM(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return prefix + "/zmk/add";
    }


    @GetMapping("/zddata")
    public String ZDData(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return prefix + "/zdwh/zdwh";
    }

    @GetMapping("/addzd")
    public String addZD(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return prefix + "/zdwh/add";
    }
}
