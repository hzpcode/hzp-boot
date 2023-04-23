package com.hzp.rabbit.tx.model.dto;

import com.hzp.mq.base.constant.MqType;
import com.hzp.mq.base.model.dto.Message;
import lombok.Data;

/**
 * 默认事务消息
 *
 * @author Yu
 * @date 2021/03/17 22:29
 **/

@Data
public class RabbitMessage<T> implements Message<T> {

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
     * topic/exchange
     */
    private String exchange;

    /**
     * tag/routingKey
     */
    private String routingKey;

    /**
     * 发送内容(不需要手动序列化)
     */
    private T content;

    /**
     * 请使用exchange
     *
     * @param topic 主题
     */
    @Override
    @Deprecated
    public void setTopic(String topic) {
        this.exchange = topic;
    }

    /**
     * 请使用routingKey
     *
     * @param tag 消费消费过滤tag
     */
    @Override
    @Deprecated
    public void setTag(String tag) {
        this.routingKey = tag;
    }

    @Override
    public void setMqType(Integer mqType) {

    }

    @Override
    public Integer getMqType() {
        return MqType.RABBIT.getType();
    }

    @Override
    public String getTopic() {
        return exchange;
    }

    @Override
    public String getTag() {
        return routingKey;
    }
}
