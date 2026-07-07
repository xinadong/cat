<template>
  <div class="ai-assistant-page">
    <div class="container">
      <div class="chat-container">
        <!-- 头部 -->
        <div class="chat-header">
          <div class="header-content">
            <el-icon :size="32" color="white"><ChatDotRound /></el-icon>
            <div>
              <h2>AI 动物科普助手</h2>
              <p>我可以回答您关于动物的各种问题</p>
            </div>
          </div>
          <el-button 
            v-if="messages.length > 0"
            type="danger"
            :icon="Delete"
            @click="handleClearMessages"
            plain
            size="small"
          >
            清空记录
          </el-button>
        </div>

        <!-- 聊天区域 -->
        <div class="chat-messages" ref="messagesContainer">
          <!-- 欢迎消息 -->
          <div v-if="messages.length === 0" class="welcome-message">
            <el-icon :size="64" color="#67c23a"><ChatDotRound /></el-icon>
            <h3>您好！我是 AI 动物科普助手</h3>
            <p>您可以问我关于动物的任何问题，例如：</p>
            <div class="example-questions">
              <el-tag 
                v-for="example in exampleQuestions" 
                :key="example"
                @click="askQuestion(example)"
                class="example-tag"
              >
                {{ example }}
              </el-tag>
            </div>
          </div>

          <!-- 消息列表 -->
          <div 
            v-for="(msg, index) in messages" 
            :key="index"
            :class="['message', msg.role]"
          >
            <div class="message-avatar">
              <el-avatar v-if="msg.role === 'user'" :src="userStore.userInfo.avatar" :size="40" />
              <el-icon v-else :size="40" color="#67c23a"><ChatDotRound /></el-icon>
            </div>
            <div class="message-content">
              <div :class="['message-text', { 'streaming-text': streaming && index === messages.length - 1 && msg.role === 'assistant' }]" v-html="formatMessage(msg.content)"></div>
              <div v-if="msg.context && msg.context.length > 0" class="message-context">
                <el-icon><Document /></el-icon>
                参考知识：{{ msg.context.join('、') }}
              </div>
            </div>
          </div>

          <!-- 加载中 -->
          <div v-if="loading" class="message assistant">
            <div class="message-avatar">
              <el-icon :size="40" color="#67c23a"><ChatDotRound /></el-icon>
            </div>
            <div class="message-content">
              <el-icon class="loading-icon"><Loading /></el-icon>
              正在思考中...
            </div>
          </div>
        </div>

        <!-- 输入区域 -->
        <div class="chat-input">
          <el-input
            v-model="inputMessage"
            type="textarea"
            :rows="3"
            placeholder="请输入您的问题..."
            @keydown.enter.prevent="handleSend"
            :disabled="loading"
          />
          <el-button 
            type="primary" 
            :icon="Position" 
            @click="handleSend"
            :loading="loading"
            :disabled="!inputMessage.trim()"
          >
            发送
          </el-button>
        </div>

        <!-- API Key 提示 -->
        <div v-if="!hasApiKey && messages.length > 0" class="api-key-warning">
          <el-alert
            title="提示"
            type="info"
            :closable="false"
          >
            <template #default>
              <p>当前未配置 API Key，仅显示知识库内容。配置 API Key 后可获得更智能的回答。</p>
            </template>
          </el-alert>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { getAIConfig } from '@/api/ai'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ChatDotRound, Position, Loading, Document, Delete } from '@element-plus/icons-vue'
import { marked } from 'marked'

const userStore = useUserStore()
const messagesContainer = ref()
const inputMessage = ref('')
const messages = ref([])
const loading = ref(false)
const streaming = ref(false)
const hasApiKey = ref(false)
const aiConfig = ref({})

const exampleQuestions = [
  '大熊猫吃什么？',
  '东北虎和华南虎有什么区别？',
  '朱鹮为什么珍贵？',
  '如何保护野生动物？'
]

// 获取 AI 配置
const fetchConfig = async () => {
  try {
    const res = await getAIConfig()
    hasApiKey.value = res.data.hasApiKey
    aiConfig.value = res.data
  } catch (error) {
    console.error('获取配置失败', error)
  }
}

// 清空聊天记录
const handleClearMessages = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要清空所有对话记录吗？',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    messages.value = []
    ElMessage.success('已清空对话记录')
  } catch (error) {
    // 用户取消
  }
}

// 发送消息（SSE 流式）
const handleSend = async () => {
  if (!inputMessage.value.trim() || loading.value) return

  const question = inputMessage.value.trim()

  // 添加用户消息
  messages.value.push({
    role: 'user',
    content: question
  })

  inputMessage.value = ''
  loading.value = true
  streaming.value = false

  // 滚动到底部
  await nextTick()
  scrollToBottom()

  let msgIndex = -1
  let currentContext = []

  try {
    const token = localStorage.getItem('token')
    const response = await fetch('/api/ai/chat/stream', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token ? 'Bearer ' + token : ''
      },
      body: JSON.stringify({ question })
    })

    if (!response.ok) {
      throw new Error('请求失败: ' + response.status)
    }

    const reader = response.body.getReader()
    const decoder = new TextDecoder()
    let buffer = ''
    let currentEvent = ''

    while (true) {
      const { value, done } = await reader.read()
      if (done) break

      buffer += decoder.decode(value, { stream: true })
      const parts = buffer.split('\n')
      buffer = parts.pop() || ''

      for (const line of parts) {
        const trimmed = line.trim()
        if (trimmed === '') continue

        if (trimmed.startsWith('event:')) {
          currentEvent = trimmed.substring(6).trim()
        } else if (trimmed.startsWith('data:')) {
          const data = trimmed.substring(5)

          if (currentEvent === 'context') {
            try {
              const contextInfo = JSON.parse(data)
              currentContext = contextInfo.context || []
            } catch (e) { /* ignore */ }
          } else if (currentEvent === 'content') {
            if (msgIndex === -1) {
              // 收到第一块内容，隐藏加载动画，创建助手消息
              loading.value = false
              streaming.value = true
              messages.value.push({
                role: 'assistant',
                content: data,
                context: currentContext
              })
              msgIndex = messages.value.length - 1
            } else {
              // 追加后续内容
              messages.value[msgIndex].content += data
            }
            await nextTick()
            scrollToBottom()
          } else if (currentEvent === 'done') {
            streaming.value = false
          } else if (currentEvent === 'error') {
            ElMessage.error(data)
          }
        }
      }
    }

    // 如果始终没收到内容
    if (msgIndex === -1) {
      messages.value.push({
        role: 'assistant',
        content: '抱歉，我没有获得到回复，请稍后再试。',
        context: []
      })
    }

  } catch (error) {
    console.error('处理失败', error)
    ElMessage.error('获取回复失败：' + (error.message || '未知错误'))
    messages.value.push({
      role: 'assistant',
      content: '抱歉，我遇到了一些问题，请稍后再试。错误信息：' + (error.message || '未知错误')
    })
  } finally {
    loading.value = false
    streaming.value = false
    await nextTick()
    scrollToBottom()
  }
}

// 点击示例问题
const askQuestion = (question) => {
  inputMessage.value = question
  handleSend()
}

// 格式化消息（Markdown 渲染）
const formatMessage = (text) => {
  if (!text) return ''
  return marked.parse(text)
}

// 滚动到底部
const scrollToBottom = () => {
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

onMounted(() => {
  fetchConfig()
})
</script>

<style scoped>
.ai-assistant-page {
  min-height: calc(100vh - 350px);
  background: #f5f7fa;
  padding: 20px 0;
}

.container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 20px;
}

.chat-container {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  height: calc(100vh - 200px);
  min-height: 650px;
}

.chat-header {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  color: white;
  padding: 20px 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-content {
  display: flex;
  align-items: center;
  gap: 15px;
}

.header-content h2 {
  margin: 0 0 5px 0;
  font-size: 24px;
  color: white;
}

.header-content p {
  margin: 0;
  opacity: 0.9;
  font-size: 14px;
  color: white;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px 30px;
}

.welcome-message {
  text-align: center;
  padding: 60px 20px;
  color: #606266;
}

.welcome-message h3 {
  margin: 20px 0 10px;
  font-size: 24px;
  color: #303133;
}

.welcome-message p {
  margin-bottom: 20px;
  color: #909399;
}

.example-questions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center;
  margin-top: 20px;
}

.example-tag {
  cursor: pointer;
  transition: all 0.3s;
}

.example-tag:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(103, 194, 58, 0.3);
}

.message {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.message.user {
  flex-direction: row-reverse;
}

.message-avatar {
  flex-shrink: 0;
}

.message-content {
  max-width: 70%;
  padding: 12px 16px;
  border-radius: 12px;
  background: #f4f4f5;
  color: #303133;
  line-height: 1.6;
}

.message.user .message-content {
  background: #67c23a;
  color: white;
}

.message-text :deep(p) {
  margin: 6px 0;
}

.message-text :deep(p:first-child) {
  margin-top: 0;
}

.message-text :deep(p:last-child) {
  margin-bottom: 0;
}

.message-text :deep(strong) {
  font-weight: 600;
}

.message-text :deep(ul),
.message-text :deep(ol) {
  margin: 6px 0;
  padding-left: 20px;
}

.message-text :deep(li) {
  margin: 4px 0;
}

.message-text :deep(h1),
.message-text :deep(h2),
.message-text :deep(h3),
.message-text :deep(h4) {
  margin: 10px 0 6px;
  font-weight: 600;
}

.message-text :deep(h1) { font-size: 1.3em; }
.message-text :deep(h2) { font-size: 1.2em; }
.message-text :deep(h3) { font-size: 1.1em; }

.message-text :deep(code) {
  background: rgba(0, 0, 0, 0.06);
  padding: 2px 5px;
  border-radius: 3px;
  font-size: 0.9em;
}

.message-text :deep(pre) {
  background: rgba(0, 0, 0, 0.06);
  padding: 10px;
  border-radius: 6px;
  overflow-x: auto;
  margin: 8px 0;
}

.message-text :deep(pre code) {
  background: none;
  padding: 0;
}

.message-text :deep(blockquote) {
  border-left: 3px solid #67c23a;
  padding-left: 10px;
  margin: 8px 0;
  color: #606266;
}

.message-text :deep(hr) {
  border: none;
  border-top: 1px solid #ebeef5;
  margin: 10px 0;
}

.message-context {
  margin-top: 10px;
  padding-top: 10px;
  border-top: 1px solid rgba(255, 255, 255, 0.3);
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 5px;
  opacity: 0.9;
}

.message.assistant .message-context {
  border-top-color: rgba(0, 0, 0, 0.1);
}

.loading-icon {
  animation: rotate 1s linear infinite;
  margin-right: 8px;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.streaming-text::after {
  content: '▌';
  animation: blink 0.7s infinite;
  color: #67c23a;
}

@keyframes blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0; }
}

.chat-input {
  padding: 20px 30px;
  border-top: 1px solid #ebeef5;
  display: flex;
  gap: 10px;
  align-items: flex-end;
  background: #fafafa;
}

.chat-input .el-input {
  flex: 1;
}

.api-key-warning {
  padding: 0 30px 20px;
}

@media (max-width: 768px) {
  .chat-container {
    height: calc(100vh - 150px);
  }

  .message-content {
    max-width: 85%;
  }

  .chat-header {
    padding: 15px 20px;
  }

  .chat-messages {
    padding: 15px 20px;
  }

  .chat-input {
    padding: 15px 20px;
  }
}
</style>

