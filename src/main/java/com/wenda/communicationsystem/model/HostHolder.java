package com.wenda.communicationsystem.model;

import org.springframework.stereotype.Component;

/**
 * @Author Liguangzhe
 * @Date created in 21:29 2020/4/16
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<>();
    public User getUser() {
        return users.get();
    }
    public void setUser(User user) {
        users.set(user);
    }
    public void clear() {
        users.remove();
    }
}
