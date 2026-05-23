package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.contact.ContactMessage;
import com.zbkj.common.request.ContactConfigRequest;
import com.zbkj.common.request.ContactMessageHandleRequest;
import com.zbkj.common.request.ContactMessageRequest;
import com.zbkj.common.request.ContactMessageSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.ContactInfoResponse;
import com.zbkj.common.response.ContactMessageResponse;

/**
 * 联系我们留言服务
 */
public interface ContactMessageService extends IService<ContactMessage> {

    Boolean submit(ContactMessageRequest request);

    ContactInfoResponse getContactInfo();

    PageInfo<ContactMessageResponse> getAdminList(ContactMessageSearchRequest request, PageParamRequest pageParamRequest);

    Boolean handle(Integer id, ContactMessageHandleRequest request);

    ContactInfoResponse getAdminConfig();

    Boolean saveAdminConfig(ContactConfigRequest request);
}
