package com.hzp.mq.tx.repository;



import com.hzp.mq.tx.model.po.TransactionalMessage;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 事务消息DAO
 *
 * @author Yu
 * @date 2021/03/17 22:20
 **/
public interface TransactionalMessageDao {

    void deleteById(Long id);

    void deleteByIds(Collection<Long> ids);

    void insertSelective(TransactionalMessage record);

    void updateStatusSelective(TransactionalMessage record);

    List<Long> queryIdsByStatusAndCreateTime(Integer messageStatus, LocalDateTime time);

    List<TransactionalMessage> queryPendingCompensationRecords(LocalDateTime minScheduleTime,
                                                               LocalDateTime maxScheduleTime,
                                                               int limit);

}
