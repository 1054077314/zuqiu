package com.service;

import java.util.Map;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.UsersEntity;
import com.utils.PageUtils;

/**
 * 系统用户
 */
public interface UsersService extends IService<UsersEntity> {
    PageUtils queryPage(Map<String, Object> params, Wrapper<UsersEntity> wrapper);
}
