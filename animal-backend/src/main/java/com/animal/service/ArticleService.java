package com.animal.service;

import com.animal.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 文章Service
 */
public interface ArticleService extends IService<Article> {
    void incrementViewCount(Long id);
}

