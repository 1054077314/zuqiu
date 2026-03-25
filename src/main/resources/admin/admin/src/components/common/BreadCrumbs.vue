<template>
  <div class="breadcrumb-wrap">
    <el-breadcrumb class="app-breadcrumb" separator="/">
      <el-breadcrumb-item>
        <a @click.prevent="toHome">首页</a>
      </el-breadcrumb-item>
      <el-breadcrumb-item v-if="currentName">{{ currentName }}</el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>

<script>
export default {
  data() {
    return {
      currentName: ''
    }
  },
  watch: {
    $route: {
      handler() {
        this.updateCurrentName()
      },
      immediate: true
    }
  },
  methods: {
    updateCurrentName() {
      const name = this.$route && this.$route.name ? String(this.$route.name) : ''
      this.currentName = name === '首页' ? '' : name
    },
    toHome() {
      this.$router.push('/index')
    }
  }
}
</script>

<style lang="scss" scoped>
.breadcrumb-wrap {
  width: 100%;
  margin-top: 8px;
  margin-bottom: 12px;
  padding: 12px 16px;
  border-radius: 14px;
  border: 1px solid #d9e8fb;
  background: linear-gradient(180deg, #ffffff 0%, #f6fbff 100%);
  box-shadow: 0 8px 20px rgba(13, 66, 122, 0.08);
  box-sizing: border-box;
}

.app-breadcrumb {
  line-height: 1;
  font-size: 14px;
}

.app-breadcrumb ::v-deep .el-breadcrumb__inner,
.app-breadcrumb ::v-deep .el-breadcrumb__inner a {
  color: #184d7a;
  font-weight: 600;
}

.app-breadcrumb ::v-deep .el-breadcrumb__separator {
  color: #7aa2c6;
}

.app-breadcrumb ::v-deep .el-breadcrumb__item:last-child .el-breadcrumb__inner {
  color: #4f7090;
  font-weight: 500;
}
</style>
