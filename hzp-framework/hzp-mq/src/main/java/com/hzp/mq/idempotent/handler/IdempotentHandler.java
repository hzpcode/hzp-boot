package com.hzp.mq.idempotent.handler;

/**
 * 幂等处理器
 *
 * @author Yu
 * @date 2021/03/23 00:04
 **/


public interface IdempotentHandler {
    /**
     * 消息是否已经处理过
     *
     * @param message 投递过来的消息
     * @return 是否已经处理
     */
    boolean isProcessed(Object message);

    /**
     * 标记消息是否处理
     *
     * @param message 消息
     * @param e       消费消息是否出异常
     */
    void markProcessed(Object message, Throwable e);


}
