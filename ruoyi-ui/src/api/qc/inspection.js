import request from '@/utils/request'

export function listInspection(query) {
  return request({ url: '/qc/inspection/list', method: 'get', params: query })
}

export function getInspection(inspectionId) {
  return request({ url: '/qc/inspection/' + inspectionId, method: 'get' })
}

export function addInspection(data) {
  return request({ url: '/qc/inspection', method: 'post', data: data })
}

export function updateInspection(data) {
  return request({ url: '/qc/inspection', method: 'put', data: data })
}

export function delInspection(inspectionId) {
  return request({ url: '/qc/inspection/' + inspectionId, method: 'delete' })
}
