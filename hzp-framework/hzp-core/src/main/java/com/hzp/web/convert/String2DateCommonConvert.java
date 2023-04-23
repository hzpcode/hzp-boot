package com.hzp.web.convert;

import com.hzp.web.exception.BizException;
import com.hzp.web.exception.ParameterException;
import com.hzp.web.exception.constant.ReturnCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * 通用String转Date
 * <p>TimeStamp只支持13位数字</p>
 *
 * @author yxy
 * @date 2019/10/23 12:44
 **/

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class String2DateCommonConvert {

    private final static String[] PATTERN =
            new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.S",
                    "yyyy.MM.dd", "yyyy.MM.dd HH:mm", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm:ss.S",
                    "yyyy/MM/dd", "yyyy/MM/dd HH:mm", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss.S",
                    "yyyyMMdd"};

    private static final String TIMESTAMP_REGEX = "^\\d{13}$";
    private static final String SECOND_REGEX = "^\\d{10}$";

    public static Date parse(String originDate) {
        if (StringUtils.isBlank(originDate)) {
            return null;
        }
        try {
            if (originDate.matches(TIMESTAMP_REGEX)) {
                return new Date(Long.parseLong(originDate));
            }

            if (originDate.matches(SECOND_REGEX)) {
                return new Date(Long.parseLong(originDate) * 1000L);
            }

            return DateUtils.parseDate(originDate, PATTERN);
        } catch (NumberFormatException e) {
            log.error("Timestamp转日期失败:" + e.getMessage(), e);
            throw new ParameterException(ReturnCode.PARAM_ERROR.getCode(), "Timestamp转日期失败，仅支持13位数字");
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            throw new BizException(ReturnCode.PARAM_ERROR.getCode(),
                    String.format(
                            "'%s' can not convert to type 'java.util.Date',just support timestamp(type of long) and following date format(%s)",
                            originDate,
                            StringUtils.join(PATTERN, ",")
                    ));
        }
    }

}
