package com.animal.controller.admin;

import com.animal.common.Result;
import com.animal.entity.Question;
import com.animal.entity.User;
import com.animal.service.QuestionService;
import com.animal.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员问答管理控制器
 */
@RestController
@RequestMapping("/api/admin/question")
public class AdminQuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    /**
     * 问题列表
     */
    @GetMapping("/list")
    public Result<Page<Map<String, Object>>> getQuestionList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Page<Question> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Question::getCreateTime);
        
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
     * 删除问题
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteQuestion(@PathVariable Long id) {
        boolean success = questionService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * 置顶问题
     */
    @PutMapping("/top/{id}")
    public Result<String> topQuestion(@PathVariable Long id, @RequestParam Integer isTop) {
        Question question = questionService.getById(id);
        if (question == null) {
            return Result.error("问题不存在");
        }
        
        question.setIsTop(isTop);
        boolean success = questionService.updateById(question);
        if (success) {
            return Result.success(isTop == 1 ? "置顶成功" : "取消置顶成功");
        } else {
            return Result.error("操作失败");
        }
    }
}

