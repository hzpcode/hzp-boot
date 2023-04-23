package com.hzp.mq.tx.task;

import com.hzp.mq.base.constant.MessageConstant;
import com.hzp.mq.base.prop.MessageQueueProperties;
import com.hzp.mq.tx.service.TransactionalMessageManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;

/**
 * 清理历史成功数据处理器
 *
 * @author yxy
 * @date 2021/07/12 17:24
 **/

@Slf4j
@RequiredArgsConstructor
public class ClearHisSuccessRecordHandler {

    private final TransactionalMessageManagementService managementService;
    private final MessageQueueProperties messageQueueProperties;
    private final RedissonClient redissonClient;

    @Value("${spring.application.name:unknown-svc}")
    private String applicationName;

    public void execute() {
        RLock lock = redissonClient.getLock(applicationName + MessageConstant.CLEAR_SEND_RECORD_TASK_LOCK_KEY);
        boolean tryLock = lock.tryLock();
        if (tryLock) {
            try {
                long start = System.currentTimeMillis();
                log.info("开始删除历史成功发送消息定时任务...");
                managementService.clearSuccessfulMessageRecord(messageQueueProperties.getTx().getClearDaysAgo());
                log.info("执行删除历史成功发送消息定时任务完毕,耗时:{} ms...", System.currentTimeMillis() - start);
            } finally {
                lock.unlock();
            }
        }

    }

}
