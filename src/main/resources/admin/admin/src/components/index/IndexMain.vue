<template>
  <el-main class="index-main">
    <div class="content-wrap">
      <div class="router-shell">
        <router-view class="router-view" />
      </div>
    </div>
  </el-main>
</template>

<script>
export default {}
</script>

<style lang="scss" scoped>
.index-main {
  padding: 14px 18px 20px;
  min-height: calc(100vh - 58px);
  background: transparent;
  box-sizing: border-box;
  font-family: var(--app-font-family);
}

.content-wrap {
  width: 100%;
  max-width: none;
  margin: 0 auto;
}

.router-shell {
  width: 100%;
  min-height: calc(100vh - 58px);
}

.router-view {
  height: auto;
  background: transparent;
}

@media (max-width: 768px) {
  .index-main {
    padding: 10px 10px 16px;
  }
}
</style>
