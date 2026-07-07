import os
import uuid
import shutil
from fastapi import UploadFile

UPLOAD_PATH = "./uploads/"


def is_image_file(filename: str) -> bool:
    ext = filename.split('.')[-1].lower()
    return ext in ['jpg', 'jpeg', 'png', 'gif']


async def upload_file(file: UploadFile) -> str:
    if not file.filename:
        raise ValueError("File name is empty")
    if not is_image_file(file.filename):
        raise ValueError("Only jpg, jpeg, png, gif are allowed")

    os.makedirs(UPLOAD_PATH, exist_ok=True)

    suffix = f".{file.filename.split('.')[-1]}"
    new_file_name = f"{uuid.uuid4().hex}{suffix}"
    file_path = os.path.join(UPLOAD_PATH, new_file_name)

    with open(file_path, "wb") as buffer:
        shutil.copyfileobj(file.file, buffer)

    return f"/uploads/{new_file_name}"