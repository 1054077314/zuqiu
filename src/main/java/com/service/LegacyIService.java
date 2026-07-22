package com.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Compatibility facade for legacy MyBatis-Plus 2.x service method names.
 */
public interface LegacyIService<T> extends IService<T> {

    default boolean insert(T entity) {
        return save(entity);
    }

    default T selectById(Serializable id) {
        return getById(id);
    }

    default T selectOne(Wrapper<T> queryWrapper) {
        return getOne(queryWrapper, false);
    }

    default List<T> selectList(Wrapper<T> queryWrapper) {
        return list(queryWrapper);
    }

    default Page<T> selectPage(Page<T> page, Wrapper<T> queryWrapper) {
        return page(page, queryWrapper);
    }

    default boolean deleteBatchIds(Collection<? extends Serializable> idList) {
        return removeByIds(idList);
    }

    default boolean insertOrUpdate(T entity) {
        return saveOrUpdate(entity);
    }
}
