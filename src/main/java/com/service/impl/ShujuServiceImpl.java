package com.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dao.ShujuDao;
import com.entity.ShujuEntity;
import com.entity.YonghuEntity;
import com.entity.view.ShujuView;
import com.service.ShujuService;
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
 * 球员数据 服务实现类
 */
@Service("shujuService")
@Transactional
public class ShujuServiceImpl extends BaseService<ShujuDao, ShujuEntity, ShujuView> implements ShujuService {

    @Autowired
    private YonghuService yonghuService;

    @Override
    protected List<ShujuView> selectListView(Page<ShujuView> page, Map<String, Object> params) {
        return baseMapper.selectListView(page, params);
    }

    @Override
    protected ShujuView toView(ShujuEntity entity) {
        ShujuView view = new ShujuView();
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
    protected ShujuEntity checkUniqueness(ShujuEntity entity) {
        Wrapper<ShujuEntity> queryWrapper = new EntityWrapper<ShujuEntity>()
                .eq("yonghu_id", entity.getYonghuId())
                .eq("shuju_name", entity.getShujuName())
                .eq("shuju_uuid_number", entity.getShujuUuidNumber())
                .eq("shuju_types", entity.getShujuTypes())
                .eq("shuju_delete", 1);
        return selectOne(queryWrapper);
    }

    @Override
    protected void setDeleteFilterParams(Map<String, Object> params) {
        params.put("shujuDeleteStart", 1);
        params.put("shujuDeleteEnd", 1);
    }

    @Override
    protected void applyRoleFilter(ShujuEntity entity, HttpServletRequest request) {
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if ("用户".equals(role)) {
            entity.setYonghuId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));
        }
    }

    @Override
    protected void sanitizeFields(ShujuEntity entity) {
        if ("".equals(entity.getShujuPhoto()) || "null".equals(entity.getShujuPhoto())) {
            entity.setShujuPhoto(null);
        }
        if ("".equals(entity.getShujuContent()) || "null".equals(entity.getShujuContent())) {
            entity.setShujuContent(null);
        }
    }

    @Override
    protected void initEntityForInsert(ShujuEntity entity) {
        super.initEntityForInsert(entity);
        entity.setCreateTime(new Date());
        entity.setShujuDelete(1);
        entity.setInsertTime(new Date());
    }

    @Override
    public void softDeleteBatch(Integer[] ids) {
        ArrayList<ShujuEntity> list = new ArrayList<>();
        for (Integer id : ids) {
            ShujuEntity entity = new ShujuEntity();
            entity.setId(id);
            entity.setShujuDelete(2);
            list.add(entity);
        }
        if (!list.isEmpty()) {
            updateBatchById(list);
        }
    }
}
