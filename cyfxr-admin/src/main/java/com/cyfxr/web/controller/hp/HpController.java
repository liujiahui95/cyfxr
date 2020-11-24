package com.cyfxr.web.controller.hp;

import com.alibaba.fastjson.JSON;
import com.cyfxr.common.utils.FTP.FtpUtils;
import com.cyfxr.hp.service.HpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hp")
public class HpController {
    @Autowired
    private HpService hpService;

    @Autowired
    private FtpUtils ftpUtils;

    @PostMapping("/sy")
    @ResponseBody
    public String findSY() {
        List<Object[]> line = hpService.getCYFXBB_SY_Line();
        List<Object[]> bar = hpService.getCYFXBB_SY_Bar();
        List<Object[]> src = hpService.getCYFXBB_SY_Scroll();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("line", line);
        map.put("bar", bar);
        map.put("src", src);
        String str = JSON.toJSONString(map);
        return str;
    }

}
