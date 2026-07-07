<template>
  <div class="page-container">
    <el-card shadow="never">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="comment.id" label="ID" width="80" />
        <el-table-column label="用户" width="150">
          <template #default="{ row }">
            <div class="user-cell">
              <el-avatar :src="row.user.avatar" :size="32" />
              <span>{{ row.user.nickname || row.user.username }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="动物" width="150">
          <template #default="{ row }">
            {{ row.animal?.name || '已删除' }}
          </template>
        </el-table-column>
        <el-table-column prop="comment.content" label="评论内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="comment.likeCount" label="点赞数" width="100" />
        <el-table-column prop="comment.status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.comment.status === 0" type="warning" size="small">待审核</el-tag>
            <el-tag v-else-if="row.comment.status === 1" type="success" size="small">已通过</el-tag>
            <el-tag v-else type="danger" size="small">已拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="comment.createTime" label="评论时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.comment.status === 0" type="success" link @click="handleAudit(row.comment.id, 1)">通过</el-button>
            <el-button v-if="row.comment.status === 0" type="warning" link @click="handleAudit(row.comment.id, 2)">拒绝</el-button>
            <el-button type="danger" link :icon="Delete" @click="handleDelete(row.comment.id)">删除</el-button>
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
import { getAdminCommentList, deleteAdminComment, auditComment } from '@/api/admin'

const loading = ref(false)
const tableData = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const fetchList = async () => {
  loading.value = true
  try {
    const res = await getAdminCommentList({
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

const handleAudit = async (id, status) => {
  try {
    await auditComment(id, status)
    ElMessage.success('审核成功')
    fetchList()
  } catch (error) {
    ElMessage.error('审核失败')
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确认删除该评论？', '提示', { type: 'warning' })
    await deleteAdminComment(id)
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

