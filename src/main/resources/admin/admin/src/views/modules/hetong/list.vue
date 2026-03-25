<template>
  <div class="main-content">
    <div v-if="showFlag">
      <el-form :inline="true" :model="searchForm" class="form-content">
        <el-form-item label="用户编号">
          <el-input
            v-model="searchForm.yonghuUuidNumber"
            placeholder="请输入用户编号"
            clearable
          />
        </el-form-item>
        <el-form-item label="用户姓名">
          <el-input
            v-model="searchForm.yonghuName"
            placeholder="请输入用户姓名"
            clearable
          />
        </el-form-item>
        <el-form-item label="合同标题">
          <el-input
            v-model="searchForm.hetongName"
            placeholder="请输入合同标题"
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="search">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-form :inline="true" class="toolbar-content">
        <el-form-item>
          <el-button type="success" icon="el-icon-plus" @click="addOrUpdateHandler()">新增合同</el-button>
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
          <el-table-column prop="yonghuPhone" label="手机号" min-width="120" />
          <el-table-column prop="hetongName" label="合同标题" min-width="140" />
          <el-table-column prop="hetongFile" label="合同附件" min-width="120">
            <template slot-scope="scope">
              <a
                v-if="scope.row.hetongFile"
                class="link-btn"
                :href="$base.url + scope.row.hetongFile"
                target="_blank"
              >下载</a>
              <span v-else>无</span>
            </template>
          </el-table-column>
          <el-table-column prop="hetongText" label="备注" min-width="180" show-overflow-tooltip />
          <el-table-column prop="createTime" label="创建时间" min-width="160" />
          <el-table-column label="操作" width="230" align="center" fixed="right">
            <template slot-scope="scope">
              <el-button type="text" @click="addOrUpdateHandler(scope.row.id, 'info')">详情</el-button>
              <el-button type="text" @click="addOrUpdateHandler(scope.row.id)">编辑</el-button>
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

    <add-or-update
      v-if="addOrUpdateFlag"
      ref="addOrUpdate"
      :parent="this"
    />
  </div>
</template>

<script>
import AddOrUpdate from './add-or-update'

export default {
  components: { AddOrUpdate },
  data() {
    return {
      searchForm: {
        yonghuUuidNumber: '',
        yonghuName: '',
        hetongName: ''
      },
      dataList: [],
      dataListLoading: false,
      dataListSelections: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      showFlag: true,
      addOrUpdateFlag: false
    }
  },
  created() {
    this.getDataList()
  },
  methods: {
    resetSearch() {
      this.searchForm = {
        yonghuUuidNumber: '',
        yonghuName: '',
        hetongName: ''
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
        hetongDelete: 1
      }
      if (this.searchForm.yonghuUuidNumber) {
        params.yonghuUuidNumber = `%${this.searchForm.yonghuUuidNumber}%`
      }
      if (this.searchForm.yonghuName) {
        params.yonghuName = `%${this.searchForm.yonghuName}%`
      }
      if (this.searchForm.hetongName) {
        params.hetongName = `%${this.searchForm.hetongName}%`
      }
      this.$http({
        url: 'hetong/page',
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
        this.$refs.addOrUpdate.init(id, type === 'info' ? 'info' : 'edit')
      })
    },
    deleteHandler(id) {
      const ids = id ? [Number(id)] : this.dataListSelections.map(item => Number(item.id))
      if (!ids.length) {
        return
      }
      this.$confirm('确定删除选中的合同记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: 'hetong/delete',
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

.link-btn {
  color: #409eff;
  text-decoration: none;
}

.danger-text {
  color: #f56c6c;
}

.pagination-content {
  margin-top: 16px;
  text-align: right;
}
</style>
