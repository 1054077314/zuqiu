package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.SaishiEntity;
import com.entity.view.SaishiView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 赛事 服务类
 */
public interface SaishiService extends IService<SaishiEntity> {

    PageUtils queryPage(Map<String, Object> params, HttpServletRequest request);

    SaishiView getViewById(Long id, HttpServletRequest request);

    void saveWithDefaults(SaishiEntity entity, HttpServletRequest request);

    void updateSanitized(SaishiEntity entity);

    void softDeleteBatch(Integer[] ids);
}
