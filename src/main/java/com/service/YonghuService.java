package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.utils.R;
import com.entity.YonghuEntity;
import com.entity.view.YonghuView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户 服务类
 */
public interface YonghuService extends IService<YonghuEntity> {

    PageUtils queryPage(Map<String, Object> params, HttpServletRequest request);

    YonghuView getViewById(Long id, HttpServletRequest request);

    void saveWithDefaults(YonghuEntity entity, HttpServletRequest request);

    void updateSanitized(YonghuEntity entity);

    R login(String username, String password);

    void register(YonghuEntity yonghu);

    void resetPassword(Integer id);

    YonghuEntity getCurrUser(HttpServletRequest request);
}
