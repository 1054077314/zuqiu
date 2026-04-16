<template>
  <div class="main-content">
    <div v-if="showFlag">
      <el-form :inline="true" :model="searchForm" class="form-content">
        <el-form-item label="训练计划名称">
          <el-input v-model="searchForm.xunlianName" placeholder="请输入训练计划名称" clearable />
        </el-form-item>
        <el-form-item label="用户姓名">
          <el-input v-model="searchForm.yonghuName" placeholder="请输入用户姓名" clearable />
        </el-form-item>
        <el-form-item label="计划类型">
          <el-select v-model="searchForm.xunlianTypes" placeholder="请选择计划类型" clearable>
            <el-option
              v-for="item in xunlianTypesOptions"
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
          <el-button type="success" icon="el-icon-plus" @click="addOrUpdateHandler()">新增训练计划</el-button>
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
          <el-table-column prop="yonghuUuidNumber" label="用户编号" min-width="120" />
          <el-table-column prop="yonghuName" label="用户姓名" min-width="110" />
          <el-table-column prop="xunlianName" label="训练计划名称" min-width="160" />
          <el-table-column prop="xunlianUuidNumber" label="计划编号" min-width="130" />
          <el-table-column prop="xunlianPhoto" label="计划图片" min-width="100">
            <template slot-scope="scope">
              <img
                v-if="scope.row.xunlianPhoto"
                :src="$base.url + scope.row.xunlianPhoto"
                alt="计划图片"
                class="table-image"
              />
              <span v-else>无</span>
            </template>
          </el-table-column>
          <el-table-column prop="xunlianValue" label="计划类型" min-width="110" />
          <el-table-column prop="xunlianKemu" label="训练科目" min-width="140" />
          <el-table-column prop="xunlianTime" label="日期" min-width="110" />
          <el-table-column prop="insertTime" label="录入时间" min-width="170" />
          <el-table-column prop="xunlianContent" label="计划介绍" min-width="200" show-overflow-tooltip />
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

export default {
  components: { AddOrUpdate },
  data() {
    return {
      showFlag: true,
      addOrUpdateFlag: false,
      searchForm: {
        xunlianName: '',
        yonghuName: '',
        xunlianTypes: ''
      },
      xunlianTypesOptions: [],
      dataList: [],
      dataListLoading: false,
      dataListSelections: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0
    }
  },
  created() {
    this.loadXunlianTypes()
    this.getDataList()
  },
  methods: {
    resetSearch() {
      this.searchForm = {
        xunlianName: '',
        yonghuName: '',
        xunlianTypes: ''
      }
      this.search()
    },
    search() {
      this.pageIndex = 1
      this.getDataList()
    },
    loadXunlianTypes() {
      this.$http({
        url: 'dictionary/page',
        method: 'get',
        params: {
          page: 1,
          limit: 100,
          dicCode: 'xunlian_types'
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.xunlianTypesOptions = data.data.list || []
        }
      })
    },
    getDataList() {
      this.dataListLoading = true
      const params = {
        page: this.pageIndex,
        limit: this.pageSize,
        sort: 'id',
        order: 'desc',
        xunlianDelete: 1
      }
      if (this.searchForm.xunlianName) {
        params.xunlianName = `%${this.searchForm.xunlianName}%`
      }
      if (this.searchForm.yonghuName) {
        params.yonghuName = `%${this.searchForm.yonghuName}%`
      }
      if (this.searchForm.xunlianTypes !== '' && this.searchForm.xunlianTypes !== null) {
        params.xunlianTypes = this.searchForm.xunlianTypes
      }
      this.$http({
        url: 'xunlian/page',
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
      this.$confirm('确定删除选中的训练计划吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: 'xunlian/delete',
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


