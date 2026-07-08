<template>
  <div class="layout" :style="mainBg ? {
    backgroundImage: `url('${mainBg}')`,
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    backgroundRepeat: 'no-repeat',
    backgroundAttachment: 'fixed'
  } : {}">
    <!-- 顶部导航 -->
    <header class="header">
      <div class="container">
        <div class="logo" @click="goHome">
          <el-icon :size="32" color="#67c23a"><Pear /></el-icon>
          <span>OurCAT</span>
        </div>

        <nav class="nav">
          <router-link to="/" class="nav-item">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </router-link>
          <router-link to="/animals" class="nav-item">
            <el-icon><Grid /></el-icon>
            <span>猫咪档案库</span>
          </router-link>
          <router-link to="/questions" class="nav-item">
            <el-icon><ChatDotRound /></el-icon>
            <span>互动社区</span>
          </router-link>
          <router-link to="/articles" class="nav-item">
            <el-icon><Document/></el-icon>
            <span>人气喵王</span>
          </router-link>
          <router-link to="/ai-assistant" class="nav-item ai-item">
            <el-icon><MagicStick /></el-icon>
            <span>AI助手</span>
          </router-link>
        </nav>

        <div class="user-actions">
          <template v-if="userStore.isLogin">
            <el-dropdown @command="handleCommand">
              <div class="user-info">
                <el-avatar :src="userStore.userInfo.avatar" :size="36" />
                <span class="username">{{ userStore.userInfo.nickname || userStore.userInfo.username }}</span>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="personal">
                    <el-icon><User /></el-icon>
                    个人中心
                  </el-dropdown-item>
                  <el-dropdown-item command="logout" divided>
                    <el-icon><SwitchButton /></el-icon>
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <div class="custom-btn login-btn" @click="goLogin">
              <el-icon><User /></el-icon>
              <span>登录</span>
            </div>
            <div class="custom-btn register-btn" @click="goRegister">
              <el-icon><EditPen /></el-icon>
              <span>注册</span>
            </div>
          </template>
        </div>
      </div>
    </header>

    <!-- 主体内容 -->
    <main class="main">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>

    <!-- 底部 -->
    <footer class="footer">
      <div class="container">
        <div class="footer-content">
          <div class="footer-section">
            <h4>关于我们</h4>
            <p>OurCAT致力于记录与守护每一只校园毛孩子，用温柔传递善意，共建人与猫咪和谐共处的温暖校园。</p>
          </div>
          <div class="footer-section">
            <h4>快速链接</h4>
            <div class="links">
              <router-link to="/">首页</router-link>
              <router-link to="/animals">猫咪档案库</router-link>
              <router-link to="/articles">互动社区</router-link>
              <router-link to="/questions">人气喵王</router-link>
            </div>
          </div>
          <div class="footer-section">
            <h4>联系我们</h4>
            <p>邮箱: 205205205@cat.com</p>
            <p>欢迎加入猫协qq群聊！：704896722</p>
          </div>
        </div>
        <div class="copyright">
          © 2026 DLUT OURCAT. All Rights Reserved.
        </div>
      </div>
    </footer>

    <!-- 返回顶部 -->
    <el-backtop :right="50" :bottom="50">
      <div class="backtop-btn">
        <el-icon><Top /></el-icon>
      </div>
    </el-backtop>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const bgMap = {
  '/': '/images/homebg.png',
  '/animals': '/images/animalbg.png',
  '/articles': '/images/articlebg.png',
  '/questions': '/images/questionbg.png',
  '/ai-assistant': '/images/aibg.png'
}

const mainBg = computed(() => {
  return bgMap[route.path] || ''
})

const goHome = () => {
  router.push('/')
}

const goLogin = () => {
  router.push('/login')
}

const goRegister = () => {
  router.push('/register')
}

const handleCommand = (command) => {
  if (command === 'personal') {
    router.push('/personal')
  } else if (command === 'logout') {
    userStore.logout()
    router.push('/')
  }
}
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  background: rgba(255, 255, 255, 0.12);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.08);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.header .container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 70px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  font-size: 24px;
  font-weight: bold;
  color: white;
  transition: transform 0.3s;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);
}

.logo:hover {
  transform: scale(1.05);
}

.nav {
  display: flex;
  gap: 30px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #303133;
  text-decoration: none;
  font-size: 16px;
  padding: 8px 16px;
  border-radius: 20px;
  transition: all 0.3s;
  text-shadow: none;
  position: relative;
  background: transparent;
  overflow: hidden;
}

.nav-item::before {
  content: '';
  position: absolute;
  top: -2px;
  left: -2px;
  right: -2px;
  bottom: -2px;
  border-radius: 22px;
  background: linear-gradient(135deg, #fe58af, #5f57ff, #2dff41, #ffef57);
  z-index: 0;
}

.nav-item::after {
  content: '';
  position: absolute;
  top: 2px;
  left: 2px;
  right: 2px;
  bottom: 2px;
  border-radius: 18px;
  background: #ffffff;
  z-index: 1;
}

.nav-item:hover::after {
  background: #f4f3ff;
}


.nav-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.nav-item.router-link-exact-active::after {
  background: #ddcfff;
}

.nav-item.router-link-exact-active::before {
  background: linear-gradient(135deg, #ffef57, #2dff41, #5f57ff, #fe58af);
}

.nav-item :deep(.el-icon),
.nav-item span {
  position: relative;
  z-index: 2;
}

.nav-item.ai-item {
  position: relative;
  animation: glow 2s ease-in-out infinite;
}

@keyframes glow {
  0%, 100% {
    box-shadow: 0 0 5px rgb(154, 255, 255);
  }
  50% {
    box-shadow: 0 0 15px rgb(0, 255, 255);
  }
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 5px 15px 5px 5px;
  border-radius: 25px;
  background: rgba(255, 255, 255, 0.2);
  transition: all 0.3s;
}

.user-info:hover {
  background: rgba(255, 255, 255, 0.3);
}

.username {
  color: white;
  font-weight: 500;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);
}

.custom-btn {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 8px 20px;
  border-radius: 25px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid rgba(255, 255, 255, 0.4);
  color: #303133;
}

.login-btn {
  background: rgba(255, 255, 255, 0.75);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
}

.login-btn:hover {
  background: rgba(255, 255, 255, 0.95);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 255, 255, 0.2);
}

.register-btn {
  background: rgba(144, 107, 255,  0.6);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
}

.register-btn:hover {
  background: rgba(144, 107, 255, 0.8);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(144, 107, 255, 0.3);
}

.main {
  flex: 1;
  min-height: calc(100vh - 350px);
}

.footer {
  background: rgba(255, 255, 255, 0.50);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  color: white;
  padding: 40px 0 20px;
  margin-top: 80px;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 40px;
  margin-bottom: 30px;
}

.footer-section h4 {
  margin-bottom: 15px;
  font-size: 18px;
  color: #906bff;
}

.footer-section p {
  line-height: 1.8;
  color: #707070;
}

.links {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.links a {
  color: #707070;
  text-decoration: none;
  transition: color 0.3s;
}

.links a:hover {
  color: #906bff;
}

.copyright {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #444;
  color: #707070;
}

.backtop-btn {
  width: 40px;
  height: 40px;
  background: rgba(144, 107, 255, 0.8);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  box-shadow: 0 2px 10px rgba(144, 107, 255, 0.3);
  transition: all 0.3s;
}

.backtop-btn:hover {
  background: #906bff;
  transform: scale(1.1);
}

@media (max-width: 768px) {
  .header .container {
    flex-wrap: wrap;
    height: auto;
    padding: 15px 20px;
  }

  .nav {
    order: 3;
    width: 100%;
    justify-content: space-around;
    margin-top: 15px;
    padding-top: 15px;
    border-top: 1px solid rgba(255, 255, 255, 0.2);
  }

  .nav-item {
    font-size: 14px;
    padding: 6px 12px;
  }

  .footer-content {
    grid-template-columns: 1fr;
    gap: 30px;
  }
}
</style>