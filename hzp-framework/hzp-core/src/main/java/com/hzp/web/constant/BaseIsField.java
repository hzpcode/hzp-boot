package com.hzp.web.constant;

import lombok.AllArgsConstructor;

import java.util.Objects;

/**
 * 基础is字段
 *
 * @author yxy
 * @date 2019/12/24 11:16
 **/

@AllArgsConstructor
public enum BaseIsField {
    /**否*/
    NO(0, "否"),
    /**是*/
    YES(1, "是"),

    ;
    public final Integer val;
    public final String desc;

    public static BaseIsField getByVal(Integer val) {
        if(Objects.isNull(val)) {
            return null;
        }

        BaseIsField[] baseIsFields = values();
        for (BaseIsField baseIsField : baseIsFields) {
            if(baseIsField.val.equals(val)) {
                return baseIsField;
            }
        }
        return null;
    }

    public static String getDescByVal(Integer val) {
        BaseIsField baseIsField = getByVal(val);
        return Objects.isNull(baseIsField) ? "" : baseIsField.desc;
    }

    public static BaseIsField getByDesc(String desc) {
        if (Objects.isNull(desc)) {
            return null;
        }

        for (BaseIsField baseIsField : values()) {
            if (baseIsField.desc.equals(desc)) {
                return baseIsField;
            }
        }

        return null;
    }

    public static Integer getValByDesc(String desc) {
        BaseIsField baseIsField = getByDesc(desc);
        if (Objects.isNull(baseIsField)) {
            return null;
        }
        return baseIsField.val;
    }

}
