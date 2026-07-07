<template>
  <div class="page-container">
    <el-card shadow="never" class="toolbar-card">
      <el-button type="success" :icon="Plus" @click="handleAdd">新增分类</el-button>
    </el-card>

    <el-card shadow="never">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="图标" width="100">
          <template #default="{ row }">
            <el-image 
              v-if="row.icon" 
              :src="row.icon" 
              style="width: 50px; height: 50px; border-radius: 8px; padding: 5px; background: #f5f7fa;"
              fit="contain"
            />
            <span v-else style="color: #999;">无图标</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="分类名称" width="150" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="sort" label="排序" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link :icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link :icon="Delete" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 编辑对话框 -->
    <el-dialog
      :model-value="dialogVisible"
      @update:model-value="dialogVisible = $event"
      :title="dialogTitle"
      width="600px"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="分类图标">
          <div class="icon-upload-box">
            <el-upload
              class="icon-uploader"
              :action="uploadUrl"
              :headers="uploadHeaders"
              :show-file-list="false"
              :on-success="handleIconSuccess"
              :before-upload="beforeIconUpload"
              accept="image/*"
            >
              <img v-if="form.icon" :src="form.icon" class="icon-preview" />
              <el-icon v-else class="icon-uploader-icon"><Plus /></el-icon>
            </el-upload>
            <div class="icon-tips">
              <p>建议上传正方形图标，尺寸 100x100 像素</p>
              <p>可以从 <a href="https://www.iconfont.cn/" target="_blank">iconfont</a> 或 
              <a href="https://www.flaticon.com/" target="_blank">flaticon</a> 下载图标</p>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import { getCategoryList, addCategory, updateCategory, deleteCategory } from '@/api/admin'
import { useAdminStore } from '@/stores/admin'

const adminStore = useAdminStore()
const uploadUrl = '/api/admin/category/upload-icon'
const uploadHeaders = {
  Authorization: 'Bearer ' + adminStore.token
}

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitting = ref(false)
const formRef = ref()

const form = reactive({
  id: null,
  name: '',
  description: '',
  icon: '',
  sort: 0
})

const rules = {
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }]
}

const handleIconSuccess = (response) => {
  if (response.code === 200) {
    form.icon = response.data
    ElMessage.success('图标上传成功')
  } else {
    ElMessage.error('图标上传失败')
  }
}

const beforeIconUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

const fetchList = async () => {
  loading.value = true
  try {
    const res = await getCategoryList()
    tableData.value = res.data
  } catch (error) {
    ElMessage.error('获取列表失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  resetForm()
  dialogTitle.value = '新增分类'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogTitle.value = '编辑分类'
  dialogVisible.value = true
}

const handleSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        if (form.id) {
          await updateCategory(form)
          ElMessage.success('更新成功')
        } else {
          await addCategory(form)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        fetchList()
      } finally {
        submitting.value = false
      }
    }
  })
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确认删除该分类？', '提示', { type: 'warning' })
    await deleteCategory(id)
    ElMessage.success('删除成功')
    fetchList()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('删除失败')
  }
}

const resetForm = () => {
  Object.assign(form, { id: null, name: '', description: '', icon: '', sort: 0 })
  formRef.value?.resetFields()
}

onMounted(() => {
  fetchList()
})
</script>

<style scoped>
.page-container {
  padding: 20px;
}
.toolbar-card {
  margin-bottom: 20px;
}

.icon-upload-box {
  display: flex;
  gap: 20px;
  align-items: flex-start;
}

.icon-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s;
}

.icon-uploader:hover {
  border-color: #409eff;
}

.icon-uploader :deep(.el-upload) {
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}

.icon-preview {
  width: 100px;
  height: 100px;
  object-fit: contain;
  display: block;
  padding: 10px;
  background: #f5f7fa;
}

.icon-tips {
  flex: 1;
  color: #909399;
  font-size: 13px;
  line-height: 1.8;
}

.icon-tips p {
  margin: 5px 0;
}

.icon-tips a {
  color: #409eff;
  text-decoration: none;
}

.icon-tips a:hover {
  text-decoration: underline;
}
</style>

