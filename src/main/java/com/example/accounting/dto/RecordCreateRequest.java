package com.example.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "创建账单请求")
public class RecordCreateRequest {
    @NotNull(message = "金额不能为空")
    @Positive(message = "金额必须大于0")
    @Schema(description = "金额", required = true, example = "100.50")
    private BigDecimal amount;

    @NotBlank(message = "类型不能为空")
    @Schema(description = "类型：INCOME(收入)或EXPENSE(支出)", required = true, example = "EXPENSE")
    private String type;

    @NotBlank(message = "分类不能为空")
    @Schema(description = "账单分类", required = true, example = "餐饮")
    private String category;

    @Schema(description = "描述", example = "午餐")
    private String description;

    @Schema(description = "创建日期（可选，默认为当前日期）", example = "2026-03-12")
    private LocalDate createDate;
}