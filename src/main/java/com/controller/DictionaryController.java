package com.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.alibaba.fastjson.JSONObject;
import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.DictionaryEntity;
import com.entity.view.DictionaryView;
import com.service.DictionaryService;
import com.utils.CommonUtil;
import com.utils.PageUtils;
import com.utils.R;

/**
 * 字典
 */
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {
    private static final Logger logger = LoggerFactory.getLogger(DictionaryController.class);

    @Autowired
    private DictionaryService dictionaryService;

    @GetMapping("/page")
    @IgnoreAuth
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        logger.debug("page方法:,,Controller:{},,params:{}", this.getClass().getName(), JSONObject.toJSONString(params));
        CommonUtil.checkMap(params);
        PageUtils page = dictionaryService.queryPage(params);
        List<DictionaryView> list = (List<DictionaryView>) page.getList();
        for (DictionaryView item : list) {
            dictionaryService.dictionaryConvert(item, request);
        }
        return R.ok().put("data", page);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request) {
        logger.debug("info方法:,,Controller:{},,id:{}", this.getClass().getName(), id);
        DictionaryEntity dictionary = dictionaryService.selectById(id);
        if (dictionary == null) {
            return R.error(511, "未找到数据");
        }
        DictionaryView view = new DictionaryView();
        BeanUtils.copyProperties(dictionary, view);
        dictionaryService.dictionaryConvert(view, request);
        return R.ok().put("data", view);
    }

    @PostMapping("/save")
    public R save(@RequestBody DictionaryEntity dictionary, HttpServletRequest request) {
        logger.debug("save方法:,,Controller:{},,dictionary:{}", this.getClass().getName(), dictionary.toString());

        Wrapper<DictionaryEntity> queryWrapper = new EntityWrapper<DictionaryEntity>()
            .eq("dic_code", dictionary.getDicCode())
            .eq("index_name", dictionary.getIndexName());
        if (dictionary.getDicCode().contains("_erji_types")) {
            queryWrapper.eq("super_id", dictionary.getSuperId());
        }

        logger.info("sql语句:{}", queryWrapper.getSqlSegment());
        DictionaryEntity dictionaryEntity = dictionaryService.selectOne(queryWrapper);
        if (dictionaryEntity != null) {
            return R.error(511, "表中已存在相同数据");
        }

        dictionary.setCreateTime(new Date());
        dictionaryService.insert(dictionary);
        refreshDictionaryMap(request);
        return R.ok();
    }

    @PostMapping("/update")
    public R update(@RequestBody DictionaryEntity dictionary, HttpServletRequest request) {
        logger.debug("update方法:,,Controller:{},,dictionary:{}", this.getClass().getName(), dictionary.toString());
        dictionaryService.updateById(dictionary);
        refreshDictionaryMap(request);
        return R.ok();
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request) {
        logger.debug("delete方法:,,Controller:{},,ids:{}", this.getClass().getName(), Arrays.toString(ids));
        dictionaryService.deleteBatchIds(Arrays.asList(ids));
        refreshDictionaryMap(request);
        return R.ok();
    }

    @PostMapping("/maxCodeIndex")
    public R maxCodeIndex(@RequestBody DictionaryEntity dictionary) {
        logger.debug("maxCodeIndex方法:,,Controller:{},,dictionary:{}", this.getClass().getName(), dictionary.toString());
        List<String> descs = new ArrayList<>();
        descs.add("code_index");
        Wrapper<DictionaryEntity> queryWrapper = new EntityWrapper<DictionaryEntity>()
            .eq("dic_code", dictionary.getDicCode())
            .orderDesc(descs);
        logger.info("sql语句:{}", queryWrapper.getSqlSegment());
        List<DictionaryEntity> dictionaryEntityList = dictionaryService.selectList(queryWrapper);
        if (dictionaryEntityList.size() > 0) {
            return R.ok().put("maxCodeIndex", dictionaryEntityList.get(0).getCodeIndex() + 1);
        }
        return R.ok().put("maxCodeIndex", 1);
    }

    private void refreshDictionaryMap(HttpServletRequest request) {
        List<DictionaryEntity> dictionaryEntities = dictionaryService.selectList(new EntityWrapper<DictionaryEntity>());
        ServletContext servletContext = request.getServletContext();
        Map<String, Map<Integer, String>> map = new HashMap<>();
        for (DictionaryEntity item : dictionaryEntities) {
            Map<Integer, String> valueMap = map.get(item.getDicCode());
            if (valueMap == null || valueMap.isEmpty()) {
                valueMap = new HashMap<>();
            }
            valueMap.put(item.getCodeIndex(), item.getIndexName());
            map.put(item.getDicCode(), valueMap);
        }
        servletContext.setAttribute("dictionaryMap", map);
    }
}
