<template>
  <a-modal title="新增考題" :width="800" :visible="visible" :confirmLoading="confirmLoading" @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <a-steps :current="currentStep" :style="{ marginBottom: '28px' }" size="small">
        <a-step title="考題內容" />
        <a-step title="問題分類" />
        <a-step title="問題選項" />
      </a-steps>
      <a-form :form="form">
        <!-- step1 -->
        <div v-show="currentStep === 0">
          <a-form-item label="考題" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <div id="summernote-question-name"></div>
          </a-form-item>
          <a-form-item label="解析" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <div id="summernote-question-desc"></div>
          </a-form-item>
        </div>
        <div v-show="currentStep === 1">
          <a-form-item label="題型" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-select v-decorator="['type', {rules: [{required: true}]}]" placeholder="請選擇題型" style="width: 100%">
              <a-select-option v-for="typeObj in types" :value="typeObj.id" :key="typeObj.id">
                {{ typeObj.description }}
              </a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item label="分類" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-select v-decorator="['category', { rules: [{required: true}]}]" placeholder="請選擇分類" style="width: 100%">
              <a-select-option v-for="category in categories" :value="category.id" :key="category.id">
                {{ category.name }}
              </a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item label="難度" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-select v-decorator="['level', { rules: [{required: true}]}]" placeholder="請選擇難度" style="width: 100%">
              <a-select-option v-for="level in levels" :value="level.id" :key="level.id">
                {{ level.description }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </div>

        <div v-show="currentStep === 2">
          <a-form-item label="建立選項" :labelCol="labelCol" :wrapperCol="wrapperCol" v-if="type!==3">
            <a-input
              v-decorator="['option', { rules: [{required: true}]}]"
              placeholder="輸入內容後按Enter，加入到下方選項列表"
              @pressEnter="addOption()"
            />
          </a-form-item>

          <a-form-item label="設定答案" :labelCol="labelCol" :wrapperCol="wrapperCol" enterButton="Search">
            <!-- 須判斷單選、是非 / 多選的區別 -->
            <!-- 單選 -->
            <a-select
              style="width: 100%"
              placeholder="請選擇答案"
              v-if="type===1"
              @change="handleSingleChange"
              :value="answerOption"
            >

              <a-select-option v-for="(option,index) in options" :value="option.content" :key="index">
                {{ option.content }}
              </a-select-option>
            </a-select>
            <!-- 多選 -->
            <a-select
              mode="multiple"
              :size="size"
              placeholder="請選擇答案"
              :value="answerOptions"
              v-if="type===2"
              style="width: 100%"
              @popupScroll="popupScroll"
              @change="handleMultiChange"
            >
              <a-select-option v-for="(option, index) in options" :value="option.content" :key="index">
                {{ option.content }}
              </a-select-option>
            </a-select>
            <!-- 是非 -->
            <a-select
              style="width: 100%"
              placeholder="請選擇答案"
              v-if="type===3"
              @change="handleSingleChange"
              :value="answerOption"
            >

              <a-select-option v-for="(option,index) in options" :value="option.content" :key="index">
                {{ option.content }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </div>
        <!-- step1 end -->
      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button key="back" @click="backward" v-if="currentStep > 0" :style="{ float: 'left' }">上一步</a-button>
      <a-button key="cancel" @click="handleCancel">取消</a-button>
      <a-button key="forward" :loading="confirmLoading" type="primary" @click="handleNext(currentStep)">
        {{ currentStep === 2 && '完成' || '下一步' }}
      </a-button>
    </template>
  </a-modal>
</template>

<script>
import '../../../plugins/summernote'
import $ from 'jquery'
import { getQuestionSelection, questionCreate } from '../../../api/exam'

const stepForms = [[], ['type', 'category', 'level'], ['option']]

export default {
  name: 'StepByStepQuestionModal',
  data () {
    return {
      labelCol: {
        xs: { span: 2 },
        sm: { span: 2 }
      },
      size: 'default',
      wrapperCol: {
        xs: { span: 22 },
        sm: { span: 22 }
      },
      answerOption: '',
      answerOptions: [],
      visible: false,
      confirmLoading: false,
      currentStep: 0,
      mdl: {},

      form: this.$form.createForm(this),
      categories: [],
      levels: [],
      types: [],
      optionToAdd: '新增選項',
      options: [],
      judgeOptions: [
        {
          answer: false,
          content: '正確'
        },
        {
          answer: false,
          content: '錯誤'
        }
      ],
      type: ''
    }
  },
  updated () {
    this.initSummernote('summernote-question-name')
    this.initSummernote('summernote-question-desc')
  },
  methods: {
    initSummernote (divId) {
      $('#' + divId).summernote({
        lang: 'zh-tw',
        placeholder: '請輸入內容',
        height: 150,
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
    create () {
      this.visible = true
      getQuestionSelection().then(res => {
        console.log(res)
        if (res.code === 0) {
          console.log(res.data)
          this.categories = res.data.categories
          this.levels = res.data.levels
          this.types = res.data.types
        } else {
          this.$notification.error({
            message: '取得考題下拉選項失敗',
            description: res.msg
          })
        }
      }).catch(err => {
        this.$notification.error({
          message: '取得考題下拉選項失敗',
          description: err.message
        })
      })
    },
    popupScroll () {
      console.log('popupScroll')
    },
    handleNext (step) {
      const { form: { validateFields } } = this
      const currentStep = step + 1
      if (currentStep <= 2) {
        // stepForms
        validateFields(stepForms[this.currentStep], (errors, values) => {
          if (!errors) {
            this.currentStep = currentStep
            this.type = values.type
            this.answerOptions = []
            this.answerOption = ''
            if (this.type === 3) {
              this.options = this.judgeOptions
            } else {
              this.options = []
            }
          }
        })
        return
      }
      this.confirmLoading = true
      validateFields((errors, values) => {
        console.log('errors:', errors, 'val:', values)
        values.options = this.options
        values.name = this.getSummernoteContent('summernote-question-name')
        values.desc = this.getSummernoteContent('summernote-question-desc')
        this.confirmLoading = false
        if (!errors) {
          console.log('values:', values)
          questionCreate(values).then(res => {
            console.log(res)
            if (res.code === 0) {
              this.$notification.success({
                message: '新增成功',
                description: '考題新增成功'
              })
              this.visible = false
              this.$emit('ok')
            }
          }).catch(err => {
            this.$notification.error({
              message: '新增失敗',
              description: err.message
            })
          })
        } else {
          this.confirmLoading = false
        }
      })
    },
    backward () {
      this.currentStep--
    },
    handleCancel () {
      // clear form & currentStep
      this.visible = false
      this.currentStep = 0
    },
    addOption () {
      // 這邊需要把選項+是否是題目答案作为一個options
      const { form: { validateFields } } = this
      validateFields((errors, values) => {
        console.log('errors:', errors, 'val:', values)
        for (let i = 0; i < this.options.length; i++) {
          const option = this.options[i]
          if (option.content === values.option) {
            this.$notification.error({
              message: '錯誤',
              description: '請勿重複新增相同的選項'
            })
            return
          }
        }
        const optionObj = { content: values.option, answer: false }
        this.options.push(optionObj)
        this.$notification.success({
          message: '選項新增成功',
          description: '若為答案，請到\'設定答案\'下拉選單中選擇此選項'
        })
      })
    },
    // 單選
    handleSingleChange (value) {
      for (let i = 0; i < this.options.length; i++) {
        if (this.options[i].content === value) {
          this.options[i].answer = true
          this.answerOption = this.options[i].content
        } else {
          this.options[i].answer = false
        }
      }
      console.log(`Selected: ${value}`)
    },
    // 多選
    handleMultiChange (values) {
      console.log(values)
      this.answerOptions = values
      for (let i = 0; i < this.options.length; i++) {
        const content = this.options[i].content
        let isAnswer = false
        for (let j = 0; j < values.length; j++) {
          const value = values[j]
          if (content === value) {
            isAnswer = true
            this.options[i].answer = true
            break
          }
        }
        if (isAnswer === false) {
          this.options[i].answer = false
        }
      }
    }

  }
}
</script>
