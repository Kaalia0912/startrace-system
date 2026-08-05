import request from '@/utils/request'

// 查询企业联系人列表
export function listContact(query) {
  return request({
    url: '/enterprise/contact/list',
    method: 'get',
    params: query
  })
}

// 查询某企业的联系人列表
export function listContactByEnt(entId) {
  return request({
    url: '/enterprise/contact/listByEnt/' + entId,
    method: 'get'
  })
}

// 查询企业联系人详细
export function getContact(contactId) {
  return request({
    url: '/enterprise/contact/' + contactId,
    method: 'get'
  })
}

// 新增企业联系人
export function addContact(data) {
  return request({
    url: '/enterprise/contact',
    method: 'post',
    data: data
  })
}

// 修改企业联系人
export function updateContact(data) {
  return request({
    url: '/enterprise/contact',
    method: 'put',
    data: data
  })
}

// 删除企业联系人
export function delContact(contactId) {
  return request({
    url: '/enterprise/contact/' + contactId,
    method: 'delete'
  })
}
