import request from '@/utils/request'

// 查询发货单列表
export function listShipment(query) {
  return request({
    url: '/inv/shipment/list',
    method: 'get',
    params: query
  })
}

// 查询发货单详细（含明细）
export function getShipment(shipmentId) {
  return request({
    url: '/inv/shipment/' + shipmentId,
    method: 'get'
  })
}

// 按选中的成品卷发货
export function shipCoils(data) {
  return request({
    url: '/inv/shipment/ship',
    method: 'post',
    data: data
  })
}

// 修改发货单（状态）
export function updateShipment(data) {
  return request({
    url: '/inv/shipment',
    method: 'put',
    data: data
  })
}

// 删除发货单
export function delShipment(shipmentId) {
  return request({
    url: '/inv/shipment/' + shipmentId,
    method: 'delete'
  })
}
