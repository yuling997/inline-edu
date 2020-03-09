<template>
  <!-- 添加和修改课时表单 -->
  <el-dialog :visible.sync="dialogVisible" title="添加课时" @close="close">
    <el-form :model="video" label-width="120px">
      <el-form-item label="课时标题">
        <el-input v-model="video.title"/>
      </el-form-item>
      <el-form-item label="课时排序">
        <el-input-number v-model="video.sort" :min="0" controls-position="right"/>
      </el-form-item>
      <el-form-item label="是否免费">
        <el-radio-group v-model="video.free">
          <el-radio :label="true">免费</el-radio>
          <el-radio :label="false">默认</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="上传视频">

        <!-- javascript sdk上传 -->
        <el-upload
          ref="upload"
          :auto-upload="false"
          :on-change="fileChange"
          :on-exceed="fileUploadExceed"
          :before-remove="handleBeforeRemove"
          :on-remove="handleOnRemove"
          :file-list="fileList"
          :limit="1"
          class="upload-demo"
          action=""><!--action属性不写会报错-->
          <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
          <el-tooltip placement="right-end">
            <div slot="content">最大支持1G，<br>
              支持3GP、ASF、AVI、DAT、DV、FLV、F4V、<br>
              GIF、M2T、M4V、MJ2、MJPEG、MKV、MOV、MP4、<br>
              MPE、MPG、MPEG、MTS、OGG、QT、RM、RMVB、<br>
              SWF、TS、VOB、WMV、WEBM 等视频格式上传</div>
            <i class="el-icon-question"/>
          </el-tooltip>
          <el-button
            :disabled="uploadBtnDisabled"
            style="margin-left: 10px;"
            size="small"
            type="success"
            @click="submitUpload()">上传</el-button>
          <label class="status"><span>{{ statusText }}</span></label>
          <span class="progress"><i id="auth-progress">{{ authProgress }}</i></span>
        </el-upload>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="saveOrUpdate">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
import videoApi from '@/api/edu/video'
import vodVideoApi from '@/api/vod/video'

export default {

  // 父组件向子组件传值
  props: {
    courseId: {
      type: String,
      default: null
    }
  },

  data() {
    return {
      dialogVisible: false,
      video: {// 课时对象
        chapterId: '',
        title: '',
        sort: 0,
        free: false,
        videoSourceId: '', // 阿里云视频id
        videoOriginalName: '' // 视频名称
      },
      fileList: [], // 文件上传列表
      uploadBtnDisabled: true, // 文件上传按钮状态

      // 初始化上传组件
      partSize: 1048576, // 分片大小默认1M，不能小于100K
      parallel: 5, // 并行上传分片个数，默认5
      retryCount: 3, // 网络原因失败时，重新上传次数，默认为3
      retryDuration: 2, // 网络原因失败时，重新上传间隔时间，默认为2秒
      region: 'cn-shanghai', // 上传到点播的地域， 默认值为'cn-shanghai',//eu-central-1,ap-southeast-1
      userId: '1506273167771201', // 阿里账号ID

      // 页面状态
      authProgress: null, // 上传进度
      statusText: '', // 上传状态提示

      // 核心对象
      uploader: null // 上传对象
    }
  },

  methods: {
    open(chapterId, videoId) {
      this.dialogVisible = true
      this.video.chapterId = chapterId
      if (videoId) { // 如果是编辑状态，则回填表单数据
        videoApi.getById(videoId).then(response => {
          this.video = response.data.item

          // 如果存在已经上传的视频文件，那么显示视频文件列表
          if (this.video.videoOriginalName) {
            this.fileList = [{
              name: this.video.videoOriginalName
            }]
          }
        })
      }
    },

    close() {
      this.dialogVisible = false
      // 重置表单
      this.resetForm()
    },

    resetForm() {
      this.video = {
        id: null,
        title: '',
        sort: 0,
        chapterId: '',
        free: false,
        videoSourceId: '',
        videoOriginalName: ''
      }

      this.fileList = []

      this.authProgress = null
      this.uploader = null
      this.statusText = ''
    },

    // 上传多于一个视频
    fileUploadExceed(files, fileList) {
      this.$message.warning('想要重新上传视频，请先删除列表中的视频')
    },

    // 删除视频文件确认:返回true则删除、否则不删除
    handleBeforeRemove(file) {
      return this.$confirm('确定要删除' + file.name + '吗？')
    },

    // 删除视频文件
    handleOnRemove() {
      vodVideoApi.removeByVodId(this.video.videoSourceId).then(respone => {
        // 删除数据库中存储的字段值
        this.video.videoSourceId = ''
        this.video.videoOriginalName = ''
        // 删除视频的同时更新视频信息到数据库中
        videoApi.updateById(this.video)
        this.$message({
          type: 'success',
          message: '删除成功!'
        })
      })
    },

    saveOrUpdate() {
      if (!this.video.id) {
        this.save()
      } else {
        this.update()
      }
    },

    save() {
      this.video.courseId = this.courseId
      videoApi.save(this.video).then(response => {
        this.$message({
          type: 'success',
          message: '保存成功!'
        })
        // 关闭组件
        this.close()
        // 调用父组件监听函数
        this.$emit('onSaveSuccess')
      })
    },

    update() {
      videoApi.updateById(this.video).then(response => {
        this.$message({
          type: 'success',
          message: '修改成功!'
        })
        // 关闭组件
        this.close()
        // 调用父组件监听函数
        this.$emit('onSaveSuccess')
      })
    },

    // 选择视频文件时触发
    fileChange(file, fileList) {
      console.log('fileChange')
      console.log('file', file.raw)
      // 初始化文件上传组件
      this.uploader = this.createUploader()
      // 调用uploader对象的添加文件的方法：文件添加成功时会触发uploader对象的addFileSuccess事件
      this.uploader.addFile(file.raw)
    },

    // 视频文件上传
    submitUpload() {
      // 调用uploader对象的文件上传方法：文件上传的过程会触发一系列的uploader对象的回调函数
      this.uploader.startUpload()
    },

    createUploader() {
      // 定义vue的this上下文
      const self = this

      /* eslint-disable no-undef */ // 防止AliyunUpload报错
      var uploader = new AliyunUpload.Vod({
        userId: self.userId,
        region: self.region,
        partSize: self.partSize,
        parallel: self.parallel,
        retryCount: self.retryCount,
        retryDuration: self.retryDuration,

        // ----定义回调事件-----
        // 添加文件成功
        addFileSuccess: function(uploadInfo) {
          console.log('addFileSuccess添加文件成功：')
          console.log('uploadInfo', uploadInfo)
          self.statusText = '添加文件成功, 等待上传...'
          self.uploadBtnDisabled = false
        },

        // 开始上传
        onUploadstarted: function(uploadInfo) {
          console.log('获取上传地址和凭证：' + uploadInfo.file.name)
          self.uploadBtnDisabled = true

          // 先获取上传凭证再执行上传
          const fileName = uploadInfo.file.name
          const title = fileName.substring(0, fileName.lastIndexOf('.'))
          vodVideoApi.getUploadAuthAndAddress(title, fileName).then(response => {
            const uploadAuth = response.data.uploadAuth
            const uploadAddress = response.data.uploadAddress
            const videoId = response.data.videoId
            // 开始上传：JavaScript SDK 携带上传地址和凭证执行上传
            uploader.setUploadAuthAndAddress(uploadInfo, uploadAuth, uploadAddress, videoId)
            console.log('开始上传')
            self.statusText = '开始上传...'
          })
        },

        // 文件上传进度，单位：字节, 可以在这个函数中拿到上传进度并显示在页面上
        onUploadProgress: function(uploadInfo, totalSize, progress) {
          console.log(
            '上传进度：' + uploadInfo.file.name +
                ', fileSize:' + totalSize +
                ', percent:' + Math.ceil(progress * 100) + '%')
          const progressPercent = Math.ceil(progress * 100)
          self.authProgress = progressPercent + '%'
          self.statusText = '文件上传中...'
        },

        // 上传凭证超时
        onUploadTokenExpired: function(uploadInfo) {
          // 上传大文件超时,需要调用resumeUploadWithAuth方法
          console.log('上传超时：' + uploadInfo.file.name)
          vodVideoApi.refreshUploadAuth(uploadInfo.videoId).then(response => {
            console.log('刷新上传凭证成功')
            const uploadAuth = response.data.uploadAuth
            // 上传凭证失效后恢复上传
            uploader.resumeUploadWithAuth(uploadAuth)
          })
        },

        // 文件上传成功
        onUploadSucceed: function(uploadInfo) {
          console.log('上传成功：' + uploadInfo.file.name)
          self.statusText = '文件上传成功!'
          const fileName = uploadInfo.file.name
          self.video.videoOriginalName = fileName.substring(0, fileName.lastIndexOf('.'))
          self.video.videoSourceId = uploadInfo.videoId
        },

        // 文件上传失败
        onUploadFailed: function(uploadInfo, code, message) {
          console.log('上传失败：' + uploadInfo.file.name + ',code:' + code + ', message:' + message)
          self.statusText = '文件上传失败!'
        },

        // 全部文件上传结束
        onUploadEnd: function(uploadInfo) {
          console.log('全部文件上传结束')
          self.uploadBtnDisabled = true
          self.statusText = '文件上传完毕'
        }

      })
      return uploader
    }
  }
}
</script>
