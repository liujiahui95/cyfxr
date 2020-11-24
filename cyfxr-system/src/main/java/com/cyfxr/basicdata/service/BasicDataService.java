package com.cyfxr.basicdata.service;

import com.cyfxr.basicdata.domain.DIC_ZD;
import com.cyfxr.basicdata.domain.DIC_ZMK;

import java.util.List;

public interface BasicDataService {
    public List<DIC_ZMK> getZmk(String dbm);

    public DIC_ZMK getZmkById(String id);

    public int addZm(DIC_ZMK zmk);

    public int updateZm(DIC_ZMK zmk);

    public int deleteZm(String id);


    /**
     * 站段信息增删改查
     */
    public List<DIC_ZD> getZdInfo(String dbm);

    public DIC_ZD getZdByDbm(String dbm);

    public int addZdInfo(DIC_ZD zd);

    public int updateZdInfo(DIC_ZD zd);

    public int deleteZdInfo(String id);

    /**
     * 分类型车站字典信息加载
     * 0集团所有站 1货运中心的站 2 公司的站 3全路的站
     */
    public List<DIC_ZMK> getZMZD(String lx, String dm);

    public List<DIC_ZMK> getCZ(String lx, String dm);

    /**
     * 检查站名、电报码是否唯一
     *
     * @return
     */
    public String checkZMUnique(String zm);

    public String checkDBMUnique(String dbm);
}
