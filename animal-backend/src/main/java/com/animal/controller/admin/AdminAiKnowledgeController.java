package com.animal.controller.admin;

import com.animal.common.Result;
import com.animal.entity.AiKnowledge;
import com.animal.service.AiKnowledgeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员-AI知识库管理控制器
 */
@RestController
@RequestMapping("/api/admin/ai-knowledge")
public class AdminAiKnowledgeController {

    @Autowired
    private AiKnowledgeService aiKnowledgeService;

    /**
     * 分页查询知识库
     */
    @GetMapping("/list")
    public Result<Page<AiKnowledge>> getList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category) {
        
        Page<AiKnowledge> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<AiKnowledge> wrapper = new LambdaQueryWrapper<>();
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(AiKnowledge::getTitle, keyword)
                    .or().like(AiKnowledge::getContent, keyword)
                    .or().like(AiKnowledge::getTags, keyword));
        }
        
        if (category != null && !category.isEmpty()) {
            wrapper.eq(AiKnowledge::getCategory, category);
        }
        
        wrapper.orderByDesc(AiKnowledge::getCreateTime);
        
        Page<AiKnowledge> result = aiKnowledgeService.page(page, wrapper);
        return Result.success(result);
    }

    /**
     * 添加知识
     */
    @PostMapping("/add")
    public Result<String> add(@RequestBody AiKnowledge knowledge) {
        if (knowledge.getStatus() == null) {
            knowledge.setStatus(1);
        }
        boolean success = aiKnowledgeService.save(knowledge);
        return success ? Result.success("添加成功") : Result.error("添加失败");
    }

    /**
     * 更新知识
     */
    @PutMapping("/update")
    public Result<String> update(@RequestBody AiKnowledge knowledge) {
        boolean success = aiKnowledgeService.updateById(knowledge);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }

    /**
     * 删除知识
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean success = aiKnowledgeService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete-batch")
    public Result<String> deleteBatch(@RequestBody java.util.List<Long> ids) {
        boolean success = aiKnowledgeService.removeByIds(ids);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 切换状态
     */
    @PutMapping("/toggle-status/{id}")
    public Result<String> toggleStatus(@PathVariable Long id) {
        AiKnowledge knowledge = aiKnowledgeService.getById(id);
        if (knowledge == null) {
            return Result.error("知识不存在");
        }
        knowledge.setStatus(knowledge.getStatus() == 1 ? 0 : 1);
        boolean success = aiKnowledgeService.updateById(knowledge);
        return success ? Result.success("状态更新成功") : Result.error("状态更新失败");
    }
}

