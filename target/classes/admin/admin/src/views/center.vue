<template>
  <div class="center-page">
    <el-form
      ref="ruleForm"
      :model="ruleForm"
      :rules="rules"
      label-width="100px"
      class="detail-form-content"
    >
      <el-row :gutter="16">
        <el-col :span="12" v-if="isAdmin">
          <el-form-item label="账号" prop="username">
            <el-input v-model="ruleForm.username" placeholder="请输入账号" clearable />
          </el-form-item>
        </el-col>

        <template v-if="isCoach">
          <el-col :span="12">
            <el-form-item label="教练编号">
              <el-input v-model="ruleForm.jiaolianUuidNumber" readonly />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="教练姓名" prop="jiaolianName">
              <el-input v-model="ruleForm.jiaolianName" placeholder="请输入教练姓名" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="jiaolianPhone">
              <el-input v-model="ruleForm.jiaolianPhone" placeholder="请输入手机号" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份证号" prop="jiaolianIdNumber">
              <el-input v-model="ruleForm.jiaolianIdNumber" placeholder="请输入身份证号" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="头像" prop="jiaolianPhoto">
              <file-upload
                tip="上传头像"
                action="file/upload"
                :limit="1"
                :multiple="false"
                :fileUrls="ruleForm.jiaolianPhoto ? $base.url + ruleForm.jiaolianPhoto : ''"
                @change="jiaolianPhotoUploadChange"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="jiaolianEmail">
              <el-input v-model="ruleForm.jiaolianEmail" placeholder="请输入邮箱" clearable />
            </el-form-item>
          </el-col>
        </template>

        <template v-if="isUser">
          <el-col :span="12">
            <el-form-item label="用户编号">
              <el-input v-model="ruleForm.yonghuUuidNumber" readonly />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户姓名" prop="yonghuName">
              <el-input v-model="ruleForm.yonghuName" placeholder="请输入用户姓名" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="yonghuPhone">
              <el-input v-model="ruleForm.yonghuPhone" placeholder="请输入手机号" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份证号" prop="yonghuIdNumber">
              <el-input v-model="ruleForm.yonghuIdNumber" placeholder="请输入身份证号" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="头像" prop="yonghuPhoto">
              <file-upload
                tip="上传头像"
                action="file/upload"
                :limit="1"
                :multiple="false"
                :fileUrls="ruleForm.yonghuPhoto ? $base.url + ruleForm.yonghuPhoto : ''"
                @change="yonghuPhotoUploadChange"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="yonghuEmail">
              <el-input v-model="ruleForm.yonghuEmail" placeholder="请输入邮箱" clearable />
            </el-form-item>
          </el-col>
        </template>

        <el-col :span="12" v-if="showSexField">
          <el-form-item label="性别" prop="sexTypes">
            <el-select v-model="ruleForm.sexTypes" placeholder="请选择性别" clearable>
              <el-option
                v-for="item in sexTypesOptions"
                :key="item.codeIndex"
                :label="item.indexName"
                :value="item.codeIndex"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item class="btn-row">
        <el-button type="primary" @click="onUpdateHandler">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { isEmail, isMobile } from '@/utils/validate'

export default {
  data() {
    return {
      ruleForm: {},
      flag: '',
      sexTypesOptions: []
    }
  },
  computed: {
    isAdmin() {
      return this.flag === 'users'
    },
    isCoach() {
      return this.flag === 'jiaolian'
    },
    isUser() {
      return this.flag === 'yonghu'
    },
    showSexField() {
      return this.isCoach || this.isUser
    },
    rules() {
      const rules = {}
      if (this.isAdmin) {
        rules.username = [{ required: true, message: '请输入账号', trigger: 'blur' }]
      }
      if (this.isCoach) {
        rules.jiaolianName = [{ required: true, message: '请输入教练姓名', trigger: 'blur' }]
        rules.jiaolianPhone = [{ required: true, message: '请输入手机号', trigger: 'blur' }]
        rules.jiaolianIdNumber = [{ required: true, message: '请输入身份证号', trigger: 'blur' }]
        rules.jiaolianPhoto = [{ required: true, message: '请上传头像', trigger: 'change' }]
        rules.jiaolianEmail = [{ required: true, message: '请输入邮箱', trigger: 'blur' }]
        rules.sexTypes = [{ required: true, message: '请选择性别', trigger: 'change' }]
      }
      if (this.isUser) {
        rules.yonghuName = [{ required: true, message: '请输入用户姓名', trigger: 'blur' }]
        rules.yonghuPhone = [{ required: true, message: '请输入手机号', trigger: 'blur' }]
        rules.yonghuIdNumber = [{ required: true, message: '请输入身份证号', trigger: 'blur' }]
        rules.yonghuPhoto = [{ required: true, message: '请上传头像', trigger: 'change' }]
        rules.yonghuEmail = [{ required: true, message: '请输入邮箱', trigger: 'blur' }]
        rules.sexTypes = [{ required: true, message: '请选择性别', trigger: 'change' }]
      }
      return rules
    }
  },
  mounted() {
    this.flag = this.$storage.get('sessionTable') || 'users'
    this.loadSession()
    if (this.showSexField) {
      this.loadSexTypes()
    }
  },
  methods: {
    loadSession() {
      this.$http({
        url: `${this.flag}/session`,
        method: 'get'
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.ruleForm = data.data || {}
        } else {
          this.$message.error(data.msg || '加载个人信息失败')
        }
      })
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
        } else {
          this.$message.error(data.msg || '加载性别类型失败')
        }
      })
    },
    jiaolianPhotoUploadChange(fileUrls) {
      this.ruleForm.jiaolianPhoto = fileUrls
      this.$refs.ruleForm && this.$refs.ruleForm.validateField('jiaolianPhoto')
    },
    yonghuPhotoUploadChange(fileUrls) {
      this.ruleForm.yonghuPhoto = fileUrls
      this.$refs.ruleForm && this.$refs.ruleForm.validateField('yonghuPhoto')
    },
    validateExtra() {
      if (this.isCoach) {
        if (this.ruleForm.jiaolianPhone && !isMobile(this.ruleForm.jiaolianPhone)) {
          this.$message.error('手机号格式不正确')
          return false
        }
        if (this.ruleForm.jiaolianEmail && !isEmail(this.ruleForm.jiaolianEmail)) {
          this.$message.error('邮箱格式不正确')
          return false
        }
      }
      if (this.isUser) {
        if (this.ruleForm.yonghuPhone && !isMobile(this.ruleForm.yonghuPhone)) {
          this.$message.error('手机号格式不正确')
          return false
        }
        if (this.ruleForm.yonghuEmail && !isEmail(this.ruleForm.yonghuEmail)) {
          this.$message.error('邮箱格式不正确')
          return false
        }
      }
      return true
    },
    onUpdateHandler() {
      this.$refs.ruleForm.validate(valid => {
        if (!valid || !this.validateExtra()) return
        this.$http({
          url: `${this.flag}/update`,
          method: 'post',
          data: this.ruleForm
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('个人信息保存成功')
            this.loadSession()
          } else {
            this.$message.error(data.msg || '保存失败')
          }
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.center-page {
  .btn-row {
    margin-top: 8px;
  }
}
</style>
