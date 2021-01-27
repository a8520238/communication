package com.wenda.communicationsystem.async;

import com.alibaba.fastjson.JSON;
import com.wenda.communicationsystem.util.JedisAdapter;
import com.wenda.communicationsystem.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author Liguangzhe
 * @Date created in 15:36 2020/6/9
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    JedisAdapter jedisAdapter;

    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);

        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry: beans.entrySet()) {
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();

                for (EventType type: eventTypes) {
                    if (!config.containsKey(type)) {
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
//                int id = 1;
                while (true) {
                    String key = RedisKeyUtil.getBizEventQueue();
                    List<String> events = jedisAdapter.brpop(0, key);
//                    System.out.println(String.valueOf(id) + events);
//                    id++;
//                while (jedisAdapter.brpop(0, key) != null) {
//                    System.out.println("tset" + jedisAdapter.brpop(0, key));
//                }
                    for (String message: events) {
                        if (message.equals(key)) {
                            continue;
                        }
                        EventModel eventModel = JSON.parseObject(message, EventModel.class);
                        if (!config.containsKey(eventModel.getType())) {
                            logger.error("无法处理的事件");
                            continue;
                        }
                        for (EventHandler eventHandler: config.get(eventModel.getType())) {
                            System.out.println("doHandle" + eventModel.getType());
                            eventHandler.doHandler(eventModel);
                        }
                    }
                }

            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
