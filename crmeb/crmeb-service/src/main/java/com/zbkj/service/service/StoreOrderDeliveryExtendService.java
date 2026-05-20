package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.order.StoreOrderDeliveryExtend;
import com.zbkj.common.request.OrderDeliveryExtendRequest;
import com.zbkj.common.response.OrderDeliveryExtendResponse;

public interface StoreOrderDeliveryExtendService extends IService<StoreOrderDeliveryExtend> {

    void saveDeliveryExtend(Integer orderId, OrderDeliveryExtendRequest request);

    void updateDeliveryExtend(Integer orderId, OrderDeliveryExtendRequest request);

    OrderDeliveryExtendResponse getDeliveryExtend(Integer orderId);
}
