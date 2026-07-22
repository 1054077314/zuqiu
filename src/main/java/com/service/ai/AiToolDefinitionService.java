package com.service.ai;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Component;

/**
 * 定义 AI Function Calling 的 6 类业务查询工具。
 */
@Component
public class AiToolDefinitionService {

    public JSONArray buildTools() {
        JSONArray tools = new JSONArray();
        tools.add(tool("queryPlayers", "查询系统内球员档案。适合用户询问球员、队员、球员资料、球员档案。用户说最新/最近/第一条时 limit 必须传 1。", "keyword", "球员姓名、账号或编号，可为空"));
        tools.add(tool("queryAnnouncements", "查询系统公告信息。适合用户询问公告、通知、最新公告。用户说最新/最近/第一条时 limit 必须传 1。", "keyword", "公告关键词，可为空"));
        tools.add(tool("queryMatches", "查询系统赛事信息。适合用户询问赛事、比赛、赛程、地点。用户说最新/最近/第一条时 limit 必须传 1。", "keyword", "赛事名称、地点或内容关键词，可为空"));
        tools.add(tool("queryTrainingPlans", "查询系统训练计划。适合用户询问训练安排、训练计划、训练科目。用户说最新/最近/第一条时 limit 必须传 1。", "keyword", "训练计划关键词，可为空"));
        tools.add(tool("queryContracts", "查询系统合同信息。适合用户询问合同、球员合同、合同附件、合同备注。用户说最新/最近/第一条时 limit 必须传 1。", "playerName", "球员姓名或合同关键词，可为空"));
        tools.add(tool("queryPlayerData", "查询系统球员数据。适合用户询问球员数据、训练数据、比赛数据记录。用户说最新/最近/第一条时 limit 必须传 1。", "keyword", "球员数据关键词，可为空"));
        return tools;
    }

    private JSONObject tool(String name, String description, String propertyName, String propertyDescription) {
        JSONObject property = new JSONObject();
        property.put("type", "string");
        property.put("description", propertyDescription);

        JSONObject properties = new JSONObject();
        properties.put(propertyName, property);
        JSONObject limitProperty = new JSONObject();
        limitProperty.put("type", "integer");
        limitProperty.put("description", "最多返回几条，默认5，最大10");
        properties.put("limit", limitProperty);

        JSONObject parameters = new JSONObject();
        parameters.put("type", "object");
        parameters.put("properties", properties);

        JSONObject function = new JSONObject();
        function.put("name", name);
        function.put("description", description);
        function.put("parameters", parameters);

        JSONObject tool = new JSONObject();
        tool.put("type", "function");
        tool.put("function", function);
        return tool;
    }
}
