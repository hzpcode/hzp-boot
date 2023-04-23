package com.hzp.web.util;

import com.hzp.web.exception.ParameterException;
import com.hzp.web.exception.constant.ReturnCode;
import org.apache.commons.lang3.StringUtils;

/**
 *  SQL过滤
 *
 *  @author yxy
 *  @date 2019/12/21 12:47
 */
public class SQLFilter {

    private SQLFilter() { }

    public static String[] illegalKeywords = {"'", "\"", ";", "\\", "master", "truncate", "insert", "select", "delete", "update", "declare", "alert", "drop"};
    /**
     * SQL注入校验
     * <p>
     * 例如： 排序字段，防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
     *
     * @param param  待验证的字符串
     */
    public static String sqlInject(String param){
        if(StringUtils.isBlank(param)){
            return param;
        }

        String lowerParam = param.toLowerCase();

        for(String illegalKeyword : illegalKeywords){
            if(lowerParam.contains(illegalKeyword)){
                throw new ParameterException(ReturnCode.PARAM_ERROR,
                        "参数:[" + param + "]，含非法关键词[', \", ;, \\, master, truncate, insert, select, delete, update, declare, alert, drop]");
            }
        }
        return param;
    }

    /**
     * SQL注入校验
     * <p>
     * 例如： 排序字段，防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
     *
     * @param param  待验证的字符串
     */
    public static boolean checkSqlInject(String param){
        if(StringUtils.isBlank(param)){
            return true;
        }

        String lowerParam = param.toLowerCase();

        for(String illegalKeyword : illegalKeywords){
            if(lowerParam.contains(illegalKeyword)){
                return false;
            }
        }
        return true;
    }
}
