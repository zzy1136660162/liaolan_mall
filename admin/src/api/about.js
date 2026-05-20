import request from '@/utils/request';

/**
 * 关于辽缆 - 获取内容块列表
 */
export function aboutBlockListApi() {
  return request({
    url: '/admin/about/block/list',
    method: 'get',
  });
}

/**
 * 关于辽缆 - 获取内容块详情
 * @param {Number} id
 */
export function aboutBlockInfoApi(id) {
  return request({
    url: `/admin/about/block/info/${id}`,
    method: 'get',
  });
}

/**
 * 关于辽缆 - 更新内容块
 * @param {Object} data
 */
export function aboutBlockUpdateApi(data) {
  return request({
    url: '/admin/about/block/update',
    method: 'post',
    data,
  });
}
