package com.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.UsersDao;
import com.entity.UsersEntity;
import com.service.UsersService;
import com.utils.PageUtils;
import com.utils.Query;

/**
 * 系统用户
 */
@Service("userService")
public class UsersServiceImpl extends ServiceImpl<UsersDao, UsersEntity> implements UsersService {

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<UsersEntity> wrapper) {
        Page<UsersEntity> page = new Query<UsersEntity>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        return new PageUtils(page);
    }
}
