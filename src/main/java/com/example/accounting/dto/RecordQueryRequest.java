package com.example.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "查询账单请求")
public class RecordQueryRequest {
    @Schema(description = "类型：INCOME或EXPENSE", example = "EXPENSE")
    private String type;

    @Schema(description = "分类", example = "餐饮")
    private String category;

    @Schema(description = "开始日期", example = "2026-03-01")
    private LocalDate startDate;

    @Schema(description = "结束日期", example = "2026-03-12")
    private LocalDate endDate;
}