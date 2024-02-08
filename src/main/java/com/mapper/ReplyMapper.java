package com.mapper;

import com.entity.Reply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  评论回复 Mapper 接口
 * </p>
 *

 */
public interface ReplyMapper {
    /**插入回复*/
    Integer insetReply(Reply reply);
    /**查询回复*/
    List<Reply> queryReplys(String cid);
    /**查询回复中用户信息*/
    Reply queryById(String rid);
    /**删除回复*/
    Integer deleteReply(Reply reply);

    List<Reply> queryPageList(@Param("begin") Integer begin, @Param("size") Integer size, @Param("recontent") String recontent);

    Long getCount(String recontent);

    boolean setInvalid(String rid);

    boolean setValid(String rid);
}
