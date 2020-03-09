import request from '@/utils/request'

const apiPath = '/admin/edu/course'

export default {
  // 保存课程
  saveCourseInfo(courseInfo) {
    return request({
      url: `${apiPath}/save-course-info`,
      method: 'post',
      data: courseInfo
    })
  },
  // 根据id查询课程
  getCourseInfoById(id) {
    return request({
      url: `${apiPath}/course-info/${id}`,
      method: 'get'
    })
  },

  // 更新课程
  updateCourseInfoById(courseInfo) {
    return request({
      url: `${apiPath}/update-course-info`,
      method: 'put',
      data: courseInfo
    })
  },

  // 分页课程列表
  getPageList(page, limit, searchObj) {
    return request({
      url: `${apiPath}/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },

  // 删除课程
  removeById(id) {
    return request({
      url: `${apiPath}/${id}`,
      method: 'delete'
    })
  },

  // 根据ID获取课程发布信息
  getCoursePublishInfoById(id) {
    return request({
      url: `${apiPath}/course-publish-info/${id}`,
      method: 'get'
    })
  },

  // 根据id发布课程
  publishCourse(id) {
    return request({
      url: `${apiPath}/publish-course/${id}`,
      method: 'put'
    })
  }
}
