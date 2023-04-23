package com.hzp.mq.tx.repository.mysql;


import com.hzp.jdbc.rs.ResultSetConverter;
import com.hzp.mq.base.prop.MessageQueueProperties;
import com.hzp.mq.tx.model.po.TransactionalMessageContent;
import com.hzp.mq.tx.repository.TransactionalMessageContentDao;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 *
 *  @author Yu
 *  @date 2021/3/23 0:08
 */
@RequiredArgsConstructor
public class MySqlTransactionalMessageContentDao implements TransactionalMessageContentDao {

    private static final String INSERT_TEMP = "INSERT INTO %s ts_transactional_message_content (message_id, content) VALUES (?,?)";
    private static final String QUERY_TEMP = "SELECT * FROM %s ts_transactional_message_content WHERE message_id IN (%s)";
    private static final String DELETE_TEMP = "DELETE FROM %s ts_transactional_message_content WHERE message_id IN (%s)";

    private final JdbcTemplate jdbcTemplate;
    private final MessageQueueProperties messageQueueProperties;

    private static final ResultSetConverter<TransactionalMessageContent> CONVERTER = r -> {
        TransactionalMessageContent content = new TransactionalMessageContent();
        content.setId(r.getLong("id"));
        content.setMessageId(r.getLong("message_id"));
        content.setContent(r.getString("content"));
        return content;
    };

    private static final ResultSetExtractor<List<TransactionalMessageContent>> RESULT_SET_EXTRACTOR = r -> {
        List<TransactionalMessageContent> list = new ArrayList<>();
        while (r.next()) {
            list.add(CONVERTER.convert(r));
        }
        return list;
    };

    @Override
    public void deleteByMsgId(Long msgId) {
        jdbcTemplate.update(String.format(DELETE_TEMP, messageQueueProperties.getDefaultDataBase() + ".", msgId));
    }

    @Override
    public void deleteByMsgIds(Collection<Long> msgIds) {
        if (CollectionUtils.isEmpty(msgIds)) {
            return;
        }

        String idsStr = msgIds.stream()
                .sorted()
                .map(Objects::toString)
                .collect(Collectors.joining(","));

        jdbcTemplate.update(String.format(DELETE_TEMP, messageQueueProperties.getDefaultDataBase() + ".", idsStr));
    }

    @Override
    public void insert(TransactionalMessageContent record) {
        jdbcTemplate.update(
                String.format(INSERT_TEMP, messageQueueProperties.getDefaultDataBase() + "."),
                p -> {
                    p.setLong(1, record.getMessageId());
                    p.setString(2, record.getContent());
                });
    }

    @Override
    public List<TransactionalMessageContent> queryByMessageIds(Collection<Long> messageIds) {

        return jdbcTemplate.query(
                String.format(
                        QUERY_TEMP,
                        messageQueueProperties.getDefaultDataBase() + ".",
                        messageIds.stream().map(Objects::toString).collect(Collectors.joining(","))),
                RESULT_SET_EXTRACTOR);
    }
}
