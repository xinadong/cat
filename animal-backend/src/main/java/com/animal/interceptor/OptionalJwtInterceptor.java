package com.animal.interceptor;

import com.animal.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 可选的JWT拦截器 - 如果有token就解析，没有也不拦截
 */
public class OptionalJwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 如果有token，尝试解析；没有token也放行
        if (token != null && !token.isEmpty()) {
            try {
                Long userId = JwtUtil.getUserIdFromToken(token);
                request.setAttribute("userId", userId);
            } catch (Exception e) {
                // token无效，但不拦截请求
                System.out.println("Token解析失败，但允许继续访问: " + e.getMessage());
            }
        }

        return true;
    }
}

