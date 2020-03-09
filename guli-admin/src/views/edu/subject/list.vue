<template>
  <div class="app-container">

    <el-input v-model="filterText" />

    <el-tree
      ref="subjectTree"
      :data="subjectList"
      :props="defaultProps"
      :filter-node-method="filterNode" />
  </div>
</template>

<script>
import subjectApi from '@/api/edu/subject'

export default {
  data() {
    return {
      filterText: '', // 过滤文本
      subjectList: [], // 嵌套数据列表

      defaultProps: {
        children: 'children',
        label: 'title'
      }
    }
  },

  watch: {
    filterText(val) {
    //   console.log('var1', var1) // 新值
    //   console.log('var2', var2) // 旧值
      console.log('val', val)
      // 根据val的值对tree执行过滤操作
      this.$refs.subjectTree.filter(val)
    }
  },
  created() {
    this.fetchNodeList()
  },

  methods: {
    // 获取远程的嵌套数据列表
    fetchNodeList() {
      subjectApi.getNestedTreeList().then(response => {
        this.subjectList = response.data.items
      })
    },

    filterNode(value, data) {
      console.log('value', value)
      console.log('data', data)
      // console.log('node', node)

      if (!value) return true
      return data.title.toLowerCase().indexOf(value.toLowerCase()) !== -1 // 忽略大小写
    }
  }
}
</script>
