package com.wenda.communicationsystem.async;

import java.util.List;

/**
 * @Author Liguangzhe
 * @Date created in 15:33 2020/6/9
 */
public interface EventHandler {
    void doHandler(EventModel eventModel);
    List<EventType> getSupportEventTypes();
}
