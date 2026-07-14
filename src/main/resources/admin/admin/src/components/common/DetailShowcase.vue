<template>
  <section class="admin-detail-showcase">
    <div class="admin-detail-showcase__overview" :class="{ 'is-text-only': !hasMedia }">
      <div v-if="hasMedia" class="admin-detail-showcase__media">
        <img :src="coverUrl || placeholderUrl" :alt="title" @error="hideBrokenImage">
      </div>
      <div class="admin-detail-showcase__summary">
        <span class="admin-detail-showcase__eyebrow">{{ definition.label }}</span>
        <span class="admin-detail-showcase__badge">{{ badge }}</span>
        <h1>{{ title }}</h1>
        <p>{{ definition.summary }}</p>
        <div class="admin-detail-showcase__fields">
          <div v-for="field in visibleFields" :key="field.en" class="admin-detail-showcase__field">
            <span>{{ field.label }}</span>
            <strong>{{ field.value || '暂无' }}</strong>
          </div>
        </div>
      </div>
    </div>
    <section v-if="content" class="admin-detail-showcase__content">
      <h2>{{ definition.contentTitle }}</h2>
      <div v-html="content"></div>
    </section>
    <p v-else-if="definition.contentKey" class="admin-detail-showcase__empty">暂无{{ definition.contentTitle }}</p>
    <footer class="admin-detail-showcase__actions"><el-button type="primary" @click="$emit('back')">返回</el-button></footer>
  </section>
</template>

<script>
const definitions = {
  saishi: { label: '赛事详情', summary: '查看比赛安排、地点与赛事介绍', titleKey: 'saishiName', badgeKey: 'saishiValue', coverKey: 'saishiPhoto', contentKey: 'saishiContent', contentTitle: '赛事介绍', fields: [['赛事编号', 'MATCH ID', 'saishiUuidNumber'], ['比赛地点', 'LOCATION', 'saishiAddress'], ['赛事类型', 'CATEGORY', 'saishiValue'], ['发布时间', 'PUBLISHED AT', 'insertTime']] },
  xunlian: { label: '训练计划详情', summary: '查看训练科目、计划日期与安排说明', titleKey: 'xunlianName', badgeKey: 'xunlianValue', contentKey: 'xunlianContent', contentTitle: '训练说明', fields: [['计划编号', 'PLAN ID', 'xunlianUuidNumber'], ['训练科目', 'TRAINING', 'xunlianKemu'], ['训练日期', 'TRAINING DATE', 'xunlianTime'], ['训练类型', 'CATEGORY', 'xunlianValue']] },
  gonggao: { label: '公告详情', summary: '俱乐部发布的官方通知与最新动态', titleKey: 'gonggaoName', badgeKey: 'gonggaoValue', contentKey: 'gonggaoContent', contentTitle: '公告内容', fields: [['公告类型', 'CATEGORY', 'gonggaoValue'], ['发布时间', 'PUBLISHED AT', 'insertTime']] },
  hetong: { label: '合同详情', summary: '查看合同归属、附件与补充约定', titleKey: 'hetongName', badgeText: '合同', contentKey: 'hetongText', contentTitle: '合同备注', fields: [['用户姓名', 'PLAYER', 'yonghuName'], ['用户编号', 'PLAYER ID', 'yonghuUuidNumber'], ['联系电话', 'PHONE', 'yonghuPhone'], ['合同附件', 'ATTACHMENT', 'hetongFile']] },
  jiaolian: { label: '教练详情', summary: '俱乐部教练人员资料', titleKey: 'jiaolianName', badgeKey: 'sexValue', coverKey: 'jiaolianPhoto', fields: [['教练编号', 'COACH ID', 'jiaolianUuidNumber'], ['账号', 'ACCOUNT', 'username'], ['联系电话', 'PHONE', 'jiaolianPhone'], ['邮箱', 'EMAIL', 'jiaolianEmail']] },
  yonghu: { label: '球员详情', summary: '俱乐部球员资料与联系信息', titleKey: 'yonghuName', badgeKey: 'sexValue', coverKey: 'yonghuPhoto', fields: [['球员编号', 'PLAYER ID', 'yonghuUuidNumber'], ['账号', 'ACCOUNT', 'username'], ['联系电话', 'PHONE', 'yonghuPhone'], ['邮箱', 'EMAIL', 'yonghuEmail']] },
  shuju: { label: '球员数据详情', summary: '球员训练与比赛数据记录', titleKey: 'shujuName', badgeKey: 'shujuValue', coverKey: 'shujuPhoto', contentKey: 'shujuContent', contentTitle: '数据说明', fields: [['数据编号', 'DATA ID', 'shujuUuidNumber'], ['球员姓名', 'PLAYER', 'yonghuName'], ['记录日期', 'RECORD DATE', 'shujuTime'], ['数据类型', 'CATEGORY', 'shujuValue']] }
}
const placeholder = "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 720 520'%3E%3Cdefs%3E%3ClinearGradient id='g' x1='0' y1='0' x2='1' y2='1'%3E%3Cstop stop-color='%23e8edf8'/%3E%3Cstop offset='1' stop-color='%23c9ddfb'/%3E%3C/linearGradient%3E%3C/defs%3E%3Crect width='720' height='520' fill='url(%23g)'/%3E%3Ccircle cx='360' cy='260' r='108' fill='%23ffffff' fill-opacity='.9'/%3E%3Cpath d='M360 176l39 29-15 46h-48l-15-46zm-65 48l41-14 25 32-18 42-44-2zm130-14l41 14-4 58-44 2-18-42zm-82 88h34l20 40-37 28-37-28zm-44-4l35 3 17 39-28 35-39-22zm122 3l35-3 15 52-39 22-28-35z' fill='%23244a88'/%3E%3Ccircle cx='360' cy='260' r='108' fill='none' stroke='%23244a88' stroke-opacity='.16' stroke-width='14'/%3E%3C/svg%3E"

export default {
  props: { moduleName: { type: String, required: true }, record: { type: Object, default: () => ({}) }, baseUrl: { type: String, default: '' } },
  computed: {
    definition() { return definitions[this.moduleName] || { label: '详情', summary: '', fields: [] } },
    title() { return this.record[this.definition.titleKey] || '暂无标题' },
    badge() { return this.definition.badgeText || this.record[this.definition.badgeKey] || '信息详情' },
    content() { return this.record[this.definition.contentKey] || '' },
    hasMedia() { return Boolean(this.definition.coverKey) },
    placeholderUrl() { return placeholder },
    coverUrl() {
      const value = this.record[this.definition.coverKey]
      if (!this.definition.coverKey) return ''
      if (!value) return ''
      const path = String(value).split(',')[0]
      return /^https?:\/\//i.test(path) ? path : `${String(this.baseUrl).replace(/\/?$/, '/')}${path.replace(/^\//, '')}`
    },
    visibleFields() { return (this.definition.fields || []).map(field => ({ label: field[0], en: field[1], value: this.record[field[2]] || '' })) }
  },
  methods: { hideBrokenImage(event) { if (event.target.src !== placeholder) event.target.src = placeholder } }
}
</script>

<style scoped>
.admin-detail-showcase { padding: 34px; background: #fff; border: 1px solid #d6def0; border-radius: 18px; }
.admin-detail-showcase__overview { display: grid; grid-template-columns: minmax(280px, .95fr) minmax(0, 1fr); gap: 46px; }
.admin-detail-showcase__overview.is-text-only { grid-template-columns: 1fr; max-width: 760px; margin: 0 auto; }
.admin-detail-showcase__media { min-height: 330px; overflow: hidden; border: 1px solid #d6def0; border-radius: 14px; background: #edf1fb; }
.admin-detail-showcase__media img { display: block; width: 100%; height: 330px; object-fit: cover; }
.admin-detail-showcase__eyebrow, .admin-detail-showcase__badge { display: inline-flex; min-height: 26px; align-items: center; padding: 4px 11px; border-radius: 6px; font-size: 12px; font-weight: 700; letter-spacing: .06em; }
.admin-detail-showcase__eyebrow { color: #616b80; background: #edf1fb; }.admin-detail-showcase__badge { margin-left: 8px; color: #463d13; background: #f4c400; }
.admin-detail-showcase h1 { margin: 20px 0 10px; color: #162033; font-size: 32px; line-height: 1.3; }.admin-detail-showcase__summary > p { margin: 0 0 22px; color: #63708a; }
.admin-detail-showcase__fields { padding: 14px 0; border-top: 1px solid #d6def0; border-bottom: 1px solid #d6def0; }
.admin-detail-showcase__field { display: flex; justify-content: space-between; gap: 20px; padding: 10px 0; color: #63708a; }.admin-detail-showcase__field strong { max-width: 58%; color: #162033; text-align: right; overflow-wrap: anywhere; }
.admin-detail-showcase__content { margin-top: 36px; padding-top: 28px; border-top: 1px solid #d6def0; color: #46536b; line-height: 1.9; overflow-wrap: anywhere; }.admin-detail-showcase__content h2 { margin: 0 0 16px; color: #162033; font-size: 16px; }.admin-detail-showcase__content ::v-deep img { max-width: 100%; height: auto; border-radius: 12px; }
.admin-detail-showcase__empty { margin-top: 34px; padding-top: 24px; border-top: 1px solid #d6def0; color: #63708a; }.admin-detail-showcase__actions { display: flex; justify-content: flex-end; margin-top: 30px; }
@media (max-width: 860px) { .admin-detail-showcase { padding: 22px; }.admin-detail-showcase__overview { grid-template-columns: 1fr; gap: 26px; }.admin-detail-showcase__field { display: block; }.admin-detail-showcase__field strong { display: block; max-width: 100%; margin-top: 4px; text-align: left; } }
</style>
