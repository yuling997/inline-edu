<template>
  <div class="app-container">
    <p>
      <el-input v-model="message" />
    </p>
    <p>
      <!-- 展示反转字符串 -->
      <!-- 使用监听器 -->
      {{ reversedMessage }}
    </p>
  </div>
</template>

<script>
export default {

  data() {
    return {
      message: '',
      reversedMessage: ''
    }
  },

  watch: {
    message() {
      this.reversedMessage = this.message.split('').reverse().join('')
    }
  }
}
</script>
