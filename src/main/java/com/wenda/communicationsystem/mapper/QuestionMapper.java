package com.wenda.communicationsystem.mapper;

import com.wenda.communicationsystem.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface QuestionMapper {
//    String TABLE_NAME = " question ";
//    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    int deleteByPrimaryKey(Integer id);

    int insert(Question record);

//    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
//            ") values (#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
//    int addQuestion(Question question);

    int insertSelective(Question record);

    Question selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Question record);

    int updateByPrimaryKeyWithBLOBs(Question record);

    int updateByPrimaryKey(Question record);

    List<Question> selectLatestQuestions(@Param("offset") int offset, @Param("limit") int limit);

    //int updateCommentCount(Integer id, @Param("count") Integer count);

    //@Update({"update ", TABLE_NAME, " set comment_count=#{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") Integer id, @Param("Count") Integer Count);
}