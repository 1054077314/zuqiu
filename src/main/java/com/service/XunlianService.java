package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.XunlianEntity;
import com.entity.view.XunlianView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 训练计划 服务类
 */
public interface XunlianService extends IService<XunlianEntity> {

    /**
     * 分页查询（原有方法，保留兼容）
     */

    /**
     * 分页查询并做字典转换
     * 包含：角色条件过滤 + 未删除过滤 + 字典表转换
     */
    PageUtils queryPage(Map<String, Object> params, HttpServletRequest request);

    /**
     * 根据ID获取详情视图（含关联用户信息和字典转换）
     */
    XunlianView getViewById(Long id, HttpServletRequest request);

    /**
     * 保存训练计划（含角色自动赋值、唯一性校验和默认值）
     */
    void saveWithDefaults(XunlianEntity entity, HttpServletRequest request);

    /**
     * 更新训练计划（含字段清理）
     */
    void updateSanitized(XunlianEntity entity);

    /**
     * 批量软删除
     */
    void softDeleteBatch(Integer[] ids);

}
