package com.cyfxr.basicdata.dao;

import com.cyfxr.basicdata.domain.DIC_ZD;
import com.cyfxr.basicdata.domain.DIC_ZMK;

import java.util.List;

public interface BasicDataDao {
    /**
     * 获取站名库信息
     */
    public List<DIC_ZMK> getZmk(String dbm);

    public DIC_ZMK getZmkById(String id);

    /**
     * 新增站名
     */
    public int addZM(DIC_ZMK zmk);

    /**
     * 更新站名、电报码
     */
    public int updateZM(DIC_ZMK zmk);

    /**
     * 删除站名、电报码
     */
    public int deleteZM(String id);


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

    /**
     * 根据SRFX_ZMK的范围电报码获取乌局各单位管理站
     */
    public List<DIC_ZMK> getCZ(String lx, String dm);

    /**
     * 检查站名、电报码是否唯一
     *
     * @return
     */
    public DIC_ZMK checkZMUnique(String zm);

    public DIC_ZMK checkDBMUnique(String dbm);

}
