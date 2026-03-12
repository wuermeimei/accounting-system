package com.example.accounting.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_roles")
public class UserRole {
    @TableId
    private Long id;

    private Long userId;

    private Long roleId;

    @TableLogic
    private Integer deleted = 0;

    private LocalDateTime createTime;
}