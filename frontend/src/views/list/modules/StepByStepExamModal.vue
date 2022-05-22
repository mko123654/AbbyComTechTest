<template>
  <a-modal
    title="新增考試"
    :width="640"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
      <a-steps :current="currentStep" :style="{ marginBottom: '28px' }" size="small">
        <a-step title="考試描述"/>
        <a-step title="每題分數"/>
        <a-step title="選擇題目"/>
      </a-steps>
      <a-form :form="form">
        <!-- step1 -->
        <div v-show="currentStep === 0">
          <a-form-item
            label="考試名稱"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
          >
            <a-input v-decorator="['name', {rules: [{required: true}]}]"/>
          </a-form-item>
          <a-form-item
            label="考試限時"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
          >
            <a-input-number
              :min="1"
              :max="200"
              v-decorator="['elapse', {initialValue: '90',rules: [{required: true}]}]"
            />
            分鐘
          </a-form-item>
          <a-form-item
            label="考試描述"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
          >
            <a-textarea :rows="2" v-decorator="['desc', {rules: [{required: true}]}]"></a-textarea>
          </a-form-item>
          <a-form-item
            label="考試封面照片"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
          >
            <div id="summernote-exam-avatar-create"></div>
          </a-form-item>
        </div>
        <div v-show="currentStep === 1">
          <a-form-item
            label="單選題"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
          >
            <a-input-number
              :min="1"
              :max="20"
              v-decorator="['radioScore', {initialValue: '5',rules: [{required: true}]}]"
            />
            分
          </a-form-item>

          <a-form-item
            label="多選題"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
          >
            <a-input-number
              :min="1"
              :max="20"
              v-decorator="['checkScore', {initialValue: '5',rules: [{required: true}]}]"
            />
            分
          </a-form-item>

          <a-form-item
            label="是非題"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
          >
            <a-input-number
              :min="1"
              :max="20"
              v-decorator="['judgeScore', {initialValue: '5',rules: [{required: true}]}]"
            />
            分
          </a-form-item>
        </div>

        <div v-show="currentStep === 2">
          <a-form-item
            label="選擇單選題"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            enterButton="Search"
          >
            <a-select
              mode="multiple"
              :size="size"
              placeholder="請選擇單選題"
              style="width: 100%"
              @popupScroll="popupScroll"
              @change="handleRadioChange"
            >
              <a-select-option v-for="radio in radios" :value="radio.name" :key="radio.id">
                {{ radio.name }}
              </a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item
            label="選擇多選題"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            enterButton="Search"
          >
            <a-select
              mode="multiple"
              :size="size"
              placeholder="請選擇多選題"
              style="width: 100%"
              @popupScroll="popupScroll"
              @change="handleCheckChange"
            >
              <a-select-option v-for="check in checks" :value="check.name" :key="check.id">
                {{ check.name }}
              </a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item
            label="選擇是非題"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            enterButton="Search"
          >
            <a-select
              mode="multiple"
              :size="size"
              placeholder="請選擇是非題"
              style="width: 100%"
              @popupScroll="popupScroll"
              @change="handleJudgeChange"
            >
              <a-select-option v-for="judge in judges" :value="judge.name" :key="judge.id">
                {{ judge.name }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </div>
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
import { getExamQuestionTypeList, examCreate } from '../../../api/exam'

const stepForms = [
  ['name', 'elapse', 'desc'],
  ['radioScore', 'checkScore', 'judgeScore'],
  ['option']
]

export default {
  name: 'StepByStepExamModal',
  data () {
    return {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 7 }
      },
      size: 'default',
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 13 }
      },
      visible: false,
      confirmLoading: false,
      currentStep: 0,
      mdl: {},

      form: this.$form.createForm(this),
      radios: [],
      checks: [],
      judges: []
    }
  },
  updated () {
    this.initSummernote()
  },
  methods: {
    initSummernote () {
      $('#summernote-exam-avatar-create').summernote({
        lang: 'zh-tw',
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
    create () {
      this.visible = true
      getExamQuestionTypeList().then(res => {
        console.log(res)
        if (res.code === 0) {
          console.log(res.data)
          this.radios = res.data.radios
          this.checks = res.data.checks
          this.judges = res.data.judges
        } else {
          this.$notification.error({
            message: '取得考題列表失敗',
            description: res.msg
          })
        }
      }).catch(err => {
        this.$notification.error({
          message: '取得考題列表失敗',
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
          }
        })
        return
      }

      this.confirmLoading = true
      validateFields((errors, values) => {
        values.avatar = $('#summernote-exam-avatar-create').summernote('code')
        values.radios = this.radios
        values.checks = this.checks
        values.judges = this.judges
        this.confirmLoading = false
        if (!errors) {
          console.log('values:', values)
          examCreate(values).then(res => {
            console.log(res)
            if (res.code === 0) {
              this.$notification.success({
                message: '新增考試成功',
                description: '新增考試成功'
              })
              this.visible = false
              this.$emit('ok')
            }
          }).catch(err => {
            this.$notification.error({
              message: '新增考試失敗',
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
    handleRadioChange (values) {
      console.log(values)
      for (let i = 0; i < this.radios.length; i++) {
        const name = this.radios[i].name
        let checked = false
        for (let j = 0; j < values.length; j++) {
          const value = values[j]
          if (name === value) {
            checked = true
            this.radios[i].checked = true
            break
          }
        }
        if (checked === false) {
          this.radios[i].checked = false
        }
      }
    },
    // 多選
    handleCheckChange (values) {
      console.log(values)
      for (let i = 0; i < this.checks.length; i++) {
        const name = this.checks[i].name
        let checked = false
        for (let j = 0; j < values.length; j++) {
          const value = values[j]
          if (name === value) {
            checked = true
            this.checks[i].checked = true
            break
          }
        }
        if (checked === false) {
          this.checks[i].checked = false
        }
      }
    },

    // 是非
    handleJudgeChange (values) {
      console.log(values)
      for (let i = 0; i < this.judges.length; i++) {
        const name = this.judges[i].name
        let checked = false
        for (let j = 0; j < values.length; j++) {
          const value = values[j]
          if (name === value) {
            checked = true
            this.judges[i].checked = true
            break
          }
        }
        if (checked === false) {
          this.judges[i].checked = false
        }
      }
    }

  }
}
</script>
