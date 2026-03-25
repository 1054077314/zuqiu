package com.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.common.BusinessException;
import com.dao.JiaolianDao;
import com.entity.JiaolianEntity;
import com.entity.TokenEntity;
import com.entity.view.JiaolianView;
import com.service.JiaolianService;
import com.service.TokenService;
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
 * 教练 服务实现类
 */
@Service("jiaolianService")
@Transactional
public class JiaolianServiceImpl extends BaseService<JiaolianDao, JiaolianEntity, JiaolianView> implements JiaolianService {

    @Autowired
    private TokenService tokenService;

    @Override
    protected List<JiaolianView> selectListView(Page<JiaolianView> page, Map<String, Object> params) {
        return baseMapper.selectListView(page, params);
    }

    @Override
    protected JiaolianView toView(JiaolianEntity entity) {
        JiaolianView view = new JiaolianView();
        BeanUtils.copyProperties(entity, view);
        return view;
    }

    @Override
    protected JiaolianEntity checkUniqueness(JiaolianEntity entity) {
        Wrapper<JiaolianEntity> queryWrapper = new EntityWrapper<JiaolianEntity>()
                .eq("username", entity.getUsername())
                .or()
                .eq("jiaolian_phone", entity.getJiaolianPhone())
                .or()
                .eq("jiaolian_id_number", entity.getJiaolianIdNumber());
        return selectOne(queryWrapper);
    }

    @Override
    protected void sanitizeFields(JiaolianEntity entity) {
        if ("".equals(entity.getJiaolianPhoto()) || "null".equals(entity.getJiaolianPhoto())) {
            entity.setJiaolianPhoto(null);
        }
    }

    @Override
    protected void initEntityForInsert(JiaolianEntity entity) {
        super.initEntityForInsert(entity);
        entity.setCreateTime(new Date());
        entity.setPassword("123456");
    }

    // ==================== 登录注册 ====================

    @Override
    public R login(String username, String password) {
        JiaolianEntity jiaolian = selectOne(new EntityWrapper<JiaolianEntity>().eq("username", username));
        if (jiaolian == null || !jiaolian.getPassword().equals(password)) {
            return R.error("账号或密码不正确");
        }
        String token = tokenService.generateToken(jiaolian.getId(), username, "jiaolian", "教练");
        R r = R.ok();
        r.put("token", token);
        r.put("role", "教练");
        r.put("username", jiaolian.getJiaolianName());
        r.put("tableName", "jiaolian");
        r.put("userId", jiaolian.getId());
        return r;
    }

    @Override
    public void register(JiaolianEntity jiaolian) {
        // 唯一性校验
        JiaolianEntity existing = checkUniqueness(jiaolian);
        if (existing != null) {
            throw new BusinessException("账号或教练手机号或教练身份证号已经被使用", 511);
        }
        jiaolian.setJiaolianUuidNumber(String.valueOf(new Date().getTime()));
        jiaolian.setCreateTime(new Date());
        insert(jiaolian);
    }

    @Override
    public void resetPassword(Integer id) {
        JiaolianEntity jiaolian = selectById(id);
        if (jiaolian == null) {
            throw new BusinessException("教练不存在", 511);
        }
        jiaolian.setPassword("123456");
        updateById(jiaolian);
    }

    @Override
    public JiaolianEntity getCurrUser(HttpServletRequest request) {
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
            JiaolianEntity jiaolian = selectById(id);
            if (jiaolian != null) {
                return jiaolian;
            }
        }
        if (tokenEntity != null && StringUtils.isNotBlank(tokenEntity.getUsername())) {
            return selectOne(new EntityWrapper<JiaolianEntity>().eq("username", tokenEntity.getUsername()));
        }
        return null;
    }
}
