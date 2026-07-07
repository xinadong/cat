package com.animal.controller.user;

import com.animal.common.Result;
import com.animal.entity.Article;
import com.animal.service.ArticleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章控制器
 */
@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 文章列表
     */
    @GetMapping("/list")
    public Result<Page<Article>> getArticleList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String category) {
        
        Page<Article> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus, 1);
        
        if (category != null && !category.isEmpty()) {
            wrapper.eq(Article::getCategory, category);
        }
        
        wrapper.orderByDesc(Article::getCreateTime);
        
        Page<Article> result = articleService.page(page, wrapper);
        return Result.success(result);
    }

    /**
     * 文章详情
     */
    @GetMapping("/detail/{id}")
    public Result<Article> getArticleDetail(@PathVariable Long id) {
        Article article = articleService.getById(id);
        if (article == null) {
            return Result.error("文章不存在");
        }
        
        // 增加浏览量
        articleService.incrementViewCount(id);
        
        // 重新查询以获取更新后的浏览量
        article = articleService.getById(id);
        return Result.success(article);
    }
}

