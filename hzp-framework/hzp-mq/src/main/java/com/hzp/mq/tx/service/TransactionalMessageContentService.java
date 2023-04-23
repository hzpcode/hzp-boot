package com.hzp.mq.tx.service;

import com.hzp.mq.tx.model.po.TransactionalMessageContent;
import com.hzp.mq.tx.repository.TransactionalMessageContentDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 事务消息内容服务层
 *
 * @author Yu
 * @date 2021/03/17 23:33
 **/

@Slf4j
@RequiredArgsConstructor
public class TransactionalMessageContentService {

    private final TransactionalMessageContentDao transactionalMessageContentDao;

    public void deleteByMsgId(Long msgId) {
        transactionalMessageContentDao.deleteByMsgId(msgId);
    }

    public void deleteByMsgIds(Collection<Long> msgIds) {
        transactionalMessageContentDao.deleteByMsgIds(msgIds);
    }

    public List<TransactionalMessageContent> findByMessageIds(Collection<Long> msgIds) {
        if (CollectionUtils.isEmpty(msgIds)) {
            return new ArrayList<>();
        }
        return transactionalMessageContentDao.queryByMessageIds(msgIds);
    }

    public void save(TransactionalMessageContent record) {
        transactionalMessageContentDao.insert(record);
    }
}
