package com.animal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * AI知识库实体
 */
@Data
@TableName("ai_knowledge")
public class AiKnowledge {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String content;
    private String category;
    private String tags;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

