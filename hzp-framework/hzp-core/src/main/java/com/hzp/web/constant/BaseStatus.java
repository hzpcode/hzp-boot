package com.hzp.web.constant;

import lombok.AllArgsConstructor;

import java.util.Objects;

/**
 * @author wangchun
 * @date 2019/12/12
 */
@AllArgsConstructor
public enum BaseStatus {

    ENABLE(1,"启用"),
    DISABLE(0,"禁用")

    ;

    private final int status;
    private final String desc;

    public int status(){
        return status;
    }

    public static int changeStatus(int status) {
        if(BaseStatus.DISABLE.status == status) {
            return BaseStatus.ENABLE.status;
        }
        return BaseStatus.DISABLE.status;
    }

    public static BaseStatus getByStatus(int status) {
        for (BaseStatus baseStatus : values()) {
            if(baseStatus.status == status) {
                return baseStatus;
            }

        }
        return null;
    }

    public static String getDescByStatus(int status) {
        BaseStatus userStatus = getByStatus(status);
        return Objects.isNull(userStatus) ? "" : userStatus.desc;
    }
}
