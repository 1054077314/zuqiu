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
import com.entity.JiaolianEntity;
import com.entity.view.JiaolianView;
import com.service.DictionaryService;
import com.service.JiaolianService;
import com.utils.PageUtils;
import com.utils.R;

/**
 * 教练
 * 后端接口
 */
@RestController
@RequestMapping("/jiaolian")
public class JiaolianController {
    private static final Logger logger = LoggerFactory.getLogger(JiaolianController.class);

    @Autowired
    private JiaolianService jiaolianService;

    @Autowired
    private DictionaryService dictionaryService;

    @GetMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        logger.debug("page方法:,,Controller:{},,params:{}", this.getClass().getName(), JSONObject.toJSONString(params));
        PageUtils page = jiaolianService.queryPage(params, request);
        return R.ok().put("data", page);
    }


    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request) {
        logger.debug("info方法:,,Controller:{},,id:{}", this.getClass().getName(), id);
        JiaolianView view = jiaolianService.getViewById(id, request);
        if (view != null) {
            return R.ok().put("data", view);
        }
        return R.error(511, "未找到数据");
    }

    @PostMapping("/save")
    public R save(@RequestBody JiaolianEntity jiaolian, HttpServletRequest request) {
        logger.debug("save方法:,,Controller:{},,jiaolian:{}", this.getClass().getName(), jiaolian.toString());
        jiaolianService.saveWithDefaults(jiaolian, request);
        return R.ok();
    }

    @PostMapping("/update")
    public R update(@RequestBody JiaolianEntity jiaolian, HttpServletRequest request) {
        logger.debug("update方法:,,Controller:{},,jiaolian:{}", this.getClass().getName(), jiaolian.toString());
        jiaolianService.updateSanitized(jiaolian);
        return R.ok();
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request) {
        logger.debug("delete:,,Controller:{},,ids:{}", this.getClass().getName(), Arrays.toString(ids));
        jiaolianService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @IgnoreAuth
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public R login(String username, String password, String captcha, HttpServletRequest request) {
        return jiaolianService.login(username, password);
    }

    @IgnoreAuth
    @PostMapping(value = "/register")
    public R register(@RequestBody JiaolianEntity jiaolian, HttpServletRequest request) {
        jiaolianService.register(jiaolian);
        return R.ok();
    }

    @GetMapping(value = "/resetPassword")
    public R resetPassword(Integer id, HttpServletRequest request) {
        jiaolianService.resetPassword(id);
        return R.ok();
    }

    @GetMapping("/session")
    public R getCurrJiaolian(HttpServletRequest request, Integer id) {
        JiaolianEntity jiaolian = jiaolianService.getCurrUser(request);
        if (jiaolian == null && id != null) {
            jiaolian = jiaolianService.selectById(id);
        }
        if (jiaolian != null) {
            JiaolianView view = new JiaolianView();
            BeanUtils.copyProperties(jiaolian, view);
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }
        return R.error(511, "未找到数据");
    }
}
