package com.zbkj.service.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.contact.ContactMessage;
import com.zbkj.common.model.system.SystemAdmin;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.ContactConfigRequest;
import com.zbkj.common.request.ContactMessageHandleRequest;
import com.zbkj.common.request.ContactMessageRequest;
import com.zbkj.common.request.ContactMessageSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.ContactInfoResponse;
import com.zbkj.common.response.ContactMessageResponse;
import com.zbkj.common.utils.SecurityUtil;
import com.zbkj.service.dao.ContactMessageDao;
import com.zbkj.service.service.ContactMessageService;
import com.zbkj.service.service.SystemConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 联系我们留言服务实现
 */
@Service
public class ContactMessageServiceImpl extends ServiceImpl<ContactMessageDao, ContactMessage> implements ContactMessageService {

    private static final int STATUS_PENDING = 0;
    private static final int STATUS_CONTACTED = 1;
    private static final int STATUS_INVALID = 2;
    private static final int STATUS_OPPORTUNITY = 3;

    @Resource
    private ContactMessageDao dao;

    @Autowired
    private SystemConfigService systemConfigService;

    @Override
    public Boolean submit(ContactMessageRequest request) {
        ContactMessage contactMessage = new ContactMessage();
        BeanUtils.copyProperties(request, contactMessage);
        contactMessage.setCompanyName(StrUtil.trim(request.getCompanyName()));
        contactMessage.setName(StrUtil.trim(request.getName()));
        contactMessage.setMobile(StrUtil.trim(request.getMobile()));
        contactMessage.setContent(StrUtil.trim(request.getContent()));
        String sourcePage = StrUtil.trim(request.getSourcePage());
        contactMessage.setSourcePage(StrUtil.isBlank(sourcePage) ? "contact_page" : sourcePage);
        contactMessage.setStatus(STATUS_PENDING);
        Date now = DateUtil.date();
        contactMessage.setCreatedAt(now);
        contactMessage.setUpdatedAt(now);
        return save(contactMessage);
    }

    @Override
    public ContactInfoResponse getContactInfo() {
        ContactInfoResponse response = new ContactInfoResponse();
        response.setCompanyName(systemConfigService.getValueByKey(Constants.CONFIG_KEY_SITE_NAME));
        response.setConsumerHotline(systemConfigService.getValueByKey(Constants.CONFIG_KEY_CONSUMER_HOTLINE));
        response.setCompanyAddress(systemConfigService.getValueByKey(Constants.CONFIG_KEY_CONTACT_COMPANY_ADDRESS));
        response.setMapLink(systemConfigService.getValueByKey(Constants.CONFIG_KEY_CONTACT_MAP_LINK));
        response.setServiceLink(systemConfigService.getValueByKey(Constants.CONFIG_KEY_YZF_H5_URL));
        response.setServiceQrcode(normalizeMediaUrl(systemConfigService.getValueByKey(Constants.CONFIG_KEY_CONTACT_SERVICE_QRCODE)));
        return response;
    }

    @Override
    public PageInfo<ContactMessageResponse> getAdminList(ContactMessageSearchRequest request, PageParamRequest pageParamRequest) {
        Page<ContactMessage> contactMessagePage = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<ContactMessage> lqw = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(request.getName())) {
            lqw.like(ContactMessage::getName, request.getName().trim());
        }
        if (StrUtil.isNotBlank(request.getMobile())) {
            lqw.like(ContactMessage::getMobile, request.getMobile().trim());
        }
        if (StrUtil.isNotBlank(request.getSourcePage())) {
            lqw.eq(ContactMessage::getSourcePage, request.getSourcePage().trim());
        }
        if (ObjectUtil.isNotNull(request.getStatus())) {
            lqw.eq(ContactMessage::getStatus, request.getStatus());
        }
        lqw.orderByDesc(ContactMessage::getCreatedAt).orderByDesc(ContactMessage::getId);
        List<ContactMessage> contactMessageList = dao.selectList(lqw);
        if (contactMessageList.isEmpty()) {
            return CommonPage.copyPageInfo(contactMessagePage, Collections.emptyList());
        }
        List<ContactMessageResponse> responseList = contactMessageList.stream().map(this::buildResponse).collect(Collectors.toList());
        return CommonPage.copyPageInfo(contactMessagePage, responseList);
    }

    @Override
    public Boolean handle(Integer id, ContactMessageHandleRequest request) {
        ContactMessage contactMessage = getById(id);
        if (ObjectUtil.isNull(contactMessage)) {
            throw new CrmebException("留言记录不存在");
        }
        SystemAdmin admin = SecurityUtil.getLoginUserVo().getUser();
        Date now = DateUtil.date();
        contactMessage.setStatus(request.getStatus());
        contactMessage.setHandleRemark(StrUtil.trim(request.getHandleRemark()));
        contactMessage.setHandleUserId(admin.getId());
        contactMessage.setHandleUserName(admin.getRealName());
        contactMessage.setHandleTime(now);
        contactMessage.setUpdatedAt(now);
        return updateById(contactMessage);
    }

    @Override
    public ContactInfoResponse getAdminConfig() {
        return getContactInfo();
    }

    @Override
    public Boolean saveAdminConfig(ContactConfigRequest request) {
        boolean companyNameResult = systemConfigService.updateOrSaveValueByName(Constants.CONFIG_KEY_SITE_NAME, blank(request.getCompanyName()));
        boolean hotlineResult = systemConfigService.updateOrSaveValueByName(Constants.CONFIG_KEY_CONSUMER_HOTLINE, blank(request.getConsumerHotline()));
        boolean addressResult = systemConfigService.updateOrSaveValueByName(Constants.CONFIG_KEY_CONTACT_COMPANY_ADDRESS, blank(request.getCompanyAddress()));
        boolean mapLinkResult = systemConfigService.updateOrSaveValueByName(Constants.CONFIG_KEY_CONTACT_MAP_LINK, blank(request.getMapLink()));
        boolean serviceLinkResult = systemConfigService.updateOrSaveValueByName(Constants.CONFIG_KEY_YZF_H5_URL, blank(request.getServiceLink()));
        boolean serviceQrcodeResult = systemConfigService.updateOrSaveValueByName(Constants.CONFIG_KEY_CONTACT_SERVICE_QRCODE, blank(request.getServiceQrcode()));
        return companyNameResult && hotlineResult && addressResult && mapLinkResult && serviceLinkResult && serviceQrcodeResult;
    }

    private ContactMessageResponse buildResponse(ContactMessage message) {
        ContactMessageResponse response = new ContactMessageResponse();
        BeanUtils.copyProperties(message, response);
        response.setStatusText(getStatusText(message.getStatus()));
        return response;
    }

    private String getStatusText(Integer status) {
        if (ObjectUtil.isNull(status)) {
            return "待处理";
        }
        switch (status) {
            case STATUS_CONTACTED:
                return "已联系";
            case STATUS_INVALID:
                return "无效线索";
            case STATUS_OPPORTUNITY:
                return "转商机";
            case STATUS_PENDING:
            default:
                return "待处理";
        }
    }

    private String normalizeMediaUrl(String value) {
        if (StrUtil.isBlank(value)) {
            return "";
        }
        if (StrUtil.startWith(value, "http://") || StrUtil.startWith(value, "https://") || StrUtil.startWith(value, "data:")) {
            return value;
        }
        String mediaDomain = systemConfigService.getMediaDomain();
        if (StrUtil.isBlank(mediaDomain)) {
            return value;
        }
        if (StrUtil.endWith(mediaDomain, "/") || StrUtil.startWith(value, "/")) {
            return mediaDomain + value;
        }
        return mediaDomain + "/" + value;
    }

    private String blank(String value) {
        String trimmed = StrUtil.trim(value);
        return StrUtil.isBlank(trimmed) ? "" : trimmed;
    }
}
