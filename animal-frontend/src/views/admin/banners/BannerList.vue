<template>
  <div class="page-container">
    <el-card shadow="never" class="toolbar-card">
      <el-button type="success" :icon="Plus" @click="handleAdd">新增轮播图</el-button>
    </el-card>

    <el-card shadow="never">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column label="图片" width="150" align="center">
          <template #default="{ row }">
            <el-image 
              :src="row.imageUrl" 
              fit="cover" 
              style="width: 120px; height: 60px; border-radius: 4px; cursor: pointer;" 
              @click="handlePreviewImage(row.imageUrl)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="linkUrl" label="链接地址" min-width="200" show-overflow-tooltip />
        <el-table-column prop="sort" label="排序" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right" align="center">
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
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="链接地址">
          <el-input v-model="form.linkUrl" placeholder="如：/animals" />
        </el-form-item>
        <el-form-item label="图片" prop="imageUrl">
          <el-upload
            class="banner-uploader"
            :show-file-list="false"
            :http-request="handleUpload"
            :before-upload="beforeUpload"
          >
            <img v-if="form.imageUrl" :src="form.imageUrl" class="banner-image" />
            <el-icon v-else class="uploader-icon"><Plus /></el-icon>
          </el-upload>
          <div class="upload-tip">建议尺寸：1920x500px</div>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 图片预览对话框 -->
    <el-dialog
      v-model="previewDialogVisible"
      title="图片预览"
      width="1000px"
      center
    >
      <div class="image-preview-container">
        <el-image 
          :src="previewImageUrl" 
          fit="contain" 
          style="width: 100%; max-height: 70vh;"
        />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import { getBannerList, addBanner, updateBanner, deleteBanner, uploadBannerImage } from '@/api/admin'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitting = ref(false)
const formRef = ref()

// 图片预览
const previewDialogVisible = ref(false)
const previewImageUrl = ref('')

const form = reactive({
  id: null,
  title: '',
  imageUrl: '',
  linkUrl: '',
  sort: 0,
  status: 1
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  imageUrl: [{ required: true, message: '请上传图片', trigger: 'change' }]
}

const fetchList = async () => {
  loading.value = true
  try {
    const res = await getBannerList()
    tableData.value = res.data
  } catch (error) {
    ElMessage.error('获取列表失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  resetForm()
  dialogTitle.value = '新增轮播图'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogTitle.value = '编辑轮播图'
  dialogVisible.value = true
}

const handleSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        if (form.id) {
          await updateBanner(form)
          ElMessage.success('更新成功')
        } else {
          await addBanner(form)
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
    await ElMessageBox.confirm('确认删除该轮播图？', '提示', { type: 'warning' })
    await deleteBanner(id)
    ElMessage.success('删除成功')
    fetchList()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('删除失败')
  }
}

const handleUpload = async ({ file }) => {
  try {
    const res = await uploadBannerImage(file)
    form.imageUrl = res.data.url
    ElMessage.success('上传成功')
  } catch (error) {
    ElMessage.error('上传失败')
  }
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isImage) {
    ElMessage.error('只能上传图片!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

const resetForm = () => {
  Object.assign(form, {
    id: null,
    title: '',
    imageUrl: '',
    linkUrl: '',
    sort: 0,
    status: 1
  })
  formRef.value?.resetFields()
}

// 预览图片
const handlePreviewImage = (imageUrl) => {
  previewImageUrl.value = imageUrl
  previewDialogVisible.value = true
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
.banner-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  overflow: hidden;
  transition: all 0.3s;
}
.banner-uploader:hover {
  border-color: #67c23a;
}
.uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 400px;
  height: 150px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.banner-image {
  width: 400px;
  height: 150px;
  display: block;
  object-fit: cover;
}
.upload-tip {
  font-size: 12px;
  color: #999;
  margin-top: 8px;
}

.image-preview-container {
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f5f7fa;
  padding: 20px;
  border-radius: 8px;
}
</style>

