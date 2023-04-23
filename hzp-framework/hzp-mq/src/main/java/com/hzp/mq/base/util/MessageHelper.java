package com.hzp.mq.base.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzp.mq.base.constant.MqType;
import com.hzp.mq.base.model.dto.DefaultMessage;
import com.hzp.mq.base.model.dto.Message;
import com.hzp.mq.tx.model.po.TransactionalMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 消息帮助类
 *
 * @author Yu
 * @date 2021/05/01 14:06
 **/

@Getter
@RequiredArgsConstructor
public class MessageHelper {

    private final ObjectMapper objectMapper;

    public DefaultMessage<Map<?, ?>> convert2DefaultMessage(TransactionalMessage transactionalMessage, String content) {
        try {
            DefaultMessage<Map<?, ?>> message = new DefaultMessage<>();
            message.setBusinessModule(transactionalMessage.getBusinessModule());
            message.setBusinessKey(transactionalMessage.getBusinessKey());
            message.setTraceId(transactionalMessage.getTraceId());
            message.setMqType(transactionalMessage.getMqType());
            message.setTopic(transactionalMessage.getTopic());
            message.setTag(transactionalMessage.getRoutingKey());
            message.setContent(objectMapper.readValue(content, Map.class));

            return message;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public TransactionalMessage convert2CommonTxMessage(Message<?> message) {

        return new TransactionalMessage()
                .setBusinessModule(message.getBusinessModule())
                .setBusinessKey(message.getBusinessKey())
                .setTraceId(message.getTraceId())
                .setTopic(message.getTopic())
                .setRoutingKey(message.getTag())
                .setMqType(MqType.RABBIT.getType())

                .setCreateTime(LocalDateTime.now())
                .setEditTime(LocalDateTime.now());
    }

    public String convert2Json(Object o) {
        try{
            return objectMapper.writeValueAsString(o);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
