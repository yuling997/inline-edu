import request from '@/utils/request'
import cookie from 'js-cookie'
const apiPath = '/web/ucenter/member'

export default {

  submitRegister(formItem) {
    return request({
      baseURL: 'http://localhost:8150',
      url: `${apiPath}/register`,
      method: 'post',
      data: formItem
    })
  },

  submitLogin(userInfo) {
    return request({
      baseURL: 'http://localhost:8150',
      url: `${apiPath}/login`,
      method: 'post',
      data: userInfo
    })
  },

  getLoginInfo() {
    return request({
      baseURL: 'http://localhost:8150',
      url: `${apiPath}/auth/get-login-info`,
      method: 'get',
      // 注意：这里通过header传递token信息，未来放入request拦截器
      headers: { 'token': cookie.get('guli_token') }
    })
  }
}
