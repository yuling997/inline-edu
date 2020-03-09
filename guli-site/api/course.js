import request from '~/utils/request'
const apiPath = '/web/edu/course'
export default {

  getPageList(searchObj) {
    return request({
      url: `${apiPath}/page-list`,
      method: 'get',
      params: searchObj
    })
  },

  getById(courseId) {
    return request({
      url: `${apiPath}/get/${courseId}`,
      method: 'get'
    })
  }
}
