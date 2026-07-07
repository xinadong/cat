package com.animal.controller.user;

import com.animal.common.Result;
import com.animal.entity.Animal;
import com.animal.entity.Banner;
import com.animal.entity.Category;
import com.animal.service.AnimalService;
import com.animal.service.BannerService;
import com.animal.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页控制器
 */
@RestController
@RequestMapping("/api/home")
public class HomeController {

    @Autowired
    private BannerService bannerService;

    @Autowired
    private AnimalService animalService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取轮播图
     */
    @GetMapping("/banner")
    public Result<List<Banner>> getBanner() {
        LambdaQueryWrapper<Banner> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Banner::getStatus, 1)
               .orderByAsc(Banner::getSort);
        List<Banner> list = bannerService.list(wrapper);
        return Result.success(list);
    }

    /**
     * 获取热门动物
     */
    @GetMapping("/hot-animals")
    public Result<List<Animal>> getHotAnimals() {
        LambdaQueryWrapper<Animal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Animal::getStatus, 1)
               .orderByDesc(Animal::getViewCount)
               .last("LIMIT 8");
        List<Animal> list = animalService.list(wrapper);
        return Result.success(list);
    }

    /**
     * 获取最新动物
     */
    @GetMapping("/new-animals")
    public Result<List<Animal>> getNewAnimals() {
        LambdaQueryWrapper<Animal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Animal::getStatus, 1)
               .orderByDesc(Animal::getCreateTime)
               .last("LIMIT 8");
        List<Animal> list = animalService.list(wrapper);
        return Result.success(list);
    }

    /**
     * 获取分类列表
     */
    @GetMapping("/categories")
    public Result<List<Category>> getCategories() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);
        List<Category> list = categoryService.list(wrapper);
        return Result.success(list);
    }

    /**
     * 首页数据（一次性获取所有数据）
     */
    @GetMapping("/data")
    public Result<Map<String, Object>> getHomeData() {
        Map<String, Object> data = new HashMap<>();
        
        // 轮播图
        LambdaQueryWrapper<Banner> bannerWrapper = new LambdaQueryWrapper<>();
        bannerWrapper.eq(Banner::getStatus, 1).orderByAsc(Banner::getSort);
        data.put("banners", bannerService.list(bannerWrapper));
        
        // 热门动物
        LambdaQueryWrapper<Animal> hotWrapper = new LambdaQueryWrapper<>();
        hotWrapper.eq(Animal::getStatus, 1).orderByDesc(Animal::getViewCount).last("LIMIT 8");
        data.put("hotAnimals", animalService.list(hotWrapper));
        
        // 最新动物
        LambdaQueryWrapper<Animal> newWrapper = new LambdaQueryWrapper<>();
        newWrapper.eq(Animal::getStatus, 1).orderByDesc(Animal::getCreateTime).last("LIMIT 8");
        data.put("newAnimals", animalService.list(newWrapper));
        
        // 分类
        LambdaQueryWrapper<Category> categoryWrapper = new LambdaQueryWrapper<>();
        categoryWrapper.orderByAsc(Category::getSort);
        data.put("categories", categoryService.list(categoryWrapper));
        
        return Result.success(data);
    }
}

