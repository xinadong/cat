import request from '@/utils/request'

// ==================== 管理员登录 ====================
export function adminLogin(data) {
  return request({
    url: '/admin/login',
    method: 'post',
    data
  })
}

export function getAdminInfo() {
  return request({
    url: '/admin/info',
    method: 'get'
  })
}

export function updateAdminInfo(data) {
  return request({
    url: '/admin/update',
    method: 'put',
    data
  })
}

export function uploadAdminAvatar(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/admin/upload-avatar',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// ==================== 数据看板 ====================
export function getDashboardStats() {
  return request({
    url: '/admin/dashboard/stats',
    method: 'get'
  })
}

export function getHotAnimals() {
  return request({
    url: '/admin/dashboard/hot-animals',
    method: 'get'
  })
}

export function getCategoryStats() {
  return request({
    url: '/admin/dashboard/category-stats',
    method: 'get'
  })
}

export function getTrendStats() {
  return request({
    url: '/admin/dashboard/trend-stats',
    method: 'get'
  })
}

export function getUserStatusStats() {
  return request({
    url: '/admin/dashboard/user-status-stats',
    method: 'get'
  })
}

// ==================== 动物管理 ====================
export function getAdminAnimalList(params) {
  return request({
    url: '/admin/animal/list',
    method: 'get',
    params
  })
}

export function getAdminAnimalDetail(id) {
  return request({
    url: `/admin/animal/detail/${id}`,
    method: 'get'
  })
}

export function addAnimal(data) {
  return request({
    url: '/admin/animal/add',
    method: 'post',
    data
  })
}

export function updateAnimal(data) {
  return request({
    url: '/admin/animal/update',
    method: 'put',
    data
  })
}

export function deleteAnimal(id) {
  return request({
    url: `/admin/animal/delete/${id}`,
    method: 'delete'
  })
}

export function uploadAnimalImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/admin/animal/upload-image',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// ==================== 分类管理 ====================
export function getCategoryList() {
  return request({
    url: '/admin/category/list',
    method: 'get'
  })
}

export function addCategory(data) {
  return request({
    url: '/admin/category/add',
    method: 'post',
    data
  })
}

export function updateCategory(data) {
  return request({
    url: '/admin/category/update',
    method: 'put',
    data
  })
}

export function deleteCategory(id) {
  return request({
    url: `/admin/category/delete/${id}`,
    method: 'delete'
  })
}

// ==================== 文章管理 ====================
export function getAdminArticleList(params) {
  return request({
    url: '/admin/article/list',
    method: 'get',
    params
  })
}

export function getAdminArticleDetail(id) {
  return request({
    url: `/admin/article/detail/${id}`,
    method: 'get'
  })
}

export function addArticle(data) {
  return request({
    url: '/admin/article/add',
    method: 'post',
    data
  })
}

export function updateArticle(data) {
  return request({
    url: '/admin/article/update',
    method: 'put',
    data
  })
}

export function deleteArticle(id) {
  return request({
    url: `/admin/article/delete/${id}`,
    method: 'delete'
  })
}

export function publishArticle(id) {
  return request({
    url: `/admin/article/publish/${id}`,
    method: 'put'
  })
}

export function uploadArticleCover(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/admin/article/upload-cover',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// ==================== 评论管理 ====================
export function getAdminCommentList(params) {
  return request({
    url: '/admin/comment/list',
    method: 'get',
    params
  })
}

export function deleteAdminComment(id) {
  return request({
    url: `/admin/comment/delete/${id}`,
    method: 'delete'
  })
}

export function auditComment(id, status) {
  return request({
    url: `/admin/comment/audit/${id}`,
    method: 'put',
    params: { status }
  })
}

// ==================== 问答管理 ====================
export function getAdminQuestionList(params) {
  return request({
    url: '/admin/question/list',
    method: 'get',
    params
  })
}

export function deleteAdminQuestion(id) {
  return request({
    url: `/admin/question/delete/${id}`,
    method: 'delete'
  })
}

export function topQuestion(id, isTop) {
  return request({
    url: `/admin/question/top/${id}`,
    method: 'put',
    params: { isTop }
  })
}

// ==================== 用户管理 ====================
export function getAdminUserList(params) {
  return request({
    url: '/admin/user/list',
    method: 'get',
    params
  })
}

export function updateUserStatus(id, status) {
  return request({
    url: `/admin/user/status/${id}`,
    method: 'put',
    params: { status }
  })
}

export function getAdminUserDetail(id) {
  return request({
    url: `/admin/user/detail/${id}`,
    method: 'get'
  })
}

// ==================== 轮播图管理 ====================
export function getBannerList() {
  return request({
    url: '/admin/banner/list',
    method: 'get'
  })
}

export function addBanner(data) {
  return request({
    url: '/admin/banner/add',
    method: 'post',
    data
  })
}

export function updateBanner(data) {
  return request({
    url: '/admin/banner/update',
    method: 'put',
    data
  })
}

export function deleteBanner(id) {
  return request({
    url: `/admin/banner/delete/${id}`,
    method: 'delete'
  })
}

export function uploadBannerImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/admin/banner/upload-image',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// ==================== 数据统计 ====================
export function getVisitStats() {
  return request({
    url: '/admin/stats/visit',
    method: 'get'
  })
}

export function getUserActiveStats() {
  return request({
    url: '/admin/stats/user-active',
    method: 'get'
  })
}

export function getAnimalRank() {
  return request({
    url: '/admin/stats/animal-rank',
    method: 'get'
  })
}

export function getCollectRank() {
  return request({
    url: '/admin/stats/collect-rank',
    method: 'get'
  })
}

