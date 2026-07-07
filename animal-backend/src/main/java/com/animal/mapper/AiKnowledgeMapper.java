package com.animal.mapper;

import com.animal.entity.AiKnowledge;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * AI知识库Mapper
 */
@Mapper
public interface AiKnowledgeMapper extends BaseMapper<AiKnowledge> {
    
    /**
     * 智能搜索知识库 - 支持模糊匹配和关键词提取
     */
    @Select("<script>" +
            "SELECT * FROM ai_knowledge " +
            "WHERE status = 1 " +
            "AND (" +
            "  title LIKE CONCAT('%', #{keyword}, '%') " +
            "  OR content LIKE CONCAT('%', #{keyword}, '%') " +
            "  OR tags LIKE CONCAT('%', #{keyword}, '%') " +
            "<if test='keywords != null'>" +
            "  <foreach collection='keywords' item='kw' separator=' '>" +
            "    OR title LIKE CONCAT('%', #{kw}, '%') " +
            "    OR content LIKE CONCAT('%', #{kw}, '%') " +
            "    OR tags LIKE CONCAT('%', #{kw}, '%') " +
            "  </foreach>" +
            "</if>" +
            ") " +
            "ORDER BY " +
            "  CASE " +
            "    WHEN title LIKE CONCAT('%', #{keyword}, '%') THEN 1 " +
            "    WHEN tags LIKE CONCAT('%', #{keyword}, '%') THEN 2 " +
            "    ELSE 3 " +
            "  END, " +
            "  create_time DESC " +
            "LIMIT #{limit}" +
            "</script>")
    List<AiKnowledge> searchByKeywordSmart(@Param("keyword") String keyword, 
                                            @Param("keywords") List<String> keywords, 
                                            @Param("limit") int limit);
}

