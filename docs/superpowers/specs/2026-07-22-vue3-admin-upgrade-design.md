# 后台管理端 Vue3 升级设计

## 背景

后台管理端位于 `src/main/resources/admin/admin`，当前技术栈是 Vue 2.6、Vue CLI 4、Element UI 2、vue-router 3。后端已经升级到 Spring Boot 3，前端仍停留在 Vue2。前台门户位于 `src/main/resources/front/front`，是静态 HTML 直接引入 Vue2 UMD 的结构，本次不纳入升级。

## 目标

将后台管理端升级为 Vue 3、Vite、Element Plus、vue-router 4，并保持以下行为不变：

- API 请求仍使用 `/zuqiujulebguanli` 前缀。
- 生产构建产物仍输出到 `src/main/resources/admin/dist`。
- 生产资源路径仍可由 Spring Boot 静态资源服务加载。
- 现有页面和业务逻辑以 Options API 为主，不进行大规模业务重构。

## 非目标

- 不升级前台门户静态页面。
- 不重写后端接口。
- 不把 Options API 全量改为 Composition API。
- 不重做 UI 视觉风格。

## 架构

入口从 Vue2 的 `new Vue({ router, render })` 改为 Vue3 的 `createApp(App).use(router).use(ElementPlus).mount('#app')`。全局属性从 `Vue.prototype` 迁移到 `app.config.globalProperties`，全局组件通过 `app.component` 注册。

路由从 `Vue.use(VueRouter)` 和 `new VueRouter()` 改为 `createRouter()` 和 `createWebHashHistory()`。保留 hash 模式，避免影响后端部署和刷新行为。Vue Router 3 的 `path: '*'` 替换为 Vue Router 4 的 `/:pathMatch(.*)*`。

UI 库从 `element-ui` 替换为 `element-plus`。模板中 Vue2 专属写法按最小改动迁移：`slot-scope` 改为 `#default`，`.native` 事件修饰符改为普通事件或显式键盘事件。

富文本编辑器从 `vue-quill-editor` 替换为 Vue3 兼容的 `@vueup/vue-quill`，保留现有上传图片和 `v-model` 内容同步语义。

## 数据流

`src/utils/http.js` 继续封装 axios 实例，保留请求头 `Token`、`withCredentials` 和 401 跳转登录逻辑。`src/utils/base.js` 继续负责生成后端上下文路径和前台首页路径。

## 构建

移除 Vue CLI 相关依赖和 `vue.config.js` 的构建职责，新增 `vite.config.js`。Vite 配置负责：

- `@` 指向 `src`。
- dev server 端口使用 8081。
- dev proxy 保持 `/zuqiujulebguanli` 到本地后端的代理。
- build 输出到 `../../admin/dist`。
- 生产 `base` 使用 `./`，保证后端静态目录下相对资源路径可用。
- SVG sprite 继续支持 `src/icons/svg`。

## 测试和验证

先用构建验证作为主门槛：`npm run build` 必须成功。迁移期间优先修复编译错误和 Vue3/Element Plus API 兼容错误。构建通过后启动 Vite dev server，至少手动检查登录页、首页、一个列表页和一个新增/编辑表单页面。

## 风险处理

Element Plus 与 Element UI 有部分 API 差异。如果某个组件在构建通过后运行时行为异常，优先对该组件做局部兼容，不进行全站重写。

`@vueup/vue-quill` 的组件接口与 `vue-quill-editor` 不完全一致。迁移时以保留内容输入、图片上传、只读展示三项能力为准。
