package com.example.accounting.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RecordQueryRequest {
    private String type;

    private String category;

    private LocalDate startDate;

    private LocalDate endDate;
}