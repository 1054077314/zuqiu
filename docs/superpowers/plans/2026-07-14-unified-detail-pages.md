# Unified Detail Pages Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Make every business detail view in the front portal and admin console use one clean, card-based information design without changing APIs or editable forms.

**Architecture:** The front portal receives one shared stylesheet and keeps each static page responsible only for its data-field mapping. The admin console receives one `DetailShowcase` component for `type === 'info'`; existing Element UI forms remain untouched for add and edit modes. Module-specific configuration supplies labels, cover image paths, metadata, field values, and rich text.

**Tech Stack:** Static HTML, Vue 2, Layui, Element UI, SCSS, Vue CLI/Webpack.

---

## File structure

- Create: `src/main/resources/front/front/css/detail-showcase.css` — shared front detail visual system, responsive layout, rich-text and media fallbacks.
- Create: `src/main/resources/admin/admin/src/components/common/DetailShowcase.vue` — reusable read-only card for admin business records.
- Modify: `src/main/resources/front/front/pages/{gonggao,hetong,jiaolian,saishi,shuju,xunlian,yonghu}/detail.html` — reference the shared CSS and render the standard showcase structure with their existing API data.
- Modify: `src/main/resources/admin/admin/src/assets/css/style.scss` — global admin showcase tokens and responsive card styles.
- Modify: `src/main/resources/admin/admin/src/views/modules/{gonggao,hetong,jiaolian,saishi,shuju,xunlian,yonghu}/add-or-update.vue` — use `DetailShowcase` only when `type === 'info'`; retain each existing editable form in `v-else`.

### Task 1: Add a reusable front-detail stylesheet

**Files:**
- Create: `src/main/resources/front/front/css/detail-showcase.css`

- [ ] **Step 1: Add the design tokens and frame styles**

```css
:root {
  --detail-ink: #172033;
  --detail-muted: #65708a;
  --detail-line: #d9e1f2;
  --detail-panel: #ffffff;
  --detail-page: #f5f7fb;
  --detail-accent: #f0c419;
  --detail-soft: #edf1fb;
}
.detail-page { min-height: 100vh; background: var(--detail-page); color: var(--detail-ink); }
.detail-container { width: min(1180px, calc(100% - 32px)); margin: 0 auto; padding: 42px 0 56px; }
.detail-showcase { background: var(--detail-panel); border: 1px solid var(--detail-line); border-radius: 22px; padding: 42px; }
```

- [ ] **Step 2: Add media, field-list, content, action, and mobile rules**

```css
.detail-overview { display: grid; grid-template-columns: minmax(280px, .95fr) minmax(0, 1fr); gap: 48px; }
.detail-cover { width: 100%; min-height: 360px; border: 1px solid var(--detail-line); border-radius: 16px; object-fit: cover; background: #e8edf8; }
.detail-fields { border-top: 1px solid var(--detail-line); border-bottom: 1px solid var(--detail-line); margin-top: 28px; padding: 14px 0; }
.detail-field { display: flex; justify-content: space-between; gap: 20px; padding: 11px 0; }
.detail-field__label { color: var(--detail-muted); font-weight: 600; }
.detail-field__value { color: var(--detail-ink); font-weight: 700; text-align: right; word-break: break-word; }
.detail-content { margin-top: 42px; padding-top: 34px; border-top: 1px solid var(--detail-line); line-height: 1.9; color: #4d5870; }
.detail-content img { max-width: 100%; height: auto; border-radius: 12px; }
.detail-actions { display: flex; justify-content: flex-end; gap: 12px; margin-top: 30px; }
@media (max-width: 860px) { .detail-container { width: min(100% - 20px, 680px); padding-top: 16px; } .detail-showcase { padding: 22px; } .detail-overview { grid-template-columns: 1fr; gap: 26px; } .detail-cover { min-height: 220px; } .detail-field { display: block; } .detail-field__value { margin-top: 5px; text-align: left; } }
```

- [ ] **Step 3: Verify CSS syntax and selectors are present**

Run: `rg -n "detail-overview|detail-field__value|@media" src/main/resources/front/front/css/detail-showcase.css`

Expected: one match for each selector and the mobile breakpoint.

### Task 2: Convert all seven front portal details to the shared layout

**Files:**
- Modify: `src/main/resources/front/front/pages/gonggao/detail.html`
- Modify: `src/main/resources/front/front/pages/hetong/detail.html`
- Modify: `src/main/resources/front/front/pages/jiaolian/detail.html`
- Modify: `src/main/resources/front/front/pages/saishi/detail.html`
- Modify: `src/main/resources/front/front/pages/shuju/detail.html`
- Modify: `src/main/resources/front/front/pages/xunlian/detail.html`
- Modify: `src/main/resources/front/front/pages/yonghu/detail.html`

- [ ] **Step 1: Link every page to the shared stylesheet and remove its page-specific showcase CSS**

```html
<link rel="stylesheet" href="../../css/detail-showcase.css">
```

Keep the existing Layui, Vue, config, module-config, and utility imports in the same order.

- [ ] **Step 2: Replace each page body with the shared semantic skeleton while retaining its current request code**

```html
<main class="detail-page">
  <div class="detail-container">
    <article class="detail-showcase">
      <section class="detail-overview">
        <img v-if="coverImage" class="detail-cover" :src="coverImage" :alt="pageTitle" @error="onImageError">
        <section class="detail-summary">
          <span class="detail-badge">{{ categoryLabel }}</span>
          <h1 class="detail-title">{{ pageTitle }}</h1>
          <div class="detail-fields">
            <div v-for="field in visibleFields" :key="field.label" class="detail-field">
              <span class="detail-field__label">{{ field.label }} // {{ field.en }}</span>
              <strong class="detail-field__value">{{ field.value || '暂无' }}</strong>
            </div>
          </div>
        </section>
      </section>
      <section v-if="detailContent" class="detail-content" v-html="renderContent(detailContent)"></section>
      <div class="detail-actions"><a class="detail-secondary-action" href="./list.html">返回列表</a><button class="detail-primary-action" type="button" @click="goBack">返回上一页</button></div>
    </article>
  </div>
</main>
```

- [ ] **Step 3: Define the exact page field maps and content sources**

| Module | Cover field | Title | Badge | Detail content | Field values |
| --- | --- | --- | --- | --- | --- |
| 赛事 | `saishiPhoto` | `saishiName` | `saishiValue` | `saishiContent` | 赛事编号/地点/类型/发布时间 |
| 训练 | no cover | `xunlianName` | `xunlianValue` | `xunlianContent` | 计划编号/训练科目/训练日期/用户 |
| 公告 | no cover | `gonggaoName` | `gonggaoValue` | `gonggaoContent` | 公告类型/发布时间 |
| 合同 | no cover | `hetongName` | `合同` | `hetongText` | 用户/用户编号/手机号/合同附件 |
| 教练 | `jiaolianPhoto` | `jiaolianName` | `sexValue` | no rich text | 教练编号/账号/手机号/邮箱 |
| 球员 | `yonghuPhoto` | `yonghuName` | `sexValue` | no rich text | 用户编号/账号/手机号/邮箱 |
| 数据 | `shujuPhoto` | `shujuName` | `shujuValue` | `shujuContent` | 数据编号/用户/日期/类型 |

For no-cover records, make `.detail-overview` receive a `detail-overview--text` class and use one column; do not add stock images.

- [ ] **Step 4: Preserve URL and media safety behavior**

```js
methods: {
  goBack: function () { window.history.back() },
  onImageError: function (event) { event.target.style.display = 'none' },
  imageUrl: function (path) {
    if (!path) return ''
    if (/^https?:\/\//.test(path)) return path
    return this.baseUrl + path.split(',')[0].replace(/^\//, '')
  }
}
```

Continue to use `layui.http.fixRichTextImages` where it already exists and do not interpolate API data with string-built HTML.

- [ ] **Step 5: Smoke-check all front details for shared-style linkage and API routes**

Run: `rg -n "detail-showcase.css|/info/|detail-showcase" src/main/resources/front/front/pages/{gonggao,hetong,jiaolian,saishi,shuju,xunlian,yonghu}/detail.html`

Expected: each file references the shared stylesheet, has a showcase root, and still calls its module’s `/info/` endpoint.

### Task 3: Add the reusable admin read-only showcase component

**Files:**
- Create: `src/main/resources/admin/admin/src/components/common/DetailShowcase.vue`
- Modify: `src/main/resources/admin/admin/src/assets/css/style.scss`

- [ ] **Step 1: Create the component API and template**

```vue
<template>
  <section class="admin-detail-showcase">
    <div class="admin-detail-showcase__overview" :class="{ 'is-text-only': !cover }">
      <div v-if="cover" class="admin-detail-showcase__media"><img :src="cover" :alt="title" @error="hideBrokenImage"></div>
      <div class="admin-detail-showcase__summary">
        <span v-if="badge" class="admin-detail-showcase__badge">{{ badge }}</span>
        <h1>{{ title || '暂无标题' }}</h1>
        <div class="admin-detail-showcase__fields">
          <div v-for="field in visibleFields" :key="field.label" class="admin-detail-showcase__field"><span>{{ field.label }} // {{ field.en }}</span><strong>{{ field.value || '暂无' }}</strong></div>
        </div>
      </div>
    </div>
    <section v-if="content" class="admin-detail-showcase__content"><h2>{{ contentTitle || '详情内容' }}</h2><div v-html="content"></div></section>
    <footer class="admin-detail-showcase__actions"><el-button @click="$emit('back')">返回</el-button></footer>
  </section>
</template>
```

Expose `title`, `badge`, `cover`, `fields`, `content`, and `contentTitle` as props. Compute `visibleFields` by filtering out fields whose `hidden` is true; this hides role-restricted owner fields without duplicating templates.

- [ ] **Step 2: Add component-scoped styles and global responsive integration**

```scss
.admin-detail-showcase { background: #fff; border: 1px solid #d9e1f2; border-radius: 18px; padding: 32px; }
.admin-detail-showcase__overview { display: grid; grid-template-columns: minmax(260px, .85fr) minmax(0, 1fr); gap: 34px; }
.admin-detail-showcase__overview.is-text-only { grid-template-columns: 1fr; }
.admin-detail-showcase__media img { width: 100%; min-height: 280px; object-fit: cover; border-radius: 14px; background: #edf1fb; }
.admin-detail-showcase__field { display: flex; justify-content: space-between; gap: 16px; padding: 10px 0; border-bottom: 1px solid #edf0f6; }
@media (max-width: 860px) { .admin-detail-showcase { padding: 20px; } .admin-detail-showcase__overview { grid-template-columns: 1fr; } .admin-detail-showcase__field { display: block; } }
```

- [ ] **Step 3: Verify the component compiles structurally**

Run: `rg -n "props:|visibleFields|\$emit\('back'\)" src/main/resources/admin/admin/src/components/common/DetailShowcase.vue`

Expected: props declaration, field-filter computed property, and back event are present.

### Task 4: Switch the admin business modules to the showcase in read-only mode

**Files:**
- Modify: `src/main/resources/admin/admin/src/views/modules/gonggao/add-or-update.vue`
- Modify: `src/main/resources/admin/admin/src/views/modules/hetong/add-or-update.vue`
- Modify: `src/main/resources/admin/admin/src/views/modules/jiaolian/add-or-update.vue`
- Modify: `src/main/resources/admin/admin/src/views/modules/saishi/add-or-update.vue`
- Modify: `src/main/resources/admin/admin/src/views/modules/shuju/add-or-update.vue`
- Modify: `src/main/resources/admin/admin/src/views/modules/xunlian/add-or-update.vue`
- Modify: `src/main/resources/admin/admin/src/views/modules/yonghu/add-or-update.vue`

- [ ] **Step 1: Import and register `DetailShowcase` in every listed module**

```js
import DetailShowcase from '@/components/common/DetailShowcase.vue'
export default { components: { DetailShowcase }, props: ['parent'] }
```

- [ ] **Step 2: Add a `detailFields` computed property per module using the same field map as Task 2**

```js
detailFields() {
  return [
    { label: '赛事编号', en: 'MATCH ID', value: this.ruleForm.saishiUuidNumber || this.ruleForm.id },
    { label: '比赛地点', en: 'LOCATION', value: this.ruleForm.saishiAddress },
    { label: '赛事类型', en: 'CATEGORY', value: this.ruleForm.saishiValue },
    { label: '发布时间', en: 'PUBLISHED AT', value: this.ruleForm.insertTime }
  ]
}
```

For user-owned data, preserve `sessionTable !== 'yonghu'` by setting the user identity fields to `{ hidden: this.sessionTable === 'yonghu', ... }`.

- [ ] **Step 3: Replace `type === 'info'` markup with the component and leave the edit form in `v-else`**

```vue
<detail-showcase
  v-if="type === 'info'"
  :title="ruleForm.saishiName"
  :badge="ruleForm.saishiValue"
  :cover="detailCover"
  :fields="detailFields"
  :content="ruleForm.saishiContent"
  content-title="赛事介绍"
  @back="back"
/>
<el-form v-else ref="ruleForm" :model="ruleForm" :rules="rules" label-width="100px" class="detail-form-content">
```

Derive `detailCover` from the first comma-separated photo value and prefix it with `$base.url`; pass an empty string for no-cover modules.

- [ ] **Step 4: Ensure existing image-rich-text fixes still run after detail data loads**

Keep each module’s existing `info(id)` request, dictionary loading, attachment link, and `replaceAll('src="upload/', ...)` handling. The only behavioral change is the read-only template branch.

- [ ] **Step 5: Verify view mode separation**

Run: `rg -n "<detail-showcase|v-else|type === 'info'" src/main/resources/admin/admin/src/views/modules/{gonggao,hetong,jiaolian,saishi,shuju,xunlian,yonghu}/add-or-update.vue`

Expected: every module has one showcase branch and one form branch; save buttons appear only in the form branch.

### Task 5: Build and visually verify the delivered pages

**Files:**
- Modify: generated `src/main/resources/admin/dist/**` only through the normal build output.

- [ ] **Step 1: Run the admin production build**

Run: `npm run build`

Working directory: `src/main/resources/admin/admin`

Expected: command exits with code 0 and regenerates `src/main/resources/admin/dist/`.

- [ ] **Step 2: Start the existing Spring Boot application and inspect representative pages**

Check these paths in a browser after the app is running:

```text
/zuqiujulebguanli/front/pages/saishi/detail.html?id=517
/zuqiujulebguanli/front/pages/gonggao/detail.html?id=<existing-id>
/zuqiujulebguanli/admin/#/saishi
```

Expected: the front event page uses left-media/right-information layout; the admin “查看” action opens the same information hierarchy; edit still exposes the current Element UI inputs and save button.

- [ ] **Step 3: Test responsive layout**

In the browser responsive viewport at 390px width, confirm the cover, fields, rich text, and buttons stack vertically without horizontal overflow on the event detail and one no-cover detail.

- [ ] **Step 4: Check the change set without staging unrelated worktree changes**

Run: `git diff --check -- src/main/resources/front/front/css/detail-showcase.css src/main/resources/front/front/pages src/main/resources/admin/admin/src/components/common/DetailShowcase.vue src/main/resources/admin/admin/src/assets/css/style.scss src/main/resources/admin/admin/src/views/modules`

Expected: no whitespace errors. Do not add the pre-existing cache, list-page, or generated-dist changes to a commit.

