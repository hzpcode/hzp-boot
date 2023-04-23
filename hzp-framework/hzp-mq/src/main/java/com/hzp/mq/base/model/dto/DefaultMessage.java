package com.hzp.mq.base.model.dto;

import lombok.Data;

/**
 * 默认事务消息
 *
 * @author Yu
 * @date 2021/03/17 22:29
 **/

@Data
public class DefaultMessage<T> implements Message<T> {

    /**
     * 业务模块(支付...)，防止不同业务模块使用相同业务id，重复错误
     */
    private String businessModule;

    /**
     * 业务id（必须保证全局唯一）
     */
    private String businessKey;

    /**
     * 不需要自己设置
     */
    private String traceId;
    /**
     * exchange/topic
     */
    private String topic;

    /**
     * @see com.hzp.mq.base.constant.MqType
     */
    private Integer mqType;
    /**
     * routingKey/tag
     */
    private String tag;

    /**
     * 发送内容(不需要手动序列化)
     */
    private T content;

}
