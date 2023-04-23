package com.hzp.mq.base.model.dto;

/**
 * 统一消息抽象
 *
 * @author Yu
 * @date 2021/03/17 22:28
 **/
public interface Message<T> {

    void setBusinessModule(String businessModule);

    void setBusinessKey(String businessKey);

    void setTraceId(String traceId);

    void setTopic(String topic);

    void setTag(String tag);

    void setMqType(Integer mqType);

    void setContent(T content);



    String getBusinessModule();

    String getBusinessKey();

    String getTraceId();

    String getTopic();

    String getTag();

    Integer getMqType();

    T getContent();

}
