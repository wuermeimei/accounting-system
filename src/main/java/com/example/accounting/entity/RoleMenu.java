package com.example.accounting.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoleMenu {
    @TableId
    private Long id;

    private Long roleId;

    private Long menuId;

    @TableLogic
    private Integer deleted = 0;

    private LocalDateTime createTime;
}