package com.cyfxr.cyfx.service.impl;

import com.cyfxr.common.utils.StringUtils;
import com.cyfxr.cyfx.dao.CyfxDao;
import com.cyfxr.cyfx.service.CyfxService;
import com.cyfxr.system.domain.CyfxSearchModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CyfxServiceImpl implements CyfxService {
    @Autowired
    private CyfxDao cyfxDao;

    @Override
    public List<Object[]> getFb1(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb1 = cyfxDao.getFb1(searchInfo);
        List<Object[]> list = StringUtils.getList(fb1);
        return list;
    }

    @Override
    public List<Object[]> getFb2_pl(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getFb2_pl(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        return list;
    }

    @Override
    public List<Object[]> getFb2_cx(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getFb2_cx(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        return list;
    }

    @Override
    public List<Object[]> getFb2_zz(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getFb2_zz(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        return list;
    }

    @Override
    public List<Object[]> getFb2_yj(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getFb2_yj(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        return list;
    }

    @Override
    public List<Object[]> getFb2_yjh(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getFb2_yjh(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        return list;
    }

    @Override
    public List<Object[]> getFb2_ysfs(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getFb2_ysfs(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        return list;
    }

    @Override
    public List<Object[]> getFb3_yjh(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getFb3_yjh(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        return list;
    }

    @Override
    public List<Object[]> getFb3_pl(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getFb3_pl(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        return list;
    }

    @Override
    public List<Object[]> getFb3_dj(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getFb3_dj(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        return list;
    }

    @Override
    public List<Object[]> getFb3_zz(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getFb3_zz(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        return list;
    }

    @Override
    public List<Object[]> getFb3_yj(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getFb3_yj(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        return list;
    }

    @Override
    public List<Object[]> getFb3_fz(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getFb3_fz(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        int i = 1;
        for (Object[] objects : list) {
            objects[0] = i;
            i++;
        }
        return list;
    }

    @Override
    public List<Object[]> getFb7_pl(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getFb7_pl(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        return list;
    }

    @Override
    public List<Object[]> getFb7_dj(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getFb7_dj(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        return list;
    }

    @Override
    public List<Object[]> getFb7_fz(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getFb7_fz(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        int i = 1;
        for (Object[] objects : list) {
            objects[0] = i;
            i++;
        }
        return list;
    }

    @Override
    public List<Object[]> getFb7_zz(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getFb7_zz(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        return list;
    }

    @Override
    public List<Object[]> getFb7_yjh(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getFb7_yjh(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        return list;
    }

    @Override
    public List<Object[]> getFb7_yj(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getFb7_yj(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        return list;
    }

    @Override
    public List<Object[]> getFb7_xm(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getFb7_xm(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        return list;
    }

    @Override
    public List<Object[]> getYssr_dw(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getYssr_dw(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        return list;
    }

    @Override
    public List<Object[]> getYssr_kyz(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getYssr_kyz(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        int i = 1;
        for (Object[] objects : list) {
            objects[0] = i;
            i++;
        }
        return list;
    }

    @Override
    public List<Object[]> getYssr_kyzmx(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getYssr_kyzmx(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        int i = 1;
        for (Object[] objects : list) {
            objects[0] = i;
            i++;
        }
        return list;
    }

    @Override
    public List<Object[]> getCYFX_ysss(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getCYFX_ysss(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        return list;
    }

    @Override
    public List<Object[]> getYssr_hyz(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getYssr_hyz(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        int i = 1;
        for (Object[] objects : list) {
            objects[0] = i;
            i++;
        }
        return list;
    }

    @Override
    public List<Object[]> getPjsr_cc(CyfxSearchModel searchInfo) {
        List<Map<String, Object>> fb = cyfxDao.getPjsr_cc(searchInfo);
        List<Object[]> list = StringUtils.getList(fb);
        int i = 1;
        for (Object[] objects : list) {
            objects[0] = i;
            i++;
        }
        return list;
    }
}
