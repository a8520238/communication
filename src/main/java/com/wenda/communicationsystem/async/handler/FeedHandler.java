package com.wenda.communicationsystem.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.wenda.communicationsystem.async.EventHandler;
import com.wenda.communicationsystem.async.EventModel;
import com.wenda.communicationsystem.async.EventType;
import com.wenda.communicationsystem.model.EntryType;
import com.wenda.communicationsystem.model.Feed;
import com.wenda.communicationsystem.model.Question;
import com.wenda.communicationsystem.model.User;
import com.wenda.communicationsystem.service.FeedService;
import com.wenda.communicationsystem.service.FollowService;
import com.wenda.communicationsystem.service.QuestionService;
import com.wenda.communicationsystem.service.UserService;
import com.wenda.communicationsystem.util.JedisAdapter;
import com.wenda.communicationsystem.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Author Liguangzhe
 * @Date created in 15:43 2020/6/25
 */
@Component
public class FeedHandler implements EventHandler {
    @Autowired
    FeedService feedService;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

//    static int acId = 0;
    private String buildFeedData(EventModel eventModel) {
        System.out.println("jinru");
        Map<String, String> map = new HashMap<>();
        User actor = userService.getUser(eventModel.getActorId());
        System.out.println("actor");
        if (actor == null) {
            return null;
        }
        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());
        if (eventModel.getType().equals(EventType.COMMENT) ||
                (eventModel.getType().equals(EventType.FOLLOW) && eventModel.getEntryType() == EntryType.ENTRY_QUESTION)) {
            System.out.println("okk");
            Question question = questionService.selectQuestionById(eventModel.getEntryId());
            if (question == null) {
                return null;
            }
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            System.out.println("map" + map);
            return JSONObject.toJSONString(map);
        }
        return null;
    }

    @Override
    public void doHandler(EventModel eventModel) {
        Feed feed = new Feed();
//        feed.setId(acId++);
        feed.setCreatedDate(new Date());
        feed.setType(eventModel.getType().getValue());
        feed.setUserId(eventModel.getActorId());
        feed.setData(buildFeedData(eventModel));
        System.out.println("feed");
        System.out.println("feed.getData()" + "," + feed.getData());
        if (feed.getData() == null) {
            System.out.println("feeddataNULL");
            return;
        }
        feedService.addFeed(feed);
        feed = feedService.getByUserId(eventModel.getActorId());
        List<Integer> followers = followService.getFollowers(EntryType.ENTRY_USER, eventModel.getActorId(), Integer.MAX_VALUE);
//        followers.add(0);
//        System.out.println("ok" + followers);
        for (int follower: followers) {
            String timelineKey = RedisKeyUtil.getTimelineKey(follower);
            System.out.println(timelineKey + "2209" + "," + feed.getId());
            jedisAdapter.lpush(timelineKey, String.valueOf(feed.getId()));
        }

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.FOLLOW, EventType.COMMENT});
    }
}
