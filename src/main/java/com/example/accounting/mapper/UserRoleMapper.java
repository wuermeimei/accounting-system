package com.example.accounting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.accounting.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    void insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    void deleteUserRoles(@Param("userId") Long userId);
}