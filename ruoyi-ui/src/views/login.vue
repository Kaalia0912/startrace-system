<template>
  <div class="login">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
      <div class="login-brand">
        <img :src="require('@/assets/logo/logo.png')" class="brand-logo" alt="logo" />
        <h3 class="title">{{title}}</h3>
        <p class="subtitle">ENTERPRISE MANAGEMENT SYSTEM</p>
      </div>
      <el-form-item prop="username">
        <el-input
          v-model="loginForm.username"
          type="text"
          auto-complete="off"
          placeholder="账号"
        >
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="code" v-if="captchaEnabled">
        <el-input
          v-model="loginForm.code"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img"/>
        </div>
      </el-form-item>
      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <div style="float: right;" v-if="register">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getCodeImg } from "@/api/login"
import Cookies from "js-cookie"
import { encrypt, decrypt } from '@/utils/jsencrypt'
import defaultSettings from '@/settings'

export default {
  name: "Login",
  data() {
    return {
      title: process.env.VUE_APP_TITLE,
      footerContent: defaultSettings.footerContent,
      codeUrl: "",
      loginForm: {
        username: "",
        password: "",
        rememberMe: false,
        code: "",
        uuid: ""
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      // 验证码开关
      captchaEnabled: true,
      // 注册开关
      register: false,
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created() {
    this.getCode()
    this.getCookie()
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img
          this.loginForm.uuid = res.uuid
        }
      })
    },
    getCookie() {
      const username = Cookies.get("username")
      const password = Cookies.get("password")
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      }
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 })
            Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 })
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 })
          } else {
            Cookies.remove("username")
            Cookies.remove("password")
            Cookies.remove('rememberMe')
          }
          this.$store.dispatch("Login", this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || "/" }).catch(()=>{})
          }).catch(() => {
            this.loading = false
            if (this.captchaEnabled) {
              this.getCode()
            }
          })
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  position: relative;
  background-image: url("/img/home.jpg");
  background-size: cover;
  background-position: center;
}
.login::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(8, 18, 45, 0.55);
}
.title {
  margin: 10px auto 6px auto;
  text-align: center;
  color: #ffffff;
  font-size: 30px;
  font-weight: 800;
  letter-spacing: 4px;
  text-shadow: 0 2px 16px rgba(0, 0, 0, 0.6);
}
.login-brand {
  text-align: center;
  margin-bottom: 28px;
}
.brand-logo {
  width: 68px;
  height: 68px;
  border-radius: 50%;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.45);
  display: inline-block;
}
.subtitle {
  margin: 0;
  color: #e3be5a;
  font-size: 11px;
  letter-spacing: 3px;
  text-transform: uppercase;
  opacity: 0.9;
}

.login-form {
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(14px);
  -webkit-backdrop-filter: blur(14px);
  width: 400px;
  padding: 34px 34px 12px 34px;
  z-index: 2;
  box-shadow: 0 16px 48px rgba(0, 0, 0, 0.45);
  border: 1px solid rgba(255, 255, 255, 0.55);
  position: relative;
  overflow: hidden;
  animation: loginFadeIn 0.6s ease-out;
}
.login-form::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, transparent, #e3be5a, #f5d97a, #e3be5a, transparent);
}
.login-form .el-input {
  height: 40px;
  input {
    height: 40px;
  }
}
.login-form .input-icon {
  height: 41px;
  width: 14px;
  margin-left: 2px;
}
.login-form ::v-deep .el-input__inner {
  border-radius: 8px;
  border-color: #e2e6ee;
  transition: border-color 0.25s, box-shadow 0.25s;
}
.login-form ::v-deep .el-input__inner:focus {
  border-color: #e3be5a;
  box-shadow: 0 0 0 3px rgba(227, 190, 90, 0.22);
}
.login-form ::v-deep .el-button--primary {
  background: linear-gradient(135deg, #e3be5a 0%, #c99a2e 100%);
  border-color: #e3be5a;
  border-radius: 8px;
  height: 42px;
  font-size: 15px;
  letter-spacing: 6px;
  font-weight: 600;
  box-shadow: 0 6px 18px rgba(227, 190, 90, 0.35);
  transition: transform 0.2s, box-shadow 0.2s;
}
.login-form ::v-deep .el-button--primary:hover {
  transform: translateY(-1px);
  box-shadow: 0 8px 24px rgba(227, 190, 90, 0.5);
}
.login-form ::v-deep .el-checkbox__input.is-checked .el-checkbox__inner {
  background-color: #c99a2e;
  border-color: #c99a2e;
}
.login-form ::v-deep .el-checkbox__input.is-checked + .el-checkbox__label {
  color: #7a6a3a;
}
@keyframes loginFadeIn {
  from {
    opacity: 0;
    transform: translateY(18px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: rgba(255, 255, 255, 0.85);
  text-shadow: 0 1px 6px rgba(0, 0, 0, 0.6);
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.login-code-img {
  height: 38px;
}

/* ===== 手机端登录页适配 ===== */
@media screen and (max-width: 480px) {
  .login-form {
    width: 92vw !important;
    max-width: 92vw !important;
    padding: 28px 18px 10px 18px !important;
  }
  .login-brand {
    margin-bottom: 20px !important;
  }
  .brand-logo {
    width: 56px !important;
    height: 56px !important;
  }
  .title {
    font-size: 20px !important;
    letter-spacing: 2px !important;
    white-space: nowrap !important;
    margin: 8px auto 4px auto !important;
  }
  .subtitle {
    font-size: 9px !important;
    letter-spacing: 2px !important;
    white-space: nowrap !important;
  }
  .login-form .el-input {
    height: 44px !important;
  }
  .login-form ::v-deep .el-input__inner {
    height: 44px !important;
    line-height: 44px !important;
  }
  .login-code {
    width: 100% !important;
    height: 44px !important;
    float: none !important;
    margin-top: 8px !important;
  }
  .login-code-img {
    width: 100% !important;
    height: 44px !important;
  }
  .login-form ::v-deep .el-button--primary {
    height: 46px !important;
    font-size: 16px !important;
  }
}
</style>
