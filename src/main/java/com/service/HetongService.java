package com.service;

import com.utils.PageUtils;
import com.entity.HetongEntity;
import com.entity.view.HetongView;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 合同 服务类
 */
public interface HetongService extends LegacyIService<HetongEntity> {


    PageUtils queryPage(Map<String, Object> params, HttpServletRequest request);

    HetongView getViewById(Long id, HttpServletRequest request);

    void saveWithDefaults(HetongEntity entity, HttpServletRequest request);

    void updateSanitized(HetongEntity entity);

    void softDeleteBatch(Integer[] ids);

}
