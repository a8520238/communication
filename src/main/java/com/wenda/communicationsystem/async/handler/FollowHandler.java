package com.wenda.communicationsystem.async.handler;

import com.wenda.communicationsystem.async.EventHandler;
import com.wenda.communicationsystem.async.EventModel;
import com.wenda.communicationsystem.async.EventType;
import com.wenda.communicationsystem.model.EntryType;
import com.wenda.communicationsystem.model.Message;
import com.wenda.communicationsystem.model.User;
import com.wenda.communicationsystem.service.MessageService;
import com.wenda.communicationsystem.service.UserService;
import com.wenda.communicationsystem.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author Liguangzhe
 * @Date created in 9:59 2020/6/11
 */
@Component
public class FollowHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandler(EventModel eventModel) {
        Message message = new Message();
        User user = userService.getUser(eventModel.getActorId());
        message.setCreateDate(new Date());
        message.setToId(eventModel.getEntryOwnerId());
        message.setFromId(WendaUtil.SYSTEM_USERID);

        if (eventModel.getEntryType() == EntryType.ENTRY_QUESTION) {
            message.setContent("用户" + user.getName() + "关注了你的问题" +
                    ", http://127.0.0.1:8080/question/" + eventModel.getEntryId());
        } else if (eventModel.getEntryType() == EntryType.ENTRY_USER) {
            message.setContent("用户" + user.getName() + "关注了你" +
                    ", http://127.0.0.1:8080/user/" + eventModel.getActorId());
        }
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
