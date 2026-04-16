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
          <el-form-item label="密码" prop="password">
            <el-input
              v-model="ruleForm.password"
              :readonly="type === 'info'"
              placeholder="请输入密码"
              clearable
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="角色" prop="role">
            <template v-if="type === 'info'">
              <el-input v-model="ruleForm.role" readonly />
            </template>
            <template v-else>
              <el-select v-model="ruleForm.role" placeholder="请选择角色" clearable>
                <el-option label="管理员" value="管理员" />
                <el-option label="教练" value="教练" />
                <el-option label="用户" value="用户" />
              </el-select>
            </template>
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
      ruleForm: {
        id: '',
        username: '',
        password: '',
        role: '管理员'
      },
      rules: {
        username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        role: [{ required: true, message: '请选择角色', trigger: 'change' }]
      }
    }
  },
  methods: {
    init(id, type) {
      this.id = id || null
      this.type = type || 'edit'
      this.ruleForm = {
        id: '',
        username: '',
        password: '',
        role: '管理员'
      }
      if (this.id) {
        this.info(this.id)
      }
    },
    info(id) {
      this.$http({
        url: `users/info/${id}`,
        method: 'get'
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.ruleForm = Object.assign(
            {
              id: '',
              username: '',
              password: '',
              role: '管理员'
            },
            data.data || {}
          )
        } else {
          this.$message.error(data.msg || '加载失败')
        }
      })
    },
    onSubmit() {
      this.$refs.ruleForm.validate(valid => {
        if (!valid) return
        const formData = { ...this.ruleForm }
        this.$http({
          url: `users/${!formData.id ? 'save' : 'update'}`,
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
</style>
