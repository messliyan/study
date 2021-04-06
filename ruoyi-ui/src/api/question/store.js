import request from '@/utils/request'

// 查询题库数据列表
export function listStore(query) {
  return request({
    url: '/question/store/list',
    method: 'get',
    params: query
  })
}

// 查询题库数据详细
export function getStore(storeId) {
  return request({
    url: '/question/store/' + storeId,
    method: 'get'
  })
}

// 新增题库数据
export function addStore(data) {
  return request({
    url: '/question/store',
    method: 'post',
    data: data
  })
}

// 修改题库数据
export function updateStore(data) {
  return request({
    url: '/question/store',
    method: 'put',
    data: data
  })
}

// 删除题库数据
export function delStore(storeId) {
  return request({
    url: '/question/store/' + storeId,
    method: 'delete'
  })
}
