package com.animal.service.impl;

import com.animal.entity.Banner;
import com.animal.mapper.BannerMapper;
import com.animal.service.BannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 轮播图Service实现
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {
}

