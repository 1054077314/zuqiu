<template>
  <div class="layout-shell">
    <div class="layout-header">
      <index-header />
    </div>
    <div class="layout-content">
      <index-main />
    </div>
  </div>
</template>

<script>
import IndexHeader from '@/components/index/IndexHeader'
import IndexMain from '@/components/index/IndexMain'

export default {
  components: {
    IndexHeader,
    IndexMain
  }
}
</script>

<style lang="scss" scoped>
.layout-shell {
  min-height: 100vh;
  position: relative;
  overflow: hidden;
  background: #f3f4f7;
}

.layout-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  width: 100%;
  z-index: 200;
}

.layout-content {
  position: relative;
  z-index: 1;
  min-height: 100vh;
  padding-top: 58px;
}
</style>
