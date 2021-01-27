package com.wenda.communicationsystem.service;

import com.wenda.communicationsystem.util.JedisAdapter;
import com.wenda.communicationsystem.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author Liguangzhe
 * @Date created in 19:02 2020/6/10
 */
@Service
public class FollowService {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean follow(int userId, int entryType, int entryId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entryType, entryId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(entryType, userId);
        Jedis jedis = jedisAdapter.getJedis();
        Date date = new Date();

        Transaction tx = jedisAdapter.multi(jedis);
        tx.zadd(followeeKey, date.getTime(), String.valueOf(entryId));
        tx.zadd(followerKey, date.getTime(), String.valueOf(userId));
        List<Object> ret = jedisAdapter.exec(tx, jedis);
        return ret.size() == 2 && (Long)ret.get(0) > 0 && (Long)ret.get(1) > 0;
    }

    public boolean unfollow(int userId, int entryType, int entryId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entryType, entryId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(entryType, userId);
        Jedis jedis = jedisAdapter.getJedis();

        Transaction tx = jedisAdapter.multi(jedis);
        tx.zrem(followeeKey, String.valueOf(entryId));
        tx.zrem(followerKey, String.valueOf(userId));
        List<Object> ret = jedisAdapter.exec(tx, jedis);
        return ret.size() == 2 && (Long)ret.get(0) > 0 && (Long)ret.get(1) > 0;
    }

    private List<Integer> getIdsFromSet(Set<String> set) {
        List<Integer> ids = new ArrayList<>();
        for (String id: set) {
            ids.add(Integer.parseInt(id));
        }
        return ids;
    }

    public List<Integer> getFollowers(int entryType, int entryId, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(entryType, entryId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey, 0, count));
    }

    public List<Integer> getFollowers(int entryType, int entryId, int offset, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(entryType, entryId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey, offset, count));
    }

    public List<Integer> getFollowees(int entryType, int userId, int count) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(entryType, userId);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, 0, count));
    }

    public List<Integer> getFollowees(int entryType, int userId, int offset, int count) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(entryType, userId);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, offset, count));
    }

    public long getFollowerCount(int entryType, int entryId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entryType, entryId);
        return jedisAdapter.zcard(followerKey);
    }

    public long getFolloweeCount(int entryType, int userId) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(entryType, userId);
        return jedisAdapter.zcard(followeeKey);
    }

    public boolean isFollower(int userId, int entryType, int entryId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entryType, entryId);
        return jedisAdapter.zscore(followerKey, String.valueOf(userId)) != null;
    }
}
