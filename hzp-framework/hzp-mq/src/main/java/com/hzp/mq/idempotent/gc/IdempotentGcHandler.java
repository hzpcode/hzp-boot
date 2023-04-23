package com.hzp.mq.idempotent.gc;

import com.hzp.mq.base.constant.MessageConstant;
import com.hzp.mq.base.prop.MessageQueueProperties;
import com.hzp.mq.idempotent.handler.MysqlTransactionalJdbcIdempotentHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

/**
 * 清零历史消息处理器
 *
 *
 * @author Yu
 * @date 2021/03/17 22:38
 **/

@Slf4j
@RequiredArgsConstructor
public class IdempotentGcHandler {

    private final MysqlTransactionalJdbcIdempotentHandler idempotentHandler;
    private final MessageQueueProperties messageQueueProperties;
    private final RedissonClient redissonClient;

    @Value("${spring.application.name:unknown-svc}")
    private String applicationName;

    public void execute() {
        RLock lock = redissonClient.getLock(applicationName + MessageConstant.MESSAGE_GC_TASK_LOCK_KEY);
        boolean tryLock = lock.tryLock();
        if (tryLock) {
            try {
                long start = System.currentTimeMillis();
                log.info("开始执行清理历史过期消息消费记录定时任务...");
                idempotentHandler.garbageCollect(LocalDateTime.now().minusDays(messageQueueProperties.getIdempotent().getClearDaysAgo()));
                log.info("执行清理历史过期消息消费记录定时任务完毕,耗时:{} ms...", System.currentTimeMillis() - start);
            } finally {
                lock.unlock();
            }
        }
    }


}
