import request from '@/utils/request'

export function listWorkOrder(query) {
  return request({ url: '/prod/workorder/list', method: 'get', params: query })
}

export function getWorkOrder(workOrderId) {
  return request({ url: '/prod/workorder/' + workOrderId, method: 'get' })
}

export function addWorkOrder(data) {
  return request({ url: '/prod/workorder', method: 'post', data: data })
}

export function updateWorkOrder(data) {
  return request({ url: '/prod/workorder', method: 'put', data: data })
}

export function delWorkOrder(workOrderId) {
  return request({ url: '/prod/workorder/' + workOrderId, method: 'delete' })
}
