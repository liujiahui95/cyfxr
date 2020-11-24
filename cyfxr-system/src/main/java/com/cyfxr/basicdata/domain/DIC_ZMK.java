package com.cyfxr.basicdata.domain;

import com.cyfxr.common.core.domain.BaseEntity;

public class DIC_ZMK extends BaseEntity {
    private int id;
    private String dbm;
    private String mc;
    private String button;//添加按钮使用
    private String dbm_old;//作为更新站名、电报码的条件

    public DIC_ZMK() {
    }

    public DIC_ZMK(int id, String dbm, String mc, String button, String dbm_old) {
        this.id = id;
        this.dbm = dbm;
        this.mc = mc;
        this.button = button;
        this.dbm_old = dbm_old;
    }


    public String getDbm_old() {
        return dbm_old;
    }

    public void setDbm_old(String dbm_old) {
        this.dbm_old = dbm_old;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
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
}
