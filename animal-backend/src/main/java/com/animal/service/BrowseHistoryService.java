package com.animal.service;

import com.animal.entity.BrowseHistory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 浏览历史Service
 */
public interface BrowseHistoryService extends IService<BrowseHistory> {
    void addHistory(Long userId, Long animalId);
}

