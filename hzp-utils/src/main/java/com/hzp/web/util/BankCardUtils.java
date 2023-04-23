package com.hzp.web.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 银行卡相关的工具类
 */
public class BankCardUtils {

    private BankCardUtils() { }

    /**
     * 校验字符串
     * <p>
     * 1. 从右边第1个数字（校验数字）开始偶数位乘以2；<br>
     * 2. 把在步骤1种获得的乘积的各位数字与原号码中未乘2的各位数字相加；<br>
     * 3. 如果在步骤2得到的总和模10为0，则校验通过。
     * </p>
     *
     * @param cardNo 含校验数字的字符串
     * @return true  校验通过<br>
     *         false 校验不通过
     */
    public static boolean isValidCardNo(String cardNo) {
        if (StringUtils.isBlank(cardNo)) {
            return false;
        }

        // 6位IIN+最多12位自定义数字+1位校验数字
        // 注意ISO/IEC 7812-1:2017中重新定义8位IIN+最多10位自定义数字+1位校验数字
        // 这里为了兼容2017之前的版本，使用8~19位数字校验
        if (!cardNo.matches("^\\d{8,19}$")) {
            return false;
        }
        return sum(cardNo) % 10 == 0;
    }


    /**
     * 根据Luhn算法计算字符串各位数字之和
     * <p>
     * 1. 从右边第1个数字（校验数字）开始偶数位乘以2；<br>
     * 2. 把在步骤1种获得的乘积的各位数字与原号码中未乘2的各位数字相加。<br>
     * </p>
     */
    private static int sum(String str) {
        char[] strArray = str.toCharArray();
        int n = strArray.length;
        int sum = 0;
        for (int i = n; i >= 1; i--) {
            int a = strArray[n - i] - '0';
            // 偶数位乘以2
            if (i % 2 == 0) {
                a *= 2;
            }
            // 十位数和个位数相加，如果不是偶数位，不乘以2，则十位数为0
            sum = sum + a / 10 + a % 10;
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(BankCardUtils.isValidCardNo("3568570114423308"));
    }
}
