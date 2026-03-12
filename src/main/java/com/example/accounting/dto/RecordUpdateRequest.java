package com.example.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@Schema(description = "更新账单请求")
public class RecordUpdateRequest {
    @NotNull(message = "金额不能为空")
    @Positive(message = "金额必须大于0")
    @Schema(description = "金额", required = true, example = "150.00")
    private BigDecimal amount;

    @NotBlank(message = "类型不能为空")
    @Schema(description = "类型：INCOME(收入)或EXPENSE(支出)", required = true, example = "EXPENSE")
    private String type;

    @NotBlank(message = "分类不能为空")
    @Schema(description = "账单分类", required = true, example = "交通")
    private String category;

    @Schema(description = "描述", example = "打车")
    private String description;
}