<template>
  <div class="app-container">
    <p>
      <el-input v-model="message" />
    </p>
    <p>
      <!-- 展示反转字符串 -->
      <!-- 表达式: html页面中展示了大量的计算逻辑 -->
      {{ message.split('').reverse().join('') }}
    </p>
  </div>
</template>

<script>
export default {

  data() {
    return {
      message: ''
    }
  }
}
</script>
