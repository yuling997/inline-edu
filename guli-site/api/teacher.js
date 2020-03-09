import request from '~/utils/request'

const apiPath = '/web/edu/teacher'
export default{

  // 获取讲师列表
  getList() {
    return request({
      url: `${apiPath}/list`,
      method: 'get'
    })
  },

  // 根据ID查询讲师
  getById(id) {
    return request({
      url: `${apiPath}/get/${id}`,
      method: 'get'
    })
  },

  // 讲师的列表页
  getPageList(page, limit) {
    return request({
      url: `${apiPath}/page-list`,
      method: 'get',
      params: {
        page,
        limit
      }
    })
  }
}
