<template>
  <div class="animal-list-page">
    <div class="container">
      <div class="page-header">
        <h1>
          <el-icon><Grid /></el-icon>
          猫咪档案库
        </h1>
        <p>你想了解的个咪信息都在这里</p>
      </div>

      <!-- 搜索和筛选 -->
      <div class="search-section">
          <div class="search-box">
            <el-input
              v-model="searchParams.keyword"
              placeholder="搜索动物名称或学名..."
              clearable
              @keyup.enter="handleSearch"
              class="search-input"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-button class="rainbow-search-btn" @click="handleSearch">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
            <el-select
              v-model="searchParams.categoryId"
              placeholder="分类筛选"
              clearable
              @change="handleSearch"
              class="filter-select"
            >
              <el-option label="全部分类" :value="null" />
              <el-option
                v-for="category in categories"
                :key="category.id"
                :label="category.name"
                :value="category.id"
              />
            </el-select>
          </div>
      </div>

      <!-- 动物列表 -->
      <div class="animal-list" v-loading="loading">
        <div v-if="animalList.length === 0" class="empty">
          <el-empty description="暂无动物数据" />
        </div>
        <div v-else class="animal-grid">
          <div
            v-for="animal in animalList"
            :key="animal.id"
            class="animal-card hover-card"
            @click="goDetail(animal.id)"
          >
            <div class="animal-image">
              <img :src="animal.coverImage" :alt="animal.name" />
              <div class="animal-mask">
                <el-button type="primary" circle size="large">
                  <el-icon><View /></el-icon>
                </el-button>
              </div>
            </div>
            <div class="animal-content">
              <h3>{{ animal.name }}</h3>
              <p class="scientific-name">{{ animal.scientificName }}</p>
              <el-tag v-if="animal.protectionLevel" type="warning" size="small">
                {{ animal.protectionLevel }}
              </el-tag>
              <div class="animal-stats">
                <span>
                  <el-icon><View /></el-icon>
                  {{ animal.viewCount }}
                </span>
                <span>
                  <el-icon><Star /></el-icon>
                  {{ animal.collectCount }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <!-- 分页 -->
        <div v-if="total > 0" class="pagination">
          <el-pagination
            :current-page="searchParams.pageNum"
            :page-size="searchParams.pageSize"
            :total="total"
            :page-sizes="[12, 24, 36, 48]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="(val) => { searchParams.pageSize = val; searchParams.pageNum = 1; fetchAnimalList(); }"
            @current-change="(val) => { searchParams.pageNum = val; fetchAnimalList(); }"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getAnimalList } from '@/api/animal'
import { getCategories } from '@/api/home'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const animalList = ref([])
const categories = ref([])
const total = ref(0)

const searchParams = reactive({
  pageNum: 1,
  pageSize: 12,
  categoryId: null,
  keyword: ''
})

const fetchCategories = async () => {
  try {
    const res = await getCategories()
    categories.value = res.data
  } catch (error) {
    console.error('获取分类失败', error)
  }
}

const fetchAnimalList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: searchParams.pageNum,
      pageSize: searchParams.pageSize
    }
    if (searchParams.categoryId) {
      params.categoryId = searchParams.categoryId
    }
    if (searchParams.keyword) {
      params.keyword = searchParams.keyword
    }

    const res = await getAnimalList(params)
    animalList.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    ElMessage.error('获取动物列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  searchParams.pageNum = 1
  fetchAnimalList()
}

const goDetail = (id) => {
  router.push(`/animal/${id}`)
}

// 监听路由参数变化
watch(
  () => route.query,
  (query) => {
    if (query.categoryId) {
      searchParams.categoryId = Number(query.categoryId)
    }
    if (query.keyword) {
      searchParams.keyword = query.keyword
    }
    fetchAnimalList()
  },
  { immediate: false }
)

onMounted(() => {
  fetchCategories()
  
  // 初始化路由参数
  if (route.query.categoryId) {
    searchParams.categoryId = Number(route.query.categoryId)
  }
  if (route.query.keyword) {
    searchParams.keyword = route.query.keyword
  }
  
  fetchAnimalList()
})
</script>

<style scoped>
.animal-list-page {
  padding: 30px 0;
  min-height: calc(100vh - 350px);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.page-header {
  text-align: center;
  margin-bottom: 30px;
}

.page-header h1 {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  font-size: 32px;
  margin-bottom: 10px;
  color: #303133;
}

.page-header p {
  font-size: 16px;
  color: #909399;
}

.search-section {
  margin-bottom: 30px;
  display: flex;
  justify-content: center;
}

.search-box {
  display: flex;
  align-items: center;
  gap: 12px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 50px;
  padding: 10px 14px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  flex-wrap: wrap;
  justify-content: center;
}

.search-input {
  flex: 1;
  min-width: 260px;
  max-width: 500px;
}

.search-input :deep(.el-input__wrapper) {
  background: transparent;
  box-shadow: none !important;
  border: none;
  border-radius: 25px;
  padding: 8px 12px;
  font-size: 16px;
}

.search-input :deep(.el-input__inner) {
  font-size: 16px;
}

.rainbow-search-btn {
  background: #fb9fac !important;
  border: none !important;
  color: white !important;
  font-weight: 600;
  border-radius: 25px !important;
  padding: 12px 28px !important;
  height: auto !important;
  font-size: 16px;
  transition: opacity 0.3s;
  white-space: nowrap;
  margin-left: -6px;
}

.rainbow-search-btn:hover {
  opacity: 0.85;
}

.filter-select {
  width: 180px;
}

.filter-select :deep(.el-select__wrapper) {
  background: #fce4ec !important;
  box-shadow: none !important;
  border: none !important;
  border-radius: 25px;
  padding: 12px 16px;
}

.filter-select :deep(.el-input__wrapper) {
  background: #fce4ec !important;
  box-shadow: none !important;
  border: none;
  border-radius: 25px;
  padding: 12px 16px;
  font-size: 16px;
}

.filter-select :deep(.el-input__inner) {
  font-size: 16px;
  color: #303133;
}

.filter-select :deep(.el-select__placeholder) {
  color: #606266;
}

.animal-list {
  min-height: 400px;
}

.empty {
  padding: 60px 0;
}

.animal-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 24px;
  margin-bottom: 30px;
}

.animal-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 4px solid rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
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

.animal-image {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.animal-image::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: linear-gradient(to bottom, transparent, rgba(255, 255, 255, 0.6));
  pointer-events: none;
}

.animal-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.animal-card:hover .animal-image img {
  transform: scale(1.1);
}

.animal-content {
  padding: 20px;
}

.animal-content h3 {
  font-size: 18px;
  margin-bottom: 8px;
  color: #303133;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.scientific-name {
  font-size: 14px;
  color: #909399;
  font-style: italic;
  margin-bottom: 10px;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.animal-stats {
  display: flex;
  gap: 20px;
  font-size: 14px;
  color: #606266;
  margin-top: 12px;
}

.animal-stats span {
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
  .search-box {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-box .el-select {
    width: 100% !important;
  }

  .animal-grid {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  }
}
</style>

