package com.wenda.communicationsystem.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Liguangzhe
 * @Date created in 21:55 2020/4/8
 */
public class ViewObject {
    private Map<String, Object> objs = new HashMap<>();
    public void set(String key, Object obj) {
        objs.put(key, obj);
    }
    public Object get(String key) {
        return objs.get(key);
    }
}
