import os
from contextlib import asynccontextmanager
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from fastapi.staticfiles import StaticFiles
import uvicorn

# 引入数据库初始化方法
from database import init_db

# ==================== 导入前台路由 (User) ====================
from routers.user.user_router import router as user_router
from routers.user.home_router import router as home_router
from routers.user.animal_router import router as animal_router
from routers.user.article_router import router as article_router
from routers.user.interaction_router import router as interaction_router
from routers.user.personal_router import router as personal_router
from routers.user.ai_assistant_router import router as ai_assistant_router
from routers.user.cat_emoji_router import router as cat_emoji_router
# ==================== 导入后台路由 (Admin) ====================
from routers.admin.admin_auth_router import router as admin_auth_router
from routers.admin.admin_dashboard_router import router as admin_dashboard_router
from routers.admin.admin_crud_router import router as admin_crud_router
from routers.admin.admin_stats_router import router as admin_stats_router

# 定义生命周期事件，在项目启动时自动建表
@asynccontextmanager
async def lifespan(app: FastAPI):
    # 确保上传目录存在
    os.makedirs("uploads", exist_ok=True)
    # 初始化数据库表结构（如果表不存在则自动创建）
    init_db()
    yield

# 初始化 FastAPI 应用
app = FastAPI(title="Animal API", version="1.0.0", lifespan=lifespan)

# ==================== 跨域配置 (CORS) ====================
# 对标原版 Java 的 WebConfig.java -> addCorsMappings[cite: 1]
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # 允许所有源
    allow_credentials=True,
    allow_methods=["GET", "POST", "PUT", "DELETE", "OPTIONS"],
    allow_headers=["*"],
    max_age=3600,
)

# ==================== 静态资源映射 ====================
# 对标原版 Java 的 WebConfig.java -> addResourceHandlers[cite: 1]
os.makedirs("uploads", exist_ok=True)
app.mount("/uploads", StaticFiles(directory="uploads"), name="uploads")

# ==================== 注册前台路由 ====================
app.include_router(user_router)
app.include_router(home_router)
app.include_router(animal_router)
app.include_router(article_router)
app.include_router(interaction_router)
app.include_router(personal_router)
app.include_router(ai_assistant_router)
app.include_router(cat_emoji_router)

# ==================== 注册后台路由 ====================
app.include_router(admin_auth_router)
app.include_router(admin_dashboard_router)
app.include_router(admin_crud_router)
app.include_router(admin_stats_router)


if __name__ == "__main__":
    # 启动命令
    # reload=True 开启热更新，修改代码后自动重启
    uvicorn.run("main:app", host="0.0.0.0", port=8088, reload=True)