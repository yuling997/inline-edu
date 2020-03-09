<template>
  <div class="app-container">
    <p>
      <el-input v-model="message" />
    </p>
    <p>
      <!-- 展示反转字符串 -->
      <!-- 使用函数计算页面中的复杂内容：使用方法的形式调用， 必须使用圆括号 -->
      {{ reversedMessage() }}
    </p>
  </div>
</template>

<script>
export default {

  data() {
    return {
      message: ''
    }
  },

  methods: {

    reversedMessage() {
      return this.message.split('').reverse().join('')
    }
  }
}
</script>
