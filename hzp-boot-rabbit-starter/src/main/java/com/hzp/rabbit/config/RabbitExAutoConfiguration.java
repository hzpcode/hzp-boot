package com.hzp.rabbit.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzp.mq.base.util.MessageHelper;
import com.hzp.mq.idempotent.extractor.MetaInfoExtractor;
import com.hzp.mq.tx.service.TransactionalMessageManagementService;
import com.hzp.rabbit.extractor.RabbitDefaultMetaInfoExtractor;
import com.hzp.rabbit.prop.RabbitMqExProperties;
import com.hzp.rabbit.trace.TraceMessageReceivePostProcessor;
import com.hzp.rabbit.trace.TraceMessageSendPostProcessor;
import com.hzp.rabbit.tx.service.RabbitTransactionMessageSender;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;

/**
 * rabbitmq自动配置类
 *
 * @author Yu
 * @date 2021/03/29 00:36
 **/
@Configuration
@ConditionalOnProperty(prefix = "hzp.boot.mq.rabbit", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({RabbitMqExProperties.class})
public class RabbitExAutoConfiguration {

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory, ObjectMapper objectMapper){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setBeforeSendReplyPostProcessors(new TraceMessageSendPostProcessor());
        factory.setAfterReceivePostProcessors(new TraceMessageReceivePostProcessor());
        factory.setMessageConverter(new Jackson2JsonMessageConverter(objectMapper));
        return factory;
    }

    @Bean
    @ConditionalOnMissingBean
    public RabbitTransactionMessageSender rabbitTransactionMessageService(AmqpAdmin amqpAdmin,
                                                                          TransactionalMessageManagementService transactionalMessageManagementService,
                                                                          RabbitTemplate rabbitTemplate,
                                                                          MessageHelper messageHelper,
                                                                          RabbitMqExProperties rabbitMqExProperties) {
        RabbitTransactionMessageSender sender = new RabbitTransactionMessageSender(amqpAdmin, transactionalMessageManagementService, rabbitTemplate, messageHelper);

        rabbitMqExProperties
                .getProducer()
                .getDeclare()
                .forEach(o -> sender.declareExchange(o.getExchange(), o.getExchangeType()));

        rabbitMqExProperties
                .getConsumer()
                .getDeclare()
                .forEach(o -> declareQueue(amqpAdmin, o.getQueue(), o.getExchange(), o.getExchangeType(), o.getRoutingKey()));
        return sender;
    }

    @Bean
    public MetaInfoExtractor rabbitKeyExtractor() {
        return new RabbitDefaultMetaInfoExtractor();
    }

    private void declareQueue(AmqpAdmin amqpAdmin, String queueName, String exchangeName, String exchangeType, String routingKey) {
        Queue queue = new Queue(queueName);
        amqpAdmin.declareQueue(queue);
        Exchange exchange = new CustomExchange(exchangeName, exchangeType);
        amqpAdmin.declareExchange(exchange);
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
        amqpAdmin.declareBinding(binding);
    }

}
