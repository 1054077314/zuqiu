<template>
  <div class="main-content">
    <div v-if="showFlag">
      <el-form :inline="true" :model="searchForm" class="form-content">
        <el-form-item label="赛事类型名称">
          <el-input v-model="searchForm.indexName" placeholder="请输入赛事类型名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="search">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-form :inline="true" class="toolbar-content">
        <el-form-item>
          <el-button type="success" icon="el-icon-plus" @click="addOrUpdateHandler()">新增赛事类型</el-button>
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
          <el-table-column prop="codeIndex" label="编码" min-width="100" />
          <el-table-column prop="indexName" label="赛事类型名称" min-width="220" show-overflow-tooltip />
          <el-table-column prop="createTime" label="创建时间" min-width="170" />
          <el-table-column label="操作" width="220" align="center" fixed="right">
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

const DIC_CODE = 'saishi_types'
const DIC_NAME = '赛事类型'

export default {
  components: { AddOrUpdate },
  data() {
    return {
      showFlag: true,
      addOrUpdateFlag: false,
      searchForm: {
        indexName: ''
      },
      dataList: [],
      dataListLoading: false,
      dataListSelections: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0
    }
  },
  created() {
    this.getDataList()
  },
  methods: {
    resetSearch() {
      this.searchForm = {
        indexName: ''
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
        order: 'desc',
        dicCode: DIC_CODE,
        dicName: DIC_NAME
      }
      if (this.searchForm.indexName) {
        params.indexName = `%${this.searchForm.indexName}%`
      }
      this.$http({
        url: 'dictionary/page',
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
    deleteHandler(id) {
      const ids = id ? [Number(id)] : this.dataListSelections.map(item => Number(item.id))
      if (!ids.length) return
      this.$confirm('确定删除选中的字典数据吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: 'dictionary/delete',
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

.danger-text {
  color: #f56c6c;
}

.pagination-content {
  margin-top: 16px;
  text-align: right;
}
</style>

