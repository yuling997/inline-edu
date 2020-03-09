<template>
  <div class="app-container">
    <p>
      <el-input v-model="message" />
    </p>
    <p>
      <!-- 展示反转字符串 -->
      <!-- 使用计算属性：使用属性的方式调用，不能使用圆括号 -->
      {{ reversedMessage }}
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

  // 计算属性
  computed: {

    reversedMessage() {
      return this.message.split('').reverse().join('')
    }
  }
}
</script>
