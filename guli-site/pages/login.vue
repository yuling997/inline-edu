<template>
  <div class="main">
    <div class="title">
      <a class="active" href="/login">登录</a>
      <span>·</span>
      <a href="/register">注册</a>
    </div>

    <div class="sign-up-container">
      <form action="register">
        <div class="input-prepend restyle">
          <input
            v-model="user.mobile"
            type="text"
            placeholder="手机号">
          <i class="iconfont icon-phone"/>
        </div>
        <div class="input-prepend">
          <input
            v-model="user.password"
            type="password"
            placeholder="密码">
          <i class="iconfont icon-password"/>
        </div>
        <div class="btn">
          <input
            type="button"
            class="sign-in-button"
            value="登录"
            @click="submitLogin()">
        </div>
      </form>
      <!-- 更多登录方式 -->
      <div class="more-sign">
        <h6>社交帐号登录</h6>
        <ul>
          <li><a id="weixin" class="weixin" target="_blank" href="http://localhost:8150/web/ucenter/wx/login"><i class="iconfont icon-weixin"/></a></li>
          <li><a id="qq" class="qq" target="_blank" href="#"><i class="iconfont icon-qq"/></a></li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>

import '~/assets/css/sign.css'
import '~/assets/css/iconfont.css'
import cookie from 'js-cookie'
import memberApi from '@/api/member'

export default {

  layout: 'sign',

  data() {
    return {
      user: {
        mobile: '',
        password: ''
      }
    }
  },

  methods: {

    // 登录
    submitLogin() {
      // 执行登录
      memberApi.submitLogin(this.user).then(response => {
        // 登录成功后将token写入cookie
        cookie.set('guli_token', response.data.data.token, { domain: 'localhost' })

        // 根据token解析用户登录信息
        memberApi.getLoginInfo().then(response => {
          // 获取用户信息放入cookie
          cookie.set('guli_user', response.data.data.item, { domain: 'localhost' })
          // 跳转到首页
          window.location.href = '/'
        })
      })
    }
  }
}
</script>
