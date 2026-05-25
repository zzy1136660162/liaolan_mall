package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.article.ArticleSeoExtend;
import com.zbkj.common.request.ArticleSeoInfoRequest;

/**
 * 鏂囩珷 SEO 鎵╁睍鏈嶅姟
 */
public interface ArticleSeoExtendService extends IService<ArticleSeoExtend> {

    /**
     * 鏍规嵁鏂囩珷 ID 鑾峰彇 SEO 鎵╁睍
     */
    ArticleSeoExtend getByArticleId(Integer articleId);

    /**
     * 鏍规嵁鏂囩珷 ID 淇濆瓨鎴栨洿鏂?SEO 鎵╁睍
     */
    Boolean saveOrUpdateByArticleId(Integer articleId, ArticleSeoInfoRequest seoInfoRequest);

    /**
     * 鏍规嵁鏂囩珷 ID 鍒犻櫎 SEO 鎵╁睍
     */
    Boolean deleteByArticleId(Integer articleId);
}
