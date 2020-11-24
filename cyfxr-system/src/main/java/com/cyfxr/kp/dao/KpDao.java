package com.cyfxr.kp.dao;

import com.cyfxr.common.domain.ImportLog;

public interface KpDao {

    /**
     * 查询最后入库文件的日期
     */
    public String checkRkwj();

    /**
     * ftp下载客票文件写入日志信息
     * 入库结束更新日志信息
     */
    public void insertLog(ImportLog log);

    /**
     * 当引用该方法会将更新信息写入数据库
     *
     * @param log 日志信息
     */
    public void updateLog(ImportLog log);

    /**
     * 导入客票票根信息
     */
    public void imKp(String yf, String filePath);

    //互联网候购票预付款财收交易明细数据接口文件
    public void impYfpay(String yf, String filePath);

    //互联网候购票预付款收入日结数据接口文件
    public void impYfcs(String yf, String filePath);

    //互联网候购票预付款交易存根数据接口文件
    public void impYf(String yf, String filePath);

    //车站退票纠错明细数据：(暂预留接口)
    public void impTpjc(String yf, String filePath);

    //车站闸机手续费明细数据
    public void impZt1(String yf, String filePath);

    //车站卡务财收四数据
    public void impZt4(String yf, String filePath);

    //车站卡务窗口二号表数据
    public void impZt2(String yf, String filePath);

    //电子客票交易明细数据
    public void impSepay(String yf, String filePath);

    //导入售票数据
    public void impLs(String yf, String filePath);

    //导入退票信息
    public void impLr(String yf, String filePath);

    //导入废票数据
    public void impFp(String yf, String filePath);

    //外站售本站数据
    public void impYs(String yf, String filePath);

    //外站退本站数据
    public void impYr(String yf, String filePath);

    //车站的财收四汇总数据
    public void impKcs4(String yf, String filePath);

    //财收二汇总
    public void impTcs2(String yf, String filePath);

    //售票二号表数据
    public void impSk2(String yf, String filePath);

    //退票二号表数据
    public void impSt2(String yf, String filePath);

    /**
     * 客票数据文件(站补)
     * 客票数据文件(车补)
     */
    public void impCbzb(String yf, String filePath);

    /**
     * 删除表
     *
     * @param yf        表月份
     * @param wjm       表文件名
     * @param tableName 表名
     */
    public void delTable(String yf, String wjm, String tableName);

    /**
     * 引用表赋值方法
     *
     * @param len      表字段长度
     * @param sql      数据库语句
     * @param log      日志信息
     * @param filePath 文件路径
     * @param yf       月份
     */
    public void impTable(int len, String sql, ImportLog log, String filePath, String yf);

    /**
     * 导入站售文件
     * * @param len 表字段长度
     *
     * @param sql      数据库语句
     * @param log      日志信息
     * @param filePath 文件路径
     * @param yf       月份
     */
    public void impTable_LS(int len, String sql, ImportLog log, String filePath, String yf);

    public void impTable_LR(int len, String sql, ImportLog log, String filePath, String yf);

    /**
     * 开办先退票后退款业务后，st2表新增两个字段信息
     */
    public void impTable_St2(int len, String sql, ImportLog log, String filePath, String yf);
}
