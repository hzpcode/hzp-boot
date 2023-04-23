package com.hzp.web.util;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;

/**
 * 对象工具类
 *
 * @author yxy
 * @date 2019/10/31 14:20
 **/

public class ModelUtils {

    private ModelUtils() { }

    /**
     * List转List
     *
     * @param source    源对象集合
     * @param clz       目标对象class
     * @param <T>       目标对象class
     * @return          目标对象集合
     */
    public static <T> List<T> toBeanList(List<?> source, Class<T> clz) {

        List<T> result = Lists.newArrayListWithCapacity(Objects.isNull(source) ? 0 : source.size());
        if (CollectionUtils.isEmpty(source)) {
            return result;
        }

        for (Object o : source) {
            T t = BeanUtil.toBean(o, clz);
            result.add(t);
        }
        return result;
    }

}
