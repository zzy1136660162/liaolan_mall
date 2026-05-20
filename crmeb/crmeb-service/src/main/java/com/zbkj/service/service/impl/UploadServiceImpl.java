package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.constants.SysConfigConstants;
import com.zbkj.common.constants.UploadConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.result.CommonResultCode;
import com.zbkj.common.vo.CloudVo;
import com.zbkj.common.vo.FileResultVo;
import com.zbkj.common.vo.UploadCommonVo;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.zbkj.common.utils.CrmebUtil;
import com.zbkj.common.utils.CrmebDateUtil;
import com.zbkj.common.utils.UploadUtil;
import com.zbkj.common.model.system.SystemAttachment;
import com.zbkj.common.config.CrmebConfig;
import com.zbkj.service.service.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * UploadServiceImpl 接口实现
 * +----------------------------------------------------------------------
 * | CRMEB [ CRMEB赋能开发者，助力企业发展 ]
 * +----------------------------------------------------------------------
 * | Copyright (c) 2016~2025 https://www.crmeb.com All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed CRMEB并不是自由软件，未经许可不能去掉CRMEB相关版权
 * +----------------------------------------------------------------------
 * | Author: CRMEB Team <admin@crmeb.com>
 * +----------------------------------------------------------------------
 */
@Service
public class UploadServiceImpl implements UploadService {

    private static final Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    @Autowired
    private QiNiuService qiNiuService;

    @Autowired
    private OssService ossService;

    @Autowired
    private CosService cosService;

    @Autowired
    CrmebConfig crmebConfig;

    @Autowired
    private JdCloudService jdCloudService;

    /**
     * 图片上传
     * @param multipartFile 文件
     * @param model 模块 用户user,商品product,微信wechat,news文章
     * @param pid 分类ID 0编辑器,1商品图片,2拼团图片,3砍价图片,4秒杀图片,5文章图片,6组合数据图,7前台用户,8微信系列
     * @return FileResultVo
     */
    @Override
    public FileResultVo imageUpload(MultipartFile multipartFile, String model, Integer pid) {
        FileResultVo fileResultVo = new FileResultVo();
        try {
            fileResultVo = commonUpload(multipartFile, model, pid, UploadConstants.UPLOAD_FILE_KEYWORD);
        } catch (IOException e) {
            logger.error("图片上传IO异常，{}", e.getMessage());
            throw new CrmebException("图片上传 IO异常");
        }
        return fileResultVo;
    }

    /**
     * 文件长传
     * @param multipartFile 文件
     * @param model 模块 用户user,商品product,微信wechat,news文章
     * @param pid 分类ID 0编辑器,1商品图片,2拼团图片,3砍价图片,4秒杀图片,5文章图片,6组合数据图,7前台用户,8微信系列
     * @return FileResultVo
     * @throws IOException
     */
    @Override
    public FileResultVo fileUpload(MultipartFile multipartFile, String model, Integer pid) throws IOException {
        FileResultVo fileResultVo = new FileResultVo();
        try {
            fileResultVo = commonUpload(multipartFile, model, pid, UploadConstants.UPLOAD_FILE_KEYWORD);
        } catch (IOException e) {
            logger.error("文件上传IO异常，{}", e.getMessage());
            throw new CrmebException("文件上传 IO异常");
        }
        return fileResultVo;
    }

    /**
     * 上传校验
     *
     * @param fileName 文件名称
     * @param fileSize 文件大小
     * @return 后缀名
     */
    private String uploadValidate(String fileName, float fileSize, String fileType, String contentType) {
        String extName = FilenameUtils.getExtension(fileName).toLowerCase();
        if (StrUtil.isEmpty(extName)) {
            if (StrUtil.isNotBlank(contentType) && contentType.contains("/")) {
                String[] parts = contentType.split("/");
                if (parts.length > 1 && StrUtil.isNotBlank(parts[1])) {
                    extName = parts[1];
                }
            }
            if (StrUtil.isEmpty(extName)) {
                throw new CrmebException(CommonResultCode.VALIDATE_FAILED, "文件类型未定义，无法上传...");
            }
        }

        String extStr = systemConfigService.getValueByKey(fileType.equals(UploadConstants.UPLOAD_AFTER_FILE_KEYWORD) ? SysConfigConstants.UPLOAD_FILE_EXT_STR_CONFIG_KEY : SysConfigConstants.UPLOAD_IMAGE_EXT_STR_CONFIG_KEY);
        // 判断文件的后缀名是否符合规则
        if (StrUtil.isNotBlank(extStr)) {
            // 切割文件扩展名
            List<String> extensionList = CrmebUtil.stringToArrayStr(extStr);
            if (CollUtil.isNotEmpty(extensionList)) {
                //判断
                if (!extensionList.contains(extName)) {
                    throw new CrmebException(CommonResultCode.VALIDATE_FAILED, "上载文件类型只能为：" + extStr);
                }
            } else {
                throw new CrmebException(CommonResultCode.VALIDATE_FAILED, "上载文件类型只能为：" + extStr);
            }
        }
        // 文件大小验证
        int size = Integer.parseInt(systemConfigService.getValueByKey(fileType.equals(UploadConstants.UPLOAD_AFTER_FILE_KEYWORD) ? SysConfigConstants.UPLOAD_FILE_MAX_SIZE_CONFIG_KEY : SysConfigConstants.UPLOAD_IMAGE_MAX_SIZE_CONFIG_KEY));
        String fs = String.format("%.2f", fileSize);
        if (fileSize > size) {
            throw new CrmebException(CommonResultCode.VALIDATE_FAILED, StrUtil.format("最大允许上传 {} MB文件，当前文件大小为 {} MB", size, fs));
        }
        return extName;
    }


    /**
     * 公共上传
     *
     * @param multipartFile 文件
     * @param model         模块 用户user,商品product,微信wechat,文章article,系统system
     * @param pid           分类ID 0编辑器,1商品图片,2拼团图片,3砍价图片,4秒杀图片,5文章图片,6组合数据图,7前台用户,8微信系列
     * @param fileType      文件类型
     * @return FileResultVo
     * @throws IOException IOE异常
     */
    private FileResultVo commonUpload(MultipartFile multipartFile, String model, Integer pid, String fileType) throws IOException {
        if (ObjectUtil.isNull(multipartFile) || multipartFile.isEmpty()) {
            throw new CrmebException(CommonResultCode.VALIDATE_FAILED, "上载的文件对象不存在...");
        }
        // 校验
        String fileName = multipartFile.getOriginalFilename();
        float fileSize = (float) multipartFile.getSize() / 1024 / 1024;
        // 文件后缀名
        String extName = uploadValidate(fileName, fileSize, fileType, multipartFile.getContentType());
        if (fileName.length() > 99) {
            fileName = StrUtil.subPre(fileName, 90).concat(".").concat(extName);
        }

        // 服务器存储地址
        String rootPath = crmebConfig.getImagePath().trim();
        // 模块
        String modelPath = "public/" + model + "/";
        // 类型
        String type = (fileType.equals(UploadConstants.UPLOAD_FILE_KEYWORD) ? UploadConstants.UPLOAD_FILE_KEYWORD : UploadConstants.UPLOAD_AFTER_FILE_KEYWORD) + "/";

        // 变更文件名
        String newFileName = UploadUtil.fileName(extName);
        // 创建目标文件的名称，规则：  子目录/年/月/日.后缀名
        String webPath = type + modelPath + CrmebDateUtil.nowDate("yyyy/MM/dd") + "/";
        // 文件分隔符转化为当前系统的格式
        String destPath = FilenameUtils.separatorsToSystem(rootPath + webPath) + newFileName;
        // 创建文件
        File file = UploadUtil.createFile(destPath);

        // 拼装返回的数据
        FileResultVo resultFile = new FileResultVo();
        resultFile.setFileSize(multipartFile.getSize());
        resultFile.setFileName(fileName);
        resultFile.setExtName(extName);
        resultFile.setUrl(webPath + newFileName);
        resultFile.setType(multipartFile.getContentType());
        if (fileType.equals(UploadConstants.UPLOAD_FILE_KEYWORD)) {
            resultFile.setType(resultFile.getType().replace("image/", ""));
        } else {
            resultFile.setType(resultFile.getType().replace("file/", ""));
        }

        SystemAttachment systemAttachment = new SystemAttachment();
        systemAttachment.setName(resultFile.getFileName());
        systemAttachment.setSattDir(resultFile.getUrl());
        systemAttachment.setAttSize(resultFile.getFileSize().toString());
        systemAttachment.setAttType(resultFile.getType());
        systemAttachment.setImageType(1);   //图片上传类型 1本地 2七牛云 3OSS 4COS, 默认本地
        systemAttachment.setPid(pid);

        //图片上传类型 1本地 2七牛云 3OSS 4COS, 默认本地
        String uploadType = systemConfigService.getValueByKeyException(SysConfigConstants.CONFIG_UPLOAD_TYPE);
        Integer uploadTypeInt = Integer.parseInt(uploadType);
        if (uploadTypeInt.equals(1)) {
            // 保存文件
            multipartFile.transferTo(file);
            systemAttachmentService.save(systemAttachment);
            return resultFile;
        }
        boolean cloudUploadSuccess = false;
        CloudVo cloudVo = new CloudVo();
        String fileIsSave = systemConfigService.getValueByKeyException(SysConfigConstants.CONFIG_FILE_IS_SAVE);
        multipartFile.transferTo(file);
        try {
            switch (uploadTypeInt) {
                case 2:
                    systemAttachment.setImageType(2);
                    cloudVo.setDomain(systemConfigService.getValueByKeyException(SysConfigConstants.CONFIG_QN_UPLOAD_URL));
                    cloudVo.setAccessKey(systemConfigService.getValueByKeyException(SysConfigConstants.CONFIG_QN_ACCESS_KEY));
                    cloudVo.setSecretKey(systemConfigService.getValueByKeyException(SysConfigConstants.CONFIG_QN_SECRET_KEY));
                    cloudVo.setBucketName(systemConfigService.getValueByKeyException(SysConfigConstants.CONFIG_QN_STORAGE_NAME));
                    cloudVo.setRegion(systemConfigService.getValueByKeyException(SysConfigConstants.CONFIG_QN_STORAGE_REGION));
                    {
                        Configuration cfg = new Configuration(Region.autoRegion());
                        UploadManager uploadManager = new UploadManager(cfg);
                        Auth auth = Auth.create(cloudVo.getAccessKey(), cloudVo.getSecretKey());
                        String upToken = auth.uploadToken(cloudVo.getBucketName());
                        String webPathQn = crmebConfig.getImagePath();
                        qiNiuService.uploadFile(uploadManager, upToken,
                                systemAttachment.getSattDir(), webPathQn + "/" + systemAttachment.getSattDir(), file);
                        cloudUploadSuccess = true;
                    }
                    break;
                case 3:
                    systemAttachment.setImageType(3);
                    cloudVo.setDomain(systemConfigService.getValueByKeyException(SysConfigConstants.CONFIG_AL_UPLOAD_URL));
                    cloudVo.setAccessKey(systemConfigService.getValueByKeyException(SysConfigConstants.CONFIG_AL_ACCESS_KEY));
                    cloudVo.setSecretKey(systemConfigService.getValueByKeyException(SysConfigConstants.CONFIG_AL_SECRET_KEY));
                    cloudVo.setBucketName(systemConfigService.getValueByKeyException(SysConfigConstants.CONFIG_AL_STORAGE_NAME));
                    cloudVo.setRegion(systemConfigService.getValueByKeyException(SysConfigConstants.CONFIG_AL_STORAGE_REGION));
                    {
                        String webPathAl = crmebConfig.getImagePath();
                        ossService.upload(cloudVo, systemAttachment.getSattDir(), webPathAl + "/" + systemAttachment.getSattDir(), file);
                        cloudUploadSuccess = true;
                    }
                    break;
                case 4:
                    systemAttachment.setImageType(4);
                    cloudVo.setDomain(systemConfigService.getValueByKeyException(SysConfigConstants.CONFIG_TX_UPLOAD_URL));
                    cloudVo.setAccessKey(systemConfigService.getValueByKeyException(SysConfigConstants.CONFIG_TX_ACCESS_KEY));
                    cloudVo.setSecretKey(systemConfigService.getValueByKeyException(SysConfigConstants.CONFIG_TX_SECRET_KEY));
                    cloudVo.setBucketName(systemConfigService.getValueByKeyException(SysConfigConstants.CONFIG_TX_STORAGE_NAME));
                    cloudVo.setRegion(systemConfigService.getValueByKeyException(SysConfigConstants.CONFIG_TX_STORAGE_REGION));
                    {
                        COSCredentials cred = new BasicCOSCredentials(cloudVo.getAccessKey(), cloudVo.getSecretKey());
                        ClientConfig clientConfig = new ClientConfig(new com.qcloud.cos.region.Region(cloudVo.getRegion()));
                        COSClient cosClient = new COSClient(cred, clientConfig);
                        try {
                            String webPathTx = crmebConfig.getImagePath();
                            cosService.uploadFile(cloudVo, systemAttachment.getSattDir(), webPathTx + "/" + systemAttachment.getSattDir(), systemAttachment.getAttId(), cosClient);
                            cloudUploadSuccess = true;
                        } finally {
                            cosClient.shutdown();
                        }
                    }
                    break;
                case 5:
                    systemAttachment.setImageType(5);
                    {
                        String bucket = systemConfigService.getValueByKeyException(SysConfigConstants.CONFIG_JD_BUCKET_NAME);
                        String webPathTx = crmebConfig.getImagePath();
                        jdCloudService.uploadFile(systemAttachment.getSattDir(), webPathTx + "/" + systemAttachment.getSattDir(), bucket);
                        cloudUploadSuccess = true;
                    }
                    break;
                default:
                    throw new CrmebException("未知的上传类型：" + uploadTypeInt);
            }
        } catch (Exception e) {
            logger.error("云存储上传失败，uploadType={}，error={}", uploadTypeInt, e.getMessage(), e);
            systemAttachment.setImageType(1);
            cloudUploadSuccess = false;
        }
        systemAttachmentService.save(systemAttachment);
        if (cloudUploadSuccess && !fileIsSave.equals("1")) {
            file.delete();
        }
        return resultFile;
    }
}

