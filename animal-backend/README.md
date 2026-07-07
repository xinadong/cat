# 动物科普网站后端

## 项目介绍

## 技术栈
核心框架: FastAPI (高性能、易于学习、基于标准 Python 类型提示)

Web 服务器: Uvicorn (轻量级、超快速的 ASGI 服务器)

数据库 ORM: SQLModel / SQLAlchemy (兼具 Pydantic 的数据验证和 SQLAlchemy 的强大数据库操作能力)

数据库: MySQL (生产环境) / SQLite (本地开发测试)

安全与认证: PyJWT (JWT Token 生成与解析), Passlib & Bcrypt (密码哈希加密)

AI 接口: 深度集成 DeepSeek 大语言模型 API

跨域处理: FastAPI 内置 CORSMiddleware

## 项目结构
```
animal-backend/
├── common/                # 公共模块
│   └── result.py          # 统一的 API 响应封装格式 (如：code, message, data)
├── core/                  # 核心配置模块
│   └── security.py        # 密码加密/校验及 Token 相关的核心安全逻辑
├── models/                # 数据库模型与 Pydantic 验证模型
│   └── entities.py        # 数据表结构定义 (User, Animal, Article, Question, Comment 等)
├── routers/               # 路由层 (API 接口定义)
│   ├── admin/             # 后台管理端接口 (需管理员权限)
│   │   ├── admin_auth_router.py
│   │   ├── admin_crud_router.py
│   │   ├── admin_dashboard_router.py
│   │   └── admin_stats_router.py
│   └── user/              # 前台用户端接口
│       ├── ai_assistant_router.py  # AI 科普助手对话接口
│       ├── animal_router.py        # 动物百科相关接口
│       ├── article_router.py       # 科普文章相关接口
│       ├── home_router.py          # 首页数据接口
│       ├── interaction_router.py   # 评论、收藏等互动接口
│       ├── personal_router.py      # 用户个人中心接口
│       └── user_router.py          # 用户登录/注册接口
├── utils/                 # 工具类模块
│   ├── file_upload_util.py# 文件/图片上传处理工具
│   ├── jwt_util.py        # JWT 签发与解析工具
│   └── pagination.py      # 分页处理工具
├── uploads/               # 静态资源目录 (存放用户上传的头像、动物图片等)
├── database.py            # 数据库引擎与会话初始化配置
├── main.py                # 🚀 项目主入口，负责挂载路由、中间件及启动服务
└── requirements.txt       # 项目依赖清单
```

## 快速开始

### 1. 环境准备
Python 3.8 或更高版本

MySQL 8.0+ (如果使用 MySQL)

### 2. 创建并激活虚拟环境 (推荐)
为了避免依赖冲突，建议使用 Anaconda 或 Python 内置的 venv 创建独立的虚拟环境。
```angular2html
# 使用 conda (示例)
conda create -n animal_env python=3.10
conda activate animal_env

# 或使用 Python venv
python -m venv venv
# Windows:
venv\Scripts\activate
# macOS/Linux:
source venv/bin/activate
```
### 3. 安装依赖
请务必按照 requirements.txt 中的指定版本安装，以避免第三方库（如 bcrypt）的版本冲突问题。
```angular2html
pip install -r requirements.txt
```
### 4. 数据库配置
打开项目根目录下的 database.py，根据您的本地环境修改数据库连接字符串：

使用 MySQL（推荐）：
```angular2html
# 将 root 和 123456 修改为您本地 MySQL 的账号密码
SQLALCHEMY_DATABASE_URL = "mysql+pymysql://root:123456@localhost:3306/animal_db?charset=utf8mb4"
```
注意：在启动前，请确保已经在 MySQL 中手动创建了名为 animal_db 的空数据库。
### 5. 启动服务
在项目根目录（main.py 所在目录）下执行以下命令启动服务器：
```angular2html
python main.py
```
启动成功后，终端会显示 Uvicorn running on [http://0.0.0.0:8080](http://0.0.0.0:8080)。
## 接口说明

### 用户端接口

#### 1. 用户认证
- `POST /api/user/register` - 用户注册
- `POST /api/user/login` - 用户登录
- `GET /api/user/info` - 获取用户信息
- `PUT /api/user/update` - 更新用户信息
- `POST /api/user/upload-avatar` - 上传头像

#### 2. 首页
- `GET /api/home/banner` - 获取轮播图
- `GET /api/home/hot-animals` - 获取热门动物
- `GET /api/home/new-animals` - 获取最新动物
- `GET /api/home/categories` - 获取分类列表
- `GET /api/home/data` - 获取首页所有数据

#### 3. 动物
- `GET /api/animal/list` - 动物列表（分页）
- `GET /api/animal/detail/{id}` - 动物详情
- `GET /api/animal/recommend` - 相关动物推荐

#### 4. 收藏
- `POST /api/collect/add` - 添加收藏
- `DELETE /api/collect/delete/{animalId}` - 取消收藏
- `GET /api/collect/list` - 我的收藏列表
- `GET /api/collect/check/{animalId}` - 检查是否已收藏

#### 5. 评论
- `POST /api/comment/add` - 发表评论
- `GET /api/comment/list` - 评论列表
- `POST /api/comment/like/{id}` - 点赞评论
- `DELETE /api/comment/delete/{id}` - 删除评论

#### 6. 文章
- `GET /api/article/list` - 文章列表
- `GET /api/article/detail/{id}` - 文章详情

#### 7. 问答
- `POST /api/question/add` - 发布问题
- `GET /api/question/list` - 问题列表
- `GET /api/question/detail/{id}` - 问题详情
- `POST /api/answer/add` - 发布回答
- `PUT /api/answer/accept/{id}` - 采纳答案

#### 8. 个人中心
- `GET /api/personal/collects` - 我的收藏
- `GET /api/personal/comments` - 我的评论
- `GET /api/personal/questions` - 我的提问
- `GET /api/personal/history` - 浏览历史

### 管理员端接口

#### 1. 管理员登录
- `POST /api/admin/login` - 管理员登录

#### 2. 数据看板
- `GET /api/admin/dashboard/stats` - 统计数据
- `GET /api/admin/dashboard/hot-animals` - 热门动物排行

#### 3. 动物管理
- `GET /api/admin/animal/list` - 动物列表
- `POST /api/admin/animal/add` - 添加动物
- `PUT /api/admin/animal/update` - 更新动物
- `DELETE /api/admin/animal/delete/{id}` - 删除动物
- `POST /api/admin/animal/upload-image` - 上传图片
- `GET /api/admin/animal/detail/{id}` - 动物详情

#### 4. 分类管理
- `GET /api/admin/category/list` - 分类列表
- `POST /api/admin/category/add` - 添加分类
- `PUT /api/admin/category/update` - 更新分类
- `DELETE /api/admin/category/delete/{id}` - 删除分类

#### 5. 文章管理
- `GET /api/admin/article/list` - 文章列表
- `POST /api/admin/article/add` - 添加文章
- `PUT /api/admin/article/update` - 更新文章
- `DELETE /api/admin/article/delete/{id}` - 删除文章
- `PUT /api/admin/article/publish/{id}` - 发布文章
- `POST /api/admin/article/upload-cover` - 上传文章封面

#### 6. 评论管理
- `GET /api/admin/comment/list` - 评论列表
- `DELETE /api/admin/comment/delete/{id}` - 删除评论
- `PUT /api/admin/comment/audit/{id}` - 审核评论

#### 7. 问答管理
- `GET /api/admin/question/list` - 问题列表
- `DELETE /api/admin/question/delete/{id}` - 删除问题
- `PUT /api/admin/question/top/{id}` - 置顶问题

#### 8. 用户管理
- `GET /api/admin/user/list` - 用户列表
- `PUT /api/admin/user/status/{id}` - 修改用户状态
- `GET /api/admin/user/detail/{id}` - 用户详情

#### 9. 轮播图管理
- `GET /api/admin/banner/list` - 轮播图列表
- `POST /api/admin/banner/add` - 添加轮播图
- `PUT /api/admin/banner/update` - 更新轮播图
- `DELETE /api/admin/banner/delete/{id}` - 删除轮播图
- `POST /api/admin/banner/upload-image` - 上传轮播图图片

#### 10. 数据统计
- `GET /api/admin/stats/visit` - 访问量统计
- `GET /api/admin/stats/user-active` - 用户活跃度
- `GET /api/admin/stats/animal-rank` - 动物浏览排行
- `GET /api/admin/stats/collect-rank` - 收藏排行

## 认证说明

### JWT Token
大部分接口需要JWT Token认证，在请求头中添加：
```
Authorization: Bearer {token}
```

### 免认证接口
以下接口无需Token：
- 用户注册/登录
- 管理员登录
- 首页数据
- 动物列表/详情
- 文章列表/详情
- 问题列表/详情
- 评论列表
- 静态资源

## 文件上传

### 上传路径
文件上传到服务器本地目录：`./uploads/`

### 访问路径
上传成功后返回访问路径：`/uploads/文件名`

### 支持格式
- 图片：jpg, jpeg, png, gif
- 最大大小：10MB

## 统一返回格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

- `code`: 状态码（200成功，500失败，401未授权）
- `message`: 返回消息
- `data`: 返回数据

## 分页格式

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [],
    "total": 100,
    "size": 10,
    "current": 1,
    "pages": 10
  }
}
```

## 测试账号

### 管理员
- 用户名：`admin`
- 密码：`admin123`

### 普通用户
- 用户名：`user01` / `user02`
- 密码：`admin123`

## 注意事项

1. 首次启动前请先执行SQL脚本创建数据库
2. 修改application.yml中的数据库连接信息
3. 确保MySQL服务已启动
4. 确保JDK版本为17或以上
5. 文件上传目录会自动创建
6. 密码使用BCrypt加密存储

## 开发建议

1. 使用Postman或Apifox测试接口
2. 开发时可关闭JWT拦截器方便测试
3. 生产环境需修改JWT密钥
4. 建议使用Redis缓存热门数据
5. 图片建议使用OSS存储

## 常见问题

### 1. 端口被占用
修改application.yml中的server.port

### 2. 数据库连接失败
检查MySQL服务是否启动，数据库名、用户名、密码是否正确

### 3. JWT Token过期
Token默认有效期7天，过期后需重新登录

### 4. 文件上传失败
检查文件大小和格式，确保uploads目录有写权限

## 联系方式
如有问题，请提Issue或联系开发者。

