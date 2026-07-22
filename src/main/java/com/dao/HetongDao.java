package com.dao;

import com.entity.HetongEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.entity.view.HetongView;

/**
 * 合同 Dao 接口
 *
 * @author 
 */
public interface HetongDao extends BaseMapper<HetongEntity> {

   List<HetongView> selectListView(Page<?> page,@Param("params")Map<String,Object> params);

}
