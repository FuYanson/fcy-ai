package com.example;

import java.time.Instant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ClockApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClockApplication.class, args);
    }

    @GetMapping("/api/time")
    public String time() {
        return Instant.now().toString();
    }
}
