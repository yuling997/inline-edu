import request from '@/utils/request'
import cookie from 'js-cookie'

const apiPath = '/web/order/auth'

export default {
  createOrder(courseId) {
    return request({
      baseURL: 'http://localhost:8170',
      url: `${apiPath}/save/${courseId}`,
      method: 'post',
      // 注意：这里通过header传递token信息，未来放入request拦截器
      headers: { 'token': cookie.get('guli_token') }
    })
  },

  isBuy(courseId) {
    return request({
      baseURL: 'http://localhost:8170',
      url: `${apiPath}/is-buy/${courseId}`,
      method: 'get',
      // 注意：这里通过header传递token信息，未来放入request拦截器
      headers: { 'token': cookie.get('guli_token') }
    })
  },

  getById(orderId) {
    return request({
      baseURL: 'http://localhost:8170',
      url: `${apiPath}/get/${orderId}`,
      method: 'get',
      // 注意：这里通过header传递token信息，未来放入request拦截器
      headers: { 'token': cookie.get('guli_token') }
    })
  }

}
