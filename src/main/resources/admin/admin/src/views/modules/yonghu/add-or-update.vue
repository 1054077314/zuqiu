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
        <el-col :span="12">
          <el-form-item label="账号" prop="username">
            <el-input
              v-model="ruleForm.username"
              :readonly="type === 'info'"
              placeholder="请输入账号"
              clearable
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="用户编号" prop="yonghuUuidNumber">
            <el-input v-model="ruleForm.yonghuUuidNumber" readonly />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="用户姓名" prop="yonghuName">
            <el-input
              v-model="ruleForm.yonghuName"
              :readonly="type === 'info'"
              placeholder="请输入用户姓名"
              clearable
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="手机号" prop="yonghuPhone">
            <el-input
              v-model="ruleForm.yonghuPhone"
              :readonly="type === 'info'"
              placeholder="请输入手机号"
              clearable
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="身份证号" prop="yonghuIdNumber">
            <el-input
              v-model="ruleForm.yonghuIdNumber"
              :readonly="type === 'info'"
              placeholder="请输入身份证号"
              clearable
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="性别" prop="sexTypes">
            <template v-if="type === 'info'">
              <el-input v-model="ruleForm.sexValue" readonly />
            </template>
            <template v-else>
              <el-select v-model="ruleForm.sexTypes" placeholder="请选择性别" clearable>
                <el-option
                  v-for="item in sexTypesOptions"
                  :key="item.codeIndex"
                  :label="item.indexName"
                  :value="item.codeIndex"
                />
              </el-select>
            </template>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="邮箱" prop="yonghuEmail">
            <el-input
              v-model="ruleForm.yonghuEmail"
              :readonly="type === 'info'"
              placeholder="请输入邮箱"
              clearable
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="头像" prop="yonghuPhoto">
            <template v-if="type === 'info'">
              <div v-if="ruleForm.yonghuPhoto">
                <img
                  v-for="(item, index) in (ruleForm.yonghuPhoto || '').split(',')"
                  :key="index"
                  :src="$base.url + item"
                  class="preview-image"
                  alt="头像"
                >
              </div>
              <span v-else>无</span>
            </template>
            <file-upload
              v-else
              tip="上传头像"
              action="file/upload"
              :limit="1"
              :multiple="false"
              :fileUrls="ruleForm.yonghuPhoto ? $base.url + ruleForm.yonghuPhoto : ''"
              @change="yonghuPhotoUploadChange"
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
      sexTypesOptions: [],
      ruleForm: {
        id: '',
        username: '',
        password: '',
        yonghuUuidNumber: '',
        yonghuName: '',
        yonghuPhone: '',
        yonghuIdNumber: '',
        yonghuPhoto: '',
        sexTypes: '',
        sexValue: '',
        yonghuEmail: ''
      },
      rules: {
        username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        yonghuUuidNumber: [{ required: true, message: '用户编号不能为空', trigger: 'blur' }],
        yonghuName: [{ required: true, message: '请输入用户姓名', trigger: 'blur' }],
        yonghuPhone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          {
            pattern: /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/,
            message: '手机号格式不正确',
            trigger: 'blur'
          }
        ],
        yonghuIdNumber: [
          { required: true, message: '请输入身份证号', trigger: 'blur' },
          {
            pattern: /^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,
            message: '身份证号格式不正确',
            trigger: 'blur'
          }
        ],
        yonghuPhoto: [{ required: true, message: '请上传头像', trigger: 'change' }],
        sexTypes: [{ required: true, message: '请选择性别', trigger: 'change' }],
        yonghuEmail: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          {
            pattern: /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-]+)+$/,
            message: '邮箱格式不正确',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  created() {
    this.loadSexTypes()
  },
  methods: {
    init(id, type) {
      this.id = id || null
      this.type = type || 'edit'
      this.ruleForm = {
        id: '',
        username: '',
        password: '',
        yonghuUuidNumber: `${Date.now()}`,
        yonghuName: '',
        yonghuPhone: '',
        yonghuIdNumber: '',
        yonghuPhoto: '',
        sexTypes: '',
        sexValue: '',
        yonghuEmail: ''
      }
      if (this.id) {
        this.info(this.id)
      }
    },
    loadSexTypes() {
      this.$http({
        url: 'dictionary/page',
        method: 'get',
        params: {
          page: 1,
          limit: 100,
          dicCode: 'sex_types'
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.sexTypesOptions = data.data.list || []
        }
      })
    },
    info(id) {
      this.$http({
        url: `yonghu/info/${id}`,
        method: 'get'
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.ruleForm = Object.assign(
            {
              id: '',
              username: '',
              password: '',
              yonghuUuidNumber: '',
              yonghuName: '',
              yonghuPhone: '',
              yonghuIdNumber: '',
              yonghuPhoto: '',
              sexTypes: '',
              sexValue: '',
              yonghuEmail: ''
            },
            data.data || {}
          )
        } else {
          this.$message.error(data.msg || '加载失败')
        }
      })
    },
    yonghuPhotoUploadChange(fileUrls) {
      this.ruleForm.yonghuPhoto = fileUrls
      if (this.$refs.ruleForm) {
        this.$refs.ruleForm.validateField('yonghuPhoto')
      }
    },
    onSubmit() {
      this.$refs.ruleForm.validate(valid => {
        if (!valid) return
        const formData = { ...this.ruleForm }
        if (!formData.password) {
          delete formData.password
        }
        delete formData.sexValue
        this.$http({
          url: `yonghu/${!formData.id ? 'save' : 'update'}`,
          method: 'post',
          data: formData
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

.preview-image {
  width: 96px;
  height: 96px;
  border-radius: 50%;
  object-fit: cover;
  border: 1px solid #ebeef5;
  margin-right: 12px;
}
</style>
