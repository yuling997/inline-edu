<template>
  <article class="col-7 fl">
    <div class="u-r-cont">
      <section>
        <div>
          <section class="c-infor-tabTitle c-tab-title">

            <a href="javascript: void(0)" title="我的订单" class="current">
              订单列表
            </a>
          </section>
        </div>
        <div class="mt40">
          <section v-if="data.total == 0" class="no-data-wrap">
            <em class="icon30 no-data-ico">&nbsp;</em>
            <span class="c-666 fsize14 ml10 vam" >您还没有收到消息哦！</span>

          </section>
          <div>
            <!-- 表格 -->
            <el-table
              v-loading="listLoading"
              :data="data.items"
              element-loading-text="数据加载中"
              border
              fit
              highlight-current-row
              row-class-name="myClassList">

              <el-table-column label="课程信息" width="300" align="center" >
                <template slot-scope="scope">
                  <div class="info" >
                    <div class="pic">
                      <img :src="scope.row.courseCover" alt="scope.row.courseTitle" width="100px">
                    </div>
                    <div class="title">
                      <a href="">{{ scope.row.courseTitle }}</a>
                      <!-- <p>{{ scope.row.lessonNum }}课时</p> -->
                      <input :value="scope.row.courseId" hidden>
                    </div>
                  </div>

                </template>
              </el-table-column>
              <el-table-column label="讲师名称" align="center">
                <template slot-scope="scope">
                  {{ scope.row.teacherName }}
                </template>
              </el-table-column>
              <el-table-column label="价格" width="100" align="center" >
                <template slot-scope="scope">

                  {{ Number(scope.row.totalFee) !== 0 ? '¥' + scope.row.totalFee : '' }}
                </template>
              </el-table-column>
              <el-table-column label="创建时间" align="center">
                <template slot-scope="scope">
                  {{ scope.row.gmtCreate.substr(0, 10) }}
                </template>
              </el-table-column>
              <el-table-column prop="status" label="订单状态" width="100" align="center" >
                <template slot-scope="scope">
                  <el-tag :type="scope.row.status == '0' ? 'warning' : 'success'">{{ scope.row.status == '0' ? '未支付' : '已支付' }}</el-tag>
                </template>
              </el-table-column>

              <el-table-column label="操作" width="150" align="center">
                <template slot-scope="scope" >
                  <router-link v-if="scope.row.status == '0'" :to="'/pay/'+scope.row.orderNo">
                    <el-button type="text" size="mini" icon="el-icon-edit">去支付</el-button>
                  </router-link>
                  <router-link v-if="scope.row.status == '1'" :to="'/course/'+scope.row.courseId">
                    <el-button type="text" size="mini" icon="el-icon-edit">去学习</el-button>
                  </router-link>
                </template>

              </el-table-column>
            </el-table>
          </div>

          <div class="paging">
            <!-- undisable这个class是否存在，取决于数据属性hasPrevious -->
            <a
              :class="{undisable: !data.hasPrevious}"
              href="#"
              title="首页"
              @click.prevent="gotoPage(1)">首</a>
            <a
              :class="{undisable: !data.hasPrevious}"
              href="#"
              title="前一页"
              @click.prevent="gotoPage(data.current-1)">&lt;</a>
            <a
              v-for="page in data.pages"
              :key="page"
              :class="{current: data.current == page, undisable: data.current == page}"
              :title="'第'+page+'页'"
              href="#"
              @click.prevent="gotoPage(page)">{{ page }}</a>
            <a
              :class="{undisable: !data.hasNext}"
              href="#"
              title="后一页"
              @click.prevent="gotoPage(data.current+1)">&gt;</a>
            <a
              :class="{undisable: !data.hasNext}"
              href="#"
              title="末页"
              @click.prevent="gotoPage(data.pages)">末</a>
            <div class="clear"/>
          </div>

          <!-- /公共分页 结束 -->
        </div>
      </section>
    </div>
  </article>
</template>
<script>
import orderApi from '@/api/order'

export default {

  data() {
    return {
      listLoading: false,
      data: {}
    }
  },
  created() {
    this.initDataObj()
  },
  methods: {
    initDataObj() {
      orderApi.getOrderPageList(1, 5).then(response => {
        this.data = response.data.data
        // console.log(this.data)
        this.listLoading = false
      })
    },
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex === 1) {
        return 'warning-row'
      } else if (rowIndex === 3) {
        return 'success-row'
      }
      return ''
    },
    gotoPage(page) {
      orderApi.getOrderPageList(page, 5).then(response => {
        this.data = response.data.data
      })
    }
  }
}
</script>
<style>
  .el-table .warning-row {
    background: oldlace;
  }

  .el-table .success-row {
    background: #f0f9eb;
  }
</style>
