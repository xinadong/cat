package com.animal.controller.admin;

import com.animal.common.Result;
import com.animal.entity.Article;
import com.animal.service.ArticleService;
import com.animal.utils.FileUploadUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员文章管理控制器
 */
@RestController
@RequestMapping("/api/admin/article")
public class AdminArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 文章列表
     */
    @GetMapping("/list")
    public Result<Page<Article>> getArticleList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        
        Page<Article> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Article::getTitle, keyword);
        }
        
        wrapper.orderByDesc(Article::getCreateTime);
        
        Page<Article> result = articleService.page(page, wrapper);
        return Result.success(result);
    }

    /**
     * 添加文章
     */
    @PostMapping("/add")
    public Result<String> addArticle(@RequestBody Article article) {
        if (article.getStatus() == null) {
            article.setStatus(0); // 默认草稿
        }
        if (article.getViewCount() == null) {
            article.setViewCount(0);
        }
        
        boolean success = articleService.save(article);
        if (success) {
            return Result.success("添加成功");
        } else {
            return Result.error("添加失败");
        }
    }

    /**
     * 更新文章
     */
    @PutMapping("/update")
    public Result<String> updateArticle(@RequestBody Article article) {
        boolean success = articleService.updateById(article);
        if (success) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }

    /**
     * 删除文章
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteArticle(@PathVariable Long id) {
        boolean success = articleService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * 发布文章
     */
    @PutMapping("/publish/{id}")
    public Result<String> publishArticle(@PathVariable Long id) {
        Article article = articleService.getById(id);
        if (article == null) {
            return Result.error("文章不存在");
        }
        
        article.setStatus(1);
        boolean success = articleService.updateById(article);
        if (success) {
            return Result.success("发布成功");
        } else {
            return Result.error("发布失败");
        }
    }

    /**
     * 上传文章封面
     */
    @PostMapping("/upload-cover")
    public Result<Map<String, String>> uploadCover(@RequestParam("file") MultipartFile file) {
        try {
            String url = FileUploadUtil.uploadFile(file);
            Map<String, String> data = new HashMap<>();
            data.put("url", url);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("上传失败：" + e.getMessage());
        }
    }

    /**
     * 获取文章详情
     */
    @GetMapping("/detail/{id}")
    public Result<Article> getArticleDetail(@PathVariable Long id) {
        Article article = articleService.getById(id);
        if (article == null) {
            return Result.error("文章不存在");
        }
        return Result.success(article);
    }
}

