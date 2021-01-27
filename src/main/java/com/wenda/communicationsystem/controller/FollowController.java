package com.wenda.communicationsystem.controller;

import com.wenda.communicationsystem.async.EventModel;
import com.wenda.communicationsystem.async.EventProducer;
import com.wenda.communicationsystem.async.EventType;
import com.wenda.communicationsystem.model.*;
import com.wenda.communicationsystem.service.CommentService;
import com.wenda.communicationsystem.service.FollowService;
import com.wenda.communicationsystem.service.QuestionService;
import com.wenda.communicationsystem.service.UserService;
import com.wenda.communicationsystem.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Liguangzhe
 * @Date created in 20:53 2020/6/10
 */
@Controller
public class FollowController {
    private static final Logger logger = LoggerFactory.getLogger(FollowController.class);

    @Autowired
    FollowService followService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = "/followUser", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    String follow(@RequestParam("userId") int userId) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);
        }
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntryType.ENTRY_USER, userId);

        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntryId(userId).setEntryOwnerId(userId)
                .setEntryType(EntryType.ENTRY_USER));

        return WendaUtil.getJSONString(ret? 0: 1,
                String.valueOf(followService.getFolloweeCount(EntryType.ENTRY_USER, hostHolder.getUser().getId())));
    }

    @RequestMapping(path = "/unfollowUser", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    String unfollow(@RequestParam("userId") int userId) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);
        }
        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntryType.ENTRY_USER, userId);

        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntryId(userId).setEntryOwnerId(userId)
                .setEntryType(EntryType.ENTRY_USER));

        return WendaUtil.getJSONString(ret? 0: 1,
                String.valueOf(followService.getFolloweeCount(EntryType.ENTRY_USER, hostHolder.getUser().getId())));
    }

    @RequestMapping(path = "/followQuestion", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    String followQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);
        }

        Question q = questionService.selectQuestionById(questionId);
        if (q == null) {
            return WendaUtil.getJSONString(1, "问题不存在");
        }

        boolean ret = followService.follow(hostHolder.getUser().getId(), EntryType.ENTRY_QUESTION, questionId);

        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntryId(questionId).setEntryOwnerId(q.getUserId())
                .setEntryType(EntryType.ENTRY_QUESTION));

        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(EntryType.ENTRY_QUESTION, questionId));
        return WendaUtil.getJSONString(ret? 0: 1, info);
    }

    @RequestMapping(path = "/unfollowQuestion", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    String unfollowQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);
        }

        Question q = questionService.selectQuestionById(questionId);
        if (q == null) {
            return WendaUtil.getJSONString(1, "问题不存在");
        }
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntryId(questionId).setEntryOwnerId(q.getUserId())
                .setEntryType(EntryType.ENTRY_QUESTION));

        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntryType.ENTRY_QUESTION, questionId);

        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("count", followService.getFollowerCount(EntryType.ENTRY_QUESTION, questionId));
        info.put("id", hostHolder.getUser().getId());
        return WendaUtil.getJSONString(ret? 0: 1, info);
    }

    @RequestMapping(path = "/user/{uid}/followers", method = RequestMethod.GET)
    public String followers(Model model, @PathVariable("uid") int userId) {
        List<Integer> followerIds = followService.getFollowers(EntryType.ENTRY_USER, userId, 0, 10);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
        } else {
            model.addAttribute("followers", getUsersInfo(0, followerIds));
        }
        model.addAttribute("followerCount", followService.getFollowerCount(EntryType.ENTRY_USER, userId));
        model.addAttribute(("curUser"), userService.getUser(userId));

        return "followers";
    }

    @RequestMapping(path = "/user/{uid}/followees", method = RequestMethod.GET)
    public String followees(Model model, @PathVariable("uid") int userId) {
        List<Integer> followeeIds = followService.getFollowees(EntryType.ENTRY_USER, userId, 0, 10);
        //System.out.println(followeeIds.size());
        if (hostHolder.getUser() != null) {
            model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
        } else {
            model.addAttribute("followees", getUsersInfo(0, followeeIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(EntryType.ENTRY_USER, userId));
        model.addAttribute(("curUser"), userService.getUser(userId));

        return "followees";
    }

    private List<ViewObject> getUsersInfo(int LocalUserId, List<Integer> userIds) {
        List<ViewObject> usersInfo = new ArrayList<>();
        for (Integer uid: userIds) {
            User user = userService.getUser(uid);

            if (user == null) {
                continue;
            }

            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vo.set("commentCount", commentService.getUserCommentCount(uid));
            vo.set("followerCount", followService.getFollowerCount(EntryType.ENTRY_USER, uid));
            vo.set("followeeCount", followService.getFolloweeCount(EntryType.ENTRY_USER, uid));
            if (LocalUserId != 0) {
                vo.set("followed", followService.isFollower(LocalUserId, EntryType.ENTRY_USER,uid));
            } else {
                vo.set("followed", false);
            }
            usersInfo.add(vo);
        }
        return usersInfo;
    }
}
