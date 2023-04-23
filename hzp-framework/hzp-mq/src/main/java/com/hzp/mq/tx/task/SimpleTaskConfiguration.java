package com.hzp.mq.tx.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 本地定时任务配置类
 * 如果使用分布式定时任务可以调用handler方法
 *
 * @author Yu
 * @date 2021/03/17 23:09
 **/

@Slf4j
@RequiredArgsConstructor
public class SimpleTaskConfiguration {

    private final TxMessageRetryHandler txMessageRetryHandler;
    private final ClearHisSuccessRecordHandler clearHisSuccessRecordHandler;

    @Scheduled(initialDelay = 2 * 1000, fixedDelay = 30 * 1000)
    public void txMessageRetrySend() {
        txMessageRetryHandler.execute();
    }

    @Scheduled(initialDelay = 2 * 1000, fixedDelay = 30 * 60 * 1000)
    public void clearHisSuccessRecord() {
        clearHisSuccessRecordHandler.execute();
    }

}
