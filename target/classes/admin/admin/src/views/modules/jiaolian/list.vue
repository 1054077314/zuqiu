<template>
  <div class="main-content">
    <div v-if="showFlag">
      <el-form :inline="true" :model="searchForm" class="form-content">
        <el-form-item label="教练编号">
          <el-input v-model="searchForm.jiaolianUuidNumber" placeholder="请输入教练编号" clearable />
        </el-form-item>
        <el-form-item label="教练姓名">
          <el-input v-model="searchForm.jiaolianName" placeholder="请输入教练姓名" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="search">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-form :inline="true" class="toolbar-content">
        <el-form-item>
          <el-button type="success" icon="el-icon-plus" @click="addOrUpdateHandler()">新增教练</el-button>
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
          <el-table-column prop="username" label="账号" min-width="120" />
          <el-table-column prop="jiaolianUuidNumber" label="教练编号" min-width="130" />
          <el-table-column prop="jiaolianName" label="教练姓名" min-width="120" />
          <el-table-column prop="jiaolianPhone" label="手机号" min-width="130" />
          <el-table-column prop="jiaolianIdNumber" label="身份证号" min-width="180" show-overflow-tooltip />
          <el-table-column prop="jiaolianPhoto" label="头像" min-width="100">
            <template slot-scope="scope">
              <img
                v-if="scope.row.jiaolianPhoto"
                :src="$base.url + scope.row.jiaolianPhoto"
                alt="头像"
                class="table-image"
              />
              <span v-else>无</span>
            </template>
          </el-table-column>
          <el-table-column prop="sexValue" label="性别" min-width="90" />
          <el-table-column prop="jiaolianEmail" label="邮箱" min-width="180" show-overflow-tooltip />
          <el-table-column prop="createTime" label="创建时间" min-width="170" />
          <el-table-column label="操作" width="280" align="center" fixed="right">
            <template slot-scope="scope">
              <el-button type="text" @click="addOrUpdateHandler(scope.row.id, 'info')">详情</el-button>
              <el-button type="text" @click="addOrUpdateHandler(scope.row.id, 'edit')">编辑</el-button>
              <el-button type="text" @click="resetPassword(scope.row.id)">重置密码</el-button>
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
        jiaolianUuidNumber: '',
        jiaolianName: ''
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
        jiaolianUuidNumber: '',
        jiaolianName: ''
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
      if (this.searchForm.jiaolianUuidNumber) {
        params.jiaolianUuidNumber = `%${this.searchForm.jiaolianUuidNumber}%`
      }
      if (this.searchForm.jiaolianName) {
        params.jiaolianName = `%${this.searchForm.jiaolianName}%`
      }
      this.$http({
        url: 'jiaolian/page',
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
    resetPassword(id) {
      this.$confirm('确定将该教练密码重置为 123456 吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: 'jiaolian/resetPassword',
          method: 'get',
          params: { id }
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('重置成功')
          } else {
            this.$message.error(data.msg || '重置失败')
          }
        })
      }).catch(() => {})
    },
    deleteHandler(id) {
      const ids = id ? [Number(id)] : this.dataListSelections.map(item => Number(item.id))
      if (!ids.length) return
      this.$confirm('确定删除选中的教练吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: 'jiaolian/delete',
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
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 50%;
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
