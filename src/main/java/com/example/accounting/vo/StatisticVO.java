package com.example.accounting.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class StatisticVO {
    private LocalDate date;

    private BigDecimal income;

    private BigDecimal expense;

    private BigDecimal balance;
}