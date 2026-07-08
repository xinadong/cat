<template>
  <div class="admin-login-page">
    <div class="login-container">
      <div class="login-header">
        <div class="logo">
          <img src="/images/logo.png" alt="OurCAT" class="admin-logo" />
        </div>
        <h2 class="title-green">OurCAT - 管理后台</h2>
        <p class="subtitle-green">OurCAT Management System</p>
      </div>

      <el-card class="login-card" shadow="always">
        <el-form ref="formRef" :model="form" :rules="rules" size="large">
          <el-form-item prop="username">
            <el-input
              v-model="form.username"
              placeholder="请输入管理员账号"
              clearable
            >
              <template #prefix>
                <el-icon><UserFilled /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              show-password
              @keyup.enter="handleLogin"
            >
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item>
            <el-button
              class="rainbow-btn"
              :loading="loading"
              @click="handleLogin"
              style="width: 100%"
            >
              登录
            </el-button>
          </el-form-item>

          <div class="form-footer">
            <el-button style="color: #906bff;" link @click="goUserLogin">
              <el-icon><User /></el-icon>
              用户登录
            </el-button>
            <span style="margin: 0 10px">|</span>
            <el-button style="color: #906bff;" link @click="goUserSite">
              <el-icon><HomeFilled /></el-icon>
              返回首页
            </el-button>
          </div>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAdminStore } from '@/stores/admin'

const router = useRouter()
const adminStore = useAdminStore()

const formRef = ref()
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入管理员账号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const handleLogin = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const success = await adminStore.login(form)
        if (success) {
          router.push('/admin')
        }
      } finally {
        loading.value = false
      }
    }
  })
}

const goUserLogin = () => {
  router.push('/login')
}

const goUserSite = () => {
  router.push('/')
}
</script>

<style scoped>
.admin-login-page {
  min-height: 100vh;
  background-image: url('/images/register_loginbg.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-container {
  width: 100%;
  max-width: 450px;
}

.login-header {
  text-align: center;
  color: white;
  margin-bottom: 30px;
}

.logo {
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
}

.admin-logo {
  height: 120px;
  width: auto;
  object-fit: contain;
  background: white;
  border-radius: 50%;
  padding: 10px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.login-header h2 {
  font-size: 28px;
  margin-bottom: 10px;
}

.title-green {
  color: #303133 !important;
  font-weight: bold;
}

.login-header p {
  font-size: 14px;
  opacity: 0.9;
  letter-spacing: 2px;
}

.subtitle-green {
  color: #303133 !important;
  font-weight: 500;
}

.login-card {
  border-radius: 16px;
}

.form-footer {
  text-align: center;
  margin-top: 10px;
}

.rainbow-btn {
  background: linear-gradient(135deg, #ff0000, #ff7700, #ffdd00, #00cc00, #0099ff, #6633ff, #cc00ff) !important;
  border: none !important;
  color: white !important;
  font-weight: 600;
  transition: opacity 0.3s;
}

.rainbow-btn:hover {
  opacity: 0.85;
}
</style>

