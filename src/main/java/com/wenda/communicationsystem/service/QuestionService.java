package com.wenda.communicationsystem.service;

import com.wenda.communicationsystem.mapper.QuestionMapper;
import com.wenda.communicationsystem.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author Liguangzhe
 * @Date created in 21:54 2020/4/8
 */

@Service
public class QuestionService {
    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    SentiveService sentiveService;

    public Question selectQuestionById(int id) {

        return questionMapper.selectByPrimaryKey(id);

    }

    public int addQuestion(Question question) {
        //敏感词过滤
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));

        question.setContent(sentiveService.filter(question.getContent()));
        question.setTitle(sentiveService.filter(question.getTitle()));
        int res = questionMapper.insert(question);
        String content = question.getContent();
        int qsId = question.getId();
        return res > 0? qsId: 0;
    }

    public List<Question> selectLatestQuestions(int userId, int offset, int limit) {
        return questionMapper.selectLatestQuestions(offset, limit);
    }

//    public void addQuestion(Question question) {
//        questionMapper.insert(question);
//    }

    public Question selectByPrimaryKey(Integer id) {

        return questionMapper.selectByPrimaryKey(id);
    }

    public int updateCommentCount(int id, int count) {
        return questionMapper.updateCommentCount(id, count);
    }
}
