<template>
  <div class="app-container">
    <el-form label-width="120px">
      <el-form-item label="讲师名称">
        <el-input v-model="teacher.name"/>
      </el-form-item>
      <el-form-item label="入驻时间">
        <el-date-picker
          v-model="teacher.joinDate"
          type="date"
          placeholder="选择入驻时间"
          value-format="yyyy-MM-dd"
        />
      </el-form-item>
      <el-form-item label="讲师排序">
        <el-input-number v-model="teacher.sort" :min="0" controls-position="right"/>
      </el-form-item>
      <el-form-item label="讲师头衔">
        <el-select v-model="teacher.level" clearable placeholder="请选择">
          <!--
            数据类型一定要和取出的json中的一致，否则没法回填
            因此，这里value使用动态绑定的值，保证其数据类型是number
          -->
          <el-option :value="1" label="高级讲师"/>
          <el-option :value="2" label="首席讲师"/>
        </el-select>
      </el-form-item>
      <el-form-item label="讲师资历">
        <el-input v-model="teacher.career"/>
      </el-form-item>
      <el-form-item label="讲师简介">
        <el-input v-model="teacher.intro" :rows="10" type="textarea"/>
      </el-form-item>

      <!-- 讲师头像：TODO -->
      <el-form-item label="讲师头像">
        <!-- 头像预览 -->
        <pan-thumb :image="teacher.avatar" />
        <!-- 上传按钮 -->
        <el-button type="primary" icon="el-icon-upload" @click="imagecropperShow=true">上传头像
        </el-button>
        <!-- 上传组件 -->
        <!-- lang-type 默认语言为：zh 中文 -->
        <!--
          v-show：是否显示上传组件
          :key：类似于id，如果一个页面多个图片上传控件，可以做区分
          :url：后台上传的url地址
          :max-size：文件大小限制，单位kb
          @close：关闭上传组件
          @crop-upload-success：上传成功后的回调 -->
        <image-cropper
          v-show="imagecropperShow"
          :width="300"
          :height="300"
          :key="imagecropperKey"
          field="file"
          url="http://localhost:8120/admin/oss/file/upload?module=avatar"
          lang-type="zh"
          @close="close"
          @crop-upload-success="cropSuccess"/>

      </el-form-item>

      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

// 导入teacher的api组件
import teacherApi from '@/api/edu/teacher'
// 导入自定义的头像组件模块
import ImageCropper from '@/components/ImageCropper'
import PanThumb from '@/components/PanThumb'

export default {

// 在当前页面注册头像组件
  components: { ImageCropper, PanThumb },

  data() {
    return {
      saveBtnDisabled: false, // 保存按钮是否禁用
      teacher: {},
      imagecropperShow: false, // 是否显示文件上传弹窗
      imagecropperKey: 0 // 防止组件重用，修改key值可以重新初始化上传组件

    }
  },

  // 在某一个位置监听路由的变化，当路由变化时重新初始化表单数据： teacher
  watch: {
    $route(to, from) {
      console.log('watch 执行： $route对象发生变化')
      console.log('to', to)
      console.log('from', from)

      // 判断当前路由中是否包含id，如果包含，则回显数据
      this.init()
    }
  },

  created() {
    console.log('created')
    // 获取路由地址的id

    // 初始化表单数据

    // if (this.$route.params && this.$route.params.id) {
    //   this.fetchDataById(this.$route.params.id)
    // }
    this.init()
  },

  methods: {

    init() {
      if (this.$route.params && this.$route.params.id) { // 编辑状态
        this.fetchDataById(this.$route.params.id)
      } else { // 新增状态
        // 初始化对象
        this.teacher = {
          sort: 0,
          level: 1,
          avatar: process.env.OSS_PATH + '/avatar/default.jpg'
        }
      }
    },

    // 初始化表单数据
    fetchDataById(id) {
      teacherApi.getById(id).then(response => {
        this.teacher = response.data.item
      })
    },
    saveOrUpdate() {
      console.log('saveOrUpdate')
      this.saveBtnDisabled = true // 禁用按钮

      this.saveBtnDisabled = true
      if (!this.teacher.id) {
        this.saveData()
      } else {
        this.updateData()
      }
    },

    // 新增数据
    saveData() {
      // 调用api模块
      teacherApi.save(this.teacher).then(response => {
        // 弹出成功提示
        this.$message({
          type: 'success',
          message: response.message
        })
        // 跳转到列表页面：路由跳转

        this.$router.push({ path: '/edu/teacher' })
      })
    },

    // 修改数据
    updateData() {
      // teacher数据的获取
      teacherApi.updateById(this.teacher).then(response => {
        this.$message({
          type: 'success',
          message: response.message
        })
        this.$router.push({ path: '/edu/teacher' })
      })
    },

    // 头像上传成功的回调
    // resData: 上传成功后的响应结果
    cropSuccess(resData) {
      console.log('resData', resData)
      // 关闭文件上传组件
      this.imagecropperShow = false

      // 讲师头像回显
      this.teacher.avatar = resData.url

      // 初始化组件
      this.imagecropperKey++
    },

    // 关闭文件上传组件
    close() {
      // 关闭文件上传组件
      this.imagecropperShow = false

      // 初始化组件
      this.imagecropperKey++
    }

  }

}
</script>
