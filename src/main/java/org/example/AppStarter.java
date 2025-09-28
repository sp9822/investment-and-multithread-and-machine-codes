package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.example")
public class AppStarter {
    public static void main(String[] args) {
        SpringApplication.run(AppStarter.class, args);
    }
}
