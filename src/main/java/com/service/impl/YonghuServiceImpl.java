package com.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.common.BusinessException;
import com.dao.YonghuDao;
import com.entity.TokenEntity;
import com.entity.YonghuEntity;
import com.entity.view.YonghuView;
import com.service.TokenService;
import com.service.YonghuService;
import com.utils.PageUtils;
import com.utils.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户 服务实现类
 */
@Service("yonghuService")
@Transactional
public class YonghuServiceImpl extends BaseService<YonghuDao, YonghuEntity, YonghuView> implements YonghuService {

    @Autowired
    private TokenService tokenService;

    @Override
    protected List<YonghuView> selectListView(Page<YonghuView> page, Map<String, Object> params) {
        return baseMapper.selectListView(page, params);
    }

    @Override
    protected YonghuView toView(YonghuEntity entity) {
        YonghuView view = new YonghuView();
        BeanUtils.copyProperties(entity, view);
        return view;
    }

    @Override
    protected YonghuEntity checkUniqueness(YonghuEntity entity) {
        Wrapper<YonghuEntity> queryWrapper = new EntityWrapper<YonghuEntity>()
                .eq("username", entity.getUsername())
                .or()
                .eq("yonghu_phone", entity.getYonghuPhone())
                .or()
                .eq("yonghu_id_number", entity.getYonghuIdNumber());
        return selectOne(queryWrapper);
    }

    @Override
    protected void sanitizeFields(YonghuEntity entity) {
        if ("".equals(entity.getYonghuPhoto()) || "null".equals(entity.getYonghuPhoto())) {
            entity.setYonghuPhoto(null);
        }
    }

    @Override
    protected void initEntityForInsert(YonghuEntity entity) {
        super.initEntityForInsert(entity);
        entity.setCreateTime(new Date());
        entity.setPassword("123456");
    }

    // ==================== 登录注册 ====================

    @Override
    public R login(String username, String password) {
        YonghuEntity yonghu = selectOne(new EntityWrapper<YonghuEntity>().eq("username", username));
        if (yonghu == null || !yonghu.getPassword().equals(password)) {
            return R.error("账号或密码不正确");
        }
        String token = tokenService.generateToken(yonghu.getId(), username, "yonghu", "用户");
        R r = R.ok();
        r.put("token", token);
        r.put("role", "用户");
        r.put("username", yonghu.getYonghuName());
        r.put("tableName", "yonghu");
        r.put("userId", yonghu.getId());
        return r;
    }

    @Override
    public void register(YonghuEntity yonghu) {
        // 唯一性校验
        YonghuEntity existing = checkUniqueness(yonghu);
        if (existing != null) {
            throw new BusinessException("账号或用户手机号或用户身份证号已经被使用", 511);
        }
        yonghu.setYonghuUuidNumber(String.valueOf(new Date().getTime()));
        yonghu.setCreateTime(new Date());
        insert(yonghu);
    }

    @Override
    public void resetPassword(Integer id) {
        YonghuEntity yonghu = selectById(id);
        if (yonghu == null) {
            throw new BusinessException("用户不存在", 511);
        }
        yonghu.setPassword("123456");
        updateById(yonghu);
    }

    @Override
    public YonghuEntity getCurrUser(HttpServletRequest request) {
        Integer id = (Integer) request.getSession().getAttribute("userId");
        TokenEntity tokenEntity = null;
        if (id == null) {
            String token = request.getHeader("Token");
            if (StringUtils.isNotBlank(token)) {
                tokenEntity = tokenService.getTokenEntity(token);
                if (tokenEntity != null) {
                    id = tokenEntity.getUserid();
                    request.getSession().setAttribute("userId", id);
                    request.getSession().setAttribute("role", tokenEntity.getRole());
                    request.getSession().setAttribute("tableName", tokenEntity.getTablename());
                    request.getSession().setAttribute("username", tokenEntity.getUsername());
                }
            }
        }
        if (id != null) {
            YonghuEntity yonghu = selectById(id);
            if (yonghu != null) {
                return yonghu;
            }
        }
        if (tokenEntity != null && StringUtils.isNotBlank(tokenEntity.getUsername())) {
            return selectOne(new EntityWrapper<YonghuEntity>().eq("username", tokenEntity.getUsername()));
        }
        return null;
    }
}
