package com.hzp.web.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 客户端类型
 *
 * @author Yu
 * @date 2020/03/12 00:17
 **/

@Getter
@AllArgsConstructor
public enum ClientType {

    PROVIDER(1, "供应商"),

    ;

    private final Integer val;
    private final String desc;


}
