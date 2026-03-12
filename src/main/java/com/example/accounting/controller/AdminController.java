package com.example.accounting.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
@io.swagger.v3.oas.annotations.tags.Tag(name = "管理员接口", description = "系统管理相关接口，仅ADMIN角色可访问")
public class AdminController {

    @GetMapping("/admin/roles")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "获取所有角色", description = "查询系统中所有角色列表（功能待完善）")
    public ResponseEntity<Map<String, Object>> getAllRoles() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "角色管理功能待完善");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/menus")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "获取所有菜单", description = "查询系统中所有菜单列表（功能待完善）")
    public ResponseEntity<Map<String, Object>> getAllMenus() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "菜单管理功能待完善");
        return ResponseEntity.ok(response);
    }
}