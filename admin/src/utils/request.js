// +----------------------------------------------------------------------
// | CRMEB [ CRMEB赋能开发者，助力企业发展 ]
// +----------------------------------------------------------------------
// | Copyright (c) 2016~2025 https://www.crmeb.com All rights reserved.
// +----------------------------------------------------------------------
// | Licensed CRMEB并不是自由软件，未经许可不能去掉CRMEB相关版权
// +----------------------------------------------------------------------
// | Author: CRMEB Team <admin@crmeb.com>
// +----------------------------------------------------------------------

import axios from 'axios';
import { MessageBox, Message } from 'element-ui';
import store from '@/store';
import { getToken } from '@/utils/auth';
import SettingMer from '@/utils/settingMer';
import { isPhone } from '@/libs/wechat';

// 网络错误重试配置
const RETRY_COUNT = 2; // 最大重试次数
const RETRY_DELAY = 2000; // 重试间隔(ms)
let networkErrorShown = false; // 防止多个请求同时弹出重复提示

const service = axios.create({
  baseURL: SettingMer.apiBaseURL,
  timeout: 60000, // 过期时间
});

// request interceptor
service.interceptors.request.use(
  (config) => {
    // 发送请求之前做的
    const token = !store.getters.token ? sessionStorage.getItem('token') : store.getters.token;
    if (token) {
      config.headers['Authori-zation'] = token;
    }
    if (/get/i.test(config.method)) {
      config.params = config.params || {};
      config.params.temp = Date.parse(new Date()) / 1000;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

// response interceptor
service.interceptors.response.use(
  (response) => {
    const res = response.data;
    // if the custom code is not 20000, it is judged as an error.
    if (res.code === 401) {
      // to re-login
      Message.error('无效的会话，或者登录已过期，请重新登录。');
      if (window.location.pathname !== '/login') location.href = '/login';
    } else if (res.code === 403) {
      Message.error('没有权限访问。');
    }
    if (res.code !== 200 && res.code !== 401) {
      if (isPhone()) {
        //移动端
        return Promise.reject(res || 'Error');
      }
      Message({
        message: res.message || 'Error',
        type: 'error',
        duration: 5 * 1000,
      });
      return Promise.reject();
    } else {
      return res.data;
    }
  },
  (error) => {
    const config = error.config;
    // 网络错误（后端未启动/重启中）自动重试
    if (!error.response && config) {
      config.__retryCount = config.__retryCount || 0;
      if (config.__retryCount < RETRY_COUNT) {
        config.__retryCount += 1;
        return new Promise((resolve) => {
          setTimeout(() => resolve(service(config)), RETRY_DELAY);
        });
      }
    }
    // 避免多个请求同时弹出重复的提示
    if (!error.response && !networkErrorShown) {
      networkErrorShown = true;
      Message({
        message: '无法连接到服务器，请确认后端已启动（端口 20500）',
        type: 'error',
        duration: 8 * 1000,
      });
      setTimeout(() => { networkErrorShown = false; }, 10000);
    } else if (error.response) {
      // 有响应但状态码异常
      const status = error.response.status;
      let msg = error.message;
      if (status === 404) msg = '请求的接口不存在';
      else if (status === 500) msg = '服务器内部错误';
      else if (status === 502 || status === 503) msg = '服务器正在启动中，请稍后再试';
      Message({ message: msg, type: 'error', duration: 5 * 1000 });
    }
    return Promise.reject(error);
  },
);

export default service;
