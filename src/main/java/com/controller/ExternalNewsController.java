package com.controller;

import com.annotation.IgnoreAuth;
import com.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("external")
public class ExternalNewsController {
    private static final String[] NEWS_IMAGES = new String[]{
            "https://commons.wikimedia.org/wiki/Special:FilePath/Real%20Madrid.JPG?width=640",
            "https://commons.wikimedia.org/wiki/Special:FilePath/Carlo%20Ancelotti%202012-01-02%20%281%29.jpg?width=640",
            "https://commons.wikimedia.org/wiki/Special:FilePath/Brazil%20vs%20Serbia%20WC2022%20Vinicius%20Jr%20and%20serbian%20players.jpg?width=640",
            "https://commons.wikimedia.org/wiki/Special:FilePath/Varane%20au%20Real%20%282%29.jpg?width=640"
    };


    @IgnoreAuth
    @GetMapping("/realmadrid/news")
    public R realmadridNews(@RequestParam(value = "limit", defaultValue = "4") Integer limit) {
        int size = Math.max(1, Math.min(limit == null ? 4 : limit, 12));

        List<Map<String, Object>> local = new ArrayList<Map<String, Object>>();
        local.add(news("\u7687\u9a6c\u4e3b\u573a\u53d6\u80dc\uff0c\u8054\u8d5b\u79ef\u5206\u7ee7\u7eed\u9886\u8dd1", "2026-03-01", NEWS_IMAGES[0]));
        local.add(news("\u5b89\u5207\u6d1b\u8482\u786e\u8ba4\u672c\u5468\u672b\u6bd4\u8d5b\u5927\u540d\u5355", "2026-03-03", NEWS_IMAGES[1]));
        local.add(news("\u7ef4\u5c3c\u4fee\u65af\u5f53\u9009\u672c\u6708\u6700\u4f73\u7403\u5458", "2026-03-06", NEWS_IMAGES[2]));
        local.add(news("\u6b27\u51a0\u8d5b\u524d\u8bad\u7ec3\u5b8c\u6210\uff0c\u7403\u961f\u72b6\u6001\u826f\u597d", "2026-03-08", NEWS_IMAGES[3]));

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < local.size() && i < size; i++) {
            list.add(local.get(i));
        }
        return R.ok().put("data", list);
    }

    private Map<String, Object> news(String title, String time, String image) {
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("title", title);
        item.put("link", "#");
        item.put("time", time);
        item.put("image", image);
        item.put("img", image);
        item.put("category", "绿茵快讯");
        return item;
    }
}
