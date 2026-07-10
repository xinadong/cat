from fastapi import APIRouter, UploadFile, File, Depends, Form
from sqlmodel import Session, select
from typing import Optional

from models.entities import CatEmoji
from utils.file_upload_util import upload_file

# 假设你的项目中有统一的数据库 session 依赖和结果返回类，例如：
from database import get_session
from common.result import Result

router = APIRouter(prefix="/api/cat-emoji", tags=["猫猫表情包模块"])
print("============== 猫猫表情包路由已被成功读取！ ==============")
@router.post("/upload")
async def upload_cat_emoji(
    file: UploadFile = File(...),
    title: Optional[str] = Form(None),
    animalId: Optional[int] = Form(None),
    session: Session = Depends(get_session)
):
    """上传猫猫表情包并保存至数据库"""
    try:
        # 1. 调用现有的上传工具类保存物理文件
        file_url = await upload_file(file)

        # 2. 构建数据库记录实体
        new_emoji = CatEmoji(
            title=title or file.filename,
            image_url=file_url,
            animal_id=animalId
        )

        # 3. 存入数据库
        session.add(new_emoji)
        session.commit()
        session.refresh(new_emoji)

        # 这里假设直接返回字典，建议按项目规范换成统一的 Result 格式
        return {"code": 200, "message": "上传成功", "data": new_emoji}
    except Exception as e:
        return {"code": 500, "message": f"上传失败: {str(e)}"}


@router.get("/list")
def get_emoji_list(
    page: int = 1,
    size: int = 20,
    session: Session = Depends(get_session)
):
    """分页获取猫猫表情包列表供前端展示"""
    offset = (page - 1) * size
    statement = select(CatEmoji).where(CatEmoji.status == 1).order_by(CatEmoji.create_time.desc()).offset(offset).limit(size)
    emojis = session.exec(statement).all()

    return {"code": 200, "data": emojis}
    pass  # 实际开发时请解开注释即可完成数据库查询


@router.delete("/delete/{emoji_id}")
async def delete_emoji(
    emoji_id: int,
    session: Session = Depends(get_session)
):
    """删除指定的猫猫表情包 (逻辑删除或物理删除均可)"""
    emoji = session.get(CatEmoji, emoji_id)
    if not emoji:
         return {"code": 404, "message": "表情包不存在"}

    emoji.status = 0 # 推荐逻辑删除
    session.add(emoji)
    session.commit()
    return {"code": 200, "message": "删除成功"}
    pass