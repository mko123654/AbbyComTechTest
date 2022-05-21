<script>
export default {
  name: 'RouteView',
  props: {
    keepAlive: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {}
  },
  render () {
    const { $route: { meta }, $store: { getters } } = this
    const inKeep = (
      <keep-alive>
        <router-view />
      </keep-alive>
    )
    const notKeep = (
      <router-view />
    )
    // 這邊加了 multiTab 的判斷式，當開啟 multiTab 時
    // 應該要全部的元件都keepAlive住，否則會出現切換頁面後還原成原始狀態的BUG
    // 如果都不需要時，可改為 return meta.keepAlive ? inKeep : notKeep
    if (!getters.multiTab && meta.keepAlive === false) {
      return notKeep
    }
    return this.keepAlive || getters.multiTab || meta.keepAlive ? inKeep : notKeep
  }
}
</script>
