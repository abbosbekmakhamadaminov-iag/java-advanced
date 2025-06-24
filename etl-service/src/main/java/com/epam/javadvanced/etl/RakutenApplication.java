package com.epam.javadvanced.etl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableR2dbcRepositories(basePackages = "rakuten.repo")
public class RakutenApplication {
    public static void main(String[] args) {
        SpringApplication.run(RakutenApplication.class, args);
    }
}
