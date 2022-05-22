<template>
  <a-layout>
    <a-layout-header class="header" style="color: #fff">
      <span style="font-size:25px;margin-left: 0px;" v-if="examDetail.exam">
        <a-avatar slot="avatar" size="large" shape="circle" :src="examDetail.exam.examAvatar | imgSrcFilter"/>
        {{ examDetail.exam.examName }}
        <span style="font-size:15px;">{{ examDetail.exam.examDescription }} </span>
      </span>
      <span style="float: right;">
        <span style="margin-right: 40px; font-size: 20px" v-if="recordDetail.examRecord">
          考試得分：<span style="color: red">{{ recordDetail.examRecord.examJoinScore }}</span>&nbsp;分&nbsp;
          <span style="font-size:15px;">參加考試時間：{{ recordDetail.examRecord.examJoinDate }}</span>
        </span>
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
              <a-icon type="check" v-if="resultsMap.get(item)==='True'"/>
              <a-icon type="close" v-if="resultsMap.get(item)==='False'"/>
              題目{{ index + 1 }}
            </a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="question_check">
            <span slot="title" v-if="examDetail.exam"><a-icon type="check-square" theme="twoTone"/>多選題(每題{{ examDetail.exam.examScoreCheck }}分)</span>
            <a-menu-item v-for="(item, index) in examDetail.checkIds" :key="item" @click="getQuestionDetail(item)">
              <a-icon type="check" v-if="resultsMap.get(item)==='True'"/>
              <a-icon type="close" v-if="resultsMap.get(item)==='False'"/>
              題目{{ index + 1 }}
            </a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="question_judge">
            <span slot="title" v-if="examDetail.exam"><a-icon type="like" theme="twoTone"/>是非題(每題{{ examDetail.exam.examScoreJudge }}分)</span>
            <a-menu-item v-for="(item, index) in examDetail.judgeIds" :key="item" @click="getQuestionDetail(item)">
              <a-icon type="check" v-if="resultsMap.get(item)==='True'"/>
              <a-icon type="close" v-if="resultsMap.get(item)==='False'"/>
              題目{{ index + 1 }}
            </a-menu-item>
          </a-sub-menu>
        </a-menu>
      </a-layout-sider>
      <a-layout :style="{ marginLeft: '200px' }">
        <a-layout-content :style="{ margin: '24px 16px 0',height: '84vh', overflow: 'initial' }">
          <div :style="{ padding: '24px', background: '#fff',height: '84vh'}">
            <span v-if="currentQuestion === ''" style="font-size: 30px;font-family: Consolas">歡迎查看本次考試情况，點擊左測題目編號可以查看答題詳情</span>
            <span v-if="currentQuestion !== ''">
              <strong>{{ currentQuestion.type }} </strong> <p v-html="currentQuestion.name"></p>
              <strong style="color: green;" v-if="questionRight">答對！</strong>
              <strong style="color: red;" v-if="!questionRight">答錯！</strong>
            </span>
            <br><br>
            <a-radio-group v-model="radioValue" v-if="currentQuestion.type === '單選題' || currentQuestion.type === '是非題'">
              <a-radio v-for="option in currentQuestion.options" :key="option.questionOptionId" :style="optionStyle" :value="option.questionOptionId">
                {{ option.questionOptionContent }}
              </a-radio>
            </a-radio-group>

            <!-- 答錯時顯示 -->
            <div v-if="!questionRight && currentQuestion!=='' && (currentQuestion.type === '單選題' || currentQuestion.type === '是非題')">
              <span style="color: red;"><br/>正確答案為：<br/></span>
              <a-radio-group v-model="radioRightValue">
                <a-radio v-for="option in currentQuestion.options" :key="option.questionOptionId" :style="optionStyle" :value="option.questionOptionId">
                  {{ option.questionOptionContent }}
                </a-radio>
              </a-radio-group>
            </div>

            <!-- 多選 -->
            <a-checkbox-group v-model="checkValues" v-if="currentQuestion.type === '多選題'">
              <a-checkbox v-for="option in currentQuestion.options" :key="option.questionOptionId" :style="optionStyle" :value="option.questionOptionId">
                {{ option.questionOptionContent }}
              </a-checkbox>
            </a-checkbox-group>

            <!-- 答錯時顯示 -->
            <div v-if="!questionRight && currentQuestion!=='' && currentQuestion.type === '多選題'">
              <span style="color: red;"><br/>正確答案為：<br/></span>
              <a-checkbox-group v-model="checkRightValues">
                <a-checkbox v-for="option in currentQuestion.options" :key="option.questionOptionId" :style="optionStyle" :value="option.questionOptionId">
                  {{ option.questionOptionContent }}
                </a-checkbox>
              </a-checkbox-group>
            </div>

            <span style="color: red;"><br/>答案解析：：<br/></span>
            <p v-html="currentQuestion.description"></p>
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
import { getExamDetail, getQuestionDetail, getExamRecordDetail } from '../../api/exam'
import UserMenu from '../../components/tools/UserMenu'
import { mapGetters } from 'vuex'

export default {
  name: 'ExamRecordDetail',
  components: {
    UserMenu
  },
  data () {
    return {
      // 考试详情对象
      examDetail: {},
      // 考试记录详情对象
      recordDetail: {},
      // 用户做过的问题都放到这个数组中，键为问题id, 值为currentQuestion(其属性answers属性用于存放答案选项地id或ids),，用于存放用户勾选的答案
      answersMap: {},
      // 题目的正确答案
      answersRightMap: {},
      // 题目的作答结果(正确或错误)
      resultsMap: {},
      // 当前用户的问题
      currentQuestion: '',
      // 单选或判断题的绑定值，用于从answersMap中初始化做题状态
      radioValue: '',
      // 单选题的正确答案，用于从answersRightMap中初始化做题状态
      radioRightValue: '',
      // 多选题的绑定值，用于从answersMap中初始化做题状态
      checkValues: [],
      // 多选题的绑定值，用于从answersRightMap中初始化做题状态
      checkRightValues: [],
      optionStyle: {
        display: 'block',
        height: '30px',
        lineHeight: '30px',
        marginLeft: '0px'
      }
    }
  },
  computed: {
    /**
     * 当前题目用户是否作答正确
     * */
    questionRight () {
      return this.resultsMap !== '' && this.resultsMap.get(this.currentQuestion.id) === 'True'
    }
  },
  mounted () {
    this.answersMap = new Map()
    this.answersRightMap = new Map()
    this.resultsMap = new Map()
    const that = this
    // 从后端获取考试的详细信息，渲染到考试详情里,需要加个延时，要不拿不到参数
    getExamDetail(this.$route.params.exam_id)
      .then(res => {
        if (res.code === 0) {
          // 赋值考试对象
          that.examDetail = res.data
          return res.data
        } else {
          this.$notification.error({
            message: '获取考试详情失败',
            description: res.msg
          })
        }
      })
    // 查看考试记录详情，渲染到前端界面
    getExamRecordDetail(this.$route.params.record_id)
      .then(res => {
        if (res.code === 0) {
          console.log(res.data)
          // 赋值考试对象
          that.recordDetail = res.data
          // 赋值用户的作答答案
          that.objToMap()
          return res.data
        } else {
          this.$notification.error({
            message: '获取考试记录详情失败',
            description: res.msg
          })
        }
      })
  },
  methods: {
    // 从全局变量中获取用户昵称和头像,
    ...mapGetters(['nickname', 'avatar']),
    /**
     * 把后端传过来的对象Object转换成Map
     **/
    objToMap () {
      for (const item in this.recordDetail.answersMap) {
        this.answersMap.set(item, this.recordDetail.answersMap[item])
      }

      for (const item in this.recordDetail.answersRightMap) {
        this.answersRightMap.set(item, this.recordDetail.answersRightMap[item])
      }

      for (const item in this.recordDetail.resultsMap) {
        this.resultsMap.set(item, this.recordDetail.resultsMap[item])
      }
    },
    getQuestionDetail (questionId) {
      // 问题切换时从后端拿到问题详情，渲染到前端content中
      const that = this
      // 清空问题绑定的值
      this.radioValue = ''
      this.radioRightValue = ''
      this.checkValues = []
      this.checkRightValues = []
      getQuestionDetail(questionId)
        .then(res => {
          if (res.code === 0) {
            // 赋值当前考试对象
            that.currentQuestion = res.data
            // 查看用户是不是已经做过这道题又切换回来的，answersMap中查找，能找到这个题目id对应的值数组不为空说明用户做过这道题
            if (that.answersMap.get(that.currentQuestion.id)) {
              // 说明之前做过这道题了
              if (that.currentQuestion.type === '单选题' || that.currentQuestion.type === '判断题') {
                that.radioValue = that.answersMap.get(that.currentQuestion.id)[0]
                that.radioRightValue = that.answersRightMap.get(that.currentQuestion.id)[0]
              } else if (that.currentQuestion.type === '多选题') {
                // 数组是引用类型，因此需要进行拷贝，千万不要直接赋值
                Object.assign(that.checkValues, that.answersMap.get(that.currentQuestion.id))
                Object.assign(that.checkRightValues, that.answersRightMap.get(that.currentQuestion.id))
              }
            }
            return res.data
          } else {
            this.$notification.error({
              message: '获取问题详情失败',
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
