package com.wenda.communicationsystem.controller;

import com.wenda.communicationsystem.model.EntryType;
import com.wenda.communicationsystem.model.Feed;
import com.wenda.communicationsystem.model.HostHolder;
import com.wenda.communicationsystem.service.FeedService;
import com.wenda.communicationsystem.service.FollowService;
import com.wenda.communicationsystem.util.JedisAdapter;
import com.wenda.communicationsystem.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Liguangzhe
 * @Date created in 14:27 2020/6/25
 */
@Controller
public class FeedController {
    @Autowired
    FeedService feedService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    JedisAdapter jedisAdapter;

    @Autowired
    FollowService followService;

    @RequestMapping(path = "/pullfeeds", method = RequestMethod.GET)
    public String getPullFeeds(Model model) {
        int localUserId = hostHolder.getUser() == null? 0: hostHolder.getUser().getId();

        List<Integer> followees = new ArrayList<>();
        if (localUserId != 0) {
            followees = followService.getFollowees(EntryType.ENTRY_USER, localUserId, Integer.MAX_VALUE);
        }
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 10);
        System.out.println(feeds);
        for (Feed feed: feeds) {
            System.out.println(feed.getData());
        }
        model.addAttribute("feeds", feeds);
        return "feeds";
    }

    @RequestMapping(path = "/pushfeeds", method = RequestMethod.GET)
    public String getPushFeeds(Model model) {
        int localUserId = hostHolder.getUser() == null? 0: hostHolder.getUser().getId();
        System.out.println(localUserId + "," + RedisKeyUtil.getTimelineKey(localUserId));
        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId), 0, 10);
        System.out.println("feedIds" + "," + feedIds);
        List<Feed> feeds = new ArrayList<>();
        if (feedIds.size() != 0) {
            for (String id: feedIds) {
                Feed feed = feedService.getById(Integer.parseInt(id));
                System.out.println(feed);
                System.out.println("getfeeder");
                System.out.println(feed.getData());
                if (feed == null) {
                    continue;
                }
                feeds.add(feed);
            }
        }
        model.addAttribute("feeds", feeds);
        return "feeds";
    }

}
