package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TestScheduler {
    private static final Logger logger = LoggerFactory.getLogger(TestScheduler.class);
    int t=1;
    // Chạy mỗi 10 giây để test nhanh
    @Scheduled(cron = "*/10 * * * * *")
    public void logOk() {
        t++;
        logger.info("lần "+t);
    }
}
