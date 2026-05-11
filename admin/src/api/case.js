import request from '@/utils/request';

/**
 * 案例列表（含行业/区域筛选）
 * @param pram
 */
export function getCaseList(pram) {
  const data = {
    keywords: pram.keywords,
    page: pram.page,
    limit: pram.limit,
    industryCategory: pram.industryCategory,
    regionCategory: pram.regionCategory,
    projectType: pram.projectType,
    status: pram.status,
  };
  return request({
    url: '/admin/case/list',
    method: 'GET',
    params: data,
  });
}

/**
 * 案例详情（扁平化，含扩展信息）
 * @param id
 */
export function getCaseDetail(id) {
  const data = {
    id: id,
  };
  return request({
    url: '/admin/case/info',
    method: 'get',
    params: data,
  });
}

/**
 * 新增案例（扁平化参数）
 * @param pram
 */
export function saveCase(pram) {
  const data = {
    author: pram.author,
    content: pram.content,
    imageInput: pram.imageInput,
    isBanner: pram.isBanner,
    isHot: pram.isHot,
    shareSynopsis: pram.shareSynopsis,
    shareTitle: pram.shareTitle,
    sort: pram.sort,
    synopsis: pram.synopsis,
    title: pram.title,
    url: pram.url,
    hide: pram.hide,
    projectName: pram.projectName,
    industryCategory: pram.industryCategory,
    regionCategory: pram.regionCategory,
    projectType: pram.projectType,
    projectAddress: pram.projectAddress,
    projectPeriod: pram.projectPeriod,
    projectBackground: pram.projectBackground,
    supplyProducts: pram.supplyProducts,
    implementationResult: pram.implementationResult,
    productIds: pram.productIds,
    coverImages: pram.coverImages,
  };
  return request({
    url: '/admin/case/save',
    method: 'post',
    data: data,
  });
}

/**
 * 更新案例（扁平化参数）
 * @param pram
 */
export function updateCase(pram) {
  const data = {
    author: pram.author,
    content: pram.content,
    imageInput: pram.imageInput,
    isBanner: pram.isBanner,
    isHot: pram.isHot,
    shareSynopsis: pram.shareSynopsis,
    shareTitle: pram.shareTitle,
    sort: pram.sort,
    synopsis: pram.synopsis,
    title: pram.title,
    url: pram.url,
    hide: pram.hide,
    projectName: pram.projectName,
    industryCategory: pram.industryCategory,
    regionCategory: pram.regionCategory,
    projectType: pram.projectType,
    projectAddress: pram.projectAddress,
    projectPeriod: pram.projectPeriod,
    projectBackground: pram.projectBackground,
    supplyProducts: pram.supplyProducts,
    implementationResult: pram.implementationResult,
    productIds: pram.productIds,
    coverImages: pram.coverImages,
  };
  return request({
    url: '/admin/case/update',
    method: 'post',
    params: { id: pram.id },
    data: data,
  });
}

/**
 * 删除案例
 * @param pram
 */
export function deleteCase(pram) {
  const data = {
    id: pram.id,
  };
  return request({
    url: '/admin/case/delete',
    method: 'GET',
    params: data,
  });
}

/**
 * 案例绑定商品
 * @param pram
 */
export function bindCaseProduct(pram) {
  const data = {
    id: pram.id,
    productId: pram.productId,
  };
  return request({
    url: '/admin/case/bind/product',
    method: 'POST',
    params: data,
  });
}
