<template>

  <div class="Page Confirm">
    <div class="Title">
      <h1 class="fl f18">订单确认</h1>
      <div class="clear"/>
    </div>
    <table class="GoodList">
      <tbody>
        <tr class="good">
          <td class="name First">
            <a :href="'https://localhost:3000/course/'+order.courseId" target="_blank">
              <img :src="order.courseCover">
            </a>
            <div class="goodInfo">
              <a :href="'https://localhost:3000/course/'+ order.courseId" target="_blank">{{ order.courseTitle }}</a>
            </div>
          </td>
          <td colspan="3" class="teacher">讲师：{{ order.teacherName }}</td>
          <td class="red priceNew Last">￥<strong>{{ order.totalFee }}</strong></td>
        </tr>
      </tbody>
    </table>
    <div class="Finish">
      <div class="check fr">
        <el-checkbox v-model="agree">我已阅读并同意<a href="javascript:" target="_blank">《谷粒学院购买协议》</a></el-checkbox>
      </div>
      <div class="clear"/>
      <div class="Main fl">
        <div class="fl">
          <a :href="'/course/'+order.courseId">返回课程详情页</a>
        </div>
      </div>
      <button id="submitPay" class="fr redb" type="button" @click="toPay()">去支付</button>
      <div class="clear"/>
    </div>
  </div>
</template>

<script>

import orderApi from '@/api/order'

export default {

  asyncData({ params }) {
    return {
      orderId: params.id
    }
  },

  data() {
    return {
      orderId: 0,
      order: {},
      agree: true
    }
  },

  created() {
    orderApi.getById(this.orderId).then(response => {
      this.order = response.data.data.item
    })
  },

  methods: {
    toPay() {
      if (this.agree) {
        this.$router.push({ path: '/pay/' + this.order.orderNo })
      }
    }
  }
}
</script>
