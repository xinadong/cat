<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="6">
        <el-card class="stats-card hover-card" shadow="hover">
          <div class="stats-icon" style="background: linear-gradient(135deg, #5B8FF9 0%, #3D76DD 100%);">
            <el-icon :size="32"><User /></el-icon>
          </div>
          <div class="stats-info">
            <div class="stats-value">{{ stats.userCount }}</div>
            <div class="stats-label">用户总数</div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="12" :sm="6">
        <el-card class="stats-card hover-card" shadow="hover">
          <div class="stats-icon" style="background: linear-gradient(135deg, #E8684A 0%, #D64A2A 100%);">
            <el-icon :size="32"><Grid /></el-icon>
          </div>
          <div class="stats-info">
            <div class="stats-value">{{ stats.animalCount }}</div>
            <div class="stats-label">动物总数</div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="12" :sm="6">
        <el-card class="stats-card hover-card" shadow="hover">
          <div class="stats-icon" style="background: linear-gradient(135deg, #5AD8A6 0%, #2FB888 100%);">
            <el-icon :size="32"><Document /></el-icon>
          </div>
          <div class="stats-info">
            <div class="stats-value">{{ stats.articleCount }}</div>
            <div class="stats-label">文章总数</div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="12" :sm="6">
        <el-card class="stats-card hover-card" shadow="hover">
          <div class="stats-icon" style="background: linear-gradient(135deg, #F6BD16 0%, #E8A003 100%);">
            <el-icon :size="32"><View /></el-icon>
          </div>
          <div class="stats-info">
            <div class="stats-value">{{ stats.todayVisit }}</div>
            <div class="stats-label">今日访问</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <!-- 左侧：分类统计饼图 -->
      <el-col :xs="24" :lg="12">
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <el-icon><PieChart /></el-icon>
              <span>动物分类统计</span>
            </div>
          </template>
          <div ref="categoryChartRef" style="height: 400px;"></div>
        </el-card>
      </el-col>

      <!-- 右侧：数据趋势柱状图 -->
      <el-col :xs="24" :lg="12">
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <el-icon><TrendCharts /></el-icon>
              <span>数据增长趋势</span>
            </div>
          </template>
          <div ref="trendChartRef" style="height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快捷操作 -->
    <el-card class="quick-actions-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <el-icon><Operation /></el-icon>
          <span>快捷操作</span>
        </div>
      </template>
      <div class="quick-actions">
        <div class="action-item" @click="goToPage('/admin/animals')">
          <div class="action-icon" style="background: linear-gradient(135deg, #E8684A 0%, #D64A2A 100%);">
            <el-icon :size="24"><Grid /></el-icon>
          </div>
          <div class="action-text">动物管理</div>
        </div>
        <div class="action-item" @click="goToPage('/admin/categories')">
          <div class="action-icon" style="background: linear-gradient(135deg, #945FB9 0%, #7A4A9B 100%);">
            <el-icon :size="24"><FolderOpened /></el-icon>
          </div>
          <div class="action-text">分类管理</div>
        </div>
        <div class="action-item" @click="goToPage('/admin/articles')">
          <div class="action-icon" style="background: linear-gradient(135deg, #5AD8A6 0%, #2FB888 100%);">
            <el-icon :size="24"><Document /></el-icon>
          </div>
          <div class="action-text">文章管理</div>
        </div>
        <div class="action-item" @click="goToPage('/admin/comments')">
          <div class="action-icon" style="background: linear-gradient(135deg, #6DC8EC 0%, #4AA8D8 100%);">
            <el-icon :size="24"><ChatDotRound /></el-icon>
          </div>
          <div class="action-text">评论管理</div>
        </div>
        <div class="action-item" @click="goToPage('/admin/questions')">
          <div class="action-icon" style="background: linear-gradient(135deg, #FF9845 0%, #E87A30 100%);">
            <el-icon :size="24"><QuestionFilled /></el-icon>
          </div>
          <div class="action-text">问答管理</div>
        </div>
        <div class="action-item" @click="goToPage('/admin/users')">
          <div class="action-icon" style="background: linear-gradient(135deg, #5B8FF9 0%, #3D76DD 100%);">
            <el-icon :size="24"><User /></el-icon>
          </div>
          <div class="action-text">用户管理</div>
        </div>
        <div class="action-item" @click="goToPage('/admin/banners')">
          <div class="action-icon" style="background: linear-gradient(135deg, #F6BD16 0%, #E8A003 100%);">
            <el-icon :size="24"><Picture /></el-icon>
          </div>
          <div class="action-text">轮播图管理</div>
        </div>
        <div class="action-item" @click="goToPage('/admin/ai-knowledge')">
          <div class="action-icon" style="background: linear-gradient(135deg, #9B5DE5 0%, #7B3FD5 100%);">
            <el-icon :size="24"><MagicStick /></el-icon>
          </div>
          <div class="action-text">AI知识库</div>
        </div>
        <div class="action-item" @click="goToPage('/admin/stats')">
          <div class="action-icon" style="background: linear-gradient(135deg, #1E9493 0%, #16787A 100%);">
            <el-icon :size="24"><TrendCharts /></el-icon>
          </div>
          <div class="action-text">数据统计</div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { getDashboardStats, getCategoryStats, getTrendStats } from '@/api/admin'
import { ElMessage } from 'element-plus'
import { 
  Document, Picture, User, PieChart, TrendCharts, Operation, Grid, View,
  FolderOpened, ChatDotRound, QuestionFilled
} from '@element-plus/icons-vue'
import * as echarts from 'echarts'

const router = useRouter()

const stats = ref({
  userCount: 0,
  animalCount: 0,
  articleCount: 0,
  todayVisit: 0
})

const categoryChartRef = ref()
const trendChartRef = ref()
let categoryChart = null
let trendChart = null

// 获取统计数据
const fetchStats = async () => {
  try {
    const res = await getDashboardStats()
    stats.value = res.data
  } catch (error) {
    ElMessage.error('获取统计数据失败')
  }
}

// 获取分类统计数据并初始化图表
const fetchCategoryStats = async () => {
  try {
    const res = await getCategoryStats()
    await nextTick()
    initCategoryChart(res.data)
  } catch (error) {
    ElMessage.error('获取分类统计数据失败')
  }
}

// 获取趋势统计数据并初始化图表
const fetchTrendStats = async () => {
  try {
    const res = await getTrendStats()
    await nextTick()
    initTrendChart(res.data)
  } catch (error) {
    ElMessage.error('获取趋势统计数据失败')
  }
}

// 初始化分类饼图 - 使用鲜艳配色
const initCategoryChart = (data) => {
  if (!categoryChartRef.value) return
  
  categoryChart = echarts.init(categoryChartRef.value)
  
  // 鲜艳的配色方案
  const colors = [
    '#5B8FF9',  // 蓝色
    '#E8684A',  // 橙红色
    '#5AD8A6',  // 青绿色
    '#F6BD16',  // 金黄色
    '#6DC8EC',  // 天蓝色
    '#945FB9',  // 紫色
    '#FF9845',  // 橙色
    '#1E9493',  // 青色
    '#FF99C3',  // 粉色
    '#5D7092'   // 灰蓝色
  ]
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      right: '5%',
      top: 'center',
      textStyle: {
        fontSize: 13,
        color: '#333'
      },
      itemWidth: 12,
      itemHeight: 12
    },
    series: [
      {
        name: '动物分类',
        type: 'pie',
        radius: ['35%', '60%'],
        center: ['40%', '50%'],
        avoidLabelOverlap: true,
        itemStyle: {
          borderRadius: 8,
          borderColor: '#fff',
          borderWidth: 3
        },
        label: {
          show: true,
          formatter: '{b}\n{d}%',
          fontSize: 12,
          color: '#333',
          fontWeight: 'bold'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 14,
            fontWeight: 'bold'
          },
          itemStyle: {
            shadowBlur: 20,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.3)'
          },
          scale: true,
          scaleSize: 8
        },
        data: data.map((item, index) => ({
          value: item.value,
          name: item.name,
          itemStyle: {
            color: colors[index % colors.length]
          }
        }))
      }
    ]
  }
  
  categoryChart.setOption(option)
}

// 初始化趋势柱状图 - 使用鲜艳配色
const initTrendChart = (data) => {
  if (!trendChartRef.value) return
  
  trendChart = echarts.init(trendChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['用户数', '动物数', '文章数'],
      top: '5%',
      textStyle: {
        fontSize: 14,
        color: '#333'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: data.months,
      axisLabel: {
        fontSize: 12,
        color: '#666'
      },
      axisLine: {
        lineStyle: {
          color: '#ddd'
        }
      }
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        fontSize: 12,
        color: '#666'
      },
      splitLine: {
        lineStyle: {
          color: '#f0f0f0'
        }
      }
    },
    series: [
      {
        name: '用户数',
        type: 'bar',
        barWidth: '20%',
        data: data.userCounts,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#5B8FF9' },
            { offset: 1, color: '#3D76DD' }
          ]),
          borderRadius: [6, 6, 0, 0]
        },
        emphasis: {
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#3D76DD' },
              { offset: 1, color: '#2A5FBB' }
            ])
          }
        }
      },
      {
        name: '动物数',
        type: 'bar',
        barWidth: '20%',
        data: data.animalCounts,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#E8684A' },
            { offset: 1, color: '#D64A2A' }
          ]),
          borderRadius: [6, 6, 0, 0]
        },
        emphasis: {
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#D64A2A' },
              { offset: 1, color: '#BD3A1A' }
            ])
          }
        }
      },
      {
        name: '文章数',
        type: 'bar',
        barWidth: '20%',
        data: data.articleCounts,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#5AD8A6' },
            { offset: 1, color: '#2FB888' }
          ]),
          borderRadius: [6, 6, 0, 0]
        },
        emphasis: {
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#2FB888' },
              { offset: 1, color: '#1F9A6C' }
            ])
          }
        }
      }
    ]
  }
  
  trendChart.setOption(option)
}

// 窗口大小改变时重新调整图表
const handleResize = () => {
  categoryChart?.resize()
  trendChart?.resize()
}

const goToPage = (path) => {
  router.push(path)
}

onMounted(() => {
  fetchStats()
  fetchCategoryStats()
  fetchTrendStats()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  categoryChart?.dispose()
  trendChart?.dispose()
})
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.stats-row {
  margin-bottom: 20px;
}

.stats-card {
  transition: transform 0.3s, box-shadow 0.3s;
}

.stats-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
}

.stats-card :deep(.el-card__body) {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 20px;
}

.stats-icon {
  width: 64px;
  height: 64px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.stats-info {
  flex: 1;
  min-width: 0;
}

.stats-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stats-label {
  font-size: 14px;
  color: #909399;
}

.chart-row {
  margin-bottom: 20px;
}

.chart-card {
  height: 500px;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.quick-actions-card {
  margin-bottom: 20px;
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 30px 20px;
  border-radius: 12px;
  background: #f9fafb;
  cursor: pointer;
  transition: all 0.3s;
}

.action-item:hover {
  background: #fff;
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
}

.action-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.action-text {
  font-size: 15px;
  font-weight: 500;
  color: #303133;
}

@media (max-width: 768px) {
  .stats-value {
    font-size: 24px;
  }
  
  .stats-icon {
    width: 48px;
    height: 48px;
  }
  
  .stats-icon .el-icon {
    font-size: 24px;
  }
  
  .quick-actions {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
