import request from '@/utils/request'

const apiPath = '/admin/edu/chapter'

export default {

  // 获取嵌套数据章节列表
  getNestedTreeList(courseId) {
    return request({
      url: `${apiPath}/nested-list/${courseId}`,
      method: 'get'
    })
  },

  // 根据id删除章节
  removeById(id) {
    return request({
      url: `${apiPath}/remove/${id}`,
      method: 'delete'
    })
  },

  // 新增章节
  save(chapter) {
    return request({
      url: `${apiPath}/save`,
      method: 'post',
      data: chapter
    })
  },

  // 根据id查询章节
  getById(id) {
    return request({
      url: `${apiPath}/get/${id}`,
      method: 'get'
    })
  },

  // 根据id修改章节
  updateById(chapter) {
    return request({
      url: `${apiPath}/update`,
      method: 'put',
      data: chapter
    })
  }
}
