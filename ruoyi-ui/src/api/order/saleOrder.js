import request from '@/utils/request'

// 查询销售订单列表
export function listSaleOrder(query) {
  return request({
    url: '/order/sale/list',
    method: 'get',
    params: query
  })
}

// 查询销售订单详细
export function getSaleOrder(orderId) {
  return request({
    url: '/order/sale/' + orderId,
    method: 'get'
  })
}

// 查询经营工作台数据
export function getSaleOrderDashboard() {
  return request({
    url: '/order/sale/dashboard',
    method: 'get'
  })
}

// 新增销售订单
export function addSaleOrder(data) {
  return request({
    url: '/order/sale',
    method: 'post',
    data: data
  })
}

// 修改销售订单
export function updateSaleOrder(data) {
  return request({
    url: '/order/sale',
    method: 'put',
    data: data
  })
}

// 删除销售订单
export function delSaleOrder(orderId) {
  return request({
    url: '/order/sale/' + orderId,
    method: 'delete'
  })
}

// 导入销售订单
export function importSaleOrder(data) {
  return request({
    url: '/order/sale/importData',
    method: 'post',
    data: data
  })
}

// 下载导入模板
export function importTemplate() {
  return request({
    url: '/order/sale/importTemplate',
    method: 'get'
  })
}
