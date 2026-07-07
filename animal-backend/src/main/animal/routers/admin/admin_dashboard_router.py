from fastapi import APIRouter, Depends
from sqlmodel import Session, select
from sqlalchemy import func
from datetime import datetime, time
from dateutil.relativedelta import relativedelta
from models.entities import User, Animal, Article, Category
from common.result import Result
from core.security import jwt_interceptor

from database import get_session

router = APIRouter(prefix="/api/admin/dashboard", tags=["Admin Dashboard"], dependencies=[Depends(jwt_interceptor)])


@router.get("/stats", response_model=Result[dict])
def get_stats(session: Session = Depends(get_session)):
    user_count = session.exec(select(func.count(User.id))).one()
    animal_count = session.exec(select(func.count(Animal.id))).one()
    article_count = session.exec(select(func.count(Article.id))).one()

    # 统计所有动物的浏览量之和
    total_views = session.exec(select(func.sum(Animal.view_count))).one() or 0

    return Result.success(data={
        "userCount": user_count,
        "animalCount": animal_count,
        "articleCount": article_count,
        "todayVisit": total_views
    })


@router.get("/trend-stats", response_model=Result[dict])
def get_trend_stats(session: Session = Depends(get_session)):
    months = []
    user_counts = []
    animal_counts = []
    article_counts = []

    now = datetime.now()

    # 模拟 Java 中的 for (int i = 5; i >= 0; i--)
    for i in range(5, -1, -1):
        target_month = now - relativedelta(months=i)
        month_str = target_month.strftime("%Y-%m")
        months.append(month_str)

        # 计算该月最后一天的时间界限
        next_month = target_month + relativedelta(months=1)
        end_of_month = datetime(next_month.year, next_month.month, 1) - relativedelta(seconds=1)

        user_c = session.exec(select(func.count(User.id)).where(User.create_time <= end_of_month)).one()
        animal_c = session.exec(select(func.count(Animal.id)).where(Animal.create_time <= end_of_month)).one()
        article_c = session.exec(select(func.count(Article.id)).where(Article.create_time <= end_of_month)).one()

        user_counts.append(user_c)
        animal_counts.append(animal_c)
        article_counts.append(article_c)

    return Result.success(data={
        "months": months,
        "userCounts": user_counts,
        "animalCounts": animal_counts,
        "articleCounts": article_counts
    })


@router.get("/category-stats", response_model=Result[list[dict]])
def get_category_stats(session: Session = Depends(get_session)):
    categories = session.exec(select(Category)).all()
    result = []
    for cat in categories:
        count = session.exec(select(func.count(Animal.id)).where(Animal.category_id == cat.id)).one()
        result.append({"name": cat.name, "value": count})
    return Result.success(data=result)

@router.get("/hot-animals", response_model=Result[list[Animal]])
def get_admin_dashboard_hot_animals(session: Session = Depends(get_session)):
    return Result.success(data=session.exec(select(Animal).order_by(Animal.view_count.desc()).limit(10)).all())

@router.get("/user-status-stats", response_model=Result[list[dict]])
def get_user_status_stats(session: Session = Depends(get_session)):
    normal_count = session.exec(select(func.count(User.id)).where(User.status == 1)).one()
    disabled_count = session.exec(select(func.count(User.id)).where(User.status == 0)).one()
    return Result.success(data=[
        {"name": "正常", "value": normal_count},
        {"name": "禁用", "value": disabled_count}
    ])