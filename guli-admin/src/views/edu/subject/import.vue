<template>
  <div class="app-container">
    <el-form label-width="120px">
      <el-form-item label="信息描述">
        <el-tag type="info">excel模版说明</el-tag>
        <el-tag>
          <i class="el-icon-download"/>
          <a :href="defaultExcelTemplate">点击下载模版</a>
        </el-tag>

      </el-form-item>

      <el-form-item label="选择Excel">
        <el-upload
          ref="upload"
          :auto-upload="false"
          :on-exceed="fileUploadExceed"
          :on-success="fileUploadSuccess"
          :on-error="fileUploadError"
          :limit="1"
          action="http://127.0.0.1:8110/admin/edu/subject/import"
          name="file"
          accept="application/vnd.ms-excel"> <!--MIME类型  媒体类型 -->
          <el-button
            slot="trigger"
            size="small"
            type="primary">选取文件</el-button>
          <el-button
            :disabled="importBtnDisabled"
            style="margin-left: 10px;"
            size="small"
            type="success"
            @click="submitUpload">导入</el-button>
        </el-upload>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      defaultExcelTemplate: process.env.OSS_PATH + '/excel/课程分类列表.xls', // 默认Excel模板
      importBtnDisabled: false // 导入按钮是否禁用
    }
  },

  methods: {

    // 上传文件数量超出范围时的回调函数
    fileUploadExceed() {
      // console.log('files', files)
      // console.log('fileList', fileList)
      this.$message.warning('只能上传一个文件，请先删除列表中的文件')
    },

    // 上传文件成功时的回调函数
    fileUploadSuccess(response) {
      console.log('fileUploadSuccess')
      this.importBtnDisabled = false
      this.$message({
        type: 'success',
        message: response.message
      })
      // 清空已经上传的文件列表
      this.$refs.upload.clearFiles()
    },

    // 上传文件失败时的回调函数
    fileUploadError() {
      console.log('fileUploadError')
      this.importBtnDisabled = false
      this.$message({
        type: 'error',
        message: '导入失败'
      })
      // 清空已经上传的文件列表
      this.$refs.upload.clearFiles()
    },

    // 执行文件上传
    submitUpload() {
      this.importBtnDisabled = true
      this.$refs.upload.submit() // 手动提交文件的请求
    }
  }

}
</script>
