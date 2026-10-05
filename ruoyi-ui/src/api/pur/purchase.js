import request from '@/utils/request'

// 查询采购单列表
export function listPurchase(query) {
  return request({ url: '/pur/order/list', method: 'get', params: query })
}

// 查询采购单详细
export function getPurchase(purchaseId) {
  return request({ url: '/pur/order/' + purchaseId, method: 'get' })
}

// 新增采购单
export function addPurchase(data) {
  return request({ url: '/pur/order', method: 'post', data: data })
}

// 修改采购单
export function updatePurchase(data) {
  return request({ url: '/pur/order', method: 'put', data: data })
}

// 删除采购单
export function delPurchase(purchaseId) {
  return request({ url: '/pur/order/' + purchaseId, method: 'delete' })
}

// 收货入库（生成原料卷）
export function receivePurchase(purchaseId) {
  return request({ url: '/pur/order/receive/' + purchaseId, method: 'put' })
}
