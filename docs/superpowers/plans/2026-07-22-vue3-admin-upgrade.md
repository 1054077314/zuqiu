# Vue3 Admin Upgrade Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Upgrade the admin frontend from Vue 2 / Vue CLI / Element UI to Vue 3 / Vite / Element Plus while preserving deployment and API behavior.

**Architecture:** Keep the existing component tree and Options API, replace only framework integration points and incompatible dependencies. Use Vite to emit static files into the same Spring Boot resource directory used by the current Vue CLI build.

**Tech Stack:** Vue 3, Vite, Element Plus, vue-router 4, axios, Sass, SVG sprite plugin, @vueup/vue-quill.

---

### Task 1: Tooling and Dependencies

**Files:**
- Modify: `src/main/resources/admin/admin/package.json`
- Modify: `src/main/resources/admin/admin/package-lock.json`
- Create: `src/main/resources/admin/admin/vite.config.js`
- Delete: `src/main/resources/admin/admin/vue.config.js`

- [ ] Replace Vue CLI scripts with Vite scripts: `serve` becomes `vite --host 0.0.0.0 --port 8081`, `build` becomes `vite build`, `preview` becomes `vite preview --host 0.0.0.0 --port 8081`.
- [ ] Replace dependencies: `vue@^3`, `vue-router@^4`, `element-plus`, `@vueup/vue-quill`, `@vitejs/plugin-vue`, `vite`, `vite-plugin-svg-icons`.
- [ ] Remove Vue2-only packages: `element-ui`, `vue-template-compiler`, `@vue/cli-service`, `@vue/cli-plugin-babel`, `@vue/cli-plugin-eslint`, `babel-eslint`, `babel-plugin-component`, `vue-quill-editor`.
- [ ] Add Vite config with `base: './'`, alias `@`, proxy `/zuqiujulebguanli`, output `../../admin/dist`, and SVG sprite support.
- [ ] Run `npm install` to refresh `package-lock.json`.

### Task 2: App Entry and Router

**Files:**
- Modify: `src/main/resources/admin/admin/src/main.js`
- Modify: `src/main/resources/admin/admin/src/router/router-static.js`

- [ ] Change `main.js` to use `createApp`.
- [ ] Register Element Plus, global CSS, global properties, global components, and router on the app instance.
- [ ] Import Element Plus styles instead of Element UI styles.
- [ ] Change router creation to `createRouter({ history: createWebHashHistory(), routes })`.
- [ ] Replace `path: '*'` with `/:pathMatch(.*)*`.
- [ ] Remove Vue Router 3 duplicate navigation monkey patch.

### Task 3: Element Plus and Template Compatibility

**Files:**
- Modify all matching Vue files under `src/main/resources/admin/admin/src`.

- [ ] Replace `slot-scope="scope"` table slots with `#default="scope"`.
- [ ] Replace simple named slot attributes such as `slot="suffix"` and `slot="tip"` with `#suffix` and `#tip`.
- [ ] Remove `.native` from component events and use direct Vue3 event syntax.
- [ ] Replace deprecated Element UI icon classes only where they break build.
- [ ] Keep existing Options API methods and data unchanged unless Vue3 compatibility requires a local edit.

### Task 4: Editor Component

**Files:**
- Modify: `src/main/resources/admin/admin/src/components/common/Editor.vue`

- [ ] Replace `vue-quill-editor` import with `@vueup/vue-quill`.
- [ ] Keep content bound through `v-model`.
- [ ] Preserve image upload flow through the existing hidden `el-upload`.
- [ ] Preserve readonly behavior and emitted updates.

### Task 5: Build Verification

**Files:**
- Generated: `src/main/resources/admin/dist/**`

- [ ] Run `npm run build` from `src/main/resources/admin/admin`.
- [ ] Fix all build errors introduced by Vue3/Vite migration.
- [ ] Confirm `src/main/resources/admin/dist/index.html` references assets with relative paths.
- [ ] Run `npm run serve -- --host 0.0.0.0 --port 8081` if build passes and report the local URL.

### Self-Review

- Spec coverage: The plan covers scope, entry/router, Element Plus migration, editor compatibility, Vite deployment, and build verification.
- Placeholder scan: No placeholder task is left; each task names concrete files and expected actions.
- Type consistency: `createApp`, `createRouter`, `createWebHashHistory`, `ElementPlus`, and `QuillEditor` are the intended Vue3 APIs.
