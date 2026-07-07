<template>
  <div class="animal-detail-page">
    <div class="container" v-loading="loading">
      <!-- 面包屑导航 -->
      <div class="breadcrumb-wrapper">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: '/animals' }">猫咪档案库</el-breadcrumb-item>
          <el-breadcrumb-item>{{ animal.name }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <!-- 主要内容区 -->
      <div class="main-content">
        <!-- 动物基本信息 -->
        <el-card class="main-card" shadow="never">
          <div class="animal-header">
            <h1>{{ animal.name }}</h1>
            <p class="scientific-name">{{ animal.scientificName }}</p>
          </div>

          <!-- 图片轮播展示 -->
          <div class="animal-images-carousel">
            <el-carousel 
              :interval="4000" 
              arrow="always" 
              height="500px"
              indicator-position="outside"
            >
              <el-carousel-item v-for="(img, index) in imageList" :key="index">
                <el-image
                  :src="img"
                  fit="contain"
                  style="width: 100%; height: 100%;"
                  :preview-src-list="imageList"
                  :initial-index="index"
                />
              </el-carousel-item>
            </el-carousel>
          </div>

          <!-- 统计信息 -->
          <div class="animal-meta">
            <div class="meta-item">
              <el-icon><View /></el-icon>
              <span>浏览 {{ animal.viewCount }}</span>
            </div>
            <div class="meta-item">
              <el-icon><Star /></el-icon>
              <span>收藏 {{ animal.collectCount }}</span>
            </div>
            <div class="meta-item" v-if="animal.protectionLevel">
              <el-tag type="warning" size="large">{{ animal.protectionLevel }}</el-tag>
            </div>
            <div class="meta-actions">
              <el-button
                :type="isCollected ? 'warning' : 'primary'"
                :icon="isCollected ? Star : StarFilled"
                @click="toggleCollect"
                v-if="userStore.isLogin"
                size="large"
              >
                {{ isCollected ? '已收藏' : '收藏' }}
              </el-button>
            </div>
          </div>

          <!-- 详细信息 -->
          <el-divider />
          <div class="animal-details">
            <div class="detail-section" v-if="animal.appearance">
              <h3>
                <el-icon><Picture /></el-icon>
                外形特征
              </h3>
              <p>{{ animal.appearance }}</p>
            </div>

            <div class="detail-section" v-if="animal.habit">
              <h3>
                <el-icon><Sunny /></el-icon>
                生活习性
              </h3>
              <p>{{ animal.habit }}</p>
            </div>

            <div class="detail-section" v-if="animal.distribution">
              <h3>
                <el-icon><Location /></el-icon>
                分布地区
              </h3>
              <p>{{ animal.distribution }}</p>
            </div>
          </div>
        </el-card>

        <!-- 评论区 -->
        <el-card class="comment-card" shadow="never">
          <template #header>
            <div class="card-header">
              <el-icon><ChatDotRound /></el-icon>
              <span>评论区 ({{ comments.length }})</span>
            </div>
          </template>

          <!-- 发表评论 -->
          <div class="comment-input" v-if="userStore.isLogin">
            <el-input
              v-model="commentContent"
              type="textarea"
              :rows="3"
              placeholder="说说你的看法..."
              maxlength="500"
              show-word-limit
            />
            <el-button
              type="primary"
              style="margin-top: 10px"
              @click="handleAddComment"
              :disabled="!commentContent.trim()"
            >
              发表评论
            </el-button>
          </div>
          <div v-else class="login-tip">
            <el-button type="primary" link @click="goLogin">登录</el-button>
            后发表评论
          </div>

          <!-- 评论列表 -->
          <div class="comment-list">
            <div v-if="comments.length === 0" class="no-comment">
              暂无评论，快来抢沙发吧！
            </div>
            <div
              v-for="item in comments"
              :key="item.comment.id"
              class="comment-item"
            >
              <el-avatar :src="item.user.avatar" :size="40" />
              <div class="comment-content">
                <div class="comment-user">
                  {{ item.user.nickname || item.user.username }}
                </div>
                <div class="comment-text">{{ item.comment.content }}</div>
                <div class="comment-footer">
                  <span class="comment-time">{{ formatTime(item.comment.createTime) }}</span>
                  <div class="comment-actions">
                    <el-button
                      size="small"
                      text
                      @click="handleLikeComment(item.comment.id)"
                      class="like-btn"
                    >
                      <el-icon><Star /></el-icon>
                      {{ item.comment.likeCount }}
                    </el-button>
                    <el-button
                      v-if="userStore.userId === item.user.id"
                      size="small"
                      text
                      type="danger"
                      @click="handleDeleteComment(item.comment.id)"
                    >
                      删除
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 相关推荐 - 移到下方 -->
        <el-card class="recommend-card" shadow="never" v-if="recommendList.length > 0">
          <template #header>
            <div class="card-header">
              <el-icon><Grid /></el-icon>
              <span>相关推荐</span>
            </div>
          </template>
          <div class="recommend-grid">
            <div
              v-for="item in recommendList"
              :key="item.id"
              class="recommend-item"
              @click="goDetail(item.id)"
            >
              <div class="recommend-image">
                <el-image :src="item.coverImage" fit="cover" />
              </div>
              <div class="recommend-info">
                <h4>{{ item.name }}</h4>
                <p class="scientific-name-small">{{ item.scientificName }}</p>
                <div class="recommend-stats">
                  <span><el-icon><View /></el-icon> {{ item.viewCount }}</span>
                  <span><el-icon><Star /></el-icon> {{ item.collectCount }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getAnimalDetail, getRecommendAnimals } from '@/api/animal'
import { checkCollect, addCollect, deleteCollect } from '@/api/collect'
import { getCommentList, addComment, likeComment, deleteComment } from '@/api/comment'
import { ElMessage } from 'element-plus'
import { 
  Star, StarFilled, View, Picture, Sunny, Location, 
  Grid 
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const animal = ref({})
const isCollected = ref(false)
const commentContent = ref('')
const comments = ref([])
const recommendList = ref([])

const imageList = computed(() => {
  const images = []
  
  // 首先添加封面图
  if (animal.value.coverImage) {
    images.push(animal.value.coverImage)
  }
  
  // 然后添加子图片
  if (animal.value.images) {
    try {
      const subImages = JSON.parse(animal.value.images)
      if (Array.isArray(subImages) && subImages.length > 0) {
        images.push(...subImages)
      }
    } catch (e) {
      console.error('解析图片列表失败', e)
    }
  }
  
  // 如果没有任何图片，返回封面图
  return images.length > 0 ? images : [animal.value.coverImage]
})

const fetchAnimalDetail = async () => {
  loading.value = true
  try {
    const res = await getAnimalDetail(route.params.id)
    animal.value = res.data

    // 获取推荐
    if (res.data.categoryId) {
      fetchRecommend()
    }

    // 检查收藏状态
    if (userStore.isLogin) {
      checkCollectStatus()
    }

    // 获取评论
    fetchComments()
  } catch (error) {
    ElMessage.error('获取动物详情失败')
  } finally {
    loading.value = false
  }
}

const fetchRecommend = async () => {
  try {
    const res = await getRecommendAnimals({
      categoryId: animal.value.categoryId,
      currentId: animal.value.id
    })
    recommendList.value = res.data
  } catch (error) {
    console.error('获取推荐失败', error)
  }
}

const checkCollectStatus = async () => {
  try {
    const res = await checkCollect(route.params.id)
    isCollected.value = res.data.isCollected
  } catch (error) {
    console.error('检查收藏状态失败', error)
  }
}

const toggleCollect = async () => {
  try {
    if (isCollected.value) {
      await deleteCollect(animal.value.id)
      ElMessage.success('取消收藏成功')
      isCollected.value = false
      animal.value.collectCount--
    } else {
      await addCollect(animal.value.id)
      ElMessage.success('收藏成功')
      isCollected.value = true
      animal.value.collectCount++
    }
  } catch (error) {
    console.error('操作失败', error)
  }
}

const fetchComments = async () => {
  try {
    const res = await getCommentList(route.params.id)
    comments.value = res.data
  } catch (error) {
    console.error('获取评论失败', error)
  }
}

const handleAddComment = async () => {
  if (!commentContent.value.trim()) return

  try {
    await addComment({
      animalId: animal.value.id,
      content: commentContent.value
    })
    ElMessage.success('评论成功')
    commentContent.value = ''
    fetchComments()
  } catch (error) {
    ElMessage.error('评论失败')
  }
}

const handleLikeComment = async (id) => {
  try {
    await likeComment(id)
    ElMessage.success('点赞成功')
    fetchComments()
  } catch (error) {
    console.error('点赞失败', error)
  }
}

const handleDeleteComment = async (id) => {
  try {
    await deleteComment(id)
    ElMessage.success('删除成功')
    fetchComments()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

const goDetail = (id) => {
  router.push(`/animal/${id}`)
}

const goLogin = () => {
  router.push('/login')
}

const formatTime = (time) => {
  return new Date(time).toLocaleString('zh-CN')
}

// 监听路由参数变化
watch(
  () => route.params.id,
  (newId) => {
    if (newId) {
      // 重置数据
      animal.value = {}
      comments.value = []
      recommendList.value = []
      commentContent.value = ''
      // 滚动到顶部
      window.scrollTo(0, 0)
      // 重新获取数据
      fetchAnimalDetail()
    }
  }
)

onMounted(() => {
  fetchAnimalDetail()
})
</script>

<style scoped>
.animal-detail-page {
  padding: 20px 0 40px;
  background: #f5f7fa;
  min-height: calc(100vh - 350px);
}

.container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 面包屑样式 */
.breadcrumb-wrapper {
  background: #fff;
  padding: 16px 24px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.04);
}

.breadcrumb-wrapper :deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: #67c23a;
  font-weight: 500;
}

/* 主内容区 - 全宽 */
.main-content {
  width: 100%;
}

.main-card, 
.comment-card, 
.recommend-card {
  margin-bottom: 20px;
}

.animal-header h1 {
  font-size: 36px;
  margin-bottom: 10px;
  color: #303133;
  font-weight: bold;
}

.scientific-name {
  font-size: 18px;
  color: #909399;
  font-style: italic;
  margin-bottom: 20px;
}

/* 轮播图样式 */
.animal-images-carousel {
  margin: 24px 0;
  background: #f5f7fa;
  border-radius: 12px;
  overflow: hidden;
}

.animal-images-carousel :deep(.el-carousel__item) {
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
}

.animal-images-carousel :deep(.el-carousel__arrow) {
  background: rgba(103, 194, 58, 0.8);
  color: #fff;
}

.animal-images-carousel :deep(.el-carousel__arrow:hover) {
  background: rgba(103, 194, 58, 1);
}

.animal-images-carousel :deep(.el-carousel__indicator .el-carousel__button) {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: #dcdfe6;
}

.animal-images-carousel :deep(.el-carousel__indicator.is-active .el-carousel__button) {
  background: #67c23a;
}

.animal-meta {
  display: flex;
  align-items: center;
  gap: 24px;
  flex-wrap: wrap;
  padding: 20px 0;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #606266;
  font-size: 16px;
}

.meta-item .el-icon {
  font-size: 20px;
}

.meta-actions {
  margin-left: auto;
}

.animal-details {
  margin-top: 24px;
}

.detail-section {
  margin-bottom: 40px;
}

.detail-section:last-child {
  margin-bottom: 0;
}

.detail-section h3 {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 22px;
  margin-bottom: 20px;
  color: #67c23a;
  font-weight: 600;
}

.detail-section h3 .el-icon {
  font-size: 24px;
}

.detail-section p {
  font-size: 16px;
  line-height: 2;
  color: #606266;
  text-indent: 2em;
  background: #fafafa;
  padding: 20px;
  border-radius: 8px;
  border-left: 4px solid #67c23a;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

.card-header .el-icon {
  font-size: 22px;
  color: #67c23a;
}

.comment-input {
  margin-bottom: 24px;
}

.login-tip {
  text-align: center;
  padding: 20px;
  color: #909399;
  font-size: 15px;
}

.comment-list {
  margin-top: 24px;
}

.no-comment {
  text-align: center;
  padding: 60px;
  color: #909399;
  font-size: 15px;
}

.comment-item {
  display: flex;
  gap: 12px;
  padding: 20px 0;
  border-bottom: 1px solid #ebeef5;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-content {
  flex: 1;
}

.comment-user {
  font-weight: 500;
  margin-bottom: 8px;
  color: #303133;
  font-size: 15px;
}

.comment-text {
  font-size: 14px;
  line-height: 1.8;
  color: #606266;
  margin-bottom: 10px;
}

.comment-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.comment-time {
  font-size: 12px;
  color: #909399;
}

.comment-actions {
  display: flex;
  gap: 8px;
}

.like-btn {
  color: #909399;
  transition: color 0.3s;
}

.like-btn:hover {
  color: #f56c6c;
}

.like-btn:hover .el-icon {
  transform: scale(1.2);
  transition: transform 0.3s;
}

/* 相关推荐网格布局 */
.recommend-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.recommend-item {
  display: flex;
  flex-direction: column;
  cursor: pointer;
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s;
  background: #fff;
  border: 1px solid #ebeef5;
}

.recommend-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

.recommend-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.recommend-image .el-image {
  width: 100%;
  height: 100%;
  transition: transform 0.3s;
}

.recommend-item:hover .recommend-image .el-image {
  transform: scale(1.1);
}

.recommend-info {
  padding: 16px;
}

.recommend-info h4 {
  font-size: 16px;
  margin-bottom: 8px;
  color: #303133;
  font-weight: 600;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.scientific-name-small {
  font-size: 13px;
  color: #909399;
  font-style: italic;
  margin-bottom: 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.recommend-stats {
  display: flex;
  gap: 16px;
  font-size: 13px;
  color: #909399;
}

.recommend-stats span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.recommend-stats .el-icon {
  font-size: 14px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .animal-header h1 {
    font-size: 28px;
  }
  
  .animal-images-carousel {
    height: 300px;
  }
  
  .detail-section h3 {
    font-size: 18px;
  }
  
  .detail-section p {
    font-size: 14px;
  }
  
  .recommend-grid {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 15px;
  }
  
  .recommend-image {
    height: 150px;
  }
}
</style>
