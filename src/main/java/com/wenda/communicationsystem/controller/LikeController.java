package com.wenda.communicationsystem.controller;

import com.wenda.communicationsystem.async.EventModel;
import com.wenda.communicationsystem.async.EventProducer;
import com.wenda.communicationsystem.async.EventType;
import com.wenda.communicationsystem.model.Comment;
import com.wenda.communicationsystem.model.EntryType;
import com.wenda.communicationsystem.model.HostHolder;
import com.wenda.communicationsystem.service.CommentService;
import com.wenda.communicationsystem.service.LikeService;
import com.wenda.communicationsystem.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Liguangzhe
 * @Date created in 16:43 2020/6/4
 */
@Controller
public class LikeController {
    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    CommentService commentService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path="/like", method = RequestMethod.POST)
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);
        }

        Comment comment = commentService.getCommentById(commentId);

        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId())
                .setEntryId(commentId)
                .setEntryOwnerId(comment.getUserId())
                .setEntryType(EntryType.ENTRY_COMMENT)
                .setExt("questionId", String.valueOf(comment.getEntryId()))
        );
        //? 再想一下这里的逻辑
        long LikeCount = likeService.like(hostHolder.getUser().getId(), EntryType.ENTRY_COMMENT, commentId);
        //System.out.println(WendaUtil.getJSONString(0, String.valueOf(LikeCount)));
        return WendaUtil.getJSONString(0, String.valueOf(LikeCount));
    }

    @RequestMapping(path="/dislike", method = RequestMethod.POST)
    @ResponseBody
    public String disLike(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);
        }

        //? 再想一下这里的逻辑
        long LikeCount = likeService.disLike(hostHolder.getUser().getId(), EntryType.ENTRY_COMMENT, commentId);
        return WendaUtil.getJSONString(0, String.valueOf(LikeCount));
    }
}
