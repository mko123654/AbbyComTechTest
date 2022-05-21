import Vue from 'vue'
import store from '../../store'

/**
 * Action 權限指令
 * 指令用法：
 *  - 在需要控制 action 級別權限的元件上使用 v-action:[method] , 如下：
 *    <i-button v-action:add >新增用戶</a-button>
 *    <a-button v-action:delete>删除用户</a-button>
 *    <a v-action:edit @click="edit(record)">修改</a>
 *
 *  - 若用戶沒有權限時，元件上使用了該指令則會被隱藏
 *  - 當後台權限跟 pro 提供的模式不同時，只要針對這裡的權限過濾修改即可
 *
 *  @see https://github.com/sendya/ant-design-pro-vue/pull/53
 */
const action = Vue.directive('action', {
  inserted: function (el, binding, vnode) {
    const actionName = binding.arg
    const roles = store.getters.roles
    const elVal = vnode.context.$route.meta.permission
    const permissionId = elVal instanceof String && [elVal] || elVal
    roles.permissions.forEach(p => {
      if (!permissionId.includes(p.permissionId)) {
        return
      }
      if (p.actionList && !p.actionList.includes(actionName)) {
        el.parentNode && el.parentNode.removeChild(el) || (el.style.display = 'none')
      }
    })
  }
})

export default action
