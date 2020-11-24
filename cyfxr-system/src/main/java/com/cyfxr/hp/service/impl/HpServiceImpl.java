package com.cyfxr.hp.service.impl;

import com.cyfxr.common.utils.StringUtils;
import com.cyfxr.hp.dao.HpDao;
import com.cyfxr.hp.service.HpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HpServiceImpl implements HpService {
    @Autowired
    private HpDao hpDao;

    @Override
    public List<Object[]> getCYFXBB_SY_Line() {
        List<Map<String, Object>> line = hpDao.getCYFXBB_SY_Line();
        List<Object[]> list = StringUtils.getList(line);
        return list;
    }

    @Override
    public List<Object[]> getCYFXBB_SY_Bar() {
        List<Map<String, Object>> bar = hpDao.getCYFXBB_SY_Bar();
        List<Object[]> list = StringUtils.getList(bar);
        return list;
    }

    @Override
    public List<Object[]> getCYFXBB_SY_Scroll() {
        List<Map<String, Object>> scroll = hpDao.getCYFXBB_SY_Scroll();
        List<Object[]> list = StringUtils.getList(scroll);
        return list;
    }
}
