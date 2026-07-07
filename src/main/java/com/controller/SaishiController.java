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
import com.annotation.IgnoreAuth;
import com.entity.SaishiEntity;
import com.entity.view.SaishiView;
import com.service.SaishiService;
import com.utils.PageUtils;
import com.utils.R;

/**
 * 赛事管理 Controller
 *
 * 接口路径: /saishi
 * 功能: 赛事信息的分页查询、详情查看、新增、修改、软删除
 * 技术点: MyBatis-Plus 分页 + XML 动态 SQL + 逻辑删除
 */
@RestController
@RequestMapping("/saishi")
public class SaishiController {
    private static final Logger logger = LoggerFactory.getLogger(SaishiController.class);

    @Autowired
    private SaishiService saishiService;

    /**
     * 分页查询赛事列表
     * GET /saishi/page?page=1&limit=10&saishiName=xxx&saishiTypes=1
     *
     * 支持条件: 赛事名称(模糊)、赛事地点(模糊)、赛事类型(精确)、录入时间范围
     * 排序: 默认按 id 降序，可通过 sort/order 参数自定义
     * 角色过滤: 登录用户只看到自己相关的数据
     */
    @IgnoreAuth
    @GetMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        logger.debug("page方法:,,Controller:{},,params:{}", this.getClass().getName(), JSONObject.toJSONString(params));
        PageUtils page = saishiService.queryPage(params, request);
        return R.ok().put("data", page);
    }

    /**
     * 查询赛事详情(含字典翻译后的类型名称)
     * GET /saishi/info/{id}
     */
    @IgnoreAuth
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request) {
        logger.debug("info方法:,,Controller:{},,id:{}", this.getClass().getName(), id);
        SaishiView view = saishiService.getViewById(id, request);
        if (view != null) {
            return R.ok().put("data", view);
        }
        return R.error(511, "未找到数据");
    }

    /**
     * 新增赛事
     * POST /saishi/save
     *
     * 处理: 自动设置创建时间、逻辑删除标记; 校验唯一性(名称+地点+类型)
     */
    @PostMapping("/save")
    public R save(@RequestBody SaishiEntity saishi, HttpServletRequest request) {
        logger.debug("save方法:,,Controller:{},,saishi:{}", this.getClass().getName(), saishi.toString());
        saishiService.saveWithDefaults(saishi, request);
        return R.ok();
    }

    /**
     * 修改赛事
     * POST /saishi/update
     *
     * 处理: 自动清理空字符串和"null"字面量为 null, 然后更新
     */
    @PostMapping("/update")
    public R update(@RequestBody SaishiEntity saishi, HttpServletRequest request) {
        logger.debug("update方法:,,Controller:{},,saishi:{}", this.getClass().getName(), saishi.toString());
        saishiService.updateSanitized(saishi);
        return R.ok();
    }

    /**
     * 批量软删除(逻辑删除, saishi_delete 置为 2)
     * POST /saishi/delete
     * Body: [1, 2, 3]
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request) {
        logger.debug("delete:,,Controller:{},,ids:{}", this.getClass().getName(), Arrays.toString(ids));
        saishiService.softDeleteBatch(ids);
        return R.ok();
    }
}
