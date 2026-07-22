package com.service;

import com.utils.PageUtils;
import com.entity.GonggaoEntity;
import com.entity.view.GonggaoView;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 公告信息 服务类
 */
public interface GonggaoService extends LegacyIService<GonggaoEntity> {

    PageUtils queryPage(Map<String, Object> params, HttpServletRequest request);

    GonggaoView getViewById(Long id, HttpServletRequest request);

    void saveWithDefaults(GonggaoEntity entity, HttpServletRequest request);

    void updateSanitized(GonggaoEntity entity);
}
