package com.animal.config;

import com.animal.interceptor.JwtInterceptor;
import com.animal.interceptor.OptionalJwtInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-path}")
    private String uploadPath;

    @Value("${file.access-path}")
    private String accessPath;

    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }

    @Bean
    public OptionalJwtInterceptor optionalJwtInterceptor() {
        return new OptionalJwtInterceptor();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(accessPath)
                .addResourceLocations("file:" + uploadPath);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 可选JWT拦截器 - 用于需要可选登录的接口（如动物详情）
        registry.addInterceptor(optionalJwtInterceptor())
                .addPathPatterns(
                        "/api/animal/detail/**",
                        "/api/article/detail/**",
                        "/api/question/detail/**"
                )
                .order(1);

        // 必需JWT拦截器 - 用于必须登录的接口
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/user/register",
                        "/api/user/login",
                        "/api/admin/login",
                        "/api/home/**",
                        "/api/animal/list",
                        "/api/animal/detail/**",
                        "/api/animal/recommend",
                        "/api/article/list",
                        "/api/article/detail/**",
                        "/api/question/list",
                        "/api/question/detail/**",
                        "/api/comment/list",
                        "/uploads/**"
                )
                .order(2);
    }
}

