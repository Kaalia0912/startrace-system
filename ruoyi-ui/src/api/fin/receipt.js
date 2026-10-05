import request from '@/utils/request'

// 查询收款登记列表
export function listReceipt(query) {
  return request({ url: '/fin/receipt/list', method: 'get', params: query })
}

// 查询收款登记详细
export function getReceipt(receiptId) {
  return request({ url: '/fin/receipt/' + receiptId, method: 'get' })
}

// 新增收款登记
export function addReceipt(data) {
  return request({ url: '/fin/receipt', method: 'post', data: data })
}

// 修改收款登记
export function updateReceipt(data) {
  return request({ url: '/fin/receipt', method: 'put', data: data })
}

// 删除收款登记
export function delReceipt(receiptId) {
  return request({ url: '/fin/receipt/' + receiptId, method: 'delete' })
}

// 应收汇总（按客户）
export function receivableSummary(query) {
  return request({ url: '/fin/receipt/summary', method: 'get', params: query })
}
