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
  background: linear-gradient(180deg, #f0f2f5 0%, #f6f7f9 45%, #f3f4f6 100%);
}

.layout-shell::before,
.layout-shell::after {
  content: "";
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
  filter: blur(70px);
}

.layout-shell::before {
  width: 420px;
  height: 420px;
  top: -160px;
  left: -120px;
  background: rgba(59, 130, 246, 0.12);
}

.layout-shell::after {
  width: 360px;
  height: 360px;
  right: -120px;
  bottom: -140px;
  background: rgba(17, 24, 39, 0.06);
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
  padding-top: 60px;
}
</style>
