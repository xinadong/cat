import { defineStore } from 'pinia'
import { adminLogin } from '@/api/admin'
import { ElMessage } from 'element-plus'

export const useAdminStore = defineStore('admin', {
  state: () => ({
    token: localStorage.getItem('adminToken') || '',
    adminInfo: JSON.parse(localStorage.getItem('adminInfo') || '{}')
  }),

  getters: {
    isAdminLogin: (state) => !!state.token
  },

  actions: {
    // 管理员登录
    async login(data) {
      try {
        const res = await adminLogin(data)
        this.token = res.data.token
        this.adminInfo = res.data.admin
        localStorage.setItem('adminToken', res.data.token)
        localStorage.setItem('adminInfo', JSON.stringify(res.data.admin))
        ElMessage.success('登录成功')
        return true
      } catch (error) {
        return false
      }
    },

    // 退出登录
    logout() {
      this.token = ''
      this.adminInfo = {}
      localStorage.removeItem('adminToken')
      localStorage.removeItem('adminInfo')
      ElMessage.success('已退出登录')
    }
  }
})

