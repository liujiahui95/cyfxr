package com.cyfxr.basicdata.service.impl;

import com.cyfxr.basicdata.dao.BasicDataDao;
import com.cyfxr.basicdata.domain.DIC_ZD;
import com.cyfxr.basicdata.domain.DIC_ZMK;
import com.cyfxr.basicdata.service.BasicDataService;
import com.cyfxr.common.constant.BasicDataConstants;
import com.cyfxr.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasicDataServiceImpl implements BasicDataService {
    @Autowired
    private BasicDataDao basicDataDao;

    @Override
    public List<DIC_ZMK> getZmk(String dbm) {
        return basicDataDao.getZmk(dbm);
    }

    @Override
    public DIC_ZMK getZmkById(String id) {
        return basicDataDao.getZmkById(id);
    }

    @Override
    public int addZm(DIC_ZMK zmk) {
        return basicDataDao.addZM(zmk);
    }

    @Override
    public int updateZm(DIC_ZMK zmk) {
        return basicDataDao.updateZM(zmk);
    }

    @Override
    public int deleteZm(String id) {
        return basicDataDao.deleteZM(id);
    }

    @Override
    public List<DIC_ZD> getZdInfo(String dbm) {
        return basicDataDao.getZdInfo(dbm);
    }

    @Override
    public DIC_ZD getZdByDbm(String dbm) {
        return basicDataDao.getZdByDbm(dbm);
    }

    @Override
    public int addZdInfo(DIC_ZD zd) {
        return basicDataDao.addZdInfo(zd);
    }

    @Override
    public int updateZdInfo(DIC_ZD zd) {
        return basicDataDao.updateZdInfo(zd);
    }

    @Override
    public int deleteZdInfo(String id) {
        return deleteZdInfo(id);
    }

    @Override
    public List<DIC_ZMK> getZMZD(String lx, String dm) {
        return basicDataDao.getZMZD(lx, dm);
    }

    @Override
    public List<DIC_ZMK> getCZ(String lx, String dm) {
        return basicDataDao.getCZ(lx, dm);
    }

    @Override
    public String checkZMUnique(String zm) {
        DIC_ZMK zmk = basicDataDao.checkZMUnique(zm);
        if (StringUtils.isNotNull(zmk)) {
            //站名已存在
            return BasicDataConstants.ZM_NOT_UNIQUE;
        }
        //站名不存在
        return BasicDataConstants.ZM_UNIQUE;
    }

    @Override
    public String checkDBMUnique(String dbm) {
        DIC_ZMK zmk = basicDataDao.checkDBMUnique(dbm);
        if (StringUtils.isNotNull(zmk)) {
            //电报码已存在
            return BasicDataConstants.DBM_NOT_UNIQUE;
        }
        //电报码不存在
        return BasicDataConstants.DBM_UNIQUE;
    }
}
