<template>
  <div class="account-settings-info-view">
    <a-row :gutter="24">
      <a-col :md="24" :lg="16">

        <a-form layout="vertical">
          <a-form-item
            label="暱稱"
          >
            <a-input placeholder="請輸入..." />
          </a-form-item>
          <a-form-item
            label="自我介紹"
          >
            <a-textarea rows="4" placeholder="尚未設定"/>
          </a-form-item>

          <a-form-item
            label="E-mail"
            :required="false"
          >
            <a-input placeholder="mko123654@gmail.com"/>
          </a-form-item>
          <a-form-item
            label="帳號密碼"
            :required="false"
          >
            <a-input placeholder="********"/>
          </a-form-item>

          <a-form-item>
            <a-button type="primary">送出</a-button>
            <a-button style="margin-left: 8px">取消</a-button>
          </a-form-item>
        </a-form>
      </a-col>

    </a-row>
  </div>
</template>

<script>

export default {
  data () {
    return {
      // cropper
      preview: {}
    }
  },
  methods: {

  }
}
</script>
