<template>
  <div class="page-container">
    <el-card shadow="never">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="question.id" label="ID" width="80" />
        <el-table-column label="用户" width="150">
          <template #default="{ row }">
            <div class="user-cell">
              <el-avatar :src="row.user.avatar" :size="32" />
              <span>{{ row.user.nickname || row.user.username }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="question.title" label="问题标题" min-width="250" show-overflow-tooltip />
        <el-table-column prop="question.answerCount" label="回答数" width="100" />
        <el-table-column prop="question.isTop" label="置顶" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.question.isTop" type="danger" size="small">置顶</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="question.createTime" label="提问时间" width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button 
              :type="row.question.isTop ? 'warning' : 'success'" 
              link 
              @click="handleToggleTop(row.question.id, row.question.isTop)"
            >
              {{ row.question.isTop ? '取消置顶' : '置顶' }}
            </el-button>
            <el-button type="danger" link :icon="Delete" @click="handleDelete(row.question.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          :current-page="pageNum"
          :page-size="pageSize"
          :total="total"
          @current-change="(val) => { pageNum = val; fetchList(); }"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete } from '@element-plus/icons-vue'
import { getAdminQuestionList, deleteAdminQuestion, topQuestion } from '@/api/admin'

const loading = ref(false)
const tableData = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const fetchList = async () => {
  loading.value = true
  try {
    const res = await getAdminQuestionList({
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    tableData.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    ElMessage.error('获取列表失败')
  } finally {
    loading.value = false
  }
}

const handleToggleTop = async (id, currentIsTop) => {
  try {
    await topQuestion(id, currentIsTop ? 0 : 1)
    ElMessage.success(currentIsTop ? '已取消置顶' : '置顶成功')
    fetchList()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确认删除该问题？', '提示', { type: 'warning' })
    await deleteAdminQuestion(id)
    ElMessage.success('删除成功')
    fetchList()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('删除失败')
  }
}

onMounted(() => {
  fetchList()
})
</script>

<style scoped>
.page-container {
  padding: 20px;
}
.user-cell {
  display: flex;
  align-items: center;
  gap: 10px;
}
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>

