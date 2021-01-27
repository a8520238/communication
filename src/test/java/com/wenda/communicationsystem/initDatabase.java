package com.wenda.communicationsystem;

import com.wenda.communicationsystem.mapper.QuestionMapper;
import com.wenda.communicationsystem.mapper.UserMapper;
import com.wenda.communicationsystem.model.Question;
import com.wenda.communicationsystem.model.User;
import com.wenda.communicationsystem.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

/**
 * @Author Liguangzhe
 * @Date created in 17:27 2020/4/8
 */
//@RunWith(SpringRunner.class)

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class initDatabase {
    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserService userService;

    @Test
    public void initDatabase() {
        //Random random = new Random();

//        for (int i = 0; i < 11; i++) {
//            User user = new User();
//            user.setId(i+1);
//            user.setName(String.format("User%d", i+1));
//            user.setHeadUrl(String.format("C:\\Users\\36023\\Desktop\\communicationsystem\\src\\main\\resources\\static\\images\\img\\%d.jpg", i+1));
//            user.setPassword("");
//            user.setSalt("");
//            System.out.println(user.getName());
//            userMapper.insert(user);
//        }

        for (int i = 0; i < 11; i++) {
            User user = userService.getUser(i+1);
            user.setHeadUrl(String.format("/images/img/%d.jpg", i+1));
            userMapper.updateByPrimaryKey(user);
        }

//        for (int i = 0; i < 11; i++) {
//            Date date = new Date();
//            date.setTime(date.getTime() + 100 * 3600 * i);
//            Question question = new Question();
//            question.setCommentCount(i);
//            question.setContent(String.format("content%d", i+1));
//            question.setCreatedDate(date);
//            question.setId(i+1);
//            question.setTitle(String.format("Title%d", i+1));
//            question.setUserId(i+1);
//            questionMapper.insert(question);
//        }
    }
}
