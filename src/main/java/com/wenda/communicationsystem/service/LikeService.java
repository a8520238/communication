package com.wenda.communicationsystem.service;

import com.wenda.communicationsystem.util.JedisAdapter;
import com.wenda.communicationsystem.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Liguangzhe
 * @Date created in 16:16 2020/6/4
 */
@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;

    public long getLikeCount(int entryType, int entryId) {
        String key = RedisKeyUtil.getLikeKey(entryType, entryId);
        return jedisAdapter.scard(key);
    }

    public int getLikeStatus(int userId, int entryType, int entryId) {
        String LikeKey = RedisKeyUtil.getLikeKey(entryType, entryId);
        if (jedisAdapter.sismember(LikeKey, String.valueOf(userId))) {
            return 1;
        }
        String DisLikeKey = RedisKeyUtil.getDisLikeKey(entryType, entryId);
        return jedisAdapter.sismember(DisLikeKey, String.valueOf(userId))? -1: 0;
    }

    public long like(int userId, int entryType, int entryId) {
        String LikeKey = RedisKeyUtil.getLikeKey(entryType, entryId);
        jedisAdapter.sadd(LikeKey, String.valueOf(userId));

        String DisLikeKey = RedisKeyUtil.getDisLikeKey(entryType, entryId);
        if (jedisAdapter.sismember(DisLikeKey, String.valueOf(userId))) {
            jedisAdapter.srem(DisLikeKey, String.valueOf(userId));
        }
        return jedisAdapter.scard(LikeKey);
    }

    public long disLike(int userId, int entityType, int entityId) {
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.sadd(disLikeKey, String.valueOf(userId));

        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        if (jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
            jedisAdapter.srem(likeKey, String.valueOf(userId));
        }
        return jedisAdapter.scard(likeKey);
    }
}
