package com.cyfxr.web.controller.rb;

import com.cyfxr.common.core.controller.BaseController;
import com.cyfxr.common.core.page.TableDataInfo;
import com.cyfxr.system.domain.CyfxSearchModel;
import com.cyfxr.rb.service.Rbservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/rb")
public class RbController extends BaseController {
    @Autowired
    private Rbservice rbservice;

    @PostMapping("/rb1")
    @ResponseBody
    public TableDataInfo getRb1(CyfxSearchModel searchInfo) {
        List<Object[]> list = rbservice.getRb1(searchInfo);
        return getDataTable(list);
    }

    @PostMapping("/rb2")
    @ResponseBody
    public TableDataInfo getRb2(CyfxSearchModel searchInfo) {
        List<Object[]> list = rbservice.getRb2(searchInfo);
        return getDataTable(list);
    }

    @PostMapping("/rb3")
    @ResponseBody
    public TableDataInfo getRb3(CyfxSearchModel searchInfo) {
        List<Object[]> list = rbservice.getRb3(searchInfo);
        return getDataTable(list);
    }

    @PostMapping("/rb4")
    @ResponseBody
    public TableDataInfo getRb4(CyfxSearchModel searchInfo) {
        List<Object[]> list = rbservice.getRb4(searchInfo);
        return getDataTable(list);
    }
}
