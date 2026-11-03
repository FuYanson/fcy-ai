package com.example;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "http://127.0.0.1:5173")
public class ClockApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClockApplication.class, args);
    }

    @GetMapping("/api/time")
    public String time() {
        return Instant.now().toString();
    }

    @GetMapping("/api/baby/profile")
    public Map<String, Object> profile() {
        return Map.of("siteName", "宝宝成长纪念馆", "subtitle", "每个小小瞬间，都值得珍藏", "publicDemo", true);
    }

    @GetMapping("/api/baby/milestones")
    public List<Map<String, String>> milestones() {
        return List.of(
            Map.of("label", "DAY 01", "title", "你好，世界", "description", "我们的故事，从这一声啼哭开始。"),
            Map.of("label", "FIRST MONTH", "title", "满月快乐", "description", "小小的你，正在慢慢长大。"),
            Map.of("label", "DAY 100", "title", "百日的约定", "description", "愿你被爱包围，平安喜乐。")
        );
    }
}
