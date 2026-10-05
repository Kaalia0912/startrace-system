import request from '@/utils/request'

// 查询宽度公差标准列表
export function listTolerance(query) {
  return request({
    url: '/basedata/tolerance/list',
    method: 'get',
    params: query
  })
}

// 查询宽度公差标准详细
export function getTolerance(toleranceId) {
  return request({
    url: '/basedata/tolerance/' + toleranceId,
    method: 'get'
  })
}

// 新增宽度公差标准
export function addTolerance(data) {
  return request({
    url: '/basedata/tolerance',
    method: 'post',
    data: data
  })
}

// 修改宽度公差标准
export function updateTolerance(data) {
  return request({
    url: '/basedata/tolerance',
    method: 'put',
    data: data
  })
}

// 删除宽度公差标准
export function delTolerance(toleranceId) {
  return request({
    url: '/basedata/tolerance/' + toleranceId,
    method: 'delete'
  })
}
