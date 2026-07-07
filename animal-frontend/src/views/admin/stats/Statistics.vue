<template>
  <div class="page-container">
    <!-- 访问统计 -->
    <el-row :gutter="20">
      <el-col :xs="24" :lg="12">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <el-icon><TrendCharts /></el-icon>
              <span>访问量统计</span>
            </div>
          </template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="总浏览量">
              <el-tag type="success" size="large">{{ visitStats.totalViews }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="动物总数">
              {{ visitStats.animalCount }}
            </el-descriptions-item>
            <el-descriptions-item label="平均浏览量">
              {{ visitStats.avgViews }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="12">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <el-icon><User /></el-icon>
              <span>用户活跃度</span>
            </div>
          </template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="用户总数">
              <el-tag type="primary" size="large">{{ userStats.totalUsers }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="活跃用户">
              <el-tag type="success">{{ userStats.activeUsers }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="禁用用户">
              <el-tag type="danger">{{ userStats.inactiveUsers }}</el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>

    <!-- 排行榜 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :xs="24" :lg="12">
        <el-card shadow="hover" class="rank-card">
          <template #header>
            <div class="card-header">
              <el-icon><View /></el-icon>
              <span>浏览量排行 TOP 20</span>
            </div>
          </template>
          <div ref="animalRankChartRef" style="height: 500px;"></div>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="12">
        <el-card shadow="hover" class="rank-card">
          <template #header>
            <div class="card-header">
              <el-icon><Star /></el-icon>
              <span>收藏量排行 TOP 20</span>
            </div>
          </template>
          <div ref="collectRankChartRef" style="height: 500px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { TrendCharts, User, View, Star } from '@element-plus/icons-vue'
import { getVisitStats, getUserActiveStats, getAnimalRank, getCollectRank } from '@/api/admin'
import * as echarts from 'echarts'

const visitStats = ref({
  totalViews: 0,
  animalCount: 0,
  avgViews: 0
})

const userStats = ref({
  totalUsers: 0,
  activeUsers: 0,
  inactiveUsers: 0
})

const animalRankChartRef = ref()
const collectRankChartRef = ref()
let animalRankChart = null
let collectRankChart = null

const fetchVisitStats = async () => {
  try {
    const res = await getVisitStats()
    visitStats.value = res.data
  } catch (error) {
    ElMessage.error('获取访问统计失败')
  }
}

const fetchUserStats = async () => {
  try {
    const res = await getUserActiveStats()
    userStats.value = res.data
  } catch (error) {
    ElMessage.error('获取用户统计失败')
  }
}

const fetchAnimalRank = async () => {
  try {
    const res = await getAnimalRank()
    await nextTick()
    initAnimalRankChart(res.data)
  } catch (error) {
    ElMessage.error('获取浏览排行失败')
  }
}

const fetchCollectRank = async () => {
  try {
    const res = await getCollectRank()
    await nextTick()
    initCollectRankChart(res.data)
  } catch (error) {
    ElMessage.error('获取收藏排行失败')
  }
}

const initAnimalRankChart = (data) => {
  if (!animalRankChartRef.value) return
  
  animalRankChart = echarts.init(animalRankChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'value',
      name: '浏览量'
    },
    yAxis: {
      type: 'category',
      data: data.map(item => item.name).reverse()
    },
    series: [
      {
        name: '浏览量',
        type: 'bar',
        data: data.map(item => item.viewCount).reverse(),
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
            { offset: 0, color: '#409eff' },
            { offset: 1, color: '#a0cfff' }
          ])
        },
        label: {
          show: true,
          position: 'right'
        }
      }
    ]
  }
  
  animalRankChart.setOption(option)
}

const initCollectRankChart = (data) => {
  if (!collectRankChartRef.value) return
  
  collectRankChart = echarts.init(collectRankChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'value',
      name: '收藏量'
    },
    yAxis: {
      type: 'category',
      data: data.map(item => item.name).reverse()
    },
    series: [
      {
        name: '收藏量',
        type: 'bar',
        data: data.map(item => item.collectCount).reverse(),
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
            { offset: 0, color: '#f56c6c' },
            { offset: 1, color: '#fbc4c4' }
          ])
        },
        label: {
          show: true,
          position: 'right'
        }
      }
    ]
  }
  
  collectRankChart.setOption(option)
}

onMounted(() => {
  fetchVisitStats()
  fetchUserStats()
  fetchAnimalRank()
  fetchCollectRank()
  
  window.addEventListener('resize', () => {
    animalRankChart?.resize()
    collectRankChart?.resize()
  })
})
</script>

<style scoped>
.page-container {
  padding: 20px;
}
.chart-card, .rank-card {
  margin-bottom: 20px;
}
.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 500;
}
</style>

