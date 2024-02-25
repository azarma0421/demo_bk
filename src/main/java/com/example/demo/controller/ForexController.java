package com.example.demo.controller;

import com.example.demo.dto.DatesRequest;
import com.example.demo.service.ForexService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ForexController {

    @Autowired
    public ForexService forexService;

    @Autowired
    public ObjectMapper objectMapper;
    @PostMapping("/processHistoryDates")
    public ResponseEntity<Map<String, Object>> processDate(
            @RequestBody DatesRequest datesRequest
    ) {

        LocalDate startDate = datesRequest.getStartDate();
        LocalDate endDate = datesRequest.getEndDate();

        try {
            String forexData = forexService.getForexData(startDate, endDate);

            return ResponseEntity.ok()
                    .body(Map.of(
                            "error", Map.of(
                                    "code", "0000",
                                    "message", "Success."
                            ),
                            "currency", forexData
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", Map.of(
                                    "code", "E001",
                                    "message", "Date range mismatch."

                            )
                    ));
        }
    }
}

