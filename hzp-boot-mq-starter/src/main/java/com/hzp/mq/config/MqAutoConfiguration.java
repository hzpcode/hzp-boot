package com.hzp.mq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzp.mq.base.prop.MessageQueueProperties;
import com.hzp.mq.base.sender.MessageQueueDispatcher;
import com.hzp.mq.base.sender.MessageQueueSender;
import com.hzp.mq.base.util.MessageHelper;
import com.hzp.mq.idempotent.extractor.MetaInfoExtractor;
import com.hzp.mq.idempotent.gc.IdempotentGcHandler;
import com.hzp.mq.idempotent.gc.SimpleGcTaskConfiguration;
import com.hzp.mq.idempotent.handler.MysqlJdbcIdempotentHandler;
import com.hzp.mq.idempotent.handler.MysqlTransactionalJdbcIdempotentHandler;
import com.hzp.mq.idempotent.intercepter.IdempotentInterceptor;
import com.hzp.mq.tx.repository.TransactionalMessageContentDao;
import com.hzp.mq.tx.repository.TransactionalMessageDao;
import com.hzp.mq.tx.repository.mysql.MySqlTransactionalMessageContentDao;
import com.hzp.mq.tx.repository.mysql.MySqlTransactionalMessageDao;
import com.hzp.mq.tx.service.TransactionalMessageContentService;
import com.hzp.mq.tx.service.TransactionalMessageManagementService;
import com.hzp.mq.tx.service.TransactionalMessageService;
import com.hzp.mq.tx.task.ClearHisSuccessRecordHandler;
import com.hzp.mq.tx.task.SimpleTaskConfiguration;
import com.hzp.mq.tx.task.TxMessageRetryHandler;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.List;

/**
 * rabbitmq自动配置类
 *
 * @author Yu
 * @date 2021/03/29 00:36
 **/
@Configuration
@ConditionalOnProperty(prefix = "hzp.boot.mq", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({MessageQueueProperties.class})
public class MqAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public IdempotentInterceptor idempotentInterceptor(List<MetaInfoExtractor> metaInfoExtractors) {
        return new IdempotentInterceptor(metaInfoExtractors);
    }

    @Bean
    @ConditionalOnMissingBean
    public MysqlJdbcIdempotentHandler jdbcIdempotentHandler(JdbcTemplate jdbcTemplate, List<MetaInfoExtractor> metaInfoExtractors,
                                                            MessageQueueProperties messageQueueProperties) {
        return new MysqlJdbcIdempotentHandler(jdbcTemplate, metaInfoExtractors, messageQueueProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public MysqlTransactionalJdbcIdempotentHandler transactionalJdbcIdempotentHandler(DataSourceTransactionManager dataSourceTransactionManager,
                                                                                      List<MetaInfoExtractor> extractors,
                                                                                      MysqlJdbcIdempotentHandler mysqlJdbcIdempotentHandler) {
        return new MysqlTransactionalJdbcIdempotentHandler(dataSourceTransactionManager, extractors, mysqlJdbcIdempotentHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public IdempotentGcHandler idempotentGcHandler(MysqlTransactionalJdbcIdempotentHandler transactionalMessageManagementService, RedissonClient redissonClient, MessageQueueProperties messageQueueProperties) {
        return new IdempotentGcHandler(transactionalMessageManagementService, messageQueueProperties, redissonClient);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "hzp.mq.idempotent", name = "enableSchedule", havingValue = "true", matchIfMissing = true)
    public SimpleGcTaskConfiguration simpleGcTaskConfiguration(IdempotentGcHandler idempotentGcHandler) {
        return new SimpleGcTaskConfiguration(idempotentGcHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public TransactionalMessageContentService transactionalMessageContentService(TransactionalMessageContentDao transactionalMessageContentDao) {
        return new TransactionalMessageContentService(transactionalMessageContentDao);
    }

    @Bean
    public TransactionalMessageService transactionalMessageService(TransactionalMessageDao transactionalMessageDao) {
        return new TransactionalMessageService(transactionalMessageDao);
    }

    @Bean
    public MessageHelper messageHelper(ObjectMapper objectMapper) {
        return new MessageHelper(objectMapper);
    }

    @Bean
    public TransactionalMessageManagementService transactionalMessageManagementService(TransactionalMessageService transactionalMessageService,
                                                                                       TransactionalMessageContentService transactionalMessageContentService) {

        return new TransactionalMessageManagementService(transactionalMessageService, transactionalMessageContentService);
    }

    @Bean
    public MessageQueueDispatcher messageQueueDispatcher(List<MessageQueueSender> messageQueueSenderList) {
        return new MessageQueueDispatcher(messageQueueSenderList);
    }

    @Bean
    @ConditionalOnMissingBean
    public TxMessageRetryHandler txMessageRetryHandler(TransactionalMessageService transactionalMessageService,
                                                       TransactionalMessageContentService transactionalMessageContentService,
                                                       RedissonClient redissonClient,
                                                       MessageQueueDispatcher dispatcher) {
        return new TxMessageRetryHandler(transactionalMessageService, transactionalMessageContentService, dispatcher, redissonClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public ClearHisSuccessRecordHandler clearHisSuccessRecordHandler(TransactionalMessageManagementService transactionalMessageManagementService,
                                                                     MessageQueueProperties messageQueueProperties,
                                                                     RedissonClient redissonClient) {
        return new ClearHisSuccessRecordHandler(transactionalMessageManagementService, messageQueueProperties, redissonClient);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "hzp.boot.mq.tx", name = "enableSchedule", havingValue = "true", matchIfMissing = true)
    public SimpleTaskConfiguration simpleTaskConfiguration(TxMessageRetryHandler txMessageRetryHandler,
                                                           ClearHisSuccessRecordHandler clearHisSuccessRecordHandler) {
        return new SimpleTaskConfiguration(txMessageRetryHandler, clearHisSuccessRecordHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public MySqlTransactionalMessageContentDao mysqlTransactionalMessageContentDao(JdbcTemplate jdbcTemplate,
                                                                                   MessageQueueProperties messageQueueProperties) {
        return new MySqlTransactionalMessageContentDao(jdbcTemplate, messageQueueProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public MySqlTransactionalMessageDao mysqlTransactionalMessageDao(JdbcTemplate jdbcTemplate,
                                                                     MessageQueueProperties messageQueueProperties) {
        return new MySqlTransactionalMessageDao(jdbcTemplate, messageQueueProperties);
    }


}
