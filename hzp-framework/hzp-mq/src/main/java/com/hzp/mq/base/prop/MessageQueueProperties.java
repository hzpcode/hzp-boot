package com.hzp.mq.base.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 幂等配置类
 *
 * @author Yu
 * @date 2021/05/01 18:04
 **/

@Data
@ConfigurationProperties(prefix = "hzp.boot.mq")
public class MessageQueueProperties {

    /**
     * 系统主库(保证原子性)
     */
    private String defaultDataBase = "";

    private boolean enabled = true;

    private TxProp tx;
    private IdempotentProp idempotent;

    @Data
    public static class TxProp {

        /**
         * 使用spring schedule定时任务来重试和删除历史数据,内嵌redis分布式锁
         */
        private boolean enableSchedule = true;
        /**
         * 删除多少天前的成功发送数据
         */
        private Integer clearDaysAgo = 30;

    }

    @Data
    public static class IdempotentProp {

        /**
         * 使用@Scheduled定时任务
         */
        private boolean enableSchedule = true;

        /**
         * 清理前X天的消息
         */
        private Integer clearDaysAgo = 30;
    }

}
