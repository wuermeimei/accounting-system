package com.example.accounting.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "统计视图对象")
public class StatisticVO {
    @Schema(description = "日期", example = "2026-03-12")
    private LocalDate date;

    @Schema(description = "收入", example = "15000.00")
    private BigDecimal income;

    @Schema(description = "支出", example = "8000.00")
    private BigDecimal expense;

    @Schema(description = "结余", example = "7000.00")
    private BigDecimal balance;
}