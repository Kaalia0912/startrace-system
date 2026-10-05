import request from '@/utils/request'

// 查询设备台账列表
export function listEquip(query) {
  return request({ url: '/prod/equip/list', method: 'get', params: query })
}

// 查询设备台账详细
export function getEquip(equipId) {
  return request({ url: '/prod/equip/' + equipId, method: 'get' })
}

// 新增设备
export function addEquip(data) {
  return request({ url: '/prod/equip', method: 'post', data: data })
}

// 修改设备
export function updateEquip(data) {
  return request({ url: '/prod/equip', method: 'put', data: data })
}

// 删除设备
export function delEquip(equipId) {
  return request({ url: '/prod/equip/' + equipId, method: 'delete' })
}
