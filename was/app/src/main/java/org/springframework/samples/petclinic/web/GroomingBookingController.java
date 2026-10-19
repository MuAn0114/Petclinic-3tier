package org.springframework.samples.petclinic.web;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/grooming/bookings")
public class GroomingBookingController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GroomingBookingController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public List<Map<String, Object>> list() {
        return jdbcTemplate.queryForList(
            "SELECT id, guardian_name AS guardianName, pet_name AS petName, service, DATE_FORMAT(preferred_at, '%Y-%m-%d') AS preferredDate, " +
            "DATE_FORMAT(preferred_at, '%H:%i') AS preferredTime, status " +
            "FROM grooming_bookings ORDER BY preferred_at DESC LIMIT 50");
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody GroomingBookingRequest request) {
        Map<String, Object> response = new HashMap<>();
        if (request == null || blank(request.guardianName) || blank(request.phone) || blank(request.petName)
                || blank(request.petType) || blank(request.service) || blank(request.preferredDate)
                || blank(request.preferredTime)) {
            response.put("message", "필수 항목을 모두 입력해주세요.");
            return ResponseEntity.badRequest().body(response);
        }
        if (tooLong(request.guardianName, 60) || tooLong(request.phone, 30) || tooLong(request.petName, 60)
                || tooLong(request.petType, 30) || tooLong(request.petWeight, 30)
                || tooLong(request.service, 50) || tooLong(request.memo, 500)) {
            response.put("message", "입력한 내용이 허용 길이를 초과했습니다.");
            return ResponseEntity.badRequest().body(response);
        }

        final Timestamp preferredAt;
        try {
            LocalDate date = LocalDate.parse(request.preferredDate.trim());
            LocalTime time = LocalTime.parse(request.preferredTime.trim());
            preferredAt = Timestamp.valueOf(LocalDateTime.of(date, time));
        } catch (DateTimeParseException ex) {
            response.put("message", "희망 날짜와 시간을 확인해주세요.");
            return ResponseEntity.badRequest().body(response);
        }

        jdbcTemplate.update("INSERT INTO grooming_bookings " +
                "(guardian_name, phone, pet_name, pet_type, pet_weight, service, preferred_at, memo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            request.guardianName.trim(), request.phone.trim(), request.petName.trim(), request.petType.trim(),
            emptyToNull(request.petWeight), request.service.trim(), preferredAt, emptyToNull(request.memo));
        response.put("status", "PENDING");
        response.put("message", "예약이 접수됐습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private boolean blank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean tooLong(String value, int max) {
        return value != null && value.length() > max;
    }

    private String emptyToNull(String value) {
        return blank(value) ? null : value.trim();
    }

    public static class GroomingBookingRequest {
        public String guardianName;
        public String phone;
        public String petName;
        public String petType;
        public String petWeight;
        public String service;
        public String preferredDate;
        public String preferredTime;
        public String memo;
    }
}

