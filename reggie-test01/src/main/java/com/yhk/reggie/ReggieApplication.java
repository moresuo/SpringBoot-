package com.yhk.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.swing.*;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/5/30 16:03
 * @注释 启动类
 */
@Slf4j //保存日志
@SpringBootApplication
@ServletComponentScan//启动servlet组件扫描，会自动扫描并注册servlet,filter,listener相关注解，@WebServlet、@WebFilter和@WebListener
@EnableTransactionManagement
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class);
        log.info("项目启动成功。。。");
    }
}
