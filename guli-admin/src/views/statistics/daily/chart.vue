<template>
  <div class="app-container">
    <!--表单-->
    <el-form :inline="true" class="demo-form-inline">

      <el-form-item>
        <el-select v-model="searchObj.type" clearable placeholder="请选择">
          <el-option label="学员登录数统计" value="login_num"/>
          <el-option label="学员注册数统计" value="register_num"/>
          <el-option label="课程播放数统计" value="video_view_num"/>
          <el-option label="每日课程数统计" value="course_num"/>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-date-picker
          v-model="searchObj.begin"
          type="date"
          placeholder="选择开始日期"
          value-format="yyyy-MM-dd" />
      </el-form-item>
      <el-form-item>
        <el-date-picker
          v-model="searchObj.end"
          type="date"
          placeholder="选择截止日期"
          value-format="yyyy-MM-dd" />
      </el-form-item>
      <el-button
        :disabled="btnDisabled"
        type="primary"
        icon="el-icon-search"
        @click="showChart()">查询</el-button>
    </el-form>

    <div class="chart-container">
      <div id="chart" class="chart" style="height:500px;width:100%" />
    </div>
  </div>
</template>

<script>

import echarts from 'echarts'
import dailyApi from '@/api/statistics/daily'

export default {

  data() {
    return {
      searchObj: {
        type: '',
        begin: '',
        end: ''
      },
      btnDisabled: false,
      title: '',
      xData: [],
      yData: []
    }
  },

  methods: {
    // 获取图表数据
    showChart() {
      dailyApi.showChart(this.searchObj).then(response => {
        this.xData = response.data.xData
        this.yData = response.data.yData

        this.setChart()
      })
    },

    // 设置图表参数并展示图表
    setChart() {
      switch (this.searchObj.type) {
        case 'login_num':
          this.title = '用户登录数统计'
          break
        case 'register_num':
          this.title = '用户注册数统计'
          break
        case 'video_view_num':
          this.title = '视频观看量统计'
          break
        case 'course_num':
          this.title = '课程发布数统计'
          break
      }

      // 基于准备好的dom，初始化echarts实例
      var myChart = echarts.init(document.getElementById('chart'))

      var option = {
        title: {
          text: this.title
        },
        tooltip: {},
        legend: {
          data: ['数量']
        },
        xAxis: {
        //   data: ['2019-01-01', '2019-01-02', '2019-01-03', '2019-01-04', '2019-01-05', '2019-01-06']
          data: this.xData
        },
        yAxis: {},
        series: [{
          name: '数量',
          type: 'line',
          data: this.yData
        }],
        dataZoom: [{
          type: 'inside',
          start: 0,
          end: 10
        }]
      }

      // 使用刚指定的配置项和数据显示图表。
      myChart.setOption(option)
    }
  }
}
</script>
