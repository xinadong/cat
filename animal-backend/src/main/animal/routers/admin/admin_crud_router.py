from fastapi import APIRouter, Depends, Query,UploadFile, File
from sqlmodel import Session, select, or_
from models.entities import Animal, User,AiKnowledge, Article, Banner, Category, Comment, Question, Answer
from common.result import Result
from core.security import jwt_interceptor
from utils.pagination import paginate
from utils.file_upload_util import upload_file

from database import get_session


# 给所有的后台接口加上拦截器依赖
router = APIRouter(prefix="/api/admin", tags=["Admin Content"], dependencies=[Depends(jwt_interceptor)])


# ==================== 动物管理 (AdminAnimalController) ====================
@router.get("/animal/list", response_model=Result[dict])
def get_animal_list(
        page_num: int = Query(1, alias="pageNum"),
        page_size: int = Query(10, alias="pageSize"),
        keyword: str = None,
        session: Session = Depends(get_session)
):
    query = select(Animal)
    if keyword:
        query = query.where(or_(Animal.name.contains(keyword), Animal.scientific_name.contains(keyword)))

    query = query.order_by(Animal.create_time.desc())
    page_data = paginate(session, query, page_num, page_size)
    return Result.success(data=page_data)


@router.post("/animal/add", response_model=Result[str])
def add_animal(animal: Animal, session: Session = Depends(get_session)):
    session.add(animal)
    session.commit()
    return Result.success(message="Success")


@router.put("/animal/update", response_model=Result[str])
def update_animal(animal: Animal, session: Session = Depends(get_session)):
    if not animal.id:
        return Result.error("ID is required")

    db_animal = session.get(Animal, animal.id)
    if not db_animal:
        return Result.error("Animal not found")

    update_data = animal.model_dump(exclude_unset=True)
    for key, value in update_data.items():
        setattr(db_animal, key, value)

    session.add(db_animal)
    session.commit()
    return Result.success(message="Success")


@router.delete("/animal/delete/{id}", response_model=Result[str])
def delete_animal(id: int, session: Session = Depends(get_session)):
    animal = session.get(Animal, id)
    if animal:
        session.delete(animal)
        session.commit()
        return Result.success(message="Success")
    return Result.error("Animal not found")

@router.post("/animal/upload-image", response_model=Result[dict])
async def upload_admin_animal_image(file: UploadFile = File(...)):
    url = await upload_file(file)
    return Result.success(data={"url": url})

@router.get("/animal/detail/{id}", response_model=Result[Animal])
def get_admin_animal_detail(id: int, session: Session = Depends(get_session)):
    animal = session.get(Animal, id)
    if not animal:
        return Result.error("Animal not found")
    return Result.success(data=animal)
# ==================== 用户管理 (AdminUserController) ====================
@router.get("/user/list", response_model=Result[dict])
def get_user_list(
        page_num: int = Query(1, alias="pageNum"),
        page_size: int = Query(10, alias="pageSize"),
        keyword: str = None,
        session: Session = Depends(get_session)
):
    query = select(User)
    if keyword:
        query = query.where(or_(User.username.contains(keyword), User.nickname.contains(keyword)))

    query = query.order_by(User.create_time.desc())
    page_data = paginate(session, query, page_num, page_size)
    return Result.success(data=page_data)


@router.put("/user/status/{id}", response_model=Result[str])
def update_user_status(id: int, status: int = Query(...), session: Session = Depends(get_session)):
    user = session.get(User, id)
    if not user:
        return Result.error("User not found")

    user.status = status
    session.add(user)
    session.commit()
    return Result.success(message="Success")


@router.get("/user/detail/{id}", response_model=Result[User])
def get_user_detail(id: int, session: Session = Depends(get_session)):
    user = session.get(User, id)
    if not user:
        return Result.error("User not found")
    return Result.success(data=user)





# ==================== AI 知识库管理 ====================
@router.get("/ai-knowledge/list", response_model=Result[dict])
def get_ai_knowledge_list(
        page_num: int = Query(1, alias="pageNum"),
        page_size: int = Query(10, alias="pageSize"),
        keyword: str = None,
        category: str = None,
        session: Session = Depends(get_session)
):
    query = select(AiKnowledge)
    if keyword:
        query = query.where(
            or_(AiKnowledge.title.contains(keyword),
                AiKnowledge.content.contains(keyword),
                AiKnowledge.tags.contains(keyword))
        )
    if category:
        query = query.where(AiKnowledge.category == category)

    query = query.order_by(AiKnowledge.create_time.desc())
    return Result.success(data=paginate(session, query, page_num, page_size))


@router.post("/ai-knowledge/add", response_model=Result[str])
def add_ai_knowledge(knowledge: AiKnowledge, session: Session = Depends(get_session)):
    session.add(knowledge)
    session.commit()
    return Result.success(message="Success")


@router.put("/ai-knowledge/update", response_model=Result[str])
def update_ai_knowledge(knowledge: AiKnowledge, session: Session = Depends(get_session)):
    db_knowledge = session.get(AiKnowledge, knowledge.id)
    if not db_knowledge:
        return Result.error("Not found")
    update_data = knowledge.model_dump(exclude_unset=True)
    for key, value in update_data.items():
        setattr(db_knowledge, key, value)
    session.add(db_knowledge)
    session.commit()
    return Result.success(message="Success")


@router.delete("/ai-knowledge/delete/{id}", response_model=Result[str])
def delete_ai_knowledge(id: int, session: Session = Depends(get_session)):
    knowledge = session.get(AiKnowledge, id)
    if knowledge:
        session.delete(knowledge)
        session.commit()
        return Result.success(message="Success")
    return Result.error("Not found")


@router.delete("/ai-knowledge/delete-batch", response_model=Result[str])
def delete_ai_knowledge_batch(ids: list[int], session: Session = Depends(get_session)):
    session.exec(select(AiKnowledge).where(AiKnowledge.id.in_(ids))).all()
    for id in ids:
        knowledge = session.get(AiKnowledge, id)
        if knowledge:
            session.delete(knowledge)
    session.commit()
    return Result.success(message="Success")


@router.put("/ai-knowledge/toggle-status/{id}", response_model=Result[str])
def toggle_ai_knowledge_status(id: int, session: Session = Depends(get_session)):
    knowledge = session.get(AiKnowledge, id)
    if not knowledge:
        return Result.error("Not found")
    knowledge.status = 0 if knowledge.status == 1 else 1
    session.add(knowledge)
    session.commit()
    return Result.success(message="Success")


# ==================== 科普文章管理 ====================
@router.get("/article/list", response_model=Result[dict])
def get_admin_article_list(
        page_num: int = Query(1, alias="pageNum"),
        page_size: int = Query(10, alias="pageSize"),
        keyword: str = None,
        session: Session = Depends(get_session)
):
    query = select(Article)
    if keyword:
        query = query.where(Article.title.contains(keyword))
    query = query.order_by(Article.create_time.desc())
    return Result.success(data=paginate(session, query, page_num, page_size))


@router.post("/article/add", response_model=Result[str])
def add_article(article: Article, session: Session = Depends(get_session)):
    session.add(article)
    session.commit()
    return Result.success(message="Success")


@router.put("/article/update", response_model=Result[str])
def update_article(article: Article, session: Session = Depends(get_session)):
    db_article = session.get(Article, article.id)
    update_data = article.model_dump(exclude_unset=True)
    for key, value in update_data.items():
        setattr(db_article, key, value)
    session.add(db_article)
    session.commit()
    return Result.success(message="Success")


@router.delete("/article/delete/{id}", response_model=Result[str])
def delete_article(id: int, session: Session = Depends(get_session)):
    article = session.get(Article, id)
    if article:
        session.delete(article)
        session.commit()
        return Result.success(message="Success")
    return Result.error("Not found")


@router.put("/article/publish/{id}", response_model=Result[str])
def publish_article(id: int, session: Session = Depends(get_session)):
    article = session.get(Article, id)
    if not article:
        return Result.error("Not found")
    article.status = 1
    session.add(article)
    session.commit()
    return Result.success(message="Success")


@router.post("/article/upload-cover", response_model=Result[dict])
async def upload_article_cover(file: UploadFile = File(...)):
    url = await upload_file(file)
    return Result.success(data={"url": url})


@router.get("/article/detail/{id}", response_model=Result[Article])
def get_admin_article_detail(id: int, session: Session = Depends(get_session)):
    return Result.success(data=session.get(Article, id))


# ==================== 轮播图管理 ====================
@router.get("/banner/list", response_model=Result[list[Banner]])
def get_admin_banner_list(session: Session = Depends(get_session)):
    return Result.success(data=session.exec(select(Banner).order_by(Banner.sort)).all())


@router.post("/banner/add", response_model=Result[str])
def add_banner(banner: Banner, session: Session = Depends(get_session)):
    session.add(banner)
    session.commit()
    return Result.success(message="Success")


@router.put("/banner/update", response_model=Result[str])
def update_banner(banner: Banner, session: Session = Depends(get_session)):
    db_banner = session.get(Banner, banner.id)
    for key, value in banner.model_dump(exclude_unset=True).items():
        setattr(db_banner, key, value)
    session.add(db_banner)
    session.commit()
    return Result.success(message="Success")


@router.delete("/banner/delete/{id}", response_model=Result[str])
def delete_banner(id: int, session: Session = Depends(get_session)):
    banner = session.get(Banner, id)
    if banner:
        session.delete(banner)
        session.commit()
        return Result.success(message="Success")
    return Result.error("Not found")


@router.post("/banner/upload-image", response_model=Result[dict])
async def upload_banner_image(file: UploadFile = File(...)):
    url = await upload_file(file)
    return Result.success(data={"url": url})


# ==================== 分类管理 ====================
@router.get("/category/list", response_model=Result[list[Category]])
def get_admin_category_list(session: Session = Depends(get_session)):
    return Result.success(data=session.exec(select(Category).order_by(Category.sort)).all())


@router.post("/category/add", response_model=Result[str])
def add_category(category: Category, session: Session = Depends(get_session)):
    session.add(category)
    session.commit()
    return Result.success(message="Success")


@router.put("/category/update", response_model=Result[str])
def update_category(category: Category, session: Session = Depends(get_session)):
    db_cat = session.get(Category, category.id)
    for key, value in category.model_dump(exclude_unset=True).items():
        setattr(db_cat, key, value)
    session.add(db_cat)
    session.commit()
    return Result.success(message="Success")


@router.delete("/category/delete/{id}", response_model=Result[str])
def delete_category(id: int, session: Session = Depends(get_session)):
    cat = session.get(Category, id)
    if cat:
        session.delete(cat)
        session.commit()
        return Result.success(message="Success")
    return Result.error("Not found")


@router.post("/category/upload-icon", response_model=Result[str])
async def upload_category_icon(file: UploadFile = File(...)):
    url = await upload_file(file)
    return Result.success(data=url)


# ==================== 评论管理 ====================
@router.get("/comment/list", response_model=Result[dict])
def get_admin_comment_list(
        page_num: int = Query(1, alias="pageNum"),
        page_size: int = Query(10, alias="pageSize"),
        session: Session = Depends(get_session)
):
    query = select(Comment).order_by(Comment.create_time.desc())
    page_data = paginate(session, query, page_num, page_size)

    records = []
    for comment in page_data["records"]:
        user = session.get(User, comment.user_id)
        animal = session.get(Animal, comment.animal_id)
        records.append({"comment": comment, "user": user, "animal": animal})

    page_data["records"] = records
    return Result.success(data=page_data)


@router.delete("/comment/delete/{id}", response_model=Result[str])
def delete_comment(id: int, session: Session = Depends(get_session)):
    comment = session.get(Comment, id)
    if comment:
        session.delete(comment)
        session.commit()
        return Result.success(message="Success")
    return Result.error("Not found")


@router.put("/comment/audit/{id}", response_model=Result[str])
def audit_comment(id: int, status: int = Query(...), session: Session = Depends(get_session)):
    comment = session.get(Comment, id)
    if not comment:
        return Result.error("Not found")
    comment.status = status
    session.add(comment)
    session.commit()
    return Result.success(message="Success")


# ==================== 问答社区管理 ====================
@router.get("/question/list", response_model=Result[dict])
def get_admin_question_list(
        page_num: int = Query(1, alias="pageNum"),
        page_size: int = Query(10, alias="pageSize"),
        session: Session = Depends(get_session)
):
    query = select(Question).order_by(Question.create_time.desc())
    page_data = paginate(session, query, page_num, page_size)

    records = []
    for question in page_data["records"]:
        user = session.get(User, question.user_id)
        records.append({"question": question, "user": user})

    page_data["records"] = records
    return Result.success(data=page_data)


@router.delete("/question/delete/{id}", response_model=Result[str])
def delete_question(id: int, session: Session = Depends(get_session)):
    question = session.get(Question, id)
    if question:
        session.delete(question)
        session.commit()
        return Result.success(message="Success")
    return Result.error("Not found")


@router.put("/question/top/{id}", response_model=Result[str])
def top_question(id: int, is_top: int = Query(..., alias="isTop"), session: Session = Depends(get_session)):
    question = session.get(Question, id)
    if not question:
        return Result.error("Not found")
    question.is_top = is_top
    session.add(question)
    session.commit()
    return Result.success(message="Success")