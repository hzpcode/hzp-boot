package com.hzp.web.holder;

import com.hzp.web.component.ThreadLocalHelper;
import lombok.experimental.UtilityClass;

import java.util.Objects;


/**
 * 脱敏设置器
 *
 * @author yxy
 * @date 2020/08/24 10:46
 **/
@UtilityClass
public class MaskSetting {

    private static final String KEY = "MASK_IS_ENABLED";

    public static void disable() {
        ThreadLocalHelper.put(KEY, Boolean.FALSE);
    }

    public static Boolean isEnabled() {
        Boolean isEnabled = ThreadLocalHelper.get(KEY);
        return Objects.isNull(isEnabled) ? Boolean.TRUE : isEnabled;
    }

    public static void remove() {
        ThreadLocalHelper.remove(KEY);
    }

}
