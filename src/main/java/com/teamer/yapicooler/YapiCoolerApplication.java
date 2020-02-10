package com.teamer.yapicooler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * yapi-cooler 启动类
 * @author tanzj
 */
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class YapiCoolerApplication {

    public static void main(String[] args) {
        SpringApplication.run(YapiCoolerApplication.class, args);
    }

}
