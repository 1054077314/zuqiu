<template>
  <div class="modern-glass-wrapper">
    <div class="background-decoration">
      <div class="blob blob-1"></div>
      <div class="blob blob-2"></div>
      <div class="blob blob-3"></div>
    </div>

    <main class="main-content">
      <div class="brand-section">
        <div class="brand-content">
          <span class="system-tag">SOVEREIGN SYSTEM</span>
          <h1 class="main-title">
            <span class="light">FOOTBALL</span><br/>
            <span class="bold">MANAGEMENT</span>
          </h1>
          <p class="description">
            The ultimate digital infrastructure for elite football institutions. 
            Managing greatness with precision.
          </p>
          
          <div class="status-card">
            <div class="status-info">
              <span class="status-label">ELITE STATUS</span>
              <span class="status-value">Active Duty</span>
            </div>
            <div class="divider"></div>
            <i class="el-icon-success verified-icon"></i>
          </div>
        </div>
      </div>

      <div class="form-section">
        <div class="glass-card">
          <div class="golden-bar"></div>
          
          <header class="form-header">
            <h2>欢迎回来</h2>
            <p>请输入您的凭据以访问指挥中心。</p>
          </header>

          <el-form :model="rulesForm" :rules="rules" ref="rulesForm" class="custom-form">
            <div class="input-group">
              <label class="input-label">账号</label>
              <el-form-item prop="username">
                <el-input v-model="rulesForm.username" placeholder="Username / Email">
                  <i slot="suffix" class="el-icon-user"></i>
                </el-input>
              </el-form-item>
            </div>
            
            <div class="input-group">
              <label class="input-label">密码</label>
              <el-form-item prop="password">
                <el-input v-model="rulesForm.password" type="password" placeholder="••••••••" show-password>
                  <i slot="suffix" class="el-icon-lock"></i>
                </el-input>
              </el-form-item>
            </div>

            <div class="input-group">
              <label class="input-label">验证码</label>
              <div class="captcha-container">
                <el-form-item prop="code" style="flex: 1; margin-bottom: 0;">
                  <el-input v-model="rulesForm.code" placeholder="Code"></el-input>
                </el-form-item>
                <div class="v-code-display" @click="getVerify" title="点击切换验证码">
                  <span v-for="(item, index) in codes" :key="index" :style="{color:item.color, transform:item.rotate}">
                    {{item.num}}
                  </span>
                </div>
              </div>
            </div>

            <div class="role-selector">
              <el-radio-group v-model="rulesForm.role">
                <el-radio v-for="item in roles" :key="item.roleName" :label="item.roleName">
                  {{item.roleName}}
                </el-radio>
              </el-radio-group>
            </div>

            <el-button type="primary" class="submit-btn" @click="login()">
              登录 <i class="el-icon-right"></i>
            </el-button>
            
            <footer class="form-footer">
              <p>还没有账户？ <el-button type="text" @click="register('jiaolian')">账号注册</el-button></p>
            </footer>
          </el-form>
        </div>
      </div>
    </main>

    <div class="side-info">EST. 2024 • SOVEREIGN GALLERY</div>
  </div>
</template>

<script>
import menu from "@/utils/menu";

export default {
  data() {
    return {
      rulesForm: { username: "", password: "", role: "", code: "" },
      menus: [],
      roles: [],
      codes: [],
      rules: {
        username: [{ required: true, message: "请输入账号", trigger: "blur" }],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
        role: [{ required: true, message: "请选择角色", trigger: "change" }],
        code: [{ required: true, message: "请输入验证码", trigger: "blur" }],
      },
    };
  },
  mounted() {
    this.getVerify();
    let menus = this.$storage.getObj("menus") || menu.list();
    this.menus = menus;
    this.roles = this.menus.filter((item) => item && item.hasBackLogin === "是");
    if (!this.rulesForm.role && this.roles.length) {
      this.rulesForm.role = this.roles[0].roleName;
    }
  },
  methods: {
    getVerify() {
      this.codes = [];
      const nums = ["1", "2", "3", "4", "5", "A", "B", "C", "D", "E"];
      const colors = ["#2d3436", "#0984e3", "#6c5ce7"];
      for (let i = 0; i < 4; i++) {
        this.codes.push({
          num: nums[Math.floor(Math.random() * nums.length)],
          color: colors[Math.floor(Math.random() * colors.length)],
          rotate: `rotate(${Math.floor(Math.random() * 20 - 10)}deg)`,
        });
      }
    },
    register(tableName) {
      this.$storage.set("loginTable", tableName);
      this.$router.push({ path: "/register" });
    },
    login() {
      let code = this.codes.map(i => i.num).join("");
      if (this.rulesForm.code.toLowerCase() != code.toLowerCase()) {
        this.getVerify();
        return this.$message.error("验证码错误");
      }
      this.$refs["rulesForm"].validate((valid) => {
        if (valid) {
          let roleItem = this.roles.find(r => r.roleName === this.rulesForm.role);
          let tableName = roleItem.tableName;
          this.$http({
            url: `${tableName}/login?username=${this.rulesForm.username}&password=${this.rulesForm.password}`,
            method: "post",
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$storage.set("Token", data.token);
              this.$storage.set("role", this.rulesForm.role);
              this.$storage.set("sessionTable", tableName);
              this.$router.replace({ path: "/index" });
            } else {
              this.$message.error(data.msg);
            }
          });
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.modern-glass-wrapper {
  min-height: 100vh;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f0f2f5;
  position: relative;
  overflow: hidden;
  font-family: 'Inter', sans-serif;

  // 动态流体背景
  .background-decoration {
    position: absolute; inset: 0; filter: blur(100px);
    .blob { position: absolute; border-radius: 50%; opacity: 0.5; animation: float 20s infinite alternate; }
    .blob-1 { width: 600px; height: 600px; background: #dbeafe; top: -100px; left: -100px; }
    .blob-2 { width: 500px; height: 500px; background: #ede9fe; bottom: -100px; right: -50px; animation-delay: -5s; }
    .blob-3 { width: 400px; height: 400px; background: #e0f2fe; top: 20%; right: 10%; animation-delay: -10s; }
  }

  .main-content {
    position: relative; z-index: 10;
    width: 100%; max-width: 1100px;
    display: grid; grid-template-columns: 1.1fr 1fr;
    padding: 0 40px;
  }

  .brand-section {
    display: flex; flex-direction: column; justify-content: center; padding-right: 50px;
    .system-tag { display: inline-block; padding: 4px 12px; background: #000; color: #fff; font-size: 10px; font-weight: 800; border-radius: 4px; letter-spacing: 2px; margin-bottom: 24px; width: fit-content; }
    .main-title { font-size: 56px; line-height: 1; color: #1a1a1a; letter-spacing: -2px; margin-bottom: 30px;
      .light { font-weight: 200; opacity: 0.4; }
      .bold { font-weight: 900; }
    }
    .description { font-size: 16px; color: #44464f; line-height: 1.6; max-width: 380px; margin-bottom: 40px; font-weight: 300; }
    
    .status-card {
      background: rgba(255, 255, 255, 0.5); backdrop-filter: blur(10px);
      padding: 20px; border-radius: 12px; display: flex; align-items: center; gap: 20px;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05); border-left: 4px solid #000; width: fit-content;
      .status-label { display: block; color: #666; font-size: 9px; font-weight: 800; letter-spacing: 2px; }
      .status-value { font-size: 24px; font-weight: 700; color: #000; }
      .divider { width: 1px; height: 35px; background: rgba(0, 0, 0, 0.1); }
      .verified-icon { font-size: 28px; color: #000; }
    }
  }

  .form-section {
    .glass-card {
      background: rgba(255, 255, 255, 0.4); // 半透明
      backdrop-filter: blur(25px); // 毛玻璃核心
      -webkit-backdrop-filter: blur(25px);
      border-radius: 24px; padding: 50px;
      border: 1px solid rgba(255, 255, 255, 0.6);
      box-shadow: 0 30px 60px rgba(0, 0, 0, 0.08); position: relative; overflow: hidden;
      .golden-bar { position: absolute; top: 0; left: 0; width: 100%; height: 4px; background: linear-gradient(90deg, #000, #755a1f, #000); }
    }
  }

  .form-header { margin-bottom: 35px; h2 { font-size: 28px; font-weight: 800; color: #000; margin-bottom: 8px; } p { font-size: 14px; color: #666; } }

  .input-group {
    margin-bottom: 20px;
    .input-label { display: block; font-size: 10px; font-weight: 800; color: #000; letter-spacing: 1.5px; text-transform: uppercase; margin-bottom: 8px; margin-left: 4px; }
    ::v-deep .el-input__inner {
      height: 50px; background: rgba(255, 255, 255, 0.5); border: 1px solid rgba(0,0,0,0.05); border-radius: 10px;
      &:focus { border-color: #000; background: #fff; }
    }
    ::v-deep .el-input__suffix { line-height: 50px; right: 12px; color: #000; }
  }

  .captcha-container {
    display: flex; gap: 12px;
    .v-code-display { width: 110px; height: 50px; background: #fff; border-radius: 10px; display: flex; align-items: center; justify-content: center; cursor: pointer; border: 1px solid #eee; }
  }

  .role-selector {
    margin: 20px 0;
    ::v-deep .el-radio__label { font-weight: 600; font-size: 13px; }
    ::v-deep .el-radio__input.is-checked .el-radio__inner { background: #000; border-color: #000; }
  }

  .submit-btn {
    width: 100%; height: 56px; background: #000; border: none; border-radius: 12px;
    font-size: 14px; font-weight: 700; letter-spacing: 1px; transition: all 0.3s;
    &:hover { transform: translateY(-2px); box-shadow: 0 10px 20px rgba(0,0,0,0.15); }
  }

  .form-footer { margin-top: 30px; text-align: center; p { font-size: 14px; } .el-button { color: #000; font-weight: 800; } }

  .side-info { position: fixed; bottom: 40px; right: 40px; font-size: 10px; font-weight: 900; color: #ccc; letter-spacing: 4px; writing-mode: vertical-rl; transform: rotate(180deg); }
}

@keyframes float { from { transform: translate(0,0); } to { transform: translate(5%, 5%); } }

@media (max-width: 992px) {
  .main-content { grid-template-columns: 1fr; }
  .brand-section { display: none; }
}
</style>