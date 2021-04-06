import request from '@/utils/request'

// 查询专题资料列表
export function listMaterial(query) {
  return request({
    url: '/question/material/list',
    method: 'get',
    params: query
  })
}

// 查询专题资料详细
export function getMaterial(materialId) {
  return request({
    url: '/question/material/' + materialId,
    method: 'get'
  })
}

// 新增专题资料
export function addMaterial(data) {
  return request({
    url: '/question/material',
    method: 'post',
    data: data
  })
}

// 修改专题资料
export function updateMaterial(data) {
  return request({
    url: '/question/material',
    method: 'put',
    data: data
  })
}

// 删除专题资料
export function delMaterial(materialId) {
  return request({
    url: '/question/material/' + materialId,
    method: 'delete'
  })
}
