package com.hzp.mq.tx.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Yu
 * @date 2021/03/17 22:30
 **/

@Getter
@AllArgsConstructor
public enum MessageStatus {


    /**
     * 成功
     */
    SUCCESS(1),

    /**
     * 待处理
     */
    PENDING(0),

    /**
     * 处理失败
     */
    FAIL(-1),

    ;

    private final Integer status;

}
