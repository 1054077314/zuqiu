package com.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dao.XunlianDao;
import com.entity.XunlianEntity;
import com.entity.YonghuEntity;
import com.entity.view.XunlianView;
import com.service.XunlianService;
import com.service.YonghuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 训练计划 服务实现类
 */
@Service("xunlianService")
@Transactional
public class XunlianServiceImpl extends BaseService<XunlianDao, XunlianEntity, XunlianView> implements XunlianService {

    @Autowired
    private YonghuService yonghuService;

    @Override
    protected List<XunlianView> selectListView(Page<XunlianView> page, Map<String, Object> params) {
        return baseMapper.selectListView(page, params);
    }

    @Override
    protected XunlianView toView(XunlianEntity entity) {
        XunlianView view = new XunlianView();
        BeanUtils.copyProperties(entity, view);
        // 关联用户信息
        YonghuEntity yonghu = yonghuService.selectById(entity.getYonghuId());
        if (yonghu != null) {
            BeanUtils.copyProperties(yonghu, view,
                    new String[]{"id", "createTime", "insertTime", "updateTime", "username", "password", "newMoney", "yonghuId"});
            view.setYonghuId(yonghu.getId());
        }
        return view;
    }

    @Override
    protected XunlianEntity checkUniqueness(XunlianEntity entity) {
        Wrapper<XunlianEntity> queryWrapper = new EntityWrapper<XunlianEntity>()
                .eq("yonghu_id", entity.getYonghuId())
                .eq("xunlian_name", entity.getXunlianName())
                .eq("xunlian_uuid_number", entity.getXunlianUuidNumber())
                .eq("xunlian_types", entity.getXunlianTypes())
                .eq("xunlian_kemu", entity.getXunlianKemu())
                .eq("xunlian_delete", 1);
        return selectOne(queryWrapper);
    }

    @Override
    protected void setDeleteFilterParams(Map<String, Object> params) {
        params.put("xunlianDeleteStart", 1);
        params.put("xunlianDeleteEnd", 1);
    }

    @Override
    protected void applyRoleFilter(XunlianEntity entity, HttpServletRequest request) {
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if ("用户".equals(role)) {
            entity.setYonghuId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));
        }
    }

    @Override
    protected void sanitizeFields(XunlianEntity entity) {
        if ("".equals(entity.getXunlianPhoto()) || "null".equals(entity.getXunlianPhoto())) {
            entity.setXunlianPhoto(null);
        }
        if ("".equals(entity.getXunlianContent()) || "null".equals(entity.getXunlianContent())) {
            entity.setXunlianContent(null);
        }
    }

    @Override
    protected void initEntityForInsert(XunlianEntity entity) {
        super.initEntityForInsert(entity);
        entity.setCreateTime(new Date());
        entity.setXunlianDelete(1);
        entity.setInsertTime(new Date());
    }

    @Override
    public void softDeleteBatch(Integer[] ids) {
        ArrayList<XunlianEntity> list = new ArrayList<>();
        for (Integer id : ids) {
            XunlianEntity entity = new XunlianEntity();
            entity.setId(id);
            entity.setXunlianDelete(2);
            list.add(entity);
        }
        if (!list.isEmpty()) {
            updateBatchById(list);
        }
    }
}
