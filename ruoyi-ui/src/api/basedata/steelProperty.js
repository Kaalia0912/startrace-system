import request from '@/utils/request'

// 查询钢种性能标准列表
export function listSteelProperty(query) {
  return request({
    url: '/basedata/property/list',
    method: 'get',
    params: query
  })
}

// 查询钢种性能标准详细
export function getSteelProperty(propertyId) {
  return request({
    url: '/basedata/property/' + propertyId,
    method: 'get'
  })
}

// 新增钢种性能标准
export function addSteelProperty(data) {
  return request({
    url: '/basedata/property',
    method: 'post',
    data: data
  })
}

// 修改钢种性能标准
export function updateSteelProperty(data) {
  return request({
    url: '/basedata/property',
    method: 'put',
    data: data
  })
}

// 删除钢种性能标准
export function delSteelProperty(propertyId) {
  return request({
    url: '/basedata/property/' + propertyId,
    method: 'delete'
  })
}
