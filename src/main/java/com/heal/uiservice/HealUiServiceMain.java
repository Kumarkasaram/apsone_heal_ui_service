package com.heal.uiservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@PropertySource(value="classpath:conf.properties")
@EnableTransactionManagement(proxyTargetClass = true)
public class HealUiServiceMain {

    public static void main(String[] args) {
        SpringApplication.run(HealUiServiceMain.class, args);
    }
}