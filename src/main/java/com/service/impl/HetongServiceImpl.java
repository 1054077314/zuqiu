package com.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dao.HetongDao;
import com.entity.HetongEntity;
import com.entity.YonghuEntity;
import com.entity.view.HetongView;
import com.service.HetongService;
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
 * 合同 服务实现类
 */
@Service("hetongService")
@Transactional
public class HetongServiceImpl extends BaseService<HetongDao, HetongEntity, HetongView> implements HetongService {

    @Autowired
    private YonghuService yonghuService;

    @Override
    protected List<HetongView> selectListView(Page<HetongView> page, Map<String, Object> params) {
        return baseMapper.selectListView(page, params);
    }

    @Override
    protected HetongView toView(HetongEntity entity) {
        HetongView view = new HetongView();
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
    protected HetongEntity checkUniqueness(HetongEntity entity) {
        Wrapper<HetongEntity> queryWrapper = new EntityWrapper<HetongEntity>()
                .eq("yonghu_id", entity.getYonghuId())
                .eq("hetong_name", entity.getHetongName())
                .eq("hetong_delete", 1);
        return selectOne(queryWrapper);
    }

    @Override
    protected void setDeleteFilterParams(Map<String, Object> params) {
        params.put("hetongDeleteStart", 1);
        params.put("hetongDeleteEnd", 1);
    }

    @Override
    protected void applyRoleFilter(HetongEntity entity, HttpServletRequest request) {
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if ("用户".equals(role)) {
            entity.setYonghuId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));
        }
    }

    @Override
    protected void sanitizeFields(HetongEntity entity) {
        if ("".equals(entity.getHetongFile()) || "null".equals(entity.getHetongFile())) {
            entity.setHetongFile(null);
        }
        if ("".equals(entity.getHetongText()) || "null".equals(entity.getHetongText())) {
            entity.setHetongText(null);
        }
    }

    @Override
    protected void initEntityForInsert(HetongEntity entity) {
        super.initEntityForInsert(entity);
        entity.setCreateTime(new Date());
        entity.setHetongDelete(1);
    }

    @Override
    public void softDeleteBatch(Integer[] ids) {
        ArrayList<HetongEntity> list = new ArrayList<>();
        for (Integer id : ids) {
            HetongEntity entity = new HetongEntity();
            entity.setId(id);
            entity.setHetongDelete(2);
            list.add(entity);
        }
        if (!list.isEmpty()) {
            updateBatchById(list);
        }
    }
}
