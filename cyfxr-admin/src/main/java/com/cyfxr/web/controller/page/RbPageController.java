package com.cyfxr.web.controller.page;

import com.cyfxr.common.config.Global;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rbPage")
public class RbPageController {
    @GetMapping("/rb1")
    public String rb1(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "rb/rb1";
    }

    @GetMapping("/rb2")
    public String rb2(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "rb/rb2";
    }

    @GetMapping("/rb3")
    public String rb3(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "rb/rb3";
    }

    @GetMapping("/rb4")
    public String rb4(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "rb/rb4";
    }
}
