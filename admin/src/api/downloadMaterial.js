import request from '@/utils/request'

export function getDownloadMaterialList(params) {
  return request({
    url: '/admin/download/material/list',
    method: 'get',
    params
  })
}

export function getDownloadMaterialInfo(id) {
  return request({
    url: `/admin/download/material/info/${id}`,
    method: 'get'
  })
}

export function createDownloadMaterial(data) {
  return request({
    url: '/admin/download/material/save',
    method: 'post',
    data
  })
}

export function updateDownloadMaterial(id, data) {
  return request({
    url: '/admin/download/material/update',
    method: 'post',
    params: { id },
    data
  })
}

export function getDownloadMaterialRecordList(params) {
  return request({
    url: '/admin/download/material/record/list',
    method: 'get',
    params
  })
}
