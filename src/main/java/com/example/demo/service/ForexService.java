package com.example.demo.service;

import com.example.demo.entity.ForexEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class ForexService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    public void saveForexData(String date, String usdToNtd) {
        ForexEntity forexEntity = new ForexEntity();
        forexEntity.setDate(date);
        forexEntity.setUsdToNtd(usdToNtd);

        String sql = "INSERT INTO forex (date, `USD/NTD`) VALUES (?, ?)";
        int rowsAffected = entityManager.createNativeQuery(sql)
                .setParameter(1, date)
                .setParameter(2, usdToNtd)
                .executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("寫入成功, 行數:" + rowsAffected);
        } else {
            System.out.println("沒有任何影響");
        }
    }

    public String getForexData(LocalDate startDate, LocalDate endDate) throws JsonProcessingException {

        LocalDate oneYearAgo = LocalDate.now().minus(Period.ofYears(1));
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Date range mismatch.");
        } else if (endDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date range mismatch.");
        } else if (startDate.isBefore(oneYearAgo)) {
            throw new IllegalArgumentException("Date range mismatch.");
        }

        String sql = "SELECT date,`USD/NTD` usd FROM forex WHERE date BETWEEN ? AND ?";
        Query query = entityManager.createNativeQuery(sql)
                .setParameter(1, startDate)
                .setParameter(2, endDate);

        List<Object[]> resultList = query.getResultList();
        String jsonResult = null;
        try {
            jsonResult = objectMapper.writeValueAsString(resultList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonResult;
    }
}

