import request from '@/utils/request'

const apiPath = '/admin/edu/video'

export default {

  // 新增课时信息
  save(videoInfo) {
    return request({
      url: `${apiPath}/save`,
      method: 'post',
      data: videoInfo
    })
  },

  // 根据ID查询课时信息
  getById(id) {
    return request({
      url: `${apiPath}/get/${id}`,
      method: 'get'
    })
  },

  // 根据ID修改课时信息
  updateById(videoInfo) {
    return request({
      url: `${apiPath}/update`,
      method: 'put',
      data: videoInfo
    })
  },

  // 根据ID删除课时信息
  removeById(id) {
    return request({
      url: `${apiPath}/remove/${id}`,
      method: 'delete'
    })
  }

}
