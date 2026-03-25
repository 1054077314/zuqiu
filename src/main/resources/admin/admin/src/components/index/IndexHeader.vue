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
  height: 60px;
  width: 100%;
  padding: 0 18px;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(180deg, #ffffff 0%, #f4faff 100%);
  border-bottom: 1px solid #dceafd;
  box-shadow: 0 4px 14px rgba(13, 66, 122, 0.08);
}

.left {
  min-width: 0;
  flex: 1;
}

.title-name {
  color: #1e3f61;
  font-size: 20px;
  font-weight: 700;
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
  color: #456684;
  font-size: 14px;
}

.logout-btn {
  border-color: #2f8ec6;
  color: #1f75ab;
  background: #edf7ff;
}

.logout-btn:hover,
.logout-btn:focus {
  background: #dff0ff;
  color: #145a88;
  border-color: #1f75ab;
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
