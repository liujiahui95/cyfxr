package com.cyfxr.rb.dao;

import com.cyfxr.system.domain.CyfxSearchModel;

import java.util.List;
import java.util.Map;

public interface RbDao {
    /**
     * 查询、展示日报1、日报2、日报3
     */
    public void insertRb1_tem(String nd, String days, String jsrq, String ksrq, String cz, String skzdbm, int flag);

    public void insertRb2_tem(String nd, String days, String jsrq, String ksrq, String cz, String skzdbm, int flag);

    public void insertRb4_tem(String nd, String days, String jsrq, String ksrq);

    //日报1到4
    public List<Map<String, Object>> getRb1(CyfxSearchModel searchInfo);

    public List<Map<String, Object>> getRb2(CyfxSearchModel searchInfo);

    public List<Map<String, Object>> getRb3(CyfxSearchModel searchInfo);

    public List<Map<String, Object>> getRb4(CyfxSearchModel searchInfo);
}
