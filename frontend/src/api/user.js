import api from './index'
import { axios } from '../utils/request'

export function login (parameter) {
  return axios({
    url: api.UserLogin,
    method: 'post',
    data: parameter
  })
}

export function register (parameter) {
  return axios({
    url: api.UserRegister,
    method: 'post',
    data: parameter
  })
}
