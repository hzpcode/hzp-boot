package com.hzp.web.util;

/**
 * @author han
 * 2019-11-20
 */
public class NumberUtils {

    /**
     * 字符串是否正确的数字描述表达（小数或整数）
     * @param str
     * @return
     */
    public static boolean isRightNumber(String str) {
        String reg = "^(-)?[0-9]+(.[0-9]+)?$";
        return str.matches(reg);
    }

    /**
     * 字符串是否整型数字，例123，123.000
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        if (isRightNumber(str)) {
            if (str.contains(".")) {
                str = str.substring(str.indexOf(".") + 1).replaceAll("0", "");
                return str.length() == 0;
            }
            return true;
        }
        return false;
    }
}
