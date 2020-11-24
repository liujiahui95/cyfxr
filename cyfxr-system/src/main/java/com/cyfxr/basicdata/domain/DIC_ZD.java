package com.cyfxr.basicdata.domain;

import com.cyfxr.common.core.domain.BaseEntity;

/**
 * 站段维护
 */
public class DIC_ZD extends BaseEntity {
    private int id;
    private String dbm;
    private String mc;
    private String fwdbm;//范围电报字段，站段所包含电报码字段
    private String button;//添加按钮使用
    private String dbm_old;//作为更新站名、电报码的条件

    public DIC_ZD(int id, String dbm, String mc, String fwdbm, String button, String dbm_old) {
        this.id = id;
        this.dbm = dbm;
        this.mc = mc;
        this.fwdbm = fwdbm;
        this.button = button;
        this.dbm_old = dbm_old;
    }

    public DIC_ZD() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDbm() {
        return dbm;
    }

    public void setDbm(String dbm) {
        this.dbm = dbm;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getFwdbm() {
        return fwdbm;
    }

    public void setFwdbm(String fwdbm) {
        this.fwdbm = fwdbm;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public String getDbm_old() {
        return dbm_old;
    }

    public void setDbm_old(String dbm_old) {
        this.dbm_old = dbm_old;
    }
}
