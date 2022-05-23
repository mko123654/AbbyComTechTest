const api = {
  Login: '/auth/login',
  Logout: '/auth/logout',
  ForgePassword: '/auth/forge-password',
  Register: '/auth/register',
  twoStepCode: '/auth/2step-code',
  SendSms: '/account/sms',
  SendSmsErr: '/account/sms_err',
  // get my info
  UserInfo: '/user/info',
  UserRegister: '/user/register',
  UserLogin: '/user/login',
  ExamQuestionList: '/exam/question/list',
  ExamQuestionAll: '/exam/question/all',
  ExamQuestionUpdate: '/exam/question/update',
  ExamQuestionSelection: '/exam/question/selection',
  ExamQuestionCreate: '/exam/question/create',
  ExamList: '/exam/list',
  ExamAll: '/exam/all',
  // 依題型
  ExamQuestionTypeList: '/exam/question/type/list',
  ExamCreate: '/exam/create',
  ExamUpdate: '/exam/update',
  ExamCardList: '/exam/card/list',
  ExamDetail: '/exam/detail/',
  QuestionDetail: '/exam/question/detail/',
  FinishExam: '/exam/finish/',
  ExamRecordList: '/exam/record/list',
  ExamRecordListAll: '/exam/record/list/all',
  recordDetail: '/exam/record/detail/'
}
export default api
