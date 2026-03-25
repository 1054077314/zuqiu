package com.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dao.SaishiDao;
import com.entity.SaishiEntity;
import com.entity.view.SaishiView;
import com.service.SaishiService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 璧涗簨 鏈嶅姟瀹炵幇绫?
 */
@Service("saishiService")
@Transactional
public class SaishiServiceImpl extends BaseService<SaishiDao, SaishiEntity, SaishiView> implements SaishiService {

    @Override
    protected List<SaishiView> selectListView(Page<SaishiView> page, Map<String, Object> params) {
        return baseMapper.selectListView(page, params);
    }

    @Override
    protected SaishiView toView(SaishiEntity entity) {
        SaishiView view = new SaishiView();
        BeanUtils.copyProperties(entity, view);
        return view;
    }

    @Override
    protected SaishiEntity checkUniqueness(SaishiEntity entity) {
        Wrapper<SaishiEntity> queryWrapper = new EntityWrapper<SaishiEntity>()
                .eq("saishi_name", entity.getSaishiName())
                .eq("saishi_address", entity.getSaishiAddress())                .eq("saishi_types", entity.getSaishiTypes())
                .eq("saishi_delete", 1);
        return selectOne(queryWrapper);
    }

    @Override
    protected void setDeleteFilterParams(Map<String, Object> params) {
        params.put("saishiDeleteStart", 1);
        params.put("saishiDeleteEnd", 1);
    }

    @Override
    protected void sanitizeFields(SaishiEntity entity) {
        if ("".equals(entity.getSaishiPhoto()) || "null".equals(entity.getSaishiPhoto())) {
            entity.setSaishiPhoto(null);
        }       if ("".equals(entity.getSaishiContent()) || "null".equals(entity.getSaishiContent())) {
            entity.setSaishiContent(null);
        }
    }

    @Override
    protected void initEntityForInsert(SaishiEntity entity) {
        super.initEntityForInsert(entity);
        entity.setCreateTime(new Date());
        entity.setSaishiDelete(1);
        entity.setInsertTime(new Date());
    }

    @Override
    public void softDeleteBatch(Integer[] ids) {
        ArrayList<SaishiEntity> list = new ArrayList<>();
        for (Integer id : ids) {
            SaishiEntity entity = new SaishiEntity();
            entity.setId(id);
            entity.setSaishiDelete(2);
            list.add(entity);
        }
        if (!list.isEmpty()) {
            updateBatchById(list);
        }
    }
}

