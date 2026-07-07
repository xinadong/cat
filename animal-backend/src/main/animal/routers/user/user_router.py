from fastapi import APIRouter, Depends, UploadFile, File
from sqlmodel import Session, select
from passlib.context import CryptContext

from models.entities import User
from common.result import Result
from core.security import jwt_interceptor
from utils.jwt_util import generate_token
from utils.file_upload_util import upload_file
from database import get_session

router = APIRouter(prefix="/api/user", tags=["User"])
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")


@router.post("/register", response_model=Result[str])
def register(user: User, session: Session = Depends(get_session)):
    if not user.username or not user.password or not user.nickname or not user.email:
        return Result.error("Required fields missing")

    # 检查用户名是否已存在
    existing = session.exec(select(User).where(User.username == user.username)).first()
    if existing:
        return Result.error("Registration failed, username already exists")

    # 密码加密
    user.password = pwd_context.hash(user.password)
    user.status = 1

    # 设置默认头像
    if not user.avatar:
        user.avatar = "https://pic57.photophoto.cn/20201125/0012024436462829_b.jpg"

    session.add(user)
    session.commit()
    return Result.success(message="Success")


@router.post("/login", response_model=Result[dict])
def login(user: dict, session: Session = Depends(get_session)):
    username = user.get("username")
    password = user.get("password")

    if not username or not password:
        return Result.error("Username or password cannot be empty")

    db_user = session.exec(select(User).where(User.username == username)).first()

    # 验证用户是否存在、状态是否正常，以及密码是否匹配
    if not db_user or db_user.status == 0 or not pwd_context.verify(password, db_user.password):
        return Result.error("Login failed")

    # 生成 JWT Token
    token = generate_token(db_user.id, db_user.username)

    return Result.success(data={"token": token, "user": db_user})


@router.get("/info", response_model=Result[User])
def get_user_info(user_id: int = Depends(jwt_interceptor), session: Session = Depends(get_session)):
    user = session.get(User, user_id)
    if not user:
        return Result.error("User not found")
    return Result.success(data=user)


@router.put("/update", response_model=Result[str])
def update_user(user: User, user_id: int = Depends(jwt_interceptor), session: Session = Depends(get_session)):
    db_user = session.get(User, user_id)
    if not db_user:
        return Result.error("User not found")

    # 防止通过普通更新接口修改核心字段
    update_data = user.model_dump(exclude_unset=True, exclude={"password", "username", "id"})
    for key, value in update_data.items():
        setattr(db_user, key, value)

    session.add(db_user)
    session.commit()
    return Result.success(message="Success")


@router.post("/upload-avatar", response_model=Result[dict])
async def upload_avatar(
        file: UploadFile = File(...),
        user_id: int = Depends(jwt_interceptor),
        session: Session = Depends(get_session)
):
    try:
        # 上传文件并获取路径
        url = await upload_file(file)

        # 更新用户头像字段
        user = session.get(User, user_id)
        if user:
            user.avatar = url
            session.add(user)
            session.commit()

        return Result.success(data={"url": url})
    except Exception as e:
        return Result.error(str(e))