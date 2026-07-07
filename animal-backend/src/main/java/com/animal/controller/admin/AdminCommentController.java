package com.animal.controller.admin;

import com.animal.common.Result;
import com.animal.entity.Animal;
import com.animal.entity.Comment;
import com.animal.entity.User;
import com.animal.service.AnimalService;
import com.animal.service.CommentService;
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
 * 管理员评论管理控制器
 */
@RestController
@RequestMapping("/api/admin/comment")
public class AdminCommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private AnimalService animalService;

    /**
     * 评论列表
     */
    @GetMapping("/list")
    public Result<Page<Map<String, Object>>> getCommentList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Page<Comment> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Comment::getCreateTime);
        
        Page<Comment> commentPage = commentService.page(page, wrapper);
        
        Page<Map<String, Object>> result = new Page<>(pageNum, pageSize, commentPage.getTotal());
        List<Map<String, Object>> records = new ArrayList<>();
        
        for (Comment comment : commentPage.getRecords()) {
            Map<String, Object> map = new HashMap<>();
            map.put("comment", comment);
            
            User user = userService.getById(comment.getUserId());
            map.put("user", user);
            
            Animal animal = animalService.getById(comment.getAnimalId());
            map.put("animal", animal);
            
            records.add(map);
        }
        
        result.setRecords(records);
        return Result.success(result);
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteComment(@PathVariable Long id) {
        boolean success = commentService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * 审核评论
     */
    @PutMapping("/audit/{id}")
    public Result<String> auditComment(@PathVariable Long id, @RequestParam Integer status) {
        Comment comment = commentService.getById(id);
        if (comment == null) {
            return Result.error("评论不存在");
        }
        
        comment.setStatus(status);
        boolean success = commentService.updateById(comment);
        if (success) {
            return Result.success("审核成功");
        } else {
            return Result.error("审核失败");
        }
    }
}

