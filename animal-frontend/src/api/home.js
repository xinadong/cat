import request from '@/utils/request'

// 获取首页所有数据
export function getHomeData() {
  return request({
    url: '/home/data',
    method: 'get'
  })
}

// 获取轮播图
export function getBanners() {
  return request({
    url: '/home/banner',
    method: 'get'
  })
}

// 获取热门动物
export function getHotAnimals() {
  return request({
    url: '/home/hot-animals',
    method: 'get'
  })
}

// 获取最新动物
export function getNewAnimals() {
  return request({
    url: '/home/new-animals',
    method: 'get'
  })
}

// 获取分类列表
export function getCategories() {
  return request({
    url: '/home/categories',
    method: 'get'
  })
}

