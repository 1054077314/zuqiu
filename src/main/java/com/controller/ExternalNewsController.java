package com.controller;

import com.annotation.IgnoreAuth;
import com.utils.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("external")
public class ExternalNewsController {

    @IgnoreAuth
    @GetMapping("/realmadrid/news")
    public R realmadridNews(@RequestParam(value = "limit", defaultValue = "4") Integer limit) {
        int size = Math.max(1, Math.min(limit == null ? 4 : limit, 12));

        List<Map<String, Object>> local = new ArrayList<Map<String, Object>>();
        local.add(new HashMap<String, Object>() {{
            put("title", "皇马主场取胜，联赛积分继续领跑");
            put("link", "#");
            put("time", "2026-03-01");
            put("image", "");
            put("category", "Football");
        }});
        local.add(new HashMap<String, Object>() {{
            put("title", "安切洛蒂确认本周末比赛大名单");
            put("link", "#");
            put("time", "2026-03-03");
            put("image", "");
            put("category", "Football");
        }});
        local.add(new HashMap<String, Object>() {{
            put("title", "维尼修斯当选本月最佳球员");
            put("link", "#");
            put("time", "2026-03-06");
            put("image", "");
            put("category", "Football");
        }});
        local.add(new HashMap<String, Object>() {{
            put("title", "欧冠赛前训练完成，球队状态良好");
            put("link", "#");
            put("time", "2026-03-08");
            put("image", "");
            put("category", "Football");
        }});

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < local.size() && i < size; i++) {
            list.add(local.get(i));
        }
        return R.ok().put("data", list);
    }
}
