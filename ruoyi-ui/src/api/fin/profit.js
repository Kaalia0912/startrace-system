import request from '@/utils/request'

// 订单毛利列表
export function listProfit(query) {
  return request({ url: '/fin/profit/list', method: 'get', params: query })
}

// 当前加工费单价(元/kg)
export function getProcessCost() {
  return request({ url: '/fin/profit/rate', method: 'get' })
}
