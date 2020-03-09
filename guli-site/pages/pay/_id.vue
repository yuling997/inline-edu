<template>
  <div class="cart py-container">
    <!--主内容-->
    <div class="checkout py-container  pay">
      <div class="checkout-tit">
        <h4 class="fl tit-txt"><span class="success-icon"/><span class="success-info">订单提交成功，请您及时付款！订单号：{{ payObj.out_trade_no }}</span>
        </h4>
        <span class="fr"><em class="sui-lead">应付金额：</em><em class="orange money">￥{{ payObj.total_fee }}</em></span>
        <div class="clearfix"/>
      </div>
      <div class="checkout-steps">
        <div class="fl weixin">微信支付</div>
        <div class="fl sao">
          <p class="red">请使用微信扫一扫。</p>
          <div class="fl code">
            <!-- <img id="qrious" src="~/assets/img/erweima.png" alt=""> -->
            <qriously :value="payObj.code_url" :size="338"/>
            <div class="saosao">
              <p>请使用微信扫一扫</p>
              <p>扫描二维码支付</p>
            </div>

          </div>

        </div>
        <div class="clearfix"/>
        <!-- <p><a href="pay.html" target="_blank">> 其他支付方式</a></p> -->
      </div>
    </div>

  </div>
</template>

<script>

import payApi from '~/api/pay'

export default {

  async asyncData({ params }) {
    const response = await payApi.createNative(params.id)
    return {
      payObj: response.data.data
    }
  },

  // 在created中获取数据，报告Invalid prop: type check failed for prop "value".
  // created() {
  //   pay.createNative(this.$route.params.id).then(response => {
  //     this.payObj = response.data.data
  //   })
  // }

  mounted() {
    const timer = setInterval(() => {
      console.log('执行定时器 ' + this.payObj.out_trade_no)
      this.queryPayStatus()
    }, 3000)

    // vue中清除定时器
    this.$once('hook:beforeDestroy', () => {
      console.log('清除定时器')
      clearInterval(timer)
    })
  },

  methods: {
    // 查询订单状态
    queryPayStatus() {
      console.log('查询订单状态 ' + this.payObj.out_trade_no)
      payApi.queryPayStatus(this.payObj.out_trade_no).then(response => {
        if (response.data.success) {
          this.$message({
            type: 'success',
            message: '支付成功!'
          })
          // 离开页面，触发hook:beforeDestroy，清除定时器
          // 跳转到课程详情页面观看视频
          this.$router.push({ path: '/course/' + this.payObj.course_id })
        }
      })
    }
  }
}
</script>
