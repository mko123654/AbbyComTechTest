<template>
  <div class="main user-layout-register">
    <h3><span>註冊帳號</span></h3>
    <a-form ref="formRegister" :form="form" id="formRegister">
      <a-form-item>
        <a-input
          size="large"
          type="text"
          placeholder="E-mail"
          v-decorator="['email', {rules: [{ required: true, type: 'email', message: '請輸入E-mail' }], validateTrigger: ['change', 'blur']}]"
        ></a-input>
      </a-form-item>

      <a-popover placement="rightTop" trigger="click" :visible="state.passwordLevelChecked">
        <template slot="content">
          <div :style="{ width: '240px' }">
            <div :class="['user-register', passwordLevelClass]">强度：<span>{{ passwordLevelName }}</span></div>
            <a-progress :percent="state.percent" :showInfo="false" :strokeColor=" passwordLevelColor "/>
            <div style="margin-top: 10px;">
              <span>至少輸入 6 個英文字母+數字</span>
            </div>
          </div>
        </template>
        <a-form-item>
          <a-input
            size="large"
            type="password"
            @click="handlePasswordInputClick"
            autocomplete="false"
            placeholder="至少輸入 6 個英文字母+數字"
            v-decorator="['password', {rules: [{ required: true, message: '至少輸入 6 個英文字母+數字'}, { validator: this.handlePasswordLevel }], validateTrigger: ['change', 'blur']}]"
          ></a-input>
        </a-form-item>
      </a-popover>

      <a-form-item>
        <a-input
          size="large"
          type="password"
          autocomplete="false"
          placeholder="確認密碼"
          v-decorator="['password2', {rules: [{ required: true, message: '至少輸入 6 個英文字母+數字' }, { validator: this.handlePasswordCheck }], validateTrigger: ['change', 'blur']}]"
        ></a-input>
      </a-form-item>

      <a-form-item>
        <a-input
          size="large"
          placeholder="11 位手机号"
          v-decorator="['mobile', {rules: [{ required: true, message: '請輸入正確的手機號碼', pattern: /[0-9]{8}$/ }, { validator: this.handlePhoneCheck } ], validateTrigger: ['change', 'blur'] }]"
        >
          <a-select slot="addonBefore" size="large" defaultValue="+886">
            <a-select-option value="+886">+886</a-select-option>
            <a-select-option value="+000">其他國碼...</a-select-option>
          </a-select>
        </a-input>
      </a-form-item>
      <a-row :gutter="16">
        <a-col class="gutter-row" :span="16">
          <a-form-item>
            <a-input
              size="large"
              type="text"
              placeholder="驗證碼"
              v-decorator="['captcha', {rules: [{ required: true, message: '請輸入驗證碼' }], validateTrigger: 'blur'}]"
            >
              <a-icon slot="prefix" type="mail" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input>
          </a-form-item>
        </a-col>
        <a-col class="gutter-row" :span="8">
          <a-button
            class="getCaptcha"
            size="large"
            :disabled="state.smsSendBtn"
            @click.stop.prevent="getCaptcha"
            v-text="!state.smsSendBtn && '取得驗證碼'||(state.time+' s')"></a-button>
        </a-col>
      </a-row>

      <a-form-item>
        <a-button
          size="large"
          type="primary"
          htmlType="submit"
          class="register-button"
          :loading="registerBtn"
          @click.stop.prevent="handleSubmit"
          :disabled="registerBtn">註冊帳號
        </a-button>
        <router-link class="login" :to="{ name: 'login' }">使用現有帳號登入</router-link>
      </a-form-item>

    </a-form>
  </div>
</template>

<script>
import { mixinDevice } from '../../utils/mixin.js'
import { getSmsCaptcha } from '../../api/login'
import { register } from '../../api/user'

const levelNames = {
  0: '低',
  1: '低',
  2: '中',
  3: '高'
}
const levelClass = {
  0: 'error',
  1: 'error',
  2: 'warning',
  3: 'success'
}
const levelColor = {
  0: '#ff0000',
  1: '#ff0000',
  2: '#ff7e05',
  3: '#52c41a'
}
export default {
  name: 'Register',
  components: {},
  mixins: [mixinDevice],
  data () {
    return {
      form: this.$form.createForm(this),

      state: {
        time: 60,
        smsSendBtn: false,
        passwordLevel: 0,
        passwordLevelChecked: false,
        percent: 10,
        progressColor: '#FF0000'
      },
      registerBtn: false
    }
  },
  computed: {
    passwordLevelClass () {
      return levelClass[this.state.passwordLevel]
    },
    passwordLevelName () {
      return levelNames[this.state.passwordLevel]
    },
    passwordLevelColor () {
      return levelColor[this.state.passwordLevel]
    }
  },
  methods: {

    handlePasswordLevel (rule, value, callback) {
      let level = 0

      // 有無數字
      if (/[0-9]/.test(value)) {
        level++
      }
      // 有無字母
      if (/[a-zA-Z]/.test(value)) {
        level++
      }
      // 有無特殊符號
      if (/[^0-9a-zA-Z_]/.test(value)) {
        level++
      }
      this.state.passwordLevel = level
      this.state.percent = level * 30
      if (level >= 2) {
        if (level >= 3) {
          this.state.percent = 100
        }
        callback()
      } else {
        if (level === 0) {
          this.state.percent = 10
        }
        callback(new Error('密碼強度不夠...'))
      }
    },

    handlePasswordCheck (rule, value, callback) {
      const password = this.form.getFieldValue('password')
      console.log('value', value)
      if (value === undefined) {
        callback(new Error('請輸入密碼'))
      }
      if (value && password && value.trim() !== password.trim()) {
        callback(new Error('輸入密碼不一致'))
      }
      callback()
    },

    handlePhoneCheck (rule, value, callback) {
      console.log('handlePhoneCheck, rule:', rule)
      console.log('handlePhoneCheck, value', value)
      console.log('handlePhoneCheck, callback', callback)

      callback()
    },

    handlePasswordInputClick () {
      if (!this.isMobile()) {
        this.state.passwordLevelChecked = true
        return
      }
      this.state.passwordLevelChecked = false
    },

    handleSubmit () {
      const { form: { validateFields }, $router, $message } = this
      validateFields({ force: true }, (err, values) => {
        if (!err) {
          // e.g. params：{email: "mko123654@gmail.com", password: "123456", password2: "123456", mobile: "0933313793", captcha: "010203"}
          register(values).then(res => {
            console.log(res)
            $router.push({ name: 'registerResult', params: { ...values } })
          }).catch(err => {
            $message.error(`load user err: ${err.message}`)
          })
        }
      })
    },

    getCaptcha (e) {
      e.preventDefault()
      const { form: { validateFields }, state, $message, $notification } = this

      validateFields(['mobile'], { force: true },
        (err, values) => {
          if (!err) {
            state.smsSendBtn = true

            const interval = window.setInterval(() => {
              if (state.time-- <= 0) {
                state.time = 60
                state.smsSendBtn = false
                window.clearInterval(interval)
              }
            }, 1000)

            const hide = $message.loading('驗證碼發送中...', 0)

            getSmsCaptcha({ mobile: values.mobile }).then(res => {
              setTimeout(hide, 2500)
              $notification['success']({
                message: '提示',
                description: '驗證碼取得成功，您的驗證碼為：' + res.result.captcha,
                duration: 8
              })
            }).catch(err => {
              setTimeout(hide, 1)
              clearInterval(interval)
              state.time = 60
              state.smsSendBtn = false
              this.requestFailed(err)
            })
          }
        }
      )
    },
    requestFailed (err) {
      this.$notification['error']({
        message: '錯誤',
        description: ((err.response || {}).data || {}).message || '發生錯誤，請稍後再試',
        duration: 4
      })
      this.registerBtn = false
    }
  },
  watch: {
    'state.passwordLevel' (val) {
      console.log(val)
    }
  }
}
</script>
<style lang="less">
  .user-register {

    &.error {
      color: #ff0000;
    }

    &.warning {
      color: #ff7e05;
    }

    &.success {
      color: #52c41a;
    }

  }

  .user-layout-register {
    .ant-input-group-addon:first-child {
      background-color: #fff;
    }
  }
</style>
<style lang="less" scoped>
  .user-layout-register {

    & > h3 {
      font-size: 16px;
      margin-bottom: 20px;
    }

    .getCaptcha {
      display: block;
      width: 100%;
      height: 40px;
    }

    .register-button {
      width: 50%;
    }

    .login {
      float: right;
      line-height: 40px;
    }
  }
</style>
