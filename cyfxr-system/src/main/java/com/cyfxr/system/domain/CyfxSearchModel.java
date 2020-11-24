package com.cyfxr.system.domain;

import com.cyfxr.common.core.domain.BaseEntity;

public class CyfxSearchModel extends BaseEntity {
    //字段名必须与前端页面的name属性保持一致，区分大小写
    private String KSRQ;
    private String JSRQ;
    private String dw;//单位代码
    private String cz;//车站电报码
    private String yjh;//运价号
    private String jm;//到局
    private String jldw;//计量单位万元、元

    public String getJldw() {
        return jldw;
    }

    public void setJldw(String jldw) {
        this.jldw = jldw;
    }

    public String getKSRQ() {
        return KSRQ;
    }

    public void setKSRQ(String kSRQ) {
        KSRQ = kSRQ;
    }

    public String getJSRQ() {
        return JSRQ;
    }

    public void setJSRQ(String jSRQ) {
        JSRQ = jSRQ;
    }

    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw;
    }

    public String getCz() {
        return cz;
    }

    public void setCz(String cz) {
        this.cz = cz;
    }

    public String getYjh() {
        return yjh;
    }

    public void setYjh(String yjh) {
        this.yjh = yjh;
    }

    public String getJm() {
        return jm;
    }

    public void setJm(String jm) {
        this.jm = jm;
    }
}
