<template>
  <div class="app-container">
    <p>
      <el-input v-model="message" />
    </p>
    <p>
      <el-input v-model="otherMassage" />
    </p>
    <p>
      message: {{ message }}
    </p>
    <p>
      otherMassage: {{ otherMassage }}
    </p>
    <p>
      计算属性：
      {{ reversedMessage }}
    </p>
    <p>
      方法：{{ reversedMessage2() }}
    </p>
  </div>
</template>

<script>
export default {

  /**
     * 结论：
     * 1. 当修改data数据导致页面重新渲染的时候，页面中调用的所有【方法】会被执行
     * 2. 当修改data数据导致页面重新渲染的时候，页面中相关的【计算属性】会被执行
     */

  data() {
    return {
      message: '', // 当改变message时，计算属性和方法都被调用
      otherMassage: '' // 当改变otherMassage时，只有方法被调用
    }
  },

  // 计算属性
  computed: {

    reversedMessage() {
      console.log('computed reversedMessage......')
      return this.message.split('').reverse().join('')
    }
  },

  methods: {

    reversedMessage2() {
      console.log('methods reversedMessage2....')
      return this.message.split('').reverse().join('')
    }
  }
}
</script>
