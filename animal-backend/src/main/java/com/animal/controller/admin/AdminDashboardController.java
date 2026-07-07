package com.animal.controller.admin;

import com.animal.common.Result;
import com.animal.entity.Animal;
import com.animal.entity.Article;
import com.animal.entity.Category;
import com.animal.entity.User;
import com.animal.service.AnimalService;
import com.animal.service.ArticleService;
import com.animal.service.CategoryService;
import com.animal.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 管理员数据看板控制器
 */
@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private AnimalService animalService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 统计数据
     */
    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        Map<String, Object> data = new HashMap<>();
        
        // 用户总数
        long userCount = userService.count();
        data.put("userCount", userCount);
        
        // 动物总数
        long animalCount = animalService.count();
        data.put("animalCount", animalCount);
        
        // 文章总数
        long articleCount = articleService.count();
        data.put("articleCount", articleCount);
        
        // 今日访问量（这里简单统计总浏览量）
        List<Animal> animals = animalService.list();
        int totalViews = animals.stream()
                                 .mapToInt(Animal::getViewCount)
                                 .sum();
        data.put("todayVisit", totalViews);
        
        return Result.success(data);
    }

    /**
     * 热门动物排行
     */
    @GetMapping("/hot-animals")
    public Result<List<Animal>> getHotAnimals() {
        LambdaQueryWrapper<Animal> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Animal::getViewCount)
               .last("LIMIT 10");
        
        List<Animal> list = animalService.list(wrapper);
        return Result.success(list);
    }

    /**
     * 分类统计数据（饼图）
     */
    @GetMapping("/category-stats")
    public Result<List<Map<String, Object>>> getCategoryStats() {
        List<Category> categories = categoryService.list();
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Category category : categories) {
            LambdaQueryWrapper<Animal> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Animal::getCategoryId, category.getId());
            long count = animalService.count(wrapper);
            
            Map<String, Object> item = new HashMap<>();
            item.put("name", category.getName());
            item.put("value", count);
            result.add(item);
        }
        
        return Result.success(result);
    }

    /**
     * 数据趋势统计（折线图）- 最近6个月
     */
    @GetMapping("/trend-stats")
    public Result<Map<String, Object>> getTrendStats() {
        Map<String, Object> result = new HashMap<>();
        List<String> months = new ArrayList<>();
        List<Integer> userCounts = new ArrayList<>();
        List<Integer> animalCounts = new ArrayList<>();
        List<Integer> articleCounts = new ArrayList<>();
        
        // 获取最近6个月的数据
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        
        for (int i = 5; i >= 0; i--) {
            LocalDateTime month = now.minusMonths(i);
            String monthStr = month.format(formatter);
            months.add(monthStr);
            
            LocalDateTime endOfMonth = month.withDayOfMonth(month.toLocalDate().lengthOfMonth())
                                          .withHour(23).withMinute(59).withSecond(59);
            
            // 统计用户数
            LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.le(User::getCreateTime, endOfMonth);
            userCounts.add((int) userService.count(userWrapper));
            
            // 统计动物数
            LambdaQueryWrapper<Animal> animalWrapper = new LambdaQueryWrapper<>();
            animalWrapper.le(Animal::getCreateTime, endOfMonth);
            animalCounts.add((int) animalService.count(animalWrapper));
            
            // 统计文章数
            LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
            articleWrapper.le(Article::getCreateTime, endOfMonth);
            articleCounts.add((int) articleService.count(articleWrapper));
        }
        
        result.put("months", months);
        result.put("userCounts", userCounts);
        result.put("animalCounts", animalCounts);
        result.put("articleCounts", articleCounts);
        
        return Result.success(result);
    }

    /**
     * 用户活跃度统计（环形图）
     */
    @GetMapping("/user-status-stats")
    public Result<List<Map<String, Object>>> getUserStatusStats() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 正常用户
        LambdaQueryWrapper<User> normalWrapper = new LambdaQueryWrapper<>();
        normalWrapper.eq(User::getStatus, 1);
        long normalCount = userService.count(normalWrapper);
        Map<String, Object> normal = new HashMap<>();
        normal.put("name", "正常用户");
        normal.put("value", normalCount);
        result.add(normal);
        
        // 禁用用户
        LambdaQueryWrapper<User> disabledWrapper = new LambdaQueryWrapper<>();
        disabledWrapper.eq(User::getStatus, 0);
        long disabledCount = userService.count(disabledWrapper);
        Map<String, Object> disabled = new HashMap<>();
        disabled.put("name", "禁用用户");
        disabled.put("value", disabledCount);
        result.add(disabled);
        
        return Result.success(result);
    }
}

