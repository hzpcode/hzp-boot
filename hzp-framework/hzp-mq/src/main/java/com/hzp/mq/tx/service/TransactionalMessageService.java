package com.hzp.mq.tx.service;

import com.hzp.mq.tx.model.po.TransactionalMessage;
import com.hzp.mq.tx.repository.TransactionalMessageDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 事务消息服务层
 *
 * @author Yu
 * @date 2021/03/22 22:23
 **/

@Slf4j
@RequiredArgsConstructor
public class TransactionalMessageService {

    private final TransactionalMessageDao transactionalMessageDao;

    public void deleteByIds(Collection<Long> ids) {
        transactionalMessageDao.deleteByIds(ids);
    }

    public void deleteById(Long id) {
        transactionalMessageDao.deleteById(id);
    }

    public void saveSelective(TransactionalMessage record) {
        transactionalMessageDao.insertSelective(record);
    }

    public void updateStatusSelective(TransactionalMessage record) {
        transactionalMessageDao.updateStatusSelective(record);
    }

    public List<TransactionalMessage> queryPendingSendRecords(LocalDateTime minScheduleTime,
                                                              LocalDateTime maxScheduleTime,
                                                              int limit) {
        return transactionalMessageDao.queryPendingCompensationRecords(minScheduleTime, maxScheduleTime, limit);
    }

    public List<Long> queryIdsByStatusAndCreateTime(Integer messageStatus, LocalDateTime time) {
        return transactionalMessageDao.queryIdsByStatusAndCreateTime(messageStatus, time);
    }

}
