<template>
  <div class="home-page">
    <section class="ops-strip">
      <article
        v-for="item in summaryCards"
        :key="item.key"
        class="metric-tile"
        @click="go(item.route)"
      >
        <span>{{ item.label }}</span>
        <strong>{{ item.total }}</strong>
        <em>进入管理</em>
      </article>
    </section>

    <section class="workbench-grid">
      <article class="work-panel main-panel">
        <div class="panel-head">
          <div>
            <h2>运营工作流</h2>
            <p>按最近更新汇总赛事、训练、合同和公告</p>
          </div>
          <button type="button" @click="refreshWorkbench">刷新</button>
        </div>

        <ul v-if="workItems.length" class="work-list">
          <li v-for="item in workItems" :key="item.key" @click="openWorkItem(item)">
            <span class="work-type">{{ item.type }}</span>
            <div class="work-main">
              <strong>{{ item.title }}</strong>
              <p>{{ item.desc }}</p>
            </div>
            <time>{{ item.time }}</time>
            <b :class="{ muted: item.muted }">{{ item.status }}</b>
          </li>
        </ul>
        <div v-else class="empty-block">暂无运营动态</div>
      </article>

      <aside class="work-side">
        <article class="work-panel">
          <div class="panel-head compact">
            <div>
              <h2>快捷操作</h2>
              <p>高频维护入口</p>
            </div>
          </div>
          <div class="quick-grid">
            <button v-for="item in quickActions" :key="item.path" type="button" @click="go(item.path)">
              <strong>{{ item.label }}</strong>
              <span>{{ item.desc }}</span>
            </button>
          </div>
        </article>

        <article class="work-panel">
          <div class="panel-head compact">
            <div>
              <h2>重点关注</h2>
              <p>当前需要优先扫一眼的状态</p>
            </div>
          </div>
          <ul class="focus-list">
            <li v-for="item in focusItems" :key="item.label" @click="go(item.route)">
              <span>{{ item.label }}</span>
              <strong>{{ item.value }}</strong>
              <em>{{ item.tip }}</em>
            </li>
          </ul>
        </article>
      </aside>
    </section>

    <section class="detail-grid">
      <article class="work-panel">
        <div class="panel-head compact">
          <div>
            <h2>最新赛事</h2>
            <p>用于安排赛程和赛后维护</p>
          </div>
          <button type="button" @click="go('/saishi')">赛程表</button>
        </div>
        <ul v-if="latestMatches.length" class="compact-list">
          <li v-for="item in latestMatches.slice(0, 4)" :key="item.id" @click="openMatchDetail(item)">
            <strong>{{ matchHome(item) }} {{ matchScore(item) }} {{ matchAway(item) }}</strong>
            <span>{{ matchTime(item) }} / {{ item.saishiValue || '赛事' }}</span>
          </li>
        </ul>
        <div v-else class="empty-block">暂无赛事数据</div>
      </article>

      <article class="work-panel">
        <div class="panel-head compact">
          <div>
            <h2>最新公告</h2>
            <p>面向俱乐部日常通知</p>
          </div>
          <button type="button" @click="go('/gonggao')">查看全部</button>
        </div>
        <ul v-if="latestNotices.length" class="compact-list">
          <li v-for="item in latestNotices.slice(0, 4)" :key="item.id" @click="openNoticeDetail(item)">
            <strong>{{ item.gonggaoName || noticeText(item) }}</strong>
            <span>{{ formatDate(item.insertTime) }} / {{ item.gonggaoValue || '公告' }}</span>
          </li>
        </ul>
        <div v-else class="empty-block">暂无公告数据</div>
      </article>
    </section>
  </div>
</template>

<script>
const CARD_CONFIG = [
  { key: 'users', label: '管理员', route: '/users', url: 'users/page', extra: {} },
  { key: 'jiaolian', label: '教练', route: '/jiaolian', url: 'jiaolian/page', extra: {} },
  { key: 'yonghu', label: '球员', route: '/yonghu', url: 'yonghu/page', extra: {} },
  { key: 'saishi', label: '赛事', route: '/saishi', url: 'saishi/page', extra: { saishiDelete: 1 } },
  { key: 'hetong', label: '合同', route: '/hetong', url: 'hetong/page', extra: { hetongDelete: 1 } },
  { key: 'xunlian', label: '训练', route: '/xunlian', url: 'xunlian/page', extra: { xunlianDelete: 1 } },
  { key: 'shuju', label: '数据', route: '/shuju', url: 'shuju/page', extra: { shujuDelete: 1 } },
  { key: 'gonggao', label: '公告', route: '/gonggao', url: 'gonggao/page', extra: {} }
]

export default {
  data() {
    return {
      summaryCards: CARD_CONFIG.map(item => Object.assign({}, item, { total: '-' })),
      latestNotices: [],
      latestMatches: [],
      latestTrainings: [],
      latestContracts: [],
      quickActions: [
        { label: '新增赛事', desc: '维护赛程与赛况', path: '/saishi' },
        { label: '新增训练', desc: '安排训练计划', path: '/xunlian' },
        { label: '发布公告', desc: '同步俱乐部通知', path: '/gonggao' },
        { label: '录入数据', desc: '维护球员指标', path: '/shuju' }
      ]
    }
  },
  computed: {
    workItems() {
      const matches = this.latestMatches.map(item => ({
        key: `match-${item.id}`,
        type: '赛事',
        title: this.matchName(item),
        desc: `${item.saishiAddress || '未填写地点'} / ${item.saishiValue || '赛事'}`,
        time: this.matchTime(item),
        status: this.isEnded(item) ? '已结束' : '待开赛',
        muted: this.isEnded(item),
        route: '/saishi',
        row: item
      }))

      const trainings = this.latestTrainings.map(item => ({
        key: `training-${item.id}`,
        type: '训练',
        title: item.xunlianName || '训练计划',
        desc: `${item.yonghuName || '未指定球员'} / ${item.xunlianKemu || item.xunlianValue || '训练科目'}`,
        time: this.formatDate(item.xunlianTime || item.insertTime),
        status: item.xunlianValue || '计划',
        route: '/xunlian',
        row: item
      }))

      const contracts = this.latestContracts.map(item => ({
        key: `contract-${item.id}`,
        type: '合同',
        title: item.hetongName || '合同记录',
        desc: `${item.yonghuName || '未关联球员'} / ${item.yonghuPhone || '无手机号'}`,
        time: this.formatDate(item.createTime),
        status: item.hetongFile ? '有附件' : '待补充',
        muted: !item.hetongFile,
        route: '/hetong',
        row: item
      }))

      const notices = this.latestNotices.map(item => ({
        key: `notice-${item.id}`,
        type: '公告',
        title: item.gonggaoName || this.noticeText(item),
        desc: this.noticeText(item),
        time: this.formatDate(item.insertTime),
        status: item.gonggaoValue || '通知',
        route: '/gonggao',
        row: item
      }))

      return matches.concat(trainings, contracts, notices).slice(0, 9)
    },
    focusItems() {
      const pendingMatches = this.latestMatches.filter(item => !this.isEnded(item)).length
      return [
        { label: '待开赛赛事', value: pendingMatches, tip: '进入赛程维护', route: '/saishi' },
        { label: '近期训练', value: this.latestTrainings.length, tip: '查看训练安排', route: '/xunlian' },
        { label: '合同跟进', value: this.latestContracts.length, tip: '核对合同附件', route: '/hetong' },
        { label: '公告更新', value: this.latestNotices.length, tip: '检查发布内容', route: '/gonggao' }
      ]
    }
  },
  created() {
    this.refreshWorkbench()
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
      const name = this.matchName(item).replace(/：/g, ':')
      const main = name.indexOf(':') > -1 ? name.split(':').slice(1).join(':') : name
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
    openWorkItem(item) {
      if (!item || !item.row) {
        this.go(item.route || '/index')
        return
      }
      if (item.route === '/saishi') {
        this.openMatchDetail(item.row)
        return
      }
      if (item.route === '/gonggao') {
        this.openNoticeDetail(item.row)
        return
      }
      this.go(item.route)
    },
    openMatchDetail(item) {
      if (!item || !item.id) {
        this.go('/saishi')
        return
      }
      this.$router.push({
        path: '/saishi',
        query: {
          openId: String(item.id),
          openType: 'info'
        }
      })
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
    refreshWorkbench() {
      this.loadStats()
      this.loadLatestNotices()
      this.loadLatestMatches()
      this.loadLatestTrainings()
      this.loadLatestContracts()
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
        params: { page: 1, limit: 5, sort: 'id', order: 'desc' }
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
        params: { page: 1, limit: 5, sort: 'id', order: 'desc', saishiDelete: 1 }
      }).then(({ data }) => {
        this.latestMatches = data && data.code === 0 && data.data ? (data.data.list || []) : []
      }).catch(() => {
        this.latestMatches = []
      })
    },
    loadLatestTrainings() {
      this.$http({
        url: 'xunlian/page',
        method: 'get',
        params: { page: 1, limit: 5, sort: 'id', order: 'desc', xunlianDelete: 1 }
      }).then(({ data }) => {
        this.latestTrainings = data && data.code === 0 && data.data ? (data.data.list || []) : []
      }).catch(() => {
        this.latestTrainings = []
      })
    },
    loadLatestContracts() {
      this.$http({
        url: 'hetong/page',
        method: 'get',
        params: { page: 1, limit: 5, sort: 'id', order: 'desc', hetongDelete: 1 }
      }).then(({ data }) => {
        this.latestContracts = data && data.code === 0 && data.data ? (data.data.list || []) : []
      }).catch(() => {
        this.latestContracts = []
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.home-page {
  width: 100%;
  max-width: 1480px;
  min-height: calc(100vh - 62px);
  margin: 0 auto;
  padding: 8px 0 0;
  background: #f3f4f7;
  color: #111827;
  box-sizing: border-box;
}

.ops-strip {
  display: grid;
  grid-template-columns: repeat(8, minmax(0, 1fr));
  gap: 10px;
  margin-bottom: 12px;
}

.metric-tile,
.work-panel {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  background: #ffffff;
  box-shadow: 0 1px 3px rgba(15, 23, 42, 0.04);
  box-sizing: border-box;
}

.metric-tile {
  min-height: 84px;
  padding: 12px 13px;
  cursor: pointer;
  transition: border-color 0.18s ease, box-shadow 0.18s ease;
}

.metric-tile:hover {
  border-color: rgba(37, 99, 235, 0.3);
  box-shadow: 0 6px 18px rgba(15, 23, 42, 0.07);
}

.metric-tile span,
.metric-tile em {
  display: block;
  color: #64748b;
  font-size: 12px;
  font-style: normal;
  line-height: 1.3;
}

.metric-tile strong {
  display: block;
  margin: 4px 0 5px;
  color: #111827;
  font-size: 28px;
  line-height: 1;
  font-weight: 800;
}

.metric-tile em {
  color: #0b57d0;
}

.workbench-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 360px;
  gap: 12px;
  margin-bottom: 12px;
}

.work-side {
  display: grid;
  gap: 12px;
}

.work-panel {
  padding: 16px;
}

.main-panel {
  min-height: 470px;
}

.panel-head {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
  padding-bottom: 12px;
  border-bottom: 1px solid #e5e7eb;
}

.panel-head.compact {
  padding-bottom: 10px;
}

.panel-head h2 {
  margin: 0 0 4px;
  color: #111827;
  font-size: 18px;
  line-height: 1.25;
  font-weight: 800;
}

.panel-head p {
  margin: 0;
  color: #64748b;
  font-size: 12px;
  line-height: 1.35;
}

.panel-head button {
  height: 30px;
  padding: 0 12px;
  border: 1px solid #d8dde5;
  border-radius: 6px;
  background: #ffffff;
  color: #0b57d0;
  font-size: 12px;
  font-weight: 700;
  cursor: pointer;
}

.work-list,
.compact-list,
.focus-list {
  margin: 0;
  padding: 0;
  list-style: none;
}

.work-list li {
  display: grid;
  grid-template-columns: 54px minmax(0, 1fr) 116px 68px;
  align-items: center;
  gap: 12px;
  min-height: 58px;
  padding: 10px 0;
  border-bottom: 1px solid #edf0f4;
  cursor: pointer;
}

.work-list li:last-child,
.compact-list li:last-child,
.focus-list li:last-child {
  border-bottom: 0;
}

.work-type {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 24px;
  border-radius: 5px;
  background: #e8f0ff;
  color: #163f86;
  font-size: 12px;
  font-weight: 800;
}

.work-main {
  min-width: 0;
}

.work-main strong,
.compact-list strong {
  display: block;
  overflow: hidden;
  color: #111827;
  font-size: 14px;
  line-height: 1.35;
  font-weight: 800;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.work-main p,
.compact-list span {
  display: block;
  overflow: hidden;
  margin: 4px 0 0;
  color: #64748b;
  font-size: 12px;
  line-height: 1.35;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.work-list time {
  color: #475569;
  font-size: 12px;
  font-weight: 700;
}

.work-list b {
  padding: 5px 8px;
  border-radius: 999px;
  background: #e8f0ff;
  color: #163f86;
  text-align: center;
  font-size: 12px;
  line-height: 1;
}

.work-list b.muted {
  background: #edf0f4;
  color: #475569;
}

.quick-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 8px;
  padding-top: 12px;
}

.quick-grid button {
  min-height: 70px;
  padding: 10px;
  border: 1px solid #e5e7eb;
  border-radius: 7px;
  background: #ffffff;
  text-align: left;
  cursor: pointer;
}

.quick-grid button:hover {
  border-color: rgba(37, 99, 235, 0.35);
}

.quick-grid strong,
.quick-grid span {
  display: block;
}

.quick-grid strong {
  color: #111827;
  font-size: 14px;
  line-height: 1.35;
}

.quick-grid span {
  margin-top: 5px;
  color: #64748b;
  font-size: 12px;
  line-height: 1.35;
}

.focus-list {
  padding-top: 8px;
}

.focus-list li {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 46px;
  gap: 4px 10px;
  padding: 10px 0;
  border-bottom: 1px solid #edf0f4;
  cursor: pointer;
}

.focus-list span {
  color: #111827;
  font-size: 13px;
  font-weight: 700;
}

.focus-list strong {
  grid-row: span 2;
  color: #111827;
  text-align: right;
  font-size: 24px;
  line-height: 1;
}

.focus-list em {
  color: #64748b;
  font-size: 12px;
  font-style: normal;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 12px;
  padding-bottom: 20px;
}

.compact-list {
  padding-top: 8px;
}

.compact-list li {
  padding: 10px 0;
  border-bottom: 1px solid #edf0f4;
  cursor: pointer;
}

.empty-block {
  margin-top: 14px;
  padding: 24px 14px;
  border: 1px dashed #cfd5df;
  border-radius: 8px;
  color: #64748b;
  text-align: center;
  font-size: 13px;
}

@media (max-width: 1280px) {
  .ops-strip {
    grid-template-columns: repeat(4, minmax(0, 1fr));
  }

  .workbench-grid {
    grid-template-columns: 1fr;
  }

  .work-side {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 760px) {
  .ops-strip,
  .detail-grid,
  .work-side {
    grid-template-columns: 1fr;
  }

  .work-list li {
    grid-template-columns: 46px minmax(0, 1fr);
  }

  .work-list time,
  .work-list b {
    grid-column: 2;
  }
}
</style>
