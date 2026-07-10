// src/api/emoji.js
// ⚠️ 注意：我们不再使用统一封装的 request，直接用原生的 axios！
import axios from 'axios'
import { ElMessage } from 'element-plus'

// 后端服务的真实地址（直接绕过代理）
const BASE_URL = 'http://127.0.0.1:8088/api'

// 获取表情包列表
export function getEmojiList(params) {
  return axios({
    url: `/cat-emoji/list`,
    method: 'get',
    params
  }).then(res => res.data).catch(err => {
    console.error('获取列表报错:', err)
    return Promise.reject(err)
  })
}

// 上传表情包
export function uploadEmoji(data) {
  return axios({
    url: `${BASE_URL}/cat-emoji/upload`, // 👈 完整的绝对路径！
    method: 'post',
    data
  }).then(res => {
    // 简单处理响应
    if (res.data.code === 200) {
      return res.data
    } else {
      ElMessage.error(res.data.message || '上传失败')
      return Promise.reject(new Error('上传失败'))
    }
  }).catch(err => {
    console.error('上传接口报错:', err)
    return Promise.reject(err)
  })
}

// 删除表情包
export function deleteEmoji(id) {
  return axios({
    url: `${BASE_URL}/cat-emoji/delete/${id}`,
    method: 'delete'
  }).then(res => res.data)
}