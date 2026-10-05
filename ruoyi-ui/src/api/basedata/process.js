import request from '@/utils/request'

// 查询工序库列表
export function listProcess(query) {
  return request({
    url: '/basedata/process/list',
    method: 'get',
    params: query
  })
}

// 查询工序详细
export function getProcess(processId) {
  return request({
    url: '/basedata/process/' + processId,
    method: 'get'
  })
}

// 新增工序
export function addProcess(data) {
  return request({
    url: '/basedata/process',
    method: 'post',
    data: data
  })
}

// 修改工序
export function updateProcess(data) {
  return request({
    url: '/basedata/process',
    method: 'put',
    data: data
  })
}

// 删除工序
export function delProcess(processId) {
  return request({
    url: '/basedata/process/' + processId,
    method: 'delete'
  })
}
