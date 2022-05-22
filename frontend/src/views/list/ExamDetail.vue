<template>
  <a-layout>
    <a-layout-header class="header" style="color: #fff">
      <span style="font-size:25px;margin-left: 0px;" v-if="examDetail.exam">
        <a-avatar slot="avatar" size="large" shape="circle" :src="examDetail.exam.examAvatar | imgSrcFilter"/>
        {{ examDetail.exam.examName }}
        <span style="font-size:15px;">{{ examDetail.exam.examDescription }} </span>
      </span>
      <span style="float: right;">
        <span style="margin-right: 60px; font-size: 20px" v-if="examDetail.exam">考試限時：{{ examDetail.exam.examTimeLimit }}分鐘</span>
        <a-button type="danger" ghost style="margin-right: 60px;" @click="finishExam()">交券</a-button>
        <a-avatar class="avatar" size="small" :src="avatar()"/>
        <span style="margin-left: 12px">{{ nickname() }}</span>
      </span>
    </a-layout-header>
    <a-layout>
      <a-layout-sider width="190" :style="{background: '#444',overflow: 'auto', height: '100vh', position: 'fixed', left: 0 }">
        <a-menu
          mode="inline"
          :defaultSelectedKeys="['1']"
          :defaultOpenKeys="['question_radio', 'question_check', 'question_judge']"
          :style="{ height: '100%', borderRight: 0 }"
        >
          <a-sub-menu key="question_radio">
            <span slot="title" v-if="examDetail.exam"><a-icon type="check-circle" theme="twoTone"/>單選題(每題{{ examDetail.exam.examScoreRadio }}分)</span>
            <a-menu-item v-for="(item, index) in examDetail.radioIds" :key="item" @click="getQuestionDetail(item)">
              <a-icon type="eye" theme="twoTone" twoToneColor="#52c41a" v-if="answersMap.get(item)"/>
              題目{{ index + 1 }}
            </a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="question_check">
            <span slot="title" v-if="examDetail.exam"><a-icon type="check-square" theme="twoTone"/>多選題(每題{{ examDetail.exam.examScoreCheck }}分)</span>
            <a-menu-item v-for="(item, index) in examDetail.checkIds" :key="item" @click="getQuestionDetail(item)">
              <a-icon type="eye" theme="twoTone" twoToneColor="#52c41a" v-if="answersMap.get(item)"/>
              題目{{ index + 1 }}
            </a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="question_judge">
            <span slot="title" v-if="examDetail.exam"><a-icon type="like" theme="twoTone"/>是非題(每題{{ examDetail.exam.examScoreJudge }}分)</span>
            <a-menu-item v-for="(item, index) in examDetail.judgeIds" :key="item" @click="getQuestionDetail(item)">
              <a-icon type="eye" theme="twoTone" twoToneColor="#52c41a" v-if="answersMap.get(item)"/>
              題目{{ index + 1 }}
            </a-menu-item>
          </a-sub-menu>
        </a-menu>
      </a-layout-sider>
      <a-layout :style="{ marginLeft: '200px' }">
        <a-layout-content :style="{ margin: '24px 16px 0',height: '84vh', overflow: 'initial' }">
          <div :style="{ padding: '24px', background: '#fff',height: '84vh'}">
            <span v-show="currentQuestion === ''" style="font-size: 30px;font-family: Consolas">歡銀參加考試，請點擊左側題目編號開始作答</span>
            <strong>{{ currentQuestion.type }} </strong> <p v-html="currentQuestion.name"></p>
            <!-- 單選 是非 -->
            <a-radio-group @change="onRadioChange" v-model="radioValue" v-if="currentQuestion.type === '單選題' || currentQuestion.type === '是非題'">
              <a-radio v-for="option in currentQuestion.options" :key="option.questionOptionId" :style="optionStyle" :value="option.questionOptionId">
                {{ option.questionOptionContent }}
              </a-radio>
            </a-radio-group>

            <!-- 多選 -->
            <a-checkbox-group @change="onCheckChange" v-model="checkValues" v-if="currentQuestion.type === '多選題'">
              <a-checkbox v-for="option in currentQuestion.options" :key="option.questionOptionId" :style="optionStyle" :value="option.questionOptionId">
                {{ option.questionOptionContent }}
              </a-checkbox>
            </a-checkbox-group>
          </div>
        </a-layout-content>
        <a-layout-footer :style="{ textAlign: 'center' }">
          Abby.com Tech Test ©2022 Crated by Abby Chang
        </a-layout-footer>
      </a-layout>
    </a-layout>
  </a-layout>
</template>

<script>
import { getExamDetail, getQuestionDetail, finishExam } from '../../api/exam'
import UserMenu from '../../components/tools/UserMenu'
import { mapGetters } from 'vuex'

export default {
  name: 'ExamDetail',
  components: {
    UserMenu
  },
  data () {
    return {
      examDetail: {},
      answersMap: {},
      currentQuestion: '',
      radioValue: '',
      checkValues: [],
      optionStyle: {
        display: 'block',
        height: '30px',
        lineHeight: '30px',
        marginLeft: '0px'
      }
    }
  },
  mounted () {
    this.answersMap = new Map()
    const that = this
    getExamDetail(this.$route.params.id)
      .then(res => {
        if (res.code === 0) {
          that.examDetail = res.data
          return res.data
        } else {
          this.$notification.error({
            message: '取得考試詳情失敗',
            description: res.msg
          })
        }
      })
  },
  methods: {
    ...mapGetters(['nickname', 'avatar']),
    getQuestionDetail (questionId) {
      const that = this
      this.radioValue = ''
      this.checkValues = []
      getQuestionDetail(questionId)
        .then(res => {
          if (res.code === 0) {
            that.currentQuestion = res.data
            if (that.answersMap.get(that.currentQuestion.id)) {
              if (that.currentQuestion.type === '單選題' || that.currentQuestion.type === '是非題') {
                that.radioValue = that.answersMap.get(that.currentQuestion.id)[0]
              } else if (that.currentQuestion.type === '多選題') {
                // 需要深層拷貝
                Object.assign(that.checkValues, that.answersMap.get(that.currentQuestion.id))
              }
            }
            return res.data
          } else {
            this.$notification.error({
              message: '取得考題詳情失敗',
              description: res.msg
            })
          }
        })
    },

    onRadioChange (e) {
      const userOptions = []
      userOptions.push(e.target.value)
      this.answersMap.set(this.currentQuestion.id, userOptions)
    },

    onCheckChange (checkedValues) {
      this.answersMap.set(this.currentQuestion.id, checkedValues)
    },
    _strMapToObj (strMap) {
      const obj = Object.create(null)
      for (const [k, v] of strMap) {
        obj[k] = v
      }
      return obj
    },
    /**
     *map轉換為json
     */
    _mapToJson (map) {
      return JSON.stringify(this._strMapToObj(map))
    },
    /**
     * 结束考试并交卷
     */
    finishExam () {
      // Todo:打後端提交answersMap
      finishExam(this.$route.params.id, this._mapToJson(this.answersMap))
        .then(res => {
          if (res.code === 0) {
            this.$notification.success({
              message: '考券提交成功'
            })
            this.$router.push('/list/exam-record-list')
            return res.data
          } else {
            this.$notification.error({
              message: '考券提交失敗...',
              description: res.msg
            })
          }
        })
    }
  }
}
</script>

<style scoped>

</style>
