<template>
  <div class="personal-page">
    <div class="container">
      <el-row :gutter="24">
        <!-- 左侧菜单 -->
        <el-col :xs="24" :md="6">
          <el-card class="menu-card" shadow="never">
            <div class="user-profile">
              <el-upload
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
                action="/api/user/upload-avatar"
                :headers="{ Authorization: 'Bearer ' + userStore.token }"
              >
                <el-avatar :src="userStore.userInfo.avatar" :size="80" class="avatar-uploader" />
              </el-upload>
              <h3>{{ userStore.userInfo.nickname || userStore.userInfo.username }}</h3>
            </div>
            <el-menu :default-active="activeMenu" @select="handleMenuSelect">
              <el-menu-item index="info">
                <el-icon><User /></el-icon>
                <span>基本信息</span>
              </el-menu-item>
              <el-menu-item index="collects">
                <el-icon><Star /></el-icon>
                <span>我的收藏</span>
              </el-menu-item>
              <el-menu-item index="comments">
                <el-icon><ChatDotRound /></el-icon>
                <span>我的评论</span>
              </el-menu-item>
              <el-menu-item index="questions">
                <el-icon><QuestionFilled /></el-icon>
                <span>我的提问</span>
              </el-menu-item>
              <el-menu-item index="history">
                <el-icon><Clock /></el-icon>
                <span>浏览历史</span>
              </el-menu-item>
            </el-menu>
          </el-card>
        </el-col>

        <!-- 右侧内容 -->
        <el-col :xs="24" :md="18">
          <!-- 基本信息 -->
          <el-card v-if="activeMenu === 'info'" class="content-card" shadow="never">
            <template #header>
              <span>基本信息</span>
            </template>
            <el-form :model="userForm" label-width="100px" class="user-info-form">
              <el-form-item label="用户名">
                <el-input v-model="userForm.username" disabled>
                  <template #prefix>
                    <el-icon><User /></el-icon>
                  </template>
                </el-input>
              </el-form-item>
              <el-form-item label="昵称">
                <el-input v-model="userForm.nickname">
                  <template #prefix>
                    <el-icon><Avatar /></el-icon>
                  </template>
                </el-input>
              </el-form-item>
              <el-form-item label="邮箱">
                <el-input v-model="userForm.email">
                  <template #prefix>
                    <el-icon><Message /></el-icon>
                  </template>
                </el-input>
              </el-form-item>
              <el-form-item label="手机号">
                <el-input v-model="userForm.phone">
                  <template #prefix>
                    <el-icon><Iphone /></el-icon>
                  </template>
                </el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleUpdateInfo">保存修改</el-button>
              </el-form-item>
            </el-form>
          </el-card>

          <!-- 我的收藏 -->
          <el-card v-if="activeMenu === 'collects'" class="content-card" shadow="never" v-loading="loading">
            <template #header>
              <span>我的收藏</span>
            </template>
            <div v-if="collects.length === 0" class="empty">
              <el-empty description="暂无收藏" />
            </div>
            <div v-else class="animal-grid">
              <div
                v-for="animal in collects"
                :key="animal.id"
                class="animal-card hover-card"
              >
                <div class="animal-image" @click="goAnimalDetail(animal.id)">
                  <img :src="animal.coverImage" :alt="animal.name" />
                </div>
                <div class="animal-info">
                  <h3 @click="goAnimalDetail(animal.id)">{{ animal.name }}</h3>
                  <p>{{ animal.scientificName }}</p>
                  <el-button 
                    type="danger" 
                    size="small" 
                    :icon="Delete"
                    @click="handleCancelCollect(animal.id)"
                    class="cancel-collect-btn"
                  >
                    取消收藏
                  </el-button>
                </div>
              </div>
            </div>
          </el-card>

          <!-- 我的评论 -->
          <el-card v-if="activeMenu === 'comments'" class="content-card" shadow="never" v-loading="loading">
            <template #header>
              <span>我的评论</span>
            </template>
            <div v-if="comments.length === 0" class="empty">
              <el-empty description="暂无评论" />
            </div>
            <div v-else class="comment-list">
              <div v-for="item in comments" :key="item.comment.id" class="comment-item">
                <div class="comment-animal" @click="goAnimalDetail(item.animal.id)">
                  <img :src="item.animal.coverImage" :alt="item.animal.name" />
                  <span>{{ item.animal.name }}</span>
                </div>
                <div class="comment-content">{{ item.comment.content }}</div>
                <div class="comment-footer-personal">
                  <span class="comment-time">{{ formatTime(item.comment.createTime) }}</span>
                  <el-button 
                    type="danger" 
                    size="small" 
                    text
                    :icon="Delete"
                    @click="handleDeleteMyComment(item.comment.id)"
                  >
                    删除
                  </el-button>
                </div>
              </div>
            </div>
          </el-card>

          <!-- 我的提问 -->
          <el-card v-if="activeMenu === 'questions'" class="content-card" shadow="never" v-loading="loading">
            <template #header>
              <span>我的提问</span>
            </template>
            <div v-if="questions.length === 0" class="empty">
              <el-empty description="暂无提问" />
            </div>
            <div v-else class="question-list">
              <div
                v-for="question in questions"
                :key="question.id"
                class="question-item"
                @click="goQuestionDetail(question.id)"
              >
                <h3>{{ question.title }}</h3>
                <p>{{ question.content }}</p>
                <div class="question-meta">
                  <span>{{ question.answerCount }} 回答</span>
                  <span>{{ formatTime(question.createTime) }}</span>
                </div>
              </div>
            </div>
          </el-card>

          <!-- 浏览历史 -->
          <el-card v-if="activeMenu === 'history'" class="content-card" shadow="never" v-loading="loading">
            <template #header>
              <div class="card-header">
                <span>浏览历史</span>
                <el-button 
                  v-if="history.length > 0"
                  type="danger" 
                  size="small" 
                  @click="handleClearHistory"
                >
                  <el-icon><Delete /></el-icon>
                  清空浏览历史
                </el-button>
              </div>
            </template>
            <div v-if="history.length === 0" class="empty">
              <el-empty description="暂无浏览记录" />
            </div>
            <div v-else class="history-list">
              <div
                v-for="item in history"
                :key="item.animal.id + item.browseTime"
                class="history-item"
                @click="goAnimalDetail(item.animal.id)"
              >
                <img :src="item.animal.coverImage" :alt="item.animal.name" />
                <div class="history-info">
                  <h4>{{ item.animal.name }}</h4>
                  <p>{{ item.animal.scientificName }}</p>
                  <span class="browse-time">{{ formatTime(item.browseTime) }}</span>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { updateUser } from '@/api/user'
import { getMyCollects, getMyComments, getMyQuestions, getBrowseHistory, clearBrowseHistory } from '@/api/personal'
import { deleteCollect } from '@/api/collect'
import { deleteComment } from '@/api/comment'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const activeMenu = ref('info')
const loading = ref(false)

const userForm = reactive({
  username: '',
  nickname: '',
  email: '',
  phone: ''
})

const collects = ref([])
const comments = ref([])
const questions = ref([])
const history = ref([])

const initUserForm = () => {
  userForm.username = userStore.userInfo.username
  userForm.nickname = userStore.userInfo.nickname
  userForm.email = userStore.userInfo.email
  userForm.phone = userStore.userInfo.phone
}

const handleMenuSelect = async (index) => {
  activeMenu.value = index
  loading.value = true
  try {
    switch (index) {
      case 'collects':
        const collectRes = await getMyCollects()
        collects.value = collectRes.data
        break
      case 'comments':
        const commentRes = await getMyComments()
        comments.value = commentRes.data
        break
      case 'questions':
        const questionRes = await getMyQuestions()
        questions.value = questionRes.data
        break
      case 'history':
        const historyRes = await getBrowseHistory()
        history.value = historyRes.data
        break
    }
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const handleUpdateInfo = async () => {
  try {
    await updateUser(userForm)
    ElMessage.success('更新成功')
    userStore.getUserInfo()
  } catch (error) {
    ElMessage.error('更新失败')
  }
}

const handleAvatarSuccess = () => {
  ElMessage.success('头像上传成功')
  userStore.getUserInfo()
}

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

const goAnimalDetail = (id) => {
  router.push(`/animal/${id}`)
}

const goQuestionDetail = (id) => {
  router.push(`/question/${id}`)
}

const formatTime = (time) => {
  return new Date(time).toLocaleString('zh-CN')
}

const handleClearHistory = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要清空所有浏览历史吗？此操作不可恢复。',
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await clearBrowseHistory()
    ElMessage.success('浏览历史已清空')
    history.value = []
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('清空失败')
    }
  }
}

const handleCancelCollect = async (animalId) => {
  try {
    await ElMessageBox.confirm(
      '确定要取消收藏吗？',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await deleteCollect(animalId)
    ElMessage.success('取消收藏成功')
    // 重新获取收藏列表
    const collectRes = await getMyCollects()
    collects.value = collectRes.data
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

const handleDeleteMyComment = async (commentId) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这条评论吗？',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await deleteComment(commentId)
    ElMessage.success('删除成功')
    // 重新获取评论列表
    const commentRes = await getMyComments()
    comments.value = commentRes.data
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  initUserForm()
})
</script>

<style scoped>
.personal-page {
  padding: 30px 0;
  background: transparent;
  min-height: calc(100vh - 350px);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.menu-card {
  position: sticky;
  top: 90px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.user-profile {
  text-align: center;
  padding: 20px 0;
  border-bottom: 1px solid #eee;
  margin-bottom: 20px;
}

.avatar-uploader {
  cursor: pointer;
  transition: all 0.3s;
  margin-bottom: 15px;
}

.avatar-uploader:hover {
  transform: scale(1.05);
}

.user-profile h3 {
  font-size: 18px;
  margin-bottom: 8px;
  color: #303133;
}

.user-profile p {
  font-size: 14px;
  color: #909399;
}

.content-card {
  min-height: 400px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.content-card .card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.user-info-form :deep(.el-form-item) {
  margin-bottom: 28px;
}

.empty {
  padding: 60px 0;
}

.animal-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
}

.animal-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 4px solid rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  cursor: pointer;
  transition: all 0.3s;
}

.animal-card:nth-child(8n+1) {
  border-color: rgba(254, 88, 175, 0.7);
}

.animal-card:nth-child(8n+2) {
  border-color: rgba(95, 87, 255, 0.7);
}

.animal-card:nth-child(8n+3) {
  border-color: rgba(45, 255, 65, 0.7);
}

.animal-card:nth-child(8n+4) {
  border-color: rgba(255, 239, 87, 0.7);
}

.animal-card:nth-child(8n+5) {
  border-color: rgba(0, 210, 255, 0.7);
}

.animal-card:nth-child(8n+6) {
  border-color: rgba(255, 140, 0, 0.7);
}

.animal-card:nth-child(8n+7) {
  border-color: rgba(255, 26, 217, 0.7);
}

.animal-card:nth-child(8n+8) {
  border-color: rgba(23, 131, 255, 0.7);
}

.animal-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
}

.animal-image {
  width: 100%;
  height: 150px;
}

.animal-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.animal-info {
  padding: 12px;
}

.animal-info h3 {
  font-size: 16px;
  margin-bottom: 6px;
  color: #303133;
  cursor: pointer;
  transition: color 0.3s;
}

.animal-info h3:hover {
  color: #906bff;
}

.animal-info p {
  font-size: 13px;
  color: #909399;
  font-style: italic;
  margin-bottom: 10px;
}

.cancel-collect-btn {
  width: 100%;
  margin-top: 8px;
}

.comment-list, .question-list, .history-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.comment-item {
  padding: 16px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.comment-animal {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 12px;
  cursor: pointer;
  width: fit-content;
}

.comment-animal img {
  width: 40px;
  height: 40px;
  border-radius: 4px;
  object-fit: cover;
}

.comment-animal span {
  font-weight: 500;
  color: #906bff;
}

.comment-content {
  font-size: 14px;
  line-height: 1.6;
  color: #606266;
  margin-bottom: 8px;
}

.comment-footer-personal {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.comment-time {
  font-size: 12px;
  color: #909399;
}

.question-item {
  padding: 16px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  cursor: pointer;
  transition: all 0.3s;
}

.question-item:hover {
  background: rgba(255, 255, 255, 0.95);
}

.question-item h3 {
  font-size: 16px;
  margin-bottom: 8px;
  color: #303133;
}

.question-item p {
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.question-meta {
  display: flex;
  gap: 20px;
  font-size: 12px;
  color: #909399;
}

.history-item {
  display: flex;
  gap: 12px;
  padding: 12px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  cursor: pointer;
  transition: all 0.3s;
}

.history-item:hover {
  background: rgba(255, 255, 255, 0.95);
}

.history-item img {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  object-fit: cover;
}

.history-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.history-info h4 {
  font-size: 16px;
  color: #303133;
}

.history-info p {
  font-size: 14px;
  color: #909399;
  font-style: italic;
}

.browse-time {
  font-size: 12px;
  color: #909399;
}
</style>
