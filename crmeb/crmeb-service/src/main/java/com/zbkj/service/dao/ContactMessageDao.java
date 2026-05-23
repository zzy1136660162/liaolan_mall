package com.zbkj.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbkj.common.model.contact.ContactMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * 联系我们留言 Dao
 */
@Mapper
public interface ContactMessageDao extends BaseMapper<ContactMessage> {
}
