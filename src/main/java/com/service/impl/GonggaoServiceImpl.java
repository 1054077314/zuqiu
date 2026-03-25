package com.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dao.GonggaoDao;
import com.entity.GonggaoEntity;
import com.entity.view.GonggaoView;
import com.service.GonggaoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 公告信息 服务实现类
 */
@Service("gonggaoService")
@Transactional
public class GonggaoServiceImpl extends BaseService<GonggaoDao, GonggaoEntity, GonggaoView> implements GonggaoService {

    @Override
    protected List<GonggaoView> selectListView(Page<GonggaoView> page, Map<String, Object> params) {
        return baseMapper.selectListView(page, params);
    }

    @Override
    protected GonggaoView toView(GonggaoEntity entity) {
        GonggaoView view = new GonggaoView();
        BeanUtils.copyProperties(entity, view);
        return view;
    }

    @Override
    protected GonggaoEntity checkUniqueness(GonggaoEntity entity) {
        Wrapper<GonggaoEntity> queryWrapper = new EntityWrapper<GonggaoEntity>()
                .eq("gonggao_name", entity.getGonggaoName())
                .eq("gonggao_types", entity.getGonggaoTypes());
        return selectOne(queryWrapper);
    }

    @Override
    protected void sanitizeFields(GonggaoEntity entity) {
        if ("".equals(entity.getGonggaoPhoto()) || "null".equals(entity.getGonggaoPhoto())) {
            entity.setGonggaoPhoto(null);
        }
        if ("".equals(entity.getGonggaoContent()) || "null".equals(entity.getGonggaoContent())) {
            entity.setGonggaoContent(null);
        }
    }

    @Override
    protected void initEntityForInsert(GonggaoEntity entity) {
        super.initEntityForInsert(entity);
        entity.setCreateTime(new Date());
        entity.setInsertTime(new Date());
    }
}
