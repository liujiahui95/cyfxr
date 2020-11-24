package com.cyfxr.basicdata.domain;

import com.cyfxr.common.core.domain.BaseEntity;

public class DIC_CITY extends BaseEntity {
    private String id;
    private String mytexts;
    private String parentid;

    public DIC_CITY() {
    }

    public DIC_CITY(String id, String mytexts, String parentid) {
        this.id = id;
        this.mytexts = mytexts;
        this.parentid = parentid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMytexts() {
        return mytexts;
    }

    public void setMytexts(String mytexts) {
        this.mytexts = mytexts;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }
}
