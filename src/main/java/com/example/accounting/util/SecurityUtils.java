package com.example.accounting.util;

import javax.servlet.http.HttpServletRequest;

public class SecurityUtils {

    public static Long getCurrentUserId(HttpServletRequest request) {
        // 这里可以从SecurityContextHolder获取用户信息，简化版本从request attribute获取
        Object userId = request.getAttribute("userId");
        return userId != null ? (Long) userId : null;
    }
}