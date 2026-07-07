import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useAdminStore } from '@/stores/admin'
import { ElMessage } from 'element-plus'
import adminRoutes from './admin'

const routes = [
  {
    path: '/',
    component: () => import('@/views/Layout.vue'),
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/views/Home.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'animals',
        name: 'AnimalList',
        component: () => import('@/views/animal/AnimalList.vue'),
        meta: { title: '猫咪档案库' }
      },
      {
        path: 'animal/:id',
        name: 'AnimalDetail',
        component: () => import('@/views/animal/AnimalDetail.vue'),
        meta: { title: '动物详情' }
      },
      {
        path: 'articles',
        name: 'ArticleList',
        component: () => import('@/views/article/ArticleList.vue'),
        meta: { title: '互动社区' }
      },
      {
        path: 'article/:id',
        name: 'ArticleDetail',
        component: () => import('@/views/article/ArticleDetail.vue'),
        meta: { title: '文章详情' }
      },
      {
        path: 'questions',
        name: 'QuestionList',
        component: () => import('@/views/question/QuestionList.vue'),
        meta: { title: '人气喵王' }
      },
      {
        path: 'question/:id',
        name: 'QuestionDetail',
        component: () => import('@/views/question/QuestionDetail.vue'),
        meta: { title: '问题详情' }
      },
      {
        path: 'personal',
        name: 'Personal',
        component: () => import('@/views/personal/Personal.vue'),
        meta: { title: '个人中心', requireAuth: true }
      },
      {
        path: 'ai-assistant',
        name: 'AiAssistant',
        component: () => import('@/views/ai/AiAssistant.vue'),
        meta: { title: 'AI助手' }
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { title: '注册' }
  },
  ...adminRoutes
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

// 路由守卫
router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 开区喵区小站` : '开区喵区小站'
  
  const userStore = useUserStore()
  const adminStore = useAdminStore()
  
  // 用户端路由守卫
  if (to.meta.requireAuth && !userStore.isLogin) {
    ElMessage.warning('请先登录')
    next('/login')
    return
  }
  
  // 管理员端路由守卫
  if (to.meta.requireAdminAuth && !adminStore.isAdminLogin) {
    ElMessage.warning('请先登录管理员账号')
    next('/admin/login')
    return
  }
  
  next()
})

export default router

