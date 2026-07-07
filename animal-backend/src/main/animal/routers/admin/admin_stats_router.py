from fastapi import APIRouter, Depends
from sqlmodel import Session, select
from sqlalchemy import func
from models.entities import Animal, User
from common.result import Result
from core.security import jwt_interceptor
from database import get_session

router = APIRouter(prefix="/api/admin/stats", tags=["Admin Stats"], dependencies=[Depends(jwt_interceptor)])

@router.get("/visit", response_model=Result[dict])
def get_visit_stats(session: Session = Depends(get_session)):
    animals = session.exec(select(Animal)).all()
    total_views = sum(a.view_count for a in animals)
    return Result.success(data={
        "totalViews": total_views,
        "animalCount": len(animals),
        "avgViews": total_views // len(animals) if animals else 0
    })

@router.get("/user-active", response_model=Result[dict])
def get_user_active_stats(session: Session = Depends(get_session)):
    total_users = session.exec(select(func.count(User.id))).one()
    active_users = session.exec(select(func.count(User.id)).where(User.status == 1)).one()
    return Result.success(data={
        "totalUsers": total_users,
        "activeUsers": active_users,
        "inactiveUsers": total_users - active_users
    })

@router.get("/animal-rank", response_model=Result[list[Animal]])
def get_animal_rank(session: Session = Depends(get_session)):
    animals = session.exec(select(Animal).order_by(Animal.view_count.desc()).limit(20)).all()
    return Result.success(data=animals)

@router.get("/collect-rank", response_model=Result[list[Animal]])
def get_collect_rank(session: Session = Depends(get_session)):
    animals = session.exec(select(Animal).order_by(Animal.collect_count.desc()).limit(20)).all()
    return Result.success(data=animals)