package com.spooky.patito.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(
        scanBasePackages = {
                "com.spooky.patito.core",
                "com.spooky.patito.model",
                "com.spooky.patito.web"})

public class GatewayApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
