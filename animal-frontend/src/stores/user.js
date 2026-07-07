import { defineStore } from 'pinia'
import { login, register, getUserInfo } from '@/api/user'
import { ElMessage } from 'element-plus'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}')
  }),

  getters: {
    isLogin: (state) => !!state.token,
    userId: (state) => state.userInfo.id
  },

  actions: {
    // 登录
    async login(data) {
      try {
        const res = await login(data)
        this.token = res.data.token
        this.userInfo = res.data.user
        localStorage.setItem('token', res.data.token)
        localStorage.setItem('userInfo', JSON.stringify(res.data.user))
        ElMessage.success('登录成功')
        return true
      } catch (error) {
        return false
      }
    },

    // 注册
    async register(data) {
      try {
        await register(data)
        ElMessage.success('注册成功，请登录')
        return true
      } catch (error) {
        return false
      }
    },

    // 获取用户信息
    async getUserInfo() {
      try {
        const res = await getUserInfo()
        this.userInfo = res.data
        localStorage.setItem('userInfo', JSON.stringify(res.data))
      } catch (error) {
        console.error('获取用户信息失败', error)
      }
    },

    // 退出登录
    logout() {
      this.token = ''
      this.userInfo = {}
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      ElMessage.success('已退出登录')
    }
  }
})

