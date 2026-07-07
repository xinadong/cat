from fastapi import APIRouter, Depends
from sqlmodel import Session, select
from models.entities import Collect, Animal, Comment, Question, BrowseHistory
from common.result import Result
from core.security import jwt_interceptor

from database import get_session

router = APIRouter(prefix="/api/personal", tags=["Personal"])


@router.get("/collects", response_model=Result[list[Animal]])
def get_my_collects(user_id: int = Depends(jwt_interceptor), session: Session = Depends(get_session)):
    collects = session.exec(
        select(Collect).where(Collect.user_id == user_id).order_by(Collect.create_time.desc())).all()
    animals = []
    for collect in collects:
        animal = session.get(Animal, collect.animal_id)
        if animal:
            animals.append(animal)
    return Result.success(data=animals)


@router.get("/history", response_model=Result[list[dict]])
def get_browse_history(user_id: int = Depends(jwt_interceptor), session: Session = Depends(get_session)):
    histories = session.exec(
        select(BrowseHistory).where(BrowseHistory.user_id == user_id).order_by(BrowseHistory.browse_time.desc()).limit(
            50)
    ).all()

    result = []
    for history in histories:
        animal = session.get(Animal, history.animal_id)
        if animal:
            result.append({"animal": animal, "browseTime": history.browse_time})

    return Result.success(data=result)


@router.delete("/history/clear", response_model=Result[None])
def clear_browse_history(user_id: int = Depends(jwt_interceptor), session: Session = Depends(get_session)):
    histories = session.exec(select(BrowseHistory).where(BrowseHistory.user_id == user_id)).all()
    for history in histories:
        session.delete(history)
    session.commit()
    return Result.success()

#[cite: 1]
@router.get("/comments", response_model=Result[list[dict]])
def get_my_comments(user_id: int = Depends(jwt_interceptor), session: Session = Depends(get_session)):
    comments = session.exec(select(Comment).where(Comment.user_id == user_id).order_by(Comment.create_time.desc())).all()
    result = [{"comment": c, "animal": session.get(Animal, c.animal_id)} for c in comments]
    return Result.success(data=result)

@router.get("/questions", response_model=Result[list[Question]])
def get_my_questions(user_id: int = Depends(jwt_interceptor), session: Session = Depends(get_session)):
    questions = session.exec(select(Question).where(Question.user_id == user_id).order_by(Question.create_time.desc())).all()
    return Result.success(data=questions)