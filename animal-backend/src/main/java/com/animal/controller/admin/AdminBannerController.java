package com.animal.controller.admin;

import com.animal.common.Result;
import com.animal.entity.Banner;
import com.animal.service.BannerService;
import com.animal.utils.FileUploadUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员轮播图管理控制器
 */
@RestController
@RequestMapping("/api/admin/banner")
public class AdminBannerController {

    @Autowired
    private BannerService bannerService;

    /**
     * 轮播图列表
     */
    @GetMapping("/list")
    public Result<List<Banner>> getBannerList() {
        LambdaQueryWrapper<Banner> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Banner::getSort);
        List<Banner> list = bannerService.list(wrapper);
        return Result.success(list);
    }

    /**
     * 添加轮播图
     */
    @PostMapping("/add")
    public Result<String> addBanner(@RequestBody Banner banner) {
        if (banner.getStatus() == null) {
            banner.setStatus(1);
        }
        if (banner.getSort() == null) {
            banner.setSort(0);
        }
        
        boolean success = bannerService.save(banner);
        if (success) {
            return Result.success("添加成功");
        } else {
            return Result.error("添加失败");
        }
    }

    /**
     * 更新轮播图
     */
    @PutMapping("/update")
    public Result<String> updateBanner(@RequestBody Banner banner) {
        boolean success = bannerService.updateById(banner);
        if (success) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }

    /**
     * 删除轮播图
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteBanner(@PathVariable Long id) {
        boolean success = bannerService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * 上传轮播图图片
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
}

