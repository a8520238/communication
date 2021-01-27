package com.wenda.communicationsystem.service;

import com.wenda.communicationsystem.mapper.FeedMapper;
import com.wenda.communicationsystem.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Liguangzhe
 * @Date created in 22:45 2020/6/24
 */
@Service
public class FeedService {
    @Autowired
    FeedMapper feedMapper;

    public boolean addFeed(Feed feed) {
        feedMapper.insert(feed);
        return true;
    }

    public Feed getById(int id) {
        return feedMapper.selectByPrimaryKey(id);
    }

    public Feed getByUserId(int userId) {return feedMapper.selectByUserId(userId);}
    public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count) {
        return feedMapper.selectUserFeeds(maxId, userIds, count);
    }
}
