package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ScheduledFuture;

@Service
public class OneTimeScheduler {
    private static final Logger logger = LoggerFactory.getLogger(OneTimeScheduler.class);
    private final TaskScheduler scheduler;

    public OneTimeScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(2);
        taskScheduler.setThreadNamePrefix("one-time-scheduler-");
        taskScheduler.initialize();
        this.scheduler = taskScheduler;
    }

    public ScheduledFuture<?> scheduleOneTimeTask(LocalDateTime time) {
        Instant startInstant = time.atZone(ZoneId.systemDefault()).toInstant();
        return scheduler.schedule((Runnable) () -> {
            logger.info("Chạy task 1 lần tại {}", LocalDateTime.now());
            // TODO: logic lưu DB ở đây
        }, startInstant);
    }
}
