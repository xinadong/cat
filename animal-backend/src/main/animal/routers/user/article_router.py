from fastapi import APIRouter, Depends, Query
from sqlmodel import Session, select
from models.entities import Article
from common.result import Result
from utils.pagination import paginate
from database import get_session

router = APIRouter(prefix="/api/article", tags=["Article"])


@router.get("/list", response_model=Result[dict])
def get_article_list(
        page_num: int = Query(1, alias="pageNum"),
        page_size: int = Query(10, alias="pageSize"),
        category: str = None,
        session: Session = Depends(get_session)
):
    query = select(Article).where(Article.status == 1)
    if category:
        query = query.where(Article.category == category)

    query = query.order_by(Article.create_time.desc())
    page_data = paginate(session, query, page_num, page_size)
    return Result.success(data=page_data)


@router.get("/detail/{id}", response_model=Result[Article])
def get_article_detail(id: int, session: Session = Depends(get_session)):
    article = session.get(Article, id)
    if not article:
        return Result.error("Article not found")

    article.view_count += 1
    session.add(article)
    session.commit()
    session.refresh(article)
    return Result.success(data=article)