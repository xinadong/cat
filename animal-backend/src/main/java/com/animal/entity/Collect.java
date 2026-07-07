package com.animal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 收藏实体
 */
@Data
@TableName("collect")
public class Collect {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long animalId;
    private LocalDateTime createTime;
}

