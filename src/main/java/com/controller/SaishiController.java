package com.controller;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.entity.SaishiEntity;
import com.entity.view.SaishiView;
import com.service.SaishiService;
import com.utils.PageUtils;
import com.utils.R;

/**
 * 赛事
 * 后端接口
 */
@RestController
@RequestMapping("/saishi")
public class SaishiController {
    private static final Logger logger = LoggerFactory.getLogger(SaishiController.class);

    @Autowired
    private SaishiService saishiService;

    @GetMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        logger.debug("page方法:,,Controller:{},,params:{}", this.getClass().getName(), JSONObject.toJSONString(params));
        PageUtils page = saishiService.queryPage(params, request);
        return R.ok().put("data", page);
    }


    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request) {
        logger.debug("info方法:,,Controller:{},,id:{}", this.getClass().getName(), id);
        SaishiView view = saishiService.getViewById(id, request);
        if (view != null) {
            return R.ok().put("data", view);
        }
        return R.error(511, "未找到数据");
    }

    @PostMapping("/save")
    public R save(@RequestBody SaishiEntity saishi, HttpServletRequest request) {
        logger.debug("save方法:,,Controller:{},,saishi:{}", this.getClass().getName(), saishi.toString());
        saishiService.saveWithDefaults(saishi, request);
        return R.ok();
    }

    @PostMapping("/update")
    public R update(@RequestBody SaishiEntity saishi, HttpServletRequest request) {
        logger.debug("update方法:,,Controller:{},,saishi:{}", this.getClass().getName(), saishi.toString());
        saishiService.updateSanitized(saishi);
        return R.ok();
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request) {
        logger.debug("delete:,,Controller:{},,ids:{}", this.getClass().getName(), Arrays.toString(ids));
        saishiService.softDeleteBatch(ids);
        return R.ok();
    }
}
