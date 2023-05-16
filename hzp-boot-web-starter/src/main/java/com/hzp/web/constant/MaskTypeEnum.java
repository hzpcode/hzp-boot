package com.hzp.web.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 脱敏类型
 *
 * @author yxy
 * @date 2020/08/24 14:17
 **/

@Getter
@AllArgsConstructor
public enum MaskTypeEnum {

    PHONE(3, 4),
    ID_CARD(8, 4),
    NULL(0, 0),

    ;

    public final int prefixLen;

    public final int suffixLen;

}
