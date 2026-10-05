import request from '@/utils/request'

// 按卷号追溯：卷 → 订单 → 工序 → 检验 → 发货
export function traceCoil(coilNo) {
  return request({
    url: '/trace/coil/' + coilNo,
    method: 'get'
  })
}
