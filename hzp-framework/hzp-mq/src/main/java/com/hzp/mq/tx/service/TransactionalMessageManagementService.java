package com.hzp.mq.tx.service;

import com.google.common.collect.Lists;
import com.hzp.mq.base.constant.MessageConstant;
import com.hzp.mq.tx.constant.MessageStatus;
import com.hzp.mq.tx.model.po.TransactionalMessage;
import com.hzp.mq.tx.model.po.TransactionalMessageContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Yu
 * @date 2021/03/17 22:36
 **/

@Slf4j
@RequiredArgsConstructor
public class TransactionalMessageManagementService {

    private final TransactionalMessageService transactionalMessageService;
    private final TransactionalMessageContentService transactionalMessageContentService;

    public void saveTransactionalMessageRecord(TransactionalMessage record, String content) {

        record.setMessageStatus(MessageStatus.PENDING.getStatus())
                .setNextScheduleTime(calculateNextScheduleTime(LocalDateTime.now(), MessageConstant.DEFAULT_INIT_BACKOFF, MessageConstant.DEFAULT_BACKOFF_FACTOR, 0))
                .setCurrentRetryTimes(0)
                .setInitBackoff(MessageConstant.DEFAULT_INIT_BACKOFF)
                .setBackoffFactor(MessageConstant.DEFAULT_BACKOFF_FACTOR)
                .setMaxRetryTimes(MessageConstant.DEFAULT_MAX_RETRY_TIMES);

        transactionalMessageService.saveSelective(record);

        TransactionalMessageContent messageContent = new TransactionalMessageContent()
                .setContent(content)
                .setMessageId(record.getId());

        transactionalMessageContentService.save(messageContent);
    }

    public void markSuccess(TransactionalMessage record) {
        record.setMessageStatus(MessageStatus.SUCCESS.getStatus());

        TransactionalMessage update = new TransactionalMessage()
                .setId(record.getId())
                .setMessageStatus(MessageStatus.SUCCESS.getStatus());

        transactionalMessageService.updateStatusSelective(update);
    }

    public void clearSuccessfulMessageRecord(Integer days) {
        LocalDateTime expiredTime = LocalDateTime.now().minusDays(days);

        List<Long> ids = transactionalMessageService.queryIdsByStatusAndCreateTime(MessageStatus.SUCCESS.getStatus(), expiredTime);
        if (CollectionUtils.isEmpty(ids)) {
            log.info(">>>>>>>>>>> 无待删除的发送成功历史消息...");
            return;
        }

        Lists.partition(ids, 2000)
                .parallelStream()
                .sorted()
                .peek(transactionalMessageService::deleteByIds)
                .forEach(transactionalMessageContentService::deleteByMsgIds);

    }

    public void markFail(TransactionalMessage record, Exception e) {
        log.error("发送消息失败,目标路由:{}, 路由键:{}", record.getTopic(), record.getRoutingKey());
        log.error("发送消息失败,异常：" + e.getMessage(), e);

        boolean isOutOfMaxRetryTimes = record.getCurrentRetryTimes().compareTo(record.getMaxRetryTimes()) >= 0;

        record.setCurrentRetryTimes(isOutOfMaxRetryTimes ? record.getMaxRetryTimes() : record.getCurrentRetryTimes() + 1);

        LocalDateTime nextScheduleTime = calculateNextScheduleTime(
                record.getNextScheduleTime(),
                record.getInitBackoff(),
                record.getBackoffFactor(),
                record.getCurrentRetryTimes()
        );

        record.setNextScheduleTime(nextScheduleTime);
        record.setMessageStatus(isOutOfMaxRetryTimes ? MessageStatus.FAIL.getStatus() : MessageStatus.PENDING.getStatus());
        record.setEditTime(LocalDateTime.now());

        transactionalMessageService.updateStatusSelective(record);
    }

    /**
     * 计算下一次执行时间
     *
     * @param base          基础时间
     * @param initBackoff   退避基准值
     * @param backoffFactor 退避指数
     * @param round         轮数
     * @return LocalDateTime
     */
    private LocalDateTime calculateNextScheduleTime(LocalDateTime base,
                                                    long initBackoff,
                                                    long backoffFactor,
                                                    long round) {

        double delta = initBackoff * Math.pow(backoffFactor, round);
        return base.plusSeconds((long) delta);
    }

}
