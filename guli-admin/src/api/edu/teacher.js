import request from '@/utils/request'

// api组件模块中发送Ajax请求
const apiPath = '/admin/edu/teacher'
export default {
  list() {
    return request({
      url: `${apiPath}/list`,
      method: 'get'
    })
  },

  getPageList(page, limit, searchObj) {
    return request({
      url: `${apiPath}/list/${page}/${limit}`,
      method: 'get',
      params: searchObj // url查询字符串提交，通过请求头提交，和请求体没有关系，所以可以是get也可以是post
    })
  },

  removeById(id) {
    return request({
      url: `${apiPath}/remove/${id}`,
      method: 'delete'
    })
  },

  save(teacher) {
    return request({
      url: `${apiPath}/save`,
      method: 'post',
      data: teacher // 提交的是json数据，通过请求体，如果使用data，一定使用post方式提交
    })
  },

  getById(id) {
    return request({
      url: `${apiPath}/get/${id}`,
      method: 'get'
    })
  },

  updateById(teacher) { // teacher对象中必须封装了id值
    return request({
      url: `${apiPath}/update`,
      method: 'put',
      data: teacher
    })
  },

  //   批量删除
  batchRemove(idList) {
    return request({
      url: `${apiPath}/batch-remove`,
      method: 'delete',
      data: idList
    })
  },

  selectNameListByKey(key) {
    return request({
      url: `${apiPath}/list/name/${key}`,
      method: 'get'
    })
  },

  getList() {
    return request({
      url: `${apiPath}/list`,
      method: 'get'
    })
  }

}
