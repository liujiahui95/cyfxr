package com.cyfxr.rb.service.impl;

import com.cyfxr.common.utils.StringUtils;
import com.cyfxr.rb.dao.RbDao;
import com.cyfxr.system.domain.CyfxSearchModel;
import com.cyfxr.rb.service.Rbservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RbServiceImpl implements Rbservice {
    @Autowired
    private RbDao rbDao;


    @Override
    public List<Object[]> getRb1(CyfxSearchModel searchModel) {
        List<Map<String, Object>> rb1 = rbDao.getRb1(searchModel);
        List<Object[]> list = StringUtils.getList(rb1);
        return list;
    }

    @Override
    public List<Object[]> getRb2(CyfxSearchModel searchModel) {
        List<Map<String, Object>> rb2 = rbDao.getRb2(searchModel);
        List<Object[]> list = StringUtils.getList(rb2);
        return list;
    }

    @Override
    public List<Object[]> getRb3(CyfxSearchModel searchModel) {
        List<Map<String, Object>> rb3 = rbDao.getRb3(searchModel);
        List<Object[]> list = StringUtils.getList(rb3);
        return list;
    }

    @Override
    public List<Object[]> getRb4(CyfxSearchModel searchModel) {
        List<Map<String, Object>> rb4 = rbDao.getRb4(searchModel);
        List<Object[]> list = StringUtils.getList(rb4);
        return list;
    }


}
