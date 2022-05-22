<template>
  <a-modal title="考題詳情" :width="640" :visible="visible" :confirmLoading="confirmLoading" @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <h3><b>題目：</b></h3>
        <div v-html="question.name"></div>
        <br>
        <h3><b>選項：</b></h3>
        <ul>
          <li v-for="option in question.options" :key="option.id" v-html="option.content"/>
        </ul>
        <br>
        <h3><b>答案：</b></h3>
        <ul>
          <li v-for="option in question.options" :key="option.id" v-show="option.answer===true" v-html="option.content"/>
        </ul>
        <br>
        <h3><b>解析：</b></h3>
        <div v-html="question.description"></div>
      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button key="cancel" @click="handleCancel">關閉</a-button>
    </template>
  </a-modal>
</template>

<script>

export default {
  name: 'QuestionViewModal',
  data () {
    return {
      visible: false,
      confirmLoading: false,

      form: this.$form.createForm(this),
      question: {},
      options: [],
      answerOption: ''
    }
  },
  methods: {
    edit (record) {
      this.visible = true
      this.question = record
    },

    handleCancel () {
      // clear form & currentStep
      this.visible = false
    }
  }
}
</script>
