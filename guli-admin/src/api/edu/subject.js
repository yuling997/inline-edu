import request from '@/utils/request'

// api组件模块中发送Ajax请求
const apiPath = '/admin/edu/subject'
export default {
  getNestedTreeList() {
    return request({
      url: `${apiPath}/nested-list`,
      method: 'get'
    })
  }

}
