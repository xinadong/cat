from sqlmodel import Session, select
from sqlalchemy import func


def paginate(session: Session, query, page_num: int, page_size: int) -> dict:
    # 获取总记录数
    count_query = select(func.count()).select_from(query.subquery())
    total = session.exec(count_query).one()

    # 获取当前页数据
    records = session.exec(query.offset((page_num - 1) * page_size).limit(page_size)).all()

    return {
        "records": records,
        "total": total,
        "size": page_size,
        "current": page_num
    }