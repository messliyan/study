import request from '@/utils/request'

// 查询意见反馈列表
export function listIdea(query) {
  return request({
    url: '/wxapp/idea/list',
    method: 'get',
    params: query
  })
}

// 查询意见反馈详细
export function getIdea(ideaId) {
  return request({
    url: '/wxapp/idea/' + ideaId,
    method: 'get'
  })
}

// 新增意见反馈
export function addIdea(data) {
  return request({
    url: '/wxapp/idea',
    method: 'post',
    data: data
  })
}

// 修改意见反馈
export function updateIdea(data) {
  return request({
    url: '/wxapp/idea',
    method: 'put',
    data: data
  })
}

// 删除意见反馈
export function delIdea(ideaId) {
  return request({
    url: '/wxapp/idea/' + ideaId,
    method: 'delete'
  })
}
