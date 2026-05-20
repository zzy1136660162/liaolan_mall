// +----------------------------------------------------------------------
// | CRMEB [ CRMEB赋能开发者，助力企业发展 ]
// +----------------------------------------------------------------------
// | Copyright (c) 2016~2025 https://www.crmeb.com All rights reserved.
// +----------------------------------------------------------------------
// | Licensed CRMEB并不是自由软件，未经许可不能去掉CRMEB相关版权
// +----------------------------------------------------------------------
// | Author: CRMEB Team <admin@crmeb.com>
// +----------------------------------------------------------------------

import Layout from '@/layout';

const contentRouter = {
  path: '/content',
  component: Layout,
  redirect: '/content/classifManager',
  name: 'content',
  meta: {
    title: '内容',
    icon: 'clipboard',
  },
  children: [
    {
      path: 'articleManager',
      name: 'articleManager',
      component: () => import('@/views/content/article/list'),
      meta: {
        title: '文章管理',
        icon: 'clipboard',
      },
    },
    {
      path: 'articleCreat/:id?',
      name: 'articleCreat',
      component: () => import('@/views/content/article/edit'),
      meta: {
        title: '添加文章',
        noCache: true,
        activeMenu: '/content/articleManager'
      }
    },
    {
      path: 'caseManager',
      name: 'caseManager',
      component: () => import('@/views/content/case/list'),
      meta: {
        title: '案例管理',
        icon: 'clipboard',
      },
    },
    {
      path: 'caseCreat/:id?',
      name: 'caseCreat',
      component: () => import('@/views/content/case/edit'),
      meta: {
        title: '添加案例',
        noCache: true,
        activeMenu: `/content/caseManager`,
      },
    },
    {
      path: 'classifManager',
      name: 'classifManager',
      component: () => import('@/views/content/articleclass/list'),
      meta: {
        title: '文章分类',
        icon: 'clipboard'
      }
    },
    {
      path: 'downloadMaterialManager',
      name: 'downloadMaterialManager',
      component: () => import('@/views/content/downloadMaterial/list'),
      meta: {
        title: '资料管理',
        icon: 'clipboard'
      }
    },
    {
      path: 'downloadMaterialCreate/:id?',
      name: 'downloadMaterialCreate',
      component: () => import('@/views/content/downloadMaterial/edit'),
      meta: {
        title: '新增资料',
        noCache: true,
        activeMenu: '/content/downloadMaterialManager'
      }
    },
    {
      path: 'downloadMaterialRecord',
      name: 'downloadMaterialRecord',
      component: () => import('@/views/content/downloadMaterialRecord/list'),
      meta: {
        title: '下载记录',
        icon: 'clipboard'
      }
    },
    {
      path: 'downloadCategory',
      name: 'downloadCategory',
      component: () => import('@/views/content/downloadCategory/list'),
      meta: {
        title: '资料分类',
        icon: 'clipboard'
      }
    }, {
      path: 'aboutManager',
      name: 'aboutManager',
      component: () => import('@/views/content/about/index'),
      meta: {
        title: '关于辽缆',
        icon: 'clipboard',
      },
    },
  ]
}

export default contentRouter;
