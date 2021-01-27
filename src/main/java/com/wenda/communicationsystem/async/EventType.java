package com.wenda.communicationsystem.async;

/**
 * @Author Liguangzhe
 * @Date created in 14:52 2020/6/9
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3),
    FOLLOW(4),
    UNFOLLOW(5)
    ;

    private int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
