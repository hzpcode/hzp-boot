package com.hzp.rabbit.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedList;
import java.util.List;

/**
 * rabbitmq配置
 *
 * @author Yu
 * @date 2021/03/29 00:37
 **/

@Data
@ConfigurationProperties(prefix = "hzp.boot.mq.rabbit")
public class RabbitMqExProperties {

    private boolean enabled = true;

    private Producer producer = new Producer();
    private Consumer consumer = new Consumer();

    @Data
    public static class Producer {

        private List<ProducerDeclareInfo> declare = new LinkedList<>();

    }

    @Data
    public static class Consumer {

        private List<ConsumerDeclareInfo> declare = new LinkedList<>();

    }



    @Data
    public static class ConsumerDeclareInfo {

        private String queue;

        private String exchange;

        private String routingKey;

        private String exchangeType;

    }

    @Data
    public static class ProducerDeclareInfo {

        private String exchange;

        private String exchangeType;

    }



}
