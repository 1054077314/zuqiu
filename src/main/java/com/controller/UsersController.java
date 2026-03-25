package com.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.UsersEntity;
import com.service.TokenService;
import com.service.UsersService;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;

/**
 * 登录相关
 */
@RequestMapping("users")
@RestController
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private TokenService tokenService;

    @IgnoreAuth
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public R login(String username, String password, String captcha, HttpServletRequest request) {
        UsersEntity user = usersService.selectOne(new EntityWrapper<UsersEntity>().eq("username", username));
        if (user == null || !user.getPassword().equals(password)) {
            return R.error("账号或密码不正确");
        }
        String token = tokenService.generateToken(user.getId(), username, "users", user.getRole());
        R r = R.ok();
        r.put("token", token);
        r.put("role", user.getRole());
        r.put("userId", user.getId());
        return r;
    }

    @GetMapping("/page")
    public R page(@RequestParam Map<String, Object> params, UsersEntity user) {
        EntityWrapper<UsersEntity> ew = new EntityWrapper<UsersEntity>();
        PageUtils page = usersService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.allLike(ew, user), params), params));
        return R.ok().put("data", page);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") String id) {
        UsersEntity user = usersService.selectById(id);
        return R.ok().put("data", user);
    }

    @GetMapping("/session")
    public R getCurrUser(HttpServletRequest request) {
        Integer id = (Integer) request.getSession().getAttribute("userId");
        UsersEntity user = usersService.selectById(id);
        return R.ok().put("data", user);
    }

    @PostMapping("/save")
    public R save(@RequestBody UsersEntity user) {
        if (usersService.selectOne(new EntityWrapper<UsersEntity>().eq("username", user.getUsername())) != null) {
            return R.error("账号已存在");
        }
        usersService.insert(user);
        return R.ok();
    }

    @PostMapping("/update")
    public R update(@RequestBody UsersEntity user) {
        usersService.updateById(user);
        return R.ok();
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        EntityWrapper<UsersEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("role", "管理员");
        List<UsersEntity> admins = usersService.selectList(wrapper);
        if (admins.size() > 1) {
            usersService.deleteBatchIds(Arrays.asList(ids));
            return R.ok();
        }
        return R.error("至少保留一个管理员账号");
    }
}
