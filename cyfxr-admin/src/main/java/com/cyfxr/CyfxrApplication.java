package com.cyfxr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author cyfxr
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CyfxrApplication {
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(CyfxrApplication.class, args);
        System.out.println("启动成功！！！！！！！！！！！！！！！！");
    }
}