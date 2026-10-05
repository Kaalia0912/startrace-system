import request from '@/utils/request'

// 查询原料卷料列表
export function listRawCoil(query) {
  return request({
    url: '/inv/rawcoil/list',
    method: 'get',
    params: query
  })
}

// 查询原料卷料详细
export function getRawCoil(coilId) {
  return request({
    url: '/inv/rawcoil/' + coilId,
    method: 'get'
  })
}

// 新增原料卷料
export function addRawCoil(data) {
  return request({
    url: '/inv/rawcoil',
    method: 'post',
    data: data
  })
}

// 修改原料卷料
export function updateRawCoil(data) {
  return request({
    url: '/inv/rawcoil',
    method: 'put',
    data: data
  })
}

// 删除原料卷料
export function delRawCoil(coilId) {
  return request({
    url: '/inv/rawcoil/' + coilId,
    method: 'delete'
  })
}
