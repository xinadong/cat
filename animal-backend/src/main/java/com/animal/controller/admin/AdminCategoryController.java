package com.animal.controller.admin;

import com.animal.common.Result;
import com.animal.entity.Category;
import com.animal.service.CategoryService;
import com.animal.utils.FileUploadUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 管理员分类管理控制器
 */
@RestController
@RequestMapping("/api/admin/category")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分类列表
     */
    @GetMapping("/list")
    public Result<List<Category>> getCategoryList() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);
        List<Category> list = categoryService.list(wrapper);
        return Result.success(list);
    }

    /**
     * 添加分类
     */
    @PostMapping("/add")
    public Result<String> addCategory(@RequestBody Category category) {
        if (category.getSort() == null) {
            category.setSort(0);
        }
        boolean success = categoryService.save(category);
        if (success) {
            return Result.success("添加成功");
        } else {
            return Result.error("添加失败");
        }
    }

    /**
     * 更新分类
     */
    @PutMapping("/update")
    public Result<String> updateCategory(@RequestBody Category category) {
        boolean success = categoryService.updateById(category);
        if (success) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteCategory(@PathVariable Long id) {
        boolean success = categoryService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * 上传分类图标
     */
    @PostMapping("/upload-icon")
    public Result<String> uploadIcon(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = FileUploadUtil.uploadFile(file);
            return Result.success(filePath);
        } catch (Exception e) {
            return Result.error("上传失败：" + e.getMessage());
        }
    }
}

