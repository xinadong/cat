from fastapi import APIRouter, Depends
from sqlmodel import Session, select
from models.entities import Banner, Animal, Category
from common.result import Result
from database import get_session

router = APIRouter(prefix="/api/home", tags=["Home"])


@router.get("/banner", response_model=Result[list[Banner]])
def get_banner(session: Session = Depends(get_session)):
    banners = session.exec(select(Banner).where(Banner.status == 1).order_by(Banner.sort)).all()
    return Result.success(data=banners)


@router.get("/hot-animals", response_model=Result[list[Animal]])
def get_hot_animals(session: Session = Depends(get_session)):
    animals = session.exec(
        select(Animal).where(Animal.status == 1).order_by(Animal.view_count.desc()).limit(8)
    ).all()
    return Result.success(data=animals)


@router.get("/new-animals", response_model=Result[list[Animal]])
def get_new_animals(session: Session = Depends(get_session)):
    animals = session.exec(
        select(Animal).where(Animal.status == 1).order_by(Animal.create_time.desc()).limit(8)
    ).all()
    return Result.success(data=animals)


@router.get("/categories", response_model=Result[list[Category]])
def get_categories(session: Session = Depends(get_session)):
    categories = session.exec(select(Category).order_by(Category.sort)).all()
    return Result.success(data=categories)


@router.get("/data", response_model=Result[dict])
def get_home_data(session: Session = Depends(get_session)):
    banners = session.exec(select(Banner).where(Banner.status == 1).order_by(Banner.sort)).all()
    hot_animals = session.exec(
        select(Animal).where(Animal.status == 1).order_by(Animal.view_count.desc()).limit(8)).all()
    new_animals = session.exec(
        select(Animal).where(Animal.status == 1).order_by(Animal.create_time.desc()).limit(8)).all()
    categories = session.exec(select(Category).order_by(Category.sort)).all()

    return Result.success(data={
        "banners": banners,
        "hotAnimals": hot_animals,
        "newAnimals": new_animals,
        "categories": categories
    })