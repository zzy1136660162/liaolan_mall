package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.download.DownloadMaterialRecord;
import com.zbkj.common.request.DownloadMaterialRecordRequest;
import com.zbkj.common.request.DownloadMaterialRecordSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.DownloadMaterialRecordResponse;

public interface DownloadMaterialRecordService extends IService<DownloadMaterialRecord> {

    PageInfo<DownloadMaterialRecordResponse> getAdminList(DownloadMaterialRecordSearchRequest request, PageParamRequest pageParamRequest);

    Boolean saveRecord(DownloadMaterialRecordRequest request);

    Boolean existsByMaterialIdAndPhone(Integer materialId, String phone);
}
