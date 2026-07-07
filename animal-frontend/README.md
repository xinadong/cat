# 动物科普网站 - 前端

## 项目简介
基于 Vue 3 + Vite + Element Plus 的动物科普网站前端项目，提供美观大气的用户界面。

## 技术栈
- **Vue 3** - 渐进式JavaScript框架
- **Vite** - 新一代前端构建工具
- **Pinia** - Vue 3状态管理
- **Vue Router** - 路由管理
- **Element Plus** - UI组件库
- **Axios** - HTTP客户端
- **ECharts** - 数据可视化

## 项目特点
- 🎨 美观大气的界面设计
- 🌿 绿色主题，符合自然动物风格
- 📱 响应式布局，支持移动端
- 🚀 路由懒加载，性能优化
- 💫 流畅的动画过渡效果
- 🔐 JWT Token认证
- 📦 组件化开发

## 功能模块

### 用户端
- ✅ 用户登录注册
- ✅ 首页（轮播图、热门动物、最新动物、分类导航）
- ✅ 动物列表（分类筛选、关键词搜索、分页）
- ✅ 动物详情（图片展示、详细信息、评论、收藏）
- ✅ 科普文章列表和详情
- ✅ 问答社区（提问、回答、采纳）
- ✅ 个人中心（基本信息、收藏、评论、提问、浏览历史）

## 快速开始

### 安装依赖
```bash
cd animal-frontend
npm install
```

### 开发环境运行
```bash
npm run dev
```
访问：http://localhost:3000

### 生产环境构建
```bash
npm run build
```

### 预览生产构建
```bash
npm run preview
```

## 项目结构
```
animal-frontend/
├── public/              # 静态资源
├── src/
│   ├── api/            # API接口
│   ├── assets/         # 资源文件
│   │   └── styles/    # 全局样式
│   ├── components/     # 公共组件
│   ├── router/         # 路由配置
│   ├── stores/         # Pinia状态管理
│   ├── utils/          # 工具函数
│   ├── views/          # 页面组件
│   │   ├── animal/    # 动物相关页面
│   │   ├── article/   # 文章相关页面
│   │   ├── question/  # 问答相关页面
│   │   └── personal/  # 个人中心
│   ├── App.vue         # 根组件
│   └── main.js         # 入口文件
├── index.html          # HTML模板
├── vite.config.js      # Vite配置
└── package.json        # 项目配置
```

## 主题色
- **主色调**: #67c23a (绿色) - 代表自然、生命
- **辅助色**: #409eff (蓝色) - 代表天空、海洋
- **成功色**: #67c23a
- **警告色**: #e6a23c
- **危险色**: #f56c6c

## 路由说明
- `/` - 首页
- `/animals` - 动物列表
- `/animal/:id` - 动物详情
- `/articles` - 文章列表
- `/article/:id` - 文章详情
- `/questions` - 问答列表
- `/question/:id` - 问题详情
- `/personal` - 个人中心（需登录）
- `/login` - 登录
- `/register` - 注册

## API代理配置
开发环境已配置API代理，自动转发到后端服务：
- `/api/*` -> `http://localhost:8080/api/*`
- `/uploads/*` -> `http://localhost:8080/uploads/*`

## 环境要求
- Node.js >= 16
- npm >= 8

## 注意事项
1. 确保后端服务已启动（端口8080）
2. 图片上传功能需要后端支持
3. 首次登录建议使用测试账号：
   - 用户名：user01
   - 密码：admin123

## 开发建议
1. 使用 Vue DevTools 调试
2. 遵循 Vue 3 Composition API 规范
3. 组件命名采用大驼峰
4. 样式使用 scoped 避免污染
5. 合理使用 Pinia 管理状态

## 构建优化
- 路由懒加载
- 组件按需引入
- 图片懒加载
- Gzip压缩
- CDN加速

## 浏览器支持
- Chrome >= 87
- Firefox >= 78
- Safari >= 14
- Edge >= 88

## 常见问题

### 1. 无法连接后端
检查后端服务是否启动，端口是否为8080

### 2. Token过期
重新登录即可，Token默认有效期7天

### 3. 图片无法显示
检查图片路径是否正确，确保后端文件上传功能正常

### 4. 开发环境跨域问题
已在vite.config.js中配置代理，无需额外处理

## 联系方式
如有问题，请提Issue或联系开发者。

## License
MIT

