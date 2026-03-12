package com.example.accounting.service;

import com.example.accounting.mapper.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {

    @Autowired
    private RecordMapper recordMapper;

    public BigDecimal sumIncome(Long userId, LocalDate startDate, LocalDate endDate) {
        return recordMapper.sumAmountByUserIdAndTypeAndDateRange(userId, "INCOME", startDate, endDate);
    }

    public BigDecimal sumExpense(Long userId, LocalDate startDate, LocalDate endDate) {
        return recordMapper.sumAmountByUserIdAndTypeAndDateRange(userId, "EXPENSE", startDate, endDate);
    }

    public List<Map<String, Object>> statisticsByCategory(Long userId, String type, LocalDate startDate, LocalDate endDate) {
        return recordMapper.statisticsByCategory(userId, type, startDate, endDate);
    }

    public List<Map<String, Object>> monthlyStatistics(Long userId, Integer year) {
        return recordMapper.monthlyStatistics(userId, year);
    }
}