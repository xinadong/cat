package com.animal.controller.user;

import com.animal.common.Result;
import com.animal.entity.Animal;
import com.animal.entity.Collect;
import com.animal.service.AnimalService;
import com.animal.service.CollectService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收藏控制器
 */
@RestController
@RequestMapping("/api/collect")
public class CollectController {

    @Autowired
    private CollectService collectService;

    @Autowired
    private AnimalService animalService;

    /**
     * 添加收藏
     */
    @PostMapping("/add")
    public Result<String> addCollect(@RequestBody Map<String, Long> params, 
                                     HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Long animalId = params.get("animalId");
        
        if (collectService.isCollected(userId, animalId)) {
            return Result.error("已经收藏过了");
        }
        
        Collect collect = new Collect();
        collect.setUserId(userId);
        collect.setAnimalId(animalId);
        
        boolean success = collectService.save(collect);
        if (success) {
            // 更新动物收藏数
            Animal animal = animalService.getById(animalId);
            animal.setCollectCount(animal.getCollectCount() + 1);
            animalService.updateById(animal);
            return Result.success("收藏成功");
        } else {
            return Result.error("收藏失败");
        }
    }

    /**
     * 取消收藏
     */
    @DeleteMapping("/delete/{animalId}")
    public Result<String> deleteCollect(@PathVariable Long animalId, 
                                        HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId)
               .eq(Collect::getAnimalId, animalId);
        
        boolean success = collectService.remove(wrapper);
        if (success) {
            // 更新动物收藏数
            Animal animal = animalService.getById(animalId);
            animal.setCollectCount(Math.max(0, animal.getCollectCount() - 1));
            animalService.updateById(animal);
            return Result.success("取消收藏成功");
        } else {
            return Result.error("取消收藏失败");
        }
    }

    /**
     * 我的收藏列表
     */
    @GetMapping("/list")
    public Result<List<Animal>> getCollectList(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId)
               .orderByDesc(Collect::getCreateTime);
        
        List<Collect> collects = collectService.list(wrapper);
        
        List<Animal> animals = new ArrayList<>();
        for (Collect collect : collects) {
            Animal animal = animalService.getById(collect.getAnimalId());
            if (animal != null) {
                animals.add(animal);
            }
        }
        
        return Result.success(animals);
    }

    /**
     * 检查是否已收藏
     */
    @GetMapping("/check/{animalId}")
    public Result<Map<String, Boolean>> checkCollect(@PathVariable Long animalId, 
                                                      HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        boolean isCollected = collectService.isCollected(userId, animalId);
        
        Map<String, Boolean> data = new HashMap<>();
        data.put("isCollected", isCollected);
        return Result.success(data);
    }
}

