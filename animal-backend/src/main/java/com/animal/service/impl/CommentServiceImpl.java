package com.animal.service.impl;

import com.animal.entity.Comment;
import com.animal.mapper.CommentMapper;
import com.animal.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 评论Service实现
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
}

