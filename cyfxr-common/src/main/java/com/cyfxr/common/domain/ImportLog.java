package com.cyfxr.common.domain;


public class ImportLog {
    private int xh;//序号
    private String yf;//月份
    private String wjm;//文件名
    private String wjlj;//文件来源
    private String wjxgsj;//上传时间
    private String ftpxzsj;//FTP下载时间
    private String rksj;//入库时间
    private int jlzs;//文件内记录总数
    private String rkjg;//入库结果 0正常 1错误
    private String cwxx;//错误信息


    public ImportLog() {
    }


    public int getXh() {
        return xh;
    }


    public void setXh(int xh) {
        this.xh = xh;
    }


    public String getWjm() {
        return wjm;
    }


    public void setWjm(String wjm) {
        this.wjm = wjm;
    }


    public String getWjlj() {
        return wjlj;
    }


    public void setWjlj(String wjlj) {
        this.wjlj = wjlj;
    }


    public int getJlzs() {
        return jlzs;
    }


    public void setJlzs(int jlzs) {
        this.jlzs = jlzs;
    }


    public String getRkjg() {
        return rkjg;
    }


    public void setRkjg(String rkjg) {
        this.rkjg = rkjg;
    }


    public String getCwxx() {
        return cwxx;
    }


    public void setCwxx(String cwxx) {
        this.cwxx = cwxx;
    }

    public String getYf() {
        return yf;
    }

    public void setYf(String yf) {
        this.yf = yf;
    }

    public String getWjxgsj() {
        return wjxgsj;
    }

    public void setWjxgsj(String wjxgsj) {
        this.wjxgsj = wjxgsj;
    }

    public String getFtpxzsj() {
        return ftpxzsj;
    }

    public void setFtpxzsj(String ftpxzsj) {
        this.ftpxzsj = ftpxzsj;
    }

    public String getRksj() {
        return rksj;
    }

    public void setRksj(String rksj) {
        this.rksj = rksj;
    }

}
