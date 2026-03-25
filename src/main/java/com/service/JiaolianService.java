package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.utils.R;
import com.entity.JiaolianEntity;
import com.entity.view.JiaolianView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 教练 服务类
 */
public interface JiaolianService extends IService<JiaolianEntity> {

    PageUtils queryPage(Map<String, Object> params, HttpServletRequest request);

    JiaolianView getViewById(Long id, HttpServletRequest request);

    void saveWithDefaults(JiaolianEntity entity, HttpServletRequest request);

    void updateSanitized(JiaolianEntity entity);

    R login(String username, String password);

    void register(JiaolianEntity jiaolian);

    void resetPassword(Integer id);

    JiaolianEntity getCurrUser(HttpServletRequest request);
}
