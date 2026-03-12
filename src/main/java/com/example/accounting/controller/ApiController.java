package com.example.accounting.controller;

import com.example.accounting.dto.LoginRequest;
import com.example.accounting.dto.RegisterRequest;
import com.example.accounting.dto.RecordCreateRequest;
import com.example.accounting.dto.RecordQueryRequest;
import com.example.accounting.dto.RecordUpdateRequest;
import com.example.accounting.entity.User;
import com.example.accounting.service.AuthService;
import com.example.accounting.service.MenuService;
import com.example.accounting.service.RecordService;
import com.example.accounting.service.StatisticsService;
import com.example.accounting.service.UserService;
import com.example.accounting.vo.RecordVO;
import com.example.accounting.vo.StatisticVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class ApiController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private StatisticsService statisticsService;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            User user = userService.findByUsername(username);
            return user != null ? user.getId() : null;
        }
        return null;
    }

    // ========== Auth ==========
    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("username", request.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "注册成功");
        return ResponseEntity.ok(response);
    }

    // ========== Menu ==========
    @GetMapping("/menus/tree")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Map<String, Object>>> getMenuTree() {
        Long userId = getCurrentUserId();
        List<Map<String, Object>> menuTree = menuService.getMenuTreeByUserId(userId);
        return ResponseEntity.ok(menuTree);
    }

    // ========== Record ==========
    @PostMapping("/records")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody RecordCreateRequest request) {
        Long userId = getCurrentUserId();
        Long recordId = recordService.createRecord(userId, request);

        Map<String, Object> response = new HashMap<>();
        response.put("id", recordId);
        response.put("success", true);
        response.put("message", "创建成功");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/records")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<RecordVO>> getRecords(@ModelAttribute RecordQueryRequest request) {
        Long userId = getCurrentUserId();
        List<RecordVO> records = recordService.getRecords(userId, request);
        return ResponseEntity.ok(records);
    }

    @PutMapping("/records/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateRecord(
            @PathVariable Long id,
            @RequestBody RecordUpdateRequest request) {
        Long userId = getCurrentUserId();
        boolean success = recordService.updateRecord(userId, id, request);

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? "更新成功" : "更新失败");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/records/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        boolean success = recordService.deleteRecord(userId, id);

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? "删除成功" : "删除失败");
        return ResponseEntity.ok(response);
    }

    // ========== Statistics ==========
    @GetMapping("/statistics/summary")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, BigDecimal>> getSummary(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        Long userId = getCurrentUserId();

        if (startDate == null) startDate = LocalDate.now().minusMonths(1);
        if (endDate == null) endDate = LocalDate.now();

        BigDecimal income = statisticsService.sumIncome(userId, startDate, endDate);
        BigDecimal expense = statisticsService.sumExpense(userId, startDate, endDate);

        Map<String, BigDecimal> result = new HashMap<>();
        result.put("income", income);
        result.put("expense", expense);
        result.put("balance", income.subtract(expense));

        return ResponseEntity.ok(result);
    }

    @GetMapping("/statistics/category")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getCategoryStatistics(
            @RequestParam String type,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        Long userId = getCurrentUserId();

        if (startDate == null) startDate = LocalDate.now().minusMonths(1);
        if (endDate == null) endDate = LocalDate.now();

        List<Map<String, Object>> result = statisticsService.statisticsByCategory(
                userId, type, startDate, endDate);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/statistics/monthly")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyStatistics(
            @RequestParam(required = false) Integer year) {

        Long userId = getCurrentUserId();

        if (year == null) year = Year.now().getValue();

        List<Map<String, Object>> result = statisticsService.monthlyStatistics(userId, year);
        return ResponseEntity.ok(result);
    }
}