package com.animal.controller.user;

import com.animal.common.Result;
import com.animal.entity.Animal;
import com.animal.service.AnimalService;
import com.animal.service.BrowseHistoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 动物控制器
 */
@RestController
@RequestMapping("/api/animal")
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    @Autowired
    private BrowseHistoryService browseHistoryService;

    /**
     * 动物列表（分页）
     */
    @GetMapping("/list")
    public Result<Page<Animal>> getAnimalList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "12") Integer pageSize,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {
        
        Page<Animal> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Animal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Animal::getStatus, 1);
        
        if (categoryId != null) {
            wrapper.eq(Animal::getCategoryId, categoryId);
        }
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Animal::getName, keyword)
                             .or()
                             .like(Animal::getScientificName, keyword));
        }
        
        wrapper.orderByDesc(Animal::getCreateTime);
        
        Page<Animal> result = animalService.page(page, wrapper);
        return Result.success(result);
    }

    /**
     * 动物详情
     */
    @GetMapping("/detail/{id}")
    public Result<Animal> getAnimalDetail(@PathVariable Long id, HttpServletRequest request) {
        Animal animal = animalService.getById(id);
        if (animal == null) {
            return Result.error("动物不存在");
        }
        
        // 增加浏览量
        animalService.incrementViewCount(id);
        
        // 记录浏览历史（如果已登录）
        Long userId = (Long) request.getAttribute("userId");
        if (userId != null) {
            browseHistoryService.addHistory(userId, id);
        }
        
        // 重新查询以获取更新后的浏览量
        animal = animalService.getById(id);
        return Result.success(animal);
    }

    /**
     * 相关动物推荐
     */
    @GetMapping("/recommend")
    public Result<List<Animal>> getRecommendAnimals(@RequestParam Long categoryId, 
                                                     @RequestParam Long currentId) {
        LambdaQueryWrapper<Animal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Animal::getStatus, 1)
               .eq(Animal::getCategoryId, categoryId)
               .ne(Animal::getId, currentId)
               .orderByDesc(Animal::getViewCount)
               .last("LIMIT 4");
        
        List<Animal> list = animalService.list(wrapper);
        return Result.success(list);
    }
}

