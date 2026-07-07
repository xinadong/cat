<template>
  <svg aria-hidden="true" class="svg-icon" :style="{ width: size, height: size }">
    <use :xlink:href="symbolId" />
  </svg>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  prefix: { type: String, default: 'icon' }, // 对应 vite.config.js 里的 symbolId 前缀
  name: { type: String, required: true },    // 文件名（不含 .svg）
  color: { type: String, default: '' },      // 图标颜色
  size: { type: String, default: '1em' }     // 图标大小
})

const symbolId = computed(() => `#${props.prefix}-${props.name}`)
</script>

<style scoped>
.svg-icon {
  display: inline-block;
  vertical-align: -0.15em;
  fill: currentColor;      /* 默认跟随文字颜色 */
  overflow: hidden;
}
/* 如果传了 color 属性，则覆盖默认颜色 */
.svg-icon[style*="fill"] {
    fill: v-bind(color);
}
</style>