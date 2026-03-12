package com.example.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Role {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String code;

    private String description;

    @TableLogic
    private Integer deleted = 0;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}