<template>
  <div class="question-list-page">
    <div class="container">
      <div class="page-header">
        <h1>
          <el-icon><ChatDotRound /></el-icon>
          问答社区
        </h1>
        <p>分享你的疑问，探讨动物知识</p>
        <el-button
          v-if="userStore.isLogin"
          type="primary"
          size="large"
          @click="showAskDialog = true"
        >
          <el-icon><Edit /></el-icon>
          我要提问
        </el-button>
        <el-button v-else type="primary" size="large" @click="goLogin">
          <el-icon><Edit /></el-icon>
          登录后提问
        </el-button>
      </div>

      <div class="question-list" v-loading="loading">
        <div v-if="questions.length === 0" class="empty">
          <el-empty description="暂无问题" />
        </div>
        <div v-else>
          <div
            v-for="item in questions"
            :key="item.question.id"
            class="question-item hover-card"
            @click="goDetail(item.question.id)"
          >
            <div class="question-header">
              <div class="user-info">
                <el-avatar :src="item.user.avatar" :size="40" />
                <div class="user-details">
                  <div class="username">{{ item.user.nickname || item.user.username }}</div>
                  <div class="time">{{ formatTime(item.question.createTime) }}</div>
                </div>
              </div>
              <el-tag v-if="item.question.isTop" type="danger" effect="dark">置顶</el-tag>
            </div>
            
            <h3 class="question-title">
              <el-icon><QuestionFilled /></el-icon>
              {{ item.question.title }}
            </h3>
            
            <p class="question-content">{{ item.question.content }}</p>
            
            <div class="question-footer">
              <span class="answer-count">
                <el-icon><ChatDotRound /></el-icon>
                {{ item.question.answerCount }} 回答
              </span>
            </div>
          </div>
        </div>

        <!-- 分页 -->
        <div v-if="total > 0" class="pagination">
          <el-pagination
            :current-page="pageNum"
            :page-size="pageSize"
            :total="total"
            layout="total, prev, pager, next"
            @current-change="(val) => { pageNum = val; fetchQuestions(); }"
          />
        </div>
      </div>
    </div>

    <!-- 提问对话框 -->
    <el-dialog
      :model-value="showAskDialog"
      @update:model-value="showAskDialog = $event"
      title="发布问题"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="问题标题" prop="title">
          <el-input
            v-model="form.title"
            placeholder="简明扼要地描述你的问题"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="问题详情" prop="content">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="6"
            placeholder="详细描述你的问题..."
            maxlength="1000"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAskDialog = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">
          发布
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getQuestionList, addQuestion } from '@/api/question'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const questions = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

const showAskDialog = ref(false)
const formRef = ref()
const submitting = ref(false)

const form = reactive({
  title: '',
  content: ''
})

const rules = {
  title: [
    { required: true, message: '请输入问题标题', trigger: 'blur' },
    { min: 5, message: '标题至少5个字', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入问题详情', trigger: 'blur' },
    { min: 10, message: '详情至少10个字', trigger: 'blur' }
  ]
}

const fetchQuestions = async () => {
  loading.value = true
  try {
    const res = await getQuestionList({
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    questions.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    ElMessage.error('获取问题列表失败')
  } finally {
    loading.value = false
  }
}

const handleSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        await addQuestion(form)
        ElMessage.success('发布成功')
        showAskDialog.value = false
        form.title = ''
        form.content = ''
        pageNum.value = 1
        fetchQuestions()
      } catch (error) {
        ElMessage.error('发布失败')
      } finally {
        submitting.value = false
      }
    }
  })
}

const goDetail = (id) => {
  router.push(`/question/${id}`)
}

const goLogin = () => {
  router.push('/login')
}

const formatTime = (time) => {
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前'
  if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前'
  if (diff < 2592000000) return Math.floor(diff / 86400000) + '天前'
  
  return date.toLocaleDateString('zh-CN')
}

onMounted(() => {
  fetchQuestions()
})
</script>

<style scoped>
.question-list-page {
  padding: 30px 0;
  min-height: calc(100vh - 350px);
}

.container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 20px;
}

.page-header {
  text-align: center;
  margin-bottom: 40px;
}

.page-header h1 {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  font-size: 32px;
  color: #303133;
  margin-bottom: 10px;
}

.page-header p {
  font-size: 16px;
  color: #909399;
  margin-bottom: 20px;
}

.question-list {
  min-height: 400px;
}

.empty {
  padding: 60px 0;
}

.question-item {
  background: white;
  padding: 24px;
  border-radius: 12px;
  margin-bottom: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  cursor: pointer;
}

.question-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
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
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 20px;
  margin-bottom: 12px;
  color: #303133;
}

.question-content {
  font-size: 14px;
  line-height: 1.8;
  color: #606266;
  margin-bottom: 16px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.question-footer {
  display: flex;
  gap: 20px;
  font-size: 14px;
  color: #909399;
}

.answer-count {
  display: flex;
  align-items: center;
  gap: 5px;
}

.pagination {
  display: flex;
  justify-content: center;
  padding: 30px 0;
}
</style>

