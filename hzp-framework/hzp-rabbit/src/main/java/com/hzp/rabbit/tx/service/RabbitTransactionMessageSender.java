package com.hzp.rabbit.tx.service;

import cn.hutool.json.JSONUtil;
import com.hzp.mq.base.constant.MessageConstant;
import com.hzp.mq.base.constant.MqType;
import com.hzp.mq.base.model.dto.Message;
import com.hzp.mq.base.sender.MessageQueueSender;
import com.hzp.mq.base.util.MessageHelper;
import com.hzp.mq.tx.model.po.TransactionalMessage;
import com.hzp.mq.tx.service.TransactionalMessageManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Yu
 * @date 2021/03/17 22:33
 **/

@Slf4j
@RequiredArgsConstructor
public class RabbitTransactionMessageSender implements MessageQueueSender {

    private final AmqpAdmin amqpAdmin;
    private final TransactionalMessageManagementService managementService;
    private final RabbitTemplate rabbitTemplate;
    private final MessageHelper messageHelper;

    private static final ConcurrentMap<String, Boolean> EXCHANGE_ALREADY_DECLARE = new ConcurrentHashMap<>();

    @Override
    public boolean isMatch(TransactionalMessage message) {
        return MqType.RABBIT.getType().equals(message.getMqType());
    }

    @Override
    public boolean isMatch(Message<?> txMessage) {
        return MqType.RABBIT.getType().equals(txMessage.getMqType());
    }

    @Override
    public void sendTransactionalMessage(Message<?> txMessage) {
        sendTransactionalMessage(messageHelper.convert2CommonTxMessage(txMessage), messageHelper.convert2Json(txMessage.getContent()));
    }

    @Override
    public void sendTransactionalMessage(TransactionalMessage record, String content) {

        managementService.saveTransactionalMessageRecord(record, content);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                sendMessageSyncAndMarkSuccess(record, content);
            }
        });
    }

    @Override
    public void sendMessageSyncAndMarkSuccess(TransactionalMessage message, String content) {
        try {
            rabbitTemplate.convertAndSend(message.getTopic(), message.getRoutingKey(), content, o -> {
                o.getMessageProperties().setHeader(MessageConstant.BIZ_MODULE_HEADER_NAME, message.getBusinessModule());
                o.getMessageProperties().setHeader(MessageConstant.BIZ_KEY_HEADER_NAME, message.getBusinessKey());
                o.getMessageProperties().setHeader(MessageConstant.TRACE_HEADER_NAME, message.getTraceId());
                o.getMessageProperties().setContentType(JSONUtil.isJson(content) ? MessageProperties.CONTENT_TYPE_JSON : MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
                return o;
            });

            if (log.isDebugEnabled()) {
                log.debug("发送消息成功,topic/exchange:{}, tag/routingKey:{} ,消息内容:{}", message.getTopic(), message.getRoutingKey(), content);
            }
            managementService.markSuccess(message);
        } catch (Exception e) {
            managementService.markFail(message, e);
        }
    }

    public void declareExchange(String exchangeName, String exchangeType) {
        EXCHANGE_ALREADY_DECLARE.computeIfAbsent(exchangeName, k -> {
            Exchange exchange = new CustomExchange(exchangeName, exchangeType);
            amqpAdmin.declareExchange(exchange);
            return true;
        });
    }


}
