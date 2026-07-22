package com.service.ai;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.GonggaoEntity;
import com.entity.HetongEntity;
import com.entity.SaishiEntity;
import com.entity.ShujuEntity;
import com.entity.XunlianEntity;
import com.entity.YonghuEntity;
import com.service.GonggaoService;
import com.service.HetongService;
import com.service.SaishiService;
import com.service.ShujuService;
import com.service.XunlianService;
import com.service.YonghuService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 执行 AI 工具对应的真实数据库查询。
 */
@Component
public class AiToolExecutor {

    private static final Logger logger = LoggerFactory.getLogger(AiToolExecutor.class);
    private static final int DEFAULT_LIMIT = 5;

    @Autowired
    private YonghuService yonghuService;
    @Autowired
    private GonggaoService gonggaoService;
    @Autowired
    private SaishiService saishiService;
    @Autowired
    private XunlianService xunlianService;
    @Autowired
    private HetongService hetongService;
    @Autowired
    private ShujuService shujuService;
    @Autowired
    private AiExecutionRecorder executionRecorder;
    @Autowired
    private AiReplyFormatter replyFormatter;

    public JSONObject executeToolCall(JSONObject toolCall, String userMessage) {
        JSONObject function = toolCall.getJSONObject("function");
        String name = function == null ? "" : function.getString("name");
        String argumentsJson = function == null ? "{}" : function.getString("arguments");
        JSONObject arguments = parseArguments(argumentsJson);
        if (replyFormatter.asksForLatestOne(userMessage) && arguments.getInteger("limit") == null) {
            arguments.put("limit", 1);
        }
        long start = System.currentTimeMillis();
        JSONObject result = executeTool(name, arguments);
        return executionRecorder.record(name, arguments, result, System.currentTimeMillis() - start, false);
    }

    public JSONObject executeTool(String name, JSONObject arguments) {
        if ("queryPlayers".equals(name)) {
            return queryPlayers(arguments);
        }
        if ("queryAnnouncements".equals(name)) {
            return queryAnnouncements(arguments);
        }
        if ("queryMatches".equals(name)) {
            return queryMatches(arguments);
        }
        if ("queryTrainingPlans".equals(name)) {
            return queryTrainingPlans(arguments);
        }
        if ("queryContracts".equals(name)) {
            return queryContracts(arguments);
        }
        if ("queryPlayerData".equals(name)) {
            return queryPlayerData(arguments);
        }
        JSONObject result = new JSONObject();
        result.put("error", "未知工具：" + name);
        return result;
    }

    public JSONObject queryPlayers(JSONObject arguments) {
        String keyword = keyword(arguments);
        QueryWrapper<YonghuEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(w -> w.like("yonghu_name", keyword)
                    .or().like("username", keyword)
                    .or().like("yonghu_uuid_number", keyword));
        }
        List<YonghuEntity> list = limit(yonghuService.selectList(wrapper), limit(arguments));
        JSONArray records = new JSONArray();
        for (YonghuEntity item : list) {
            JSONObject row = new JSONObject();
            row.put("id", item.getId());
            row.put("name", item.getYonghuName());
            row.put("number", item.getYonghuUuidNumber());
            row.put("account", item.getUsername());
            row.put("sex", formatSex(item.getSexTypes()));
            row.put("phone", maskPhone(item.getYonghuPhone()));
            row.put("email", item.getYonghuEmail());
            row.put("detailUrl", "/zuqiujulebguanli/front/pages/yonghu/detail.html?id=" + item.getId());
            records.add(row);
        }
        return toolPayload("球员档案", keyword, records);
    }

    public JSONObject queryAnnouncements(JSONObject arguments) {
        String keyword = keyword(arguments);
        QueryWrapper<GonggaoEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("insert_time");
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(w -> w.like("gonggao_name", keyword).or().like("gonggao_content", keyword));
        }
        List<GonggaoEntity> list = limit(gonggaoService.selectList(wrapper), limit(arguments));
        JSONArray records = new JSONArray();
        for (GonggaoEntity item : list) {
            JSONObject row = new JSONObject();
            row.put("id", item.getId());
            row.put("title", item.getGonggaoName());
            row.put("publishTime", formatDateTime(item.getInsertTime()));
            row.put("summary", cleanText(item.getGonggaoContent(), 90));
            row.put("detailUrl", "/zuqiujulebguanli/front/pages/gonggao/detail.html?id=" + item.getId());
            records.add(row);
        }
        return toolPayload("公告信息", keyword, records);
    }

    public JSONObject queryMatches(JSONObject arguments) {
        String keyword = keyword(arguments);
        QueryWrapper<SaishiEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("saishi_delete", 1);
        wrapper.orderByDesc("insert_time");
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(w -> w.like("saishi_name", keyword)
                    .or().like("saishi_address", keyword)
                    .or().like("saishi_content", keyword));
        }
        List<SaishiEntity> list = limit(saishiService.selectList(wrapper), limit(arguments));
        JSONArray records = new JSONArray();
        for (SaishiEntity item : list) {
            JSONObject row = new JSONObject();
            row.put("id", item.getId());
            row.put("title", item.getSaishiName());
            row.put("address", item.getSaishiAddress());
            row.put("publishTime", formatDateTime(item.getInsertTime()));
            row.put("summary", cleanText(item.getSaishiContent(), 90));
            row.put("detailUrl", "/zuqiujulebguanli/front/pages/saishi/detail.html?id=" + item.getId());
            records.add(row);
        }
        return toolPayload("赛事信息", keyword, records);
    }

    public JSONObject queryTrainingPlans(JSONObject arguments) {
        String keyword = keyword(arguments);
        QueryWrapper<XunlianEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("xunlian_delete", 1);
        wrapper.orderByDesc("insert_time");
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(w -> w.like("xunlian_name", keyword)
                    .or().like("xunlian_kemu", keyword)
                    .or().like("xunlian_content", keyword));
        }
        List<XunlianEntity> list = limit(xunlianService.selectList(wrapper), limit(arguments));
        JSONArray records = new JSONArray();
        for (XunlianEntity item : list) {
            JSONObject row = new JSONObject();
            row.put("id", item.getId());
            row.put("title", item.getXunlianName());
            row.put("number", item.getXunlianUuidNumber());
            row.put("subject", item.getXunlianKemu());
            row.put("trainingDate", formatDate(item.getXunlianTime()));
            row.put("summary", cleanText(item.getXunlianContent(), 90));
            row.put("detailUrl", "/zuqiujulebguanli/front/pages/xunlian/detail.html?id=" + item.getId());
            records.add(row);
        }
        return toolPayload("训练计划", keyword, records);
    }

    public JSONObject queryContracts(JSONObject arguments) {
        String keyword = firstNotBlank(arguments.getString("playerName"), keyword(arguments));
        QueryWrapper<HetongEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("hetong_delete", 1);
        wrapper.orderByDesc("id");
        JSONArray playerCandidates = new JSONArray();
        boolean playerMissing = false;
        boolean playerAmbiguous = false;
        if (StringUtils.isNotBlank(keyword)) {
            List<YonghuEntity> players = yonghuService.selectList(new QueryWrapper<YonghuEntity>().like("yonghu_name", keyword));
            if (players != null && !players.isEmpty()) {
                List<YonghuEntity> exactPlayers = exactPlayers(players, keyword);
                if (exactPlayers.size() == 1) {
                    wrapper.eq("yonghu_id", exactPlayers.get(0).getId());
                } else if (players.size() == 1) {
                    wrapper.eq("yonghu_id", players.get(0).getId());
                } else {
                    playerAmbiguous = true;
                    for (YonghuEntity player : limit(players, DEFAULT_LIMIT)) {
                        JSONObject candidate = new JSONObject();
                        candidate.put("name", player.getYonghuName());
                        candidate.put("number", player.getYonghuUuidNumber());
                        candidate.put("account", player.getUsername());
                        candidate.put("detailUrl", "/zuqiujulebguanli/front/pages/yonghu/detail.html?id=" + player.getId());
                        playerCandidates.add(candidate);
                    }
                }
            } else {
                playerMissing = true;
            }
        }
        List<HetongEntity> list = playerMissing || playerAmbiguous
                ? null
                : limit(hetongService.selectList(wrapper), limit(arguments));
        JSONArray records = new JSONArray();
        if (list != null) {
            for (HetongEntity item : list) {
                JSONObject row = new JSONObject();
                row.put("id", item.getId());
                row.put("title", item.getHetongName());
                row.put("playerId", item.getYonghuId());
                row.put("file", item.getHetongFile());
                row.put("remark", cleanText(item.getHetongText(), 90));
                row.put("createTime", formatDateTime(item.getCreateTime()));
                row.put("detailUrl", "/zuqiujulebguanli/front/pages/hetong/detail.html?id=" + item.getId());
                records.add(row);
            }
        }
        JSONObject payload = toolPayload("合同信息", keyword, records);
        payload.put("playerMissing", playerMissing);
        payload.put("playerAmbiguous", playerAmbiguous);
        payload.put("playerCandidates", playerCandidates);
        payload.put("schemaNote", "当前合同表没有到期时间和薪资字段，只能返回合同标题、归属、附件、备注和创建时间。");
        return payload;
    }

    public JSONObject queryPlayerData(JSONObject arguments) {
        String keyword = keyword(arguments);
        QueryWrapper<ShujuEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("shuju_delete", 1);
        wrapper.orderByDesc("insert_time");
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(w -> w.like("shuju_name", keyword)
                    .or().like("shuju_content", keyword)
                    .or().like("shuju_uuid_number", keyword));
        }
        List<ShujuEntity> list = limit(shujuService.selectList(wrapper), limit(arguments));
        JSONArray records = new JSONArray();
        for (ShujuEntity item : list) {
            JSONObject row = new JSONObject();
            row.put("id", item.getId());
            row.put("title", item.getShujuName());
            row.put("number", item.getShujuUuidNumber());
            row.put("recordDate", formatDate(item.getShujuTime()));
            row.put("summary", cleanText(item.getShujuContent(), 90));
            row.put("detailUrl", "/zuqiujulebguanli/front/pages/shuju/detail.html?id=" + item.getId());
            records.add(row);
        }
        return toolPayload("球员数据", keyword, records);
    }

    public JSONObject parseArguments(String argumentsJson) {
        if (StringUtils.isBlank(argumentsJson)) {
            return new JSONObject();
        }
        try {
            return JSON.parseObject(argumentsJson);
        } catch (Exception e) {
            logger.warn("Failed to parse tool arguments: {}", argumentsJson, e);
            return new JSONObject();
        }
    }

    private JSONObject toolPayload(String module, String keyword, JSONArray records) {
        JSONObject payload = new JSONObject();
        payload.put("module", module);
        payload.put("keyword", defaultText(keyword, "无"));
        payload.put("count", records.size());
        payload.put("records", records);
        return payload;
    }

    private String keyword(JSONObject arguments) {
        return firstNotBlank(arguments.getString("keyword"), arguments.getString("name"), arguments.getString("title"));
    }

    private int limit(JSONObject arguments) {
        Integer limit = arguments.getInteger("limit");
        if (limit == null || limit <= 0) {
            return DEFAULT_LIMIT;
        }
        return Math.min(limit, 10);
    }

    private <T> List<T> limit(List<T> list, int limit) {
        if (list == null || list.size() <= limit) {
            return list;
        }
        return list.subList(0, limit);
    }

    private List<YonghuEntity> exactPlayers(List<YonghuEntity> players, String keyword) {
        List<YonghuEntity> exactPlayers = new ArrayList<>();
        if (players == null || StringUtils.isBlank(keyword)) {
            return exactPlayers;
        }
        for (YonghuEntity player : players) {
            if (keyword.equals(player.getYonghuName())) {
                exactPlayers.add(player);
            }
        }
        return exactPlayers;
    }

    private String cleanText(String value, int maxLength) {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        String text = value.replaceAll("<[^>]+>", "").replaceAll("\\s+", " ").trim();
        return text.length() > maxLength ? text.substring(0, maxLength) + "..." : text;
    }

    private String formatSex(Integer sexTypes) {
        if (sexTypes == null) {
            return "暂无";
        }
        if (sexTypes == 1) {
            return "男";
        }
        if (sexTypes == 2) {
            return "女";
        }
        return String.valueOf(sexTypes);
    }

    private String maskPhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return "暂无";
        }
        String value = phone.trim();
        if (value.length() < 7) {
            return value;
        }
        return value.substring(0, 3) + "****" + value.substring(value.length() - 4);
    }

    private String formatDate(Date date) {
        if (date == null) {
            return "暂无";
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private String formatDateTime(Date date) {
        if (date == null) {
            return "暂无";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
    }

    private String defaultText(String value, String fallback) {
        return StringUtils.isBlank(value) ? fallback : value;
    }

    private String firstNotBlank(String first, String second) {
        return StringUtils.isNotBlank(first) ? first : second;
    }

    private String firstNotBlank(String first, String second, String third) {
        if (StringUtils.isNotBlank(first)) {
            return first;
        }
        return StringUtils.isNotBlank(second) ? second : third;
    }
}
