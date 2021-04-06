import request from '@/utils/request'

// 查询专题数据列表
export function listSubject(query) {
  return request({
    url: '/question/subject/list',
    method: 'get',
    params: query
  })
}

// 查询专题数据详细
export function getSubject(subjectId) {
  return request({
    url: '/question/subject/' + subjectId,
    method: 'get'
  })
}

// 新增专题数据
export function addSubject(data) {
  return request({
    url: '/question/subject',
    method: 'post',
    data: data
  })
}

// 修改专题数据
export function updateSubject(data) {
  return request({
    url: '/question/subject',
    method: 'put',
    data: data
  })
}

// 删除专题数据
export function delSubject(subjectId) {
  return request({
    url: '/question/subject/' + subjectId,
    method: 'delete'
  })
}
