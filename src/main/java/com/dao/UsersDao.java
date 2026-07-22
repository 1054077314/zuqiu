package com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.UsersEntity;

/**
 * 系统用户
 */
public interface UsersDao extends BaseMapper<UsersEntity> {

    List<UsersEntity> selectListView(Page<?> page, @Param("ew") QueryWrapper<UsersEntity> wrapper);
}
