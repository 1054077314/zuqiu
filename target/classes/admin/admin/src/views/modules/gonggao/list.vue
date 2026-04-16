<template>
  <div class="main-content">
    <div v-if="showFlag">
      <el-form :inline="true" :model="searchForm" class="form-content">
        <el-form-item label="公告名称">
          <el-input v-model="searchForm.gonggaoName" placeholder="请输入公告名称" clearable />
        </el-form-item>
        <el-form-item label="公告类型">
          <el-select v-model="searchForm.gonggaoTypes" placeholder="请选择公告类型" clearable>
            <el-option
              v-for="item in gonggaoTypesOptions"
              :key="item.codeIndex"
              :label="item.indexName"
              :value="item.codeIndex"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="search">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-form :inline="true" class="toolbar-content">
        <el-form-item>
          <el-button type="success" icon="el-icon-plus" @click="addOrUpdateHandler()">新增公告</el-button>
          <el-button
            type="danger"
            icon="el-icon-delete"
            :disabled="dataListSelections.length === 0"
            @click="deleteHandler()"
          >批量删除</el-button>
        </el-form-item>
      </el-form>

      <div class="table-content">
        <el-table
          class="tables"
          :data="dataList"
          border
          stripe
          v-loading="dataListLoading"
          @selection-change="selectionChangeHandler"
        >
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column type="index" label="#" width="60" align="center" />
          <el-table-column prop="gonggaoName" label="公告名称" min-width="180" />
          <el-table-column prop="gonggaoPhoto" label="公告图片" min-width="120">
            <template slot-scope="scope">
              <img
                v-if="scope.row.gonggaoPhoto"
                :src="$base.url + scope.row.gonggaoPhoto"
                alt="公告图片"
                class="table-image"
              />
              <span v-else>无</span>
            </template>
          </el-table-column>
          <el-table-column prop="gonggaoValue" label="公告类型" min-width="110" />
          <el-table-column prop="insertTime" label="发布时间" min-width="170" />
          <el-table-column prop="gonggaoContent" label="公告详情" min-width="220" show-overflow-tooltip>
            <template slot-scope="scope">
              <span>{{ stripHtml(scope.row.gonggaoContent) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="210" align="center" fixed="right">
            <template slot-scope="scope">
              <el-button type="text" @click="addOrUpdateHandler(scope.row.id, 'info')">详情</el-button>
              <el-button type="text" @click="addOrUpdateHandler(scope.row.id, 'edit')">编辑</el-button>
              <el-button type="text" class="danger-text" @click="deleteHandler(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

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
    </div>

    <add-or-update v-if="addOrUpdateFlag" ref="addOrUpdate" :parent="this" />
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
      lastOpenedStorageKey: '',
      searchForm: {
        gonggaoName: '',
        gonggaoTypes: ''
      },
      gonggaoTypesOptions: [],
      dataList: [],
      dataListLoading: false,
      dataListSelections: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0
    }
  },
  created() {
    this.loadGonggaoTypes()
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
    stripHtml(content) {
      if (!content) return ''
      return content.replace(/<[^>]*>/g, '').replace(/\s+/g, ' ').trim()
    },
    resetSearch() {
      this.searchForm = {
        gonggaoName: '',
        gonggaoTypes: ''
      }
      this.search()
    },
    search() {
      this.pageIndex = 1
      this.getDataList()
    },
    loadGonggaoTypes() {
      this.$http({
        url: 'dictionary/page',
        method: 'get',
        params: {
          page: 1,
          limit: 100,
          dicCode: 'gonggao_types'
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.gonggaoTypesOptions = data.data.list || []
        }
      })
    },
    getDataList() {
      this.dataListLoading = true
      const params = {
        page: this.pageIndex,
        limit: this.pageSize,
        sort: 'id',
        order: 'desc'
      }
      if (this.searchForm.gonggaoName) {
        params.gonggaoName = `%${this.searchForm.gonggaoName}%`
      }
      if (this.searchForm.gonggaoTypes !== '' && this.searchForm.gonggaoTypes !== null) {
        params.gonggaoTypes = this.searchForm.gonggaoTypes
      }
      this.$http({
        url: 'gonggao/page',
        method: 'get',
        params
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.data.list || []
          this.totalPage = data.data.total || 0
        } else {
          this.dataList = []
          this.totalPage = 0
          this.$message.error(data.msg || '加载失败')
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
    selectionChangeHandler(val) {
      this.dataListSelections = val
    },
    addOrUpdateHandler(id, type) {
      this.showFlag = false
      this.addOrUpdateFlag = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id, type || 'edit')
      })
    },
    tryOpenFromStorage() {
      const openId = this.$storage.get('pendingGonggaoOpenId')
      const openType = this.$storage.get('pendingGonggaoOpenType') || 'info'
      const parsedId = Number(openId)
      if (!parsedId) return false
      const storageKey = String(parsedId) + ':' + String(openType)
      if (this.lastOpenedStorageKey === storageKey && this.addOrUpdateFlag) return true
      this.lastOpenedStorageKey = storageKey
      this.$nextTick(() => {
        this.addOrUpdateHandler(parsedId, openType)
        this.$storage.remove('pendingGonggaoOpenId')
        this.$storage.remove('pendingGonggaoOpenType')
      })
      return true
    },
    tryOpenFromRoute() {
      const openId = this.$route && this.$route.query ? this.$route.query.openId : ''
      const openType = this.$route && this.$route.query ? this.$route.query.openType : ''
      const parsedId = Number(openId)
      if (!parsedId) return false
      const routeKey = 'route:' + String(parsedId) + ':' + String(openType || 'info')
      if (this.lastOpenedStorageKey === routeKey && this.addOrUpdateFlag) return true
      this.lastOpenedStorageKey = routeKey
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
      this.$confirm('确定删除选中的公告吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: 'gonggao/delete',
          method: 'post',
          data: ids
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('删除成功')
            this.search()
          } else {
            this.$message.error(data.msg || '删除失败')
          }
        })
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.form-content,
.toolbar-content {
  margin-bottom: 12px;
}

.table-image {
  width: 70px;
  height: 70px;
  object-fit: cover;
  border-radius: 6px;
  border: 1px solid #ebeef5;
}

.danger-text {
  color: #f56c6c;
}

.pagination-content {
  margin-top: 16px;
  text-align: right;
}
</style>
