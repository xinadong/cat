from sqlmodel import create_engine, Session, SQLModel
# 请确保在此处导入您的模型，以便 SQLModel 能够识别并自动建表
from models.entities import *

# 配置您的数据库连接 URL
# 这里以 MySQL 为例，格式为：mysql+pymysql://用户名:密码@主机地址:端口/数据库名
# 注意：使用 MySQL 需要先 pip install pymysql
SQLALCHEMY_DATABASE_URL = "mysql+pymysql://root:123456@localhost:3306/animal_db?charset=utf8mb4"

# 如果您想先用 SQLite 本地轻量级数据库测试，可以换成下面这行：
# SQLALCHEMY_DATABASE_URL = "sqlite:///./animal.db"

# 创建数据库引擎
engine = create_engine(SQLALCHEMY_DATABASE_URL, echo=True)

# 初始化数据库（可选：应用启动时自动在数据库中创建缺失的表）
def init_db():
    SQLModel.metadata.create_all(engine)

# FastAPI 的依赖注入函数，每次请求时提供一个独立的数据库 Session
def get_session():
    with Session(engine) as session:
        yield session