package com.wenda.communicationsystem.async.handler;

import com.wenda.communicationsystem.async.EventHandler;
import com.wenda.communicationsystem.async.EventModel;
import com.wenda.communicationsystem.async.EventType;
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
 * @Date created in 17:15 2020/6/9
 */
@Component
public class LikeHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandler(EventModel eventModel) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(eventModel.getEntryOwnerId());
        message.setCreateDate(new Date());

        User user = userService.getUser(eventModel.getActorId());
        message.setContent("用户" + user.getName() + "赞了你的评论"
            + "http://127.0.0.1:8080/question/" + eventModel.getExt("questionId"));
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
