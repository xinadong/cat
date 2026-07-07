package com.animal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 动物实体
 */
@Data
@TableName("animal")
public class Animal {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Long categoryId;
    private String coverImage;
    private String images;
    private String scientificName;
    private String appearance;
    private String habit;
    private String distribution;
    private String protectionLevel;
    private Integer viewCount;
    private Integer collectCount;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

