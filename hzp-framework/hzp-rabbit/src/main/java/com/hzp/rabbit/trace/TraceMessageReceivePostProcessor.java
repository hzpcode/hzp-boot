package com.hzp.rabbit.trace;


import com.hzp.mq.base.constant.MessageConstant;
import com.hzp.web.constant.TraceConstant;
import org.slf4j.MDC;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

/**
 * @author Yu
 * @date 2021/07/18 18:38
 **/
public class TraceMessageReceivePostProcessor implements MessagePostProcessor {

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        MDC.put(TraceConstant.TRACE_ID_NAME, message.getMessageProperties().getHeader(MessageConstant.TRACE_HEADER_NAME));
        return message;
    }
}
