package com.animal.service.impl;

import com.animal.entity.AiKnowledge;
import com.animal.mapper.AiKnowledgeMapper;
import com.animal.service.AiKnowledgeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AI知识库Service实现
 */
@Service
public class AiKnowledgeServiceImpl extends ServiceImpl<AiKnowledgeMapper, AiKnowledge> implements AiKnowledgeService {
    
    @Override
    public List<AiKnowledge> searchKnowledge(String keyword, int limit) {
        // 提取关键词
        List<String> keywords = extractKeywords(keyword);
        return baseMapper.searchByKeywordSmart(keyword, keywords, limit);
    }
    
    /**
     * 提取关键词 - 智能分词和语义扩展
     */
    private List<String> extractKeywords(String text) {
        List<String> keywords = new ArrayList<>();
        
        // 移除标点符号，保留疑问词用于语义分析
        String questionType = detectQuestionType(text);
        String cleaned = text.replaceAll("[？?！!。，,、]", " ");
        
        // 移除常见的助词和疑问词
        cleaned = cleaned.replaceAll("(什么|为什么|怎么|如何|吗|呢|啊|的|了|是)", " ");
        
        // 提取主要词汇
        String[] words = cleaned.split("\\s+");
        for (String word : words) {
            if (word.length() >= 2) {
                keywords.add(word);
            }
        }
        
        // 根据问题类型扩展相关词汇
        keywords.addAll(expandKeywordsByQuestion(questionType, keywords));
        
        return keywords.stream().distinct().collect(Collectors.toList());
    }
    
    /**
     * 检测问题类型
     */
    private String detectQuestionType(String text) {
        if (text.contains("吃") || text.contains("食")) {
            return "diet";
        } else if (text.contains("住") || text.contains("栖息") || text.contains("生活在")) {
            return "habitat";
        } else if (text.contains("特征") || text.contains("样子") || text.contains("长什么")) {
            return "appearance";
        } else if (text.contains("习性") || text.contains("习惯") || text.contains("行为")) {
            return "behavior";
        } else if (text.contains("繁殖") || text.contains("生育") || text.contains("幼崽")) {
            return "reproduction";
        } else if (text.contains("保护") || text.contains("濒危") || text.contains("数量")) {
            return "conservation";
        } else if (text.contains("区别") || text.contains("不同") || text.contains("差异")) {
            return "comparison";
        }
        return "general";
    }
    
    /**
     * 根据问题类型扩展关键词
     */
    private List<String> expandKeywordsByQuestion(String questionType, List<String> originalKeywords) {
        List<String> expanded = new ArrayList<>();
        
        switch (questionType) {
            case "diet":
                expanded.add("饮食");
                expanded.add("食物");
                expanded.add("食谱");
                expanded.add("捕食");
                break;
            case "habitat":
                expanded.add("栖息地");
                expanded.add("栖息");
                expanded.add("环境");
                expanded.add("分布");
                break;
            case "appearance":
                expanded.add("特征");
                expanded.add("外形");
                expanded.add("体型");
                expanded.add("毛色");
                break;
            case "behavior":
                expanded.add("习性");
                expanded.add("习惯");
                expanded.add("行为");
                expanded.add("活动");
                break;
            case "reproduction":
                expanded.add("繁殖");
                expanded.add("生育");
                expanded.add("幼崽");
                expanded.add("发情");
                break;
            case "conservation":
                expanded.add("保护");
                expanded.add("濒危");
                expanded.add("保护等级");
                break;
            case "comparison":
                expanded.add("区别");
                expanded.add("差异");
                expanded.add("对比");
                break;
        }
        
        return expanded;
    }
}

