<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-left">
        <div class="welcome-content">
          <h1>
            <el-icon :size="60" color="white"><Ship /></el-icon>
          </h1>
          <h2>欢迎来到动物科普网站</h2>
          <p>探索神奇的动物世界，学习丰富的科普知识</p>
          <div class="features">
            <div class="feature-item">
              <el-icon color="white"><CircleCheck /></el-icon>
              <span>海量动物资料</span>
            </div>
            <div class="feature-item">
              <el-icon color="white"><CircleCheck /></el-icon>
              <span>精彩科普文章</span>
            </div>
            <div class="feature-item">
              <el-icon color="white"><CircleCheck /></el-icon>
              <span>互动问答社区</span>
            </div>
          </div>
        </div>
      </div>

      <div class="login-right">
        <div class="login-form-wrapper">
          <h2>用户登录</h2>
          <el-form ref="formRef" :model="form" :rules="rules" size="large">
            <el-form-item prop="username">
              <el-input
                v-model="form.username"
                placeholder="请输入用户名"
                clearable
              >
                <template #prefix>
                  <el-icon><User /></el-icon>
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
                type="primary"
                :loading="loading"
                @click="handleLogin"
                style="width: 100%"
              >
                登录
              </el-button>
            </el-form-item>

            <div class="form-footer">
              <span>还没有账号？</span>
              <el-button type="success" link @click="goRegister">
                立即注册
              </el-button>
              <span style="margin: 0 10px">|</span>
              <el-button type="success" link @click="goAdminLogin">
                管理员登录
              </el-button>
            </div>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref()
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
  ]
}

const handleLogin = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const success = await userStore.login(form)
        if (success) {
          router.push('/')
        }
      } finally {
        loading.value = false
      }
    }
  })
}

const goRegister = () => {
  router.push('/register')
}

const goAdminLogin = () => {
  router.push('/admin/login')
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background-image: url('/images/login.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-container {
  display: flex;
  width: 900px;
  min-height: 500px;
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.login-left {
  flex: 1;
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  padding: 60px 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.welcome-content {
  text-align: center;
}

.welcome-content h1 {
  margin-bottom: 20px;
}

.welcome-content h2 {
  font-size: 32px;
  margin-bottom: 15px;
}

.welcome-content p {
  font-size: 16px;
  opacity: 0.9;
  margin-bottom: 40px;
}

.features {
  display: flex;
  flex-direction: column;
  gap: 20px;
  text-align: left;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 16px;
}

.login-right {
  flex: 1;
  padding: 60px 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-form-wrapper {
  width: 100%;
  max-width: 350px;
}

.login-form-wrapper h2 {
  text-align: center;
  margin-bottom: 40px;
  font-size: 28px;
  color: #303133;
}

.form-footer {
  text-align: center;
  color: #909399;
  font-size: 14px;
}

@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
    width: 100%;
    max-width: 400px;
  }

  .login-left {
    padding: 40px 20px;
  }

  .login-right {
    padding: 40px 20px;
  }
}
</style>

