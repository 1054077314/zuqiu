package com.service;

import com.utils.PageUtils;
import com.entity.SaishiEntity;
import com.entity.view.SaishiView;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 赛事 服务类
 */
public interface SaishiService extends LegacyIService<SaishiEntity> {

    PageUtils queryPage(Map<String, Object> params, HttpServletRequest request);

    SaishiView getViewById(Long id, HttpServletRequest request);

    void saveWithDefaults(SaishiEntity entity, HttpServletRequest request);

    void updateSanitized(SaishiEntity entity);

    void softDeleteBatch(Integer[] ids);
}
