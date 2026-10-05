import request from '@/utils/request'

// 查询成品卷列表
export function listFinishCoil(query) {
  return request({
    url: '/inv/finishcoil/list',
    method: 'get',
    params: query
  })
}

// 查询成品卷详细
export function getFinishCoil(coilId) {
  return request({
    url: '/inv/finishcoil/' + coilId,
    method: 'get'
  })
}

// 新增成品卷
export function addFinishCoil(data) {
  return request({
    url: '/inv/finishcoil',
    method: 'post',
    data: data
  })
}

// 修改成品卷
export function updateFinishCoil(data) {
  return request({
    url: '/inv/finishcoil',
    method: 'put',
    data: data
  })
}

// 删除成品卷
export function delFinishCoil(coilId) {
  return request({
    url: '/inv/finishcoil/' + coilId,
    method: 'delete'
  })
}
