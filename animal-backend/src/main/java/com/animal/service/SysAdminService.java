package com.animal.service;

import com.animal.entity.SysAdmin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 管理员Service
 */
public interface SysAdminService extends IService<SysAdmin> {
    SysAdmin login(String username, String password);
}

