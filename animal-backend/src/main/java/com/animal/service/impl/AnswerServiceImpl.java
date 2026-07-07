package com.animal.service.impl;

import com.animal.entity.Answer;
import com.animal.mapper.AnswerMapper;
import com.animal.service.AnswerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 回答Service实现
 */
@Service
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements AnswerService {
}

