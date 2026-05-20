package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.product.StoreProductIndustry;
import com.zbkj.common.request.ProductIndustryRequest;
import com.zbkj.common.response.ProductIndustryResponse;

public interface StoreProductIndustryService extends IService<StoreProductIndustry> {

    void saveIndustryInfo(Integer productId, ProductIndustryRequest request);

    void updateIndustryInfo(Integer productId, ProductIndustryRequest request);

    ProductIndustryResponse getIndustryInfo(Integer productId);

    void deleteByProductId(Integer productId);
}
