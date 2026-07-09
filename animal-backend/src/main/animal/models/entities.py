from typing import Optional
from datetime import datetime
from sqlmodel import SQLModel, Field

# 驼峰命名转换器：确保 API JSON 字段与 Java 保持一致（如 create_time -> createTime）
def to_camel(string: str) -> str:
    words = string.split('_')
    return words[0] + ''.join(word.capitalize() for word in words[1:])

class BaseEntity(SQLModel):
    # Pydantic V2 配置：允许使用下划线名称填充数据，但序列化为 JSON 时使用驼峰别名
    model_config = {
        "populate_by_name": True,
        "alias_generator": to_camel
    }

# 1. AI 知识库模型
class AiKnowledge(BaseEntity, table=True):
    __tablename__ = "ai_knowledge"
    id: Optional[int] = Field(default=None, primary_key=True)
    title: Optional[str] = None
    content: Optional[str] = None
    category: Optional[str] = None
    tags: Optional[str] = None
    status: Optional[int] = Field(default=1)
    create_time: Optional[datetime] = Field(default_factory=datetime.now)
    update_time: Optional[datetime] = Field(default_factory=datetime.now)

# 2. 动物信息模型
class Animal(BaseEntity, table=True):
    __tablename__ = "animal"
    id: Optional[int] = Field(default=None, primary_key=True)
    name: Optional[str] = None
    category_id: Optional[int] = None
    cover_image: Optional[str] = None
    images: Optional[str] = None
    scientific_name: Optional[str] = None
    selfintro: Optional[str] = Field(default=None)
    habit: Optional[str] = None
    over: Optional[str] = Field(default=None)
    protection_level: Optional[str] = None
    view_count: Optional[int] = Field(default=0)
    collect_count: Optional[int] = Field(default=0)
    status: Optional[int] = Field(default=1)
    create_time: Optional[datetime] = Field(default_factory=datetime.now)
    update_time: Optional[datetime] = Field(default_factory=datetime.now)

# 3. 问答社区 - 回答模型
class Answer(BaseEntity, table=True):
    __tablename__ = "answer"
    id: Optional[int] = Field(default=None, primary_key=True)
    question_id: Optional[int] = None
    user_id: Optional[int] = None
    content: Optional[str] = None
    is_accepted: Optional[int] = Field(default=0)
    create_time: Optional[datetime] = Field(default_factory=datetime.now)

# 4. 科普文章模型
class Article(BaseEntity, table=True):
    __tablename__ = "article"
    id: Optional[int] = Field(default=None, primary_key=True)
    title: Optional[str] = None
    cover_image: Optional[str] = None
    content: Optional[str] = None
    category: Optional[str] = None
    view_count: Optional[int] = Field(default=0)
    status: Optional[int] = Field(default=1)
    create_time: Optional[datetime] = Field(default_factory=datetime.now)
    update_time: Optional[datetime] = Field(default_factory=datetime.now)

# 5. 轮播图模型
class Banner(BaseEntity, table=True):
    __tablename__ = "banner"
    id: Optional[int] = Field(default=None, primary_key=True)
    title: Optional[str] = None
    image_url: Optional[str] = None
    link_url: Optional[str] = None
    sort: Optional[int] = Field(default=0)
    status: Optional[int] = Field(default=1)
    create_time: Optional[datetime] = Field(default_factory=datetime.now)

# 6. 浏览记录模型
class BrowseHistory(BaseEntity, table=True):
    __tablename__ = "browse_history"
    id: Optional[int] = Field(default=None, primary_key=True)
    user_id: Optional[int] = None
    animal_id: Optional[int] = None
    browse_time: Optional[datetime] = Field(default_factory=datetime.now)

# 7. 分类模型
class Category(BaseEntity, table=True):
    __tablename__ = "category"
    id: Optional[int] = Field(default=None, primary_key=True)
    name: Optional[str] = None
    description: Optional[str] = None
    icon: Optional[str] = None
    sort: Optional[int] = Field(default=0)
    create_time: Optional[datetime] = Field(default_factory=datetime.now)

# 8. 收藏模型
class Collect(BaseEntity, table=True):
    __tablename__ = "collect"
    id: Optional[int] = Field(default=None, primary_key=True)
    user_id: Optional[int] = None
    animal_id: Optional[int] = None
    create_time: Optional[datetime] = Field(default_factory=datetime.now)

# 9. 评论模型
class Comment(BaseEntity, table=True):
    __tablename__ = "comment"
    id: Optional[int] = Field(default=None, primary_key=True)
    user_id: Optional[int] = None
    animal_id: Optional[int] = None
    content: Optional[str] = None
    like_count: Optional[int] = Field(default=0)
    status: Optional[int] = Field(default=1)
    create_time: Optional[datetime] = Field(default_factory=datetime.now)

# 10. 问答社区 - 提问模型
class Question(BaseEntity, table=True):
    __tablename__ = "question"
    id: Optional[int] = Field(default=None, primary_key=True)
    user_id: Optional[int] = None
    title: Optional[str] = None
    content: Optional[str] = None
    answer_count: Optional[int] = Field(default=0)
    is_top: Optional[int] = Field(default=0)
    status: Optional[int] = Field(default=1)
    create_time: Optional[datetime] = Field(default_factory=datetime.now)

# 11. 系统管理员模型
class SysAdmin(BaseEntity, table=True):
    __tablename__ = "sys_admin"
    id: Optional[int] = Field(default=None, primary_key=True)
    username: str
    # exclude=True 对标 Java 中的 @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)，防止密码返回给前端
    password: str = Field(exclude=True)
    nickname: Optional[str] = None
    avatar: Optional[str] = None
    email: Optional[str] = None
    phone: Optional[str] = None
    status: Optional[int] = Field(default=1)
    create_time: Optional[datetime] = Field(default_factory=datetime.now)
    update_time: Optional[datetime] = Field(default_factory=datetime.now)

# 12. 前台用户模型
class User(BaseEntity, table=True):
    __tablename__ = "user"
    id: Optional[int] = Field(default=None, primary_key=True)
    username: str
    password: str = Field(exclude=True)
    nickname: Optional[str] = None
    avatar: Optional[str] = None
    email: Optional[str] = None
    phone: Optional[str] = None
    status: Optional[int] = Field(default=1)
    create_time: Optional[datetime] = Field(default_factory=datetime.now)
    update_time: Optional[datetime] = Field(default_factory=datetime.now)