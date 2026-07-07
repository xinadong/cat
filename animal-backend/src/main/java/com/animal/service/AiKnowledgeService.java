package com.animal.service;

import com.animal.entity.AiKnowledge;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * AI知识库Service
 */
public interface AiKnowledgeService extends IService<AiKnowledge> {
    
    /**
     * 根据关键词搜索知识库
     */
    List<AiKnowledge> searchKnowledge(String keyword, int limit);
}

