<template>
  <header class="navbar">
    <div class="brand" @click="go('/index')">足球俱乐部管理系统</div>

    <nav class="nav-menu">
      <button
        v-for="item in navItems"
        :key="item.path"
        type="button"
        :class="{ active: isActive(item.path) }"
        @click="go(item.path)"
      >
        {{ item.label }}
      </button>
    </nav>

    <div class="right-tools">
      <div class="admin-entry" @click="go('/center')">
        <span>{{ roleName }}</span>
        <strong>{{ adminName || '管理员' }}</strong>
      </div>
      <el-button class="logout-btn" size="mini" type="primary" plain @click="onLogout">退出登录</el-button>
    </div>
  </header>
</template>

<script>
export default {
  data() {
    return {
      user: {},
      navItems: [
        { label: '首页', path: '/index' },
        { label: '球队管理', path: '/yonghu' },
        { label: '教练管理', path: '/jiaolian' },
        { label: '训练计划', path: '/xunlian' },
        { label: '合同管理', path: '/hetong' },
        { label: '赛事管理', path: '/saishi' },
        { label: '球员数据', path: '/shuju' },
        { label: '公告管理', path: '/gonggao' }
      ]
    }
  },
  computed: {
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
    isActive(path) {
      if (path === '/index') {
        return this.$route.path === '/' || this.$route.path === '/index'
      }
      return this.$route.path === path
    },
    go(path) {
      this.$router.push({ path })
    },
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
  height: 58px;
  width: 100%;
  padding: 0 28px;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  gap: 20px;
  background: #ffffff;
  border-bottom: 1px solid #dfe3ea;
  box-shadow: 0 3px 14px rgba(15, 23, 42, 0.05);
}

.brand {
  flex: 0 0 auto;
  max-width: 230px;
  color: #0f172a;
  font-size: 18px;
  line-height: 1.2;
  font-weight: 900;
  cursor: pointer;
}

.nav-menu {
  display: flex;
  align-items: center;
  gap: 6px;
  min-width: 0;
  flex: 1 1 auto;
  overflow-x: auto;
}

.nav-menu button {
  position: relative;
  height: 34px;
  padding: 0 11px;
  border-radius: 6px;
  border: 0;
  background: transparent;
  color: #5b6b83;
  font-size: 14px;
  white-space: nowrap;
  cursor: pointer;
}

.nav-menu button.active,
.nav-menu button:hover {
  color: #0b57d0;
  font-weight: 700;
  background: #f3f7ff;
}

.nav-menu button.active::after {
  content: "";
  position: absolute;
  left: 10px;
  right: 10px;
  bottom: -12px;
  height: 2px;
  background: #2563eb;
}

.right-tools {
  display: flex;
  align-items: center;
  gap: 12px;
  flex: 0 0 auto;
}

.admin-entry {
  width: auto;
  min-width: 72px;
  height: 36px;
  padding: 0 12px;
  border-radius: 999px;
  background: #0f172a;
  color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(15, 23, 42, 0.24);
  box-sizing: border-box;
}

.admin-entry span {
  display: block;
  color: rgba(255, 255, 255, 0.72);
  font-size: 12px;
  line-height: 1;
}

.admin-entry strong {
  max-width: 64px;
  overflow: hidden;
  color: #ffffff;
  font-size: 12px;
  line-height: 1;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.logout-btn {
  border-color: rgba(37, 99, 235, 0.4);
  color: #2563eb;
  background: #ffffff;
}

@media (max-width: 1320px) {
  .navbar {
    gap: 16px;
    padding: 0 24px;
  }
}

@media (max-width: 980px) {
  .navbar {
    height: auto;
    min-height: 62px;
    align-items: flex-start;
    flex-wrap: wrap;
    padding: 12px 16px;
  }

  .nav-menu {
    order: 3;
    width: 100%;
    flex-basis: 100%;
  }

  .nav-menu button {
    height: 38px;
  }

  .right-tools {
    margin-left: auto;
  }
}

@media (max-width: 640px) {
  .brand {
    max-width: 160px;
    font-size: 17px;
  }

  .logout-btn {
    display: none;
  }
}
</style>
