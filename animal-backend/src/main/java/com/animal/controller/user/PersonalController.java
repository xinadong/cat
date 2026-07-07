package com.animal.controller.user;

import com.animal.common.Result;
import com.animal.entity.*;
import com.animal.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人中心控制器
 */
@RestController
@RequestMapping("/api/personal")
public class PersonalController {

    @Autowired
    private CollectService collectService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private BrowseHistoryService browseHistoryService;

    @Autowired
    private AnimalService animalService;

    /**
     * 我的收藏
     */
    @GetMapping("/collects")
    public Result<List<Animal>> getMyCollects(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId)
               .orderByDesc(Collect::getCreateTime);
        
        List<Collect> collects = collectService.list(wrapper);
        
        List<Animal> animals = new ArrayList<>();
        for (Collect collect : collects) {
            Animal animal = animalService.getById(collect.getAnimalId());
            if (animal != null) {
                animals.add(animal);
            }
        }
        
        return Result.success(animals);
    }

    /**
     * 我的评论
     */
    @GetMapping("/comments")
    public Result<List<Map<String, Object>>> getMyComments(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getUserId, userId)
               .orderByDesc(Comment::getCreateTime);
        
        List<Comment> comments = commentService.list(wrapper);
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (Comment comment : comments) {
            Map<String, Object> map = new HashMap<>();
            map.put("comment", comment);
            
            Animal animal = animalService.getById(comment.getAnimalId());
            map.put("animal", animal);
            
            result.add(map);
        }
        
        return Result.success(result);
    }

    /**
     * 我的提问
     */
    @GetMapping("/questions")
    public Result<List<Question>> getMyQuestions(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getUserId, userId)
               .orderByDesc(Question::getCreateTime);
        
        List<Question> questions = questionService.list(wrapper);
        return Result.success(questions);
    }

    /**
     * 浏览历史
     */
    @GetMapping("/history")
    public Result<List<Map<String, Object>>> getBrowseHistory(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        
        LambdaQueryWrapper<BrowseHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BrowseHistory::getUserId, userId)
               .orderByDesc(BrowseHistory::getBrowseTime)
               .last("LIMIT 50");
        
        List<BrowseHistory> histories = browseHistoryService.list(wrapper);
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (BrowseHistory history : histories) {
            Animal animal = animalService.getById(history.getAnimalId());
            if (animal != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("animal", animal);
                map.put("browseTime", history.getBrowseTime());
                result.add(map);
            }
        }
        
        return Result.success(result);
    }

    /**
     * 清空浏览历史
     */
    @DeleteMapping("/history/clear")
    public Result<Void> clearBrowseHistory(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        
        LambdaQueryWrapper<BrowseHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BrowseHistory::getUserId, userId);
        
        browseHistoryService.remove(wrapper);
        return Result.success();
    }
}

