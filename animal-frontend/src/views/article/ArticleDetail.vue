<template>
  <div class="article-detail-page">
    <div class="container" v-loading="loading">
      <!-- 面包屑导航 -->
      <el-breadcrumb :separator-icon="ArrowRight" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/' }">
          <el-icon><HomeFilled /></el-icon>
          首页
        </el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/articles' }">科普文章</el-breadcrumb-item>
        <el-breadcrumb-item>{{ article.title || '文章详情' }}</el-breadcrumb-item>
      </el-breadcrumb>

      <el-card class="article-card" shadow="never">
        <div class="article-header">
          <h1>{{ article.title }}</h1>
          <div class="article-meta">
            <el-tag v-if="article.category">{{ article.category }}</el-tag>
            <span>
              <el-icon><View /></el-icon>
              {{ article.viewCount }}
            </span>
            <span>{{ formatTime(article.createTime) }}</span>
          </div>
        </div>

        <div class="article-cover" v-if="article.coverImage">
          <img :src="article.coverImage" :alt="article.title" />
        </div>

        <div class="article-content" v-html="article.content"></div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getArticleDetail } from '@/api/article'
import { ElMessage } from 'element-plus'
import { ArrowRight } from '@element-plus/icons-vue'

const route = useRoute()

const loading = ref(false)
const article = ref({})

const fetchArticle = async () => {
  loading.value = true
  try {
    const res = await getArticleDetail(route.params.id)
    article.value = res.data
  } catch (error) {
    ElMessage.error('获取文章详情失败')
  } finally {
    loading.value = false
  }
}

const formatTime = (time) => {
  return new Date(time).toLocaleString('zh-CN')
}

onMounted(() => {
  fetchArticle()
})
</script>

<style scoped>
.article-detail-page {
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

.article-header h1 {
  font-size: 32px;
  margin-bottom: 20px;
  color: #303133;
}

.article-meta {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 30px;
  color: #909399;
}

.article-meta span {
  display: flex;
  align-items: center;
  gap: 5px;
}

.article-cover {
  margin-bottom: 30px;
  border-radius: 8px;
  overflow: hidden;
}

.article-cover img {
  width: 100%;
  max-height: 500px;
  object-fit: cover;
}

.article-content {
  font-size: 16px;
  line-height: 2;
  color: #606266;
}

.article-content :deep(p) {
  margin-bottom: 20px;
  text-indent: 2em;
}

.article-content :deep(h2) {
  font-size: 24px;
  margin: 30px 0 20px;
  color: #303133;
}

.article-content :deep(h3) {
  font-size: 20px;
  margin: 25px 0 15px;
  color: #67c23a;
}

.article-content :deep(img) {
  max-width: 100%;
  margin: 20px 0;
  border-radius: 8px;
}
</style>

