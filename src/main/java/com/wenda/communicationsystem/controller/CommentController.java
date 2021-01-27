package com.wenda.communicationsystem.controller;

import com.wenda.communicationsystem.async.EventModel;
import com.wenda.communicationsystem.async.EventProducer;
import com.wenda.communicationsystem.async.EventType;
import com.wenda.communicationsystem.model.Comment;
import com.wenda.communicationsystem.model.EntryType;
import com.wenda.communicationsystem.model.HostHolder;
import com.wenda.communicationsystem.service.CommentService;
import com.wenda.communicationsystem.service.QuestionService;
import com.wenda.communicationsystem.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @Author Liguangzhe
 * @Date created in 18:15 2020/5/27
 */
@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content) {
        try {
            Comment comment = new Comment();
            comment.setContent(content);
            if (hostHolder.getUser() != null) {
                comment.setUserId(hostHolder.getUser().getId());
            } else {
                comment.setUserId(WendaUtil.ANONYMOUS_USERID);
            }
            comment.setCreatedDate(new Date());
            comment.setEntryType(EntryType.ENTRY_QUESTION);
            comment.setEntryId(questionId);
            comment.setStatus(0);
            int access = commentService.addComment(comment);

            int count = commentService.getCommentCount(comment.getEntryId(), comment.getEntryType());
            questionService.updateCommentCount(comment.getEntryId(), count);

            eventProducer.fireEvent(new EventModel(EventType.COMMENT)
                    .setActorId(hostHolder.getUser().getId())
                    .setEntryId(comment.getId())
                    .setEntryOwnerId(comment.getUserId())
                    .setEntryType(EntryType.ENTRY_COMMENT)
                    );
        } catch (Exception e) {
            logger.error("增加问题评论失败1" + e.getMessage());
        }
        return "redirect:/question/" + questionId;
    }
}
