package com.robert.qrcodelogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(value = "com.robert.qrcodelogin.*")
public class QrcodeloginApplication {

    public static void main(String[] args) {
        SpringApplication.run(QrcodeloginApplication.class, args);
    }
}
