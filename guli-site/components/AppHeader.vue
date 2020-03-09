<template>
  <!-- 公共头 -->
  <header id="header">
    <section class="container">
      <h1 id="logo">
        <a href="#" title="谷粒学院">
          <img src="~/assets/img/logo.png" width="100%" alt="谷粒学院">
        </a>
      </h1>
      <div class="h-r-nsl">
        <ul class="nav">
          <router-link to="/" tag="li" active-class="current" exact>
            <a>首页</a>
          </router-link>
          <router-link to="/course" tag="li" active-class="current">
            <a>课程</a>
          </router-link>
          <router-link to="/teacher" tag="li" active-class="current">
            <a>名师</a>
          </router-link>
          <router-link to="/article" tag="li" active-class="current">
            <a>文章</a>
          </router-link>
          <router-link to="/qa" tag="li" active-class="current">
            <a>问答</a>
          </router-link>
        </ul>
        <!-- / nav -->
        <ul class="h-r-login">
          <li v-if="!loginInfo.id" id="no-login">
            <a href="/login" title="登录">
              <em class="icon18 login-icon">&nbsp;</em>
              <span class="vam ml5">登录</span>
            </a>
            |
            <a href="/register" title="注册">
              <span class="vam ml5">注册</span>
            </a>
          </li>
          <li v-if="loginInfo.id" id="is-login-one" class="mr10">
            <a id="headerMsgCountId" href="#" title="消息">
              <em class="icon18 news-icon">&nbsp;</em>
            </a>
            <q class="red-point">&nbsp;</q>
          </li>
          <li v-if="loginInfo.id" id="is-login-two" class="h-r-user">
            <a href="/ucenter" title>
              <img
                :src="loginInfo.avatar"
                width="30"
                height="30"
                class="vam picImg"
                alt
              >
              <span id="userName" class="vam disIb">{{ loginInfo.nickname }}</span>
            </a>
            <a href="javascript:void(0)" title="退出" class="ml5" @click="logout()">退出</a>
          </li>
          <!-- /未登录显示第1 li；登录后显示第2，3 li -->
        </ul>
        <aside class="h-r-search">
          <form action="#" method="post">
            <label class="h-r-s-box">
              <input type="text" placeholder="搜索课程" name="queryCourse.courseName" value>
              <button type="submit" class="s-btn">
                <em class="icon18">&nbsp;</em>
              </button>
            </label>
          </form>
        </aside>
      </div>
      <aside class="mw-nav-btn">
        <div class="mw-nav-icon"/>
      </aside>
      <div class="clear"/>
    </section>
  </header>
  <!-- /公共头 -->
</template>

<script>
import cookie from 'js-cookie'
import memberApi from '@/api/member'
export default {

  data() {
    return {
      loginInfo: {},
      token: ''
    }
  },

  created() {
    this.token = this.$route.query.token

    if (this.token) { // 微信登录
      this.wxLogin()
    } else {
      // 显示当前用户信息
      this.showInfo()
    }
  },

  methods: {

    showInfo() {
      // 从cookie中获取用户信息
      var json = cookie.get('guli_user')
      if (json) {
        // 如果json存在，则将json转换成对象绑定到loginInfo上
        this.loginInfo = JSON.parse(json)
      }
    },

    // 微信登录
    wxLogin() {
      // 把token存在cookie中，也可以放在localStorage中
      cookie.set('guli_token', this.token, { domain: 'localhost ' })
      // 登录成功根据token获取用户信息
      memberApi.getLoginInfo().then(response => {
        this.loginInfo = response.data.data.item
        // 将用户信息记录cookie
        cookie.set('guli_user', this.loginInfo, { domain: 'localhost' })

        // 跳转页面：擦除url中的token
        window.location.href = '/'
      })
    },

    // 退出
    logout() {
      cookie.set('guli_token', '', { domain: 'localhost' })
      cookie.set('guli_user', '', { domain: 'localhost' })

      window.location.href = '/'
    }
  }
}
</script>
