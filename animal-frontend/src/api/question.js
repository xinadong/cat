import request from '@/utils/request'

// 发布问题
export function addQuestion(data) {
  return request({
    url: '/question/add',
    method: 'post',
    data
  })
}

// 获取问题列表
export function getQuestionList(params) {
  return request({
    url: '/question/list',
    method: 'get',
    params
  })
}

// 获取问题详情
export function getQuestionDetail(id) {
  return request({
    url: `/question/detail/${id}`,
    method: 'get'
  })
}

// 发布回答
export function addAnswer(data) {
  return request({
    url: '/answer/add',
    method: 'post',
    data
  })
}

// 采纳答案
export function acceptAnswer(id) {
  return request({
    url: `/answer/accept/${id}`,
    method: 'put'
  })
}

