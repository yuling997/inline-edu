import request from '@/utils/request'

const apiPath = '/admin/statistics/daily'

export default {

  createStatistics(day) {
    return request({
      baseURL: 'http://127.0.0.1:8140',
      url: `${apiPath}/create/${day}`,
      method: 'post'
    })
  },

  showChart(searchObj) {
    return request({
      baseURL: 'http://127.0.0.1:8140',
      url: `${apiPath}/show-chart/${searchObj.begin}/${searchObj.end}/${searchObj.type}`,
      method: 'get'
    })
  }
}
