<template>
  <div class="addEdit-block">
    <el-form
      ref="ruleForm"
      :model="ruleForm"
      :rules="rules"
      label-width="100px"
      class="detail-form-content"
    >
      <el-row :gutter="16">
        <el-col :span="12" v-if="sessionTable !== 'yonghu'">
          <el-form-item label="用户" prop="yonghuId">
            <el-select
              v-model="ruleForm.yonghuId"
              placeholder="请选择用户"
              filterable
              clearable
              :disabled="type === 'info'"
              @change="yonghuChange"
            >
              <el-option
                v-for="item in yonghuOptions"
                :key="item.id"
                :label="item.yonghuName"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12" v-if="sessionTable !== 'yonghu'">
          <el-form-item label="用户编号">
            <el-input v-model="yonghuForm.yonghuUuidNumber" readonly />
          </el-form-item>
        </el-col>

        <el-col :span="12" v-if="sessionTable !== 'yonghu'">
          <el-form-item label="用户姓名">
            <el-input v-model="yonghuForm.yonghuName" readonly />
          </el-form-item>
        </el-col>

        <el-col :span="12" v-if="sessionTable !== 'yonghu'">
          <el-form-item label="手机号">
            <el-input v-model="yonghuForm.yonghuPhone" readonly />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="合同标题" prop="hetongName">
            <el-input
              v-model="ruleForm.hetongName"
              :readonly="type === 'info'"
              placeholder="请输入合同标题"
              clearable
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="合同附件" prop="hetongFile">
            <div v-if="type === 'info'">
              <a
                v-if="ruleForm.hetongFile"
                class="link-btn"
                :href="$base.url + ruleForm.hetongFile"
                target="_blank"
              >下载合同附件</a>
              <span v-else>无</span>
            </div>
            <file-upload
              v-else
              tip="上传合同附件"
              action="file/upload"
              :limit="1"
              :multiple="false"
              :fileUrls="ruleForm.hetongFile ? $base.url + ruleForm.hetongFile : ''"
              @change="hetongFileUploadChange"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="备注" prop="hetongText">
            <el-input
              v-model="ruleForm.hetongText"
              type="textarea"
              :rows="4"
              :readonly="type === 'info'"
              placeholder="请输入备注"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item class="btn">
        <el-button v-if="type !== 'info'" type="primary" @click="onSubmit">保存</el-button>
        <el-button @click="back">{{ type === 'info' ? '返回' : '取消' }}</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  props: ['parent'],
  data() {
    return {
      id: null,
      type: 'edit',
      sessionTable: '',
      role: '',
      userId: '',
      yonghuOptions: [],
      yonghuForm: {
        yonghuUuidNumber: '',
        yonghuName: '',
        yonghuPhone: ''
      },
      ruleForm: {
        id: '',
        yonghuId: '',
        hetongName: '',
        hetongFile: '',
        hetongText: '',
        hetongDelete: 1
      },
      rules: {
        yonghuId: [{ required: true, message: '请选择用户', trigger: 'change' }],
        hetongName: [{ required: true, message: '请输入合同标题', trigger: 'blur' }],
        hetongFile: [{ required: true, message: '请上传合同附件', trigger: 'change' }],
        hetongText: [{ required: true, message: '请输入备注', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.sessionTable = this.$storage.get('sessionTable')
    this.role = this.$storage.get('role')
    this.userId = this.$storage.get('userId')
    this.loadYonghuOptions()
  },
  methods: {
    init(id, type) {
      this.id = id || null
      this.type = type || 'edit'
      this.ruleForm = {
        id: '',
        yonghuId: '',
        hetongName: '',
        hetongFile: '',
        hetongText: '',
        hetongDelete: 1
      }
      this.yonghuForm = {
        yonghuUuidNumber: '',
        yonghuName: '',
        yonghuPhone: ''
      }
      if (this.sessionTable === 'yonghu') {
        this.ruleForm.yonghuId = Number(this.userId)
        this.yonghuChange(this.ruleForm.yonghuId)
      }
      if (this.id) {
        this.info(this.id)
      }
    },
    loadYonghuOptions() {
      this.$http({
        url: 'yonghu/page',
        method: 'get',
        params: { page: 1, limit: 1000 }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.yonghuOptions = data.data.list || []
        }
      })
    },
    yonghuChange(id) {
      if (!id) {
        this.yonghuForm = {
          yonghuUuidNumber: '',
          yonghuName: '',
          yonghuPhone: ''
        }
        return
      }
      this.$http({
        url: `yonghu/info/${id}`,
        method: 'get'
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.yonghuForm = data.data || {}
        }
      })
    },
    info(id) {
      this.$http({
        url: `hetong/info/${id}`,
        method: 'get'
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.ruleForm = Object.assign(
            {
              id: '',
              yonghuId: '',
              hetongName: '',
              hetongFile: '',
              hetongText: '',
              hetongDelete: 1
            },
            data.data || {}
          )
          if (this.ruleForm.yonghuId) {
            this.yonghuChange(this.ruleForm.yonghuId)
          }
        } else {
          this.$message.error(data.msg || '加载详情失败')
        }
      })
    },
    hetongFileUploadChange(fileUrls) {
      this.ruleForm.hetongFile = fileUrls
      if (this.$refs.ruleForm) {
        this.$refs.ruleForm.validateField('hetongFile')
      }
    },
    onSubmit() {
      this.$refs.ruleForm.validate(valid => {
        if (!valid) {
          return
        }
        if (this.sessionTable === 'yonghu') {
          this.ruleForm.yonghuId = Number(this.userId)
        }
        this.$http({
          url: `hetong/${!this.ruleForm.id ? 'save' : 'update'}`,
          method: 'post',
          data: this.ruleForm
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('保存成功')
            this.parent.showFlag = true
            this.parent.addOrUpdateFlag = false
            this.parent.getDataList()
          } else {
            this.$message.error(data.msg || '保存失败')
          }
        })
      })
    },
    back() {
      this.parent.showFlag = true
      this.parent.addOrUpdateFlag = false
    }
  }
}
</script>

<style lang="scss" scoped>
.detail-form-content {
  padding: 8px 4px;
}

.link-btn {
  color: #409eff;
  text-decoration: none;
}

.btn {
  margin-top: 8px;
}
</style>
