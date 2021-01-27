package com.wenda.communicationsystem.async;

import com.alibaba.fastjson.JSONObject;
import com.wenda.communicationsystem.util.JedisAdapter;
import com.wenda.communicationsystem.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Liguangzhe
 * @Date created in 15:18 2020/6/9
 */
@Service
public class EventProducer {

    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel) {
        try{
            String json = JSONObject.toJSONString(eventModel);
            System.out.println("json" + json);
            String key = RedisKeyUtil.getBizEventQueue();
            jedisAdapter.lpush(key, json);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

}
