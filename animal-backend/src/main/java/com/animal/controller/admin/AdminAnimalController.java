package com.animal.controller.admin;

import com.animal.common.Result;
import com.animal.entity.Animal;
import com.animal.service.AnimalService;
import com.animal.utils.FileUploadUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员动物管理控制器
 */
@RestController
@RequestMapping("/api/admin/animal")
public class AdminAnimalController {

    @Autowired
    private AnimalService animalService;

    /**
     * 动物列表
     */
    @GetMapping("/list")
    public Result<Page<Animal>> getAnimalList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        
        Page<Animal> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Animal> wrapper = new LambdaQueryWrapper<>();
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Animal::getName, keyword)
                   .or()
                   .like(Animal::getScientificName, keyword);
        }
        
        wrapper.orderByDesc(Animal::getCreateTime);
        
        Page<Animal> result = animalService.page(page, wrapper);
        return Result.success(result);
    }

    /**
     * 添加动物
     */
    @PostMapping("/add")
    public Result<String> addAnimal(@RequestBody Animal animal) {
        if (animal.getStatus() == null) {
            animal.setStatus(1);
        }
        if (animal.getViewCount() == null) {
            animal.setViewCount(0);
        }
        if (animal.getCollectCount() == null) {
            animal.setCollectCount(0);
        }
        
        boolean success = animalService.save(animal);
        if (success) {
            return Result.success("添加成功");
        } else {
            return Result.error("添加失败");
        }
    }

    /**
     * 更新动物
     */
    @PutMapping("/update")
    public Result<String> updateAnimal(@RequestBody Animal animal) {
        boolean success = animalService.updateById(animal);
        if (success) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }

    /**
     * 删除动物
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteAnimal(@PathVariable Long id) {
        boolean success = animalService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * 上传图片
     */
    @PostMapping("/upload-image")
    public Result<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
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
     * 获取动物详情
     */
    @GetMapping("/detail/{id}")
    public Result<Animal> getAnimalDetail(@PathVariable Long id) {
        Animal animal = animalService.getById(id);
        if (animal == null) {
            return Result.error("动物不存在");
        }
        return Result.success(animal);
    }
}

