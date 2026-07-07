package com.animal.service.impl;

import com.animal.entity.BrowseHistory;
import com.animal.mapper.BrowseHistoryMapper;
import com.animal.service.BrowseHistoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 浏览历史Service实现
 */
@Service
public class BrowseHistoryServiceImpl extends ServiceImpl<BrowseHistoryMapper, BrowseHistory> implements BrowseHistoryService {

    @Override
    public void addHistory(Long userId, Long animalId) {
        // 查询是否已存在该用户对该动物的浏览记录
        LambdaQueryWrapper<BrowseHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BrowseHistory::getUserId, userId)
               .eq(BrowseHistory::getAnimalId, animalId);
        
        BrowseHistory existingHistory = this.getOne(wrapper);
        
        if (existingHistory != null) {
            // 如果已存在，只更新浏览时间
            existingHistory.setBrowseTime(LocalDateTime.now());
            this.updateById(existingHistory);
        } else {
            // 如果不存在，创建新记录
            BrowseHistory history = new BrowseHistory();
            history.setUserId(userId);
            history.setAnimalId(animalId);
            this.save(history);
        }
    }
}

