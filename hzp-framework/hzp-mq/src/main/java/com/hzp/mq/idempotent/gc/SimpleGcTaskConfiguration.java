package com.hzp.mq.idempotent.gc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 本地垃圾消息回收定时任务配置类
 * 如果使用分布式定时任务可以调用handler方法
 *
 * @author Yu
 * @date 2021/03/17 23:09
 **/

@Slf4j
@RequiredArgsConstructor
public class SimpleGcTaskConfiguration {

    private final IdempotentGcHandler idempotentGCHandler;

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void consumeRecordGarbageCollect() {
        idempotentGCHandler.execute();
    }

}
