package com.wenda.communicationsystem.async.handler;

import com.wenda.communicationsystem.async.EventHandler;
import com.wenda.communicationsystem.async.EventModel;
import com.wenda.communicationsystem.async.EventType;
import com.wenda.communicationsystem.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Liguangzhe
 * @Date created in 17:25 2020/6/9
 */
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MailSender mailSender;

    @Override
    public void doHandler(EventModel eventModel) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", eventModel.getExt("username"));
        mailSender.sendWithHTMLTemplate(eventModel.getExt("email"),
                "登录ip异常", "/mails/loginException.html", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
