package com.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.entity.ConfigEntity;
import com.service.ConfigService;
import com.utils.PageUtils;
import com.utils.R;

/**
 * 系统配置
 */
@RequestMapping("config")
@RestController
public class ConfigController {

    @Autowired
    private ConfigService configService;

    /**
     * 分页列表
     */
    @GetMapping("/page")
    public R page(@RequestParam Map<String, Object> params) {
        PageUtils page = configService.queryPage(params);
        return R.ok().put("data", page);
    }

    /**
     * 详情
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") String id) {
        ConfigEntity config = configService.selectById(id);
        return R.ok().put("data", config);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody ConfigEntity config) {
        configService.insert(config);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody ConfigEntity config) {
        configService.updateById(config);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        configService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}
