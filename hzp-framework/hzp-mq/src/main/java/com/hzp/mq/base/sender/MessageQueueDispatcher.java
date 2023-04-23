package com.hzp.mq.base.sender;

import com.hzp.mq.base.model.dto.Message;
import com.hzp.mq.tx.model.po.TransactionalMessage;
import com.hzp.web.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.List;
import java.util.Optional;

/**
 * 消息转发者
 *
 * @author Yu
 * @date 2021/04/22 23:31
 **/

@RequiredArgsConstructor
public class MessageQueueDispatcher {

    private final List<MessageQueueSender> messageQueueSenderList;

    public void send(TransactionalMessage message, String content) {
        Optional<MessageQueueSender> sender = messageQueueSenderList.stream().filter(o -> o.isMatch(message)).findFirst();
        if (!sender.isPresent()) {
            throw new BizException("can`t find message sender");
        }
        sender.ifPresent(o -> o.sendMessageSyncAndMarkSuccess(message, content));
    }

    public void sendTransactionMessage(Message<?> defaultMessage) {
        Optional<MessageQueueSender> sender = messageQueueSenderList.stream().filter(o -> o.isMatch(defaultMessage)).findFirst();
        if (!sender.isPresent()) {
            throw new BizException("can`t find message sender");
        }

        if (StringUtils.isBlank(defaultMessage.getTraceId())) {
            // todo
//            defaultMessage.setTraceId(MDC.get(TraceConstant.TRACE_ID_NAME));
        }

        sender.ifPresent(o -> o.sendTransactionalMessage(defaultMessage));
    }

}
