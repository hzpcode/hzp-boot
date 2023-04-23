package com.hzp.rabbit.tx.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Yu
 * @date 2021/03/17 22:23
 **/

@Getter
@RequiredArgsConstructor
public enum ExchangeType {

    FANOUT("fanout"),

    DIRECT("direct"),

    TOPIC("topic"),

    DEFAULT(""),

    ;

    private final String type;

}
