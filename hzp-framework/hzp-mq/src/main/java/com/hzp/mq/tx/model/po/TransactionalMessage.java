package com.hzp.mq.tx.model.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 事务消息
 *
 * @author Yu
 * @date 2021/03/17 22:19
 **/

@Data
@Accessors(chain = true)
public class TransactionalMessage {

    private Long id;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 最后修改时间
     */
    private LocalDateTime editTime;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 最后修改人
     */
    private String editor;
    /**
     * 是否删除
     */
    private Integer deleted;
    /**
     * 当前重试次数
     */
    private Integer currentRetryTimes;

    /**
     * 主题
     */
    private String topic;

    /**
     * 路由键
     */
    private String routingKey;

    /**
     * MQ类型
     */
    private Integer mqType;

    /**
     * 最大重试次数
     */
    private Integer maxRetryTimes;

    /**
     * 业务模块
     */
    private String businessModule;
    /**
     * 业务键
     */
    private String businessKey;

    /**
     * 链路id
     */
    private String traceId;
    /**
     * 下一次调度时间
     */
    private LocalDateTime nextScheduleTime;
    /**
     * 消息状态
     */
    private Integer messageStatus;
    /**
     * 退避初始化值,单位为秒
     */
    private Long initBackoff;
    /**
     * 退避因子(也就是指数)
     */
    private Integer backoffFactor;


}
