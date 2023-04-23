package com.hzp.web.util;

import java.util.UUID;

/**
 * UUID工具类
 *
 * @author Yu
 * @date 2020/02/10 15:46
 **/

public class UUIDUtils {

    private UUIDUtils() {}

    /**
     * 生成UUID不带"-"
     *
     * @return UUID
     */
    public static String generate() {
        return UUID.randomUUID().toString().replaceAll("-" , "");
    }

    public static void main(String[] args) {
        System.out.println(generate() + ":" + generate());
    }

}
