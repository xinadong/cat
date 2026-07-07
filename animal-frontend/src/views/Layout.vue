<template>
  <div class="layout">
    <!-- 顶部导航 -->
    <header class="header">
      <div class="container">
        <div class="logo" @click="goHome">
          <el-icon :size="32" color="#67c23a"><Pear /></el-icon>
          <span>喵喵喵</span>
        </div>
        
        <nav class="nav">
          <router-link to="/" class="nav-item">
            <el-icon><HomeFilled /></el-icon>
            首页
          </router-link>
          <router-link to="/animals" class="nav-item">
            <el-icon><Grid /></el-icon>
            猫咪档案库
          </router-link>
          <router-link to="/questions" class="nav-item">
            <el-icon><ChatDotRound/></el-icon>
            互动社区
          </router-link>
          <router-link to="/articles" class="nav-item">
            <el-icon><Document  /></el-icon>
            人气喵王
          </router-link>
          <router-link to="/ai-assistant" class="nav-item ai-item">
            <el-icon><MagicStick /></el-icon>
            AI助手
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
            <el-button type="primary" @click="goLogin">登录</el-button>
            <el-button @click="goRegister">注册</el-button>
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
            <p>动物科普网站致力于传播动物知识，保护野生动物，共建美好生态。</p>
          </div>
          <div class="footer-section">
            <h4>快速链接</h4>
            <div class="links">
              <router-link to="/">首页</router-link>
              <router-link to="/animals">动物百科</router-link>
              <router-link to="/articles">科普文章</router-link>
              <router-link to="/questions">问答社区</router-link>
            </div>
          </div>
          <div class="footer-section">
            <h4>联系我们</h4>
            <p>邮箱: contact@animal.com</p>
            <p>电话: 400-123-4567</p>
          </div>
        </div>
        <div class="copyright">
          © 2025 动物科普网站. All Rights Reserved.
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
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

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
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
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
  color: white;
  text-decoration: none;
  font-size: 16px;
  padding: 8px 16px;
  border-radius: 20px;
  transition: all 0.3s;
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

.nav-item.router-link-exact-active {
  background: rgba(255, 255, 255, 0.3);
}

.nav-item.ai-item {
  position: relative;
  animation: glow 2s ease-in-out infinite;
}

@keyframes glow {
  0%, 100% {
    box-shadow: 0 0 5px rgba(255, 215, 0, 0.5);
  }
  50% {
    box-shadow: 0 0 15px rgba(255, 215, 0, 0.8);
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
}

.main {
  flex: 1;
  min-height: calc(100vh - 350px);
}

.footer {
  background: #2c3e50;
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
  color: #67c23a;
}

.footer-section p {
  line-height: 1.8;
  color: #bbb;
}

.links {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.links a {
  color: #bbb;
  text-decoration: none;
  transition: color 0.3s;
}

.links a:hover {
  color: #67c23a;
}

.copyright {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #444;
  color: #999;
}

.backtop-btn {
  width: 40px;
  height: 40px;
  background: #67c23a;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  box-shadow: 0 2px 10px rgba(103, 194, 58, 0.3);
  transition: all 0.3s;
}

.backtop-btn:hover {
  background: #85ce61;
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

