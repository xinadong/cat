from fastapi import APIRouter, Depends, UploadFile, File
from sqlmodel import Session, select
from passlib.context import CryptContext
from models.entities import SysAdmin
from common.result import Result
from core.security import jwt_interceptor
from utils.jwt_util import generate_token
from utils.file_upload_util import upload_file

from database import get_session

router = APIRouter(prefix="/api/admin", tags=["Admin Auth"])
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")


@router.post("/login", response_model=Result[dict])
def login(admin: dict, session: Session = Depends(get_session)):
    username = admin.get("username")
    password = admin.get("password")

    if not username or not password:
        return Result.error("Username or password cannot be empty")

    db_admin = session.exec(select(SysAdmin).where(SysAdmin.username == username)).first()

    # 验证管理员状态和密码
    if not db_admin or db_admin.status == 0 or not pwd_context.verify(password, db_admin.password):
        return Result.error("Invalid credentials or disabled account")

    token = generate_token(db_admin.id, db_admin.username)
    return Result.success(data={"token": token, "admin": db_admin})


@router.get("/info", response_model=Result[SysAdmin])
def get_admin_info(admin_id: int = Depends(jwt_interceptor), session: Session = Depends(get_session)):
    admin = session.get(SysAdmin, admin_id)
    return Result.success(data=admin)


@router.put("/update", response_model=Result[str])
def update_admin(admin: SysAdmin, admin_id: int = Depends(jwt_interceptor), session: Session = Depends(get_session)):
    db_admin = session.get(SysAdmin, admin_id)
    if not db_admin:
        return Result.error("Admin not found")

    # 不允许通过此接口修改密码和用户名
    update_data = admin.model_dump(exclude_unset=True, exclude={"password", "username", "id"})
    for key, value in update_data.items():
        setattr(db_admin, key, value)

    session.add(db_admin)
    session.commit()
    return Result.success(message="Success")


@router.post("/upload-avatar", response_model=Result[dict])
async def upload_avatar(file: UploadFile = File(...)):
    try:
        url = await upload_file(file)
        return Result.success(data={"url": url})
    except Exception as e:
        return Result.error(str(e))