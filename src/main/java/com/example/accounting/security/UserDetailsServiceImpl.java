package com.example.accounting.security;

import com.example.accounting.entity.User;
import com.example.accounting.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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

        // 查询用户角色
        List<Long> roleIds = userMapper.selectRoleIdsByUserId(user.getId());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getEnabled() == null || user.getEnabled(),
                true, true, true,
                AuthorityUtils.createAuthorityListFromRoles(
                    roleIds.stream().map(Object::toString).toArray(String[]::new)
                )
        );
    }
}