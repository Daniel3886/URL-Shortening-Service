package org.springboot.urlshorteningservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class UrlShorteningServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlShorteningServiceApplication.class, args);
    }

}
