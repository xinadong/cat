package com.animal.controller.user;

import com.animal.common.Result;
import com.animal.entity.Answer;
import com.animal.entity.Question;
import com.animal.service.AnswerService;
import com.animal.service.QuestionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 回答控制器
 */
@RestController
@RequestMapping("/api/answer")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;

    /**
     * 发布回答
     */
    @PostMapping("/add")
    public Result<String> addAnswer(@RequestBody Answer answer, 
                                    HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        answer.setUserId(userId);
        answer.setIsAccepted(0);
        
        boolean success = answerService.save(answer);
        if (success) {
            // 更新问题的回答数
            Question question = questionService.getById(answer.getQuestionId());
            question.setAnswerCount(question.getAnswerCount() + 1);
            questionService.updateById(question);
            
            return Result.success("回答成功");
        } else {
            return Result.error("回答失败");
        }
    }

    /**
     * 采纳答案
     */
    @PutMapping("/accept/{id}")
    public Result<String> acceptAnswer(@PathVariable Long id, 
                                       HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        
        Answer answer = answerService.getById(id);
        if (answer == null) {
            return Result.error("回答不存在");
        }
        
        // 验证是否是问题的提问者
        Question question = questionService.getById(answer.getQuestionId());
        if (!question.getUserId().equals(userId)) {
            return Result.error("只有提问者才能采纳答案");
        }
        
        // 取消该问题下其他已采纳的答案
        LambdaQueryWrapper<Answer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Answer::getQuestionId, answer.getQuestionId())
               .eq(Answer::getIsAccepted, 1);
        
        List<Answer> acceptedAnswers = answerService.list(wrapper);
        for (Answer acceptedAnswer : acceptedAnswers) {
            acceptedAnswer.setIsAccepted(0);
            answerService.updateById(acceptedAnswer);
        }
        
        // 设置当前答案为采纳状态
        answer.setIsAccepted(1);
        answerService.updateById(answer);
        
        return Result.success("采纳成功");
    }
}

