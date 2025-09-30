package com.example.demo.controller;

import com.example.demo.service.OneTimeScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/job")
public class OneTimeSchedulerController {
    private static final Logger logger = LoggerFactory.getLogger(OneTimeSchedulerController.class);
    private final OneTimeScheduler oneTimeScheduler;

    public OneTimeSchedulerController(OneTimeScheduler oneTimeScheduler) {
        this.oneTimeScheduler = oneTimeScheduler;
    }

    @PostMapping("/schedule")
    public ResponseEntity<String> schedule(@RequestParam String dateTime) {
        String input = dateTime == null ? "" : dateTime.trim(); // remove newline / spaces
        LocalDateTime ldt;

        try {
            // epoch (10 or 13 digits)
            if (input.matches("^\\d{10}$") || input.matches("^\\d{13}$")) {
                long epoch = Long.parseLong(input);
                if (input.length() == 10) epoch *= 1000L;
                ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault());
            } else {
                // try parse OffsetDateTime (handles +07:00 or Z)
                try {
                    OffsetDateTime odt = OffsetDateTime.parse(input);
                    ldt = odt.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
                } catch (DateTimeParseException ignored) {
                    // try LocalDateTime (ISO_LOCAL_DATE_TIME)
                    try {
                        ldt = LocalDateTime.parse(input, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    } catch (DateTimeParseException ignored2) {
                        // try pattern "yyyy-MM-dd HH:mm:ss"
                        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        ldt = LocalDateTime.parse(input, f);
                    }
                }
            }
        } catch (Exception e) {
            String msg = "Sai format dateTime. Hỗ trợ:\n" +
                    "- yyyy-MM-ddTHH:mm:ss  (ví dụ 2025-09-30T21:50:00)\n" +
                    "- yyyy-MM-dd HH:mm:ss  (ví dụ 2025-09-30 21:50:00)\n" +
                    "- yyyy-MM-ddTHH:mm:ss+07:00 hoặc Z (ví dụ 2025-09-30T21:50:00+07:00)\n" +
                    "- epoch millis hoặc epoch seconds (vd: 1696091400000 hoặc 1696091400)";
            return ResponseEntity.badRequest().body(msg);
        }

        LocalDateTime now = LocalDateTime.now();
        if (ldt.isBefore(now)) {
            // nếu đã qua -> chạy ngay (sau 1s) để test
            oneTimeScheduler.scheduleOneTimeTask(now.plusSeconds(1));
            logger.info("Yêu cầu lịch ở quá khứ ({}), sẽ chạy ngay.", ldt);
            return ResponseEntity.ok("Thời gian đã qua, job sẽ chạy ngay (1s).");
        } else {
            oneTimeScheduler.scheduleOneTimeTask(ldt);
            logger.info("Đã tạo job chạy lúc {}", ldt);
            return ResponseEntity.ok("Đã tạo job chạy lúc " + ldt.toString());
        }
    }
}
