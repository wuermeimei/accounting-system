package com.example.accounting.service;

import com.example.accounting.dto.UserCreateRequest;
import com.example.accounting.entity.User;
import com.example.accounting.mapper.UserMapper;
import com.example.accounting.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    public boolean existsByUsername(String username) {
        User user = userMapper.selectByUsername(username);
        return user != null && user.getDeleted() == 0;
    }

    public User findByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Transactional
    public void saveUserWithDefaultRole(User user) {
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);

        // 为普通用户分配USER角色（假设角色ID为2，需要在data.sql中初始化）
        userRoleMapper.insertUserRole(user.getId(), 2L);
    }
}