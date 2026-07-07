<template>
  <div class="home-page">
    <section class="page-title">
      <h1>俱乐部运营中心</h1>
      <p>系统实时数据概览</p>
    </section>

    <section class="summary-grid">
      <article
        v-for="item in summaryCards"
        :key="item.key"
        class="summary-card"
        @click="go(item.route)"
      >
        <div class="summary-icon">
          <i :class="item.icon"></i>
        </div>
        <div class="summary-label">{{ item.label }}</div>
        <strong class="summary-number">{{ item.total }}</strong>
      </article>
    </section>

    <section class="dashboard-panels">
      <article class="panel-card">
        <div class="panel-head">
          <h2>最新公告</h2>
          <button type="button" @click="go('/gonggao')">查看全部</button>
        </div>
        <ul v-if="latestNotices.length" class="notice-list">
          <li
            v-for="item in latestNotices.slice(0, 3)"
            :key="item.id"
            class="notice-item"
            @click="openNoticeDetail(item)"
          >
            <div class="notice-meta">
              <span>{{ item.gonggaoValue || '公告' }}</span>
              <time>{{ formatDate(item.insertTime) }}</time>
            </div>
            <p>{{ noticeText(item) }}</p>
          </li>
        </ul>
        <div v-else class="empty-block">暂无公告数据</div>
      </article>

      <article class="panel-card">
        <div class="panel-head">
          <h2>最新赛事</h2>
          <button type="button" @click="go('/saishi')">赛程表</button>
        </div>
        <ul v-if="latestMatches.length" class="match-list">
          <li
            v-for="item in latestMatches.slice(0, 3)"
            :key="item.id"
            class="match-item"
            @click="openMatchDetail(item)"
          >
            <div class="match-main">
              <div class="match-time">{{ matchTime(item) }} - {{ item.saishiValue || '赛事' }}</div>
              <div class="match-teams">
                <strong>{{ matchHome(item) }}</strong>
                <span>{{ matchScore(item) }}</span>
                <strong>{{ matchAway(item) }}</strong>
              </div>
            </div>
            <span class="match-status" :class="{ ended: isEnded(item) }">
              {{ isEnded(item) ? '已结束' : '未开始' }}
            </span>
          </li>
        </ul>
        <div v-else class="empty-block">暂无赛事数据</div>
      </article>
    </section>

    <footer class="home-footer">
      <strong>足球俱乐部管理系统</strong>
      <span>© 2026 足球俱乐部管理系统后台运营中心. All rights reserved.</span>
    </footer>
  </div>
</template>

<script>
const CARD_CONFIG = [
  { key: 'users', label: '管理员账号', icon: 'el-icon-s-custom', route: '/users', url: 'users/page', extra: {} },
  { key: 'jiaolian', label: '教练人数', icon: 'el-icon-user-solid', route: '/jiaolian', url: 'jiaolian/page', extra: {} },
  { key: 'yonghu', label: '用户人数', icon: 'el-icon-user', route: '/yonghu', url: 'yonghu/page', extra: {} },
  { key: 'saishi', label: '赛事数量', icon: 'el-icon-trophy', route: '/saishi', url: 'saishi/page', extra: { saishiDelete: 1 } },
  { key: 'hetong', label: '合同数量', icon: 'el-icon-document', route: '/hetong', url: 'hetong/page', extra: { hetongDelete: 1 } },
  { key: 'xunlian', label: '训练计划', icon: 'el-icon-date', route: '/xunlian', url: 'xunlian/page', extra: { xunlianDelete: 1 } },
  { key: 'shuju', label: '球员数据', icon: 'el-icon-data-analysis', route: '/shuju', url: 'shuju/page', extra: { shujuDelete: 1 } },
  { key: 'gonggao', label: '公告数量', icon: 'el-icon-bell', route: '/gonggao', url: 'gonggao/page', extra: {} }
]

export default {
  data() {
    return {
      summaryCards: CARD_CONFIG.map(item => Object.assign({}, item, { total: '-' })),
      latestNotices: [],
      latestMatches: []
    }
  },
  created() {
    this.loadStats()
    this.loadLatestNotices()
    this.loadLatestMatches()
  },
  methods: {
    formatDate(value) {
      if (!value) return '--'
      const text = String(value)
      return text.length > 16 ? text.slice(0, 16) : text
    },
    stripHtml(value) {
      return String(value || '').replace(/<[^>]+>/g, '').replace(/\s+/g, ' ').trim()
    },
    noticeText(item) {
      return this.stripHtml(item.gonggaoContent) || item.gonggaoName || '暂无公告内容'
    },
    matchTime(item) {
      return this.formatDate(item.insertTime)
    },
    matchName(item) {
      return String(item.saishiName || '俱乐部赛事')
    },
    matchParts(item) {
      const name = this.matchName(item)
      const cleanName = name.replace(/：/g, ':')
      const main = cleanName.indexOf(':') > -1 ? cleanName.split(':').slice(1).join(':') : cleanName
      const parts = main.split(/vs|VS|Vs|vS|对阵|-/)
      if (parts.length >= 2) {
        return [parts[0].trim() || '主队', parts.slice(1).join('vs').trim() || '客队']
      }
      return ['足球俱乐部', main.trim() || '对手球队']
    },
    matchHome(item) {
      return this.matchParts(item)[0]
    },
    matchAway(item) {
      return this.matchParts(item)[1]
    },
    isEnded(item) {
      const time = item && item.insertTime ? new Date(item.insertTime).getTime() : 0
      return time > 0 && time < Date.now()
    },
    matchScore(item) {
      return this.isEnded(item) ? '0 - 0' : 'vs'
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
        this.latestNotices = data && data.code === 0 && data.data ? (data.data.list || []) : []
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
        this.latestMatches = data && data.code === 0 && data.data ? (data.data.list || []) : []
      }).catch(() => {
        this.latestMatches = []
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.home-page {
  width: 100%;
  max-width: 1440px;
  min-height: calc(100vh - 62px);
  margin: 0 auto;
  padding: 28px 4px 0;
  background: #f3f4f7;
  color: #111827;
  box-sizing: border-box;
}

.page-title {
  margin-bottom: 28px;
  padding: 0 0 22px;
  border-bottom: 1px solid #e5e7eb;
}

.page-title h1 {
  margin: 0 0 6px;
  color: #0f172a;
  font-size: 28px;
  line-height: 1.2;
  font-weight: 800;
}

.page-title p {
  margin: 0;
  color: #64748b;
  font-size: 14px;
  line-height: 1.4;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 24px;
  margin-bottom: 32px;
}

.summary-card {
  min-height: 142px;
  padding: 24px 26px 22px;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  background: #ffffff;
  box-shadow: 0 1px 3px rgba(15, 23, 42, 0.04);
  box-sizing: border-box;
  cursor: pointer;
  transition: border-color 0.18s ease, box-shadow 0.18s ease, transform 0.18s ease;
  position: relative;
  overflow: hidden;
}

.summary-card::after {
  content: '';
  position: absolute;
  top: 0; right: 0;
  width: 80px; height: 80px;
  border-radius: 0 12px 0 80px;
  background: linear-gradient(135deg, transparent 50%, rgba(37, 99, 235, 0.04) 50%);
}

.summary-card:hover {
  border-color: rgba(37, 99, 235, 0.3);
  box-shadow: 0 8px 24px rgba(15, 23, 42, 0.08);
  transform: translateY(-2px);
}

.summary-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 2px;
  color: #0b57d0;
  font-size: 21px;
}

.summary-label {
  display: inline-block;
  margin-left: 10px;
  color: #111827;
  font-size: 14px;
  line-height: 22px;
  font-weight: 700;
  vertical-align: top;
}

.summary-number {
  display: block;
  margin-top: 22px;
  color: #111827;
  font-size: 48px;
  line-height: 1;
  font-weight: 800;
  letter-spacing: 0;
}

.dashboard-panels {
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(0, 1fr);
  gap: 24px;
  margin-bottom: 64px;
}

.panel-card {
  min-height: 318px;
  padding: 26px;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  background: #ffffff;
  box-shadow: 0 1px 3px rgba(15, 23, 42, 0.04);
  box-sizing: border-box;
}

.panel-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 14px;
  padding-bottom: 16px;
  border-bottom: 1px solid #d8dde5;
}

.panel-head h2 {
  margin: 0;
  color: #111827;
  font-size: 23px;
  line-height: 1.25;
  font-weight: 800;
}

.panel-head button {
  border: 0;
  background: transparent;
  color: #0b57d0;
  font-size: 14px;
  line-height: 1;
  cursor: pointer;
}

.notice-list,
.match-list {
  margin: 18px 0 0;
  padding: 0;
  list-style: none;
}

.notice-item,
.match-item {
  cursor: pointer;
}

.notice-item {
  padding: 0 0 15px;
  border-bottom: 1px solid #d8dde5;
}

.notice-item + .notice-item {
  padding-top: 14px;
}

.notice-item:last-child {
  border-bottom: 0;
  padding-bottom: 0;
}

.notice-meta {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
  color: #1f2937;
  font-size: 14px;
  line-height: 1.4;
  font-weight: 700;
}

.notice-meta span {
  display: inline-flex;
  align-items: center;
  min-height: 22px;
  padding: 2px 9px;
  border-radius: 4px;
  background: #e8f0ff;
  color: #163f86;
  font-size: 12px;
}

.notice-meta time {
  color: #1f2937;
  font-weight: 700;
}

.notice-item p {
  margin: 0;
  color: #111827;
  font-size: 16px;
  line-height: 1.5;
  word-break: break-word;
}

.match-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 0 0 15px;
  border-bottom: 1px solid #d8dde5;
}

.match-item + .match-item {
  padding-top: 14px;
}

.match-item:last-child {
  border-bottom: 0;
  padding-bottom: 0;
}

.match-main {
  min-width: 0;
}

.match-time {
  margin-bottom: 6px;
  color: #374151;
  font-size: 14px;
  line-height: 1.35;
  font-weight: 700;
}

.match-teams {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
  color: #111827;
  font-size: 17px;
  line-height: 1.35;
}

.match-teams strong {
  max-width: 190px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-weight: 800;
}

.match-teams span {
  color: #0b57d0;
  font-weight: 800;
}

.match-status {
  flex: 0 0 auto;
  min-width: 62px;
  padding: 5px 10px;
  border-radius: 999px;
  background: #edf0f4;
  color: #374151;
  text-align: center;
  font-size: 13px;
  line-height: 1;
  font-weight: 700;
}

.match-status.ended {
  background: #536179;
  color: #ffffff;
}

.empty-block {
  margin-top: 24px;
  padding: 30px 18px;
  border: 1px dashed #cfd5df;
  border-radius: 10px;
  color: #6b7280;
  text-align: center;
  font-size: 15px;
}

.home-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 18px;
  margin: 0 -4px;
  padding: 28px;
  border-top: 1px solid #e5e7eb;
  background: #fff;
  border-radius: 0 0 12px 12px;
  color: #64748b;
  font-size: 13px;
}

.home-footer strong {
  color: #334155;
  font-size: 15px;
}

.home-footer span {
  color: #94a3b8;
}

@media (max-width: 1200px) {
  .summary-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .dashboard-panels {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 720px) {
  .home-page {
    padding: 20px 2px 0;
  }

  .page-title {
    margin-bottom: 28px;
  }

  .page-title h1 {
    font-size: 28px;
  }

  .summary-grid {
    grid-template-columns: 1fr;
    gap: 14px;
    margin-bottom: 24px;
  }

  .summary-card {
    min-height: 132px;
    padding: 22px;
  }

  .summary-number {
    font-size: 44px;
  }

  .dashboard-panels {
    gap: 14px;
    margin-bottom: 42px;
  }

  .panel-card {
    padding: 22px;
  }

  .match-item {
    align-items: flex-start;
    flex-direction: column;
  }

  .match-teams strong {
    max-width: 100%;
  }

  .home-footer {
    align-items: flex-start;
    flex-direction: column;
    margin: 0 -2px;
    padding: 24px 14px;
  }
}
</style>
