<template> 
 <div class="stitch-login-container modern-login">
    <div class="gradient-bg">
      <div class="gradients-container">
        <div class="g1"></div>
        <div class="g2"></div>
        <div class="g3"></div>
      </div>
    </div>

    <div class="main-card">
      <div class="brand-side">
        <div class="brand-inner">
          <div class="ai-tag"> 系统</div>
          <h1 class="main-title">
            <span class="light">FOOTBALL</span><br/>
            <span class="bold">MANAGEMENT</span>
          </h1>
          <div class="accent-line"></div>
          <p class="brand-footer"></p>
        </div>
      </div>

      <div class="form-side">
        <div class="form-wrapper">
          <div class="welcome-text">
            <h2>欢迎回来</h2>
            
          </div>

          <el-form :model="rulesForm" :rules="rules" ref="rulesForm">
            <el-form-item prop="username">
              <el-input 
                v-model="rulesForm.username" 
                placeholder="Account"
                class="stitch-input"
              >
                <i slot="prefix" class="el-icon-user"></i>
              </el-input>
            </el-form-item>
            
            <el-form-item prop="password">
              <el-input 
                v-model="rulesForm.password" 
                type="password" 
                placeholder="Password" 
                show-password
                class="stitch-input"
              >
                <i slot="prefix" class="el-icon-lock"></i>
              </el-input>
            </el-form-item>
            
            <div class="captcha-row">
              <el-form-item prop="code" style="flex: 1; margin-bottom: 0;">
                <el-input v-model="rulesForm.code" placeholder="Code" class="stitch-input"></el-input>
              </el-form-item>
              <div class="v-code-display" @click="getVerify">
                <span v-for="(item, index) in codes" :key="index" :style="{color:item.color, transform:item.rotate}">{{item.num}}</span>
              </div>
            </div>

            <div class="role-selector">
              <el-radio-group v-model="rulesForm.role">
                <el-radio 
                  v-for="item in roles" 
                  :key="item.roleName" 
                  :label="item.roleName"
                >{{item.roleName}}</el-radio>
              </el-radio-group>
            </div>

            <el-button type="primary" class="stitch-btn" @click="login()">登录</el-button>
            
            <div class="action-footer" v-if="roles.length>1">
              <span></span>
              <el-button type="text" @click="register('jiaolian')">账号注册</el-button>
            </div>
          </el-form>
        </div>
        <div class="copyright"></div>
      </div>
    </div>
  </div>
</template>

<script>
import menu from "@/utils/menu";

export default {
  data() {
    return {
      rulesForm: {
        username: "",
        password: "",
        role: "",
        code: "",
      },
      menus: [],
      roles: [],
      codes: [],
      rules: {
        username: [{ required: true, message: "Required", trigger: "blur" }],
        password: [{ required: true, message: "Required", trigger: "blur" }],
        role: [{ required: true, message: "Select Role", trigger: "change" }],
        code: [{ required: true, message: "Required", trigger: "blur" }],
      },
    };
  },
  mounted() {
    this.getVerify();
    let menus = [];
    try {
      menus = this.$storage.getObj("menus");
    } catch (e) {
      menus = [];
    }
    if (!Array.isArray(menus) || !menus.length) {
      menus = menu.list();
      this.$storage.set("menus", menus);
    }
    this.menus = Array.isArray(menus) ? menus : [];
    this.roles = this.menus.filter((item) => item && item.hasBackLogin === "是");
    if (!this.rulesForm.role && this.roles.length) {
      this.rulesForm.role = this.roles[0].roleName;
    }
  },
  methods: {
    getVerify() {
      this.codes = [];
      const nums = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"];
      const colors = ["#2d3436", "#0984e3", "#6c5ce7"];
      const rotate = ["-10deg", "0deg", "10deg"];
      for (let i = 0; i < 4; i++) {
        this.codes.push({
          num: nums[Math.floor(Math.random() * nums.length)],
          color: colors[Math.floor(Math.random() * colors.length)],
          rotate: rotate[Math.floor(Math.random() * rotate.length)],
        });
      }
    },
    register(tableName) {
      this.$storage.set("loginTable", tableName);
      this.$router.push({ path: "/register" });
    },
    login() {
      let code = "";
      for (let i = 0; i < this.codes.length; i++) {
        code += this.codes[i].num;
      }
      if (!this.rulesForm.code) return this.$message.error("Input Code");
      if (this.rulesForm.code.toLowerCase() != code.toLowerCase()) {
        this.getVerify();
        return this.$message.error("Wrong Code");
      }
      this.$refs["rulesForm"].validate((valid) => {
        if (valid) {
          let tableName = "";
          for (let i = 0; i < this.roles.length; i++) {
            if (this.roles[i].roleName == this.rulesForm.role) {
              tableName = this.roles[i].tableName;
            }
          }
          this.$http({
            url: `${tableName}/login?username=${this.rulesForm.username}&password=${this.rulesForm.password}`,
            method: "post",
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$storage.set("Token", data.token);
              this.$storage.set("role", this.rulesForm.role);
              this.$storage.set("sessionTable", tableName);
              this.$storage.set("adminName", this.rulesForm.username);
              this.$http({ url: `${tableName}/session`, method: "get" }).then(({ data }) => {
                if (data && data.code === 0) {
                  this.$storage.set("userid", data.data.id);
                  this.$router.replace({ path: "/index" });
                }
              });
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
.stitch-login-container {
  height: 100vh;
  width: 100vw;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  position: relative;
  background: #f8fafc;

  // AI 流体背景
  .gradient-bg {
    position: absolute; width: 100%; height: 100%;
    filter: blur(80px); opacity: 0.4;
    .gradients-container {
      width: 100%; height: 100%;
      .g1 { position: absolute; background: radial-gradient(circle at center, #4e9af1 0, transparent 50%); width: 800px; height: 800px; top: -10%; left: -10%; animation: move 20s infinite; }
      .g2 { position: absolute; background: radial-gradient(circle at center, #6c5ce7 0, transparent 50%); width: 600px; height: 600px; bottom: -10%; right: -10%; animation: move 15s infinite reverse; }
    }
  }

  .main-card {
    position: relative; z-index: 10;
    width: 1100px; height: 680px;
    background: rgba(255, 255, 255, 0.7);
    backdrop-filter: blur(20px);
    border: 1px solid rgba(255, 255, 255, 0.5);
    border-radius: 32px;
    display: flex;
    box-shadow: 0 40px 100px rgba(0, 0, 0, 0.05);
  }

  .brand-side {
    flex: 1.2; padding: 80px; display: flex; align-items: center;
    border-right: 1px solid rgba(0, 0, 0, 0.03);
    .ai-tag { display: inline-block; padding: 6px 12px; background: #000; color: #fff; border-radius: 100px; font-size: 10px; font-weight: 700; letter-spacing: 1px; margin-bottom: 24px; }
    .main-title { font-size: 48px; line-height: 1; margin: 0; color: #1a1a1a; 
      .light { font-weight: 300; opacity: 0.6; }
      .bold { font-weight: 800; letter-spacing: -1px; }
    }
    .accent-line { width: 60px; height: 2px; background: #000; margin: 40px 0; }
    .brand-footer { font-size: 11px; color: #999; letter-spacing: 2px; }
  }

  .form-side {
    flex: 1; padding: 80px; display: flex; flex-direction: column; position: relative;
    .welcome-text { margin-bottom: 50px; h2 { font-size: 28px; margin: 0 0 8px 0; } p { color: #888; font-size: 14px; } }
    
    .stitch-input ::v-deep .el-input__inner {
      background: rgba(0, 0, 0, 0.02); border: 1px solid rgba(0, 0, 0, 0.05); height: 52px; border-radius: 12px;
      &:focus { border-color: #000; background: #fff; }
    }

    .captcha-row { display: flex; gap: 12px; margin: 24px 0;
      .v-code-display { width: 120px; height: 52px; background: #fff; border-radius: 12px; border: 1px solid rgba(0,0,0,0.05); display: flex; align-items: center; justify-content: center; cursor: pointer; font-weight: bold; }
    }

    .role-selector {
      margin-bottom: 30px;
      .el-radio {
        margin-right: 20px;
      }
      ::v-deep .el-radio__label {
        font-weight: 500;
      }
    }

    .stitch-btn {
      width: 100%; height: 56px; background: #000; border: none; border-radius: 14px; font-weight: 700; font-size: 14px; letter-spacing: 1px;
      &:hover { background: #333; transform: scale(1.02); }
    }

    .action-footer { margin-top: 24px; text-align: center; font-size: 13px; color: #999; 
      .el-button--text { color: #000; font-weight: 700; margin-left: 8px; }
    }
    .copyright { position: absolute; bottom: 40px; left: 80px; font-size: 10px; color: #ccc; letter-spacing: 1px; }
  }
}

@keyframes move {
  0% { transform: translate(0, 0); }
  50% { transform: translate(10%, 10%); }
  100% { transform: translate(0, 0); }
}
.modern-login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;

  .stitch-login-container {
    max-width: 1100px;
    margin: auto;
    display: flex;
    border-radius: 20px;
    overflow: hidden;
    box-shadow: 0 20px 60px rgba(0,0,0,0.15);
  }

  .left {
    flex: 1;
    background: linear-gradient(135deg, #0f2a44, #1e5fa8);
    color: #fff;
    padding: 60px;
  }

  .right {
    flex: 1;
    background: #f5f7fa;
    padding: 50px;
  }

  .el-button {
    border-radius: 30px;
    height: 45px;
    font-size: 16px;
  }
}
</style>
