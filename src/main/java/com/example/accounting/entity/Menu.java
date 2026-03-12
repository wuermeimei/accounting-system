package com.example.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Menu {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String menuName;

    private Long parentId = 0L;

    private String path;

    private String component;

    private String icon;

    private Integer sortOrder = 0;

    private String permission;

    private String type; // MENU, BUTTON

    private Integer status = 0;

    @TableLogic
    private Integer deleted = 0;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}