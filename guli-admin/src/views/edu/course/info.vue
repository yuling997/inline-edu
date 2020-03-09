<template>

  <div class="app-container">

    <h2 style="text-align: center;">发布新课程</h2>

    <el-steps :active="active" finish-status="success" align-center style="margin-bottom: 40px;">
      <el-step title="填写课程基本信息"/>
      <el-step title="创建课程大纲"/>
      <el-step title="发布课程"/>
    </el-steps>

    <!-- 课程信息表单 TODO -->
    <!-- 课程信息表单 -->
    <el-form label-width="120px">

      <el-form-item label="课程标题">
        <el-input v-model="courseInfo.title" placeholder=" 示例：机器学习项目课：从基础到搭建项目视频课程。专业名称注意大小写"/>
      </el-form-item>

      <!-- 所属分类 TODO -->
      <!-- 所属分类：级联下拉列表 -->
      <el-form-item label="课程类别">
        <!-- 一级分类 -->
        <el-select
          v-model="courseInfo.subjectParentId"
          placeholder="请选择"
          @change="subjectLevelOneChanged">
          <el-option
            v-for="subject in subjectNestedList"
            :key="subject.id"
            :label="subject.title"
            :value="subject.id"/>
        </el-select>

        <!-- 二级分类 TODO -->
        <!-- 二级分类 -->
        <el-select v-model="courseInfo.subjectId" placeholder="请选择">
          <el-option
            v-for="subject in subSubjectList"
            :key="subject.id"
            :label="subject.title"
            :value="subject.id"/>
        </el-select>
      </el-form-item>

      <!-- 课程讲师 TODO -->
      <el-form-item label="课程讲师">
        <el-select v-model="courseInfo.teacherId" aria-placeholder="请选择">
          <el-option
            v-for="teacher in teacherList"
            :key="teacher.id"
            :label="teacher.name"
            :value="teacher.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="总课时">
        <el-input-number :min="0" v-model="courseInfo.lessonNum" controls-position="right" placeholder="请填写课程的总课时数"/>
      </el-form-item>

      <!-- 课程简介 TODO -->
      <el-form-item label="课程简介">
        <tinymce ref="tinymce" :height="300" v-model="courseInfo.description" />
      </el-form-item>
      <!-- 课程封面 TODO -->
      <!-- 课程封面-->
      <el-form-item label="课程封面">
        <el-upload
          :show-file-list="false"
          :on-success="handleCoverSuccess"
          :before-upload="beforeCoverUpload"
          action="http://127.0.0.1:8120/admin/oss/file/upload?module=cover"
          class="avatar-uploader">
          <img :src="courseInfo.cover">
        </el-upload>
      </el-form-item>

      <el-form-item label="课程价格">
        <el-input-number :min="0" v-model="courseInfo.price" controls-position="right" placeholder="免费课程请设置为0元"/> 元
      </el-form-item>

      <div style="text-align:center">
        <el-button :disabled="saveBtnDisabled" type="primary" @click="next()">保存并下一步</el-button>
      </div>
  </el-form></div>
</template>

<script>
import courseApi from '@/api/edu/course'
import teacherApi from '@/api/edu/teacher'
import subjectApi from '@/api/edu/subject'
// 子组件
import Tinymce from '@/components/Tinymce'

export default {

  components: {
    Tinymce
  },

  data() {
    return {
      active: 0,
      saveBtnDisabled: false, // 保存按钮是否禁用
      courseInfo: {// 表单数据
        price: 0,
        lessonNum: 0,
        // 以下解决表单数据不全时insert语句非空校验
        teacherId: '',
        subjectId: '',
        subjectParentId: '',
        cover: process.env.OSS_PATH + '/cover/default.gif',
        description: ''
      },
      teacherList: [], // 讲师列表
      subjectNestedList: [], // 一级分类列表
      subSubjectList: [] // 二级分类列表

    }
  },

  watch: {
    $route(to, from) {
      console.log('watch $route')
      this.init()
    }
  },

  created() {
    console.log('info created')
    this.init()
  },

  methods: {

    init() {
      if (this.$route.params && this.$route.params.id) { // 回显
        // 根据id获取课程基本信息
        this.fetchCourseInfoById(this.$route.params.id)
      } else { // 新增
      // 表单数据的重置
        this.courseInfo = { // 表单数据
          lessonNum: 0,
          price: 0,
          cover: process.env.OSS_PATH + '/cover/default.gif',
          subjectId: '' // 解决二级分类无法选择
        }

        // 清空富文本编辑器的内容：富文本违法重置问题
        if (this.$refs.tinymce) { // 解决新建状态找不到this.$refs.tinymce
          this.$refs.tinymce.setContent('')
        }
        // 初始化分类列表
        this.initSubjectList()
      }
      // 获取讲师列表
      this.initTeacherList()
    },

    fetchCourseInfoById(id) {
      // 获取表单回填数据                    //1 Java    2 后端开发
      courseApi.getCourseInfoById(id).then(response => { // subject_id   subject_parent_id
        this.courseInfo = response.data.item

        // 初始化分类列表
        // this.initSubjectList()
        subjectApi.getNestedTreeList().then(response => {
          this.subjectNestedList = response.data.items

          // 填充二级分类菜单：遍历一级菜单列表
          this.subjectNestedList.forEach(subject => {
            // 找到和courseInfo.subjectParentId一致的父类别记录
            if (subject.id === this.courseInfo.subjectParentId) {
              // 拿到当前类别下的子类别列表，将子类别列表填入二级下拉菜单列表
              this.subSubjectList = subject.children
            }
          })
        })
      })
    },

    initTeacherList() {
      teacherApi.getList().then(response => {
        this.teacherList = response.data.items
      })
    },

    initSubjectList() {
      subjectApi.getNestedTreeList().then(response => {
        this.subjectNestedList = response.data.items
      })
    },

    next() {
      console.log('next')
      this.saveBtnDisabled = true
      if (!this.courseInfo.id) {
        this.saveData()
      } else {
        this.updateData()
      }
    },

    // 保存
    saveData() {
      courseApi.saveCourseInfo(this.courseInfo).then(response => {
        this.$message({
          type: 'success',
          message: '保存成功'
        })
        this.$router.push({ path: '/edu/course/chapter/' + response.data.courseId })
      })
    },

    updateData() {
      this.saveBtnDisabled = true
      courseApi.updateCourseInfoById(this.courseInfo).then(response => {
        this.$message({
          type: 'success',
          message: '更新成功'
        })
        this.$router.push({ path: '/edu/course/chapter/' + this.courseInfo.id })
      })
    },

    subjectLevelOneChanged(value) {
      console.log(value)
      this.subjectNestedList.forEach(subjectNested => {
        if (subjectNested.id === value) {
          this.courseInfo.subjectId = ''
          this.subSubjectList = subjectNested.children
        }
      })
    },

    // 课程封面 成功回调
    handleCoverSuccess(response) {
      this.courseInfo.cover = response.data.url
    },

    beforeCoverUpload(file) {
      const isJPG = file.type === 'image/jpeg'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('上传课程封面只能是 JPG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传课程封面大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    }

    // 课程信息回显

  }
}
</script>

<style scoped>
.tinymce-container {
  position: relative;
  line-height: normal;
}
</style>
