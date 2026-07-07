<template>
  <div class="home">
    <!-- 轮播图 -->
    <div class="banner-section">
      <el-carousel height="500px" :interval="4000" arrow="always">
        <el-carousel-item v-for="banner in banners" :key="banner.id">
          <div class="banner-item" @click="handleBannerClick(banner)">
            <img :src="banner.imageUrl" :alt="banner.title" />
            <div class="banner-content">
              <h2>{{ banner.title }}</h2>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <div class="container">
      <!-- 分类导航 -->
      <div class="category-section">
        <div class="section-header">
          <h2>
            <el-icon><Grid /></el-icon>
            喵的家族
          </h2>
        </div>
        <div class="category-grid">
          <div
            v-for="category in categories"
            :key="category.id"
            class="category-card hover-card"
            @click="goAnimalList(category.id)"
          >
            <div class="category-icon">
              <img v-if="category.icon" :src="category.icon" :alt="category.name" />
              <el-icon v-else :size="40"><Pear /></el-icon>
            </div>
            <h3>{{ category.name }}</h3>
            <p>{{ category.description }}</p>
          </div>
        </div>
      </div>

      <!-- 热门动物 -->
      <div class="animal-section">
        <div class="section-header">
          <h2>
            <el-icon><TrendCharts /></el-icon>
            热门表情包
          </h2>
          <el-button type="primary" link @click="goAnimalList()">
            查看更多
            <el-icon><ArrowRight /></el-icon>
          </el-button>
        </div>
        <div class="animal-grid">
          <div
            v-for="animal in hotAnimals"
            :key="animal.id"
            class="animal-card hover-card"
            @click="goAnimalDetail(animal.id)"
          >
            <div class="animal-image">
              <img :src="animal.coverImage" :alt="animal.name" />
              <div class="animal-badge">
                <el-tag type="danger" effect="dark">HOT</el-tag>
              </div>
            </div>
            <div class="animal-info">
              <h3>{{ animal.name }}</h3>
              <p class="scientific-name">{{ animal.scientificName }}</p>
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
      </div>

      <!-- 最新动物 -->
      <div class="animal-section">
        <div class="section-header">
          <h2>
            <el-icon><Clock /></el-icon>
           个咪档案
          </h2>
          <el-button type="primary" link @click="goAnimalList()">
            查看更多
            <el-icon><ArrowRight /></el-icon>
          </el-button>
        </div>
        <div class="animal-grid">
          <div
            v-for="animal in newAnimals"
            :key="animal.id"
            class="animal-card hover-card"
            @click="goAnimalDetail(animal.id)"
          >
            <div class="animal-image">
              <img :src="animal.coverImage" :alt="animal.name" />
              <div class="animal-badge">
                <el-tag type="success" effect="dark">NEW</el-tag>
              </div>
            </div>
            <div class="animal-info">
              <h3>{{ animal.name }}</h3>
              <p class="scientific-name">{{ animal.scientificName }}</p>
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
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getHomeData } from '@/api/home'
import { ElMessage } from 'element-plus'
import SvgIcon from '@/components/SvgIcon.vue'

const router = useRouter()

const banners = ref([])
const categories = ref([])
const hotAnimals = ref([])
const newAnimals = ref([])

const fetchHomeData = async () => {
  try {
    const res = await getHomeData()
    banners.value = res.data.banners
    categories.value = res.data.categories
    hotAnimals.value = res.data.hotAnimals
    newAnimals.value = res.data.newAnimals
  } catch (error) {
    ElMessage.error('获取首页数据失败')
  }
}

const handleBannerClick = (banner) => {
  if (banner.linkUrl) {
    router.push(banner.linkUrl)
  }
}

const goAnimalList = (categoryId) => {
  router.push({
    path: '/animals',
    query: categoryId ? { categoryId } : {}
  })
}

const goAnimalDetail = (id) => {
  router.push(`/animal/${id}`)
}

onMounted(() => {
  fetchHomeData()
})
</script>

<style scoped>
.home {
  padding-bottom: 40px;
}

.banner-section {
  margin-bottom: 60px;
}

.banner-item {
  width: 100%;
  height: 100%;
  position: relative;
  cursor: pointer;
  overflow: hidden;
}

.banner-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s;
}

.banner-item:hover img {
  transform: scale(1.05);
}

.banner-content {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 40px;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.7), transparent);
  color: white;
}

.banner-content h2 {
  font-size: 32px;
  margin: 0;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 30px;
}

.section-header h2 {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 28px;
  color: #303133;
}

.category-section {
  margin-bottom: 60px;
}

.category-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 20px;
}

.category-card {
  background: white;
  border-radius: 12px;
  padding: 30px 20px;
  text-align: center;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.category-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto 15px;
  background: linear-gradient(135deg, #67c23a, #85ce61);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  overflow: hidden;
  padding: 12px;
}

.category-icon img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.category-card h3 {
  font-size: 20px;
  margin-bottom: 10px;
  color: #303133;
}

.category-card p {
  font-size: 14px;
  color: #909399;
  line-height: 1.6;
}

.animal-section {
  margin-bottom: 60px;
}

.animal-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 24px;
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

.animal-badge {
  position: absolute;
  top: 10px;
  right: 10px;
}

.animal-info {
  padding: 20px;
}

.animal-info h3 {
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
  margin-bottom: 12px;
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
}

.animal-stats span {
  display: flex;
  align-items: center;
  gap: 5px;
}

@media (max-width: 768px) {
  .banner-item {
    height: 300px;
  }

  .banner-content {
    padding: 20px;
  }

  .banner-content h2 {
    font-size: 24px;
  }

  .category-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  }

  .animal-grid {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  }
}
</style>

