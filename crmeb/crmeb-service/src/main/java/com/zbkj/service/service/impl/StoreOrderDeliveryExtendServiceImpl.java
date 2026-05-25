package com.zbkj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.common.model.order.StoreOrderDeliveryExtend;
import com.zbkj.common.request.OrderDeliveryExtendRequest;
import com.zbkj.common.response.OrderDeliveryExtendResponse;
import com.zbkj.service.dao.StoreOrderDeliveryExtendDao;
import com.zbkj.service.service.StoreOrderDeliveryExtendService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class StoreOrderDeliveryExtendServiceImpl extends ServiceImpl<StoreOrderDeliveryExtendDao, StoreOrderDeliveryExtend>
        implements StoreOrderDeliveryExtendService {

    @Override
    public void saveDeliveryExtend(Integer orderId, OrderDeliveryExtendRequest request) {
        StoreOrderDeliveryExtend extend = new StoreOrderDeliveryExtend();
        extend.setOrderId(orderId);
        extend.setDeliveryCompany(request.getDeliveryCompany() != null ? request.getDeliveryCompany() : "");
        extend.setDeliveryNo(request.getDeliveryNo() != null ? request.getDeliveryNo() : "");
        extend.setDeliveryRemark(request.getDeliveryRemark() != null ? request.getDeliveryRemark() : "");
        extend.setBatchNo(request.getBatchNo() != null ? request.getBatchNo() : "");
        extend.setFactorySerialNo(request.getFactorySerialNo() != null ? request.getFactorySerialNo() : "");
        extend.setCreateTime(new Date());
        extend.setUpdateTime(new Date());
        save(extend);
    }

    @Override
    public void updateDeliveryExtend(Integer orderId, OrderDeliveryExtendRequest request) {
        StoreOrderDeliveryExtend existing = getByOrderId(orderId);
        if (existing == null) {
            saveDeliveryExtend(orderId, request);
            return;
        }
        existing.setDeliveryCompany(request.getDeliveryCompany() != null ? request.getDeliveryCompany() : "");
        existing.setDeliveryNo(request.getDeliveryNo() != null ? request.getDeliveryNo() : "");
        existing.setDeliveryRemark(request.getDeliveryRemark() != null ? request.getDeliveryRemark() : "");
        existing.setBatchNo(request.getBatchNo() != null ? request.getBatchNo() : "");
        existing.setFactorySerialNo(request.getFactorySerialNo() != null ? request.getFactorySerialNo() : "");
        existing.setUpdateTime(new Date());
        updateById(existing);
    }

    @Override
    public OrderDeliveryExtendResponse getDeliveryExtend(Integer orderId) {
        StoreOrderDeliveryExtend extend = getByOrderId(orderId);
        if (extend == null) {
            return buildEmptyResponse();
        }
        OrderDeliveryExtendResponse response = new OrderDeliveryExtendResponse();
        BeanUtils.copyProperties(extend, response);
        return response;
    }

    private StoreOrderDeliveryExtend getByOrderId(Integer orderId) {
        LambdaQueryWrapper<StoreOrderDeliveryExtend> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoreOrderDeliveryExtend::getOrderId, orderId);
        return getOne(wrapper);
    }

    private OrderDeliveryExtendResponse buildEmptyResponse() {
        OrderDeliveryExtendResponse response = new OrderDeliveryExtendResponse();
        response.setDeliveryCompany("");
        response.setDeliveryNo("");
        response.setDeliveryRemark("");
        response.setBatchNo("");
        response.setFactorySerialNo("");
        return response;
    }
}
