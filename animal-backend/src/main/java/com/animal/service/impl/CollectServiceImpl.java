package com.animal.service.impl;

import com.animal.entity.Collect;
import com.animal.mapper.CollectMapper;
import com.animal.service.CollectService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 收藏Service实现
 */
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {

    @Override
    public boolean isCollected(Long userId, Long animalId) {
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId)
               .eq(Collect::getAnimalId, animalId);
        return this.count(wrapper) > 0;
    }
}

