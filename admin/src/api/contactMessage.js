// +----------------------------------------------------------------------
// | CRMEB [ CRMEB赋能开发者，助力企业发展 ]
// +----------------------------------------------------------------------
// | Copyright (c) 2016~2025 https://www.crmeb.com All rights reserved.
// +----------------------------------------------------------------------
// | Licensed CRMEB并不是自由软件，未经许可不能去掉CRMEB相关版权
// +----------------------------------------------------------------------
// | Author: CRMEB Team <admin@crmeb.com>
// +----------------------------------------------------------------------

import request from '@/utils/request'

export function getContactMessageList(params) {
  return request({
    url: '/admin/contact/message/list',
    method: 'GET',
    params
  })
}

export function handleContactMessage(id, data) {
  return request({
    url: '/admin/contact/message/handle',
    method: 'POST',
    params: { id },
    data
  })
}

export function getContactConfig() {
  return request({
    url: '/admin/contact/message/config',
    method: 'GET'
  })
}

export function saveContactConfig(data) {
  return request({
    url: '/admin/contact/message/config',
    method: 'POST',
    data
  })
}
