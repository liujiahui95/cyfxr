package com.cyfxr.web.controller.basicdata;

import com.cyfxr.basicdata.domain.DIC_ZD;
import com.cyfxr.basicdata.domain.DIC_ZMK;
import com.cyfxr.basicdata.service.BasicDataService;
import com.cyfxr.common.constant.BasicDataConstants;

import com.cyfxr.common.core.controller.BaseController;
import com.cyfxr.common.core.domain.AjaxResult;
import com.cyfxr.common.core.page.TableDataInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/basicdata")
public class BasicDataController extends BaseController {
    @Autowired
    private BasicDataService basicDataService;

    @PostMapping("/zmk")
    @ResponseBody
    public TableDataInfo getZMK(DIC_ZMK zmk) {
        List<DIC_ZMK> list = basicDataService.getZmk(zmk.getDbm());
        return getDataTable(list);
    }

    @PostMapping("/zd")
    @ResponseBody
    public TableDataInfo getZD(String dbm) {
        List<DIC_ZD> zd = basicDataService.getZdInfo(dbm);
        return getDataTable(zd);
    }


    @PostMapping("/checkZMUnique")
    @ResponseBody
    public String checkZMUnique(String zm) {
        return basicDataService.checkZMUnique(zm);
    }

    @PostMapping("/checkDBMUnique")
    @ResponseBody
    public String checkDBMUnique(String dbm) {
        return basicDataService.checkDBMUnique(dbm);
    }

    @PostMapping("/addzmk")
    @ResponseBody
    public AjaxResult addZm(DIC_ZMK zmk) {
        String zm = zmk.getMc();
        String dbm = zmk.getDbm();
        if (BasicDataConstants.ZM_NOT_UNIQUE.equals(basicDataService.checkZMUnique(zm))) {
            return error("新增站名'" + zm + "'失败，站名已存在");
        } else if (BasicDataConstants.DBM_NOT_UNIQUE.equals(basicDataService.checkDBMUnique(dbm))) {
            return error("新增电报码 '" + dbm + "'失败，电报码已存在");
        }
        return toAjax(basicDataService.addZm(zmk));
    }

    @PostMapping("/deletezmk")
    @ResponseBody
    public AjaxResult deleteZMK(String ids) {
        try {
            return toAjax(basicDataService.deleteZm(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @GetMapping("/editzmk/{id}")
    public String editZMKPage(@PathVariable("id") String id, ModelMap mmap) {
        mmap.put("ZMK", basicDataService.getZmkById(id));
        return "basicdata/zmk/edit";
    }

    @PostMapping("/editzmk")
    @ResponseBody
    public AjaxResult editZMKSave(DIC_ZMK zmk) {
        return toAjax(basicDataService.updateZm(zmk));
    }

    @PostMapping("/addzd")
    @ResponseBody
    public AjaxResult addZD(DIC_ZD zd) {
        return toAjax(basicDataService.addZdInfo(zd));
    }

    @PostMapping("/deletezd")
    @ResponseBody
    public AjaxResult deleteZD(String ids) {
        try {
            return toAjax(basicDataService.deleteZdInfo(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @GetMapping("/editzd/{id}")
    public String editZDPage(@PathVariable("id") String dbm, ModelMap mmap) {
        mmap.put("ZD", basicDataService.getZdByDbm(dbm));
        return "basicdata/zdwh/edit";
    }

    @PostMapping("/editzd")
    @ResponseBody
    public AjaxResult editZDSave(DIC_ZD zd) {
        return toAjax(basicDataService.updateZdInfo(zd));
    }

    @PostMapping("/getCZ")
    @ResponseBody
    public List<DIC_ZMK> getCz(String lx, String dm) {
        List<DIC_ZMK> list = basicDataService.getCZ(lx, dm);
        return list;
    }
}
