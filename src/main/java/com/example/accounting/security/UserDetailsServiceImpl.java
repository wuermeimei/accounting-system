package com.example.accounting.security;

import com.example.accounting.entity.User;
import com.example.accounting.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectByUsername(username);
        if (user == null || user.getDeleted() == 1) {
            throw new UsernameNotFoundException("用户不存在或被禁用");
        }

        // 查询用户角色代码 (如: ADMIN, USER)
        List<String> roleCodes = userMapper.selectRoleCodesByUserId(user.getId());

        // 转换为 Spring Security 的 Authority (自动添加 "ROLE_" 前缀以匹配 @PreAuthorize("hasRole('ADMIN')"))
        List<SimpleGrantedAuthority> authorities = roleCodes.stream()
                .map(roleCode -> "ROLE_" + roleCode)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getEnabled() == null || user.getEnabled(),
                true, true, true,
                authorities
        );
    }
}