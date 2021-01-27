package com.wenda.communicationsystem.util;

/**
 * @Author Liguangzhe
 * @Date created in 16:17 2020/6/4
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";

    private static String BIZ_EVENTQUEUE = "EVENTQUEUE";
    private static String BIZ_TIMELINE = "TIMELINE";

    private static String BIZ_FOLLOWER = "FOLLOWER";
    private static String BIZ_FOLLOWEE = "FOLLOWEE";

    public static String getBizEventQueue() {
        return BIZ_EVENTQUEUE;
    }

    public static String getLikeKey(int entryType, int entryId) {
        return BIZ_LIKE + SPLIT + String.valueOf(entryType) + SPLIT + String.valueOf(entryId);
    }

    public static String getDisLikeKey(int entryType, int entryId) {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entryType) + SPLIT + String.valueOf(entryId);
    }

    public static String getFollowerKey(int entryType, int entryId) {
        return BIZ_FOLLOWER + SPLIT + String.valueOf(entryType) + SPLIT + String.valueOf(entryId);
    }

    public static String getFolloweeKey(int entryType, int userId) {
        return BIZ_FOLLOWEE + SPLIT + String.valueOf(entryType) + SPLIT + String.valueOf(userId);
    }

    public static String getTimelineKey(int userId) {
        return BIZ_TIMELINE + SPLIT + String.valueOf(userId);
    }
}
