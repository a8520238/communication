package com.wenda.communicationsystem.mapper;

import com.wenda.communicationsystem.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);
    List<Comment> selectCommentByEntry(int entryId, int entryType);

    int getCommentCount(@Param("entryId") int entryId, @Param("entryType") int entryType);

    int updateStatus(int id, int status);

    int getUserCommentCount(@Param("userId") int userId);
}