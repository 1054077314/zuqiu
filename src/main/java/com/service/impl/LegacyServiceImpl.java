package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.service.LegacyIService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Compatibility base class for services migrated from MyBatis-Plus 2.x.
 */
public class LegacyServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements LegacyIService<T> {

    @Override
    public boolean insert(T entity) {
        return save(entity);
    }

    @Override
    public T selectById(Serializable id) {
        return getById(id);
    }

    @Override
    public T selectOne(Wrapper<T> queryWrapper) {
        return getOne(queryWrapper, false);
    }

    @Override
    public List<T> selectList(Wrapper<T> queryWrapper) {
        return list(queryWrapper);
    }

    @Override
    public Page<T> selectPage(Page<T> page, Wrapper<T> queryWrapper) {
        return page(page, queryWrapper);
    }

    @Override
    public boolean deleteBatchIds(Collection<? extends Serializable> idList) {
        return removeByIds(idList);
    }
}
