<template>
  <a-modal title="編輯考題" :width="640" :visible="visible" :confirmLoading="confirmLoading" @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <h3><b>題型：</b></h3>
        <div id="summernote-question-name-edit" />
        <ul v-show="question.type==='多選題'">
          <li v-for="option in question.options" :key="option.id">
            <a-input v-model="option.content" />
          </li>
        </ul>

        <ul v-show="question.type!=='多選題'">
          <li v-for="option in question.options" :key="option.id">
            <a-input v-model="option.content" />
          </li>
        </ul>

        <h3><b>答案：</b></h3>
        <ul v-show="question.type!=='多選題'">
          <li>
            <a-select :size="size" :value="answerOptionId" style="width: 100%" @change="handleSingleChange">
              <a-select-option v-for="option in question.options" :key="option.id">
                {{ option.content }}
              </a-select-option>
            </a-select>
          </li>
        </ul>

        <ul v-show="question.type==='多選題'">
          <li>
            <a-select
              mode="multiple"
              :size="size"
              placeholder="Please select"
              :value="answerOptionIds"
              style="width: 100%"
              @change="handleMultiChange"
              @popupScroll="popupScroll"
            >
              <a-select-option v-for="option in question.options" :key="option.id">
                {{ option.content }}
              </a-select-option>
            </a-select>
          </li>
        </ul>
        <h3><b>解析：</b></h3>
        <div id="summernote-question-desc-edit" />
      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button key="cancel" @click="handleCancel">關閉</a-button>
      <a-button key="update" type="primary" @click="handleUpdate">更新</a-button>
    </template>
  </a-modal>
</template>

<script>
import '../../../plugins/summernote'
import $ from 'jquery'
import { questionUpdate } from '../../../api/exam'

export default {
  name: 'QuestionEditModal',
  data () {
    return {
      visible: false,
      size: 'default',
      confirmLoading: false,

      form: this.$form.createForm(this),
      question: {},
      // 單選/是非題的答案
      answerOptionId: '',
      // 多選題的答案
      answerOptionIds: [],
      radioStyle: {
        display: 'block',
        height: '30px',
        lineHeight: '30px'
      },
      name: '',
      desc: ''
    }
  },

  updated () {
    this.initSummernote('summernote-question-name-edit')
    this.initSummernote('summernote-question-desc-edit')
    this.setSummernoteContent('summernote-question-name-edit', this.name)
    this.setSummernoteContent('summernote-question-desc-edit', this.desc)
  },
  methods: {
    initSummernote (divId) {
      $('#' + divId).summernote({
        lang: 'zh-tw',
        placeholder: '請輸入內容',
        height: 200,
        width: '100%',
        htmlMode: true,
        toolbar: [
          ['style', ['bold', 'italic', 'underline', 'clear']],
          ['fontsize', ['fontsize']],
          ['fontname', ['fontname']],
          ['para', ['ul', 'ol', 'paragraph']]
        ],
        fontSizes: ['8', '9', '10', '11', '12', '14', '18', '24', '36'],
        fontNames: [
          'Arial', 'Arial Black', 'Comic Sans MS', 'Courier New',
          'Helvetica Neue', 'Helvetica', 'Impact', 'Lucida Grande',
          'Tahoma', 'Times New Roman', 'Verdana'
        ],
        callbacks: {
          onSubmit: function () {
            this.richContent = $('#summernote').summernote('code')
          }
        }
      })
    },
    getSummernoteContent (divId) {
      return $('#' + divId).summernote('code')
    },
    setSummernoteContent (divId, content) {
      return $('#' + divId).summernote('code', content)
    },
    edit (record) {
      this.name = record.name
      this.desc = record.description
      this.question = record
      this.answerOptionId = ''
      this.answerOptionIds = []
      this.visible = true
      for (let i = 0; i < this.question.options.length; i++) {
        if (this.question.options[i].answer === true) {
          this.answerOptionId = this.question.options[i].id
          this.answerOptionIds.push(this.question.options[i].id)
        }
      }
    },

    handleCancel () {
      // clear form & currentStep
      this.visible = false
    },

    handleSingleChange (value) {
      // 單選
      for (let i = 0; i < this.question.options.length; i++) {
        if (this.question.options[i].id === value) {
          // 更新唯一的正確答案
          this.question.options[i].answer = true
          // 設定答案內容
          this.answerOptionId = this.question.options[i].id
        } else {
          // id不和答案相同地就設為false
          this.question.options[i].answer = false
        }
      }
      console.log(`Selected: ${value}`)
    },

    // 多選
    handleMultiChange (values) {
      console.log(values)
      this.answerOptionIds = values
      for (let i = 0; i < this.question.options.length; i++) {
        const id = this.question.options[i].id
        let isAnswer = false
        for (let j = 0; j < values.length; j++) {
          const value = values[j]
          if (id === value) {
            isAnswer = true
            this.question.options[i].answer = true
          }
        }
        if (isAnswer === false) {
          this.question.options[i].answer = false
        }
      }
    },

    popupScroll () {
      console.log('popupScroll')
    },

    handleUpdate () {
      const that = this
      that.question.name = that.getSummernoteContent('summernote-question-name-edit')
      that.question.description = that.getSummernoteContent('summernote-question-desc-edit')
      console.log(that.question)
      questionUpdate(that.question).then(res => {
        console.log(res)
        if (res.code === 0) {
          that.$notification.success({
            message: '更新成功',
            description: '考題更新成功'
          })
          that.visible = false
          that.$emit('ok')
        }
      }).catch(err => {
        that.$notification.error({
          message: '更新失敗',
          description: err.message
        })
      })
    }
  }
}
</script>
