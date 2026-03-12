package com.example.accounting.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.accounting.entity.Role;
import com.example.accounting.entity.RoleMenu;
import com.example.accounting.mapper.RoleMapper;
import com.example.accounting.mapper.RoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    public List<Role> getAllRoles() {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0);
        return roleMapper.selectList(queryWrapper);
    }

    public Role getRoleById(Long roleId) {
        return roleMapper.selectById(roleId);
    }

    @Transactional
    public void assignMenusToRole(Long roleId, List<Long> menuIds) {
        // 删除原有菜单权限
        roleMenuMapper.deleteRoleMenus(roleId);

        // 批量插入新菜单权限
        LocalDateTime now = LocalDateTime.now();
        menuIds.forEach(menuId -> {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenu.setCreateTime(now);
            roleMenuMapper.insert(roleMenu);
        });
    }

    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return roleMapper.selectMenuIdsByRoleId(roleId);
    }
}