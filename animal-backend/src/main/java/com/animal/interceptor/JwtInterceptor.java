package com.animal.interceptor;

import com.animal.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.animal.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT拦截器
 */
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (token == null || token.isEmpty()) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);
            Result<String> result = Result.error(401, "未登录或token已过期");
            response.getWriter().write(new ObjectMapper().writeValueAsString(result));
            return false;
        }

        try {
            Long userId = JwtUtil.getUserIdFromToken(token);
            request.setAttribute("userId", userId);
            return true;
        } catch (Exception e) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);
            Result<String> result = Result.error(401, "token无效或已过期");
            response.getWriter().write(new ObjectMapper().writeValueAsString(result));
            return false;
        }
    }
}

