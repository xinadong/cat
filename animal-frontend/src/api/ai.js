import request from '@/utils/request'

// ============ 用户端 API ============

/**
 * AI 对话
 */
export function chatWithAI(question) {
  return request({
    url: '/ai/chat',
    method: 'post',
    data: { question }
  })
}

/**
 * 获取 AI 配置
 */
export function getAIConfig() {
  return request({
    url: '/ai/config',
    method: 'get'
  })
}

// ============ 管理端 API ============

/**
 * 获取知识库列表
 */
export function getAiKnowledgeList(params) {
  return request({
    url: '/admin/ai-knowledge/list',
    method: 'get',
    params
  })
}

/**
 * 添加知识
 */
export function addAiKnowledge(data) {
  return request({
    url: '/admin/ai-knowledge/add',
    method: 'post',
    data
  })
}

/**
 * 更新知识
 */
export function updateAiKnowledge(data) {
  return request({
    url: '/admin/ai-knowledge/update',
    method: 'put',
    data
  })
}

/**
 * 删除知识
 */
export function deleteAiKnowledge(id) {
  return request({
    url: `/admin/ai-knowledge/delete/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除知识
 */
export function batchDeleteAiKnowledge(ids) {
  return request({
    url: '/admin/ai-knowledge/delete-batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 切换状态
 */
export function toggleAiKnowledgeStatus(id) {
  return request({
    url: `/admin/ai-knowledge/toggle-status/${id}`,
    method: 'put'
  })
}

