import request from '@/utils/request'

export function listProcessRecord(query) {
  return request({ url: '/prod/record/list', method: 'get', params: query })
}

export function getProcessRecord(recordId) {
  return request({ url: '/prod/record/' + recordId, method: 'get' })
}

export function addProcessRecord(data) {
  return request({ url: '/prod/record', method: 'post', data: data })
}

export function updateProcessRecord(data) {
  return request({ url: '/prod/record', method: 'put', data: data })
}

export function delProcessRecord(recordId) {
  return request({ url: '/prod/record/' + recordId, method: 'delete' })
}
