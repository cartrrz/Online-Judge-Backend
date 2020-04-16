package com.spooky.patito.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.spooky.patito"})
@EnableJpaRepositories(basePackages = {"com.spooky.patito.repository"})
@EntityScan(basePackages = {"com.spooky.patito.model"})
public class GatewayApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
