package com.hzp.rabbit.trace;


import com.hzp.mq.base.constant.MessageConstant;
import com.hzp.web.constant.TraceConstant;
import org.slf4j.MDC;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.util.StringUtils;

/**
 * 链路拦截器
 *
 * @author Yu
 * @date 2021/03/29 23:44
 **/
public class TraceMessageSendPostProcessor implements MessagePostProcessor {

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        if (StringUtils.isEmpty(message.getMessageProperties().getHeader(MessageConstant.TRACE_HEADER_NAME))) {
            String traceId = MDC.get(TraceConstant.TRACE_ID_NAME);
            message.getMessageProperties().setHeader(MessageConstant.TRACE_HEADER_NAME, traceId);

        }
        return message;
    }
}
