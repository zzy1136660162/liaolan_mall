// +----------------------------------------------------------------------
// | CRMEB [ CRMEB empowered development ]
// +----------------------------------------------------------------------

import request from '@/utils/request';

function buildArticlePayload(pram) {
  const seoInfo = pram.seoInfo || {};
  return {
    author: pram.author,
    cid: pram.cid,
    content: pram.content,
    imageInput: pram.imageInput,
    isBanner: pram.isBanner,
    isHot: pram.isHot,
    shareSynopsis: seoInfo.seoDescription || pram.shareSynopsis,
    shareTitle: seoInfo.shareTitle || pram.shareTitle,
    sort: pram.sort,
    synopsis: pram.synopsis,
    title: pram.title,
    url: pram.url,
    seoInfo: {
      seoTitle: seoInfo.seoTitle,
      seoKeywords: seoInfo.seoKeywords,
      seoDescription: seoInfo.seoDescription,
      shareTitle: seoInfo.shareTitle,
      shareImage: seoInfo.shareImage,
    },
  };
}

export function bindProduct(pram) {
  return request({
    url: '/admin/article/bind/product',
    method: 'POST',
    params: {
      id: pram.id,
      productId: pram.productId,
    },
  });
}

export function DelArticle(pram) {
  return request({
    url: '/admin/article/delete',
    method: 'GET',
    params: {
      id: pram.id,
    },
  });
}

export function InfoArticle(id) {
  return request({
    url: '/admin/article/info',
    method: 'get',
    params: {
      id,
    },
  });
}

export function ListArticle(pram) {
  return request({
    url: '/admin/article/list',
    method: 'GET',
    params: {
      keywords: pram.keywords,
      cid: pram.cid,
      page: pram.page,
      limit: pram.limit,
    },
  });
}

export function AddArticle(pram) {
  return request({
    url: '/admin/article/save',
    method: 'post',
    data: buildArticlePayload(pram),
  });
}

export function UpdateArticle(pram) {
  return request({
    url: '/admin/article/update',
    method: 'post',
    params: { id: pram.id },
    data: buildArticlePayload(pram),
  });
}
