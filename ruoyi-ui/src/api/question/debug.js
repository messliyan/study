import request from '@/utils/request'

// 查询纠错管理列表
export function listDebug(query) {
  return request({
    url: '/question/debug/list',
    method: 'get',
    params: query
  })
}

// 查询纠错管理详细
export function getDebug(debugId) {
  return request({
    url: '/question/debug/' + debugId,
    method: 'get'
  })
}

// 新增纠错管理
export function addDebug(data) {
  return request({
    url: '/question/debug',
    method: 'post',
    data: data
  })
}

// 修改纠错管理
export function updateDebug(data) {
  return request({
    url: '/question/debug',
    method: 'put',
    data: data
  })
}

// 删除纠错管理
export function delDebug(debugId) {
  return request({
    url: '/question/debug/' + debugId,
    method: 'delete'
  })
}

// 进行纠错
export function debug(debugId) {
  return request({
    url: '/question/debug/' + debugId,
    method: 'put'
  })
}
