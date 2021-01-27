package com.wenda.communicationsystem.controller;

import com.wenda.communicationsystem.async.EventModel;
import com.wenda.communicationsystem.async.EventProducer;
import com.wenda.communicationsystem.async.EventType;
import com.wenda.communicationsystem.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author Liguangzhe
 * @Date created in 22:11 2020/4/15
 */
@Controller
public class LoginController {
    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/reg"}, method = RequestMethod.POST)
    public String register(Model model,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam(value = "next", required = false) String next,
                           HttpServletResponse response) {
        try {
            Map<String,String> map = userService.register(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                if (StringUtils.isNotBlank(next)) {
                    return "redirect:" + next;
                }
                //System.out.println("ok");
                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        } catch(Exception err) {
            return "login";
        }
    }

    @RequestMapping(path = {"/login"}, method = RequestMethod.POST)
    public String login(Model model,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam(value = "next", required = false) String next,
                           @RequestParam(value = "remember", defaultValue = "false") boolean remember,
                           HttpServletResponse response) {
        try {
            Map<String,String> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                if (remember) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);

                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
                        .setExt("username", username)
                        .setExt("email", "360238076@qq.com")
                        .setActorId(Integer.parseInt(map.get("userId"))));

                if (StringUtils.isNotBlank(next)) {
                    System.out.println("redirect:" + next);
                    return "redirect:" + next;
                }
                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        } catch(Exception err) {
            System.out.println(""+err);
            return "login";
        }
    }
    @RequestMapping(path = {"/reglogin"}, method = RequestMethod.GET)
    public String reglogin(Model model,
                          @RequestParam(value = "next", defaultValue = "", required = false) String next) {
        model.addAttribute("next", next);
        return "login";

    }
    @RequestMapping(path = {"/logout"}, method = RequestMethod.GET)
    public String relogin(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "login";

    }
}
