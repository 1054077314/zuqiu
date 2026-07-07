<template>
  <div class="main-content">
    <div v-if="showFlag">
      <div class="module-head">
        <div>
          <h1>合同管理</h1>
          <p>统一维护球员及用户合同资料、附件与备注信息</p>
        </div>
        <span>Contract Center</span>
      </div>

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
        <div class="table-head">
          <div>
            <h2>合同列表</h2>
            <p>共 {{ totalPage }} 条合同记录</p>
          </div>
        </div>
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
.main-content {
  max-width: 1440px;
  margin: 0 auto;
  color: #111827;
}

.module-head {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 20px;
  margin-bottom: 16px;
}

.module-head h1 {
  margin: 0 0 6px;
  color: #0f172a;
  font-size: 28px;
  line-height: 1.2;
  font-weight: 800;
}

.module-head p {
  margin: 0;
  color: #667085;
  font-size: 14px;
  line-height: 1.5;
}

.module-head span {
  color: #8aa0bc;
  font-size: 13px;
  font-weight: 700;
  letter-spacing: 0.02em;
}

.form-content,
.toolbar-content {
  margin-bottom: 14px;
  padding: 18px 20px 4px;
  border: 1px solid #d8e1ee;
  border-radius: 10px;
  background: #ffffff;
  box-shadow: 0 6px 16px rgba(15, 23, 42, 0.04);
  box-sizing: border-box;
}

.toolbar-content {
  padding: 14px 20px 0;
  display: flex;
  justify-content: flex-end;
}

.form-content ::v-deep .el-form-item,
.toolbar-content ::v-deep .el-form-item {
  margin-right: 14px;
  margin-bottom: 14px;
}

.form-content ::v-deep .el-form-item__label {
  color: #344054;
  font-weight: 600;
}

.form-content ::v-deep .el-input__inner {
  height: 36px;
  line-height: 36px;
  border-color: #d8e1ee;
  border-radius: 7px;
}

.form-content ::v-deep .el-input__inner:focus {
  border-color: #2563eb;
}

.form-content ::v-deep .el-button,
.toolbar-content ::v-deep .el-button {
  height: 36px;
  padding: 0 16px;
  border-radius: 7px;
  font-weight: 600;
}

.table-content {
  padding: 18px 20px 20px;
  border: 1px solid #d8e1ee;
  border-radius: 10px;
  background: #ffffff;
  box-shadow: 0 6px 16px rgba(15, 23, 42, 0.04);
}

.table-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 14px;
}

.table-head h2 {
  margin: 0 0 4px;
  color: #0f172a;
  font-size: 20px;
  line-height: 1.25;
  font-weight: 800;
}

.table-head p {
  margin: 0;
  color: #667085;
  font-size: 13px;
}

.tables {
  border-radius: 8px;
  overflow: hidden;
}

.tables ::v-deep .el-table__header th {
  background: #f6f8fb;
  color: #344054;
  font-weight: 700;
}

.tables ::v-deep .el-table__cell {
  padding: 10px 0;
}

.tables ::v-deep .el-table__body tr:hover > td {
  background: #f8fbff;
}

.link-btn {
  color: #0b57d0;
  text-decoration: none;
  font-weight: 700;
}

.danger-text {
  color: #f56c6c;
}

.pagination-content {
  margin-top: 18px;
  text-align: right;
}

@media (max-width: 768px) {
  .module-head {
    align-items: flex-start;
    flex-direction: column;
  }

  .module-head h1 {
    font-size: 24px;
  }

  .form-content,
  .toolbar-content,
  .table-content {
    padding-left: 14px;
    padding-right: 14px;
  }
}
</style>
