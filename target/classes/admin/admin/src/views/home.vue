<template>
  <div class="home-page">
    <section class="hero-panel banner" :class="{ loaded }">
      <span class="hero-glow hero-glow-one"></span>
      <span class="hero-glow hero-glow-two"></span>

      <div class="hero-main-grid">
        <div class="hero-copy">
      
          <h1 class="hero-title">
            <span>俱乐部运营中心</span>
          </h1>
          <p class="hero-desc">
            {{ greeting }}，{{ currentUser || '管理员' }}。今天是 {{ nowDate }}，系统运行正常，
            可在此查看公告、赛事、合同、训练计划与首页轮播。
          </p>

          <div class="hero-actions">
            <el-button type="primary" class="primary-btn" @click="go('/gonggao')">发布公告</el-button>
          </div>
        </div>

        <div class="hero-info-card">
          <p class="hero-info-tag">实时概览</p>
          <h3>运营状态良好</h3>
          <ul class="hero-info-list">
            <li v-for="item in heroMetrics.slice(0, 3)" :key="item.label">
              <span>{{ item.label }}</span>
              <strong>{{ item.value }}</strong>
            </li>
          </ul>
        </div>
      </div>

      <section class="summary-grid hero-summary-grid">
        <article
          v-for="(item, index) in summaryCards"
          :key="item.key"
          class="summary-card"
          :style="{ '--accent': item.color, '--accent-rgb': item.rgb, '--delay': index * 0.04 + 's' }"
          @click="go(item.route)"
        >
          <div class="summary-icon">
            <i :class="item.icon"></i>
          </div>
          <div class="summary-info">
            <span>{{ item.label }}</span>
            <strong>{{ item.total }}</strong>
          </div>
        </article>
      </section>

    </section>

    <section class="content-stack">
      <div class="dual-grid">
        <article class="glass-card notice-card">
        <div class="panel-head">
          <div>
            <h3>最新公告</h3>
            <p>查看近期公告与通知</p>
          </div>
          <button class="panel-link" @click="go('/gonggao')">公告管理</button>
        </div>

          <ul v-if="latestNotices.length" class="notice-list">
            <li
              v-for="item in latestNotices.slice(0, 3)"
              :key="item.id"
              class="info-card notice-item"
              @click="openNoticeDetail(item)"
            >
              <div class="card-main">
                <strong class="card-title">{{ item.gonggaoName || '未命名公告' }}</strong>
                <p class="card-sub">{{ item.gonggaoValue || '系统公告' }}</p>
              </div>
              <span class="card-meta">{{ formatDate(item.insertTime) }}</span>
            </li>
          </ul>
          <div v-else class="empty-block compact-empty">暂无公告数据</div>
        </article>

        <article class="glass-card status-card">
        <div class="panel-head">
          <div>
            <h3>最新赛事</h3>
            <p>查看近期赛事安排</p>
          </div>
          <button class="panel-link" @click="go('/saishi')">赛事管理</button>
        </div>

          <ul v-if="latestMatches.length" class="status-list">
            <li
              v-for="item in latestMatches.slice(0, 3)"
              :key="item.id"
              class="info-card status-item"
              @click="openMatchDetail(item)"
            >
              <div class="card-main">
                <strong class="card-title">{{ item.saishiName || '未命名赛事' }}</strong>
                <p class="card-sub">地点：{{ item.saishiAddress || '待补充' }}</p>
              </div>
              <span class="card-meta">{{ item.saishiValue || '赛事' }}</span>
            </li>
          </ul>
          <div v-else class="empty-block compact-empty">暂无赛事数据</div>
        </article>
      </div>

    </section>
  </div>
</template>

<script>

const CARD_CONFIG = [
  { key: 'users', label: '管理员账号', icon: 'el-icon-s-custom', color: '#0d8bf2', rgb: '13,139,242', route: '/users', url: 'users/page', extra: {} },
  { key: 'jiaolian', label: '教练人数', icon: 'el-icon-user-solid', color: '#1d74d6', rgb: '29,116,214', route: '/jiaolian', url: 'jiaolian/page', extra: {} },
  { key: 'yonghu', label: '用户人数', icon: 'el-icon-user', color: '#2d8be6', rgb: '45,139,230', route: '/yonghu', url: 'yonghu/page', extra: {} },
  { key: 'saishi', label: '赛事数量', icon: 'el-icon-trophy', color: '#155fbe', rgb: '21,95,190', route: '/saishi', url: 'saishi/page', extra: { saishiDelete: 1 } },
  { key: 'hetong', label: '合同数量', icon: 'el-icon-document', color: '#327fd8', rgb: '50,127,216', route: '/hetong', url: 'hetong/page', extra: { hetongDelete: 1 } },
  { key: 'xunlian', label: '训练计划', icon: 'el-icon-date', color: '#3f96ea', rgb: '63,150,234', route: '/xunlian', url: 'xunlian/page', extra: { xunlianDelete: 1 } },
  { key: 'shuju', label: '球员数据', icon: 'el-icon-data-analysis', color: '#226dc9', rgb: '34,109,201', route: '/shuju', url: 'shuju/page', extra: { shujuDelete: 1 } },
  { key: 'gonggao', label: '公告数量', icon: 'el-icon-bell', color: '#4a9ced', rgb: '74,156,237', route: '/gonggao', url: 'gonggao/page', extra: {} }
]

export default {
  data() {
    return {
      loaded: false,
      timer: null,
      nowDate: '',
      nowTime: '',
      summaryCards: CARD_CONFIG.map(item => Object.assign({}, item, { total: '-' })),
      latestNotices: [],
      latestMatches: []
    }
  },
  computed: {
    projectName() {
      return (this.$project && this.$project.projectName) || '足球俱乐部管理系统'
    },
    currentUser() {
      return this.$storage.get('adminName') || this.$storage.get('username') || ''
    },
    currentRole() {
      return this.$storage.get('role') || ''
    },
    greeting() {
      const hour = new Date().getHours()
      if (hour < 6) return '凌晨好'
      if (hour < 12) return '上午好'
      if (hour < 18) return '下午好'
      return '晚上好'
    },
    heroMetrics() {
      return [
        { label: '当前时间', value: this.nowTime || '--:--:--' },
        { label: '教练人数', value: this.statValue('jiaolian') },
        { label: '赛事数量', value: this.statValue('saishi') }
      ]
    }
  },
  created() {
    this.updateClock()
    this.timer = setInterval(this.updateClock, 1000)
    this.loadStats()
    this.loadLatestNotices()
    this.loadLatestMatches()
  },
  mounted() {
    setTimeout(() => {
      this.loaded = true
    }, 100)
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
      this.timer = null
    }
  },
  methods: {
    statValue(key) {
      const item = this.summaryCards.find(card => card.key === key)
      return item ? item.total : '-'
    },
    formatDate(value) {
      if (!value) return '--'
      const text = String(value)
      return text.length > 16 ? text.slice(0, 16) : text
    },
    updateClock() {
      const now = new Date()
      const pad = n => String(n).padStart(2, '0')
      this.nowDate = now.getFullYear() + '-' + pad(now.getMonth() + 1) + '-' + pad(now.getDate())
      this.nowTime = pad(now.getHours()) + ':' + pad(now.getMinutes()) + ':' + pad(now.getSeconds())
    },
    go(path) {
      this.$router.push({ path })
    },
    openMatchDetail(item) {
      if (!item || !item.id) {
        this.go('/saishi')
        return
      }
      this.$storage.set('pendingSaishiOpenId', item.id)
      this.$storage.set('pendingSaishiOpenType', 'info')
      this.$router.push({ path: '/saishi' })
    },
    openNoticeDetail(item) {
      if (!item || !item.id) {
        this.go('/gonggao')
        return
      }
      this.$storage.set('pendingGonggaoOpenId', item.id)
      this.$storage.set('pendingGonggaoOpenType', 'info')
      this.$router.push({
        path: '/gonggao',
        query: {
          openId: String(item.id),
          openType: 'info'
        }
      })
    },
    loadStats() {
      const tasks = this.summaryCards.map(item => {
        return this.$http({
          url: item.url,
          method: 'get',
          params: Object.assign({
            page: 1,
            limit: 1,
            sort: 'id',
            order: 'desc'
          }, item.extra)
        }).then(({ data }) => {
          const total = data && data.code === 0 && data.data ? data.data.total : 0
          item.total = Number(total || 0)
        }).catch(() => {
          item.total = '-'
        })
      })

      Promise.all(tasks).then(() => {
        this.summaryCards = this.summaryCards.slice()
      })
    },
    loadLatestNotices() {
      this.$http({
        url: 'gonggao/page',
        method: 'get',
        params: {
          page: 1,
          limit: 5,
          sort: 'id',
          order: 'desc'
        }
      }).then(({ data }) => {
        if (data && data.code === 0 && data.data) {
          this.latestNotices = data.data.list || []
        } else {
          this.latestNotices = []
        }
      }).catch(() => {
        this.latestNotices = []
      })
    },
    loadLatestMatches() {
      this.$http({
        url: 'saishi/page',
        method: 'get',
        params: {
          page: 1,
          limit: 4,
          sort: 'id',
          order: 'desc',
          saishiDelete: 1
        }
      }).then(({ data }) => {
        if (data && data.code === 0 && data.data) {
          this.latestMatches = data.data.list || []
        } else {
          this.latestMatches = []
        }
      }).catch(() => {
        this.latestMatches = []
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.home-page {
  --space-xs: 8px;
  --space-sm: 10px;
  --space-md: 12px;
  --space-lg: 16px;
  --space-xl: 18px;
  --card-hover-transform: translateY(-3px);
  --card-hover-shadow: 0 12px 28px rgba(15, 23, 42, 0.08);
  --card-hover-border: rgba(59, 130, 246, 0.24);
  --hero-title-size: 36px;
  --section-title-size: 22px;
  --card-number-size: 20px;
  --body-text-size: 14px;
  --meta-text-size: 13px;
  --list-title-size: 16px;
  --label-text-size: 14px;
  --text-title: #0f172a;
  --text-label: #334155;
  --text-body: #475569;
  --text-sub: #64748b;
  --text-meta: #94a3b8;
  --lh-title: 1.2;
  --lh-body: 1.5;
  --lh-meta: 1.4;
  --section-gap: 12px;
  min-height: auto;
  padding: 10px;
  border-radius: 14px;
  position: relative;
  overflow: hidden;
  background: linear-gradient(180deg, #f3f5f7 0%, #f7f8fa 42%, #f3f4f6 100%);
}

.home-page::before,
.home-page::after {
  content: '';
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
  filter: blur(60px);
}

.home-page::before {
  width: 340px;
  height: 340px;
  left: -120px;
  top: -120px;
  background: rgba(59, 130, 246, 0.12);
}

.home-page::after {
  width: 320px;
  height: 320px;
  right: -130px;
  bottom: -140px;
  background: rgba(17, 24, 39, 0.06);
}

.hero-panel,
.content-stack,
.summary-grid {
  position: relative;
  z-index: 1;
}

.hero-panel {
  position: relative;
  overflow: hidden;
  padding: 12px;
  border-radius: 14px;
  color: #111827;
  border: 1px solid rgba(0, 0, 0, 0.05);
  background: rgba(255, 255, 255, 0.58);
  box-shadow: 0 10px 20px rgba(15, 23, 42, 0.05);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  opacity: 0;
  transform: translateY(8px);
  transition: all 0.25s ease;
}

.hero-panel.loaded {
  opacity: 1;
  transform: translateY(0);
}

.hero-glow {
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
  filter: blur(28px);
}

.hero-glow-one {
  width: 140px;
  height: 140px;
  top: -64px;
  left: -48px;
  background: rgba(59, 130, 246, 0.08);
}

.hero-glow-two {
  width: 130px;
  height: 130px;
  right: -60px;
  bottom: -72px;
  background: rgba(148, 163, 184, 0.12);
}

.hero-main-grid {
  position: relative;
  z-index: 1;
  display: grid;
  grid-template-columns: minmax(0, 1.45fr) minmax(240px, 0.78fr);
  gap: 8px;
  align-items: stretch;
}

.hero-copy {
  min-width: 0;
}

.hero-tag {
  margin: 0;
  color: var(--text-label);
  font-size: var(--label-text-size);
  font-weight: 600;
  letter-spacing: 0.04em;
  text-transform: uppercase;
}

.hero-title {
  margin: 4px 0;
  line-height: 1.12;
}

.hero-title span {
  display: inline-block;
  font-size: var(--hero-title-size);
  letter-spacing: 0.015em;
  font-weight: 700;
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.hero-subtitle {
  margin: 1px 0 0;
  color: var(--text-body);
  font-size: var(--body-text-size);
  font-weight: 500;
  line-height: var(--lh-body);
}

.hero-desc {
  margin: 6px 0 0;
  line-height: var(--lh-body);
  font-size: var(--body-text-size);
  color: var(--text-body);
  font-weight: 400;
  max-width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.hero-actions {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-sm);
  margin-top: 8px;
}

.primary-btn,
.secondary-btn {
  min-width: 116px;
  height: 36px;
  border-radius: 9px;
  padding: 0 14px;
  font-size: 13px;
  font-weight: 700;
  transition: transform 0.24s ease, box-shadow 0.24s ease, background 0.24s ease, border-color 0.24s ease;
}

.primary-btn {
  border: none;
  background: linear-gradient(135deg, #3b82f6, #2563eb);
  box-shadow: 0 8px 18px rgba(37, 99, 235, 0.24);
}

.primary-btn:hover,
.primary-btn:focus {
  transform: translateY(-2px);
  box-shadow: 0 12px 24px rgba(37, 99, 235, 0.28);
}

.secondary-btn {
  border: 1px solid rgba(0, 0, 0, 0.1);
  background: rgba(255, 255, 255, 0.72);
  color: #1f2937;
}

.secondary-btn:hover,
.secondary-btn:focus {
  transform: translateY(-2px);
  border-color: rgba(59, 130, 246, 0.34);
  background: rgba(255, 255, 255, 0.92);
  box-shadow: 0 10px 20px rgba(15, 23, 42, 0.08);
  color: #111827;
}

.hero-info-card {
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.6);
  background: rgba(255, 255, 255, 0.5);
  box-shadow: 0 8px 16px rgba(15, 23, 42, 0.05);
  backdrop-filter: blur(14px);
  -webkit-backdrop-filter: blur(14px);
  position: relative;
  overflow: hidden;
  padding: 10px;
}

.hero-info-card::before {
  content: '';
  position: absolute;
  width: 72px;
  height: 72px;
  right: -22px;
  top: -24px;
  border-radius: 50%;
  background: rgba(59, 130, 246, 0.08);
  pointer-events: none;
}

.hero-info-tag {
  position: relative;
  margin: 0;
  color: var(--text-label);
  font-size: var(--label-text-size);
  letter-spacing: 0.04em;
  text-transform: uppercase;
  font-weight: 600;
}

.hero-info-card h3 {
  position: relative;
  margin: 4px 0 8px;
  color: var(--text-title);
  font-size: var(--section-title-size);
  line-height: var(--lh-title);
  font-weight: 600;
}

.hero-info-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: grid;
  gap: 6px;
}

.hero-info-list li {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 6px 8px;
  border-radius: 9px;
  border: 1px solid rgba(0, 0, 0, 0.05);
  background: rgba(255, 255, 255, 0.62);
}

.hero-info-list span {
  color: var(--text-sub);
  font-size: var(--meta-text-size);
  line-height: var(--lh-meta);
  font-weight: 400;
}

.hero-info-list strong {
  color: #1e293b;
  font-size: var(--label-text-size);
  line-height: 1.35;
  font-weight: 500;
}

.hero-summary-grid {
  position: relative;
  z-index: 1;
  margin-top: 8px;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 8px;
  margin-top: var(--section-gap);
}

.summary-grid.hero-summary-grid {
  margin-top: 8px;
}

.summary-card {
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  min-height: 58px;
  padding: 8px;
  border-radius: 12px;
  border: 1px solid rgba(0, 0, 0, 0.05);
  background: rgba(255, 255, 255, 0.62);
  box-shadow: 0 10px 22px rgba(15, 23, 42, 0.05);
  cursor: pointer;
  opacity: 0;
  transform: translateY(12px);
  animation: rise-in 0.35s ease forwards;
  animation-delay: var(--delay);
  transition: transform 0.22s ease, box-shadow 0.22s ease, border-color 0.22s ease;
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
}

.summary-card:hover {
  transform: var(--card-hover-transform);
  border-color: var(--card-hover-border);
  box-shadow: var(--card-hover-shadow);
}

.summary-card::after {
  content: '';
  position: absolute;
  top: -10px;
  right: -8px;
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: rgba(var(--accent-rgb), 0.1);
}

.summary-icon {
  position: relative;
  z-index: 1;
  width: 26px;
  height: 26px;
  border-radius: 8px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #3b82f6, #2563eb);
  color: #fff;
  font-size: 11px;
  box-shadow: 0 6px 10px rgba(37, 99, 235, 0.18);
}

.summary-info {
  position: relative;
  z-index: 1;
}

.summary-info span {
  display: block;
  color: var(--text-label);
  font-size: var(--label-text-size);
  line-height: var(--lh-meta);
  font-weight: 500;
}

.summary-info strong {
  display: block;
  margin-top: 1px;
  color: var(--text-title);
  font-size: var(--card-number-size);
  line-height: 1.1;
  font-weight: 700;
}

.content-stack {
  display: grid;
  gap: var(--section-gap);
  margin-top: 10px;
}

.dual-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
  align-items: start;
}

.notice-card,
.status-card {
  min-height: 0;
  height: auto;
  display: flex;
  flex-direction: column;
}

.notice-card .compact-empty,
.status-card .compact-empty {
  flex: 1;
  min-height: 0;
}

.glass-card {
  padding: 14px;
  border-radius: 14px;
  border: 1px solid rgba(0, 0, 0, 0.05);
  background: rgba(255, 255, 255, 0.58);
  box-shadow: 0 12px 26px rgba(15, 23, 42, 0.05);
  backdrop-filter: blur(14px);
  -webkit-backdrop-filter: blur(14px);
  transition: transform 0.22s ease, box-shadow 0.22s ease, border-color 0.22s ease;
}

.glass-card:hover {
  transform: var(--card-hover-transform);
  border-color: var(--card-hover-border);
  box-shadow: var(--card-hover-shadow);
}

.panel-head {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: var(--space-sm);
  margin-bottom: 10px;
}

.panel-head h3 {
  margin: 0;
  color: var(--text-title);
  font-size: var(--section-title-size);
  font-weight: 600;
  letter-spacing: 0.01em;
  line-height: var(--lh-title);
}

.panel-head p {
  margin: 4px 0 0;
  color: var(--text-body);
  font-size: var(--body-text-size);
  font-weight: 400;
  line-height: var(--lh-body);
}

.panel-link {
  border: none;
  background: rgba(59, 130, 246, 0.1);
  color: #2563eb;
  border-radius: 10px;
  height: 34px;
  padding: 0 12px;
  font-size: 13px;
  font-weight: 700;
  white-space: nowrap;
  cursor: pointer;
  transition: all 0.22s ease;
}

.panel-link:hover {
  transform: translateY(-2px);
  color: #fff;
  background: linear-gradient(135deg, #3b82f6, #2563eb);
  box-shadow: 0 8px 16px rgba(37, 99, 235, 0.24);
}

.notice-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: grid;
  gap: 10px;
}

.status-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: grid;
  gap: 10px;
}

.info-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 10px 11px;
  border: 1px solid rgba(0, 0, 0, 0.05);
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.66);
  cursor: pointer;
  transition: transform 0.22s ease, box-shadow 0.22s ease, border-color 0.22s ease;
}

.info-card:hover {
  transform: var(--card-hover-transform);
  border-color: var(--card-hover-border);
  box-shadow: var(--card-hover-shadow);
}

.card-main {
  min-width: 0;
  flex: 1;
}

.card-title {
  display: block;
  color: var(--text-title);
  font-size: var(--list-title-size);
  line-height: 1.35;
  font-weight: 600;
}

.card-sub {
  margin: 3px 0 0;
  color: var(--text-sub);
  font-size: var(--body-text-size);
  line-height: var(--lh-body);
  font-weight: 400;
}

.card-meta {
  flex-shrink: 0;
  color: var(--text-meta);
  font-size: var(--meta-text-size);
  line-height: var(--lh-meta);
  font-weight: 400;
  white-space: nowrap;
}

.empty-block {
  min-height: 96px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  border: 1px dashed rgba(0, 0, 0, 0.14);
  background: rgba(255, 255, 255, 0.55);
  color: #6b7280;
}

.compact-empty {
  min-height: 108px;
}

@keyframes rise-in {
  from {
    opacity: 0;
    transform: translateY(12px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 1380px) {
  .hero-main-grid {
    grid-template-columns: minmax(0, 1fr) minmax(0, 1fr);
  }

  .summary-grid {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}

@media (max-width: 1080px) {
  .hero-main-grid,
  .dual-grid {
    display: grid;
    grid-template-columns: 1fr;
  }

  .hero-desc {
    max-width: none;
  }

  .summary-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 760px) {
  .home-page {
    padding: 8px;
    border-radius: 16px;
  }

  .hero-panel,
  .glass-card {
    padding: 12px 10px;
    border-radius: 12px;
  }

  .hero-desc {
    white-space: nowrap;
  }

  .hero-main-grid,
  .dual-grid,
  .summary-grid {
    grid-template-columns: 1fr;
  }

  .summary-card,
  .info-card {
    margin-bottom: 12px;
  }

  .info-card {
    align-items: flex-start;
    flex-direction: column;
    gap: 4px;
  }

  .card-meta {
    white-space: normal;
  }
}
</style>
