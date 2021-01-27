package com.wenda.communicationsystem.controller;

import com.wenda.communicationsystem.model.*;
import com.wenda.communicationsystem.service.CommentService;
import com.wenda.communicationsystem.service.FollowService;
import com.wenda.communicationsystem.service.QuestionService;
import com.wenda.communicationsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Liguangzhe
 * @Date created in 22:36 2020/4/7
 */
@Controller
public class indexController {

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    FollowService followService;

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    private List<ViewObject> getViewObjects(int userId, int offset, int limit) {
        List<Question> questions = questionService.selectLatestQuestions(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question: questions) {
            //System.out.println(question.getId());
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("followCount", followService.getFollowerCount(EntryType.ENTRY_QUESTION, question.getId()));
            vo.set("user", userService.getUser(question.getUserId()));
            //System.out.println(userService.getUser(question.getUserId()));
            //vo.set("user", userService.getUser(userId));
            //System.out.println(vo.get("user"));
            vos.add(vo);
            //System.out.println(userService.getUser(question.getUserId()).getName());
        }
//        Question test = questionService.selectByPrimaryKey(1);
//        System.out.println(test.getCreatedDate());
        return vos;
    }

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object index(Model model) {
        model.addAttribute("vos", getViewObjects(0, 0, 10));
        return "index";
    }

    @RequestMapping(path = "/user/{userId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {

        User user = userService.getUser(userId);
        ViewObject vo = new ViewObject();
        vo.set("user", user);
        vo.set("commentCount", commentService.getUserCommentCount(userId));
        vo.set("followerCount", followService.getFollowerCount(EntryType.ENTRY_USER, userId));
        vo.set("followeeCount", followService.getFolloweeCount(EntryType.ENTRY_USER, userId));

        if (hostHolder.getUser() != null) {
            vo.set("followed", followService.isFollower(hostHolder.getUser().getId(), EntryType.ENTRY_USER, userId));
        } else {
            vo.set("followed", false);
        }

        model.addAttribute("profileUser", vo);
        //model.addAttribute("vos", getViewObjects(0,0,10));
        return "profile";
    }
}
