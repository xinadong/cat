package com.animal.service.impl;

import com.animal.entity.SysAdmin;
import com.animal.mapper.SysAdminMapper;
import com.animal.service.SysAdminService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 管理员Service实现
 */
@Service
public class SysAdminServiceImpl extends ServiceImpl<SysAdminMapper, SysAdmin> implements SysAdminService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public SysAdmin login(String username, String password) {
        LambdaQueryWrapper<SysAdmin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysAdmin::getUsername, username);
        SysAdmin admin = this.getOne(wrapper);
        
        if (admin == null) {
            return null;
        }
        
        if (admin.getStatus() == 0) {
            return null;
        }
        
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            return null;
        }
        
        return admin;
    }
}

