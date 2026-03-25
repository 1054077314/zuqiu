package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.GonggaoEntity;
import com.entity.view.GonggaoView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 公告信息 服务类
 */
public interface GonggaoService extends IService<GonggaoEntity> {

    PageUtils queryPage(Map<String, Object> params, HttpServletRequest request);

    GonggaoView getViewById(Long id, HttpServletRequest request);

    void saveWithDefaults(GonggaoEntity entity, HttpServletRequest request);

    void updateSanitized(GonggaoEntity entity);
}
