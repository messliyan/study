import request from '@/utils/request'

// 查询考试设置列表
export function listExam(query) {
  return request({
    url: '/wxapp/exam/list',
    method: 'get',
    params: query
  })
}

// 查询考试设置详细
export function getExam(examId) {
  return request({
    url: '/wxapp/exam/' + examId,
    method: 'get'
  })
}

// 新增考试设置
export function addExam(data) {
  return request({
    url: '/wxapp/exam',
    method: 'post',
    data: data
  })
}

// 修改考试设置
export function updateExam(data) {
  return request({
    url: '/wxapp/exam',
    method: 'put',
    data: data
  })
}

// 删除考试设置
export function delExam(examId) {
  return request({
    url: '/wxapp/exam/' + examId,
    method: 'delete'
  })
}
