import request from '@/utils/request'

// 查询企业档案列表
export function listEnterprise(query) {
  return request({
    url: '/enterprise/enterprise/list',
    method: 'get',
    params: query
  })
}

// 查询企业档案详细
export function getEnterprise(entId) {
  return request({
    url: '/enterprise/enterprise/' + entId,
    method: 'get'
  })
}

// 新增企业档案
export function addEnterprise(data) {
  return request({
    url: '/enterprise/enterprise',
    method: 'post',
    data: data
  })
}

// 修改企业档案
export function updateEnterprise(data) {
  return request({
    url: '/enterprise/enterprise',
    method: 'put',
    data: data
  })
}

// 删除企业档案
export function delEnterprise(entId) {
  return request({
    url: '/enterprise/enterprise/' + entId,
    method: 'delete'
  })
}

// 导入企业档案
export function importEnterprise(data) {
  return request({
    url: '/enterprise/enterprise/importData',
    method: 'post',
    data: data
  })
}

// 下载导入模板
export function importTemplate() {
  return request({
    url: '/enterprise/enterprise/importTemplate',
    method: 'get'
  })
}
