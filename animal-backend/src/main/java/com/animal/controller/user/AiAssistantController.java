package com.animal.controller.user;

import com.animal.common.Result;
import com.animal.entity.AiKnowledge;
import com.animal.service.AiKnowledgeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * AI助手控制器
 */
@RestController
@RequestMapping("/api/ai")
public class AiAssistantController {

    @Autowired
    private AiKnowledgeService aiKnowledgeService;

    @Value("${ai.api-key:}")
    private String apiKey;

    @Value("${ai.base-url:https://dashscope.aliyuncs.com/compatible-mode/v1}")
    private String baseUrl;

    @Value("${ai.model:qwen-plus}")
    private String model;

    /**
     * AI对话接口
     */
    @PostMapping("/chat")
    public Result<Map<String, Object>> chat(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        
        if (question == null || question.trim().isEmpty()) {
            return Result.error("问题不能为空");
        }

        try {
            // 1. 从知识库搜索相关内容
            List<AiKnowledge> knowledgeList = aiKnowledgeService.searchKnowledge(question, 3);
            
            // 2. 构建上下文
            StringBuilder contextText = new StringBuilder();
            if (!knowledgeList.isEmpty()) {
                contextText.append("参考以下知识库内容：\n\n");
                for (AiKnowledge knowledge : knowledgeList) {
                    contextText.append("【").append(knowledge.getTitle()).append("】\n");
                    contextText.append(knowledge.getContent()).append("\n\n");
                }
            }
            
            // 3. 构建系统提示词
            String systemPrompt = "你是一个专业的动物科普助手，名字叫\"动物小百科\"。" +
                    "你的职责是用友好、专业、生动的语气回答用户关于动物的各种问题。\n\n" +
                    "回答要求：\n" +
                    "1. 如果提供了知识库内容，请优先基于知识库内容回答，这些内容是经过专业审核的权威信息。\n" +
                    "2. 在知识库内容的基础上，你可以适当补充相关知识，使回答更加完整和有趣。\n" +
                    "3. 如果知识库中没有相关内容，请使用你自己的知识进行准确、详细的回答。\n" +
                    "4. 回答要清晰、易懂，适合科普，可以使用适当的比喻和生动的描述。\n" +
                    "5. 对于保护动物相关问题，要强调保护意识和生态平衡的重要性。\n" +
                    "6. 回答要有条理，必要时可以分点说明。\n" +
                    "7. 保持友好、耐心的态度，鼓励用户继续提问。";
            // 4. 准备返回数据
            Map<String, Object> response = new HashMap<>();
            response.put("question", question);
            response.put("context", knowledgeList.stream()
                    .map(AiKnowledge::getTitle)
                    .collect(Collectors.toList()));
            response.put("contextText", contextText.toString());
            response.put("systemPrompt", systemPrompt);
            response.put("hasApiKey", apiKey != null && !apiKey.isEmpty());
            
            // 返回 AI 配置供前端调用
            if (apiKey != null && !apiKey.isEmpty()) {
                response.put("apiKey", apiKey);
                response.put("baseUrl", baseUrl);
                response.put("model", model);
            }
            
            return Result.success(response);
            
        } catch (Exception e) {
            return Result.error("处理失败：" + e.getMessage());
        }
    }

    /**
     * AI对话流式接口 (SSE)
     */
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chatStream(@RequestBody Map<String, String> request) {
        SseEmitter emitter = new SseEmitter(120000L); // 2分钟超时
        String question = request.get("question");

        if (question == null || question.trim().isEmpty()) {
            try {
                emitter.send(SseEmitter.event().name("error").data("问题不能为空", MediaType.TEXT_PLAIN));
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
            return emitter;
        }

        CompletableFuture.runAsync(() -> {
            try {
                ObjectMapper mapper = new ObjectMapper();

                // 1. 从知识库搜索相关内容
                List<AiKnowledge> knowledgeList = aiKnowledgeService.searchKnowledge(question, 3);

                // 2. 构建上下文
                StringBuilder contextText = new StringBuilder();
                if (!knowledgeList.isEmpty()) {
                    contextText.append("参考以下知识库内容：\n\n");
                    for (AiKnowledge knowledge : knowledgeList) {
                        contextText.append("【").append(knowledge.getTitle()).append("】\n");
                        contextText.append(knowledge.getContent()).append("\n\n");
                    }
                }

                // 3. 发送知识库上下文信息
                List<String> contextTitles = knowledgeList.stream()
                        .map(AiKnowledge::getTitle)
                        .collect(Collectors.toList());
                Map<String, Object> contextInfo = new HashMap<>();
                contextInfo.put("context", contextTitles);
                emitter.send(SseEmitter.event()
                        .name("context")
                        .data(mapper.writeValueAsString(contextInfo), MediaType.TEXT_PLAIN));

                // 4. 检查 API Key
                if (apiKey == null || apiKey.isEmpty()) {
                    String answer;
                    if (!knowledgeList.isEmpty()) {
                        answer = "根据知识库，我找到了以下相关信息：\n\n" + contextText;
                        answer += "\n\n💡 提示：当前未配置API Key，仅显示知识库内容。配置API Key后可获得AI智能回答。";
                    } else {
                        answer = "抱歉，我在知识库中没有找到相关信息。\n\n💡 提示：如果配置了 API Key，我可以根据通用知识为您解答。请联系管理员配置API Key。";
                    }
                    emitter.send(SseEmitter.event().name("content").data(answer, MediaType.TEXT_PLAIN));
                    emitter.send(SseEmitter.event().name("done").data("[DONE]", MediaType.TEXT_PLAIN));
                    emitter.complete();
                    return;
                }

                // 5. 构建系统提示词
                String systemPrompt = "你是一个专业的动物科普助手，名字叫\"动物小百科\"。" +
                        "你的职责是用友好、专业、生动的语气回答用户关于动物的各种问题。\n\n" +
                        "回答要求：\n" +
                        "1. 如果提供了知识库内容，请优先基于知识库内容回答，这些内容是经过专业审核的权威信息。\n" +
                        "2. 在知识库内容的基础上，你可以适当补充相关知识，使回答更加完整和有趣。\n" +
                        "3. 如果知识库中没有相关内容，请使用你自己的知识进行准确、详细的回答。\n" +
                        "4. 回答要清晰、易懂，适合科普，可以使用适当的比喻和生动的描述。\n" +
                        "5. 对于保护动物相关问题，要强调保护意识和生态平衡的重要性。\n" +
                        "6. 回答要有条理，必要时可以分点说明。\n" +
                        "7. 保持友好、耐心的态度，鼓励用户继续提问。";

                // 6. 构建消息列表
                List<Map<String, String>> aiMessages = new ArrayList<>();
                aiMessages.add(Map.of("role", "system", "content", systemPrompt));

                if (contextText.length() > 0) {
                    aiMessages.add(Map.of("role", "system", "content",
                            "以下是相关的知识库内容，请优先参考这些内容回答：\n\n" + contextText));
                } else {
                    aiMessages.add(Map.of("role", "system", "content",
                            "知识库中暂无相关内容，请使用你的通用知识进行准确、详细的回答。"));
                }
                aiMessages.add(Map.of("role", "user", "content", question));

                // 7. 构建请求体
                Map<String, Object> requestBody = new LinkedHashMap<>();
                requestBody.put("model", model);
                requestBody.put("messages", aiMessages);
                requestBody.put("stream", true);
                requestBody.put("temperature", 0.7);
                requestBody.put("max_tokens", 1500);

                String jsonBody = mapper.writeValueAsString(requestBody);

                // 8. 调用 AI API（流式）
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest httpRequest = HttpRequest.newBuilder()
                        .uri(URI.create(baseUrl + "/chat/completions"))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + apiKey)
                        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                        .build();

                HttpResponse<java.io.InputStream> httpResponse = client.send(httpRequest,
                        HttpResponse.BodyHandlers.ofInputStream());

                if (httpResponse.statusCode() != 200) {
                    String errorBody = new String(httpResponse.body().readAllBytes(), StandardCharsets.UTF_8);
                    // AI调用失败，降级到知识库
                    if (!knowledgeList.isEmpty()) {
                        String fallback = "（AI服务暂时不可用，以下是知识库内容）\n\n" + contextText;
                        emitter.send(SseEmitter.event().name("content").data(fallback, MediaType.TEXT_PLAIN));
                    } else {
                        emitter.send(SseEmitter.event().name("content")
                                .data("抱歉，AI服务暂时不可用，且知识库中没有找到相关信息。请稍后再试。", MediaType.TEXT_PLAIN));
                    }
                    emitter.send(SseEmitter.event().name("done").data("[DONE]", MediaType.TEXT_PLAIN));
                    emitter.complete();
                    return;
                }

                // 9. 逐行读取流式响应并转发给前端
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(httpResponse.body(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("data: ")) {
                            String data = line.substring(6).trim();
                            if ("[DONE]".equals(data)) {
                                break;
                            }
                            try {
                                JsonNode node = mapper.readTree(data);
                                JsonNode choices = node.get("choices");
                                if (choices != null && choices.isArray() && choices.size() > 0) {
                                    JsonNode delta = choices.get(0).get("delta");
                                    if (delta != null && delta.has("content")) {
                                        String content = delta.get("content").asText();
                                        if (content != null && !content.isEmpty()) {
                                            emitter.send(SseEmitter.event()
                                                    .name("content")
                                                    .data(content, MediaType.TEXT_PLAIN));
                                        }
                                    }
                                }
                            } catch (Exception parseError) {
                                // 忽略解析错误，继续处理下一行
                            }
                        }
                    }
                }

                emitter.send(SseEmitter.event().name("done").data("[DONE]", MediaType.TEXT_PLAIN));
                emitter.complete();

            } catch (Exception e) {
                try {
                    emitter.send(SseEmitter.event().name("error")
                            .data("处理失败：" + e.getMessage(), MediaType.TEXT_PLAIN));
                    emitter.complete();
                } catch (Exception ex) {
                    emitter.completeWithError(ex);
                }
            }
        });

        emitter.onTimeout(emitter::complete);
        return emitter;
    }

    /**
     * 获取 API 配置状态
     */
    @GetMapping("/config")
    public Result<Map<String, Object>> getConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("hasApiKey", apiKey != null && !apiKey.isEmpty());
        config.put("baseUrl", baseUrl);
        config.put("model", model);
        return Result.success(config);
    }
}

