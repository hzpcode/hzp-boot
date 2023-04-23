package com.hzp.mq.base.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Yu
 * @date 2021/03/17 22:23
 **/

@Getter
@RequiredArgsConstructor
public enum MqType {

    /**rabbitMq*/
    RABBIT(1),

    ;

    private final Integer type;

}
