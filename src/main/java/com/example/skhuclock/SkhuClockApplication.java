package com.example.skhuclock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SkhuClockApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkhuClockApplication.class, args);
    }

}
