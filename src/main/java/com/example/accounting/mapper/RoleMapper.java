package com.example.accounting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.accounting.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    List<Long> selectMenuIdsByRoleId(Long roleId);

    void insertRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

    void deleteRoleMenus(@Param("roleId") Long roleId);
}