package com.animal.service.impl;

import com.animal.entity.Question;
import com.animal.mapper.QuestionMapper;
import com.animal.service.QuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 问题Service实现
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {
}

