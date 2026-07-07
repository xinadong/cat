<template>
  <div class="article-list-page">
    <div class="container">
      <div class="page-header">
        <h1>
          <el-icon><Document /></el-icon>
          科普文章
        </h1>
        <p>探索动物世界的奥秘</p>
      </div>

      <div class="article-list" v-loading="loading">
        <div v-if="articles.length === 0" class="empty">
          <el-empty description="暂无文章" />
        </div>
        <div v-else>
          <div
            v-for="article in articles"
            :key="article.id"
            class="article-item hover-card"
            @click="goDetail(article.id)"
          >
            <div class="article-cover">
              <img :src="article.coverImage" :alt="article.title" />
            </div>
            <div class="article-content">
              <h3>{{ article.title }}</h3>
              <p class="article-summary">{{ getArticleSummary(article.content) }}</p>
              <div class="article-meta">
                <el-tag size="small" v-if="article.category">{{ article.category }}</el-tag>
                <span class="view-count">
                  <el-icon><View /></el-icon>
                  {{ article.viewCount }}
                </span>
                <span class="time">{{ formatTime(article.createTime) }}</span>
              </div>
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
            @current-change="(val) => { pageNum = val; fetchArticles(); }"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getArticleList } from '@/api/article'
import { ElMessage } from 'element-plus'

const router = useRouter()

const loading = ref(false)
const articles = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

const fetchArticles = async () => {
  loading.value = true
  try {
    const res = await getArticleList({
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    articles.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    ElMessage.error('获取文章列表失败')
  } finally {
    loading.value = false
  }
}

const getArticleSummary = (content) => {
  const text = content.replace(/<[^>]+>/g, '').substring(0, 150)
  return text + (content.length > 150 ? '...' : '')
}

const goDetail = (id) => {
  router.push(`/article/${id}`)
}

const formatTime = (time) => {
  return new Date(time).toLocaleDateString('zh-CN')
}

onMounted(() => {
  fetchArticles()
})
</script>

<style scoped>
.article-list-page {
  padding: 30px 0;
  background: #f5f7fa;
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
}

.article-list {
  min-height: 400px;
}

.empty {
  padding: 60px 0;
}

.article-item {
  display: flex;
  gap: 20px;
  background: white;
  padding: 20px;
  border-radius: 12px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  cursor: pointer;
}

.article-cover {
  width: 200px;
  height: 150px;
  flex-shrink: 0;
  border-radius: 8px;
  overflow: hidden;
}

.article-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.article-item:hover .article-cover img {
  transform: scale(1.1);
}

.article-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.article-content h3 {
  font-size: 20px;
  margin-bottom: 12px;
  color: #303133;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-summary {
  flex: 1;
  font-size: 14px;
  line-height: 1.8;
  color: #606266;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-bottom: 12px;
}

.article-meta {
  display: flex;
  align-items: center;
  gap: 15px;
  font-size: 14px;
  color: #909399;
}

.view-count, .time {
  display: flex;
  align-items: center;
  gap: 5px;
}

.pagination {
  display: flex;
  justify-content: center;
  padding: 30px 0;
}

@media (max-width: 768px) {
  .article-item {
    flex-direction: column;
  }

  .article-cover {
    width: 100%;
    height: 200px;
  }
}
</style>

