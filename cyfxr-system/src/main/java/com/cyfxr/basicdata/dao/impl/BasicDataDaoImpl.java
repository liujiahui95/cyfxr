package com.cyfxr.basicdata.dao.impl;

import com.cyfxr.basicdata.dao.BasicDataDao;
import com.cyfxr.basicdata.domain.DIC_ZD;
import com.cyfxr.basicdata.domain.DIC_ZMK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("basicDataDao")
public class BasicDataDaoImpl implements BasicDataDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<DIC_ZMK> getZmk(String dbm) {
        String sql = "select id,dbm,mc from SRFX_ZMK ";
        if (dbm != null && !dbm.trim().equals("")) {
            sql = sql + " and dbm='" + dbm + "'";
        }
        sql = sql + " order by id";
        List<DIC_ZMK> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(DIC_ZMK.class));
        return list;
    }

    @Override
    public DIC_ZMK getZmkById(String id) {
        String sql = "select id,dbm,mc from srfx_zmk where id = ? ";
        DIC_ZMK zmk = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(DIC_ZMK.class), id);
        return zmk;
    }

    @Override
    public int addZM(DIC_ZMK zmk) {
        String sql = "select  max(id) from SRFX_ZMK t";
        int maxid = jdbcTemplate.queryForObject(sql, Integer.class);
        maxid = maxid + 1;
        sql = "insert into SRFX_ZMK(id,dbm,mc) values(?,?,?)";
        int count = jdbcTemplate.update(sql, maxid, zmk.getDbm(), zmk.getMc());
        return count;
    }

    @Override
    public int updateZM(DIC_ZMK zmk) {
        String sql = "update SRFX_ZMK t set t.dbm=?,t.mc=? where id=?";
        int count = jdbcTemplate.update(sql, zmk.getDbm(), zmk.getMc(), zmk.getId());
        return count;
    }

    @Override
    public int deleteZM(String id) {
        String sql = "delete from SRFX_ZMK where id=?";
        int count = jdbcTemplate.update(sql, id);
        return count;
    }

    @Override
    public List<DIC_ZD> getZdInfo(String dbm) {
        String sql = "select id,dbm,mc,fwdbm from SRFX_ZMK where cztype = 0";
        if (dbm != null && !dbm.trim().equals("")) {
            sql = sql + " and dbm='" + dbm + "'";
        }
        sql = sql + " order by dbm";
        List<DIC_ZD> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(DIC_ZD.class));
        return list;
    }

    @Override
    public DIC_ZD getZdByDbm(String dbm) {
        String sql = "select id,dbm,mc,fwdbm from srfx_zmk where dbm = ? ";
        DIC_ZD zd = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(DIC_ZD.class), dbm);
        return zd;
    }

    @Override
    public int addZdInfo(DIC_ZD zd) {
        String sql = "select  max(id) from SRFX_ZMK t";
        int maxid = jdbcTemplate.queryForObject(sql, Integer.class);
        sql = "insert into SRFX_ZMK(id,dbm,mc,fwdbm,cztype) values(?,?,?,?,?) ";
        int count = jdbcTemplate.update(sql, maxid, zd.getDbm(), zd.getMc(), zd.getFwdbm(), 0);
        return count;
    }

    @Override
    public int updateZdInfo(DIC_ZD zd) {
        String sql = "update SRFX_ZMK t set t.dbm=?,t.mc=?,t.fwdbm=? where id=?";
        int count = jdbcTemplate.update(sql, zd.getDbm(), zd.getMc(), zd.getFwdbm(), zd.getId());
        return count;
    }

    @Override
    public int deleteZdInfo(String id) {
        String sql = "delete from  SRFX_ZMK where dbm=?";
        int count = jdbcTemplate.update(sql, id);
        return count;
    }

    @Override
    public List<DIC_ZMK> getZMZD(String lx, String dm) {
        String sql = "";
        if (lx == null || "".equals(lx)) {
            lx = "0";
        }
        //0集团所有站 1货运中心的站 2 公司的站 3全路的站
        if (lx.equals("0")) {
            sql = "select cs4dbm as dbm,mc from srfx_zmk where cztype=1  order by dbm";
        }
        if (lx.equals("1")) {
            if (dm == null || dm.equals("")) {
                dm = "0";
            }
            sql = "select cs4dbm as dbm,mc from srfx_zmk t  "
                    + " where instr((select fwdbm from srfx_zmk where flag = " + dm + "),t.dbm) > 0 "
                    + "order by dbm";
        }
        if (lx.equals("2")) {
            if (dm == null || dm.equals("")) {
                dm = "0";
            }
            sql = "select dbm,zm as mc from zd_站名 t " +
                    " where qydm = '" + dm + "' order by dbm";
        }
        List<DIC_ZMK> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(DIC_ZMK.class));
        return list;
    }

    @Override
    public List<DIC_ZMK> getCZ(String lx, String dm) {
        String sql = "";
        //0：车务段、 3：公司、 4：货运中心
        if (lx == null || lx.equals("")) {
            lx = "0";
        }
        if (lx.equals("0")) {
            sql = "select cs4dbm as dbm,mc from srfx_zmk t  "
                    + " where instr((select fwdbm from srfx_zmk where id = " + dm + "),t.cs4dbm) > 0 "
                    + "order by mc";
        }
        if (lx.equals("2")) {
            sql = "select cs4dbm as dbm,mc from srfx_zmk t  "
                    + " where instr((select fwdbm from srfx_zmk where rxybz='3' and ssqydm = '" + dm + "'),t.cs4dbm) > 0 "
                    + "order by dbm";
        }
        List<DIC_ZMK> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(DIC_ZMK.class));
        return list;
    }

    @Override
    public DIC_ZMK checkZMUnique(String zm) {
        String sql = "select  * from srfx_zmk where  mc = ? ";
        DIC_ZMK zmk = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(DIC_ZMK.class), zm);
        return zmk;
    }

    @Override
    public DIC_ZMK checkDBMUnique(String dbm) {
        String sql = "select  * from srfx_zmk where  dbm = ? ";
        DIC_ZMK zmk = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(DIC_ZMK.class), dbm);
        return zmk;
    }
}
