package com.hzp.mq.tx.repository;



import com.hzp.mq.tx.model.po.TransactionalMessageContent;

import java.util.Collection;
import java.util.List;

/**
 * @author Yu
 * @date 2021/03/17 22:22
 **/
public interface TransactionalMessageContentDao {

    void deleteByMsgIds(Collection<Long> msgIds);

    void deleteByMsgId(Long msgId);

    void insert(TransactionalMessageContent record);

    List<TransactionalMessageContent> queryByMessageIds(Collection<Long> messageIds);

}
