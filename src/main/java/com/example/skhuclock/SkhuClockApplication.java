package com.example.skhuclock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
@Slf4j
public class SkhuClockApplication {

    public static void main(String[] args) {
        init();
        SpringApplication.run(SkhuClockApplication.class, args);
    }

    private static void init() {
        log.info("Spring Server TimeZone : Asia/Seoul");
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}
