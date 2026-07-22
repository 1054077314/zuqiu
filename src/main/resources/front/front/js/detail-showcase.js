(function (window) {
  var modules = {
    saishi: { label: '赛事详情', title: 'saishiName', badge: 'saishiValue', cover: 'saishiPhoto', content: 'saishiContent', contentTitle: '赛事介绍', fields: [['赛事编号', 'MATCH ID', 'saishiUuidNumber'], ['比赛地点', 'LOCATION', 'saishiAddress'], ['赛事类型', 'CATEGORY', 'saishiValue'], ['发布时间', 'PUBLISHED AT', 'insertTime']] },
    xunlian: { label: '训练计划详情', title: 'xunlianName', badge: 'xunlianValue', content: 'xunlianContent', contentTitle: '训练说明', fields: [['计划编号', 'PLAN ID', 'xunlianUuidNumber'], ['训练科目', 'TRAINING', 'xunlianKemu'], ['训练日期', 'TRAINING DATE', 'xunlianTime'], ['训练类型', 'CATEGORY', 'xunlianValue']] },
    gonggao: { label: '公告详情', title: 'gonggaoName', badge: 'gonggaoValue', content: 'gonggaoContent', contentTitle: '公告内容', fields: [['公告类型', 'CATEGORY', 'gonggaoValue'], ['发布时间', 'PUBLISHED AT', 'insertTime']] },
    hetong: { label: '合同详情', title: 'hetongName', badgeText: '合同', content: 'hetongText', contentTitle: '合同备注', fields: [['用户姓名', 'PLAYER', 'yonghuName'], ['用户编号', 'PLAYER ID', 'yonghuUuidNumber'], ['联系电话', 'PHONE', 'yonghuPhone'], ['合同附件', 'ATTACHMENT', 'hetongFile']] },
    jiaolian: { label: '教练详情', title: 'jiaolianName', badge: 'sexValue', cover: 'jiaolianPhoto', fields: [['教练编号', 'COACH ID', 'jiaolianUuidNumber'], ['账号', 'ACCOUNT', 'username'], ['联系电话', 'PHONE', 'jiaolianPhone'], ['邮箱', 'EMAIL', 'jiaolianEmail']] },
    yonghu: { label: '球员详情', title: 'yonghuName', badge: 'sexValue', cover: 'yonghuPhoto', fields: [['球员编号', 'PLAYER ID', 'yonghuUuidNumber'], ['账号', 'ACCOUNT', 'username'], ['联系电话', 'PHONE', 'yonghuPhone'], ['邮箱', 'EMAIL', 'yonghuEmail']] },
    shuju: { label: '球员数据详情', title: 'shujuName', badge: 'shujuValue', cover: 'shujuPhoto', content: 'shujuContent', contentTitle: '数据说明', fields: [['数据编号', 'DATA ID', 'shujuUuidNumber'], ['球员姓名', 'PLAYER', 'yonghuName'], ['记录日期', 'RECORD DATE', 'shujuTime'], ['数据类型', 'CATEGORY', 'shujuValue']] }
  }
  var placeholder = "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 720 520'%3E%3Cdefs%3E%3ClinearGradient id='g' x1='0' y1='0' x2='1' y2='1'%3E%3Cstop stop-color='%23f0f9ff'/%3E%3Cstop offset='.62' stop-color='%23bae6fd'/%3E%3Cstop offset='1' stop-color='%23dcfce7'/%3E%3C/linearGradient%3E%3C/defs%3E%3Crect width='720' height='520' fill='url(%23g)'/%3E%3Ccircle cx='360' cy='260' r='108' fill='%23ffffff' fill-opacity='.9'/%3E%3Cpath d='M360 176l39 29-15 46h-48l-15-46zm-65 48l41-14 25 32-18 42-44-2zm130-14l41 14-4 58-44 2-18-42zm-82 88h34l20 40-37 28-37-28zm-44-4l35 3 17 39-28 35-39-22zm122 3l35-3 15 52-39 22-28-35z' fill='%230ea5e9'/%3E%3Ccircle cx='360' cy='260' r='108' fill='none' stroke='%2334d399' stroke-opacity='.28' stroke-width='14'/%3E%3C/svg%3E"
  function image(baseUrl, value) {
    if (!value) return placeholder
    var path = String(value).split(',')[0]
    if (/^https?:\/\//i.test(path)) return path
    return String(baseUrl || '').replace(/\/?$/, '/') + path.replace(/^\//, '')
  }
  window.DetailShowcase = {
    placeholder: placeholder,
    get: function (name, detail, baseUrl) {
      var config = modules[name] || {}
      detail = detail || {}
      return {
        label: config.label || '详情',
        title: detail[config.title] || '暂无标题',
        badge: config.badgeText || detail[config.badge] || '信息详情',
        cover: image(baseUrl, detail[config.cover]),
        content: detail[config.content] || '',
        contentTitle: config.contentTitle || '详情内容',
        fields: (config.fields || []).map(function (field) { return { label: field[0], en: field[1], value: detail[field[2]] || '' } })
      }
    }
  }
  function removeDecorativeEnglish() {
    var selectors = '.detail-eyebrow, .detail-field__label, .detail-content__title'
    Array.prototype.forEach.call(document.querySelectorAll(selectors), function (element) {
      var cleaned = element.textContent.replace(/\s*\/\/\s*[A-Za-z ]+/g, '').trim()
      if (cleaned !== element.textContent) element.textContent = cleaned
    })
  }
  window.addEventListener('DOMContentLoaded', function () {
    var root = document.getElementById('app')
    if (!root || !window.MutationObserver) return
    function markReady() {
      var title = root.querySelector('.detail-title')
      if (title && title.textContent.trim() !== '暂无标题') root.classList.add('detail-page--ready')
    }
    var pending = false
    new MutationObserver(function () {
      if (pending) return
      pending = true
      window.requestAnimationFrame(function () { pending = false; removeDecorativeEnglish(); markReady() })
    }).observe(root, { childList: true, subtree: true, characterData: true })
    removeDecorativeEnglish()
    markReady()
  })
  window.addEventListener('error', function (event) {
    var image = event.target
    if (image && image.matches && image.matches('.detail-cover') && image.src !== placeholder) {
      event.stopImmediatePropagation()
      image.src = placeholder
    }
  }, true)
})(window)
