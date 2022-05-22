<template>
  <a-modal title="編輯方面" :width="400" :visible="visible" :confirmLoading="confirmLoading" @cancel="handleCancel">
    <div id="summernote-exam-avatar"></div>
    <template slot="footer">
      <a-button key="update" @click="handleUpdate">完成</a-button>
      <a-button key="cancel" @click="handleCancel">關閉</a-button>
    </template>
  </a-modal>
</template>

<script>
import { examUpdate } from '@api/exam'
import '../../../plugins/summernote'
import $ from 'jquery'

export default {
  name: 'UpdateAvatarModal',
  data () {
    return {
      confirmLoading: false,
      visible: false,
      exam: {}
    }
  },
  updated () {
    this.initSummernote()
    $('#summernote-exam-avatar').summernote('code', this.exam.avatar)
  },
  methods: {
    initSummernote () {
      $('#summernote-exam-avatar').summernote({
        lang: 'zh-tw',
        placeholder: '請輸入內容',
        height: 200,
        width: 320,
        htmlMode: true,
        toolbar: [],
        fontSizes: ['8', '9', '10', '11', '12', '14', '18', '24', '36'],
        fontNames: [
          'Arial', 'Arial Black', 'Comic Sans MS', 'Courier New',
          'Helvetica Neue', 'Helvetica', 'Impact', 'Lucida Grande',
          'Tahoma', 'Times New Roman', 'Verdana'
        ]
      })
    },
    edit (exam) {
      this.visible = true
      Object.assign(this.exam, exam)
      this.avatar = exam.avatar
    },
    handleCancel () {
      // clear form & currentStep
      this.visible = false
    },
    handleUpdate () {
      const that = this
      that.exam.avatar = $('#summernote-exam-avatar').summernote('code')
      examUpdate(that.exam).then(res => {
        console.log(res)
        if (res.code === 0) {
          that.$notification.success({
            message: '更新成功',
            description: '考試更新成功'
          })
          that.visible = false
          that.$emit('ok')
        }
      }).catch(err => {
        that.$notification.error({
          message: '考試更新失敗',
          description: err.message
        })
      })
    }
  }
}
</script>
