<template>
  <!-- .sync：关闭窗口 -->
  <el-dialog :visible.sync="dialogVisible" title="添加章节" @close="close">
    <el-form :model="chapter" label-width="120px">
      <el-form-item label="章节标题">
        <el-input v-model="chapter.title"/>
      </el-form-item>
      <el-form-item label="章节排序">
        <el-input-number v-model="chapter.sort" :min="0" controls-position="right"/>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close()">取 消</el-button>
      <el-button type="primary" @click="saveOrUpdate()">确 定</el-button>
    </div>
  </el-dialog>
  <!-- <div>
    章节表单 {{ courseId }}
  </div> -->
</template>

<script>
// 引入api
import chapterApi from '@/api/edu/chapter'

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

      dialogVisible: false, // 隐藏对话框
      chapter: {
        sort: 0
      } // 章节对象
    }
  },

  methods: {

    // 打开添加章节对话框
    open(chapterId) {
      this.dialogVisible = true
      if (chapterId) {
        // 获取chapter的回显数据
        chapterApi.getById(chapterId).then(response => {
          this.chapter = response.data.item
        })
      }
    },

    // 关闭窗口
    close() {
      // 隐藏窗口
      this.dialogVisible = false
      // 重置表单
      this.resetForm()
    },

    resetForm() {
      this.chapter = {
        id: null,
        title: '',
        sort: 0
      }
    },

    // 保存章节信息
    saveOrUpdate() {
      if (!this.chapter.id) {
        this.save()
      } else {
        this.update()
      }
    },

    // 新增
    save() {
      this.chapter.courseId = this.courseId
      // 保存
      chapterApi.save(this.chapter).then(response => {
        //   提示
        this.$message({
          type: 'success',
          message: '保存成功'
        })
        // 关闭窗口
        this.close()
        // 刷新列表(子组件中调用父组件得到方法)
        // 定义事件event
        this.$emit('onSaveSuccess')
      })
    },

    // 更新
    update() {
      chapterApi.updateById(this.chapter).then(response => {
        //   提示
        this.$message({
          type: 'success',
          message: '修改成功'
        })
        // 关闭窗口
        this.close()
        // 刷新列表(子组件中调用父组件得到方法)
        // 定义事件event
        this.$emit('onSaveSuccess')
      })
    }
  }

}
</script>
