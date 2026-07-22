<template>
  <div class="modern-register-wrapper">
    <div class="background-decoration">
      <div class="glow-top"></div>
      <div class="glow-bottom"></div>
    </div>

    <main class="main-content">
      <section class="brand-section">
        <div class="brand-content">
          <span class="system-tag">SOVEREIGN SYSTEM</span>
          <h1 class="main-title">
            加入<br/>管理系统
          </h1>
          <p class="description">
            完成注册后可登录管理端，进行训练计划、赛事安排、球员数据与公告信息等业务操作。
          </p>
          
          <ul class="feature-list">
            <li><i class="el-icon-notebook-2"></i><span>统一资料录入，减少沟通成本</span></li>
            <li><i class="el-icon-data-line"></i><span>支持角色权限隔离，数据更安全</span></li>
            <li><i class="el-icon-medal-1"></i><span>围绕俱乐部日常管理场景设计</span></li>
          </ul>

          <div class="status-card">
            <div class="status-info">
              <span class="status-label">REGISTRATION</span>
              <span class="status-value">Open Access</span>
            </div>
            <div class="divider"></div>
            <i class="el-icon-connection verified-icon"></i>
          </div>
        </div>
      </section>

      <section class="form-section">
        <div class="register-card">
          <div class="golden-bar"></div>
          
          <header class="form-header">
            <h2>{{ roleTitle }}注册</h2>
            <p>请准确填写以下信息，访问指挥中心权限。</p>
          </header>

          <el-form class="custom-form" ref="registerForm" @keyup.enter="submitRegister">
            
            <div class="form-grid">
              <div class="input-group">
                <label class="input-label">账号 *</label>
                <el-input v-model.trim="ruleForm.username" placeholder="Username" />
              </div>

              <div class="input-group">
                <label class="input-label">密码 *</label>
                <el-input v-model="ruleForm.password" type="password" show-password placeholder="Min. 6 characters" />
              </div>

              <div class="input-group full-width">
                <label class="input-label">重复密码 *</label>
                <el-input v-model="ruleForm.repetitionPassword" type="password" show-password placeholder="Repeat Password" />
              </div>

              <template v-if="isCoach">
                <div class="input-group">
                  <label class="input-label">教练姓名 *</label>
                  <el-input v-model.trim="ruleForm.jiaolianName" placeholder="Full Name" />
                </div>
                <div class="input-group">
                  <label class="input-label">身份证号 *</label>
                  <el-input v-model.trim="ruleForm.jiaolianIdNumber" placeholder="ID Number" />
                </div>
                <div class="input-group">
                  <label class="input-label">手机号</label>
                  <el-input v-model.trim="ruleForm.jiaolianPhone" placeholder="Phone Number" />
                </div>
                <div class="input-group">
                  <label class="input-label">邮箱</label>
                  <el-input v-model.trim="ruleForm.jiaolianEmail" placeholder="Email Address" />
                </div>
              </template>

              <template v-if="isUser">
                <div class="input-group">
                  <label class="input-label">用户姓名 *</label>
                  <el-input v-model.trim="ruleForm.yonghuName" placeholder="Full Name" />
                </div>
                <div class="input-group">
                  <label class="input-label">身份证号 *</label>
                  <el-input v-model.trim="ruleForm.yonghuIdNumber" placeholder="ID Number" />
                </div>
                <div class="input-group">
                  <label class="input-label">手机号</label>
                  <el-input v-model.trim="ruleForm.yonghuPhone" placeholder="Phone Number" />
                </div>
                <div class="input-group">
                  <label class="input-label">邮箱</label>
                  <el-input v-model.trim="ruleForm.yonghuEmail" placeholder="Email Address" />
                </div>
              </template>
            </div>

            <div class="action-row">
              <el-button type="primary" class="submit-btn" @click="submitRegister">
                完成注册 <i class="el-icon-right"></i>
              </el-button>
              <el-button type="text" class="back-link" @click="close">返回登录界面</el-button>
            </div>
          </el-form>
          
          <p class="bottom-tips">注册成功后，请在个人中心完善详细资料。</p>
        </div>
      </section>
    </main>

    <div class="side-info">EST. 2024 • SOVEREIGN GALLERY</div>
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
    isCoach() { return this.tableName === "jiaolian"; },
    isUser() { return this.tableName === "yonghu"; },
    roleTitle() {
      if (this.isCoach) return "教练";
      if (this.isUser) return "用户";
      return "账号";
    },
  },
  mounted() {
    this.tableName = this.$storage.get("loginTable") || "jiaolian";
  },
  methods: {
    close() { this.$router.push({ path: "/login" }); },
    submitRegister() {
      // 保持原有验证逻辑
      if (!this.ruleForm.username) return this.$message.error("账号不能为空");
      if (!this.ruleForm.password) return this.$message.error("密码不能为空");
      if (this.ruleForm.password.length < 6) return this.$message.error("密码长度不能少于6位");
      if (this.ruleForm.repetitionPassword !== this.ruleForm.password) return this.$message.error("密码不一致");

      if (this.isCoach) {
        if (!this.ruleForm.jiaolianName) return this.$message.error("教练姓名不能为空");
        if (!this.ruleForm.jiaolianIdNumber) return this.$message.error("教练身份证号不能为空");
        if (this.ruleForm.jiaolianPhone && !this.$validate.isMobile(this.ruleForm.jiaolianPhone)) return this.$message.error("手机号格式错误");
      }

      if (this.isUser) {
        if (!this.ruleForm.yonghuName) return this.$message.error("用户姓名不能为空");
        if (!this.ruleForm.yonghuIdNumber) return this.$message.error("用户身份证号不能为空");
      }

      this.$http({
        url: `${this.tableName}/register`,
        method: "post",
        data: this.ruleForm,
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message.success("注册成功");
          this.$router.replace({ path: "/login" });
        } else {
          this.$message.error(data.msg || "注册失败");
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.modern-register-wrapper {
  min-height: 100vh;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f9f9f9;
  position: relative;
  overflow: hidden;
  font-family: 'Inter', sans-serif;

  .background-decoration {
    position: absolute; inset: 0; z-index: 0;
    .glow-top { position: absolute; top: -10%; right: -5%; width: 600px; height: 600px; background: rgba(0, 8, 31, 0.05); filter: blur(120px); border-radius: 50%; }
    .glow-bottom { position: absolute; bottom: -10%; left: -5%; width: 500px; height: 500px; background: rgba(117, 90, 31, 0.05); filter: blur(100px); border-radius: 50%; }
  }

  .main-content {
    position: relative; z-index: 10;
    width: 100%; max-width: 1200px;
    display: grid; grid-template-columns: 0.9fr 1.1fr;
    padding: 20px 40px;
    align-items: center;
  }

  // 左侧品牌区
  .brand-section {
    padding-right: 60px;
    .system-tag { display: inline-block; padding: 4px 12px; background: #ffdea2; color: #755d22; font-size: 10px; font-weight: 800; border-radius: 100px; letter-spacing: 2px; margin-bottom: 24px; }
    .main-title { font-size: 64px; font-weight: 900; color: #00081f; line-height: 1; margin: 0 0 24px 0; letter-spacing: -2px; font-style: italic; }
    .description { font-size: 16px; color: #44464f; line-height: 1.6; margin-bottom: 30px; font-weight: 300; }
    
    .feature-list {
      list-style: none; padding: 0; margin-bottom: 40px;
      li { display: flex; align-items: center; gap: 12px; margin-bottom: 12px; color: #44464f; font-size: 14px;
        i { color: #755a1f; font-weight: bold; }
      }
    }

    .status-card {
      background: rgba(255, 255, 255, 0.85); backdrop-filter: blur(20px);
      padding: 20px; border-radius: 12px; display: flex; align-items: center; gap: 20px;
      box-shadow: 0 10px 40px rgba(0, 8, 31, 0.08); border-left: 4px solid #755a1f; width: fit-content;
      .status-label { display: block; color: #755a1f; font-size: 10px; font-weight: 800; letter-spacing: 2px; }
      .status-value { font-size: 22px; font-weight: 700; color: #00081f; }
      .divider { width: 1px; height: 30px; background: rgba(0, 0, 0, 0.1); }
      .verified-icon { font-size: 28px; color: #755a1f; }
    }
  }

  // 右侧表单区
  .form-section {
    .register-card {
      background: #ffffff; border-radius: 16px; padding: 40px;
      box-shadow: 0 30px 60px rgba(0, 8, 31, 0.12); position: relative; overflow: hidden;
      border-top: 1px solid rgba(255, 255, 255, 0.4);
      .golden-bar { position: absolute; top: 0; left: 0; width: 100%; height: 4px; background: linear-gradient(90deg, #00081f, #755a1f, #00081f); }
    }
  }

  .form-header {
    margin-bottom: 30px;
    h2 { font-size: 28px; font-weight: 700; color: #00081f; margin: 0 0 6px 0; }
    p { font-size: 13px; color: #44464f; font-weight: 300; }
  }

  .form-grid {
    display: grid; grid-template-columns: 1fr 1fr; gap: 16px;
    .full-width { grid-column: span 2; }
  }

  .input-group {
    margin-bottom: 16px;
    .input-label { display: block; font-size: 10px; font-weight: 800; color: #00081f; letter-spacing: 1.5px; text-transform: uppercase; margin-bottom: 6px; margin-left: 4px; }
    
    :deep(.el-input__inner){
      height: 48px; background: #f3f3f3; border: 1px solid transparent; border-radius: 8px; padding: 0 16px;
      transition: all 0.3s;
      &:focus { border-color: #755a1f; background: #fff; box-shadow: 0 0 0 4px rgba(117, 90, 31, 0.1); }
    }
  }

  .action-row {
    margin-top: 32px; display: flex; flex-direction: column; gap: 16px; align-items: center;
    .submit-btn {
      width: 100%; height: 54px; background: #00081f; border: none; border-radius: 8px;
      font-size: 13px; font-weight: 700; letter-spacing: 2px; text-transform: uppercase;
      box-shadow: 0 10px 20px rgba(0, 8, 31, 0.2); transition: all 0.3s;
      &:hover { background: #2f4677; transform: translateY(-2px); }
    }
    .back-link { color: #755a1f; font-weight: 700; font-size: 13px; }
  }

  .bottom-tips { margin-top: 24px; text-align: center; font-size: 11px; color: #999; }

  .side-info {
    position: fixed; bottom: 40px; right: 40px; font-size: 10px; font-weight: 800;
    color: #c5c6d0; letter-spacing: 4px; writing-mode: vertical-rl; transform: rotate(180deg);
  }
}

@media (max-width: 992px) {
  .main-content { grid-template-columns: 1fr; padding: 20px; }
  .brand-section { display: none; }
  .form-grid { grid-template-columns: 1fr; .full-width { grid-column: span 1; } }
}
</style>