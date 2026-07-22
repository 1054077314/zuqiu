package com.dao;

import com.entity.ShujuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.entity.view.ShujuView;

/**
 * 球员数据 Dao 接口
 *
 * @author 
 */
public interface ShujuDao extends BaseMapper<ShujuEntity> {

   List<ShujuView> selectListView(Page<?> page,@Param("params")Map<String,Object> params);

}
