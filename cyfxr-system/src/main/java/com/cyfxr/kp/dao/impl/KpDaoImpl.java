package com.cyfxr.kp.dao.impl;

import com.cyfxr.common.utils.GlobalMethod;
import com.cyfxr.kp.dao.KpDao;
import com.cyfxr.common.domain.ImportLog;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.cyfxr.common.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

@Component("kpDao")
public class KpDaoImpl implements KpDao {
    private static final Logger logger = LoggerFactory.getLogger(KpDaoImpl.class);


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String checkRkwj() {
        String sql = "select max(substr(yf,1,4)||substr(wjm,3,4)) as rq from import_log t\r\n" +
                "where wjm like 'LS%00R'\r\n";
        String date = jdbcTemplate.queryForObject(sql, String.class);
        return date;
    }

    /*
     * importlog_sq.nextval处理主键
     */
    @Override
    public void insertLog(ImportLog log) {
        String sql = "insert into IMPORT_LOG(xh,yf,wjm,wjlj,wjxgsj,ftpxzsj)"
                + "values(importlog_sq.nextval,?,?,?,"
                + "to_date(?,'yyyymmddhh24miss'),"
                + "to_date(?,'yyyymmddhh24miss'))";
        jdbcTemplate.update(sql, log.getYf(), log.getWjm(), log.getWjlj(), log.getWjxgsj(), log.getFtpxzsj());
    }

    @Override
    public void updateLog(ImportLog log) {
        String sql = "update IMPORT_LOG set (rksj,jlzs,rkjg,cwxx)="
                + " (select sysdate,?,?,? from dual)"
                + " where yf=? and wjm=?"
                + " and ftpxzsj = to_date(?,'yyyymmddhh24miss')";
        //FtpUtil.ftpmap.get(log.getWjm())
        jdbcTemplate.update(sql, log.getJlzs(), log.getRkjg(), log.getCwxx(), log.getYf(), log.getWjm(), DateUtil.getSystemDate2() + "000000");
    }

    /*
     * 按天入库文件
     */
    @Override
    public void imKp(String date, String filePath) {
        long a = System.currentTimeMillis();
        List<String> fs = new ArrayList<String>();
        GlobalMethod.getFiles(filePath, fs);
        List<ListenableFuture<Integer>> futures = Lists.newArrayList();
        ExecutorService pool = Executors.newFixedThreadPool(20);// 线程数
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(pool);
        for (int i = 0; i < fs.size(); i++) {
            futures.add(executorService.submit(new WriteTask(fs.get(i), date)));
        }
        final ListenableFuture<List<Integer>> resultsFuture = Futures.successfulAsList(futures);
        try {
            resultsFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
        logger.info(DateUtil.getSystemDate2() + "共计" + fs.size() + "个文件，入库完成, 耗时：" + (System.currentTimeMillis() - a) / 1000 + "s");
    }

    class WriteTask implements Callable<Integer> {
        String fileAbsolutePath;
        String date;

        public WriteTask(String fileAbsolutePath, String date) {
            this.fileAbsolutePath = fileAbsolutePath;
            this.date = date;
        }

        public Integer call() throws Exception {
            int m = fileAbsolutePath.lastIndexOf("\\");
            String fileName = fileAbsolutePath.substring(m + 1);
            if (fileName.toUpperCase().startsWith("YFPAY")) {
                impYfpay(date, fileAbsolutePath);
            } else if (fileName.toUpperCase().startsWith("YFCS")) {
                impYfcs(date, fileAbsolutePath);
            } else if (fileName.toUpperCase().startsWith("YF") && fileName.length() == 10) {
                impYf(date, fileAbsolutePath);
            } else if (fileName.toUpperCase().startsWith("TPJC")) {
                impTpjc(date, fileAbsolutePath);
            } else if (fileName.toUpperCase().startsWith("ZT1")) {
                impZt1(date, fileAbsolutePath);
            } else if (fileName.toUpperCase().startsWith("ZT4")) {
                impZt4(date, fileAbsolutePath);
            } else if (fileName.toUpperCase().startsWith("ZT2")) {
                impZt2(date, fileAbsolutePath);
            } else if (fileName.toUpperCase().startsWith("SEPAY")) {
                impSepay(date, fileAbsolutePath);
            } else if (fileName.toUpperCase().startsWith("ST2")) {
                impSt2(date, fileAbsolutePath);
            } else if (fileName.toUpperCase().startsWith("SK2")) {
                //impSk2(date,fileAbsolutePath);//电子票号断号，字段长度太大，暂时不提2020年5月30日16:10:14

            } else if (fileName.toUpperCase().startsWith("TCS2")) {
                impTcs2(date, fileAbsolutePath);
            } else if (fileName.toUpperCase().startsWith("KCS4")) {
                impKcs4(date, fileAbsolutePath);
            } else if (fileName.toUpperCase().startsWith("YR")) {
                impYr(date, fileAbsolutePath);
            } else if (fileName.toUpperCase().startsWith("YS")) {
                impYs(date, fileAbsolutePath);
            } else if (fileName.toUpperCase().startsWith("FP")) {
                impFp(date, fileAbsolutePath);
            } else if (fileName.toUpperCase().startsWith("LR")) {
                impLr(date, fileAbsolutePath);
            } else if (fileName.toUpperCase().startsWith("LS")) {
                impLs(date, fileAbsolutePath);
            } else if (fileName.toUpperCase().startsWith("CBKP") || fileName.toUpperCase().startsWith("ZBKP")) {
                impCbzb(date, fileAbsolutePath);
            }
            return 1;
        }
    }

    /*
     * 导入客票数据
     * 售票数据网售和站售文件不一致，站售75个字段，网售74个字段
     */
    @Override
    public void impYfpay(String date, String filePath) {
        long a = System.currentTimeMillis();
        File file = new File(filePath);
        String filename = file.getName();
        ImportLog log = new ImportLog();
        //执行删除操作
        delTable(date, filename, "KYPG_YFCS");
        String sql = "insert into KYPG_YFPAY(yf,WJM,SPBZ,wjrq,CZID,SPCDM,CKH,JZRQ,YWLSH,ZFYH,ZFQD,KH,\r\n"
                + "JYSJ,JYLX,YHLSH,JE)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (!file.exists() || file.isDirectory()) {
            System.out.println("此文件不存在");
        } else {
            impTable(12, sql, log, filePath, date);
        }
        logger.info(filename + "，入库完成, 耗时：" + (System.currentTimeMillis() - a) / 1000 + "s");
    }

    @Override
    public void impYfcs(String date, String filePath) {
        long a = System.currentTimeMillis();
        File file = new File(filePath);
        String filename = file.getName();
        ImportLog log = new ImportLog();
        //执行删除操作
        delTable(date, filename, "KYPG_YFCS");
        String sql = "insert into  KYPG_YFCS" +
                "(yf,WJM,SPBZ,wjrq,JYBZ,JZRQ,CZ,SPCDM,CKH,BC,HGYFK,DY,DYTC,WDHTK)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (!file.exists() || file.isDirectory()) {
            System.out.println("此文件不存在");
        } else {
            impTable(10, sql, log, filePath, date);
        }
        logger.info(filename + "，入库完成, 耗时：" + (System.currentTimeMillis() - a) / 1000 + "s");
    }

    @Override
    public void impYf(String date, String filePath) {
        long a = System.currentTimeMillis();
        File file = new File(filePath);
        String filename = file.getName();
        ImportLog log = new ImportLog();
        //执行删除操作
        delTable(date, filename, "KYPG_YFCS");

        if (!file.exists() || file.isDirectory()) {
            System.out.println("此文件不存在");
        } else {
            FileInputStream io;
            try {
                io = new FileInputStream(file);
                InputStreamReader is = new InputStreamReader(io, "gbk");
                @SuppressWarnings("resource")
                BufferedReader br = new BufferedReader(is, 5 * 1024 * 1024);
                String line = null;
                if ((line = br.readLine()) != null) {
                    if (line.equals("") || line.equals("#")) {//结束标志
                        return;
                    }
                    String newline = line.substring(0, line.lastIndexOf(";"));
                    String[] info = newline.split(",", -1);
                    String sql = "";
                    if (info.length == 17) {
                        sql = "insert into  KYPG_YF" +
                                "(yf,WJM,SPBZ,wjrq,CZID,SPCDM,CKH,JZRQ,CGLX,HGCGH,YFK,SJPK,YTK,JTK,SXF,CGRQ,\r\n"
                                + "JYH,YFKJZRQ,YJYH,YFDYCCZID,YFDYCCZ)" +
                                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    } else if (info.length == 15) {
                        sql = "insert into  KYPG_YF" +
                                "(yf,WJM,SPBZ,wjrq,CZID,SPCDM,CKH,JZRQ,CGLX,HGCGH,YFK,SJPK,YTK,JTK,SXF,CGRQ,\r\n"
                                + "JYH,YFKJZRQ,YJYH)" +
                                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    }
                    impTable(info.length, sql, log, filePath, date);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info(filename + "，入库完成, 耗时：" + (System.currentTimeMillis() - a) / 1000 + "s");
    }

    @Override
    public void impTpjc(String date, String filePath) {
        long a = System.currentTimeMillis();
        File file = new File(filePath);
        String filename = file.getName();
        ImportLog log = new ImportLog();
        //执行删除操作
        delTable(date, filename, "KYPG_YFCS");
        String sql = "insert into  KYPG_TPJC" +
                "(yf,WJM,SPBZ,wjrq,YSPRQ,YSPCDM,YSPCKH,TPZID,TPCDM,TPCKH,TPRQ,TPSJ,JZRQ,\r\n"
                + "TPPH,SPRQ,FZID,FZ,FZJM,DZID,DZ,DZJM,JYZID,LC,SY1,SY2,SY3,LCTZ,ZXTZ,\r\n"
                + "ZZZID,ZZZ,XB,PB,CC,ZCC,DDQYDM,CCRQ,TPYY,TSJJ1,TSJJLC1,TSJJPJ1,TSJJ2,\r\n"
                + "TSJJLC2,TSJJPJ2,TSJJ3,TSJJLC3,TSJJPJ3,TSJJ4,TSJJLC4,TSJJPJ4,TSJJ5,TSJJLC5,\r\n"
                + "TSJJPJ5,JCPJ,YZYJK,JJFDF,WPDPF,CZKTF,RPF,YHJ,YSPJ,YTPJ,TPSXF,JTK,TPRS,\r\n"
                + "SGTPBZ,BZKCSJ,DZLSH,DPBZ,ZXBS)\r\n" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,\r\n"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (!file.exists() || file.isDirectory()) {
            System.out.println("此文件不存在");
        } else {
            impTable(65, sql, log, filePath, date);
        }
        logger.info(filename + "，入库完成, 耗时：" + (System.currentTimeMillis() - a) / 1000 + "s");
    }

    @Override
    public void impZt1(String date, String filePath) {
        long a = System.currentTimeMillis();
        File file = new File(filePath);
        String filename = file.getName();
        ImportLog log = new ImportLog();
        //执行删除操作
        delTable(date, filename, "KYPG_YFCS");
        String sql = "insert into  KYPG_ZT1" +
                "(yf,WJM,SPBZ,wjrq,JYBZ,JZRQ,CZHZM,SPCDM,CKH,XH,JZJPRQ,FZID,\r\n"
                + "FZHZM,FZSSJM,DZID,DZHZM,DZSSJM,SXF)\r\n" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (!file.exists() || file.isDirectory()) {
            System.out.println("此文件不存在");
        } else {
            impTable(14, sql, log, filePath, date);
        }
        logger.info(filename + "，入库完成, 耗时：" + (System.currentTimeMillis() - a) / 1000 + "s");
    }

    @Override
    public void impZt4(String date, String filePath) {
        long a = System.currentTimeMillis();
        File file = new File(filePath);
        String filename = file.getName();
        ImportLog log = new ImportLog();
        //执行删除操作
        delTable(date, filename, "KYPG_YFCS");
        String sql = "insert into  KYPG_ZT4" +
                "(yf,WJM,SPBZ,wjrq,JYBZ,JZRQ,CZ,SPCDM,CKH,KH,JYLX,JYLSH,JYRQ,JYSJ,CZJE,XJK,YHKK,\r\n"
                + "SKYJ,SXF,TKJTJ,YTYJ,TKYJ,TKSXF)\r\n" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (!file.exists() || file.isDirectory()) {
            System.out.println("此文件不存在");
        } else {
            impTable(19, sql, log, filePath, date);
        }
        logger.info(filename + "，入库完成, 耗时：" + (System.currentTimeMillis() - a) / 1000 + "s");
    }

    @Override
    public void impZt2(String date, String filePath) {
        long a = System.currentTimeMillis();
        File file = new File(filePath);
        String filename = file.getName();
        ImportLog log = new ImportLog();
        //执行删除操作
        delTable(date, filename, "KYPG_YFCS");
        String sql = "insert into  KYPG_ZT2" +
                "(yf,WJM,SPBZ,wjrq,JYBZ,JZRQ,CZ,SPCDM,CKH,BC,SKS,FKS,HKS,CZS,TKS,HKS1,GSS,\r\n"
                + "JGS,YJK,YJXJ,YJYHKK,SJK,SJXJ,SJYHKK,YSK,XJYSK,YHKYSK,PK,XJPK,YHKPK,CZJ,\r\n"
                + "XJK,YHKK,SKYJ,SXF,TKJTJ,YTKK,TKYJ,TKSXF)\r\n" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,\r\n"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (!file.exists() || file.isDirectory()) {
            System.out.println("此文件不存在");
        } else {
            impTable(35, sql, log, filePath, date);
        }
        logger.info(filename + "，入库完成, 耗时：" + (System.currentTimeMillis() - a) / 1000 + "s");
    }

    @Override
    public void impSepay(String date, String filePath) {
        long a = System.currentTimeMillis();
        File file = new File(filePath);
        String filename = file.getName();
        ImportLog log = new ImportLog();
        //执行删除操作
        delTable(date, filename, "KYPG_YFCS");
        String sql = "insert into  KYPG_SEPAY" +
                "(yf,WJM,SPBZ,wjrq,CZID,SPCDM,CKH,JZRQ,KPLSH,ZFYH,ZFQD,KH,JYSJ,JYLX,YHLSH,JE)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (!file.exists() || file.isDirectory()) {
            System.out.println("此文件不存在");
        } else {
            impTable(12, sql, log, filePath, date);
        }
        logger.info(filename + "，入库完成, 耗时：" + (System.currentTimeMillis() - a) / 1000 + "s");
    }

    @Override
    public void impLs(String date, String filePath) {
        long a = System.currentTimeMillis();
        File file = new File(filePath);
        String filename = file.getName();
        ImportLog log = new ImportLog();
        String sql = "insert into KYPG_LS (yf,WJM,SPBZ,wjrq,sfcc,CZID,SPCDM,CKH,JZRQ,PZTBZ,PH,SPRQ,FZID,FZ,FZJ,\r\n"
                + "DZID,DZ,DZJ,JYZID,LC,SY1,SY2,SY3,LCTZ,KTTZ,ZZZID,ZZZ,XB,PB,CC,CCZCC,\r\n"
                + "DDQYDM,CCRQ,TSJJ1,TSJJLC1,TSJJPJ1,TSJJ2,TSJJLC2,TSJJPJ2,TSJJ3,TSJJLC3,\r\n"
                + "TSJJPJ3,TSJJ4,TSJJLC4,TSJJPJ4,TSJJ5,TSJJLC5,TSJJPJ5,JCPJ,YZYJK,JJFDF,WPDPF,\r\n"
                + "CZKTF,RPF,YHJ,PJHJ,ZZBCPJ,YTPCC,YTPCCRQ,YTPCCZ,DZLSH,FPTKLSH,DPBZ,XSFWF,\r\n"
                + "YDPSXF,GSXPBS,YPSPRQ,YPSPCDM,YPSPCKH,YPPH,KDWLDH,KDF,KDQYDM,GQCJ,\r\n"
                + "GQTPF,GQTPK,BDH,SBJ,ZXBS)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,\r\n"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,\r\n"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        //执行删除操作
        delTable(date, filename, "KYPG_LS");
        if (!file.exists() || file.isDirectory()) {
            System.out.println("此文件不存在");
        } else {
            impTable_LS(74, sql, log, filePath, date);
        }
        logger.info(filename + "，入库完成, 耗时：" + (System.currentTimeMillis() - a) / 1000 + "s");
    }

    @Override
    public void impLr(String date, String filePath) {
        long a = System.currentTimeMillis();
        File file = new File(filePath);
        String filename = file.getName();
        ImportLog log = new ImportLog();
        String sql = "insert into KYPG_LR  (yf,wjm,spbz,wjrq,sfcc,ypsprq,ypspcdm,ypspckh,tpzid,tpcdm,tpckh,tprq,tpsj,\r\n"
                + "jzrq,tpph,sprq,fzid,fz,fzj,dzid,dz,dzj,jyzid,lc,sy1,sy2,sy3,lctz,zxtz,zzzid,zzz,xb,pb,cc,\r\n"
                + "zcc,ddqydm,ccrq,tpyy,tsjj1,tsjjlc1,tsjjpj1,tsjj2,tsjjlc2,tsjjpj2,tsjj3,tsjjlc3,tsjjpj3,tsjj4,\r\n"
                + "tsjjlc4,tsjjpj4,tsjj5,tsjjlc5,tsjjpj5,jcpj,yzyjk,jjfdf,wpdpf,czktf,rpf,yhj,yspj,ytpj,tpsxf,\r\n"
                + "jtk,tprs,sgtpbz,bzkcsj,dzlsh,dpbz,kdwldh,kdftk,kdqydm,bdh,tbj,zxbs)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,\r\n"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,\r\n"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        //执行删除操作
        delTable(date, filename, "KYPG_LR");
        if (!file.exists() || file.isDirectory()) {
            System.out.println("此文件不存在");
        } else {
            impTable_LR(70, sql, log, filePath, date);
        }
        logger.info(filename + "，入库完成, 耗时：" + (System.currentTimeMillis() - a) / 1000 + "s");
    }

    @Override
    public void impFp(String date, String filePath) {
        long a = System.currentTimeMillis();
        File file = new File(filePath);
        String filename = file.getName();
        ImportLog log = new ImportLog();
        //执行删除操作
        delTable(date, filename, "KYPG_FP");
        String sql = "insert into KYPG_FP(yf,wjm,spbz,wjrq,czid,spcdm,ckh,jzrq,jzsj,sprq,spsj,zfrq,zfsj,\r\n"
                + "zfscdm,zfckh,zfbzh,zfbc,zfczygh,pjph,fzid,fz,dzid,dz,jyzid,lc,xb,pb,cc,cczcc,ccrq,\r\n"
                + "bzkcsh,pjhj,dpbz,zxbs,tvmzfbs)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,\r\n"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (!file.exists() || file.isDirectory()) {
            System.out.println("此文件不存在");
        } else {
            impTable(31, sql, log, filePath, date);
        }
        logger.info(filename + "，入库完成, 耗时：" + (System.currentTimeMillis() - a) / 1000 + "s");
    }

    @Override
    public void impYs(String date, String filePath) {
        long a = System.currentTimeMillis();
        File file = new File(filePath);
        String filename = file.getName();
        ImportLog log = new ImportLog();
        //执行删除操作
        delTable(date, filename, "KYPG_YS");
        String sql = "insert into KYPG_YS(yf,wjm,spbz,wjrq,czid,wzspcdm,ckh,jzrq,pztbz,ph,sprq,bzid,fz,fzj,dzid,\r\n"
                + "dz,dzj,jyzid,lc,sy1,sy2,sy3,lctz,kttz,zzzid,zzz,xb,pb,cc,cczcc,ddqydm,ccrq,tsjj1,\r\n"
                + "tsjjlc1,tsjjpj1,tsjj2,tsjjlc2,tsjjpj2,tsjj3,tsjjlc3,tsjjpj3,tsjj4,tsjjlc4,tsjjpj4,tsjj5,tsjjlc5,\r\n"
                + "tsjjpj5,jcpj,yzyjk,jjfdf,wpdpf,czktf,rpf,pjhj,yhj,zzbcpj,ytpcc,ytpccrq,ytpccz)\r\n" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,\r\n"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)\r\n";
        if (!file.exists() || file.isDirectory()) {
            System.out.println("此文件不存在");
        } else {
            impTable(55, sql, log, filePath, date);
        }
        logger.info(filename + "，入库完成, 耗时：" + (System.currentTimeMillis() - a) / 1000 + "s");
    }

    @Override
    public void impYr(String date, String filePath) {
        long a = System.currentTimeMillis();
        File file = new File(filePath);
        String filename = file.getName();
        ImportLog log = new ImportLog();
        //执行删除操作
        delTable(date, filename, "KYPG_YR");
        String sql = "insert into KYPG_YR(yf,wjm,spbz,wjrq,wzid,wzspcdm,tpckh,tprq,tpsj,jzrq,tpph,sprq,fzid,fz,\r\n"
                + "fzj,dzid,dz,dzj,jyzid,lc,sy1,sy2,sy3,lctz,zxtz,zzzid,zzz,xb,pb,cc,zcc,ddqydm,ccrq,\r\n"
                + "tpyy,tsjj1,tsjjlc1,tsjjpj1,tsjj2,tsjjlc2,tsjjpj2,tsjj3,tsjjlc3,tsjjpj3,tsjj4,tsjjlc4,tsjjpj4,\r\n"
                + "tsjj5,tsjjlc5,tsjjpj5,jcpj,yzyjk,jjfdf,wpdpf,czktf,rpf,yhj,yspj,ytpj,tpsxf,jtk,tprs,sgtpbz)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,\r\n"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (!file.exists() || file.isDirectory()) {
            System.out.println("此文件不存在");
        } else {
            impTable(58, sql, log, filePath, date);
        }
        logger.info(filename + "，入库完成, 耗时：" + (System.currentTimeMillis() - a) / 1000 + "s");
    }

    @Override
    public void impKcs4(String date, String filePath) {
        long a = System.currentTimeMillis();
        File file = new File(filePath);
        String filename = file.getName();
        ImportLog log = new ImportLog();
        //执行删除操作
        delTable(date, filename, "KYPG_KCS4");
        String sql = "insert into KYPG_KCS4(yf,wjm,spbz,wjrq,jybz,jzrq,cz,spcdm,zs,zfzs,fsrs,gmrs,zzrs,zj,kdjs,\r\n"
                + "kdf,sbfs,fbfs,shfs,sbbf)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (!file.exists() || file.isDirectory()) {
            System.out.println("此文件不存在");
        } else {
            impTable(16, sql, log, filePath, date);
        }
        logger.info(filename + "，入库完成, 耗时：" + (System.currentTimeMillis() - a) / 1000 + "s");
    }

    @Override
    public void impTcs2(String date, String filePath) {
        long a = System.currentTimeMillis();
        File file = new File(filePath);
        String filename = file.getName();
        ImportLog log = new ImportLog();
        String sql = "insert into KYPG_TCS2(yf,WJM,SPBZ,wjrq,JYBZ,JZRQ,CZ,TPDM,TPZS,TPRS,YTPK,JTK,TPSXF,"
                + "TKDJS,KDFTK,TBFS,TBJE)\r\n"
                + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        //执行删除操作
        delTable(date, filename, "KYPG_TCS2");
        if (!file.exists() || file.isDirectory()) {
            System.out.println("此文件不存在");
        } else {
            impTable(13, sql, log, filePath, date);
        }
        logger.info(filename + "，入库完成, 耗时：" + (System.currentTimeMillis() - a) / 1000 + "s");
    }

    @Override
    public void impSk2(String date, String filePath) {
        long a = System.currentTimeMillis();
        File file = new File(filePath);
        String filename = file.getName();
        ImportLog log = new ImportLog();
        //执行删除操作
        delTable(date, filename, "KYPG_SK2");
        if (!file.exists() || file.isDirectory()) {
            System.out.println("此文件不存在");
        } else {
            FileInputStream io;
            try {
                io = new FileInputStream(file);
                InputStreamReader is = new InputStreamReader(io, "gbk");
                @SuppressWarnings("resource")
                BufferedReader br = new BufferedReader(is, 5 * 1024 * 1024);
                String line = null;
                if ((line = br.readLine()) != null) {
                    if (line.equals("") || line.equals("#")) {//结束标志
                        return;
                    }
                    String newline = line.substring(0, line.lastIndexOf(";"));
                    String[] info = newline.split(",", -1);
                    String sql = "";
                    if (info.length == 63) {
                        sql = "insert into KYPG_SK2(yf,wjm,spbz,wjrq,jybz,jzrq,cz,spcdm,ckh,bc,zs,sszs,fpzs,ccpzzs,posjzs,\r\n"
                                + "yjk,yjxjzp,yjkjzj,yjyhkj,sjk,sjxjzp,sjkjzj,sjyhkk,ysk,xjzpysk,kjzysk,yhkysk,pk,xjzppk,\r\n"
                                + "kjzpk,yhkpk,pmzpk,gqypk,gqsk,gqxjk,gqkjzk,gqyhkk,gqcj,gqjtk,jtxj,jtkjzk,jtyhkk,\r\n"
                                + "dqgypk,ypxjk,ypkjzk,ypyhkk,ccpzk,xsfwf,fwfxj,fwfyhkk,fwfytkk,ydpsxf,sxfxj,sxfyhkk,\r\n"
                                + "sxfytkk,kdjs,kdf,gqtpf,gqtpxj,gqtpyhkk,sbfs,fbfs,sbj,sbxj,sbyhkk,dzph,bxdh)\r\n" +
                                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,\r\n"
                                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,\r\n"
                                + "?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    } else if (info.length == 64) {
                        sql = "insert into KYPG_SK2(yf,wjm,spbz,wjrq,jybz,jzrq,cz,spcdm,ckh,bc,zs,sszs,fpzs,ccpzzs,posjzs,\r\n"
                                + "yjk,yjxjzp,yjkjzj,yjyhkj,sjk,sjxjzp,sjkjzj,sjyhkk,ysk,xjzpysk,kjzysk,yhkysk,pk,xjzppk,\r\n"
                                + "kjzpk,yhkpk,pmzpk,gqypk,gqsk,gqxjk,gqkjzk,gqyhkk,gqcj,gqjtk,jtxj,jtkjzk,jtyhkk,\r\n"
                                + "dqgypk,ypxjk,ypkjzk,ypyhkk,ccpzk,xsfwf,fwfxj,fwfyhkk,fwfytkk,ydpsxf,sxfxj,sxfyhkk,\r\n"
                                + "sxfytkk,kdjs,kdf,gqtpf,gqtpxj,gqtpyhkk,sbfs,fbfs,sbj,sbxj,sbyhkk,dzph,bxdh,xfkdy)\r\n" +
                                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,\r\n"
                                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,\r\n"
                                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    }
                    impTable(info.length, sql, log, filePath, date);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info(filename + "，入库完成, 耗时：" + (System.currentTimeMillis() - a) / 1000 + "s");
    }

    @Override
    public void impSt2(String date, String filePath) {
        long a = System.currentTimeMillis();
        File file = new File(filePath);
        String filename = file.getName();
        ImportLog log = new ImportLog();
        //执行删除操作
        //先退票再退款，退票二号表文件中增加“应退现金票款”、“应退卡票退款”
        delTable(date, filename, "KYPG_ST2");
        String sql = "insert into  KYPG_ST2(yf,wjm,spbz,wjrq,jybz,jzrq,cz,spcdm,ckh,bc,zs,jk,ygjy,sjjy,ys,pk,\r\n"
                + "pmzpk,ytxj,ytczkk,ytyhkk,jtk,jtxj,jtczkk,jtyhkk,tpf,tpfxj,tpfczkk,tpfyhkk,tkdjs,kdftk,\r\n"
                + "tbfs,tbj,tbxjk,tbyhkk,wbxjtk,tpqrtk)" +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (!file.exists() || file.isDirectory()) {
            System.out.println("此文件不存在");
        } else {
            impTable_St2(32, sql, log, filePath, date);
        }
        logger.info(filename + "，入库完成, 耗时：" + (System.currentTimeMillis() - a) / 1000 + "s");
    }

    @Override
    public void impCbzb(String date, String filePath) {
        long a = System.currentTimeMillis();
        File file = new File(filePath);
        String filename = file.getName();
        ImportLog log = new ImportLog();
        //执行删除操作
        delTable(date, filename, "KYPG_CBZB");
        String sql = "insert into  KYPG_CBZB" +
                "(yf,wjm,pjlx,wjrq,czid,spcdm,ckh,jzri,pjztbz,ph,bprq,fzid,fz,fzj,dzid,dz,dzj,\r\n"
                + "jyzid,lc,sy1,sy2,sy3,lctz,kttz,zzzid,zzz,xb,pb,cc,cczcc,ddqydm,ccrq,tsjj1,tsjjlc1,\r\n"
                + "tsjjpj1,tsjj2,tsjjlc2,tsjjpj2,tsjj3,tsjjlc3,tsjjpj3,tsjj4,tsjjlc4,tsjjpj4,tsjj5,tsjjlc5,tsjjpj5,\r\n"
                + "jcpj,by0,jjfdf,wpdpf,ccktf,rpf,yhj,pjhj,zcpj,zzypcc,zzypccrq,zzypccz,sspk,jjje,sxf,\r\n"
                + "sydm,bcsy,yzyjk,yph,gpj,gpz,ypgpckh,gpfs,gprq,zffs,zflsh)\r\n" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,\r\n"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (!file.exists() || file.isDirectory()) {
            System.out.println("此文件不存在");
        } else {
            impTable(69, sql, log, filePath, date);
        }
        logger.info(filename + "，入库完成, 耗时：" + (System.currentTimeMillis() - a) / 1000 + "s");
    }

    /**
     * 删除表方法
     *
     * @param参数：日期，文件名，表名
     */
    @Override
    public void delTable(String yf, String wjm, String tableName) {
        String sql = "delete  from " + tableName
                + " where yf=? and wjm=?";
        jdbcTemplate.update(sql, yf.substring(0, 6), wjm);
    }

    /**
     * 导入表方法
     *
     * @param参数：数组长度，sql语句，日志对象，文件路径，月份
     */
    @Override
    public void impTable(int len, String sql, ImportLog log, String filePath, String date) {
        boolean flag = true;//入库标志
        File file = new File(filePath);
        String filename = file.getName();
        String line = null;
        int num = 0;
        try {
            FileInputStream io = new FileInputStream(file);
            InputStreamReader is = new InputStreamReader(io, "gbk");
            @SuppressWarnings("resource")
            BufferedReader br = new BufferedReader(is, 5 * 1024 * 1024);
            while ((line = br.readLine()) != null) {
                List<String> list = new ArrayList<>();
                if (line.equals("") || line.equals("#")) {//结束标志
                    continue;
                }
                String newline = line.substring(0, line.lastIndexOf(";"));
                String[] info = newline.split(",", -1);
                num++;
                if (info.length != len) {//文件异常，
                    flag = false;
                    log.setYf(date.substring(0, 6));
                    log.setWjm(filename);
                    log.setJlzs(num);
                    log.setRkjg("1");
                    log.setCwxx(filename + ",第" + num + "行数据异常!长度和指定长度不一致");
                    logger.error(filename + ",第" + num + "行数据异常!长度和指定长度不一致");
                    updateLog(log);
                    return;
                }
                if (info.length == len) {
                    list.add(date.substring(0, 6));
                    list.add(filename);
                    if (filename.toUpperCase().startsWith("CBKP") || filename.toUpperCase().startsWith("ZBKP")) {
                        if (filename.toUpperCase().startsWith("CBKP")) {
                            list.add("0");//0车补，1站补
                        } else {
                            list.add("1");
                        }
                    } else if (filename.contains(".00")) {
                        list.add("1");//1网售 0站售
                    } else {
                        list.add("0");
                    }
                    list.add(date);
                    for (int i = 0; i < len; i++) {
                        list.add(info[i].trim());
                    }
                    Object[] obj = list.toArray();
                    jdbcTemplate.update(sql, obj);
                }
            }
        } catch (IOException e) {
            flag = false;
            log.setYf(date.substring(0, 6));
            log.setWjm(filename);
            log.setJlzs(num);
            log.setRkjg("1");
            log.setCwxx(filename + ",第" + num + "行数据异常," + e.getMessage());
            logger.error(filename + ",第" + num + "行数据异常," + e.getMessage());
            updateLog(log);
            e.printStackTrace();
        } finally {
            if (flag) {
                log.setYf(date.substring(0, 6));
                log.setWjm(filename);
                log.setJlzs(num);
                log.setRkjg("0");
                log.setCwxx(null);
                updateLog(log);
            }
        }
    }

    @Override
    public void impTable_LS(int len, String sql, ImportLog log, String filePath, String date) {
        boolean flag = true;//入库标志
        File file = new File(filePath);
        String filename = file.getName();
        String line = null;
        int num = 0;
        try {
            FileInputStream io = new FileInputStream(file);
            InputStreamReader is = new InputStreamReader(io, "gbk");
            @SuppressWarnings("resource")
            BufferedReader br = new BufferedReader(is, 5 * 1024 * 1024);
            while ((line = br.readLine()) != null) {
                List<String> list = new ArrayList<>();
                if (line.equals("") || line.equals("#")) {//结束标志
                    continue;
                }
                String newline = line.substring(0, line.lastIndexOf(";"));
                String[] info = newline.split(",", -1);
                if (!filename.contains(".00") && info.length == 75) {//站售
                    len = 75;
                }
                num++;
                if (info.length != len) {//文件异常，
                    flag = false;
                    log.setYf(date.substring(0, 6));
                    log.setWjm(filename);
                    log.setJlzs(num);
                    log.setRkjg("1");
                    log.setCwxx(filename + ",第" + num + "行数据异常!长度和指定长度不一致");
                    logger.error(filename + ",第" + num + "行数据异常!长度和指定长度不一致");
                    updateLog(log);
                    return;
                }
                if (info.length == len) {
                    list.add(date.substring(0, 6));
                    list.add(filename);
                    if (filename.contains(".00")) {
                        list.add("1");//1网售 0站售
                    } else {
                        list.add("0");
                    }
                    list.add(date);
                    //第5位sfcc信息根据第25、26个字段信息获取,如:38000K138707	K1387
                    String cc = info[24].trim();
                    if (cc != null && !cc.equals("")) {
                        String cczcc = info[25].trim();
                        int l = cczcc.length() + 2;
                        String sfcc = cc.substring(cc.length() - l, cc.length() - 2);
                        list.add(sfcc);
                    } else {
                        list.add(null);
                    }
                    if (filename.contains(".00")) {//网售票文件里面只有74个字段
                        for (int i = 0; i < len; i++) {
                            list.add(info[i].trim());
                        }
                    } else {
                        if (len == 75) {
                            for (int i = 0; i < len - 1; i++) {//站售，文件包含75个字段，只取前74个字段，最后一个字段不要
                                list.add(info[i].trim());
                            }
                        } else {
                            for (int i = 0; i < len; i++) {
                                list.add(info[i].trim());
                            }
                        }
                    }
                    Object[] obj = list.toArray();
                    jdbcTemplate.update(sql, obj);
                }
            }
        } catch (IOException e) {
            flag = false;
            log.setYf(date.substring(0, 6));
            log.setWjm(filename);
            log.setJlzs(num);
            log.setRkjg("1");
            log.setCwxx(filename + ",第" + num + "行数据异常," + e.getMessage());
            logger.error(filename + ",第" + num + "行数据异常," + e.getMessage());
            updateLog(log);
            e.printStackTrace();
        } finally {

            if (flag) {
                log.setYf(date.substring(0, 6));
                log.setWjm(filename);
                log.setJlzs(num);
                log.setRkjg("0");
                log.setCwxx(null);
                updateLog(log);
            }

        }
    }

    @Override
    public void impTable_LR(int len, String sql, ImportLog log, String filePath, String date) {
        boolean flag = true;//入库标志
        File file = new File(filePath);
        String filename = file.getName();
        String line = null;
        int num = 0;
        try {
            FileInputStream io = new FileInputStream(file);
            InputStreamReader is = new InputStreamReader(io, "gbk");
            @SuppressWarnings("resource")
            BufferedReader br = new BufferedReader(is, 5 * 1024 * 1024);
            while ((line = br.readLine()) != null) {
                List<String> list = new ArrayList<>();
                if (line.equals("") || line.equals("#")) {//结束标志
                    continue;
                }
                String newline = line.substring(0, line.lastIndexOf(";"));
                String[] info = newline.split(",", -1);
                num++;
                if (info.length != len) {//文件异常，
                    flag = false;
                    log.setYf(date.substring(0, 6));
                    log.setWjm(filename);
                    log.setJlzs(num);
                    log.setRkjg("1");
                    log.setCwxx(filename + ",第" + num + "行数据异常!长度和指定长度不一致");
                    logger.error(filename + ",第" + num + "行数据异常!长度和指定长度不一致");
                    updateLog(log);
                    return;
                }
                if (info.length == len) {
                    list.add(date.substring(0, 6));
                    list.add(filename);
                    if (filename.contains(".00")) {
                        list.add("1");//1网售 0站售
                    } else {
                        list.add("0");
                    }
                    list.add(date);
                    //第5位sfcc信息根据第29、30个字段信息获取,如:800000Z16208	Z162
                    String cc = info[28].trim();
                    if (cc != null && !cc.equals("")) {
                        String cczcc = info[29].trim();
                        int l = cczcc.length() + 2;
                        String sfcc = cc.substring(cc.length() - l, cc.length() - 2);
                        list.add(sfcc);
                    } else {
                        list.add(null);
                    }
                    for (int i = 0; i < len; i++) {
                        list.add(info[i].trim());
                    }
                    Object[] obj = list.toArray();
                    jdbcTemplate.update(sql, obj);
                }
            }
        } catch (IOException e) {
            flag = false;
            log.setYf(date.substring(0, 6));
            log.setWjm(filename);
            log.setJlzs(num);
            log.setRkjg("1");
            log.setCwxx(filename + ",第" + num + "行数据异常," + e.getMessage());
            logger.error(filename + ",第" + num + "行数据异常," + e.getMessage());
            updateLog(log);
            e.printStackTrace();
        } finally {
            if (flag) {
                log.setYf(date.substring(0, 6));
                log.setWjm(filename);
                log.setJlzs(num);
                log.setRkjg("0");
                log.setCwxx(null);
                updateLog(log);
            }

        }
    }

    @Override
    public void impTable_St2(int len, String sql, ImportLog log, String filePath, String date) {
        boolean flag = true;//入库标志
        File file = new File(filePath);
        String filename = file.getName();
        String line = null;
        int num = 0;
        try {
            FileInputStream io = new FileInputStream(file);
            InputStreamReader is = new InputStreamReader(io, "gbk");
            @SuppressWarnings("resource")
            BufferedReader br = new BufferedReader(is, 5 * 1024 * 1024);
            while ((line = br.readLine()) != null) {
                List<String> list = new ArrayList<>();
                String[] info = line.split(",", -1);
                if ((info.length != len || info.length != len - 2) && info[0].equals("#")) {//结束标志
                    continue;
                }
                if (info.length == 1 && info[0].equals("")) {
                    continue;
                }
                num++;
                if (info.length != len && info.length != len - 2) {//文件异常，
                    flag = false;
                    log.setYf(date.substring(0, 6));
                    log.setWjm(filename);
                    log.setJlzs(num);
                    log.setRkjg("1");
                    log.setCwxx(filename + ",第" + num + "行数据异常!长度和指定长度不一致");
                    logger.error(filename + ",第" + num + "行数据异常!长度和指定长度不一致");
                    updateLog(log);
                    return;
                }
                if (info.length == len || info.length == len - 2) {
                    list.add(date);
                    list.add(filename);
                    if (filename.contains(".00")) {
                        list.add("1");//1网售 0站售
                    } else {
                        list.add("0");
                    }
                    list.add(date);
                    if (info.length == len) {//开办先退票后退款业务后接口格式
                        for (int i = 0; i < info.length; i++) {
                            if (i != info.length - 1) {
                                list.add(info[i].trim());
                            } else {
                                list.add(info[i].trim().replace(";", ""));
                            }
                        }
                    }
                    if (info.length == len - 2) {
                        for (int i = 0; i < info.length; i++) {
                            if (i != info.length - 1) {
                                list.add(info[i].trim());
                            } else {
                                list.add(info[i].trim().replace(";", ""));
                            }
                        }
                        list.add(null);
                        list.add(null);
                    }
                    Object[] obj = list.toArray();
                    jdbcTemplate.update(sql, obj);
                }
            }

        } catch (IOException e) {
            flag = false;
            log.setYf(date.substring(0, 6));
            log.setWjm(filename);
            log.setJlzs(num);
            log.setRkjg("1");
            log.setCwxx(filename + ",第" + num + "行数据异常," + e.getMessage());
            logger.error(filename + ",第" + num + "行数据异常," + e.getMessage());
            updateLog(log);
            e.printStackTrace();
        } finally {
            if (flag) {
                log.setYf(date.substring(0, 6));
                log.setWjm(filename);
                log.setJlzs(num);
                log.setRkjg("0");
                log.setCwxx(null);
                updateLog(log);
            }

        }
    }
}
