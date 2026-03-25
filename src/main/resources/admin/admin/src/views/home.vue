<template>
  <div class="home-page">
    <section class="hero-panel" :class="{ loaded }">
      <div class="hero-head">
        <div class="hero-copy">
          <p class="hero-tag">首页总览</p>
          <h1>俱乐部运营中心</h1>
          <p class="hero-subtitle">{{ projectName }}</p>
          <p class="hero-desc">
            {{ greeting }}，{{ currentUser || '管理员' }}。今天是 {{ nowDate }}，系统运行正常，
            可在此查看公告、赛事、合同、训练计划与首页轮播。
          </p>
        </div>

        <div class="hero-side">
          <div class="hero-side-card">
            <span>当前时间</span>
            <strong>{{ nowTime }}</strong>
          </div>
          <div class="hero-side-card compact">
            <span>当前角色</span>
            <strong>{{ currentRole || '管理员' }}</strong>
          </div>
        </div>
      </div>

      <div class="hero-actions">
        <el-button type="primary" class="primary-btn" @click="go('/gonggao')">发布公告</el-button>
        <el-button class="secondary-btn" @click="go('/config')">管理轮播图</el-button>
      </div>

      <div class="hero-points">
        <div class="hero-point">
          <i class="el-icon-trophy"></i>
          <span>统一掌握赛事与训练安排</span>
        </div>
        <div class="hero-point">
          <i class="el-icon-document"></i>
          <span>合同、公告与数据集中管理</span>
        </div>
        <div class="hero-point">
          <i class="el-icon-data-analysis"></i>
          <span>首页即可查看关键模块状态</span>
        </div>
      </div>
    </section>

    <section class="summary-grid">
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
            <li v-for="item in latestNotices.slice(0, 5)" :key="item.id" @click="openNoticeDetail(item)">
              <div class="notice-badge">公告</div>
              <div class="notice-text">
                <strong>{{ item.gonggaoName || '未命名公告' }}</strong>
                <p>{{ item.gonggaoValue || '系统公告' }}</p>
              </div>
              <div class="notice-time">
                <span>{{ formatDate(item.insertTime) }}</span>
              </div>
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

          <div v-if="latestMatches.length" class="status-list">
            <div v-for="item in latestMatches" :key="item.id" class="status-item" @click="openMatchDetail(item)">
              <div class="status-copy">
                <span>{{ item.saishiName || '未命名赛事' }}</span>
                <small>地点：{{ item.saishiAddress || '待补充' }}</small>
              </div>
              <div class="status-side">
             
                <strong>{{ item.saishiValue || '赛事' }}</strong>
              </div>
            </div>
          </div>
          <div v-else class="empty-block compact-empty">暂无赛事数据</div>
        </article>
      </div>

        <article class="glass-card quick-card-panel">
          <div class="panel-head">
            <div>
              <h3>快捷入口</h3>
              <p>常用入口与核心业务模块</p>
            </div>
          </div>

        <div v-if="utilityQuickLinks.length" class="utility-row">
          <button
            v-for="item in utilityQuickLinks"
            :key="item.path"
            class="utility-chip"
            @click="go(item.path)"
          >
            <i :class="item.icon"></i>
            <span>{{ item.name }}</span>
          </button>
        </div>

        <div class="quick-grid">
          <button
            v-for="(item, index) in featuredQuickLinks"
            :key="item.path"
            class="quick-entry"
            :style="{ '--quick-delay': index * 0.03 + 's', '--quick-tint': item.tint }"
            @click="go(item.path)"
          >
            <div class="quick-entry-main">
              <i :class="item.icon"></i>
              <div class="quick-copy">
                <span>{{ item.name }}</span>
                <small>{{ item.desc }}</small>
              </div>
            </div>
            <em v-if="item.badge">{{ item.badge }}</em>
          </button>
        </div>
      </article>

      <article class="glass-card banner-card">
        <div class="panel-head">
          <div>
            <h3>轮播图预览</h3>
            <p>查看首页轮播效果与图片顺序</p>
          </div>
          <button class="panel-link" @click="go('/config')">轮播管理</button>
        </div>

        <template v-if="bannerSlides.length">
          <el-carousel height="320px" indicator-position="outside" :interval="4200" arrow="always">
            <el-carousel-item v-for="(item, index) in bannerSlides" :key="(item.groupId || item.link || 'banner') + '-' + index">
              <div class="banner-item banner-clickable" @click="openBannerDetail(item)">
                <img :src="item.url" :alt="item.name">
                <div class="banner-mask">
                  <div class="spotlight-card">
                    <span class="banner-index">{{ item.kicker || ('首页焦点 ' + (index + 1)) }}</span>
                    <h4>{{ item.name || '轮播图' }}</h4>
                    <p>{{ item.desc || '用于首页主视觉展示，可统一管理图片与展示顺序。' }}</p>
                    <button class="spotlight-btn" @click.stop="openBannerDetail(item)">{{ item.actionText || '管理本组轮播' }}</button>
                  </div>
                </div>
              </div>
            </el-carousel-item>
          </el-carousel>
        </template>
        <div v-else class="empty-block">暂无轮播图数据</div>
      </article>
    </section>
  </div>
</template>

<script>
import menu from '@/utils/menu'

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

const MENU_NAME_MAP = {
  users: '管理员管理',
  jiaolian: '教练管理',
  yonghu: '用户管理',
  hetong: '合同管理',
  saishi: '赛事管理',
  shuju: '球员数据管理',
  xunlian: '训练计划管理',
  gonggao: '公告信息管理',
  dictionary: '基础数据管理',
  dictionaryGonggao: '公告类型管理',
  dictionarySaishi: '赛事类型管理',
  dictionarySex: '性别类型管理',
  dictionaryShuju: '球员数据类型管理',
  dictionaryXunlian: '训练计划类型管理',
  config: '轮播图信息'
}

const QUICK_ICON_MAP = {
  '/index': 'el-icon-s-home',
  '/center': 'el-icon-user',
  '/updatePassword': 'el-icon-lock',
  '/users': 'el-icon-s-custom',
  '/jiaolian': 'el-icon-user-solid',
  '/yonghu': 'el-icon-user',
  '/hetong': 'el-icon-document',
  '/saishi': 'el-icon-trophy',
  '/shuju': 'el-icon-data-analysis',
  '/xunlian': 'el-icon-date',
  '/gonggao': 'el-icon-bell',
  '/config': 'el-icon-picture-outline'
}

const QUICK_META_MAP = {
  '/index': { desc: '返回首页总览', tint: '13,139,242', type: 'utility', order: 90 },
  '/center': { desc: '查看并完善个人资料', tint: '59,130,246', type: 'utility', order: 91 },
  '/updatePassword': { desc: '维护账号安全设置', tint: '99,102,241', type: 'utility', order: 92 },
  '/users': { desc: '维护后台管理员账号与权限', tint: '14,165,233', type: 'core', order: 10, badge: '权限' },
  '/jiaolian': { desc: '管理教练资料与任职信息', tint: '16,185,129', type: 'core', order: 20, badge: '人员' },
  '/yonghu': { desc: '查看用户档案与基础信息', tint: '245,158,11', type: 'core', order: 30, badge: '成员' },
  '/hetong': { desc: '处理合同记录与签约情况', tint: '20,184,166', type: 'core', order: 40, badge: '合同' },
  '/saishi': { desc: '安排赛事计划与赛程信息', tint: '239,68,68', type: 'core', order: 50, badge: '赛事' },
  '/shuju': { desc: '追踪球员数据与表现统计', tint: '249,115,22', type: 'core', order: 60, badge: '数据' },
  '/xunlian': { desc: '维护训练计划与执行安排', tint: '6,182,212', type: 'core', order: 70, badge: '训练' },
  '/gonggao': { desc: '发布公告并同步重要通知', tint: '59,130,246', type: 'core', order: 80, badge: '公告' },
  '/config': { desc: '调整首页轮播与展示内容', tint: '99,102,241', type: 'core', order: 85, badge: '展示' }
}

export default {
  data() {
    return {
      loaded: false,
      timer: null,
      nowDate: '',
      nowTime: '',
      summaryCards: CARD_CONFIG.map(item => Object.assign({}, item, { total: '-' })),
      latestNotices: [],
      bannerSlides: [],
      realmadridNews: [],
      latestMatches: [],
      quickLinks: []
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
    featuredQuickLinks() {
      return this.quickLinks.filter(item => item.type !== 'utility')
    },
    utilityQuickLinks() {
      return this.quickLinks.filter(item => item.type === 'utility')
    },
    realmadridBannerSlides() {
      return this.realmadridNews
        .filter(item => item && item.image)
        .slice(0, 4)
        .map((item, index) => ({
          isExternal: true,
          link: item.link,
          name: item.title || '皇家马德里动态',
          url: item.image,
          kicker: index === 0 ? '皇家马德里焦点' : '皇家马德里动态',
          desc: '优先使用皇家马德里官网官方图片，作为后台首页轮播预览展示内容。',
          actionText: '查看官网原文'
        }))
    }
  },
  created() {
    this.updateClock()
    this.timer = setInterval(this.updateClock, 1000)
    this.loadQuickLinks()
    this.loadStats()
    this.loadLatestNotices()
    this.loadLatestMatches()
    this.loadRealmadridNews().finally(() => {
      this.loadBanners()
    })
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
    normalizeMenuName(name, tableName) {
      const text = String(name || '').trim()
      if (!text || /[\uFFFD]/.test(text)) {
        return MENU_NAME_MAP[tableName] || tableName
      }
      return text
    },
    loadQuickLinks() {
      const links = [
        { name: '首页', path: '/index' },
        { name: '个人信息', path: '/center' },
        { name: '修改密码', path: '/updatePassword' }
      ]
      const menuList = typeof menu.list === 'function' ? menu.list() : []
      const role = this.currentRole
      let roleMenu = menuList.find(item => item.roleName === role)
      if (!roleMenu && menuList.length) {
        roleMenu = menuList[0]
      }

      const appendLink = (name, tableName) => {
        if (!tableName) return
        const path = '/' + tableName
        if (links.some(item => item.path === path)) return
        links.push({
          name: this.normalizeMenuName(name, tableName),
          path
        })
      }

      if (roleMenu && Array.isArray(roleMenu.backMenu)) {
        roleMenu.backMenu.forEach(group => {
          if (Array.isArray(group.child) && group.child.length) {
            group.child.forEach(child => {
              appendLink(child.menu, child.tableName)
            })
          } else {
            appendLink(group.menu, group.tableName)
          }
        })
      }

      this.quickLinks = links
        .map(item => this.buildQuickLink(item))
        .sort((a, b) => a.order - b.order)
        .slice(0, 10)
    },
    buildQuickLink(item) {
      const meta = QUICK_META_MAP[item.path] || {}
      return {
        name: item.name,
        path: item.path,
        icon: QUICK_ICON_MAP[item.path] || 'el-icon-s-grid',
        desc: meta.desc || '进入该模块查看详细内容',
        badge: meta.badge || '',
        tint: meta.tint || '13,139,242',
        type: meta.type || 'core',
        order: typeof meta.order === 'number' ? meta.order : 999
      }
    },
    statValue(key) {
      const item = this.summaryCards.find(card => card.key === key)
      return item ? item.total : '-'
    },
    formatDate(value) {
      if (!value) return '--'
      const text = String(value)
      return text.length > 16 ? text.slice(0, 16) : text
    },
    toImageUrl(path) {
      if (!path) return ''
      return /^https?:\/\//i.test(path) ? path : this.$base.url + path
    },
    parseImages(value) {
      if (!value) return []
      return String(value)
        .split(',')
        .map(item => item.trim())
        .filter(Boolean)
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
    openBannerDetail(item) {
      if (item && item.isExternal && item.link) {
        window.open(item.link, '_blank')
        return
      }
      if (!item || !item.groupId) {
        this.go('/config')
        return
      }
      this.$storage.set('pendingConfigOpenId', item.groupId)
      this.$storage.set('pendingConfigOpenType', 'info')
      this.$router.push({
        path: '/config',
        query: {
          openId: String(item.groupId),
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
    },
    loadRealmadridNews() {
      return this.$http({
        url: 'external/realmadrid/news',
        method: 'get',
        params: {
          limit: 4
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.realmadridNews = data.data || []
        } else {
          this.realmadridNews = []
        }
      }).catch(() => {
        this.realmadridNews = []
      })
    },
    loadBanners() {
      this.$http({
        url: 'config/page',
        method: 'get',
        params: {
          page: 1,
          limit: 100,
          sort: 'id',
          order: 'desc'
        }
      }).then(({ data }) => {
        if (!(data && data.code === 0 && data.data)) {
          this.bannerSlides = this.realmadridBannerSlides.slice()
          return
        }

        const slides = []
        ;(data.data.list || []).forEach(row => {
          this.parseImages(row.value).forEach(path => {
            slides.push({
              groupId: row.id,
              name: row.name || '轮播图',
              url: this.toImageUrl(path)
            })
          })
        })
        this.bannerSlides = this.realmadridBannerSlides.length ? this.realmadridBannerSlides.slice() : slides
      }).catch(() => {
        this.bannerSlides = this.realmadridBannerSlides.slice()
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.home-page {
  --space-xs: 8px;
  --space-sm: 12px;
  --space-md: 16px;
  --space-lg: 20px;
  --space-xl: 24px;
  --card-hover-transform: translateY(-4px);
  --card-hover-shadow: 0 18px 30px rgba(16, 63, 104, 0.12);
  --card-hover-border: rgba(13, 139, 242, 0.24);
  --hero-title-size: 38px;
  --section-title-size: 20px;
  --card-number-size: 24px;
  --body-text-size: 15px;
  --meta-text-size: 13px;
  --section-gap: 24px;
  min-height: calc(100vh - 130px);
  padding: 20px;
  border-radius: 28px;
  position: relative;
  overflow: hidden;
  background-image:
    linear-gradient(120deg, rgba(9, 34, 64, 0.18), rgba(17, 104, 173, 0.12)),
    url(/zuqiujulebguanli/img/back-img-bg.jpg);
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;
}

.home-page::before,
.home-page::after {
  content: '';
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
}

.home-page::before {
  width: 340px;
  height: 340px;
  left: -120px;
  top: -100px;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.34), rgba(255, 255, 255, 0));
}

.home-page::after {
  width: 420px;
  height: 420px;
  right: -160px;
  bottom: -150px;
  background: radial-gradient(circle, rgba(255, 138, 61, 0.2), rgba(255, 138, 61, 0));
}

.hero-panel,
.content-stack,
.summary-grid {
  position: relative;
  z-index: 1;
}

.hero-panel {
  padding: 28px;
  border-radius: 28px;
  color: #f7fbff;
  background:
    linear-gradient(160deg, rgba(4, 29, 55, 0.94), rgba(10, 67, 123, 0.86)),
    repeating-linear-gradient(-45deg, rgba(255, 255, 255, 0.04) 0 8px, rgba(255, 255, 255, 0) 8px 16px);
  border: 1px solid rgba(255, 255, 255, 0.28);
  box-shadow: 0 28px 60px rgba(9, 20, 40, 0.18);
  backdrop-filter: blur(10px);
  opacity: 0;
  transform: translateY(16px);
  transition: all 0.45s ease;
}

.hero-panel.loaded {
  opacity: 1;
  transform: translateY(0);
}

.hero-head {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: var(--space-xl);
}

.hero-copy {
  max-width: 880px;
  min-width: 0;
}

.hero-tag {
  margin: 0;
  font-size: 12px;
  letter-spacing: 2px;
  opacity: 0.84;
}

.hero-panel h1 {
  margin: 12px 0 8px;
  font-size: var(--hero-title-size);
  line-height: 1.14;
}

.hero-subtitle {
  margin: 0;
  color: #bfdcff;
  font-size: 17px;
}

.hero-desc {
  margin: 14px 0 0;
  line-height: 1.85;
  font-size: var(--body-text-size);
  color: rgba(247, 251, 255, 0.9);
}

.hero-side {
  display: grid;
  gap: var(--space-sm);
  min-width: 240px;
}

.hero-side-card {
  padding: 16px;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.11);
  border: 1px solid rgba(255, 255, 255, 0.16);
  text-align: right;
  backdrop-filter: blur(14px);
}

.hero-side-card span {
  display: block;
  color: #dcedff;
  font-size: 13px;
}

.hero-side-card strong {
  display: block;
  margin-top: 6px;
  color: #fff;
  font-size: 30px;
  line-height: 1;
}

.hero-side-card.compact strong {
  font-size: 20px;
}

.hero-actions {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-sm);
  margin-top: var(--space-lg);
}

.primary-btn,
.secondary-btn {
  min-width: 132px;
  height: 44px;
  border-radius: 14px;
  padding: 0 20px;
  font-size: 14px;
  font-weight: 700;
  transition: transform 0.24s ease, box-shadow 0.24s ease, background 0.24s ease, border-color 0.24s ease;
  backdrop-filter: blur(12px);
}

.primary-btn {
  border: none;
  background: linear-gradient(135deg, #0d8bf2, #0a63c9);
  box-shadow: 0 12px 22px rgba(13, 139, 242, 0.28);
}

.primary-btn:hover,
.primary-btn:focus {
  transform: translateY(-2px);
  box-shadow: 0 18px 32px rgba(13, 139, 242, 0.34);
}

.secondary-btn {
  border: 1px solid rgba(255, 255, 255, 0.34);
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
}

.secondary-btn:hover,
.secondary-btn:focus {
  transform: translateY(-2px);
  border-color: rgba(255, 255, 255, 0.58);
  background: rgba(255, 255, 255, 0.18);
  box-shadow: 0 16px 28px rgba(4, 29, 55, 0.2);
  color: #fff;
}

.hero-points {
  margin-top: var(--space-lg);
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: var(--space-sm);
}

.hero-point {
  display: flex;
  align-items: center;
  gap: 10px;
  min-height: 58px;
  padding: 12px 14px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.08);
  font-size: 14px;
  line-height: 1.5;
  backdrop-filter: blur(10px);
}

.hero-point i {
  width: 30px;
  height: 30px;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.16);
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: var(--space-md);
  margin-top: var(--section-gap);
}

.summary-card {
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  min-height: 96px;
  padding: 16px;
  border-radius: 18px;
  border: 1px solid #d9e7f7;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.9), rgba(244, 249, 255, 0.82));
  box-shadow: 0 12px 22px rgba(15, 63, 106, 0.06);
  cursor: pointer;
  opacity: 0;
  transform: translateY(14px);
  animation: rise-in 0.4s ease forwards;
  animation-delay: var(--delay);
  transition: transform 0.24s ease, box-shadow 0.24s ease, border-color 0.24s ease;
  backdrop-filter: blur(12px);
}

.summary-card:hover {
  transform: var(--card-hover-transform);
  border-color: var(--card-hover-border);
  box-shadow: var(--card-hover-shadow);
}

.summary-card::after {
  content: '';
  position: absolute;
  top: -28px;
  right: -20px;
  width: 94px;
  height: 94px;
  border-radius: 50%;
  background: rgba(var(--accent-rgb), 0.14);
}

.summary-icon {
  position: relative;
  z-index: 1;
  width: 46px;
  height: 46px;
  border-radius: 14px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  background: var(--accent);
  color: #fff;
  font-size: 20px;
  box-shadow: 0 10px 18px rgba(var(--accent-rgb), 0.2);
}

.summary-info {
  position: relative;
  z-index: 1;
}

.summary-info span {
  display: block;
  color: #607a95;
  font-size: var(--meta-text-size);
  line-height: 1.45;
}

.summary-info strong {
  display: block;
  margin-top: 4px;
  color: #163e64;
  font-size: var(--card-number-size);
  line-height: 1.2;
}

.content-stack {
  display: grid;
  gap: var(--section-gap);
  margin-top: var(--section-gap);
}

.dual-grid {
  display: grid;
  grid-template-columns: minmax(0, 1.1fr) minmax(300px, 0.85fr);
  gap: var(--space-md);
  align-items: start;
}

.glass-card {
  padding: 22px;
  border-radius: 22px;
  border: 1px solid rgba(214, 228, 245, 0.95);
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.74), rgba(244, 249, 255, 0.66));
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.55), 0 20px 42px rgba(13, 44, 78, 0.08);
  backdrop-filter: blur(16px);
  transition: transform 0.24s ease, box-shadow 0.24s ease, border-color 0.24s ease;
}

.glass-card:hover {
  transform: var(--card-hover-transform);
  border-color: var(--card-hover-border);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.55), var(--card-hover-shadow);
}

.panel-head {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: var(--space-sm);
  margin-bottom: 18px;
}

.panel-head h3 {
  margin: 0;
  color: #163f65;
  font-size: var(--section-title-size);
}

.panel-head p {
  margin: 6px 0 0;
  color: #6b86a1;
  font-size: var(--meta-text-size);
  line-height: 1.6;
}

.panel-link {
  border: none;
  background: rgba(13, 139, 242, 0.08);
  color: #0d8bf2;
  border-radius: 999px;
  height: 36px;
  padding: 0 14px;
  font-size: 13px;
  font-weight: 700;
  white-space: nowrap;
  cursor: pointer;
  transition: all 0.24s ease;
}

.panel-link:hover {
  transform: translateY(-2px);
  color: #fff;
  background: linear-gradient(135deg, #0d8bf2, #0a63c9);
  box-shadow: 0 12px 22px rgba(13, 139, 242, 0.2);
}

.banner-item {
  position: relative;
  height: 320px;
  overflow: hidden;
  border-radius: 18px;
}

.banner-clickable {
  cursor: pointer;
}

.banner-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s ease;
}

.banner-card:hover .banner-item img {
  transform: scale(1.02);
}

.banner-mask {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 20px;
  background: linear-gradient(180deg, rgba(7, 33, 61, 0.02) 0%, rgba(7, 33, 61, 0.86) 100%);
}

.banner-index {
  display: inline-flex;
  align-items: center;
  height: 26px;
  padding: 0 10px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.16);
  color: #eaf4ff;
  font-size: 12px;
}

.spotlight-card {
  max-width: 420px;
  padding: 16px;
  border-radius: 20px;
  background: rgba(7, 33, 61, 0.52);
  border: 1px solid rgba(255, 255, 255, 0.14);
  backdrop-filter: blur(10px);
  box-shadow: 0 18px 34px rgba(7, 33, 61, 0.22);
  transition: transform 0.24s ease;
}

.banner-card:hover .spotlight-card {
  transform: translateY(-3px);
}

.banner-mask h4 {
  margin: 8px 0 0;
  color: #fff;
  font-size: 24px;
}

.spotlight-card p {
  margin: 10px 0 0;
  color: rgba(236, 244, 255, 0.9);
  font-size: var(--meta-text-size);
  line-height: 1.7;
}

.spotlight-btn {
  margin-top: 12px;
  height: 36px;
  padding: 0 14px;
  border: 1px solid rgba(255, 255, 255, 0.18);
  border-radius: 999px;
  background: linear-gradient(135deg, #0d8bf2, #0a63c9);
  color: #fff;
  font-size: 13px;
  font-weight: 700;
  cursor: pointer;
}

.notice-list {
  list-style: none;
  margin: 0;
  padding: 0;
}

.notice-list li {
  display: grid;
  grid-template-columns: 62px minmax(0, 1fr) 136px;
  gap: var(--space-sm);
  align-items: center;
  padding: 14px;
  margin-bottom: var(--space-sm);
  border: 1px solid #d9e7f8;
  border-radius: 16px;
  background: linear-gradient(180deg, #fbfdff, #f3f8ff);
  cursor: pointer;
  transition: transform 0.24s ease, box-shadow 0.24s ease, border-color 0.24s ease;
}

.notice-list li:last-child {
  margin-bottom: 0;
}

.notice-list li:hover {
  transform: var(--card-hover-transform);
  border-color: var(--card-hover-border);
  box-shadow: var(--card-hover-shadow);
}

.notice-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 32px;
  border-radius: 999px;
  background: linear-gradient(135deg, #0d8bf2, #0a63c9);
  color: #fff;
  font-size: 12px;
  font-weight: 700;
}

.notice-text strong {
  display: block;
  color: #163f64;
  font-size: 14px;
  line-height: 1.5;
}

.notice-text p {
  margin: 4px 0 0;
  color: #708aa3;
  font-size: var(--meta-text-size);
  line-height: 1.6;
}

.notice-time {
  text-align: right;
}

.notice-time span {
  display: inline-block;
  color: #5f7c98;
  font-size: var(--meta-text-size);
}

.utility-row {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-sm);
  margin-bottom: var(--space-md);
}

.utility-chip {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  height: 36px;
  padding: 0 12px;
  border: 1px solid #d8e6f8;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.76);
  color: #3f6182;
  cursor: pointer;
  transition: all 0.22s ease;
}

.utility-chip:hover {
  transform: translateY(-2px);
  border-color: rgba(13, 139, 242, 0.24);
  color: #0d8bf2;
  box-shadow: 0 12px 20px rgba(16, 63, 104, 0.08);
}

.quick-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: var(--space-sm);
}

.quick-entry {
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: var(--space-sm);
  min-height: 92px;
  padding: 14px;
  border-radius: 16px;
  border: 1px solid #d6e6f8;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.92), rgba(238, 245, 255, 0.84));
  color: #18486f;
  text-align: left;
  cursor: pointer;
  opacity: 0;
  transform: translateY(14px);
  animation: rise-in 0.4s ease forwards;
  animation-delay: var(--quick-delay);
  transition: transform 0.22s ease, box-shadow 0.22s ease, border-color 0.22s ease, background 0.22s ease;
}

.quick-entry:hover {
  transform: translateY(-4px) scale(1.01);
  border-color: rgba(var(--quick-tint), 0.28);
  background: linear-gradient(135deg, rgba(var(--quick-tint), 0.14), rgba(10, 99, 201, 0.06)), rgba(255, 255, 255, 0.92);
  box-shadow: var(--card-hover-shadow);
}

.quick-entry-main {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  min-width: 0;
}

.quick-entry i {
  width: 38px;
  height: 38px;
  border-radius: 12px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0d8bf2, #0a63c9);
  color: #fff;
  font-size: 18px;
  transition: all 0.22s ease;
}

.quick-entry:hover i {
  transform: scale(1.04);
  box-shadow: 0 12px 22px rgba(13, 139, 242, 0.26);
}

.quick-copy {
  min-width: 0;
}

.quick-copy span {
  display: block;
  font-size: 13px;
  font-weight: 700;
  line-height: 1.5;
}

.quick-copy small {
  display: block;
  margin-top: 3px;
  color: #6c87a2;
  font-size: 12px;
  line-height: 1.55;
}

.quick-entry em {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 44px;
  height: 26px;
  padding: 0 10px;
  border-radius: 999px;
  background: rgba(var(--quick-tint), 0.12);
  color: #315c85;
  font-size: 12px;
  font-style: normal;
  font-weight: 700;
}

.status-list {
  display: grid;
  gap: var(--space-sm);
}

.status-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px;
  border-radius: 16px;
  border: 1px solid #d7e7f8;
  background: linear-gradient(180deg, #f8fbff, #eef5ff);
  transition: transform 0.24s ease, box-shadow 0.24s ease, border-color 0.24s ease;
  cursor: pointer;
}

.status-item:hover {
  transform: var(--card-hover-transform);
  border-color: var(--card-hover-border);
  box-shadow: var(--card-hover-shadow);
}

.status-copy {
  min-width: 0;
}

.status-item span {
  display: block;
  font-size: 14px;
  font-weight: 700;
  color: #163f64;
  line-height: 1.5;
}

.status-copy small {
  display: block;
  margin-top: 4px;
  color: #6d88a3;
  font-size: 12px;
  line-height: 1.5;
}

.status-side {
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8px;
}

.status-item strong {
  color: #123b60;
  font-size: 13px;
  font-weight: 700;
}

.empty-block {
  min-height: 160px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 18px;
  border: 1px dashed #c8dbef;
  background: linear-gradient(180deg, #f8fbff, #eef6ff);
  color: #6d86a0;
}

.compact-empty {
  min-height: 180px;
}

@keyframes rise-in {
  from {
    opacity: 0;
    transform: translateY(14px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 1380px) {
  .summary-grid {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }

  .quick-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 1080px) {
  .hero-head,
  .dual-grid {
    display: grid;
    grid-template-columns: 1fr;
  }

  .hero-side {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .summary-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 760px) {
  .home-page {
    padding: 12px;
    border-radius: 18px;
  }

  .hero-panel,
  .glass-card {
    padding: 20px 16px;
    border-radius: 20px;
  }

  .hero-panel h1 {
    font-size: 30px;
  }

  .hero-head,
  .dual-grid,
  .summary-grid,
  .quick-grid,
  .hero-points {
    display: block;
  }

  .summary-card,
  .quick-entry,
  .hero-point,
  .status-item,
  .notice-list li {
    margin-bottom: 12px;
  }

  .notice-list li {
    grid-template-columns: 1fr;
  }

  .notice-time {
    text-align: left;
  }
}
</style>
