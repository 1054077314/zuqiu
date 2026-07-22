package com.service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.UsersEntity;
import com.utils.PageUtils;

/**
 * 系统用户
 */
public interface UsersService extends LegacyIService<UsersEntity> {
    PageUtils queryPage(Map<String, Object> params, QueryWrapper<UsersEntity> wrapper);
}
