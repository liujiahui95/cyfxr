package com.cyfxr.common.utils.FTP;


import com.cyfxr.common.domain.ImportLog;
import com.cyfxr.common.utils.DateUtil;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.*;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @Description ftp 工具类
 * @Author liujiahui
 **/
@Component
public class FtpUtils {

    @Value("${ftp.host}")
    private String host;

    @Value("${ftp.port}")
    private int port;

    @Value("${ftp.username}")
    private String userName;

    @Value("${ftp.password}")
    private String password;
    //ftp路径
    @Value("${ftp.ftpPath}")
    private String FTPPath;
    //本地保存路径
    @Value("${ftp.localPath}")
    private String localPath;
    private static final Logger log = LoggerFactory.getLogger(FtpUtils.class);
    private static final String CHARSET_GBK = "GBK";
    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String CHARSET_ISO = "ISO-8859-1";


    /**
     * 链接 ftp 获取 ftpClient
     */
    public FTPClient getFtpClient() {
        FTPClient ftp = new FTPClient();
        try {
            ftp.setDefaultTimeout(20000);
            ftp.setDataTimeout(3600000);
            ftp.setConnectTimeout(60000);
            ftp.setControlEncoding(CHARSET_GBK);
            // 连接ftp
            ftp.connect(host, port);
            // 登陆ftp
            ftp.login(userName, password);
            ftp.setBufferSize(4096);
            ftp.setKeepAlive(true);
            //以二进制流的方式读取
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            // 判断ftp是否登录成功
            if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                log.info("登陆成功" + host + ":" + port);
            } else {
                log.info("登陆失败，用户名或者密码错误" + host + ":" + port);
                ftp.disconnect();
            }
        } catch (SocketException e) {
            e.printStackTrace();
            log.error("ftp地址可能错误");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("ftp端口错误");
        }
        return ftp;
    }

    public void FtpDownload(String date) throws Exception {
        FTPClient ftpClient = this.getFtpClient();
        HashMap<String, String> pathMap = new HashMap<String, String>();
        getFilePath(ftpClient, pathMap);
        Iterator<Map.Entry<String, String>> it = pathMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            ftpClient.changeWorkingDirectory(entry.getKey());
            //List<String> listName = aryToList( ftpClient.listNames());
            //获取指定类型的文件，如果全年的在一个文件夹，可以在此处过滤指定日期的文件，添加一个参数
            List<String> listName = aryToList(ftpClient.listNames("*" + date.substring(4, 8) + ".*"));
            System.out.println(entry.getKey());
            System.err.println(listName.size());
            if (listName.size() > 0) {
                for (int i = 0; i < listName.size(); i++) {
                    downloadFile(ftpClient, listName.get(i), localPath, entry.getKey(), date);
                }
            }
        }
        logout(ftpClient);
    }

    /**
     * 下载文件
     */
    public void downloadFile(FTPClient ftpClient, String listName, String localFilePath, String ftpPath, String date) throws IOException {
        ftpClient.changeWorkingDirectory(ftpPath);
        try {
            FTPFile[] files = ftpClient.listFiles(listName);
            String wjxgsj = "";
            for (FTPFile ftpFile : files) {//获取文件上传时间
                if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) continue;
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                wjxgsj = df.format(ftpFile.getTimestamp().getTime());
            }
            log.info("正在下载文件:" + listName);
            File file = new File(localFilePath + ftpPath + File.separator);
            if (!file.exists() && !file.isDirectory()) {
                file.mkdirs(); //创建多个目录
            }
            File localFile = new File(localFilePath + ftpPath + File.separator + listName);
            OutputStream os = new FileOutputStream(localFile);
            boolean flag = ftpClient.retrieveFile(listName, os);
            if (flag) {// 判断文件是否正确下载
                ImportLog info = new ImportLog();
                info.setYf(date.substring(0, 6));
                info.setWjm(listName);
                info.setWjlj(ftpPath);
                info.setWjxgsj(wjxgsj);
                String ftpxzsj = DateUtil.getSystemTime2();
                info.setFtpxzsj(ftpxzsj);
                log.info("文件下载成功:" + listName);
            }
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //获取所有文件路径
    public void getFilePath(FTPClient ftpClient, HashMap<String, String> pathMap) {
        try {
            ftpClient.changeWorkingDirectory(FTPPath);
            recursionGetPath(ftpClient, FTPPath, pathMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //递归获取路径
    public void recursionGetPath(FTPClient ftpClient, String path, HashMap<String, String> pathMap) throws IOException {
        FTPFile[] files = ftpClient.listFiles();
        for (FTPFile ftpFile : files) {
            if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) continue;
            if (ftpFile.isDirectory()) {//如果是目录，则递归调用，查找里面所有文件
                path += "/" + ftpFile.getName();
                pathMap.put(path, "");
                ftpClient.changeWorkingDirectory(path);//改变当前路径
                recursionGetPath(ftpClient, path, pathMap);//递归调用
                //避免对之后的同目录下的路径构造作出干扰，遍历同级目录“//C_test/P”
                path = path.substring(0, path.lastIndexOf("/"));
            }
        }
    }

    //登出ftp
    public void logout(FTPClient ftp) {
        try {
            ftp.logout();
            ftp.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //数组转集合
    private static List<String> aryToList(String[] ary) {
        if (ary == null)
            return new ArrayList<String>();
        else return Arrays.asList(ary);
    }
}

