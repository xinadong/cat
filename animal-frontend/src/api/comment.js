import request from '@/utils/request'

// 发表评论
export function addComment(data) {
  return request({
    url: '/comment/add',
    method: 'post',
    data
  })
}

// 获取评论列表
export function getCommentList(animalId) {
  return request({
    url: '/comment/list',
    method: 'get',
    params: { animalId }
  })
}

// 点赞评论
export function likeComment(id) {
  return request({
    url: `/comment/like/${id}`,
    method: 'post'
  })
}

// 删除评论
export function deleteComment(id) {
  return request({
    url: `/comment/delete/${id}`,
    method: 'delete'
  })
}

