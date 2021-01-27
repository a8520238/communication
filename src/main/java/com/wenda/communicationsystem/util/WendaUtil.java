package com.wenda.communicationsystem.util;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @Author Liguangzhe
 * @Date created in 17:10 2020/5/26
 */
public class WendaUtil {

    public static int ANONYMOUS_USERID = 3; //匿名用户id

    public static int SYSTEM_USERID = 0;

    public static String getJSONString(int code) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        return json.toJSONString();
    }

    public static String getJSONString(int code, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        for (Map.Entry<String, Object> entry: map.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toJSONString();
    }
}



