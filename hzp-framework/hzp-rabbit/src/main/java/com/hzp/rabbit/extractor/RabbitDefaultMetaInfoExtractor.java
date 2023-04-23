package com.hzp.rabbit.extractor;


import com.hzp.mq.base.constant.MessageConstant;
import com.hzp.mq.idempotent.extractor.MessageMetaInfo;
import com.hzp.mq.idempotent.extractor.MetaInfoExtractor;
import org.springframework.amqp.core.Message;

import java.util.Map;

/**
 * RabbitMQ默认提取器
 *
 * @author Yu
 * @date 2021/04/24 00:00
 **/

public class RabbitDefaultMetaInfoExtractor implements MetaInfoExtractor {

    @Override
    public boolean isMatch(Object message) {
        return message instanceof Message;
    }

    @Override
    public MessageMetaInfo extract(Object message) {
        Message rabbitMsg = (Message) message;
        Map<String, Object> headers = rabbitMsg.getMessageProperties().getHeaders();
        String bizModule = (String) headers.get(MessageConstant.BIZ_MODULE_HEADER_NAME);
        String bizKey = (String) headers.get(MessageConstant.BIZ_KEY_HEADER_NAME);
        String messageId = rabbitMsg.getMessageProperties().getMessageId();
        String traceId = rabbitMsg.getMessageProperties().getHeader(MessageConstant.TRACE_HEADER_NAME);

        return new MessageMetaInfo()
                .setBusinessKey(bizKey)
                .setBusinessModule(bizModule)
                .setMessageId(messageId)
                .setTraceId(traceId)
                .setIdempotentKey(rabbitMsg.getMessageProperties().getConsumerQueue() + ":" + bizModule + ":" + bizKey);
    }
}
