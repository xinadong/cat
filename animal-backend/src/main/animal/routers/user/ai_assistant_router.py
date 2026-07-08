import json
import aiohttp
import re
from fastapi import APIRouter, Depends
from pydantic import BaseModel
from sse_starlette.sse import EventSourceResponse
from sqlmodel import Session, select
from sqlalchemy import or_

from models.entities import AiKnowledge
from common.result import Result
from database import get_session

router = APIRouter(prefix="/api/ai", tags=["AI Assistant"])

API_KEY = "sk-0b6ee6d9f6fe41a99cccdb3d3ba89196"
BASE_URL = "https://api.deepseek.com"
MODEL = "deepseek-chat"


class ChatRequest(BaseModel):
    question: str


# ==================== NLP 关键词智能提取逻辑 ====================
def detect_question_type(text: str) -> str:
    if "吃" in text or "食物" in text:
        return "diet"
    elif "住" in text or "栖息" in text or "环境" in text:
        return "habitat"
    elif "长什么样" in text or "外观" in text or "特征" in text:
        return "appearance"
    elif "行为" in text or "习性" in text or "活动" in text:
        return "behavior"
    elif "繁殖" in text or "生育" in text or "幼崽" in text:
        return "reproduction"
    elif "保护" in text or "濒危" in text or "灭绝" in text:
        return "conservation"
    elif "区别" in text or "对比" in text or "不同" in text:
        return "comparison"
    return "general"


def expand_keywords_by_question(question_type: str) -> list[str]:
    expanded = []
    if question_type == "diet":
        expanded.extend(["食物", "饮食", "捕食", "吃"])
    elif question_type == "habitat":
        expanded.extend(["栖息地", "分布", "环境", "生存"])
    elif question_type == "appearance":
        expanded.extend(["外观", "特征", "体型", "颜色"])
    elif question_type == "behavior":
        expanded.extend(["习性", "行为", "活动", "群居"])
    elif question_type == "reproduction":
        expanded.extend(["繁殖", "交配", "孕期", "幼崽"])
    elif question_type == "conservation":
        expanded.extend(["保护级别", "濒危", "现状"])
    elif question_type == "comparison":
        expanded.extend(["区别", "不同", "对比"])
    return expanded


def extract_keywords(text: str) -> list[str]:
    keywords = []
    question_type = detect_question_type(text)

    cleaned = re.sub(r'[?？,，.。!！]', ' ', text)
    words = cleaned.split()
    for word in words:
        if len(word) >= 2:
            keywords.append(word)

    keywords.extend(expand_keywords_by_question(question_type))
    return list(set(keywords))


# ==================== API 路由 ====================
@router.post("/chat", response_model=Result[dict])
def chat(request: ChatRequest, session: Session = Depends(get_session)):
    if not request.question.strip():
        return Result.error("Question cannot be empty")

    # 智能关键词提取
    keywords = extract_keywords(request.question)

    # 模拟 MyBatisPlus searchByKeywordSmart
    query = select(AiKnowledge).where(
        or_(*(AiKnowledge.title.contains(kw) for kw in keywords)) | AiKnowledge.title.contains(request.question)
    ).limit(3)
    knowledge_list = session.exec(query).all()

    context_text = ""
    if knowledge_list:
        context_text = "\n".join([f"《{k.title}》：{k.content}" for k in knowledge_list])

    system_prompt = (
        "你是一个拟人化的动物科普专家，拥有渊博的动物知识。你的回答必须生动、专业、直接。\n"
        "【绝对禁令】：\n"
        "你不能在回答中暴露你是一个正在查阅资料的AI。回答中绝对不允许出现“根据提供的知识库”、“参考资料”、“资料中没有提到”、“基于我自己的知识”等任何类似的机械套话。\n"
        "请直接以动物专家的口吻，自然地回答用户的具体问题！"
    )

    user_prompt = request.question
    if context_text:
        user_prompt = f"【隐藏的参考资料】（如果与问题完全无关请直接无视）：\n{context_text}\n\n我的问题是：{request.question}"

    response_data = {
        "question": request.question,
        "context": [k.title for k in knowledge_list],
        "contextText": context_text,
        "systemPrompt": system_prompt,
        "hasApiKey": bool(API_KEY),
        "apiKey": API_KEY,
        "baseUrl": BASE_URL,
        "model": MODEL
    }
    return Result.success(data=response_data)


@router.post("/chat/stream")
async def chat_stream(request: ChatRequest, session: Session = Depends(get_session)):
    async def event_generator():
        if not request.question.strip():
            yield {"event": "error", "data": "Question cannot be empty"}
            return

        keywords = extract_keywords(request.question)
        query = select(AiKnowledge).where(
            or_(*(AiKnowledge.title.contains(kw) for kw in keywords)) | AiKnowledge.title.contains(request.question)
        ).limit(3)
        knowledge_list = session.exec(query).all()

        context_titles = [k.title for k in knowledge_list]
        yield {"event": "context", "data": json.dumps({"context": context_titles})}

        # === 将 chat_stream 中生成 context_text 和 messages 的部分替换为以下代码 ===
        context_text = "\n".join([f"《{k.title}》：{k.content}" for k in knowledge_list])

        system_prompt = (
            "你是一个拟人化的动物科普专家，拥有渊博的动物知识。你的回答必须生动、专业、直接。\n"
            "【绝对禁令】：\n"
            "你不能在回答中暴露你是一个正在查阅资料的AI。回答中绝对不允许出现“根据提供的知识库”、“参考资料”、“资料中没有提到”、“基于我自己的知识”等任何类似的机械套话。\n"
            "请直接以动物专家的口吻，自然地回答用户的具体问题！"
        )

        user_prompt = request.question
        if context_text:
            user_prompt = f"【隐藏的参考资料】（如果与问题完全无关请直接无视）：\n{context_text}\n\n我的问题是：{request.question}"

        messages = [
            {"role": "system", "content": system_prompt},
            {"role": "user", "content": user_prompt}
        ]

        payload = {
            "model": MODEL,
            "messages": messages,
            "stream": True,
            "temperature": 0.7,
            "max_tokens": 1500
        }

        headers = {
            "Content-Type": "application/json",
            "Authorization": f"Bearer {API_KEY}"
        }

        async with aiohttp.ClientSession() as http_session:
            async with http_session.post(f"{BASE_URL}/chat/completions", json=payload, headers=headers) as resp:
                if resp.status != 200:
                    # 捕获并读取阿里云返回的真实错误信息
                    error_body = await resp.text()
                    print(f"【AI 接口调用失败】状态码: {resp.status}, 详情: {error_body}")

                    # 将错误信息直接显示在前端页面上，方便我们排查
                    fallback = f"**AI 接口调用失败！**\n\n原因：`{error_body}`\n\n---\n以下是本地知识库的匹配结果：\n\n" + "\n\n".join(
                        [k.content for k in knowledge_list])
                    yield {"event": "content", "data": fallback}
                    yield {"event": "done", "data": "[DONE]"}
                    return

                full_content = ""
                async for line in resp.content:
                    line = line.decode('utf-8').strip()
                    if line.startswith("data: "):
                        data = line[6:]
                        if data == "[DONE]":
                            break
                        try:
                            node = json.loads(data)
                            content = node.get("choices", [{}])[0].get("delta", {}).get("content", "")
                            if content:
                                full_content += content
                        except Exception:
                            pass

                cleaned = re.sub(r'[\s]+', ' ', full_content)
                cleaned = re.sub(r'(?<=[\u4e00-\u9fff\uff01-\uff5e])\s+(?=[\u4e00-\u9fff\uff01-\uff5e])', '', cleaned)
                cleaned = re.sub(r'(?<=[\u4e00-\u9fff])\s+(?=[a-zA-Z0-9])', '', cleaned)
                cleaned = re.sub(r'(?<=[a-zA-Z0-9])\s+(?=[\u4e00-\u9fff])', '', cleaned)
                cleaned = cleaned.strip()

                if cleaned:
                    yield {"event": "content", "data": cleaned}
                yield {"event": "done", "data": "[DONE]"}

    return EventSourceResponse(event_generator())


@router.get("/config", response_model=Result[dict])
def get_config():
    return Result.success(data={
        "hasApiKey": bool(API_KEY),
        "baseUrl": BASE_URL,
        "model": MODEL
    })
