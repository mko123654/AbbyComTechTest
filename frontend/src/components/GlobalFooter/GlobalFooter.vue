<template>
  <div class="footer">
    <div class="links">
      <a href="https://hackmd.io/@mko123654" target="_blank">HackMD</a>
      <a href="https://github.com/mko123654/AbbyComTechTest" target="_blank">GitHub</a>
      <a href="mailto:mko123654@gmail.com">E-mail</a>
    </div>
    <div class="copyright">
      Copyright
      <a-icon type="copyright" /> 2022 <span>Abby Chang</span> based on: https://github.com/andrewgreat/antd-pro-vite-vue3
    </div>
  </div>
</template>

<script>
export default {
  name: 'GlobalFooter',
  data () {
    return {}
  }
}
</script>

<style lang="less" scoped>
.footer {
  padding: 0 16px;
  margin: 50px 0 50px;
  text-align: center;

  .links {
    margin-bottom: 8px;

    a {
      color: rgba(0, 0, 0, 0.45);

      &:hover {
        color: rgba(0, 0, 0, 0.65);
      }

      &:not(:last-child) {
        margin-right: 40px;
      }
    }
  }
  .copyright {
    color: rgba(0, 0, 0, 0.45);
    font-size: 14px;
  }
}
</style>
