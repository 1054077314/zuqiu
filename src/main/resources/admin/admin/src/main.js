import { createApp } from 'vue'
import App from '@/App.vue'
import ElementPlus, { ElMessage, ElMessageBox, ElNotification } from 'element-plus'
import 'element-plus/dist/index.css'
import '@/assets/css/style.scss'
import router from '@/router/router-static.js'
import BreadCrumbs from '@/components/common/BreadCrumbs'
import http from '@/utils/http.js'
import base from '@/utils/base'
import storage from '@/utils/storage'
import FileUpload from '@/components/common/FileUpload'
import Editor from '@/components/common/Editor'
import * as validate from '@/utils/validate.js'

router.beforeEach((to, from, next) => {
  const publicPages = ['/login', '/register']
  if (!publicPages.includes(to.path) && !storage.get('Token')) {
    next('/login')
    return
  }
  next()
})

const app = createApp(App)

app.config.globalProperties.$validate = validate
app.config.globalProperties.$http = http
app.config.globalProperties.$base = base.get()
app.config.globalProperties.$storage = storage
app.config.globalProperties.$message = ElMessage
app.config.globalProperties.$confirm = ElMessageBox.confirm
app.config.globalProperties.$alert = ElMessageBox.alert
app.config.globalProperties.$notify = ElNotification

app.use(router)
app.use(ElementPlus, { size: 'default', zIndex: 3000 })

app.component('bread-crumbs', BreadCrumbs)
app.component('file-upload', FileUpload)
app.component('editor', Editor)

app.mount('#app')
