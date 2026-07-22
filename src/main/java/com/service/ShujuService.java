package com.service;

import com.utils.PageUtils;
import com.entity.ShujuEntity;
import com.entity.view.ShujuView;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 球员数据 服务类
 */
public interface ShujuService extends LegacyIService<ShujuEntity> {


    PageUtils queryPage(Map<String, Object> params, HttpServletRequest request);

    ShujuView getViewById(Long id, HttpServletRequest request);

    void saveWithDefaults(ShujuEntity entity, HttpServletRequest request);

    void updateSanitized(ShujuEntity entity);

    void softDeleteBatch(Integer[] ids);

}
