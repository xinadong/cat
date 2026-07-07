package com.animal.service.impl;

import com.animal.entity.Animal;
import com.animal.mapper.AnimalMapper;
import com.animal.service.AnimalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 动物Service实现
 */
@Service
public class AnimalServiceImpl extends ServiceImpl<AnimalMapper, Animal> implements AnimalService {

    @Override
    public void incrementViewCount(Long id) {
        Animal animal = this.getById(id);
        if (animal != null) {
            animal.setViewCount(animal.getViewCount() + 1);
            this.updateById(animal);
        }
    }
}

