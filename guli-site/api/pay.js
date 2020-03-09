import request from '@/utils/request'
const apiPath = '/web/weixin-pay'
export default {
  createNative(orderNo) {
    return request({
      baseURL: 'http://localhost:8170',
      url: `${apiPath}/create-native/${orderNo}`,
      method: 'get'
    })
  },

  queryPayStatus(out_trade_no) {
    return request({
      url: `${apiPath}/queryPayStatus/${out_trade_no}`,
      method: 'get'
    })
  }

}
