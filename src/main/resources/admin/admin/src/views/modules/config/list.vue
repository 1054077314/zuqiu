<template>
  <div class="main-content banner-manage-page">
    <div v-if="showFlag">
      <section class="page-hero">
        <div class="hero-copy">
          <p class="hero-tag">轮播图管理</p>
          <h2>首页轮播管理</h2>
          <p class="hero-desc">统一维护轮播图名称、图片顺序与首页展示内容。</p>
        </div>
        <div class="hero-meta">
          <div class="hero-stat">
            <span>轮播分组</span>
            <strong>{{ totalPage || 0 }}</strong>
          </div>
          <el-button type="primary" icon="el-icon-plus" class="hero-btn" @click="addOrUpdateHandler()">新增轮播图</el-button>
        </div>
      </section>

      <el-form :inline="true" :model="searchForm" class="form-content filter-card">
        <el-form-item label="轮播图名称">
          <el-input v-model="searchForm.name" placeholder="请输入轮播图名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="search">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
        <el-form-item class="toolbar-right">
          <el-button
            type="danger"
            icon="el-icon-delete"
            :disabled="dataListSelections.length === 0"
            @click="deleteHandler()"
          >批量删除</el-button>
        </el-form-item>
      </el-form>

      <section class="preview-panel board-card">
        <div class="board-head">
          <div>
            <h3>首页播放预览</h3>
            <p>当前共有 {{ bannerSlides.length }} 张轮播图参与首页展示。</p>
          </div>
          <span class="board-badge">自动轮播</span>
        </div>

        <template v-if="bannerSlides.length">
          <el-carousel height="300px" indicator-position="outside" :interval="4000" arrow="always">
            <el-carousel-item v-for="(item, index) in bannerSlides" :key="item.name + '-' + index">
              <div class="banner-item">
                <img :src="item.url" :alt="item.name">
                <div class="banner-mask">
                  <span class="banner-index">第 {{ index + 1 }} 张</span>
                  <h4>{{ item.name }}</h4>
                </div>
              </div>
            </el-carousel-item>
          </el-carousel>
        </template>
        <div v-else class="empty-card">暂无轮播图数据</div>
      </section>

      <section class="banner-list">
        <article
          v-for="item in dataList"
          :key="item.id"
          class="banner-row board-card"
        >
          <div class="row-check">
            <el-checkbox :value="isSelected(item.id)" @change="toggleSelect(item, $event)"></el-checkbox>
          </div>

          <div class="row-main">
            <div class="row-title">
              <div>
                <h3>{{ item.name || '未命名轮播图' }}</h3>
                <p>共 {{ parseImages(item.value).length }} 张图片，可用于首页展示。</p>
              </div>
              <span class="row-tag">轮播组</span>
            </div>

            <div v-if="parseImages(item.value).length" class="thumb-group">
              <div
                v-for="(img, idx) in parseImages(item.value).slice(0, 6)"
                :key="idx"
                class="thumb-item"
              >
                <img :src="toImageUrl(img)" alt="轮播图">
                <span class="thumb-order">{{ idx + 1 }}</span>
              </div>
              <div v-if="parseImages(item.value).length > 6" class="thumb-more">
                +{{ parseImages(item.value).length - 6 }}
              </div>
            </div>
            <div v-else class="empty-inline">暂无图片</div>
          </div>

          <div class="row-side">
            <el-button plain @click="openPreview(item)">播放预览</el-button>
            <el-button type="primary" plain @click="addOrUpdateHandler(item.id, 'info')">详情</el-button>
            <el-button type="primary" @click="addOrUpdateHandler(item.id, 'edit')">编辑</el-button>
            <el-button type="danger" plain @click="deleteHandler(item.id)">删除</el-button>
          </div>
        </article>
      </section>

      <div v-if="!dataListLoading && !dataList.length" class="board-card empty-card data-empty">
        当前暂无轮播图记录，可通过右上角新增。
      </div>

      <el-pagination
        class="pagination-content"
        layout="total, sizes, prev, pager, next, jumper"
        :current-page="pageIndex"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        :total="totalPage"
        @size-change="sizeChangeHandle"
        @current-change="currentChangeHandle"
      />
    </div>

    <add-or-update v-if="addOrUpdateFlag" ref="addOrUpdate" :parent="this" />

    <el-dialog
      title="轮播图播放预览"
      :visible.sync="previewVisible"
      width="760px"
      destroy-on-close
      custom-class="banner-preview-dialog"
    >
      <div v-if="previewImages.length" class="dialog-preview">
        <div class="dialog-header">
          <div>
            <h4>{{ previewName || '轮播图' }}</h4>
            <p>共 {{ previewImages.length }} 张图片，按当前顺序进行预览。</p>
          </div>
        </div>

        <el-carousel height="380px" indicator-position="outside" :interval="3200" arrow="always">
          <el-carousel-item v-for="(img, index) in previewImages" :key="index">
            <div class="banner-item dialog-item">
              <img :src="toImageUrl(img)" :alt="previewName">
              <div class="banner-mask">
                <span class="banner-index">第 {{ index + 1 }} 张</span>
                <h4>{{ previewName }}</h4>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>
      <div v-else class="empty-card">暂无图片可预览</div>
    </el-dialog>
  </div>
</template>

<script>
import AddOrUpdate from './add-or-update'

export default {
  components: { AddOrUpdate },
  data() {
    return {
      showFlag: true,
      addOrUpdateFlag: false,
      lastOpenedConfigKey: '',
      searchForm: {
        name: ''
      },
      dataList: [],
      bannerSlides: [],
      dataListLoading: false,
      dataListSelections: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      previewVisible: false,
      previewName: '',
      previewImages: []
    }
  },
  created() {
    this.getDataList()
    this.tryOpenPendingDetail()
  },
  mounted() {
    this.tryOpenPendingDetail()
  },
  watch: {
    '$route.fullPath'() {
      this.tryOpenPendingDetail()
    }
  },
  methods: {
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
    rebuildBannerSlides() {
      const slides = []
      this.dataList.forEach(row => {
        this.parseImages(row.value).forEach(path => {
          slides.push({
            name: row.name || '轮播图',
            url: this.toImageUrl(path)
          })
        })
      })
      this.bannerSlides = slides
    },
    isSelected(id) {
      return this.dataListSelections.some(item => Number(item.id) === Number(id))
    },
    toggleSelect(row, checked) {
      if (checked) {
        if (!this.isSelected(row.id)) {
          this.dataListSelections = this.dataListSelections.concat(row)
        }
      } else {
        this.dataListSelections = this.dataListSelections.filter(item => Number(item.id) !== Number(row.id))
      }
    },
    openPreview(row) {
      this.previewName = row.name || '轮播图'
      this.previewImages = this.parseImages(row.value)
      this.previewVisible = true
    },
    resetSearch() {
      this.searchForm = {
        name: ''
      }
      this.search()
    },
    search() {
      this.pageIndex = 1
      this.getDataList()
    },
    getDataList() {
      this.dataListLoading = true
      const params = {
        page: this.pageIndex,
        limit: this.pageSize,
        sort: 'id',
        order: 'desc'
      }
      if (this.searchForm.name) {
        params.name = '%' + this.searchForm.name + '%'
      }
      this.$http({
        url: 'config/page',
        method: 'get',
        params
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.data.list || []
          this.totalPage = data.data.total || 0
          this.dataListSelections = []
          this.rebuildBannerSlides()
        } else {
          this.dataList = []
          this.totalPage = 0
          this.bannerSlides = []
          this.$message.error((data && data.msg) || '加载失败')
        }
      }).finally(() => {
        this.dataListLoading = false
        this.tryOpenPendingDetail()
      })
    },
    sizeChangeHandle(val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getDataList()
    },
    currentChangeHandle(val) {
      this.pageIndex = val
      this.getDataList()
    },
    addOrUpdateHandler(id, type) {
      this.showFlag = false
      this.addOrUpdateFlag = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id, type || 'edit')
      })
    },
    tryOpenFromStorage() {
      const openId = this.$storage.get('pendingConfigOpenId')
      const openType = this.$storage.get('pendingConfigOpenType') || 'info'
      const parsedId = Number(openId)
      if (!parsedId) return false
      const storageKey = 'storage:' + String(parsedId) + ':' + String(openType)
      if (this.lastOpenedConfigKey === storageKey && this.addOrUpdateFlag) return true
      this.lastOpenedConfigKey = storageKey
      this.$nextTick(() => {
        this.addOrUpdateHandler(parsedId, openType)
        this.$storage.remove('pendingConfigOpenId')
        this.$storage.remove('pendingConfigOpenType')
      })
      return true
    },
    tryOpenFromRoute() {
      const openId = this.$route && this.$route.query ? this.$route.query.openId : ''
      const openType = this.$route && this.$route.query ? this.$route.query.openType : ''
      const parsedId = Number(openId)
      if (!parsedId) return false
      const routeKey = 'route:' + String(parsedId) + ':' + String(openType || 'info')
      if (this.lastOpenedConfigKey === routeKey && this.addOrUpdateFlag) return true
      this.lastOpenedConfigKey = routeKey
      this.$nextTick(() => {
        this.addOrUpdateHandler(parsedId, openType || 'info')
        this.clearRouteOpenQuery()
      })
      return true
    },
    tryOpenPendingDetail() {
      if (this.tryOpenFromStorage()) return
      this.tryOpenFromRoute()
    },
    clearRouteOpenQuery() {
      if (!(this.$route && this.$route.query && this.$route.query.openId)) return
      this.$router.replace({
        path: this.$route.path,
        query: {}
      }).catch(() => {})
    },
    deleteHandler(id) {
      const ids = id ? [Number(id)] : this.dataListSelections.map(item => Number(item.id))
      if (!ids.length) return
      this.$confirm('确定删除选中的轮播图吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: 'config/delete',
          method: 'post',
          data: ids
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('删除成功')
            this.search()
          } else {
            this.$message.error((data && data.msg) || '删除失败')
          }
        })
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.banner-manage-page {
  .page-hero {
    display: flex;
    align-items: stretch;
    justify-content: space-between;
    gap: 18px;
    margin-bottom: 14px;
    padding: 22px 24px;
    border-radius: 22px;
    background:
      linear-gradient(150deg, rgba(4, 29, 55, 0.94), rgba(10, 67, 123, 0.88)),
      repeating-linear-gradient(-45deg, rgba(255, 255, 255, 0.04) 0 8px, rgba(255, 255, 255, 0) 8px 16px);
    box-shadow: 0 22px 34px rgba(10, 63, 115, 0.16);
  }

  .hero-copy {
    color: #f5fbff;
  }

  .hero-tag {
    margin: 0;
    font-size: 12px;
    letter-spacing: 2px;
    opacity: 0.82;
  }

  .hero-copy h2 {
    margin: 12px 0 8px;
    font-size: 30px;
  }

  .hero-desc {
    margin: 0;
    color: rgba(234, 245, 255, 0.9);
    font-size: 14px;
    line-height: 1.8;
  }

  .hero-meta {
    min-width: 220px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items: flex-end;
    gap: 14px;
  }

  .hero-stat {
    min-width: 150px;
    padding: 14px 16px;
    border-radius: 16px;
    background: rgba(255, 255, 255, 0.1);
    border: 1px solid rgba(255, 255, 255, 0.16);
    color: #e4f2ff;
    text-align: right;
  }

  .hero-stat span {
    display: block;
    font-size: 13px;
    opacity: 0.84;
  }

  .hero-stat strong {
    display: block;
    margin-top: 8px;
    font-size: 28px;
    line-height: 1;
  }

  .hero-btn {
    height: 44px;
    border-radius: 14px;
    padding: 0 20px;
  }

  .filter-card,
  .board-card {
    padding: 18px 20px;
    border-radius: 20px;
    border: 1px solid #d7e6f8;
    background: rgba(255, 255, 255, 0.94);
    box-shadow: 0 12px 24px rgba(15, 64, 107, 0.06);
  }

  .filter-card {
    margin-bottom: 14px;
  }

  .toolbar-right {
    float: right;
    margin-right: 0;
  }

  .board-head {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    gap: 12px;
    margin-bottom: 14px;
  }

  .board-head h3 {
    margin: 0;
    color: #163f64;
    font-size: 24px;
  }

  .board-head p {
    margin: 6px 0 0;
    color: #6b86a1;
    font-size: 13px;
  }

  .board-badge {
    display: inline-flex;
    align-items: center;
    height: 30px;
    padding: 0 12px;
    border-radius: 999px;
    background: #edf5ff;
    color: #1d5d93;
    font-size: 12px;
    font-weight: 700;
  }

  .preview-panel {
    margin-bottom: 14px;
  }

  .banner-list {
    display: grid;
    gap: 14px;
  }

  .banner-row {
    display: grid;
    grid-template-columns: 32px minmax(0, 1fr) 360px;
    gap: 18px;
    align-items: center;
  }

  .row-title {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    gap: 14px;
    margin-bottom: 14px;
  }

  .row-title h3 {
    margin: 0;
    color: #163f64;
    font-size: 20px;
  }

  .row-title p {
    margin: 6px 0 0;
    color: #6b86a1;
    font-size: 13px;
  }

  .row-tag {
    display: inline-flex;
    align-items: center;
    height: 28px;
    padding: 0 10px;
    border-radius: 999px;
    background: #f2f8ff;
    color: #49729b;
    font-size: 12px;
  }

  .thumb-group {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 10px;
  }

  .thumb-item,
  .thumb-more {
    position: relative;
    width: 100px;
    height: 66px;
    border-radius: 14px;
    overflow: hidden;
    border: 1px solid #d7e6f8;
    background: #f2f7fd;
  }

  .thumb-item img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .thumb-order {
    position: absolute;
    left: 8px;
    top: 8px;
    min-width: 22px;
    height: 22px;
    padding: 0 6px;
    border-radius: 999px;
    background: rgba(7, 34, 65, 0.75);
    color: #fff;
    font-size: 12px;
    line-height: 22px;
    text-align: center;
  }

  .thumb-more {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    color: #426684;
    font-weight: 700;
  }

  .row-side {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 10px;
  }

  .banner-item {
    position: relative;
    width: 100%;
    height: 300px;
    border-radius: 18px;
    overflow: hidden;
  }

  .banner-item img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .dialog-item {
    height: 380px;
  }

  .banner-mask {
    position: absolute;
    inset: auto 0 0 0;
    padding: 20px 18px;
    background: linear-gradient(180deg, rgba(7, 34, 65, 0) 0%, rgba(7, 34, 65, 0.82) 100%);
  }

  .banner-mask h4 {
    margin: 8px 0 0;
    color: #fff;
    font-size: 24px;
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

  .dialog-header {
    margin-bottom: 12px;
  }

  .dialog-header h4 {
    margin: 0;
    color: #163f65;
    font-size: 20px;
  }

  .dialog-header p {
    margin: 6px 0 0;
    color: #6b86a1;
    font-size: 13px;
  }

  .empty-inline,
  .empty-card {
    color: #6d87a0;
  }

  .empty-card {
    display: flex;
    align-items: center;
    justify-content: center;
    min-height: 160px;
  }

  .data-empty {
    margin-top: 14px;
  }

  .dialog-preview {
    padding-top: 4px;
  }

  ::v-deep .el-carousel__arrow {
    width: 42px;
    height: 42px;
    background: rgba(7, 34, 65, 0.42);
  }

  ::v-deep .el-carousel__indicator button {
    background-color: #9fbfdf;
  }

  ::v-deep .el-carousel__indicator.is-active button {
    background-color: #0d8bf2;
  }
}

@media (max-width: 1180px) {
  .banner-manage-page {
    .banner-row {
      grid-template-columns: 32px 1fr;
    }

    .row-side {
      grid-column: 2;
      grid-template-columns: repeat(4, minmax(0, 1fr));
    }
  }
}

@media (max-width: 760px) {
  .banner-manage-page {
    .page-hero {
      flex-direction: column;
      align-items: flex-start;
    }

    .hero-meta {
      width: 100%;
      align-items: stretch;
    }

    .hero-stat {
      text-align: left;
    }

    .row-side {
      grid-template-columns: repeat(2, minmax(0, 1fr));
    }

    .thumb-item,
    .thumb-more {
      width: 78px;
      height: 56px;
    }

    .banner-item,
    ::v-deep .el-carousel,
    ::v-deep .el-carousel__container {
      height: 220px !important;
    }

    .dialog-item {
      height: 260px;
    }
  }
}
</style>
