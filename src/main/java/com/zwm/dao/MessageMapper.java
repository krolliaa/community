package com.zwm.dao;

import com.zwm.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    //查询当前用户的所有会话，每个会话只显示私信最新一条消息，分页查询【排除系统通知即 from_id != 2】
    public abstract List<Message> selectConversations(int userId, int start, int limit);

    //查询当前用户的会话数量
    public abstract int selectConversationCount(int userId);

    //查询某个会话中所包含的私信列表
    public abstract List<Message> selectLetters(String conversationId, int start, int limit);

    //查询某个会话中所包含的私信列表的数量
    public abstract int selectLetterCount(String conversationId);

    //查询某个会话中未读私信的数量
    public abstract int selectLetterUnreadCount(int userId, String conversationId);

    //添加私信
    public abstract int insertMessage(Message message);

    //修改私信状态 ---> 一个会话里头有多个私信，一旦打开会话需要将该会话中的所有私信都设置为已读
    public abstract int updateStatus(List<Integer> ids, int status);
}
