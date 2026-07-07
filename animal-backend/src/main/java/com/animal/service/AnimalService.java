package com.animal.service;

import com.animal.entity.Animal;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 动物Service
 */
public interface AnimalService extends IService<Animal> {
    void incrementViewCount(Long id);
}

