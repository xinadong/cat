package com.animal.service.impl;

import com.animal.entity.User;
import com.animal.mapper.UserMapper;
import com.animal.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户Service实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User login(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = this.getOne(wrapper);
        
        if (user == null) {
            return null;
        }
        
        if (user.getStatus() == 0) {
            return null;
        }
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }
        
        return user;
    }

    @Override
    public boolean register(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        if (this.count(wrapper) > 0) {
            return false;
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(1);
        if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
            user.setAvatar("https://pic57.photophoto.cn/20201125/0012024436462829_b.jpg");
        }
        
        return this.save(user);
    }
}

