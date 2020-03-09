<!-- 讲师列表组件
      1. 使用ajax请求获取数据：api模块
      2. 展示数据
-->
<template>
  <div class="app-container">

    <!-- 查询表单组件 -->
    <el-form :inline="true">
      <el-form-item label="讲师姓名">
        <!-- <el-input v-model="searchObj.name" placeholder="讲师名称"/> -->

        <!-- : value-key 加冒号name为变量；不加冒号为字符串 -->
        <el-autocomplete
          v-model="searchObj.name"
          :fetch-suggestions="querySearch"
          :trigger-on-focus="false"
          class="inline-input"
          placeholder="讲师名称"
          value-key="name"
        />
      </el-form-item>
      <el-form-item label="讲师头衔">
        <el-select v-model="searchObj.level" placeholder="请选择">
          <el-option label="高级讲师" value="1"/>
          <el-option label="首席讲师" value="2"/>
        </el-select>
      </el-form-item>
      <el-form-item label="入驻时间">
        <el-date-picker
          v-model="searchObj.joinDateBegin"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="开始时间"/>
      </el-form-item>
      <el-form-item>
        至
        <el-date-picker
          v-model="searchObj.joinDateEnd"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="结束时间"/>
      </el-form-item>
      <el-button type="primary" @click="fetchData()">查询</el-button>
      <el-button type="defalut" @click="resetData()">清空</el-button>
    </el-form>

    <!-- 工具条 -->
    <div style="margin-bottom: 10px">
      <el-button type="danger" size="mini" @click="batchRemove()">批量删除</el-button>
    </div>

    <!-- 表格组件 -->
    <el-table
      :data="list"
      stripe
      border
      style="width: 100%"
      @selection-change="handleSelectionChange">
      <!-- 多选列 -->
      <el-table-column
        type="selection"
        width="55"/>
      <el-table-column
        label="序号"
        width="70"
        align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="name" label="名称" width="80" />

      <el-table-column label="头衔" width="90">
        <template slot-scope="scope">
          <span v-if="scope.row.level === 1">
            <el-tag type="success" size="mini">高级讲师</el-tag>
          </span>
          <span v-if="scope.row.level === 2">
            <el-tag size="mini">首席讲师</el-tag>
          </span>
        </template>
      </el-table-column>

      <el-table-column prop="intro" label="资历" />

      <el-table-column label="入驻时间" width="160">
        <template slot-scope="scope">
          {{ scope.row.joinDate }}
        </template>
      </el-table-column>

      <el-table-column prop="sort" label="排序" width="60" />

      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">

          <router-link :to="'/edu/teacher/edit/'+scope.row.id">
            <el-button type="primary" size="mini" icon="el-icon-edit">修改</el-button>
          </router-link>
          <el-button type="danger" size="mini" icon="el-icon-delete" @click="removeById(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <!-- 注意  @size-change="changeSize" ： changeSize是函数的引用 -->
    <el-pagination
      :total="total"
      :page-size="limit"
      :page-sizes="[5,10,20]"
      layout="sizes, prev, pager, next, jumper, ->, total"
      @size-change="changeSize"
      @current-change="changeCurrent"/>
  </div>
</template>

<script>
// 导入teacher的api组件
import teacherApi from '@/api/edu/teacher'

export default {

  data() {
    return {
      list: null, // 讲师列表
      total: 0, // 总记录数
      page: 1, // 页码
      limit: 5, // 每页记录数
      searchObj: {}, // 查询条件
      multipleSelection: [] // 批量删除选中的记录列表
    }
  },

  // 生命周期函数：页面加载时调用远程ajax请求，初始化讲师列表
  created() {
  // 初始化讲师列表
    this.fetchData()
  },

  methods: {

    querySearch(queryString, cb) {
      console.log('queryString', queryString)
      console.log('cb', cb)

      teacherApi.selectNameListByKey(queryString).then(response => {
        cb(response.data.nameList)
      })
    },

    // 事件： 当表格的选中项发生变化的时候
    handleSelectionChange(selection) {
      console.log('selection', selection)
      this.multipleSelection = selection
    },

    // 批量删除
    batchRemove() {
      console.log('batchRemove')
      // 被选中的数据的id列表
      // var idList = []
      // this.multipleSelection.forEach(item => {
      //   idList.push(item.id)
      // })
      // // 调用批量删除的方法
      // teacherApi.batchRemove(idList)

      if (this.multipleSelection.length === 0) {
        this.$message({
          type: 'warning',
          message: '请选择要删除的记录!'
        })
        return
      }

      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { // promise
        // 点击确定，远程调用ajax
        // 遍历selection，将id取出放入id列表
        var idList = []
        this.multipleSelection.forEach(item => {
          idList.push(item.id)
          // console.log(idList)
        })
        // 调用api
        return teacherApi.batchRemove(idList)
      }).then((response) => {
        this.fetchData(this.page)
        this.$message({
          type: 'success',
          message: '删除成功!'
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },

    // 每页记录数改变
    changeSize(size) {
      console.log('每页记录数改变', size)
      this.limit = size
      this.fetchData()
    },

    // 页码数的改变
    changeCurrent(page) {
      console.log('当前页码', page)
      this.page = page
      this.fetchData()
    },

    // 初始化讲师列表
    // 参数 page: 当前页
    fetchData() {
      // console.log('page', page)
      // 调用api模块中的获取讲师列表的方法
      teacherApi.getPageList(this.page, this.limit, this.searchObj).then(response => {
        // r对象：response
        this.list = response.data.rows
        this.total = response.data.total
        console.log(response)
      })
    },

    // 执行删除
    removeById(teacherId) {
      console.log('teacherId', teacherId)
      // 询问用户是否要执行删除
      this.$confirm('您确定要删除这条记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { // 确认回调
        // Ajax调用远程方法
        // 执行删除api
        console.log('执行删除api')
        return teacherApi.removeById(teacherId)
      }).then(response => {
        // 刷新列表
        this.fetchData()
        // 弹出删除成功
        this.$message({
          message: response.message,
          type: 'success'
        })
      }).catch(error => { // 取消回调
        console.log('error', error)
        // 当取消时会进入catch语句：error = 'cancel'
        // 当后端服务抛出异常时：error = 'error'
        if (error === 'cancel') {
          this.$message({
            message: '取消删除'
          // duration: 0, // 显示提示时间，为0时为永远不消失提示信息
          // showClose: true // 关闭显示弹框
          })
        }
      })
    },

    // 重置查询表单
    resetData() {
      this.searchObj = {}
      this.fetchData()
    }
  }
}
</script>
