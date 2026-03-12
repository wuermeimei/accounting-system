package com.example.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Record {
    @TableId(type = IdType.AUTO)
    private Long id;

    private BigDecimal amount;

    private String type; // INCOME, EXPENSE

    private String category;

    private String description;

    private Long userId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted = 0;
}