package com.inditex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan("com.inditex.infraestructure.adapters.outbound.h2.model")
public class InditexServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InditexServiceApplication.class, args);
    }

}
