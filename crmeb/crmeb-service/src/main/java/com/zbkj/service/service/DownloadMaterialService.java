package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.category.Category;
import com.zbkj.common.model.download.DownloadMaterial;
import com.zbkj.common.request.DownloadMaterialRequest;
import com.zbkj.common.request.DownloadMaterialSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.DownloadMaterialResponse;

import java.util.List;

public interface DownloadMaterialService extends IService<DownloadMaterial> {

    PageInfo<DownloadMaterialResponse> getAdminList(DownloadMaterialSearchRequest request, PageParamRequest pageParamRequest);

    Boolean create(DownloadMaterialRequest request);

    Boolean updateMaterial(Integer id, DownloadMaterialRequest request);

    DownloadMaterialResponse getAdminDetail(Integer id);

    PageInfo<DownloadMaterialResponse> getFrontList(DownloadMaterialSearchRequest request, PageParamRequest pageParamRequest);

    DownloadMaterialResponse getFrontDetail(Integer id);

    String getDownloadUrl(Integer id, String phone);

    List<Category> getFrontCategoryList();
}
