export default [
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: () => import('@/views/admin/AdminLogin.vue'),
    meta: { title: '管理员登录' }
  },
  {
    path: '/admin',
    component: () => import('@/views/admin/AdminLayout.vue'),
    meta: { requireAdminAuth: true },
    children: [
      {
        path: '',
        redirect: '/admin/dashboard'
      },
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { title: '数据看板', icon: 'DataAnalysis' }
      },
      {
        path: 'animals',
        name: 'AdminAnimals',
        component: () => import('@/views/admin/animals/AnimalList.vue'),
        meta: { title: '动物管理', icon: 'Grid' }
      },
      {
        path: 'categories',
        name: 'AdminCategories',
        component: () => import('@/views/admin/categories/CategoryList.vue'),
        meta: { title: '分类管理', icon: 'FolderOpened' }
      },
      {
        path: 'articles',
        name: 'AdminArticles',
        component: () => import('@/views/admin/articles/ArticleList.vue'),
        meta: { title: '文章管理', icon: 'Document' }
      },
      {
        path: 'comments',
        name: 'AdminComments',
        component: () => import('@/views/admin/comments/CommentList.vue'),
        meta: { title: '评论管理', icon: 'ChatDotRound' }
      },
      {
        path: 'questions',
        name: 'AdminQuestions',
        component: () => import('@/views/admin/questions/QuestionList.vue'),
        meta: { title: '问答管理', icon: 'QuestionFilled' }
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('@/views/admin/users/UserList.vue'),
        meta: { title: '用户管理', icon: 'User' }
      },
      {
        path: 'banners',
        name: 'AdminBanners',
        component: () => import('@/views/admin/banners/BannerList.vue'),
        meta: { title: '轮播图管理', icon: 'Picture' }
      },
      {
        path: 'ai-knowledge',
        name: 'AdminAiKnowledge',
        component: () => import('@/views/admin/ai-knowledge/AiKnowledgeList.vue'),
        meta: { title: 'AI知识库', icon: 'MagicStick' }
      },
      {
        path: 'stats',
        name: 'AdminStats',
        component: () => import('@/views/admin/stats/Statistics.vue'),
        meta: { title: '数据统计', icon: 'TrendCharts' }
      }
    ]
  }
]

