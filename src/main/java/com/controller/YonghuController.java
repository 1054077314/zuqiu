package com.controller;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.alibaba.fastjson.JSONObject;
import com.annotation.IgnoreAuth;
import com.entity.YonghuEntity;
import com.entity.view.YonghuView;
import com.service.DictionaryService;
import com.service.YonghuService;
import com.utils.PageUtils;
import com.utils.R;

/**
 * 用户
 * 后端接口
 */
@RestController
@RequestMapping("/yonghu")
public class YonghuController {
    private static final Logger logger = LoggerFactory.getLogger(YonghuController.class);

    @Autowired
    private YonghuService yonghuService;

    @Autowired
    private DictionaryService dictionaryService;

    @GetMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        logger.debug("page方法:,,Controller:{},,params:{}", this.getClass().getName(), JSONObject.toJSONString(params));
        PageUtils page = yonghuService.queryPage(params, request);
        return R.ok().put("data", page);
    }


    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request) {
        logger.debug("info方法:,,Controller:{},,id:{}", this.getClass().getName(), id);
        YonghuView view = yonghuService.getViewById(id, request);
        if (view != null) {
            return R.ok().put("data", view);
        }
        return R.error(511, "未找到数据");
    }

    @PostMapping("/save")
    public R save(@RequestBody YonghuEntity yonghu, HttpServletRequest request) {
        logger.debug("save方法:,,Controller:{},,yonghu:{}", this.getClass().getName(), yonghu.toString());
        yonghuService.saveWithDefaults(yonghu, request);
        return R.ok();
    }

    @PostMapping("/update")
    public R update(@RequestBody YonghuEntity yonghu, HttpServletRequest request) {
        logger.debug("update方法:,,Controller:{},,yonghu:{}", this.getClass().getName(), yonghu.toString());
        yonghuService.updateSanitized(yonghu);
        return R.ok();
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request) {
        logger.debug("delete:,,Controller:{},,ids:{}", this.getClass().getName(), Arrays.toString(ids));
        yonghuService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @IgnoreAuth
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public R login(String username, String password, String captcha, HttpServletRequest request) {
        return yonghuService.login(username, password);
    }

    @IgnoreAuth
    @PostMapping(value = "/register")
    public R register(@RequestBody YonghuEntity yonghu, HttpServletRequest request) {
        yonghuService.register(yonghu);
        return R.ok();
    }

    @GetMapping(value = "/resetPassword")
    public R resetPassword(Integer id, HttpServletRequest request) {
        yonghuService.resetPassword(id);
        return R.ok();
    }

    @GetMapping("/session")
    public R getCurrYonghu(HttpServletRequest request, Integer id) {
        YonghuEntity yonghu = yonghuService.getCurrUser(request);
        if (yonghu == null && id != null) {
            yonghu = yonghuService.selectById(id);
        }
        if (yonghu != null) {
            YonghuView view = new YonghuView();
            BeanUtils.copyProperties(yonghu, view);
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }
        return R.error(511, "未找到数据");
    }
}
