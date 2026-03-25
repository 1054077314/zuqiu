package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.ShujuEntity;
import com.entity.view.ShujuView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 球员数据 服务类
 */
public interface ShujuService extends IService<ShujuEntity> {


    PageUtils queryPage(Map<String, Object> params, HttpServletRequest request);

    ShujuView getViewById(Long id, HttpServletRequest request);

    void saveWithDefaults(ShujuEntity entity, HttpServletRequest request);

    void updateSanitized(ShujuEntity entity);

    void softDeleteBatch(Integer[] ids);

}
