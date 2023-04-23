package com.hzp.mq.idempotent.handler;

import com.hzp.mq.base.prop.MessageQueueProperties;
import com.hzp.mq.idempotent.extractor.MetaInfoExtractor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

/**
 *  JDBC幂等处理器
 *
 *  @author Yu
 *  @date 2021/3/28 23:56
 */
public class MysqlJdbcIdempotentHandler extends AbstractIdempotentHandler {

    private static final String INSERT_TEMP = "INSERT IGNORE INTO %s ts_msg_consume_record(k) VALUES(?)";
    private static final String DELETE_TEMP = "DELETE FROM %s ts_msg_consume_record WHERE k=?";
    private static final String GARBAGE_TEMP = "DELETE FROM %s ts_msg_consume_record WHERE create_time < ?";

    private final JdbcTemplate jdbcTemplate;
    private final MessageQueueProperties messageQueueProperties;

    public MysqlJdbcIdempotentHandler(JdbcTemplate jdbcTemplate, List<MetaInfoExtractor> extractors, MessageQueueProperties messageQueueProperties) {
        super(extractors);
        this.jdbcTemplate = jdbcTemplate;
        this.messageQueueProperties = messageQueueProperties;
    }

    @Override
    protected boolean doIsProcessed(Object message) throws Exception {
        int update = jdbcTemplate.update(String.format(INSERT_TEMP, messageQueueProperties.getDefaultDataBase() + "."), extractMetaInfo(message).getIdempotentKey());
        return update != 1;
    }

    @Override
    protected void markFailed(Object message) {
        jdbcTemplate.update(String.format(DELETE_TEMP, messageQueueProperties.getDefaultDataBase() + "."), extractMetaInfo(message).getIdempotentKey());
    }

    @Override
    protected void markProcessed(Object message) {

    }

    @Override
    public void garbageCollect(LocalDateTime before) {
        this.jdbcTemplate.update(String.format(GARBAGE_TEMP, messageQueueProperties.getDefaultDataBase() + "."), before);
    }


}
