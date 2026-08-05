import request from '@/utils/request'

// 获取统计看板数据
export function getStats() {
  return request({
    url: '/enterprise/dashboard/stats',
    method: 'get'
  })
}
