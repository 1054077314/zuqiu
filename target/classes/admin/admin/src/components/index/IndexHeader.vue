<template>
  <header class="navbar">
    <div class="left">
      <div class="title-name">{{ projectName }}</div>
    </div>
    <div class="right">
      <span class="user-info">{{ roleName }} {{ adminName }}</span>
      <el-button class="logout-btn" size="mini" type="primary" plain @click="onLogout">退出登录</el-button>
    </div>
  </header>
</template>

<script>
export default {
  data() {
    return {
      user: {}
    }
  },
  computed: {
    projectName() {
      return (this.$project && this.$project.projectName) || '足球俱乐部管理系统'
    },
    roleName() {
      return this.$storage.get('role') || '管理员'
    },
    adminName() {
      return this.$storage.get('adminName') || this.$storage.get('username') || ''
    }
  },
  mounted() {
    const sessionTable = this.$storage.get('sessionTable')
    if (!sessionTable) {
      return
    }
    this.$http({
      url: `${sessionTable}/session`,
      method: 'get'
    }).then(({ data }) => {
      if (data && data.code === 0) {
        this.user = data.data
        this.$storage.set('userid', data.data.id)
      }
    }).catch(() => {})
  },
  methods: {
    onLogout() {
      this.$storage.clear()
      this.$router.replace({ path: '/login' }).catch(() => {})
      setTimeout(() => {
        window.location.hash = '#/login'
      }, 30)
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  position: sticky;
  top: 0;
  height: 60px;
  width: 100%;
  padding: 0 18px;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: rgba(255, 255, 255, 0.62);
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
  box-shadow: 0 6px 18px rgba(15, 23, 42, 0.06);
  backdrop-filter: blur(14px);
  -webkit-backdrop-filter: blur(14px);
}

.left {
  min-width: 0;
  flex: 1;
}

.title-name {
  color: #111827;
  font-size: 20px;
  font-weight: 800;
  letter-spacing: 0.5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.right {
  margin-left: 12px;
  display: flex;
  align-items: center;
  gap: 10px;
  flex-shrink: 0;
}

.user-info {
  color: #4b5563;
  font-size: 14px;
}

.logout-btn {
  border-color: rgba(59, 130, 246, 0.45);
  color: #2563eb;
  background: rgba(59, 130, 246, 0.08);
}

.logout-btn:hover,
.logout-btn:focus {
  background: rgba(59, 130, 246, 0.16);
  color: #1d4ed8;
  border-color: rgba(37, 99, 235, 0.6);
}

@media (max-width: 760px) {
  .navbar {
    padding: 0 10px;
  }

  .title-name {
    font-size: 16px;
  }

  .user-info {
    display: none;
  }
}
</style>
