import request from '@/utils/request'

// 订单执行进度（订单 → 工单 → 成品入库 → 发货）
export function listOrderProgress(query) {
  return request({ url: '/order/sale/progress', method: 'get', params: query })
}
