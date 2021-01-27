package com.wenda.communicationsystem.mapper;

import com.wenda.communicationsystem.model.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);

    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("limit") Integer limit,
                                        @Param("offset") Integer offset);

    int getConversationUnreadCount(@Param("userId") int userId,
                                   @Param("conversationId") String conversationId);

    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("limit") int limit,
                                      @Param("offset") int offset);

    void updateHasRead(@Param("toId") int toId);

}