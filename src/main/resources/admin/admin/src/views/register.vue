<template>
  <div class="register-page">
    <div class="register-shell">
      <section class="register-brand">
        <p class="badge">PLAYER · COACH · CLUB</p>
        <h1>加入足球俱乐部管理系统</h1>
        <p class="desc">
          完成注册后可登录管理端，进行训练计划、赛事安排、球员数据与公告信息等业务操作。
        </p>
        <ul class="feature-list">
          <li><i class="el-icon-notebook-2"></i><span>统一资料录入，减少重复沟通成本</span></li>
          <li><i class="el-icon-data-line"></i><span>支持角色权限隔离，数据更安全</span></li>
          <li><i class="el-icon-medal-1"></i><span>围绕俱乐部日常管理场景设计</span></li>
        </ul>
      </section>

      <section class="register-form-panel">
        <div class="title-wrap">
          <h2>{{ roleTitle }}注册</h2>
          <p>请准确填写以下信息，标注 * 的字段为必填。</p>
        </div>

        <el-form class="register-form" label-width="106px" @keyup.enter.native="submitRegister">
          <el-form-item label="账号 *" class="field">
            <el-input v-model.trim="ruleForm.username" placeholder="请输入账号" autocomplete="off" />
          </el-form-item>

          <el-form-item label="密码 *" class="field">
            <el-input v-model="ruleForm.password" type="password" show-password placeholder="请输入密码（至少6位）" autocomplete="off" />
          </el-form-item>

          <el-form-item label="重复密码 *" class="field">
            <el-input v-model="ruleForm.repetitionPassword" type="password" show-password placeholder="请再次输入密码" autocomplete="off" />
          </el-form-item>

          <el-form-item v-if="isCoach" label="教练姓名 *" class="field">
            <el-input v-model.trim="ruleForm.jiaolianName" placeholder="请输入教练姓名" autocomplete="off" />
          </el-form-item>

          <el-form-item v-if="isCoach" label="教练手机号" class="field">
            <el-input v-model.trim="ruleForm.jiaolianPhone" placeholder="请输入教练手机号" autocomplete="off" />
          </el-form-item>

          <el-form-item v-if="isCoach" label="教练身份证号 *" class="field">
            <el-input v-model.trim="ruleForm.jiaolianIdNumber" placeholder="请输入教练身份证号" autocomplete="off" />
          </el-form-item>

          <el-form-item v-if="isCoach" label="教练邮箱" class="field">
            <el-input v-model.trim="ruleForm.jiaolianEmail" placeholder="请输入教练邮箱" autocomplete="off" />
          </el-form-item>

          <el-form-item v-if="isUser" label="用户姓名 *" class="field">
            <el-input v-model.trim="ruleForm.yonghuName" placeholder="请输入用户姓名" autocomplete="off" />
          </el-form-item>

          <el-form-item v-if="isUser" label="用户手机号" class="field">
            <el-input v-model.trim="ruleForm.yonghuPhone" placeholder="请输入用户手机号" autocomplete="off" />
          </el-form-item>

          <el-form-item v-if="isUser" label="用户身份证号 *" class="field">
            <el-input v-model.trim="ruleForm.yonghuIdNumber" placeholder="请输入用户身份证号" autocomplete="off" />
          </el-form-item>

          <el-form-item v-if="isUser" label="用户邮箱" class="field">
            <el-input v-model.trim="ruleForm.yonghuEmail" placeholder="请输入用户邮箱" autocomplete="off" />
          </el-form-item>

          <div class="btn-row">
            <el-button type="primary" class="submit-btn" @click="submitRegister">完成注册</el-button>
            <el-button class="cancel-btn" @click="close">返回登录</el-button>
          </div>
        </el-form>

        <p class="tips">注册成功后，请登录并在个人中心完善详细资料。</p>
      </section>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      ruleForm: {
        username: "",
        password: "",
        repetitionPassword: "",
        jiaolianName: "",
        jiaolianPhone: "",
        jiaolianIdNumber: "",
        jiaolianEmail: "",
        yonghuName: "",
        yonghuPhone: "",
        yonghuIdNumber: "",
        yonghuEmail: "",
      },
      tableName: "",
    };
  },
  computed: {
    isCoach() {
      return this.tableName === "jiaolian";
    },
    isUser() {
      return this.tableName === "yonghu";
    },
    roleTitle() {
      if (this.isCoach) return "教练";
      if (this.isUser) return "用户";
      return "账号";
    },
  },
  mounted() {
    const table = this.$storage.get("loginTable");
    this.tableName = table || "jiaolian";
  },
  methods: {
    close() {
      this.$router.push({ path: "/login" });
    },
    submitRegister() {
      if (!this.ruleForm.username) {
        this.$message.error("账号不能为空");
        return;
      }
      if (!this.ruleForm.password) {
        this.$message.error("密码不能为空");
        return;
      }
      if (this.ruleForm.password.length < 6) {
        this.$message.error("密码长度不能少于6位");
        return;
      }
      if (!this.ruleForm.repetitionPassword) {
        this.$message.error("重复密码不能为空");
        return;
      }
      if (this.ruleForm.repetitionPassword !== this.ruleForm.password) {
        this.$message.error("密码和重复密码不一致");
        return;
      }

      if (this.isCoach) {
        if (!this.ruleForm.jiaolianName) {
          this.$message.error("教练姓名不能为空");
          return;
        }
        if (this.ruleForm.jiaolianPhone && !this.$validate.isMobile(this.ruleForm.jiaolianPhone)) {
          this.$message.error("教练手机号格式不正确");
          return;
        }
        if (!this.ruleForm.jiaolianIdNumber) {
          this.$message.error("教练身份证号不能为空");
          return;
        }
        if (this.ruleForm.jiaolianEmail && !this.$validate.isEmail(this.ruleForm.jiaolianEmail)) {
          this.$message.error("教练邮箱格式不正确");
          return;
        }
      }

      if (this.isUser) {
        if (!this.ruleForm.yonghuName) {
          this.$message.error("用户姓名不能为空");
          return;
        }
        if (this.ruleForm.yonghuPhone && !this.$validate.isMobile(this.ruleForm.yonghuPhone)) {
          this.$message.error("用户手机号格式不正确");
          return;
        }
        if (!this.ruleForm.yonghuIdNumber) {
          this.$message.error("用户身份证号不能为空");
          return;
        }
        if (this.ruleForm.yonghuEmail && !this.$validate.isEmail(this.ruleForm.yonghuEmail)) {
          this.$message.error("用户邮箱格式不正确");
          return;
        }
      }

      this.$http({
        url: `${this.tableName}/register`,
        method: "post",
        data: this.ruleForm,
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message({
            message: "注册成功，请登录后在个人中心页面补充个人数据",
            type: "success",
            duration: 1500,
            onClose: () => {
              this.$router.replace({ path: "/login" });
            },
          });
        } else {
          this.$message.error(data.msg || "注册失败");
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.register-page {
  --primary: #0d8bf2;
  --primary-dark: #0a63c9;
  --accent: #ff8a3d;
  min-height: 100vh;
  width: 100%;
  padding: 28px;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  justify-content: center;
  background-image: linear-gradient(120deg, rgba(9, 34, 64, 0.78), rgba(17, 104, 173, 0.52)),
    url(/zuqiujulebguanli/img/back-img-bg.jpg);
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;
  overflow: auto;
  font-family: var(--app-font-family);
}

.register-shell {
  width: min(1100px, 100%);
  min-height: 640px;
  display: grid;
  grid-template-columns: 0.92fr 1.08fr;
  border-radius: 24px;
  overflow: hidden;
  border: 1px solid rgba(255, 255, 255, 0.28);
  box-shadow: 0 28px 60px rgba(9, 20, 40, 0.46);
}

.register-brand {
  padding: 56px 44px;
  color: #f7fbff;
  background: linear-gradient(165deg, rgba(5, 33, 60, 0.94), rgba(11, 70, 125, 0.82));
}

.badge {
  margin: 0;
  font-size: 12px;
  letter-spacing: 2px;
  opacity: 0.88;
}

.register-brand h1 {
  margin: 14px 0;
  font-size: 38px;
  line-height: 1.2;
}

.desc {
  margin: 0;
  line-height: 1.8;
  font-size: 15px;
  opacity: 0.94;
}

.feature-list {
  margin: 30px 0 0;
  list-style: none;
  padding: 0;
}

.feature-list li {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 16px;
  font-size: 15px;
}

.feature-list i {
  width: 30px;
  height: 30px;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.18);
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.register-form-panel {
  padding: 38px 38px 24px;
  background: linear-gradient(180deg, rgba(247, 251, 255, 0.96), rgba(236, 246, 255, 0.9));
  overflow: auto;
}

.title-wrap h2 {
  margin: 0;
  font-size: 30px;
  color: #10294a;
}

.title-wrap p {
  margin: 10px 0 18px;
  color: #4d6486;
  font-size: 14px;
}

.register-form {
  padding-right: 4px;
}

.field {
  margin-bottom: 14px;
}

.field ::v-deep .el-form-item__label {
  color: #23466f;
  font-weight: 700;
  line-height: 42px;
}

.field ::v-deep .el-input__inner {
  height: 42px;
  border-radius: 12px;
  border: 1px solid #c2d5ee;
  background: rgba(255, 255, 255, 0.94);
  color: #17365f;
  transition: all 0.22s ease;
}

.field ::v-deep .el-input__inner:focus {
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(13, 139, 242, 0.15);
}

.btn-row {
  margin-top: 8px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.submit-btn,
.cancel-btn {
  width: 100%;
  height: 44px;
  border-radius: 12px;
  font-size: 15px;
}

.submit-btn {
  border: none;
  background: linear-gradient(135deg, var(--primary), var(--primary-dark));
  box-shadow: 0 10px 20px rgba(13, 139, 242, 0.3);
}

.cancel-btn {
  color: #285380;
  border: 1px solid #b7cee8;
  background: linear-gradient(135deg, #f9fcff, #eaf3ff);
}

.cancel-btn:hover {
  color: #1e476f;
  border-color: #8eb1d8;
}

.tips {
  margin: 16px 0 0;
  text-align: center;
  font-size: 12px;
  color: #5f7696;
}

@media (max-width: 980px) {
  .register-page {
    padding: 18px;
  }

  .register-shell {
    grid-template-columns: 1fr;
    min-height: auto;
  }

  .register-brand {
    padding: 30px 22px;
  }

  .register-brand h1 {
    font-size: 30px;
  }

  .register-form-panel {
    padding: 28px 18px 20px;
  }
}

@media (max-width: 600px) {
  .register-page {
    padding: 10px;
  }

  .register-shell {
    border-radius: 18px;
  }

  .register-brand {
    padding: 24px 16px;
  }

  .register-brand h1 {
    font-size: 24px;
  }

  .title-wrap h2 {
    font-size: 24px;
  }

  .field ::v-deep .el-form-item__label {
    width: 90px !important;
    font-size: 13px;
  }

  .btn-row {
    grid-template-columns: 1fr;
  }
}
</style>
