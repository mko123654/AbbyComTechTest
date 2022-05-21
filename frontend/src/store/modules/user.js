import Vue from 'vue'
import { login, getInfo, logout } from '../../api/login'
import { ACCESS_TOKEN } from '../../store/mutation-types'
import { welcome } from '../../utils/util'

const user = {
  state: {
    token: '',
    name: '',
    welcome: '',
    avatar: '',
    roles: [],
    info: {}
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, { name, welcome }) => {
      state.name = name
      state.welcome = welcome
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_INFO: (state, info) => {
      state.info = info
    }
  },

  actions: {
    // 登入
    Login ({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        login(userInfo).then(response => {
          if (response.code === 0) {
            const token = response.data
            // 把API回傳的token字串值設定到localStorage的token內，有效期是1天
            // Vue.ls中的ls是localStorage的簡寫
            Vue.ls.set(ACCESS_TOKEN, token, 24 * 60 * 60 * 1000)
            // 將Token丟進全域的store裡
            commit('SET_TOKEN', token)
            resolve()
          } else {
            reject(new Error('帳號或密碼錯誤'))
          }
        }).catch(error => {
          console.log(error)
          reject(error)
        })
      })
    },

    // get user info
    GetInfo ({ commit }) {
      return new Promise((resolve, reject) => {
        getInfo().then(response => {
          console.log('/user/info的response如下：')
          console.log(response)
          const result = response.data

          if (result.role && result.role.permissions.length > 0) {
            const role = result.role
            role.permissions = result.role.permissions // permissions是給頁面行為設定權限
            role.permissions.map(per => {
              if (per.actionEntitySet != null && per.actionEntitySet.length > 0) {
                const action = per.actionEntitySet.map(action => {
                  return action.action
                })
                per.actionList = action
              }
            })
            role.permissionList = role.permissions.map(permission => { // permissionList是由permissions中forEach解析出來的
              return permission.permissionId
            })

            commit('SET_ROLES', result.role) // 在store中設定帳號權限
            commit('SET_INFO', result) // 在store中設定帳號資訊
          } else {
            reject(new Error('getInfo: roles must be a non-null array !'))
          }
          commit('SET_NAME', { name: result.name, welcome: welcome() }) // 設定帳號名
          commit('SET_AVATAR', result.avatar) // 設定帳號頭像

          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    Logout ({ commit, state }) {
      return new Promise((resolve) => {
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        Vue.ls.remove(ACCESS_TOKEN)

        logout(state.token).then(() => {
          resolve()
        }).catch(() => {
          resolve()
        })
      })
    }

  }
}

export default user
