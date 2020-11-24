package com.cyfxr.quartz.task;

import com.cyfxr.common.utils.FTP.FtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component("task")
public class Task {
    @Autowired
    private FtpUtils ftpUtils;

    //通过ftp  下载kp文件
    public void downloadKp() {
        try {
            ftpUtils.FtpDownload("20201118");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
