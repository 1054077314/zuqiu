<template>
  <div>
    <el-form
      class="detail-form-content"
      ref="ruleForm"
      :rules="rules"
      :model="ruleForm"
      label-width="90px"
    >
      <el-form-item label="原密码" prop="password">
        <el-input v-model="ruleForm.password" type="password" show-password />
      </el-form-item>
      <el-form-item label="新密码" prop="newpassword">
        <el-input v-model="ruleForm.newpassword" type="password" show-password />
      </el-form-item>
      <el-form-item label="确认密码" prop="repassword">
        <el-input v-model="ruleForm.repassword" type="password" show-password />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onUpdateHandler">保存密码</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      ruleForm: {
        password: '',
        newpassword: '',
        repassword: ''
      },
      user: {},
      rules: {
        password: [{ required: true, message: '原密码不能为空', trigger: 'blur' }],
        newpassword: [{ required: true, message: '新密码不能为空', trigger: 'blur' }],
        repassword: [{ required: true, message: '确认密码不能为空', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.$http({
      url: `${this.$storage.get('sessionTable')}/session`,
      method: 'get'
    }).then(({ data }) => {
      if (data && data.code === 0) {
        this.user = data.data || {}
      } else {
        this.$message.error(data.msg || '加载用户信息失败')
      }
    })
  },
  methods: {
    onUpdateHandler() {
      this.$refs.ruleForm.validate(valid => {
        if (!valid) return

        const oldPassword = this.user.mima || this.user.password || ''
        if (this.ruleForm.password !== oldPassword) {
          this.$message.error('原密码错误')
          return
        }
        if (this.ruleForm.newpassword.length < 6) {
          this.$message.error('新密码长度不能少于 6 位')
          return
        }
        if (this.ruleForm.newpassword !== this.ruleForm.repassword) {
          this.$message.error('两次输入的密码不一致')
          return
        }

        this.user.password = this.ruleForm.newpassword
        this.user.mima = this.ruleForm.newpassword

        this.$http({
          url: `${this.$storage.get('sessionTable')}/update`,
          method: 'post',
          data: this.user
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('密码修改成功，下次登录生效')
            this.ruleForm = {
              password: '',
              newpassword: '',
              repassword: ''
            }
          } else {
            this.$message.error(data.msg || '密码修改失败')
          }
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
