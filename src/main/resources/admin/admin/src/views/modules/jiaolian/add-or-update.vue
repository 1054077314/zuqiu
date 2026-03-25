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
          <el-form-item label="教练编号" prop="jiaolianUuidNumber">
            <el-input v-model="ruleForm.jiaolianUuidNumber" readonly />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="教练姓名" prop="jiaolianName">
            <el-input
              v-model="ruleForm.jiaolianName"
              :readonly="type === 'info'"
              placeholder="请输入教练姓名"
              clearable
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="手机号" prop="jiaolianPhone">
            <el-input
              v-model="ruleForm.jiaolianPhone"
              :readonly="type === 'info'"
              placeholder="请输入手机号"
              clearable
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="身份证号" prop="jiaolianIdNumber">
            <el-input
              v-model="ruleForm.jiaolianIdNumber"
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
          <el-form-item label="邮箱" prop="jiaolianEmail">
            <el-input
              v-model="ruleForm.jiaolianEmail"
              :readonly="type === 'info'"
              placeholder="请输入邮箱"
              clearable
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="头像" prop="jiaolianPhoto">
            <template v-if="type === 'info'">
              <div v-if="ruleForm.jiaolianPhoto">
                <img
                  v-for="(item, index) in (ruleForm.jiaolianPhoto || '').split(',')"
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
              :fileUrls="ruleForm.jiaolianPhoto ? $base.url + ruleForm.jiaolianPhoto : ''"
              @change="jiaolianPhotoUploadChange"
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
        jiaolianUuidNumber: '',
        jiaolianName: '',
        jiaolianPhone: '',
        jiaolianIdNumber: '',
        jiaolianPhoto: '',
        sexTypes: '',
        sexValue: '',
        jiaolianEmail: ''
      },
      rules: {
        username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        jiaolianUuidNumber: [{ required: true, message: '教练编号不能为空', trigger: 'blur' }],
        jiaolianName: [{ required: true, message: '请输入教练姓名', trigger: 'blur' }],
        jiaolianPhone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          {
            pattern: /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/,
            message: '手机号格式不正确',
            trigger: 'blur'
          }
        ],
        jiaolianIdNumber: [
          { required: true, message: '请输入身份证号', trigger: 'blur' },
          {
            pattern: /^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,
            message: '身份证号格式不正确',
            trigger: 'blur'
          }
        ],
        jiaolianPhoto: [{ required: true, message: '请上传头像', trigger: 'change' }],
        sexTypes: [{ required: true, message: '请选择性别', trigger: 'change' }],
        jiaolianEmail: [
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
        jiaolianUuidNumber: `${Date.now()}`,
        jiaolianName: '',
        jiaolianPhone: '',
        jiaolianIdNumber: '',
        jiaolianPhoto: '',
        sexTypes: '',
        sexValue: '',
        jiaolianEmail: ''
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
        url: `jiaolian/info/${id}`,
        method: 'get'
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.ruleForm = Object.assign(
            {
              id: '',
              username: '',
              password: '',
              jiaolianUuidNumber: '',
              jiaolianName: '',
              jiaolianPhone: '',
              jiaolianIdNumber: '',
              jiaolianPhoto: '',
              sexTypes: '',
              sexValue: '',
              jiaolianEmail: ''
            },
            data.data || {}
          )
        } else {
          this.$message.error(data.msg || '加载失败')
        }
      })
    },
    jiaolianPhotoUploadChange(fileUrls) {
      this.ruleForm.jiaolianPhoto = fileUrls
      if (this.$refs.ruleForm) {
        this.$refs.ruleForm.validateField('jiaolianPhoto')
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
          url: `jiaolian/${!formData.id ? 'save' : 'update'}`,
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
