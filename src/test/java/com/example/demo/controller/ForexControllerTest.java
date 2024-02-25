package com.example.demo.controller;

import com.example.demo.dto.DatesRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ForexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testProcessDate1() throws Exception {
        DatesRequest datesRequest = new DatesRequest();
        datesRequest.setStartDate(LocalDate.parse("2024-02-22"));
        datesRequest.setEndDate(LocalDate.parse("2024-02-23"));

        String expectedJson = objectMapper.writeValueAsString(Map.of(
                "error", Map.of(
                        "code", "0000",
                        "message", "Success."
                ),
                "currency", "[[\"2024-02-22\",\"31.45\"],[\"2024-02-23\",\"31.555\"]]"
        ));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/processHistoryDates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datesRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));

    }

    @Test
    public void testProcessDate2() throws Exception {
        DatesRequest datesRequest = new DatesRequest();
        datesRequest.setStartDate(LocalDate.parse("2022-02-22"));
        datesRequest.setEndDate(LocalDate.parse("2024-02-23"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/processHistoryDates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datesRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{'error': {'code': 'E001','message': 'Date range mismatch.'}}"));
    }
}