import { fileURLToPath, URL } from 'node:url'
import path from 'node:path'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

const rootDir = fileURLToPath(new URL('.', import.meta.url))

export default defineConfig({
  base: './',
  plugins: [vue()],
  resolve: {
    extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue'],
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    host: '0.0.0.0',
    port: 8081,
    proxy: {
      '/zuqiujulebguanli': {
        target: 'http://localhost:8080/zuqiujulebguanli/',
        changeOrigin: true,
        secure: false,
        rewrite: value => value.replace(/^\/zuqiujulebguanli/, '')
      }
    }
  },
  build: {
    outDir: path.resolve(rootDir, '../../admin/dist'),
    emptyOutDir: true,
    sourcemap: true
  }
})
