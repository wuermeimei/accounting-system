package com.example.accounting.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "账单视图对象")
public class RecordVO {
    @Schema(description = "账单ID", example = "1")
    private Long id;

    @Schema(description = "金额", example = "100.50")
    private BigDecimal amount;

    @Schema(description = "类型：INCOME或EXPENSE", example = "EXPENSE")
    private String type;

    @Schema(description = "分类", example = "餐饮")
    private String category;

    @Schema(description = "描述", example = "午餐")
    private String description;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}