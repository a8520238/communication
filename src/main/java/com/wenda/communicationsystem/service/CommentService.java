package com.wenda.communicationsystem.service;

import com.wenda.communicationsystem.mapper.CommentMapper;
import com.wenda.communicationsystem.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author Liguangzhe
 * @Date created in 17:57 2020/5/27
 */
@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;

    @Autowired
    SentiveService sentiveService;

    public List<Comment> getCommentListByEntry(int entryId, int entryType) {
        return commentMapper.selectCommentByEntry(entryId, entryType);
    }

    public int addComment(Comment comment) {
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sentiveService.filter(comment.getContent()));
        return commentMapper.insert(comment) > 0? comment.getId(): 0;
    }

    public int getCommentCount(int entryId, int entryType) {
        return commentMapper.getCommentCount(entryId, entryType);
    }

    public int getUserCommentCount(int userId) {
        return commentMapper.getUserCommentCount(userId);
    }

    public boolean deleteComment(int commentId) {
        return commentMapper.updateStatus(commentId, 1) > 0;
    }

    public Comment getCommentById(int commentId) {
        return commentMapper.selectByPrimaryKey(commentId);
    }
}
