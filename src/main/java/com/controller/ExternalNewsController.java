package com.controller;

import cn.hutool.http.HttpUtil;
import com.annotation.IgnoreAuth;
import com.utils.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("external")
public class ExternalNewsController {

    private static final String REAL_MADRID_SITEMAP = "https://www.realmadrid.com/sitemap-latest-news-en-US.xml";

    private static final Pattern NEWS_ITEM_PATTERN = Pattern.compile(
            "<url>\\s*<loc>(.*?)</loc>.*?<news:publication_date>(.*?)</news:publication_date>.*?<news:title>(.*?)</news:title>.*?<image:loc>(.*?)</image:loc>.*?</url>",
            Pattern.DOTALL
    );
    private static final Pattern ARTICLE_IMAGE_PATTERN = Pattern.compile(
            "<meta[^>]+property=[\"']og:image[\"'][^>]+content=[\"'](.*?)[\"']",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );

    @IgnoreAuth
    @GetMapping("/realmadrid/news")
    public R realmadridNews(@RequestParam(value = "limit", defaultValue = "4") Integer limit) {
        int size = Math.max(1, Math.min(limit == null ? 4 : limit, 12));
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            String xml = HttpUtil.createGet(REAL_MADRID_SITEMAP)
                    .timeout(8000)
                    .execute()
                    .body();
            Matcher matcher = NEWS_ITEM_PATTERN.matcher(xml);
            while (matcher.find() && list.size() < size) {
                String link = decodeXml(matcher.group(1));
                String publishTime = decodeXml(matcher.group(2));
                String title = decodeXml(matcher.group(3));
                String image = decodeXml(matcher.group(4));

                Map<String, Object> item = new HashMap<String, Object>();
                item.put("title", title);
                item.put("link", link);
                item.put("time", publishTime);
                item.put("image", resolveImage(link, image));
                item.put("category", resolveCategory(link));
                list.add(item);
            }
            return R.ok().put("data", list).put("source", REAL_MADRID_SITEMAP);
        } catch (Exception e) {
            return R.error("Failed to fetch Real Madrid official news");
        }
    }

    private String resolveCategory(String link) {
        if (link == null) {
            return "Official";
        }
        if (link.contains("/football/")) {
            return "Football";
        }
        if (link.contains("/basket/")) {
            return "Basketball";
        }
        if (link.contains("/club/")) {
            return "Club";
        }
        return "Official";
    }

    private String decodeXml(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("&amp;", "&")
                .replace("&quot;", "\"")
                .replace("&apos;", "'")
                .replace("&#39;", "'")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .trim();
    }

    private String resolveImage(String link, String image) {
        String decoded = decodeXml(image);
        if (decoded.length() > 0 && !"undefined".equalsIgnoreCase(decoded)) {
            return decoded;
        }
        if (link == null || link.trim().length() == 0) {
            return "";
        }
        try {
            String html = HttpUtil.createGet(link)
                    .timeout(8000)
                    .execute()
                    .body();
            Matcher matcher = ARTICLE_IMAGE_PATTERN.matcher(html);
            if (matcher.find()) {
                return decodeXml(matcher.group(1));
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }
}
