package com.zbkj.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbkj.common.model.about.AboutPageBlock;

/**
 * 关于页内容块 DAO

 * 继承 MyBatis-Plus 的 BaseMapper，自动拥有基础的增删改查能力
 * 因为本模块逻辑简单（6条固定记录的查和改），不需要自定义SQL
 */
public interface AboutPageBlockDao extends BaseMapper<AboutPageBlock> {
}
