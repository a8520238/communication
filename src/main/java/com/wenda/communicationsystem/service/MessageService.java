package com.wenda.communicationsystem.service;

import com.wenda.communicationsystem.mapper.MessageMapper;
import com.wenda.communicationsystem.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Liguangzhe
 * @Date created in 20:15 2020/6/2
 */
@Service
public class MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    SentiveService sentiveService;

    public int addMessage(Message message) {
        message.setContent(sentiveService.filter(message.getContent()));
        return messageMapper.insert(message) > 0? message.getId() : 0;
    }

    public List<Message> getMessageDetail(String conversationId, int limit, int offset) {
        return messageMapper.getConversationDetail(conversationId, limit, offset);
    }

    public int getConversationUnreadCount(int userId, String conversationId) {
        return messageMapper.getConversationUnreadCount(userId, conversationId);
    }

    public void updateHasRead(int toId) {
        messageMapper.updateHasRead(toId);
    }

    public List<Message> getMessageList(int userId, int limit, int offset) {
        return messageMapper.getConversationList(userId, limit, offset);
    }
}

