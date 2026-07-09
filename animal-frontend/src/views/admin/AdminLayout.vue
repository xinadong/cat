<template>
  <el-container class="admin-layout">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '200px'" class="admin-aside">
      <!-- Logo区域 -->
      <div class="logo-container" :class="{ collapsed: isCollapse }">
        <el-icon :size="28" color="#906bff">
          <component :is="Management" />
        </el-icon>
        <transition name="fade">
          <span v-show="!isCollapse" class="logo-text">动物科普管理</span>
        </transition>
      </div>

      <!-- 菜单 -->
      <el-scrollbar class="menu-scrollbar">
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapse"
          router
          unique-opened
          class="admin-menu"
        >
          <!-- 数据看板 -->
          <el-menu-item index="/admin/dashboard">
            <el-icon><DataAnalysis /></el-icon>
            <template #title>数据看板</template>
          </el-menu-item>

          <!-- 动物管理 -->
          <el-menu-item index="/admin/animals">
            <el-icon><Grid /></el-icon>
            <template #title>动物管理</template>
          </el-menu-item>

          <!-- 分类管理 -->
          <el-menu-item index="/admin/categories">
            <el-icon><FolderOpened /></el-icon>
            <template #title>分类管理</template>
          </el-menu-item>

          <!-- 文章管理 -->
          <el-menu-item index="/admin/articles">
            <el-icon><Document /></el-icon>
            <template #title>文章管理</template>
          </el-menu-item>

          <!-- 评论管理 -->
          <el-menu-item index="/admin/comments">
            <el-icon><ChatDotRound /></el-icon>
            <template #title>评论管理</template>
          </el-menu-item>

          <!-- 问答管理 -->
          <el-menu-item index="/admin/questions">
            <el-icon><QuestionFilled /></el-icon>
            <template #title>问答管理</template>
          </el-menu-item>

          <!-- 用户管理 -->
          <el-menu-item index="/admin/users">
            <el-icon><User /></el-icon>
            <template #title>用户管理</template>
          </el-menu-item>

          <!-- 轮播图管理 -->
          <el-menu-item index="/admin/banners">
            <el-icon><Picture /></el-icon>
            <template #title>轮播图管理</template>
          </el-menu-item>

          <!-- AI知识库 -->
          <el-menu-item index="/admin/ai-knowledge">
            <el-icon><MagicStick /></el-icon>
            <template #title>AI知识库</template>
          </el-menu-item>

          <!-- 数据统计 -->
          <el-menu-item index="/admin/stats">
            <el-icon><TrendCharts /></el-icon>
            <template #title>数据统计</template>
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <!-- 主体内容 -->
    <el-container class="main-container">
      <!-- 顶部导航 -->
      <el-header class="admin-header">
        <div class="header-left">
          <!-- 折叠按钮 -->
          <el-icon class="collapse-btn" @click="toggleCollapse" :size="20">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>

          <!-- 面包屑 -->
          <el-breadcrumb separator="/">
            <el-breadcrumb-item>管理后台</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentPageTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div class="header-right">
          <!-- 用户下拉菜单 -->
          <el-dropdown @command="handleCommand">
            <div class="user-info">
              <el-avatar :size="36" :src="adminStore.adminInfo.avatar">
                <el-icon><User /></el-icon>
              </el-avatar>
              <span class="username">{{ adminStore.adminInfo.nickname || adminStore.adminInfo.username || '系统管理员' }}</span>
              <el-icon class="arrow-down"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  <span>个人中心</span>
                </el-dropdown-item>
                <el-dropdown-item command="logout" divided>
                  <el-icon><SwitchButton /></el-icon>
                  <span>退出登录</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 内容区 -->
      <el-main class="admin-main">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>

    <!-- 个人中心对话框 -->
    <el-dialog
      v-model="profileDialogVisible"
      title="个人中心"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="profileForm" :rules="profileRules" ref="profileFormRef" label-width="80px">
        <el-form-item label="头像">
          <div class="avatar-upload-container">
            <el-avatar :size="80" :src="profileForm.avatar">
              <el-icon :size="40"><User /></el-icon>
            </el-avatar>
            <el-upload
              class="avatar-uploader"
              :show-file-list="false"
              :before-upload="beforeAvatarUpload"
              :http-request="handleAvatarUpload"
              accept="image/*"
            >
              <el-button type="primary" size="small" :icon="Upload">更换头像</el-button>
            </el-upload>
          </div>
        </el-form-item>

        <el-form-item label="用户名">
          <el-input v-model="profileForm.username" disabled>
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="profileForm.nickname" placeholder="请输入昵称">
            <template #prefix>
              <el-icon><Avatar /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="profileForm.email" placeholder="请输入邮箱">
            <template #prefix>
              <el-icon><Message /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item label="手机号" prop="phone">
          <el-input v-model="profileForm.phone" placeholder="请输入手机号">
            <template #prefix>
              <el-icon><Phone /></el-icon>
            </template>
          </el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="profileDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleProfileSubmit" :loading="profileLoading">
          保存
        </el-button>
      </template>
    </el-dialog>
  </el-container>
</template>

<script setup>
import { ref, computed, reactive, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAdminStore } from '@/stores/admin'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Management,
  DataAnalysis,
  Grid,
  FolderOpened,
  Document,
  ChatDotRound,
  QuestionFilled,
  User,
  Picture,
  MagicStick,
  TrendCharts,
  Fold,
  Expand,
  ArrowDown,
  SwitchButton,
  Upload,
  Avatar,
  Message,
  Phone
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const adminStore = useAdminStore()

// 侧边栏折叠状态
const isCollapse = ref(false)

// 当前激活的菜单
const activeMenu = computed(() => route.path)

// 当前页面标题
const currentPageTitle = computed(() => {
  const titleMap = {
    '/admin/dashboard': '数据看板',
    '/admin/animals': '动物管理',
    '/admin/categories': '分类管理',
    '/admin/articles': '文章管理',
    '/admin/comments': '评论管理',
    '/admin/questions': '问答管理',
    '/admin/users': '用户管理',
    '/admin/banners': '轮播图管理',
    '/admin/ai-knowledge': 'AI知识库',
    '/admin/stats': '数据统计'
  }
  return titleMap[route.path] || '管理后台'
})

// 个人中心相关
const profileDialogVisible = ref(false)
const profileLoading = ref(false)
const profileFormRef = ref()

const profileForm = reactive({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  avatar: ''
})

const profileRules = {
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

// 切换侧边栏折叠
const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

// 处理用户下拉菜单命令
const handleCommand = (command) => {
  if (command === 'profile') {
    openProfileDialog()
  } else if (command === 'logout') {
    handleLogout()
  }
}

// 打开个人中心对话框
const openProfileDialog = () => {
  const info = adminStore.adminInfo || {}
  profileForm.username = info.username || ''
  profileForm.nickname = info.nickname || ''
  profileForm.email = info.email || ''
  profileForm.phone = info.phone || ''
  profileForm.avatar = info.avatar || ''
  profileDialogVisible.value = true
}

// 退出登录
const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    adminStore.logout()
    router.push('/admin/login')
    ElMessage.success('退出成功')
  }).catch(() => {})
}

// 头像上传前验证
const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 处理头像上传
const handleAvatarUpload = async (options) => {
  try {
    const { uploadAdminAvatar } = await import('@/api/admin')
    const res = await uploadAdminAvatar(options.file)
    profileForm.avatar = res.data.url
    ElMessage.success('头像上传成功')
  } catch (error) {
    ElMessage.error('头像上传失败')
  }
}

// 提交个人信息
const handleProfileSubmit = async () => {
  if (!profileFormRef.value) return

  profileFormRef.value.validate(async (valid) => {
    if (valid) {
      profileLoading.value = true
      try {
        const { updateAdminInfo } = await import('@/api/admin')
        await updateAdminInfo({
          nickname: profileForm.nickname,
          email: profileForm.email,
          phone: profileForm.phone,
          avatar: profileForm.avatar
        })

        // 更新本地store
        adminStore.adminInfo = {
          ...adminStore.adminInfo,
          nickname: profileForm.nickname,
          email: profileForm.email,
          phone: profileForm.phone,
          avatar: profileForm.avatar
        }
        localStorage.setItem('adminInfo', JSON.stringify(adminStore.adminInfo))

        ElMessage.success('保存成功')
        profileDialogVisible.value = false
      } catch (error) {
        ElMessage.error('保存失败')
      } finally {
        profileLoading.value = false
      }
    }
  })
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
  width: 100%;
  background: url('/images/adminbg.png') center/cover no-repeat fixed;
}

/* ============ 侧边栏样式 ============ */
.admin-aside {
  background: #fff;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  transition: width 0.3s;
  height: 100vh;
  overflow: hidden;
}

/* Logo区域 */
.logo-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  padding: 0 20px;
  gap: 12px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
  background: transparent;
}

.logo-container.collapsed {
  justify-content: center;
  padding: 0;
}

.logo-text {
  font-size: 18px;
  font-weight: bold;
  color: #906bff;
  white-space: nowrap;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 菜单滚动条 */
.menu-scrollbar {
  height: calc(100vh - 60px);
}

.menu-scrollbar :deep(.el-scrollbar__wrap) {
  overflow-x: hidden;
}

/* 菜单样式 */
.admin-menu {
  border: none;
  background: transparent;
}

.admin-menu:not(.el-menu--collapse) {
  width: 200px;
}

.admin-menu .el-menu-item {
  height: 56px;
  line-height: 56px;
  font-size: 14px;
  color: #333;
}

.admin-menu .el-menu-item .el-icon {
  font-size: 18px;
  margin-right: 8px;
  color: #666;
}

.admin-menu .el-menu-item:hover {
  background-color: #f5f0ff;
  color: #906bff;
}

.admin-menu .el-menu-item:hover .el-icon {
  color: #906bff;
}

.admin-menu .el-menu-item.is-active {
  background: linear-gradient(90deg, rgba(144, 107, 255, 0.1), transparent);
  color: #906bff;
  border-right: 3px solid #906bff;
  font-weight: 600;
}

.admin-menu .el-menu-item.is-active .el-icon {
  color: #906bff;
}

/* ============ 主容器样式 ============ */
.main-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow: hidden;
  background: transparent;
}

/* ============ 顶部导航样式 ============ */
.admin-header {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  height: 60px;
  z-index: 10;
  margin: 10px 10px 0 0;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.collapse-btn {
  cursor: pointer;
  color: #666;
  transition: all 0.3s;
}

.collapse-btn:hover {
  color: #906bff;
  transform: scale(1.1);
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 8px 16px;
  border-radius: 20px;
  transition: all 0.3s;
}

.user-info:hover {
  background: #f5f7fa;
}

.username {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.arrow-down {
  font-size: 12px;
  color: #999;
}

/* ============ 内容区样式 ============ */
.admin-main {
  background: transparent;
  padding: 20px;
  overflow-y: auto;
  flex: 1;
}

/* 内容切换动画 */
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}

/* ============ 个人中心对话框样式 ============ */
.avatar-upload-container {
  display: flex;
  align-items: center;
  gap: 20px;
}

.avatar-uploader {
  display: flex;
  align-items: center;
}

/* ============ 响应式设计 ============ */
@media (max-width: 768px) {
  .admin-aside {
    position: fixed;
    left: 0;
    top: 0;
    z-index: 1000;
  }

  .username {
    display: none;
  }

  .header-left {
    gap: 12px;
  }

  .admin-main {
    padding: 12px;
  }
}
</style>

<style>
/* 管理后台全局 el-card 样式覆盖 */
.admin-layout .el-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}
</style>
