<template>
  <div class="popular-cats-page">
    <div class="container">
      <div class="page-header">
        <h1>人气喵王</h1>
        <p class="subtitle">pick你的心上咪 / 上传Ta的专属表情包</p>
      </div>

      <div class="cat-list" v-loading="loading">
        <el-card
          v-for="(animal, index) in rankedAnimals"
          :key="animal.id"
          class="cat-card hover-card"
          shadow="hover"
        >
          <div class="cat-header">
            <div class="header-left">
              <span :class="['rank-badge', `rank-${index + 1}`]">
                No.{{ index + 1 }}
              </span>
              <el-avatar :src="getImageUrl(animal.coverImage)" :size="56" class="cat-avatar" />
              <span class="cat-name">{{ animal.name }}</span>
            </div>

            <div class="header-right">
              <el-button class="vote-btn" round @click="handleVote(animal)">
                🐾 {{ animal.vote_count || animal.voteCount || 0 }} 票
              </el-button>
              <el-button type="primary" class="upload-btn" round @click="openUploadDialog(animal)">
                上传
              </el-button>
            </div>
          </div>

          <div class="emoji-section">
            <el-button class="scroll-btn left-btn" circle @click="scrollTrack(animal.id, -300)">
              <el-icon><ArrowLeft /></el-icon>
            </el-button>

            <div class="emoji-track" :id="'track-' + animal.id">
              <div
                v-for="emoji in emojiList.filter(e => e.animal_id == animal.id || e.animalId == animal.id)"
                :key="emoji.id"
                class="emoji-item"
              >
                <el-image
                  :src="getImageUrl(emoji.image_url || emoji.imageUrl)"
                  :preview-src-list="[getImageUrl(emoji.image_url || emoji.imageUrl)]"
                  fit="cover"
                  class="emoji-img"
                />
              </div>
              <div v-if="emojiList.filter(e => e.animal_id == animal.id || e.animalId == animal.id).length === 0" class="empty-emoji">
                <span style="color: #909399; font-size: 14px;">暂无表情包，快来上传吧！</span>
              </div>
            </div>

            <el-button class="scroll-btn right-btn" circle @click="scrollTrack(animal.id, 300)">
              <el-icon><ArrowRight /></el-icon>
            </el-button>
          </div>
        </el-card>

        <el-empty v-if="rankedAnimals.length === 0" description="暂无喵星人数据" />
      </div>
    </div>

    <el-dialog
      v-model="uploadDialogVisible"
      :title="`为 ${currentCat?.name} 上传表情包`"
      width="400px"
      center
      :close-on-click-modal="false"
    >
      <el-form :model="uploadForm" ref="formRef">
        <el-form-item>
          <el-upload
            class="emoji-uploader"
            action="#"
            :show-file-list="false"
            :auto-upload="false"
            :on-change="handleFileChange"
            accept="image/*"
          >
            <img v-if="uploadPreview" :src="uploadPreview" class="preview-img" />
            <div v-else class="uploader-placeholder">
              <el-icon :size="30"><Plus /></el-icon>
              <span style="margin-top: 10px;">点击选择图片</span>
            </div>
          </el-upload>
        </el-form-item>

        <el-form-item>
          <el-input v-model="uploadForm.title" placeholder="给表情包起个名字 (可选)" />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer-custom">
          <!-- 确认上传按钮 -->
          <el-button
            type="primary"
            @click="submitUpload"
            :loading="uploading"
            class="confirm-btn"
          >
            确认上传
          </el-button>

          <!-- 取消按钮，放在下面并增加顶部间距 -->
          <el-button
            @click="uploadDialogVisible = false"
            class="cancel-btn"
          >
            取消
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { ArrowLeft, ArrowRight, Plus } from '@element-plus/icons-vue'

// 我们只保留 animal 的接口，表情包全部使用绝对直连！
import { getAnimalList } from '@/api/animal'
import { addCollect } from '@/api/collect'

const loading = ref(false)
const rankedAnimals = ref([])
const emojiList = ref([])

// 万能路径补全
const getImageUrl = (url) => {
  if (!url) return '';
  if (url.startsWith('http')) return url;
  const cleanUrl = url.startsWith('/') ? url.slice(1) : url;
  return `http://127.0.0.1:8088/${cleanUrl}`;
}

// 🔥 绝杀动作 1：绕过所有代理，原生 fetch 强制获取列表
const fetchEmojis = async () => {
  try {
    const res = await fetch('http://127.0.0.1:8088/api/cat-emoji/list?page=1&size=100');
    const data = await res.json();
    if (data.code === 200) {
      emojiList.value = data.data;
      console.log("🔥 表情包数据已强行拉取成功：", emojiList.value);
    }
  } catch (error) {
    console.error('获取表情包彻底失败，请确认后端 8088 是否启动:', error);
  }
}

const fetchData = async () => {
  loading.value = true
  try {
    const animalRes = await getAnimalList({ pageNum: 1, pageSize: 50 })
    if (animalRes.code === 200) {
      rankedAnimals.value = animalRes.data.records.sort((a, b) => (b.vote_count || b.voteCount || 0) - (a.vote_count || a.voteCount || 0))
    }
    await fetchEmojis() // 获取完猫咪后，立刻强制获取表情包
  } catch (error) {
    console.error('获取数据失败:', error)
  } finally {
    loading.value = false
  }
}

const handleVote = async (animal) => {
  try {
    // 1. 调用刚刚在后端写好的专属投票接口
    const res = await fetch(`http://127.0.0.1:8088/api/animal/vote/${animal.id}`, {
      method: 'POST'
    });
    const data = await res.json();

    if (data.code === 200) {
      // 2. 接口成功后，前端的票数直接 +1，实现秒级响应
      if (animal.vote_count !== undefined) {
        animal.vote_count += 1;
      } else if (animal.voteCount !== undefined) {
        animal.voteCount += 1;
      } else {
        animal.vote_count = 1;
      }
      ElMessage.success(`成功为 ${animal.name} 投出一票！`);
    } else {
      ElMessage.error(data.message || '投票失败');
    }
  } catch (error) {
    console.error('投票出错:', error);
    ElMessage.error('网络出错，投票失败');
  }
}

const scrollTrack = (animalId, offset) => {
  const track = document.getElementById('track-' + animalId)
  if (track) {
    track.scrollBy({ left: offset, behavior: 'smooth' })
  }
}

const uploadDialogVisible = ref(false)
const uploading = ref(false)
const uploadPreview = ref('')
const selectedFile = ref(null)
const currentCat = ref(null)

const uploadForm = ref({
  title: ''
})

const openUploadDialog = (animal) => {
  currentCat.value = animal
  uploadForm.value.title = ''
  selectedFile.value = null
  uploadPreview.value = ''
  uploadDialogVisible.value = true
}

const handleFileChange = (file) => {
  selectedFile.value = file.raw
  uploadPreview.value = URL.createObjectURL(file.raw)
}

// 🔥 绝杀动作 2：绕过所有代理，原生 fetch 强制上传
const submitUpload = async () => {
  if (!selectedFile.value) {
    ElMessage.warning('请先选择一张表情包图片')
    return
  }

  uploading.value = true
  const formData = new FormData()
  formData.append('file', selectedFile.value)
  if (uploadForm.value.title) {
    formData.append('title', uploadForm.value.title)
  }
  formData.append('animalId', Number(currentCat.value.id))

  try {
    const res = await fetch('http://127.0.0.1:8088/api/cat-emoji/upload', {
      method: 'POST',
      body: formData
    });
    const data = await res.json();

    if (data.code === 200) {
      ElMessage.success('表情包上传成功！')
      uploadDialogVisible.value = false
      await fetchEmojis() // 重新拉取
    } else {
      ElMessage.error(data.message || '上传失败')
    }
  } catch (error) {
    console.error('上传失败:', error)
  } finally {
    uploading.value = false
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
/* 保持原有的完美样式 */
.popular-cats-page { padding: 40px 0; min-height: calc(100vh - 300px); background: transparent; }
.container { max-width: 900px; margin: 0 auto; padding: 0 20px; }
.page-header { text-align: center; margin-bottom: 40px; }
.page-header h1 { font-size: 36px; color: #303133; margin-bottom: 10px; letter-spacing: 2px; font-family: 'STKaiti', '华文楷体', serif; }
.subtitle { font-size: 16px; color: #909399; letter-spacing: 1px; }
.cat-list { display: flex; flex-direction: column; gap: 25px; }
.cat-card { border-radius: 16px; border: 1px solid #ebeef5; background: rgba(255, 255, 255, 0.95); backdrop-filter: blur(10px); }
.cat-header { display: flex; justify-content: space-between; align-items: center; padding-bottom: 20px; border-bottom: 1px solid #f0f2f5; margin-bottom: 20px; }
.header-left { display: flex; align-items: center; gap: 15px; }
.rank-badge { font-size: 22px; font-weight: 900; font-style: italic; min-width: 60px; }
.rank-1 { color: #FFD700; font-size: 28px; text-shadow: 1px 1px 2px rgba(0,0,0,0.1); }
.rank-2 { color: #C0C0C0; font-size: 26px; }
.rank-3 { color: #CD7F32; font-size: 24px; }
.rank-badge:not(.rank-1):not(.rank-2):not(.rank-3) { color: #909399; }
.cat-avatar { border: 2px solid #fff; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
.cat-name { font-size: 20px; font-weight: bold; color: #303133; }
.header-right { display: flex; gap: 12px; }
.vote-btn { border-color: #ff9800; color: #ff9800; font-weight: bold; transition: all 0.3s; }
.vote-btn:hover { background-color: #ff9800; color: #fff; transform: scale(1.05); }
.upload-btn { background-color: #333; border-color: #333; color: #fff; }
.upload-btn:hover { background-color: #555; border-color: #555; }
.emoji-section { position: relative; display: flex; align-items: center; }
.emoji-track { display: flex; gap: 15px; overflow-x: auto; scroll-behavior: smooth; padding: 5px 40px; width: 100%; -ms-overflow-style: none; scrollbar-width: none; }
.emoji-track::-webkit-scrollbar { display: none; }
.emoji-item { flex: 0 0 auto; width: 140px; height: 140px; border-radius: 8px; border: 1px solid #ebeef5; overflow: hidden; box-shadow: 0 2px 6px rgba(0,0,0,0.05); transition: transform 0.2s; }
.emoji-item:hover { transform: translateY(-3px); }
.emoji-img { width: 100%; height: 100%; display: block; }
.empty-emoji { width: 100%; text-align: center; padding: 40px 0; }
.scroll-btn { position: absolute; z-index: 2; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
.left-btn { left: -10px; }
.right-btn { right: -10px; }
.emoji-uploader :deep(.el-upload) { border: 2px dashed #dcdfe6; border-radius: 12px; cursor: pointer; position: relative; overflow: hidden; width: 100%; height: 200px; transition: border-color 0.3s; }
.emoji-uploader :deep(.el-upload:hover) { border-color: #409eff; }
.uploader-placeholder { display: flex; flex-direction: column; justify-content: center; align-items: center; height: 100%; color: #8c939d; }
.preview-img { width: 100%; height: 100%; object-fit: contain; }
.dialog-footer-custom {
  display: flex;
  flex-direction: column; /* 垂直排列 */
  gap: 15px;              /* 按钮之间的间距 */
  width: 100%;
}

.confirm-btn {
  width: 100%;
  margin: 0 !important;   /* 清除默认边距以确保对齐 */
  background: linear-gradient(135deg, #ff7700, #ffdd00);
  border: none;
  color: #333;
  font-weight: bold;
}

.cancel-btn {
  width: 100%;
  margin: 0 !important;   /* 确保在垂直布局中撑满 */
}

</style>