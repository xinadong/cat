package com.animal.service;

import com.animal.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户Service
 */
public interface UserService extends IService<User> {
    User login(String username, String password);
    boolean register(User user);
}

