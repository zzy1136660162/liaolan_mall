package com.zbkj.service.service;

import com.zbkj.common.request.CaseRequest;
import com.zbkj.common.request.CaseSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.CaseDetailResponse;
import com.zbkj.common.response.CaseListResponse;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 案例服务接口
 */
public interface CaseService {

    /**
     * 获取案例详情（包含关联商品）
     * @param id 案例ID
     * @return CaseDetailResponse
     */
    CaseDetailResponse getCaseDetail(Integer id);

    /**
     * 管理员获取案例列表
     * @param request 搜索参数
     * @param pageParamRequest 分页参数
     * @return PageInfo<CaseListResponse>
     */
    PageInfo<CaseListResponse> getAdminList(CaseSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 前台获取案例列表
     * @param request 筛选参数
     * @param pageParamRequest 分页参数
     * @return PageInfo<CaseListResponse>
     */
    PageInfo<CaseListResponse> getFrontList(CaseSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 新增案例
     * @param request 案例请求参数
     * @return Boolean
     */
    Boolean create(CaseRequest request);

    /**
     * 更新案例
     * @param id 案例ID
     * @param request 案例请求参数
     * @return Boolean
     */
    Boolean update(Integer id, CaseRequest request);

    /**
     * 删除案例
     * @param id 案例ID
     * @return Boolean
     */
    Boolean delete(Integer id);

    /**
     * 案例绑定商品
     * @param id 案例ID
     * @param productId 商品ID
     * @return Boolean
     */
    Boolean bindProduct(Integer id, Integer productId);

    /**
     * 获取所有去重的行业分类
     * @return 行业分类列表
     */
    List<String> getDistinctIndustryCategories();
}
