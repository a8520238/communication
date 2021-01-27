package com.wenda.communicationsystem.service;

import com.wenda.communicationsystem.mapper.LoginTicketMapper;
import com.wenda.communicationsystem.mapper.UserMapper;
import com.wenda.communicationsystem.model.LoginTicket;
import com.wenda.communicationsystem.model.User;
import com.wenda.communicationsystem.util.ForumUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author Liguangzhe
 * @Date created in 21:54 2020/4/8
 */

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    LoginTicketMapper loginTicketMapper;

    public User getUser(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKey(User user) {

        return userMapper.updateByPrimaryKey(user);
    }

    public User getUserByName(String name) {
        return userMapper.selectByName(name);
    }

    public Map<String, String> register(String username, String password) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        User user = userMapper.selectByName(username);
        if (user != null) {
            map.put("msg", "用户已被注册");
            return map;
        }
        user = new User();
        user.setName(username);
        user.setHeadUrl("/images/img/2.jpg");
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setPassword(ForumUtil.MD5(password + user.getSalt()));
        userMapper.insert(user);

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    public Map<String, String> login(String username, String password) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("msg", "用户名无效");
            return map;
        }
        if (!ForumUtil.MD5(password + user.getSalt()).equals(user.getPassword())){
            map.put("msg", "密码错误");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        map.put("userId", String.valueOf(user.getId()));
        return map;
    }

    public String addLoginTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);

        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.DAY_OF_MONTH, curr.get(Calendar.DAY_OF_MONTH) + 7);
        Date date = curr.getTime();

        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));

        loginTicketMapper.addTicket(loginTicket);

        return loginTicket.getTicket();
    }

    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
    }
}