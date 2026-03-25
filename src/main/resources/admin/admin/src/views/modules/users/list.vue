<template>
  <div class="main-content">
    <div v-if="showFlag">
      <el-form :inline="true" :model="searchForm" class="form-content">
        <el-form-item label="账号">
          <el-input v-model="searchForm.username" placeholder="请输入账号" clearable />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="searchForm.role" placeholder="请选择角色" clearable>
            <el-option label="管理员" value="管理员" />
            <el-option label="教练" value="教练" />
            <el-option label="用户" value="用户" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="search">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-form :inline="true" class="toolbar-content">
        <el-form-item>
          <el-button type="success" icon="el-icon-plus" @click="addOrUpdateHandler()">新增账号</el-button>
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
          <el-table-column prop="username" label="账号" min-width="160" />
          <el-table-column prop="password" label="密码" min-width="160" />
          <el-table-column prop="role" label="角色" min-width="120" />
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
        username: '',
        role: ''
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
        username: '',
        role: ''
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
      if (this.searchForm.username) {
        params.username = `%${this.searchForm.username}%`
      }
      if (this.searchForm.role) {
        params.role = this.searchForm.role
      }
      this.$http({
        url: 'users/page',
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
      this.$confirm('确定删除选中的管理员账号吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: 'users/delete',
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
