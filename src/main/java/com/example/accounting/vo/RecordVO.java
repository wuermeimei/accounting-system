package com.example.accounting.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RecordVO {
    private Long id;
    private BigDecimal amount;
    private String type;
    private String category;
    private String description;
    private Long userId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}