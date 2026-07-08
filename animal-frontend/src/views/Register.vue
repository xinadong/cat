<template>
  <div class="register-page">
    <div class="register-container">
      <div class="register-left">

      </div>

      <div class="register-right">
        <div class="register-form-wrapper">
          <h2>欢迎加入<img src="/images/logo.png" alt="OurCAT" class="title-logo" />！</h2>
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

            <el-form-item prop="nickname">
              <el-input
                v-model="form.nickname"
                placeholder="请输入昵称"
                clearable
              >
                <template #prefix>
                  <el-icon><Avatar /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="email">
              <el-input
                v-model="form.email"
                placeholder="请输入邮箱"
                clearable
              >
                <template #prefix>
                  <el-icon><Message /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="password">
              <el-input
                v-model="form.password"
                type="password"
                placeholder="请输入密码"
                show-password
              >
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="confirmPassword">
              <el-input
                v-model="form.confirmPassword"
                type="password"
                placeholder="请确认密码"
                show-password
                @keyup.enter="handleRegister"
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
                @click="handleRegister"
                style="width: 100%"
              >
                注册
              </el-button>
            </el-form-item>

            <div class="form-footer">
              <span>已有账号？</span>
              <el-button style="color: #906bff;" link @click="goLogin">
                立即登录
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
  nickname: '',
  email: '',
  password: '',
  confirmPassword: ''
})

const validatePass2 = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3-20个字符', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validatePass2, trigger: 'blur' }
  ]
}

const handleRegister = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 只发送后端需要的字段，排除 confirmPassword
        const registerData = {
          username: form.username,
          password: form.password,
          nickname: form.nickname,
          email: form.email
        }
        const success = await userStore.register(registerData)
        if (success) {
          router.push('/login')
        }
      } finally {
        loading.value = false
      }
    }
  })
}

const goLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-page {
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

.register-container {
  display: flex;
  width: 900px;
  min-height: 600px;
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.register-left {
  flex: 1;
  background-image: url('/images/register.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  padding: 60px 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  position: relative;
}

.register-left::before {
  content: '';
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
}

.register-right {
  flex: 1;
  padding: 60px 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow-y: auto;
}

.register-form-wrapper {
  width: 100%;
  max-width: 350px;
}

.register-form-wrapper h2 {
  text-align: center;
  margin-bottom: 30px;
  font-size: 28px;
  color: #303133;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.title-logo {
  height: 80px;
  width: auto;
  object-fit: contain;
  vertical-align: middle;
}

.form-footer {
  text-align: center;
  color: #909399;
  font-size: 14px;
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

@media (max-width: 768px) {
  .register-container {
    flex-direction: column;
    width: 100%;
    max-width: 400px;
  }

  .register-left {
    padding: 40px 20px;
  }

  .register-right {
    padding: 40px 20px;
  }
}
</style>

