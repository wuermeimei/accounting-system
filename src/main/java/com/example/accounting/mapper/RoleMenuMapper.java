package com.example.accounting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.accounting.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    void deleteRoleMenus(@Param("roleId") Long roleId);
}