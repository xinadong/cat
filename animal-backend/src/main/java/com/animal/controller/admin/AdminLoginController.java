package com.animal.controller.admin;

import com.animal.common.Result;
import com.animal.entity.SysAdmin;
import com.animal.service.SysAdminService;
import com.animal.utils.FileUploadUtil;
import com.animal.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员登录控制器
 */
@RestController
@RequestMapping("/api/admin")
public class AdminLoginController {

    @Autowired
    private SysAdminService sysAdminService;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody SysAdmin admin) {
        if (admin.getUsername() == null || admin.getUsername().trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (admin.getPassword() == null || admin.getPassword().trim().isEmpty()) {
            return Result.error("密码不能为空");
        }
        
        SysAdmin loginAdmin = sysAdminService.login(admin.getUsername(), admin.getPassword());
        if (loginAdmin == null) {
            return Result.error("用户名或密码错误，或账号已被禁用");
        }
        
        String token = JwtUtil.generateToken(loginAdmin.getId(), loginAdmin.getUsername());
        
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("admin", loginAdmin);
        
        return Result.success(data);
    }

    /**
     * 获取管理员信息
     */
    @GetMapping("/info")
    public Result<SysAdmin> getAdminInfo(HttpServletRequest request) {
        Long adminId = (Long) request.getAttribute("userId");
        SysAdmin admin = sysAdminService.getById(adminId);
        return Result.success(admin);
    }

    /**
     * 更新管理员信息
     */
    @PutMapping("/update")
    public Result<String> updateAdmin(@RequestBody SysAdmin admin, HttpServletRequest request) {
        Long adminId = (Long) request.getAttribute("userId");
        admin.setId(adminId);
        admin.setPassword(null); // 不允许通过此接口修改密码
        admin.setUsername(null); // 不允许修改用户名
        
        boolean success = sysAdminService.updateById(admin);
        if (success) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }

    /**
     * 上传管理员头像
     */
    @PostMapping("/upload-avatar")
    public Result<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            String url = FileUploadUtil.uploadFile(file);
            Map<String, String> data = new HashMap<>();
            data.put("url", url);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("上传失败：" + e.getMessage());
        }
    }
}

