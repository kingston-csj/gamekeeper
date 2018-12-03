package com.kingston.jforgame.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
public class WebStartup {

    public static void main(String[] args) {
        SpringApplication.run(WebStartup.class, args);
    }
}
