<template>
  <div class="animal-list-page">
    <div class="container">
      <!-- 搜索和筛选 -->
      <div class="search-section">
        <el-card shadow="never">
          <div class="search-box">
            <el-input
              v-model="searchParams.keyword"
              placeholder="搜索动物名称或学名..."
              size="large"
              clearable
              @keyup.enter="handleSearch"
              style="flex: 1; max-width: 600px;"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
              <template #append>
                <el-button type="primary" @click="handleSearch">
                  <el-icon><Search /></el-icon>
                  搜索
                </el-button>
              </template>
            </el-input>
            
            <el-select
              v-model="searchParams.categoryId"
              placeholder="分类筛选"
              size="large"
              clearable
              @change="handleSearch"
              style="width: 200px;"
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
        </el-card>
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

.search-section {
  margin-bottom: 30px;
}

.search-box {
  display: flex;
  align-items: center;
  gap: 15px;
  flex-wrap: wrap;
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
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.animal-image {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
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

.animal-mask {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.animal-card:hover .animal-mask {
  opacity: 1;
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

