<template>
  <div class="question-detail-page">
    <div class="container" v-loading="loading">
      <!-- 面包屑导航 -->
      <el-breadcrumb :separator-icon="ArrowRight" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/' }">
          <el-icon><HomeFilled /></el-icon>
          首页
        </el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/questions' }">问答社区</el-breadcrumb-item>
        <el-breadcrumb-item>{{ questionData.question?.title || '问题详情' }}</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 问题详情 -->
      <el-card class="question-card" shadow="never">
        <div class="question-header">
          <div class="user-info">
            <el-avatar :src="questionData.user?.avatar" :size="50" />
            <div class="user-details">
              <div class="username">{{ questionData.user?.nickname || questionData.user?.username }}</div>
              <div class="time">{{ formatTime(questionData.question?.createTime) }}</div>
            </div>
          </div>
          <el-tag v-if="questionData.question?.isTop" type="danger" effect="dark">置顶</el-tag>
        </div>

        <h1 class="question-title">{{ questionData.question?.title }}</h1>
        <div class="question-content">{{ questionData.question?.content }}</div>
        
        <div class="question-meta">
          <span>{{ questionData.question?.answerCount }} 回答</span>
        </div>
      </el-card>

      <!-- 回答列表 -->
      <el-card class="answer-card" shadow="never">
        <template #header>
          <div class="card-header">
            <span>全部回答 ({{ questionData.answers?.length || 0 }})</span>
          </div>
        </template>

        <!-- 发布回答 -->
        <div class="answer-input" v-if="userStore.isLogin">
          <el-input
            v-model="answerContent"
            type="textarea"
            :rows="4"
            placeholder="写下你的回答..."
            maxlength="1000"
            show-word-limit
          />
          <el-button
            type="primary"
            style="margin-top: 10px"
            @click="handleAddAnswer"
            :disabled="!answerContent.trim()"
          >
            发布回答
          </el-button>
        </div>
        <div v-else class="login-tip">
          <el-button type="primary" link @click="goLogin">登录</el-button>
          后发表回答
        </div>

        <!-- 回答列表 -->
        <div class="answer-list">
          <div v-if="!questionData.answers || questionData.answers.length === 0" class="no-answer">
            暂无回答，快来抢沙发吧！
          </div>
          <div
            v-for="item in questionData.answers"
            :key="item.answer.id"
            class="answer-item"
            :class="{ accepted: item.answer.isAccepted }"
          >
            <div class="answer-header">
              <div class="user-info">
                <el-avatar :src="item.user.avatar" :size="40" />
                <div class="user-details">
                  <div class="username">{{ item.user.nickname || item.user.username }}</div>
                  <div class="time">{{ formatTime(item.answer.createTime) }}</div>
                </div>
              </div>
              <div class="answer-actions">
                <el-tag v-if="item.answer.isAccepted" type="success" effect="dark">
                  <el-icon><Select /></el-icon>
                  已采纳
                </el-tag>
                <el-button
                  v-else-if="userStore.userId === questionData.question?.userId"
                  type="success"
                  size="small"
                  @click="handleAcceptAnswer(item.answer.id)"
                >
                  采纳
                </el-button>
              </div>
            </div>
            <div class="answer-content">{{ item.answer.content }}</div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getQuestionDetail, addAnswer, acceptAnswer } from '@/api/question'
import { ElMessage } from 'element-plus'
import { ArrowRight } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const questionData = ref({})
const answerContent = ref('')

const fetchQuestionDetail = async () => {
  loading.value = true
  try {
    const res = await getQuestionDetail(route.params.id)
    questionData.value = res.data
  } catch (error) {
    ElMessage.error('获取问题详情失败')
  } finally {
    loading.value = false
  }
}

const handleAddAnswer = async () => {
  if (!answerContent.value.trim()) return

  try {
    await addAnswer({
      questionId: route.params.id,
      content: answerContent.value
    })
    ElMessage.success('回答成功')
    answerContent.value = ''
    fetchQuestionDetail()
  } catch (error) {
    ElMessage.error('回答失败')
  }
}

const handleAcceptAnswer = async (id) => {
  try {
    await acceptAnswer(id)
    ElMessage.success('采纳成功')
    fetchQuestionDetail()
  } catch (error) {
    ElMessage.error('采纳失败')
  }
}

const goLogin = () => {
  router.push('/login')
}

const formatTime = (time) => {
  return new Date(time).toLocaleString('zh-CN')
}

onMounted(() => {
  fetchQuestionDetail()
})
</script>

<style scoped>
.question-detail-page {
  padding: 30px 0;
  background: #f5f7fa;
  min-height: calc(100vh - 350px);
}

.container {
  max-width: 900px;
  margin: 0 auto;
  padding: 0 20px;
}

.breadcrumb {
  margin-bottom: 20px;
  padding: 15px 20px;
  background: white;
  border-radius: 8px;
}

.breadcrumb :deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: #606266;
  font-weight: 500;
  max-width: 400px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: inline-block;
  vertical-align: middle;
}

.question-card, .answer-card {
  margin-bottom: 20px;
}

.question-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.username {
  font-weight: 500;
  color: #303133;
}

.time {
  font-size: 12px;
  color: #909399;
}

.question-title {
  font-size: 28px;
  margin-bottom: 20px;
  color: #303133;
}

.question-content {
  font-size: 16px;
  line-height: 1.8;
  color: #606266;
  margin-bottom: 20px;
}

.question-meta {
  display: flex;
  gap: 20px;
  font-size: 14px;
  color: #909399;
}

.card-header {
  font-size: 18px;
  font-weight: 500;
}

.answer-input {
  margin-bottom: 30px;
}

.login-tip {
  text-align: center;
  padding: 20px;
  color: #909399;
  margin-bottom: 30px;
}

.answer-list {
  margin-top: 20px;
}

.no-answer {
  text-align: center;
  padding: 40px;
  color: #909399;
}

.answer-item {
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 16px;
  background: #f9fafb;
  border: 2px solid transparent;
  transition: all 0.3s;
}

.answer-item.accepted {
  background: #f0f9ff;
  border-color: #67c23a;
}

.answer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.answer-actions {
  display: flex;
  gap: 8px;
}

.answer-content {
  font-size: 15px;
  line-height: 1.8;
  color: #606266;
}
</style>

