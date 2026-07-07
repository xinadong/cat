package com.animal.controller.admin;

import com.animal.common.Result;
import com.animal.entity.Animal;
import com.animal.entity.User;
import com.animal.service.AnimalService;
import com.animal.service.CollectService;
import com.animal.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员数据统计控制器
 */
@RestController
@RequestMapping("/api/admin/stats")
public class AdminStatsController {

    @Autowired
    private AnimalService animalService;

    @Autowired
    private UserService userService;

    @Autowired
    private CollectService collectService;

    /**
     * 访问量统计
     */
    @GetMapping("/visit")
    public Result<Map<String, Object>> getVisitStats() {
        Map<String, Object> data = new HashMap<>();
        
        List<Animal> animals = animalService.list();
        int totalViews = animals.stream()
                                .mapToInt(Animal::getViewCount)
                                .sum();
        
        data.put("totalViews", totalViews);
        data.put("animalCount", animals.size());
        data.put("avgViews", animals.isEmpty() ? 0 : totalViews / animals.size());
        
        return Result.success(data);
    }

    /**
     * 用户活跃度统计
     */
    @GetMapping("/user-active")
    public Result<Map<String, Object>> getUserActiveStats() {
        Map<String, Object> data = new HashMap<>();
        
        long totalUsers = userService.count();
        
        LambdaQueryWrapper<User> activeWrapper = new LambdaQueryWrapper<>();
        activeWrapper.eq(User::getStatus, 1);
        long activeUsers = userService.count(activeWrapper);
        
        data.put("totalUsers", totalUsers);
        data.put("activeUsers", activeUsers);
        data.put("inactiveUsers", totalUsers - activeUsers);
        
        return Result.success(data);
    }

    /**
     * 动物浏览排行
     */
    @GetMapping("/animal-rank")
    public Result<List<Animal>> getAnimalRank() {
        LambdaQueryWrapper<Animal> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Animal::getViewCount)
               .last("LIMIT 20");
        
        List<Animal> list = animalService.list(wrapper);
        return Result.success(list);
    }

    /**
     * 收藏排行
     */
    @GetMapping("/collect-rank")
    public Result<List<Animal>> getCollectRank() {
        LambdaQueryWrapper<Animal> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Animal::getCollectCount)
               .last("LIMIT 20");
        
        List<Animal> list = animalService.list(wrapper);
        return Result.success(list);
    }
}

