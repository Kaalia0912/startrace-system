import request from '@/utils/request'

// 查询物料主数据列表
export function listMaterial(query) {
  return request({
    url: '/basedata/material/list',
    method: 'get',
    params: query
  })
}

// 查询物料主数据详细
export function getMaterial(materialId) {
  return request({
    url: '/basedata/material/' + materialId,
    method: 'get'
  })
}

// 新增物料主数据
export function addMaterial(data) {
  return request({
    url: '/basedata/material',
    method: 'post',
    data: data
  })
}

// 修改物料主数据
export function updateMaterial(data) {
  return request({
    url: '/basedata/material',
    method: 'put',
    data: data
  })
}

// 删除物料主数据
export function delMaterial(materialId) {
  return request({
    url: '/basedata/material/' + materialId,
    method: 'delete'
  })
}

// 导入物料主数据
export function importMaterial(data) {
  return request({
    url: '/basedata/material/importData',
    method: 'post',
    data: data
  })
}

// 下载物料导入模板
export function importTemplate() {
  return request({
    url: '/basedata/material/importTemplate',
    method: 'get'
  })
}
