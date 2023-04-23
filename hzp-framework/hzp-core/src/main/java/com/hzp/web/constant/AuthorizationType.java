package com.hzp.web.constant;

import lombok.AllArgsConstructor;

/**
 *  鉴权类型
 *
 *  @author Yu
 *  @date 2020/4/25 10:36
 */
@AllArgsConstructor
public enum AuthorizationType {
    Basic,
    Bearer,
    Digest,
    Mutual,
    // 未匹配的类型
    NA;

    public static AuthorizationType getAuthorizationType(String type) {
        for (AuthorizationType authorizationType : AuthorizationType.values()) {
            if (authorizationType.name().equals(type)) {
                return authorizationType;
            }
        }
        return NA;
    }
}
