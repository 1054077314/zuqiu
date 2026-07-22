package com.dao;

import com.entity.GonggaoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.entity.view.GonggaoView;

/**
 * 公告信息 Dao 接口
 *
 * @author 
 */
public interface GonggaoDao extends BaseMapper<GonggaoEntity> {

   List<GonggaoView> selectListView(Page<?> page,@Param("params")Map<String,Object> params);

}
