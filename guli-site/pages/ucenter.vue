<template>
  <!-- 内容区域 -->
  <div class="bg-fa of">
    <section class="container">
      <div class="u-body mt40">

        <menu class="col-3 fl uMenu">
          <dl>
            <dt>
              <section class="of">
                <div class="u-face-pic">
                  <img
                    :src="loginInfo.avatar"
                    :alt="loginInfo.nickname"
                    class="userImgPhoto">
                  <a href="uc_avatar.html" title="" class="c-fff">
                    修改头像
                  </a>
                </div>
                <h4 class="mt10">
                  <span class="fsize16 c-666 userNameClass">{{ loginInfo.nickname }}</span>
                </h4>
                <div class="hLh30 mt10">
                  <a href="/ucenter" title="" class="c-blue">
                    个人资料设置
                  </a>
                </div>
                <div class="clear"/>
              </section>
            </dt>
            <dd class="u-m-dd">
              <ul>
                <li>
                  <span>Wo的学习</span>
                  <ol>
                    <router-link to="/ucenter/order" tag="li" active-class="current" exact>
                      <a>我的订单</a>
                    </router-link>
                    <router-link to="/ucenter/collect" tag="li" active-class="current" exact>
                      <a>我的收藏</a>
                    </router-link>
                  </ol>
                </li>
              </ul>
              <ul>
                <li>
                  <span>Wo的问答</span>
                  <ol>
                    <li>
                      <a href="uc_question.html" title="">
                        Wo的提问
                      </a>
                    </li>
                    <li>
                      <a href="uc_anwser.html" title="">
                        Wo的回答
                      </a>
                    </li>
                  </ol>
                </li>
              </ul>
              <ul>
                <li>
                  <span>Wo的资料</span>
                  <ol>
                    <li>
                      <a href="uc_base.html" title="">
                        基本资料
                      </a>
                    </li>
                    <li>
                      <a href="uc_avatar.html" title="">
                        个人头像
                      </a>
                    </li>
                    <li>
                      <a href="uc_password.html" title="">
                        密码设置
                      </a>
                    </li>
                  </ol>
                </li>
              </ul>
              <ul>
                <li >
                  <span>Wo的消息</span>
                  <ol>
                    <li>
                      <a href="uc_letter.html" title="">系统消息</a>
                    </li>
                  </ol>
                </li>
              </ul>
            </dd>
          </dl>
        </menu>

        <nuxt-child />

        <div class="clear"/>
      </div>
    </section>
  </div>
  <!-- /内容区域 -->
</template>
<script>
import cookie from 'js-cookie'

export default {
  data() {
    return {
      loginInfo: {}
    }
  },

  created() {
    this.showInfo()
  },

  methods: {
    showInfo() {
      // debugger
      var jsonStr = cookie.get('guli_user')
      if (jsonStr) {
        this.loginInfo = JSON.parse(jsonStr)
      }
    }
  }
}
</script>
