<template>
  <div class="page-container">
    <el-card shadow="never" class="search-card">
      <el-form inline>
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="搜索动物名称" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" :icon="Plus" @click="handleAdd">新增动物</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column label="封面" width="100" align="center">
          <template #default="{ row }">
            <el-image 
              :src="row.coverImage" 
              fit="cover" 
              style="width: 60px; height: 60px; border-radius: 4px; cursor: pointer;" 
              @click="handlePreviewImage(row.coverImage)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="动物名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="scientificName" label="学名" min-width="180" show-overflow-tooltip />
        <el-table-column prop="protectionLevel" label="保护等级" min-width="150" show-overflow-tooltip>
          <template #default="{ row }">
            <el-tag v-if="row.protectionLevel" type="warning" size="small">{{ row.protectionLevel }}</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览量" width="100" align="center" />
        <el-table-column prop="collectCount" label="收藏量" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '上架' : '下架' }}
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

      <div class="pagination">
        <el-pagination
          :current-page="pageNum"
          :page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="(val) => { pageNum = val; fetchList(); }"
          @size-change="(val) => { pageSize = val; pageNum = 1; fetchList(); }"
        />
      </div>
    </el-card>

    <!-- 编辑对话框 -->
    <el-dialog
      :model-value="dialogVisible"
      @update:model-value="dialogVisible = $event"
      :title="dialogTitle"
      width="800px"
      top="3vh"
      :close-on-click-modal="false"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="动物名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入动物名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学名" prop="scientificName">
              <el-input v-model="form.scientificName" placeholder="请输入学名" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分类" prop="categoryId">
              <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%">
                <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="保护等级">
              <el-input v-model="form.protectionLevel" placeholder="如：国家一级保护动物" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="分布地区">
          <el-input v-model="form.distribution" placeholder="请输入分布地区" />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="封面图" prop="coverImage">
              <el-upload
                class="avatar-uploader"
                :show-file-list="false"
                :http-request="handleUpload"
                :before-upload="beforeUpload"
              >
                <img v-if="form.coverImage" :src="form.coverImage" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
              </el-upload>
              <div class="upload-tip">建议尺寸：600x400px</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="子图片集">
              <div class="sub-images-container">
                <div class="sub-image-list">
                  <div v-for="(img, index) in subImagesList" :key="index" class="sub-image-item">
                    <el-image :src="img" fit="cover" class="sub-image" />
                    <div class="sub-image-mask">
                      <el-icon class="sub-image-delete" @click="handleDeleteSubImage(index)">
                        <Delete />
                      </el-icon>
                    </div>
                  </div>
                  <el-upload
                    v-if="subImagesList.length < 6"
                    class="sub-image-uploader"
                    :show-file-list="false"
                    :http-request="handleSubImageUpload"
                    :before-upload="beforeUpload"
                  >
                    <div class="sub-upload-btn">
                      <el-icon :size="20"><Plus /></el-icon>
                    </div>
                  </el-upload>
                </div>
                <div class="upload-tip">最多上传6张，用于详情页轮播</div>
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="外形特征">
          <el-input v-model="form.appearance" type="textarea" :rows="3" placeholder="描述动物的外形特征" />
        </el-form-item>

        <el-form-item label="生活习性">
          <el-input v-model="form.habit" type="textarea" :rows="3" placeholder="描述动物的生活习性" />
        </el-form-item>

        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">上架</el-radio>
            <el-radio :label="0">下架</el-radio>
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
      width="800px"
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
import { Search, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { 
  getAdminAnimalList, 
  addAnimal, 
  updateAnimal, 
  deleteAnimal,
  uploadAnimalImage
} from '@/api/admin'
import { getCategories } from '@/api/home'

const loading = ref(false)
const tableData = ref([])
const categories = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchForm = reactive({
  keyword: ''
})

const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitting = ref(false)
const formRef = ref()

// 图片预览
const previewDialogVisible = ref(false)
const previewImageUrl = ref('')

// 子图片列表
const subImagesList = ref([])

const form = reactive({
  id: null,
  name: '',
  scientificName: '',
  categoryId: null,
  coverImage: '',
  images: '',
  appearance: '',
  habit: '',
  distribution: '',
  protectionLevel: '',
  status: 1
})

const rules = {
  name: [{ required: true, message: '请输入动物名称', trigger: 'blur' }],
  scientificName: [{ required: true, message: '请输入学名', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  coverImage: [{ required: true, message: '请上传封面图', trigger: 'change' }]
}

const fetchList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value
    }
    if (searchForm.keyword) {
      params.keyword = searchForm.keyword
    }
    const res = await getAdminAnimalList(params)
    tableData.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    ElMessage.error('获取列表失败')
  } finally {
    loading.value = false
  }
}

const fetchCategories = async () => {
  try {
    const res = await getCategories()
    categories.value = res.data
  } catch (error) {
    console.error('获取分类失败', error)
  }
}

const handleSearch = () => {
  pageNum.value = 1
  fetchList()
}

const handleReset = () => {
  searchForm.keyword = ''
  handleSearch()
}

const handleAdd = () => {
  resetForm()
  dialogTitle.value = '新增动物'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  // 解析子图片JSON
  if (row.images) {
    try {
      subImagesList.value = JSON.parse(row.images)
    } catch (e) {
      subImagesList.value = []
    }
  } else {
    subImagesList.value = []
  }
  dialogTitle.value = '编辑动物'
  dialogVisible.value = true
}

const handleSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        // 将子图片列表转换为JSON字符串
        form.images = JSON.stringify(subImagesList.value)
        
        if (form.id) {
          await updateAnimal(form)
          ElMessage.success('更新成功')
        } else {
          await addAnimal(form)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        fetchList()
      } catch (error) {
        ElMessage.error('操作失败')
      } finally {
        submitting.value = false
      }
    }
  })
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确认删除该动物？', '提示', {
      type: 'warning'
    })
    await deleteAnimal(id)
    ElMessage.success('删除成功')
    fetchList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleUpload = async ({ file }) => {
  try {
    const res = await uploadAnimalImage(file)
    form.coverImage = res.data.url
    ElMessage.success('上传成功')
  } catch (error) {
    ElMessage.error('上传失败')
  }
}

// 上传子图片
const handleSubImageUpload = async ({ file }) => {
  try {
    const res = await uploadAnimalImage(file)
    subImagesList.value.push(res.data.url)
    ElMessage.success('子图片上传成功')
  } catch (error) {
    ElMessage.error('子图片上传失败')
  }
}

// 删除子图片
const handleDeleteSubImage = (index) => {
  ElMessageBox.confirm('确认删除该子图片？', '提示', {
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(() => {
    subImagesList.value.splice(index, 1)
    ElMessage.success('删除成功')
  }).catch(() => {})
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
    name: '',
    scientificName: '',
    categoryId: null,
    coverImage: '',
    images: '',
    appearance: '',
    habit: '',
    distribution: '',
    protectionLevel: '',
    status: 1
  })
  subImagesList.value = []
  formRef.value?.resetFields()
}

// 预览图片
const handlePreviewImage = (imageUrl) => {
  previewImageUrl.value = imageUrl
  previewDialogVisible.value = true
}

onMounted(() => {
  fetchList()
  fetchCategories()
})
</script>

<style scoped>
.page-container {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  overflow: hidden;
  transition: all 0.3s;
}

.avatar-uploader:hover {
  border-color: #67c23a;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
  object-fit: cover;
}

.upload-tip {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}

/* 子图片样式 */
.sub-images-container {
  width: 100%;
}

.sub-image-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.sub-image-item {
  position: relative;
  width: 80px;
  height: 80px;
  border-radius: 4px;
  overflow: hidden;
  border: 1px solid #dcdfe6;
}

.sub-image {
  width: 100%;
  height: 100%;
}

.sub-image-mask {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.sub-image-item:hover .sub-image-mask {
  opacity: 1;
}

.sub-image-delete {
  font-size: 20px;
  color: #fff;
  cursor: pointer;
}

.sub-image-delete:hover {
  color: #f56c6c;
}

.sub-image-uploader {
  display: inline-block;
}

.sub-upload-btn {
  width: 80px;
  height: 80px;
  border: 1px dashed #d9d9d9;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: border-color 0.3s;
  background: #fafafa;
}

.sub-upload-btn:hover {
  border-color: #67c23a;
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

