package com.dao;

import com.entity.XunlianEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.entity.view.XunlianView;

/**
 * 训练计划 Dao 接口
 *
 * @author 
 */
public interface XunlianDao extends BaseMapper<XunlianEntity> {

   List<XunlianView> selectListView(Page<?> page,@Param("params")Map<String,Object> params);

}
