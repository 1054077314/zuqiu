import Vue from 'vue'
import App from '@/App.vue'
// element-ui 全量引入
import ElementUI from 'element-ui'
import '@/assets/css/element-variables.scss'
import '@/assets/css/style.scss'
// 加载路由
// import router from '@/router/router-static.js';
import router from '@/router/router-static.js'
// 面包屑导航，注册为全局组件
import BreadCrumbs from '@/components/common/BreadCrumbs'
// ajax
import http from '@/utils/http.js'
// 基础配置
import base from '@/utils/base'
// storage 封装
import storage from '@/utils/storage'
// 上传组件
import FileUpload from '@/components/common/FileUpload'
// 富文本编辑器组件
import Editor from '@/components/common/Editor'
// 数据校验工具
import * as validate from '@/utils/validate.js'

Vue.prototype.$validate = validate
Vue.prototype.$http = http // ajax 请求方法
Vue.prototype.$base = base.get()
Vue.prototype.$storage = storage

Vue.use(ElementUI, { size: 'medium', zIndex: 3000 })
Vue.config.productionTip = false

// 全局组件
Vue.component('bread-crumbs', BreadCrumbs)
Vue.component('file-upload', FileUpload)
Vue.component('editor', Editor)

new Vue({
  render: h => h(App),
  router
}).$mount('#app')
