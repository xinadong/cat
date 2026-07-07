from fastapi import APIRouter, Depends, Query
from sqlmodel import Session, select
from models.entities import Collect, Animal, Comment, User, Question, Answer
from common.result import Result
from core.security import jwt_interceptor
from database import get_session
from utils.pagination import paginate

router = APIRouter(tags=["Interaction"])


# ==================== 收藏 (CollectController) ====================
@router.post("/api/collect/add", response_model=Result[str])
def add_collect(payload: dict, user_id: int = Depends(jwt_interceptor), session: Session = Depends(get_session)):
    animal_id = payload.get("animalId")
    exists = session.exec(select(Collect).where(Collect.user_id == user_id, Collect.animal_id == animal_id)).first()
    if exists:
        return Result.error("Already collected")

    session.add(Collect(user_id=user_id, animal_id=animal_id))

    animal = session.get(Animal, animal_id)
    if animal:
        animal.collect_count += 1
        session.add(animal)

    session.commit()
    return Result.success(message="Success")


@router.delete("/api/collect/delete/{animal_id}", response_model=Result[str])
def delete_collect(animal_id: int, user_id: int = Depends(jwt_interceptor), session: Session = Depends(get_session)):
    collect = session.exec(select(Collect).where(Collect.user_id == user_id, Collect.animal_id == animal_id)).first()
    if collect:
        session.delete(collect)
        animal = session.get(Animal, animal_id)
        if animal:
            animal.collect_count = max(0, animal.collect_count - 1)
            session.add(animal)
        session.commit()
        return Result.success(message="Success")
    return Result.error("Not found")


# ==================== 评论 (CommentController) ====================
@router.post("/api/comment/add", response_model=Result[str])
def add_comment(comment: Comment, user_id: int = Depends(jwt_interceptor), session: Session = Depends(get_session)):
    comment.user_id = user_id
    comment.like_count = 0
    comment.status = 1
    session.add(comment)
    session.commit()
    return Result.success(message="Success")


@router.get("/api/comment/list", response_model=Result[list[dict]])
def get_comment_list(animal_id: int = Query(..., alias="animalId"), session: Session = Depends(get_session)):
    comments = session.exec(
        select(Comment).where(Comment.animal_id == animal_id, Comment.status == 1).order_by(Comment.create_time.desc())
    ).all()

    result = []
    for comment in comments:
        user = session.get(User, comment.user_id)
        result.append({"comment": comment, "user": user})
    return Result.success(data=result)


# ==================== 问答 (Question & Answer) ====================
@router.post("/api/question/add", response_model=Result[str])
def add_question(question: Question, user_id: int = Depends(jwt_interceptor), session: Session = Depends(get_session)):
    question.user_id = user_id
    session.add(question)
    session.commit()
    return Result.success(message="Success")


@router.get("/api/question/detail/{id}", response_model=Result[dict])
def get_question_detail(id: int, session: Session = Depends(get_session)):
    question = session.get(Question, id)
    if not question:
        return Result.error("Question not found")

    user = session.get(User, question.user_id)

    # 查询回答
    answers = session.exec(
        select(Answer).where(Answer.question_id == id).order_by(Answer.is_accepted.desc(), Answer.create_time.desc())
    ).all()

    answer_list = []
    for answer in answers:
        ans_user = session.get(User, answer.user_id)
        answer_list.append({"answer": answer, "user": ans_user})

    return Result.success(data={"question": question, "user": user, "answers": answer_list})


@router.put("/api/answer/accept/{id}", response_model=Result[str])
def accept_answer(id: int, user_id: int = Depends(jwt_interceptor), session: Session = Depends(get_session)):
    answer = session.get(Answer, id)
    if not answer:
        return Result.error("Answer not found")

    question = session.get(Question, answer.question_id)
    if question.user_id != user_id:
        return Result.error("No permission")

    # 将其他回答设为未采纳
    other_answers = session.exec(
        select(Answer).where(Answer.question_id == answer.question_id, Answer.is_accepted == 1)
    ).all()
    for other in other_answers:
        other.is_accepted = 0
        session.add(other)

    answer.is_accepted = 1
    session.add(answer)
    session.commit()
    return Result.success(message="Success")


# 补充 CollectController 的接口[cite: 1]
@router.get("/api/collect/list", response_model=Result[list[Animal]])
def get_collect_list(user_id: int = Depends(jwt_interceptor), session: Session = Depends(get_session)):
    collects = session.exec(
        select(Collect).where(Collect.user_id == user_id).order_by(Collect.create_time.desc())).all()
    animals = [session.get(Animal, c.animal_id) for c in collects if session.get(Animal, c.animal_id)]
    return Result.success(data=animals)


@router.get("/api/collect/check/{animal_id}", response_model=Result[dict])
def check_collect(animal_id: int, user_id: int = Depends(jwt_interceptor), session: Session = Depends(get_session)):
    is_collected = session.exec(
        select(Collect).where(Collect.user_id == user_id, Collect.animal_id == animal_id)).first() is not None
    return Result.success(data={"isCollected": is_collected})


# 补充 CommentController 的接口[cite: 1]
@router.post("/api/comment/like/{id}", response_model=Result[str])
def like_comment(id: int, session: Session = Depends(get_session)):
    comment = session.get(Comment, id)
    if not comment:
        return Result.error("Comment not found")
    comment.like_count += 1
    session.add(comment)
    session.commit()
    return Result.success(message="Success")


@router.delete("/api/comment/delete/{id}", response_model=Result[str])
def delete_comment(id: int, user_id: int = Depends(jwt_interceptor), session: Session = Depends(get_session)):
    comment = session.get(Comment, id)
    if not comment or comment.user_id != user_id:
        return Result.error("No permission or not found")
    session.delete(comment)
    session.commit()
    return Result.success(message="Success")


# 补充 QuestionController 的列表接口[cite: 1]
@router.get("/api/question/list", response_model=Result[dict])
def get_question_list(
        page_num: int = Query(1, alias="pageNum"),
        page_size: int = Query(10, alias="pageSize"),
        session: Session = Depends(get_session)
):
    query = select(Question).where(Question.status == 1).order_by(Question.is_top.desc(), Question.create_time.desc())
    page_data = paginate(session, query, page_num, page_size)

    records = []
    for q in page_data["records"]:
        user = session.get(User, q.user_id)
        records.append({"question": q, "user": user})
    page_data["records"] = records
    return Result.success(data=page_data)


# 补充 AnswerController 的添加回答接口[cite: 1]
@router.post("/api/answer/add", response_model=Result[str])
def add_answer(answer: Answer, user_id: int = Depends(jwt_interceptor), session: Session = Depends(get_session)):
    answer.user_id = user_id
    answer.is_accepted = 0
    session.add(answer)

    question = session.get(Question, answer.question_id)
    if question:
        question.answer_count += 1
        session.add(question)

    session.commit()
    return Result.success(message="Success")