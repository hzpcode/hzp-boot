package com.hzp.mq.tx.task;

import com.hzp.mq.base.constant.MessageConstant;
import com.hzp.mq.base.sender.MessageQueueDispatcher;
import com.hzp.mq.tx.model.po.TransactionalMessage;
import com.hzp.mq.tx.service.TransactionalMessageContentService;
import com.hzp.mq.tx.service.TransactionalMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 事务消息重试处理器
 *
 *
 * @author Yu
 * @date 2021/03/17 22:38
 **/

@Slf4j
@RequiredArgsConstructor
public class TxMessageRetryHandler {

    private final TransactionalMessageService transactionalMessageService;
    private final TransactionalMessageContentService transactionalMessageContentService;
    private final MessageQueueDispatcher messageQueueDispatcher;
    private final RedissonClient redissonClient;

    @Value("${spring.application.name:unknown-svc}")
    private String applicationName;

    public void execute() {
        RLock lock = redissonClient.getLock(applicationName + MessageConstant.RETRY_TASK_LOCK_KEY);
        boolean tryLock = lock.tryLock();
        if (tryLock) {
            try {
                long start = System.currentTimeMillis();
                log.info("开始执行事务消息推送补偿定时任务...");
                processPendingCompensationRecords();
                log.info("执行事务消息推送补偿定时任务完毕,耗时:{} ms...", System.currentTimeMillis() - start);
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 推送补偿 - 里面的参数应该根据实际场景定制
     */
    private void processPendingCompensationRecords() {
        // 时间的右值为当前时间减去退避初始值，这里预防把刚保存的消息也推送了
        LocalDateTime max = LocalDateTime.now().plusSeconds(-MessageConstant.DEFAULT_INIT_BACKOFF);
        // 时间的左值为右值减去1小时
        LocalDateTime min = max.plusHours(-1);

        Map<Long, TransactionalMessage> messageByMsgId = transactionalMessageService.queryPendingSendRecords(min, max, MessageConstant.RETRY_LIMIT)
                .stream()
                .collect(Collectors.toMap(TransactionalMessage::getId, Function.identity()));

        log.info(">>>>>>>>>> 待补偿消息:{}条", messageByMsgId.size());

        if (!CollectionUtils.isEmpty(messageByMsgId)) {
            transactionalMessageContentService.findByMessageIds(messageByMsgId.keySet())
                    .forEach(item -> {
                        TransactionalMessage message = messageByMsgId.get(item.getMessageId());

                        messageQueueDispatcher.send(message, item.getContent());
                    });
        }
    }


}
