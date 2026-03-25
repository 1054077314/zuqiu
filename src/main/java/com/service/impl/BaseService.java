package com.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.common.BusinessException;
import com.service.DictionaryService;
import com.utils.CommonUtil;
import com.utils.PageUtils;
import com.utils.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service 基类，抽取公共逻辑
 *
 * 子类需要实现：
 * - selectListView(page, params)  — 调用各自的 baseMapper.selectListView
 * - toView(entity)               — 创建对应 View 对象并拷贝属性
 * - checkUniqueness(entity)      — 唯一性校验
 * - sanitizeFields(entity)       — 清理 ""/"null" 字符串字段
 * - initEntityForInsert(entity)  — 设置实体默认值（delete 字段、insertTime 等）
 *
 * @param <M> Mapper 类型 (extends BaseMapper)
 * @param <E> Entity 类型
 * @param <V> View 类型
 */
public abstract class BaseService<M extends BaseMapper<E>, E, V> extends ServiceImpl<M, E> {

    private static final Logger logger = LoggerFactory.getLogger(BaseService.class);

    @Autowired
    protected DictionaryService dictionaryService;

    // ==================== 子类必须实现的模板方法 ====================

    /**
     * 调用 baseMapper.selectListView 分页查询
     */
    protected abstract List<V> selectListView(Page<V> page, Map<String, Object> params);

    /**
     * 创建 View 对象并从 Entity 拷贝属性
     */
    protected abstract V toView(E entity);

    /**
     * 唯一性校验，返回已存在的记录（null 表示不重复）
     */
    protected abstract E checkUniqueness(E entity);

    // ==================== 子类可选覆盖的钩子方法 ====================

    /**
     * 清理字段中的 "" 和 "null" 为 null，子类按需覆盖
     */
    protected void sanitizeFields(E entity) {
    }

    /**
     * 设置新增时的默认值，子类按需覆盖
     */
    protected void initEntityForInsert(E entity) {
        // 子类自行设置 createTime
    }

    /**
     * 用户角色自动设置字段（如 yonghuId），子类按需覆盖
     */
    protected void applyRoleFilter(E entity, HttpServletRequest request) {
    }

    /**
     * 设置删除过滤参数，子类有软删除的覆盖此方法
     */
    protected void setDeleteFilterParams(Map<String, Object> params) {
    }

    // ==================== 公共业务方法 ====================

    /**
     * 分页查询 + 角色过滤 + 字典转换
     */
    public PageUtils queryPage(Map<String, Object> params, HttpServletRequest request) {
        // 角色条件过滤
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if ("用户".equals(role)) {
            params.put("yonghuId", request.getSession().getAttribute("userId"));
        } else if ("教练".equals(role)) {
            params.put("jiaolianId", request.getSession().getAttribute("userId"));
        }
        // 未删除过滤
        setDeleteFilterParams(params);
        CommonUtil.checkMap(params);
        // 分页查询
        Page<V> page = new Query<V>(params).getPage();
        page.setRecords(selectListView(page, params));
        PageUtils pageUtils = new PageUtils(page);
        // 字典表转换
        List<V> list = (List<V>) pageUtils.getList();
        for (V item : list) {
            dictionaryService.dictionaryConvert(item, request);
        }
        return pageUtils;
    }

    /**
     * 根据 ID 获取详情视图（含字典转换）
     */
    public V getViewById(Long id, HttpServletRequest request) {
        E entity = selectById(id);
        if (entity == null) {
            return null;
        }
        V view = toView(entity);
        dictionaryService.dictionaryConvert(view, request);
        return view;
    }

    /**
     * 保存（含唯一性校验 + 默认值）
     */
    public void saveWithDefaults(E entity, HttpServletRequest request) {
        // 用户角色自动赋值
        applyRoleFilter(entity, request);
        // 唯一性校验
        E existing = checkUniqueness(entity);
        if (existing != null) {
            throw new BusinessException("表中已存在相同数据", 511);
        }
        // 设置默认值
        initEntityForInsert(entity);
        insert(entity);
    }

    /**
     * 更新（含字段清理）
     */
    public void updateSanitized(E entity) {
        sanitizeFields(entity);
        updateById(entity);
    }

    /**
     * 批量软删除，子类有软删除需求的覆盖此方法
     */
    public void softDeleteBatch(Integer[] ids) {
        throw new BusinessException("该模块不支持软删除", 500);
    }
}
