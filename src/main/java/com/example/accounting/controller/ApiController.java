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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "API接口", description = "记账系统主要API接口")
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
    @Operation(summary = "用户登录", description = "用户登录并获取JWT Token")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("username", request.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/register")
    @Operation(summary = "用户注册", description = "注册新用户账号，成功后会分配USER角色")
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
    @Operation(summary = "获取菜单树", description = "根据当前用户权限返回动态菜单树结构")
    public ResponseEntity<List<Map<String, Object>>> getMenuTree() {
        Long userId = getCurrentUserId();
        List<Map<String, Object>> menuTree = menuService.getMenuTreeByUserId(userId);
        return ResponseEntity.ok(menuTree);
    }

    // ========== Record ==========
    @PostMapping("/records")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "创建账单", description = "创建一条新的收入或支出记录")
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
    @Operation(summary = "查询账单列表", description = "分页查询当前用户的账单记录，支持按类型、分类、时间范围筛选")
    public ResponseEntity<List<RecordVO>> getRecords(@ModelAttribute RecordQueryRequest request) {
        Long userId = getCurrentUserId();
        List<RecordVO> records = recordService.getRecords(userId, request);
        return ResponseEntity.ok(records);
    }

    @PutMapping("/records/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "更新账单", description = "修改指定ID的账单记录", parameters = {
            @Parameter(name = "id", description = "账单ID", required = true)
    })
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
    @Operation(summary = "删除账单", description = "逻辑删除指定ID的账单记录", parameters = {
            @Parameter(name = "id", description = "账单ID", required = true)
    })
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
    @Operation(summary = "汇总统计", description = "统计指定时间范围的收入、支出和结余",
            parameters = {
                    @Parameter(name = "startDate", description = "开始日期 (yyyy-MM-dd)，默认前30天"),
                    @Parameter(name = "endDate", description = "结束日期 (yyyy-MM-dd)，默认今天")
            })
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
    @Operation(summary = "按分类统计", description = "统计指定类型、时间范围内各分类的金额汇总",
            parameters = {
                    @Parameter(name = "type", description = "类型: INCOME或EXPENSE", required = true),
                    @Parameter(name = "startDate", description = "开始日期，默认前30天"),
                    @Parameter(name = "endDate", description = "结束日期，默认今天")
            })
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
    @Operation(summary = "月度统计", description = "统计指定年份每个月的收支金额",
            parameters = {
                    @Parameter(name = "year", description = "年份，默认当前年份")
            })
    public ResponseEntity<List<Map<String, Object>>> getMonthlyStatistics(
            @RequestParam(required = false) Integer year) {

        Long userId = getCurrentUserId();

        if (year == null) year = Year.now().getValue();

        List<Map<String, Object>> result = statisticsService.monthlyStatistics(userId, year);
        return ResponseEntity.ok(result);
    }
}