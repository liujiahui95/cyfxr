package com.cyfxr.web.controller.excel;

import com.cyfxr.common.utils.poi.ExcelUtils;
import com.cyfxr.cyfx.service.CyfxService;
import com.cyfxr.system.domain.CyfxSearchModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/cyfxExcel")
public class CyfxExcelController {

    @Autowired
    private CyfxService cyfxService;

    @RequestMapping(value = "/fb1", produces = "application/json; charset=utf-8")
    @ResponseBody
    public void exportRB1_Exc(HttpServletRequest request, HttpServletResponse response,
                              CyfxSearchModel searchInfo) throws IOException {
        ClassPathResource cpr = new ClassPathResource("/templates/Excel/" + "运输收入营业收入辅助分析系统附表1.xlsx");
        String tempPath = cpr.getFile().getPath();
        String path = new ClassPathResource("/templates/Excel/").getFile().getPath();
        System.out.println(path);
        List<Object[]> bs = cyfxService.getFb1(searchInfo);
        int rownum = 4;//数据从第几行开始写入模板
        int colnum = 3;
        String filename = "";
        String strs = getStrs(searchInfo);
        ExcelUtils ex = new ExcelUtils();
        ex.exportExcel(tempPath, path, bs, rownum, colnum, filename, strs, response);
    }

    //判断条件是否存在
    public String getStrs(CyfxSearchModel searchInfo) {
        String strs = searchInfo.getKSRQ() + "-" + searchInfo.getJSRQ();
        if (searchInfo.getDw() != null && !searchInfo.getDw().equals("") && !searchInfo.getDw().equals("0")) {
            strs += " 单位：" + searchInfo.getDw();
        }
        if (searchInfo.getCz() != null && !searchInfo.getCz().equals("") && !searchInfo.getCz().equals("0")) {
            strs += " 车站：" + searchInfo.getCz();
        }
        if (searchInfo.getYjh() != null && !searchInfo.getYjh().equals("") && !searchInfo.getYjh().equals("0")) {
            strs += " 运价号：" + searchInfo.getYjh();
        }
        if (searchInfo.getJm() != null && !searchInfo.getJm().equals("") && !searchInfo.getJm().equals("0")) {
            strs += " 局码：" + searchInfo.getYjh();
        }

        return strs;

    }
}
