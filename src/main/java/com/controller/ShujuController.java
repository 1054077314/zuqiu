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
import com.entity.ShujuEntity;
import com.entity.view.ShujuView;
import com.service.ShujuService;
import com.utils.PageUtils;
import com.utils.R;

/**
 * 球员数据
 * 后端接口
 */
@RestController
@RequestMapping("/shuju")
public class ShujuController {
    private static final Logger logger = LoggerFactory.getLogger(ShujuController.class);

    @Autowired
    private ShujuService shujuService;

    @GetMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        logger.debug("page方法:,,Controller:{},,params:{}", this.getClass().getName(), JSONObject.toJSONString(params));
        PageUtils page = shujuService.queryPage(params, request);
        return R.ok().put("data", page);
    }


    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request) {
        logger.debug("info方法:,,Controller:{},,id:{}", this.getClass().getName(), id);
        ShujuView view = shujuService.getViewById(id, request);
        if (view != null) {
            return R.ok().put("data", view);
        }
        return R.error(511, "未找到数据");
    }

    @PostMapping("/save")
    public R save(@RequestBody ShujuEntity shuju, HttpServletRequest request) {
        logger.debug("save方法:,,Controller:{},,shuju:{}", this.getClass().getName(), shuju.toString());
        shujuService.saveWithDefaults(shuju, request);
        return R.ok();
    }

    @PostMapping("/update")
    public R update(@RequestBody ShujuEntity shuju, HttpServletRequest request) {
        logger.debug("update方法:,,Controller:{},,shuju:{}", this.getClass().getName(), shuju.toString());
        shujuService.updateSanitized(shuju);
        return R.ok();
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request) {
        logger.debug("delete:,,Controller:{},,ids:{}", this.getClass().getName(), Arrays.toString(ids));
        shujuService.softDeleteBatch(ids);
        return R.ok();
    }
}
