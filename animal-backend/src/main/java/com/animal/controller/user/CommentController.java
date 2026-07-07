package com.animal.controller.user;

import com.animal.common.Result;
import com.animal.entity.Comment;
import com.animal.entity.User;
import com.animal.service.CommentService;
import com.animal.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论控制器
 */
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    /**
     * 发表评论
     */
    @PostMapping("/add")
    public Result<String> addComment(@RequestBody Comment comment, 
                                     HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        comment.setUserId(userId);
        comment.setLikeCount(0);
        comment.setStatus(1); // 默认通过审核
        
        boolean success = commentService.save(comment);
        if (success) {
            return Result.success("评论成功");
        } else {
            return Result.error("评论失败");
        }
    }

    /**
     * 评论列表
     */
    @GetMapping("/list")
    public Result<List<Map<String, Object>>> getCommentList(@RequestParam Long animalId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getAnimalId, animalId)
               .eq(Comment::getStatus, 1)
               .orderByDesc(Comment::getCreateTime);
        
        List<Comment> comments = commentService.list(wrapper);
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (Comment comment : comments) {
            Map<String, Object> map = new HashMap<>();
            map.put("comment", comment);
            
            User user = userService.getById(comment.getUserId());
            map.put("user", user);
            
            result.add(map);
        }
        
        return Result.success(result);
    }

    /**
     * 点赞评论
     */
    @PostMapping("/like/{id}")
    public Result<String> likeComment(@PathVariable Long id) {
        Comment comment = commentService.getById(id);
        if (comment == null) {
            return Result.error("评论不存在");
        }
        
        comment.setLikeCount(comment.getLikeCount() + 1);
        commentService.updateById(comment);
        
        return Result.success("点赞成功");
    }

    /**
     * 删除自己的评论
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteComment(@PathVariable Long id, 
                                        HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        
        Comment comment = commentService.getById(id);
        if (comment == null) {
            return Result.error("评论不存在");
        }
        
        if (!comment.getUserId().equals(userId)) {
            return Result.error("只能删除自己的评论");
        }
        
        boolean success = commentService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
}

