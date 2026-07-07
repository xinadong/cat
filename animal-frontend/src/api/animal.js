import request from '@/utils/request'

// 获取动物列表
export function getAnimalList(params) {
  return request({
    url: '/animal/list',
    method: 'get',
    params
  })
}

// 获取动物详情
export function getAnimalDetail(id) {
  return request({
    url: `/animal/detail/${id}`,
    method: 'get'
  })
}

// 获取相关推荐
export function getRecommendAnimals(params) {
  return request({
    url: '/animal/recommend',
    method: 'get',
    params
  })
}

