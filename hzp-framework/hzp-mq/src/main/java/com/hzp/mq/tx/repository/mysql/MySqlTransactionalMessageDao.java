package com.hzp.mq.tx.repository.mysql;


import com.hzp.jdbc.rs.PreparedStatementProcessor;
import com.hzp.jdbc.rs.ResultSetConverter;
import com.hzp.mq.base.prop.MessageQueueProperties;
import com.hzp.mq.tx.constant.MessageStatus;
import com.hzp.mq.tx.model.po.TransactionalMessage;
import com.hzp.mq.tx.repository.TransactionalMessageDao;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *  事务消息DAO
 *
 *  @author Yu
 *  @date 2021/3/23 0:08
 */

@RequiredArgsConstructor
public class MySqlTransactionalMessageDao implements TransactionalMessageDao {

    private static final String DELETE_TEMP = "DELETE FROM %s ts_transactional_message WHERE id IN (%s)";
    private static final String QUERY_TEMP = "SELECT * FROM %s ts_transactional_message WHERE next_schedule_time >= ? AND next_schedule_time <= ? AND message_status <> ? AND current_retry_times < max_retry_times LIMIT ?";
    private static final String QUERY_HIS_RECORD_TEMP = "SELECT id FROM %s ts_transactional_message WHERE message_status = ? AND create_time < ?";

    private final JdbcTemplate jdbcTemplate;
    private final MessageQueueProperties messageQueueProperties;

    private static final ResultSetConverter<TransactionalMessage> CONVERTER = r -> {
        TransactionalMessage message = new TransactionalMessage();
        message.setId(r.getLong("id"));
        message.setCreateTime(r.getTimestamp("create_time").toLocalDateTime());
        message.setEditTime(r.getTimestamp("edit_time").toLocalDateTime());
        message.setCreator(r.getString("creator"));
        message.setEditor(r.getString("editor"));
        message.setDeleted(r.getInt("deleted"));
        message.setCurrentRetryTimes(r.getInt("current_retry_times"));
        message.setTopic(r.getString("topic"));
        message.setRoutingKey(r.getString("routing_key"));
        message.setMqType(r.getInt("mq_type"));
        message.setMaxRetryTimes(r.getInt("max_retry_times"));
        message.setBusinessModule(r.getString("business_module"));
        message.setBusinessKey(r.getString("business_key"));
        message.setTraceId(r.getString("trace_id"));
        message.setNextScheduleTime(r.getTimestamp("next_schedule_time").toLocalDateTime());
        message.setMessageStatus(r.getInt("message_status"));
        message.setInitBackoff(r.getLong("init_backoff"));
        message.setBackoffFactor(r.getInt("backoff_factor"));
        return message;
    };

    private static final ResultSetConverter<Long> ID_CONVERTER = r -> r.getLong("id");

    private static final ResultSetExtractor<List<Long>> ID_MULTI = r -> {
        List<Long> list = new ArrayList<>();
        while (r.next()) {
            list.add(ID_CONVERTER.convert(r));
        }
        return list;
    };

    private static final ResultSetExtractor<List<TransactionalMessage>> MULTI = r -> {
        List<TransactionalMessage> list = new ArrayList<>();
        while (r.next()) {
            list.add(CONVERTER.convert(r));
        }
        return list;
    };

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(String.format(DELETE_TEMP, messageQueueProperties.getDefaultDataBase() + ".", id));
    }

    @Override
    public void deleteByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }

        String idsStr = ids.stream()
                .sorted()
                .map(Objects::toString)
                .collect(Collectors.joining(","));

        jdbcTemplate.update(String.format(DELETE_TEMP, messageQueueProperties.getDefaultDataBase() + ".", idsStr));
    }

    @Override
    public void insertSelective(TransactionalMessage record) {

        List<PreparedStatementProcessor> processors = new ArrayList<>();
        IndexHolder holder = new IndexHolder();

        StringBuilder sql = new StringBuilder(String.format("INSERT INTO %s ts_transactional_message(", messageQueueProperties.getDefaultDataBase() + "."));

        if (null != record.getCurrentRetryTimes()) {
            holder.incr();
            sql.append("current_retry_times,");
            int idx = holder.index;
            processors.add(p -> p.setInt(idx, record.getCurrentRetryTimes()));
        }

        if (null != record.getMaxRetryTimes()) {
            holder.incr();
            sql.append("max_retry_times,");
            int idx = holder.index;
            processors.add(p -> p.setInt(idx, record.getMaxRetryTimes()));
        }

        if (null != record.getTopic()) {
            holder.incr();
            sql.append("topic,");
            int idx = holder.index;
            processors.add(p -> p.setString(idx, record.getTopic()));
        }

        if (null != record.getRoutingKey()) {
            holder.incr();
            sql.append("routing_key,");
            int idx = holder.index;
            processors.add(p -> p.setString(idx, record.getRoutingKey()));
        }

        if (null != record.getMqType()) {
            holder.incr();
            sql.append("mq_type,");
            int idx = holder.index;
            processors.add(p -> p.setInt(idx, record.getMqType()));
        }

        if (null != record.getBusinessModule()) {
            holder.incr();
            sql.append("business_module,");
            int idx = holder.index;
            processors.add(p -> p.setString(idx, record.getBusinessModule()));
        }

        if (null != record.getBusinessKey()) {
            holder.incr();
            sql.append("business_key,");
            int idx = holder.index;
            processors.add(p -> p.setString(idx, record.getBusinessKey()));
        }

        if (null != record.getBusinessKey()) {
            holder.incr();
            sql.append("trace_id,");
            int idx = holder.index;
            processors.add(p -> p.setString(idx, record.getTraceId()));
        }

        if (null != record.getNextScheduleTime()) {
            holder.incr();
            sql.append("next_schedule_time,");
            int idx = holder.index;
            processors.add(p -> p.setTimestamp(idx, Timestamp.valueOf(record.getNextScheduleTime())));
        }

        if (null != record.getMessageStatus()) {
            holder.incr();
            sql.append("message_status,");
            int idx = holder.index;
            processors.add(p -> p.setInt(idx, record.getMessageStatus()));
        }

        if (null != record.getInitBackoff()) {
            holder.incr();
            sql.append("init_backoff,");
            int idx = holder.index;
            processors.add(p -> p.setLong(idx, record.getInitBackoff()));
        }

        if (null != record.getBackoffFactor()) {
            holder.incr();
            sql.append("backoff_factor,");
            int idx = holder.index;
            processors.add(p -> p.setInt(idx, record.getBackoffFactor()));
        }

        StringBuilder realSql = new StringBuilder(sql.substring(0, sql.lastIndexOf(",")));

        realSql.append(") VALUES (");
        realSql.append(Stream.generate(() -> "?").limit(holder.index).collect(Collectors.joining(",")));
        realSql.append(")");

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(p -> {
            PreparedStatement ps = p.prepareStatement(realSql.toString(), Statement.RETURN_GENERATED_KEYS);
            for (PreparedStatementProcessor processor : processors) {
                processor.process(ps);
            }
            return ps;
        }, keyHolder);

        record.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    @Override
    public void updateStatusSelective(TransactionalMessage record) {
        List<PreparedStatementProcessor> processors = new ArrayList<>();
        IndexHolder holder = new IndexHolder();
        StringBuilder sql = new StringBuilder(String.format("UPDATE %s ts_transactional_message SET ", messageQueueProperties.getDefaultDataBase() + "."));
        if (null != record.getCurrentRetryTimes()) {
            holder.incr();
            sql.append("current_retry_times = ?,");
            int idx = holder.index;
            processors.add(p -> p.setInt(idx, record.getCurrentRetryTimes()));
        }
        if (null != record.getNextScheduleTime()) {
            holder.incr();
            sql.append("next_schedule_time = ?,");
            int idx = holder.index;
            processors.add(p -> p.setTimestamp(idx, Timestamp.valueOf(record.getNextScheduleTime())));
        }
        if (null != record.getEditTime()) {
            holder.incr();
            sql.append("edit_time = ?,");
            int idx = holder.index;
            processors.add(p -> p.setTimestamp(idx, Timestamp.valueOf(record.getEditTime())));
        }
        if (null != record.getMessageStatus()) {
            holder.incr();
            sql.append("message_status = ?,");
            int idx = holder.index;
            processors.add(p -> p.setInt(idx, record.getMessageStatus()));
        }
        StringBuilder realSql = new StringBuilder(sql.substring(0, sql.lastIndexOf(",")));
        holder.incr();
        int idx = holder.index;
        processors.add(p -> p.setLong(idx, record.getId()));
        realSql.append(" WHERE id = ?");
        jdbcTemplate.update(realSql.toString(), p -> {
            for (PreparedStatementProcessor processor : processors) {
                processor.process(p);
            }
        });
    }

    @Override
    public List<Long> queryIdsByStatusAndCreateTime(Integer messageStatus, LocalDateTime time) {
        return jdbcTemplate.query(
                String.format(QUERY_HIS_RECORD_TEMP, messageQueueProperties.getDefaultDataBase() + "."),
                p -> {
                    p.setInt(1, MessageStatus.SUCCESS.getStatus());
                    p.setTimestamp(2, Timestamp.valueOf(time));
                },
                ID_MULTI);
    }

    @Override
    public List<TransactionalMessage> queryPendingCompensationRecords(LocalDateTime minScheduleTime,
                                                                      LocalDateTime maxScheduleTime,
                                                                      int limit) {
        return jdbcTemplate.query(
                String.format(QUERY_TEMP, messageQueueProperties.getDefaultDataBase() + "."),
                p -> {
                    p.setTimestamp(1, Timestamp.valueOf(minScheduleTime));
                    p.setTimestamp(2, Timestamp.valueOf(maxScheduleTime));
                    p.setInt(3, MessageStatus.SUCCESS.getStatus());
                    p.setInt(4, limit);
                },
                MULTI);
    }

    private static class IndexHolder {

        private int index;

        public void incr() {
            index++;
        }
    }
}
