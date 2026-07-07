import request from '@/utils/request'

// 添加收藏
export function addCollect(animalId) {
  return request({
    url: '/collect/add',
    method: 'post',
    data: { animalId }
  })
}

// 取消收藏
export function deleteCollect(animalId) {
  return request({
    url: `/collect/delete/${animalId}`,
    method: 'delete'
  })
}

// 获取收藏列表
export function getCollectList() {
  return request({
    url: '/collect/list',
    method: 'get'
  })
}

// 检查是否已收藏
export function checkCollect(animalId) {
  return request({
    url: `/collect/check/${animalId}`,
    method: 'get'
  })
}

