<template>
  <div class="login">
    <el-form :model="loginForm" :rules="loginRules" class="login-form" ref="loginForm">
      <h3 class="title">若依后台管理系统</h3>
      <el-form-item prop="username">
        <el-input auto-complete="off" placeholder="账号" type="text" v-model="loginForm.username">
          <svg-icon class="el-input__icon input-icon" icon-class="user" slot="prefix"/>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          @keyup.enter.native="handleLogin"
          auto-complete="off"
          placeholder="密码"
          type="password"
          v-model="loginForm.password"
        >
          <svg-icon class="el-input__icon input-icon" icon-class="password" slot="prefix"/>
        </el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-input
          @keyup.enter.native="handleLogin"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          v-model="loginForm.code"
        >
          <svg-icon class="el-input__icon input-icon" icon-class="validCode" slot="prefix"/>
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img"/>
        </div>
      </el-form-item>
      <el-checkbox style="margin:0px 0px 25px 0px;" v-model="loginForm.rememberMe">记住密码
      </el-checkbox>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          @click.native.prevent="handleLogin"
          size="medium"
          style="width:100%;"
          type="primary"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2018-2021 ruoyi.vip All Rights Reserved.</span>
    </div>
  </div>
</template>

<script>
  import {getCodeImg} from "@/api/login";
  import Cookies from "js-cookie";
  import {decrypt, encrypt} from '@/utils/jsencrypt'

  export default {
    name: "Login",
    data() {
      return {
        codeUrl: "",
        cookiePassword: "",
        loginForm: {
          username: "admin",
          password: "admin123",
          rememberMe: false,
          code: "",
          uuid: ""
        },
        loginRules: {
          username: [
            {required: true, trigger: "blur", message: "用户名不能为空"}
          ],
          password: [
            {required: true, trigger: "blur", message: "密码不能为空"}
          ],
          code: [{required: true, trigger: "change", message: "验证码不能为空"}]
        },
        loading: false,
        redirect: undefined
      };
    },
    watch: {
      $route: {
        handler: function (route) {
          this.redirect = route.query && route.query.redirect;
        },
        immediate: true
      }
    },
    created() {
      this.getCode();
      this.getCookie();
    },
    methods: {
      getCode() {
        getCodeImg().then(res => {
          this.codeUrl = "data:image/gif;base64," + res.img;
          this.loginForm.uuid = res.uuid;
        });
      },
      getCookie() {
        const username = Cookies.get("username");
        const password = Cookies.get("password");
        const rememberMe = Cookies.get('rememberMe')
        this.loginForm = {
          username: username === undefined ? this.loginForm.username : username,
          password: password === undefined ? this.loginForm.password : decrypt(password),
          rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
        };
      },
      handleLogin() {
        this.$refs.loginForm.validate(valid => {
          if (valid) {
            this.loading = true;
            if (this.loginForm.rememberMe) {
              Cookies.set("username", this.loginForm.username, {expires: 30});
              Cookies.set("password", encrypt(this.loginForm.password), {expires: 30});
              Cookies.set('rememberMe', this.loginForm.rememberMe, {expires: 30});
            } else {
              Cookies.remove("username");
              Cookies.remove("password");
              Cookies.remove('rememberMe');
            }
            this.$store.dispatch("Login", this.loginForm).then(() => {
              this.$router.push({path: this.redirect || "/"}).catch(() => {
              });
            }).catch(() => {
              this.loading = false;
              this.getCode();
            });
          }
        });
      }
    }
  };
</script>

<style lang="scss" rel="stylesheet/scss">
  .login {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    background-image: url("../assets/images/login-background.jpg");
    background-size: cover;
  }

  .title {
    margin: 0px auto 30px auto;
    text-align: center;
    color: #707070;
  }

  .login-form {
    border-radius: 6px;
    background: #ffffff;
    width: 400px;
    padding: 25px 25px 5px 25px;

    .el-input {
      height: 38px;

      input {
        height: 38px;
      }
    }

    .input-icon {
      height: 39px;
      width: 14px;
      margin-left: 2px;
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
    color: #fff;
    font-family: Arial;
    font-size: 12px;
    letter-spacing: 1px;
  }

  .login-code-img {
    height: 38px;
  }
</style>
