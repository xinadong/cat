from fastapi import APIRouter, Depends, Query
from sqlmodel import Session, select, or_
from models.entities import Animal, BrowseHistory
from common.result import Result
from core.security import optional_jwt_interceptor
from utils.pagination import paginate
from datetime import datetime
from database import get_session
router = APIRouter(prefix="/api/animal", tags=["Animal"])


@router.get("/list", response_model=Result[dict])
def get_animal_list(
        page_num: int = Query(1, alias="pageNum"),
        page_size: int = Query(12, alias="pageSize"),
        category_id: int = Query(None, alias="categoryId"),
        keyword: str = None,
        session: Session = Depends(get_session)
):
    query = select(Animal).where(Animal.status == 1)

    if category_id:
        query = query.where(Animal.category_id == category_id)

    if keyword:
        query = query.where(or_(Animal.name.contains(keyword), Animal.scientific_name.contains(keyword)))

    query = query.order_by(Animal.create_time.desc())
    page_data = paginate(session, query, page_num, page_size)
    return Result.success(data=page_data)


@router.get("/detail/{animal_id}", response_model=Result[Animal])
def get_animal_detail(
        animal_id: int,
        user_id: int | None = Depends(optional_jwt_interceptor),
        session: Session = Depends(get_session)
):
    animal = session.get(Animal, animal_id)
    if not animal:
        return Result.error("Animal not found")

    # 增加浏览量
    animal.view_count += 1
    session.add(animal)

    # 如果用户已登录，记录浏览历史
    if user_id:
        history = session.exec(
            select(BrowseHistory).where(BrowseHistory.user_id == user_id, BrowseHistory.animal_id == animal_id)
        ).first()

        if history:
            history.browse_time = datetime.now()
            session.add(history)
        else:
            new_history = BrowseHistory(user_id=user_id, animal_id=animal_id)
            session.add(new_history)

    session.commit()
    session.refresh(animal)
    return Result.success(data=animal)


@router.get("/recommend", response_model=Result[list[Animal]])
def get_recommend_animals(
        category_id: int = Query(..., alias="categoryId"),
        current_id: int = Query(..., alias="currentId"),
        session: Session = Depends(get_session)
):
    animals = session.exec(
        select(Animal)
        .where(Animal.status == 1, Animal.category_id == category_id, Animal.id != current_id)
        .order_by(Animal.view_count.desc())
        .limit(4)
    ).all()
    return Result.success(data=animals)


@router.post("/vote/{animal_id}")
async def vote_animal(animal_id: int, session: Session = Depends(get_session)):
    """专属投票接口：一票一票投出来"""
    try:
        # 1. 查出这只猫咪
        animal = session.get(Animal, animal_id)
        if not animal:
            return {"code": 404, "message": "未找到该猫咪"}

        # 2. 投票数 +1
        animal.vote_count = (animal.vote_count or 0) + 1

        # 3. 保存回数据库
        session.add(animal)
        session.commit()

        return {"code": 200, "message": "投票成功", "data": animal.vote_count}
    except Exception as e:
        return {"code": 500, "message": f"投票失败: {str(e)}"}