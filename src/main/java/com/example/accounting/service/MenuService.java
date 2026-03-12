package com.example.accounting.service;

import com.example.accounting.entity.Menu;
import com.example.accounting.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Autowired
    private MenuMapper menuMapper;

    public List<Menu> getMenuTreeByUserId(Long userId) {
        List<Menu> menus = menuMapper.selectMenuTreeByUserId(userId);
        return buildTree(menus, 0L);
    }

    public List<Menu> getAllMenus() {
        List<Menu> menus = menuMapper.selectAllMenus();
        return buildTree(menus, 0L);
    }

    private List<Menu> buildTree(List<Menu> menus, Long parentId) {
        return menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .sorted(Comparator.comparingInt(Menu::getSortOrder))
                .peek(menu -> menu.setChildren(buildTree(menus, menu.getId())))
                .collect(Collectors.toList());
    }
}