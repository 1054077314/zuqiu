package com.service;

import com.utils.PageUtils;
import com.entity.DictionaryEntity;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import java.util.List;

/**
 * 字典 服务类
 */
public interface DictionaryService extends LegacyIService<DictionaryEntity> {

    /**
    * @param params 查询参数
    * @return 带分页的查询出来的数据
    */
     PageUtils queryPage(Map<String, Object> params);
      /**
      * 字典表转换
      * @param obj
      */
     void dictionaryConvert(Object obj, HttpServletRequest request);

}