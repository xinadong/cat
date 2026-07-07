import request from '@/utils/request'

// 获取我的收藏
export function getMyCollects() {
  return request({
    url: '/personal/collects',
    method: 'get'
  })
}

// 获取我的评论
export function getMyComments() {
  return request({
    url: '/personal/comments',
    method: 'get'
  })
}

// 获取我的提问
export function getMyQuestions() {
  return request({
    url: '/personal/questions',
    method: 'get'
  })
}

// 获取浏览历史
export function getBrowseHistory() {
  return request({
    url: '/personal/history',
    method: 'get'
  })
}

// 清空浏览历史
export function clearBrowseHistory() {
  return request({
    url: '/personal/history/clear',
    method: 'delete'
  })
}

