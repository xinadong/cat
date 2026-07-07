package com.animal.service.impl;

import com.animal.entity.Category;
import com.animal.mapper.CategoryMapper;
import com.animal.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 分类Service实现
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}

