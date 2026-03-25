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
  background: linear-gradient(180deg, #edf4ff 0%, #f5f9ff 36%, #f2f7fd 100%);
}

.layout-header {
  position: sticky;
  top: 0;
  z-index: 200;
}

.layout-content {
  min-height: calc(100vh - 60px);
}
</style>
