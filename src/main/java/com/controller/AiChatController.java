package com.controller;

import com.annotation.IgnoreAuth;
import com.service.AiChatService;
import com.utils.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * AI 问答接口。
 */
@RestController
@RequestMapping("/ai")
public class AiChatController {

    @Autowired
    private AiChatService aiChatService;

    @IgnoreAuth
    @PostMapping("/chat")
    public R chat(@RequestBody Map<String, Object> params) {
        String message = params == null || params.get("message") == null ? "" : String.valueOf(params.get("message")).trim();
        if (StringUtils.isBlank(message)) {
            return R.error(400, "请输入问题");
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("reply", aiChatService.chat(message));
        return R.ok().put("data", data);
    }
}
