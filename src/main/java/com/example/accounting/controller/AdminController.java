package com.example.accounting.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class AdminController {

    // ========== Role & Menu (简化版，仅返回示例) ==========
    @GetMapping("/admin/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAllRoles() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "角色管理功能待完善");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/menus")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAllMenus() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "菜单管理功能待完善");
        return ResponseEntity.ok(response);
    }
}