import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 根据请求路径判断使用哪个 token
    let token = null
    if (config.url && config.url.startsWith('/admin')) {
      // 管理员接口使用 adminToken
      token = localStorage.getItem('adminToken')
    } else {
      // 用户接口使用 token
      token = localStorage.getItem('token')
    }
    
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === 200) {
      return res
    } else if (res.code === 401) {
      ElMessage.error('登录已过期，请重新登录')
      
      // 判断是管理员还是用户
      const currentPath = router.currentRoute.value.path
      if (currentPath.startsWith('/admin')) {
        // 管理员退出
        localStorage.removeItem('adminToken')
        localStorage.removeItem('adminInfo')
        router.push('/admin/login')
      } else {
        // 用户退出
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        router.push('/login')
      }
      return Promise.reject(new Error(res.message || '未授权'))
    } else {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
  },
  error => {
    console.error('响应错误:', error)
    if (error.response && error.response.status === 401) {
      ElMessage.error('登录已过期，请重新登录')
      
      // 判断是管理员还是用户
      const currentPath = router.currentRoute.value.path
      if (currentPath.startsWith('/admin')) {
        // 管理员退出
        localStorage.removeItem('adminToken')
        localStorage.removeItem('adminInfo')
        router.push('/admin/login')
      } else {
        // 用户退出
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        router.push('/login')
      }
    } else {
      ElMessage.error(error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

export default request

