import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'
export default defineConfig({
  plugins: [vue(),createSvgIconsPlugin({
      // 指定需要缓存的图标文件夹（建议你在 src/assets 下新建一个 icons 文件夹）
      iconDirs: [resolve(process.cwd(), 'src/assets/icons')],
      // 指定 symbolId 格式，这里设置为 'icon-[name]'
      symbolId: 'icon-[name]',
    }),],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  server: {
    port: 3000,
    proxy: {
      '/api': {
        // 将 localhost 改为 127.0.0.1 强制使用 IPv4 解析
        target: 'http://127.0.0.1:8080',
        changeOrigin: true
      },
      '/uploads': {
        // 同样将上传目录的代理也修改为 127.0.0.1
        target: 'http://127.0.0.1:8080',
        changeOrigin: true

      }
    }
  }

})