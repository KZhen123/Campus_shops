package com.service;

import com.entity.Reply;
import com.mapper.ReplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  评论回复 服务类
 * </p>
 *

 */
@Service
@Transactional
public class ReplyService {
    @Autowired
    private ReplyMapper replyMapper;

    /**插入回复*/
    public Integer insetReply(Reply reply){
        return replyMapper.insetReply(reply);
    }
    /**查询回复*/
    public List<Reply> queryReply(String cid){
        return replyMapper.queryReplys(cid);
    }
    /**查询回复中用户信息*/
    public Reply queryById(String rid){
        return replyMapper.queryById(rid);
    }
    /**删除回复*/
    public Integer deleteReply(Reply reply){
        return replyMapper.deleteReply(reply);
    }

    public List<Reply> queryPage(int page, int size, String content) {
        int begin = (page - 1) * size;
        List<Reply> replies = replyMapper.queryPageList(begin, size, content);
        return replies;
    }

    public int getCount(String content) {
        return replyMapper.getCount(content).intValue();
    }

    public boolean setValid(String rid) {
        return replyMapper.setValid(rid);
    }

    public boolean setInvalid(String rid) {
        return replyMapper.setInvalid(rid);
    }
}
