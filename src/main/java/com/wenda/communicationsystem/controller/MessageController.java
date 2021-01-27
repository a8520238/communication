package com.wenda.communicationsystem.controller;

import com.wenda.communicationsystem.model.HostHolder;
import com.wenda.communicationsystem.model.Message;
import com.wenda.communicationsystem.model.User;
import com.wenda.communicationsystem.model.ViewObject;
import com.wenda.communicationsystem.service.MessageService;
import com.wenda.communicationsystem.service.UserService;
import com.wenda.communicationsystem.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Liguangzhe
 * @Date created in 20:49 2020/6/2
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = "/msg/list", method = RequestMethod.GET)
    public String getConversationList(Model model) {
        if (hostHolder.getUser() == null) {
            return "redirect:/relogin";
        }
        int localUserId = hostHolder.getUser().getId();
        //List<Message> conversationList = messageService.getMessageList(localUserId, 1, 10);
        List<Message> conversationList = messageService.getMessageList(localUserId, 10, 0);
        List<ViewObject> conversations = new ArrayList<>();
        for (Message message: conversationList) {
            ViewObject vo = new ViewObject();
            vo.set("message", message);
            int targetUserId = message.getFromId() == localUserId? message.getToId(): message.getFromId();
            vo.set("user", userService.getUser(targetUserId));
            vo.set("unread", messageService.getConversationUnreadCount(localUserId, message.getConversationId()));
            conversations.add(vo);
        }
        model.addAttribute("conversations", conversations);
        return "letter";
    }

    @RequestMapping(path = "/msg/detail", method = RequestMethod.GET)
    public String getConversationDetail(Model model,
                                        @RequestParam("conversationId") String conversationId) {
        try {
            List<Message> conversationDetail = messageService.getMessageDetail(conversationId, 10, 0);
            List<ViewObject> conversations = new ArrayList<>();
            for (Message message: conversationDetail) {
                messageService.updateHasRead(hostHolder.getUser().getId());
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                vo.set("user", userService.getUser(message.getFromId()));
                conversations.add(vo);
            }
            model.addAttribute("messages", conversations);
        } catch(Exception e) {
            logger.error("获取消息详情失败" + e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = "/msg/addMessage", method = RequestMethod.POST)
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content) {
        try {
            if (hostHolder.getUser() == null) {
                return WendaUtil.getJSONString(999, "未登录！");
            }
            User toUser = userService.getUserByName(toName);
            if (toUser == null) {
                return WendaUtil.getJSONString(1, "用户不存在");
            }
            Message message = new Message();
            message.setContent(content);
            message.setCreateDate(new Date());
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(toUser.getId());
            message.setHasRead(0);
            messageService.addMessage(message);
            return WendaUtil.getJSONString(0);
        } catch (Exception e) {
            logger.error("发送消息失败" + e.getMessage());
            return WendaUtil.getJSONString(1, "发送消息失败");
        }
    }
}
