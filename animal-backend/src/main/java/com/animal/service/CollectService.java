package com.animal.service;

import com.animal.entity.Collect;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 收藏Service
 */
public interface CollectService extends IService<Collect> {
    boolean isCollected(Long userId, Long animalId);
}

