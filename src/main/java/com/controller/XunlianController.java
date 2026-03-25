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
import com.entity.XunlianEntity;
import com.entity.view.XunlianView;
import com.service.XunlianService;
import com.utils.PageUtils;
import com.utils.R;

/**
 * 训练计划
 * 后端接口
 */
@RestController
@RequestMapping("/xunlian")
public class XunlianController {
    private static final Logger logger = LoggerFactory.getLogger(XunlianController.class);

    @Autowired
    private XunlianService xunlianService;

    @GetMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        logger.debug("page方法:,,Controller:{},,params:{}", this.getClass().getName(), JSONObject.toJSONString(params));
        PageUtils page = xunlianService.queryPage(params, request);
        return R.ok().put("data", page);
    }


    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request) {
        logger.debug("info方法:,,Controller:{},,id:{}", this.getClass().getName(), id);
        XunlianView view = xunlianService.getViewById(id, request);
        if (view != null) {
            return R.ok().put("data", view);
        }
        return R.error(511, "未找到数据");
    }

    @PostMapping("/save")
    public R save(@RequestBody XunlianEntity xunlian, HttpServletRequest request) {
        logger.debug("save方法:,,Controller:{},,xunlian:{}", this.getClass().getName(), xunlian.toString());
        xunlianService.saveWithDefaults(xunlian, request);
        return R.ok();
    }

    @PostMapping("/update")
    public R update(@RequestBody XunlianEntity xunlian, HttpServletRequest request) {
        logger.debug("update方法:,,Controller:{},,xunlian:{}", this.getClass().getName(), xunlian.toString());
        xunlianService.updateSanitized(xunlian);
        return R.ok();
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request) {
        logger.debug("delete:,,Controller:{},,ids:{}", this.getClass().getName(), Arrays.toString(ids));
        xunlianService.softDeleteBatch(ids);
        return R.ok();
    }
}
