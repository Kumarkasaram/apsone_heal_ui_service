package com.heal.uiservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@PropertySource(value="classpath:conf.properties")
@EnableJpaRepositories("com.heal.uiservice")
public class HealUiServiceMain {

    public static void main(String[] args) {
        SpringApplication.run(HealUiServiceMain.class, args);
    }
}