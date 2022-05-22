<template>
  <a-modal title="更新考試" :width="640" :visible="visible" :confirmLoading="confirmLoading" @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <a-steps :current="currentStep" :style="{ marginBottom: '28px' }" size="small">
        <a-step title="考試描述" />
        <a-step title="每題分數" />
        <a-step title="選擇題目" />
      </a-steps>
      <a-form :form="form">
        <!-- step1 -->
        <div v-show="currentStep === 0">
          <a-form-item label="考試名稱" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-model="name" />
          </a-form-item>
          <a-form-item label="考試限時" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input-number :min="1" :max="200" v-model="elapse" />
            分鐘
          </a-form-item>
          <a-form-item label="考試描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-textarea :rows="2" v-model="desc"></a-textarea>
          </a-form-item>
          <a-form-item label="考試封面圖片" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <p>請點擊列表圖片修改</p>
          </a-form-item>
        </div>
        <div v-show="currentStep === 1">
          <a-form-item label="單選題" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input-number :min="1" :max="20" v-model="radioScore" />
            分
          </a-form-item>
          <a-form-item label="多選題" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input-number :min="1" :max="20" v-model="checkScore" />
            分
          </a-form-item>
          <a-form-item label="是非題" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input-number :min="1" :max="20" v-model="judgeScore" />
            分
          </a-form-item>
        </div>

        <div v-show="currentStep === 2">
          <a-form-item label="選擇單選題" :labelCol="labelCol" :wrapperCol="wrapperCol" enterButton="Search">
            <!-- 單選 -->
            <a-select
              mode="multiple"
              :size="size"
              :default-value="defaultRadios"
              v-if="visible"
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

          <a-form-item label="選擇多選題" :labelCol="labelCol" :wrapperCol="wrapperCol" enterButton="Search">
            <!-- 多選 -->
            <a-select
              mode="multiple"
              :size="size"
              :default-value="defaultChecks"
              v-if="visible"
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

          <a-form-item label="選擇是非題" :labelCol="labelCol" :wrapperCol="wrapperCol" enterButton="Search">
            <!-- 是非 -->
            <a-select
              mode="multiple"
              :size="size"
              :default-value="defaultJudges"
              v-if="visible"
              placeholder="請選擇是非題"
              style="width: 100%"
              @popupScroll="popupScroll"
              @change="handleJudgeChange">
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
import { getExamQuestionTypeList, examUpdate } from '../../../api/exam'

const stepForms = [
  ['name', 'elapse', 'desc', 'avatar'],
  ['radioScore', 'checkScore', 'judgeScore'],
  ['option']
]

export default {
  name: 'ExamEditModal',
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
      exam: {},
      name: '',
      elapse: 0,
      desc: '',
      avatar: '',
      radioScore: 0,
      checkScore: 0,
      judgeScore: 0,
      radios: [],
      checks: [],
      judges: [],
      defaultRadios: [],
      defaultChecks: [],
      defaultJudges: []
    }
  },
  methods: {
    edit (exam) {
      Object.assign(this.exam, exam) // 深度拷贝
      this.visible = true
      // 每次編輯需要先清掉
      this.radios = []
      this.checks = []
      this.judges = []
      this.defaultRadios = []
      this.defaultChecks = []
      this.defaultJudges = []
      this.name = exam.name
      this.elapse = exam.elapse
      this.desc = exam.desc
      this.avatar = exam.avatar
      this.radioScore = exam.radioScore
      this.checkScore = exam.checkScore
      this.judgeScore = exam.judgeScore
      const that = this
      // 打後端拿到題目列表.在編輯時要再點【編輯的時候傳進來】
      getExamQuestionTypeList().then(res => {
        console.log(res)
        if (res.code !== 0) {
          that.$notification.error({
            message: '取得問題列表失敗',
            description: res.msg
          })
        }
        console.log(res.data)
        that.radios = res.data.radios
        that.checks = res.data.checks
        that.judges = res.data.judges
        // 从exam里面的radios、checks、judges捨定下上面的this內的三個屬性，把checked設為true
        for (let i = 0; i < exam.radios.length; i++) {
          that.defaultRadios.push(exam.radios[i].name)
        }
        that.handleRadioChange(that.defaultRadios)
        for (let i = 0; i < exam.checks.length; i++) {
          that.defaultChecks.push(exam.checks[i].name)
        }
        that.handleCheckChange(that.defaultChecks)
        for (let i = 0; i < exam.judges.length; i++) {
          that.defaultJudges.push(exam.judges[i].name)
        }
        that.handleJudgeChange(that.defaultJudges)
      }).catch(err => {
        this.$notification.error({
          message: '取得問題列表失敗',
          description: err.message
        })
      })
    },
    popupScroll () {
      console.log('popupScroll')
    },
    handleNext (step) {
      // 處理下一步or完成
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
      // last step，最後一步，需要點【完成】來新增考試
      this.confirmLoading = true
      console.log('考試設定完成')
      this.confirmLoading = false
      this.exam.name = this.name
      this.exam.elapse = this.elapse
      this.exam.desc = this.desc
      this.exam.avatar = this.avatar
      this.exam.radioScore = this.radioScore
      this.exam.checkScore = this.checkScore
      this.exam.judgeScore = this.judgeScore
      this.exam.radios = this.radios
      this.exam.checks = this.checks
      this.exam.judges = this.judges
      const that = this
      examUpdate(that.exam).then(res => {
        console.log(res)
        if (res.code === 0) {
          that.$notification.success({
            message: '更新成功',
            description: '考試更新成功'
          })
          that.visible = false
          that.currentStep = 0
          that.$emit('ok')
        }
      }).catch(err => {
        that.$notification.error({
          message: '考試更新失敗',
          description: err.message
        })
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
    // 單選
    handleRadioChange (values) {
      console.log(values)
      for (let i = 0; i < this.radios.length; i++) {
        const name = this.radios[i].name
        // flag，是否被選中
        let checked = false
        for (let j = 0; j < values.length; j++) {
          const value = values[j]
          if (name === value) {
            checked = true
            this.radios[i].checked = true
            break
          }
        }
        // 若不是答案，則該選項的answer屬性為false
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
    // 是非題
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
