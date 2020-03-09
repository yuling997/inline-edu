package com.atguigu.guli.service.ucenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author helen
 * @since 2020/3/2
 */
@SpringBootApplication
@ComponentScan({"com.atguigu.guli"})
@EnableEurekaClient

public class ServiceUcenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceUcenterApplication.class, args);
    }
}
