import axios from 'axios'
import { Message } from 'element-ui'
import cookie from 'js-cookie'

// 创建axios实例
const service = axios.create({
  baseURL: 'http://localhost:9120',
  timeout: 5000 // 请求超时时间
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // debugger
    if (cookie.get('guli_token')) { // 如果cookie中包含guli_token
      // 则发送后端api请求的时候携带token
      config.headers['token'] = cookie.get('guli_token')
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    /**
     * code为非20000是抛错 可结合自己业务进行修改
     */
    const res = response.data
    if (res.code === 20000) { // 成功
      return response
    } else if (res.code === 25000) { // 支付中
      return response
    } else if (res.code === 28004) { // 需要登录
      window.location.href = '/login'
      return
    } else {
      Message({
        message: res.message,
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject('error')
    }
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
