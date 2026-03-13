package com.example.accounting.service;

import com.example.accounting.entity.Menu;
import com.example.accounting.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMenuTreeByUserId(Long userId) {
        List<Menu> menus = menuMapper.selectMenuTreeByUserId(userId);
        return buildTree(menus, 0L);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAllMenus() {
        List<Menu> menus = menuMapper.selectAllMenus();
        return buildTree(menus, 0L);
    }

    private List<Map<String, Object>> buildTree(List<Menu> menus, Long parentId) {
        return menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .sorted(Comparator.comparingInt(Menu::getSortOrder))
                .map(menu -> {
                    Map<String, Object> node = new java.util.HashMap<>();
                    node.put("id", menu.getId());
                    node.put("menuName", menu.getMenuName());
                    node.put("parentId", menu.getParentId());
                    node.put("path", menu.getPath());
                    node.put("component", menu.getComponent());
                    node.put("icon", menu.getIcon());
                    node.put("sortOrder", menu.getSortOrder());
                    node.put("permission", menu.getPermission());
                    node.put("type", menu.getType());
                    node.put("status", menu.getStatus());
                    node.put("children", buildTree(menus, menu.getId()));
                    return node;
                })
                .collect(Collectors.toList());
    }
}