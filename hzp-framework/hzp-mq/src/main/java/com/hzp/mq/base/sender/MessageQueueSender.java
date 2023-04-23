package com.hzp.mq.base.sender;


import com.hzp.mq.base.model.dto.Message;
import com.hzp.mq.tx.model.po.TransactionalMessage;

/**
 * MQ发送者
 *
 * @author Yu
 * @date 2021/04/22 23:31
 **/
public interface MessageQueueSender {

    boolean isMatch(TransactionalMessage message);

    boolean isMatch(Message<?> txMessage);

    void sendTransactionalMessage(Message<?> txMessage);

    void sendTransactionalMessage(TransactionalMessage transactionalMessage, String content);

    void sendMessageSyncAndMarkSuccess(TransactionalMessage transactionalMessage, String content);

}
