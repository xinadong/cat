package com.animal.controller.user;

import com.animal.common.Result;
import com.animal.entity.Answer;
import com.animal.entity.Question;
import com.animal.entity.User;
import com.animal.service.AnswerService;
import com.animal.service.QuestionService;
import com.animal.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 问答控制器
 */
@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private UserService userService;

    /**
     * 发布问题
     */
    @PostMapping("/add")
    public Result<String> addQuestion(@RequestBody Question question, 
                                      HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        question.setUserId(userId);
        question.setAnswerCount(0);
        question.setIsTop(0);
        question.setStatus(1); // 默认通过审核
        
        boolean success = questionService.save(question);
        if (success) {
            return Result.success("发布成功");
        } else {
            return Result.error("发布失败");
        }
    }

    /**
     * 问题列表
     */
    @GetMapping("/list")
    public Result<Page<Map<String, Object>>> getQuestionList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Page<Question> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getStatus, 1)
               .orderByDesc(Question::getIsTop)
               .orderByDesc(Question::getCreateTime);
        
        Page<Question> questionPage = questionService.page(page, wrapper);
        
        Page<Map<String, Object>> result = new Page<>(pageNum, pageSize, questionPage.getTotal());
        List<Map<String, Object>> records = new ArrayList<>();
        
        for (Question question : questionPage.getRecords()) {
            Map<String, Object> map = new HashMap<>();
            map.put("question", question);
            
            User user = userService.getById(question.getUserId());
            map.put("user", user);
            
            records.add(map);
        }
        
        result.setRecords(records);
        return Result.success(result);
    }

    /**
     * 问题详情
     */
    @GetMapping("/detail/{id}")
    public Result<Map<String, Object>> getQuestionDetail(@PathVariable Long id) {
        Question question = questionService.getById(id);
        if (question == null) {
            return Result.error("问题不存在");
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("question", question);
        
        User user = userService.getById(question.getUserId());
        data.put("user", user);
        
        // 获取回答列表
        LambdaQueryWrapper<Answer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Answer::getQuestionId, id)
               .orderByDesc(Answer::getIsAccepted)
               .orderByDesc(Answer::getCreateTime);
        
        List<Answer> answers = answerService.list(wrapper);
        List<Map<String, Object>> answerList = new ArrayList<>();
        
        for (Answer answer : answers) {
            Map<String, Object> answerMap = new HashMap<>();
            answerMap.put("answer", answer);
            
            User answerUser = userService.getById(answer.getUserId());
            answerMap.put("user", answerUser);
            
            answerList.add(answerMap);
        }
        
        data.put("answers", answerList);
        
        return Result.success(data);
    }
}

