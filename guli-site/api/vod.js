import request from '~/utils/request'
const apiPath = '/web/vod/video'

export default {

  getPlayAuth(videoSourceId) {
    return request({
      baseURL: 'http://localhost:8130',
      url: `${apiPath}/get-play-auth/${videoSourceId}`,
      method: 'get'
    })
  }
}
