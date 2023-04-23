package com.hzp.sys.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

/**
 * Created by XuJijun on 2017-10-13.
 */
public final class SDateUtils {
	public static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

	// patterns:
	public static final String FMT_ISO_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss";
	public static final String FMT_ISO_DATE_TIME_MINUTE = "yyyy-MM-dd'T'HH:mm";
	public static final String FMT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FMT_DATE_TIME_COMPACT = "yyyyMMddHHmmss";
	public static final String FMT_DATE_TIME_COMPACT_MILLISECOND = "yyyyMMddHHmmssSSS";
	public static final String FMT_DATE_TIME_MILLISECOND = "yyyy-MM-dd HH:mm:ss,SSS";
	public static final String FMT_DATE_STANDARD = "yyyy-MM-dd";
    public static final String FMT_DATE_COMPACT = "yyyyMMdd";
	public static final String FMT_YEAR_MONTH = "yyyy-MM";

	public static final String FMT_DATE_CHN = "yyyy年MM月dd日";
	public static final String FMT_HHmm = "HH:mm";

	public static final String FMT_DATE_FILE = "yyyy/MM/dd";


	/**
	 * Hours per day.
	 */
	public static final int HOURS_PER_DAY = 24;
	/**
	 * Minutes per hour.
	 */
	public static final int MINUTES_PER_HOUR = 60;
	/**
	 * Minutes per day.
	 */
	public static final int MINUTES_PER_DAY = MINUTES_PER_HOUR * HOURS_PER_DAY;
	/**
	 * Seconds per minute.
	 */
	public static final int SECONDS_PER_MINUTE = 60;
	/**
	 * Seconds per hour.
	 */
	public static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
	/**
	 * Seconds per day.
	 */
	public static final int SECONDS_PER_DAY = SECONDS_PER_HOUR * HOURS_PER_DAY;
	/**
	 * Milliseconds per day.
	 */
	public static final long MILLIS_PER_DAY = SECONDS_PER_DAY * 1000L;

	/**
	 * 获取系统当前时间字符串（由pattern指定格式）
	 * @param pattern 字符串格式，比如："yyyy-MM-dd HH:mm:ss"
	 * @return 由pattern指定格式的字符串
	 */
	public static String nowToStr(String pattern) {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 获取系统当前时间字符串（ISO格式）
	 * @return "2007-12-03T10:15:30"
	 */
	public static String nowToIsoStr() {
		return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}

    /**
     * 将long时间戳转换pattern格式的String
     * @param timestamp 时间戳（秒）
     * @param pattern 格式
     * @return 格式化后的时间
     */
    public static String timestampToString(long timestamp, String pattern) {
        if(timestamp == 0) {
            return "";
        }
        LocalDateTime dt = timestampToLocalDateTime(timestamp);
        return dt.format(DateTimeFormatter.ofPattern(pattern));
    }

	/**
	 * 把某个日期时间格式化为字符串
	 * @param dateTime 时间
	 * @param pattern 格式
	 */
	public static String localDateTimeToString(LocalDateTime dateTime, String pattern){
		return dateTime.format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 转换long类型的timestamp（秒）为LocalDateTime类型
	 */
	public static LocalDateTime timestampToLocalDateTime(long timestamp){
		return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), DEFAULT_ZONE_ID);
	}

	/**
	 * 转换long类型的timestamp为LocalDate类型
	 */
	public static LocalDate timestampToLocalDate(long timestamp){
		return timestampToLocalDateTime(timestamp).toLocalDate();
	}

	/**
	 * 根据timestamp返回int类型的日期
	 * @param timestamp 时间戳（秒）
	 * @return 比如：20170214
	 */
	public static int timestampToIntDate(long timestamp){
		LocalDate localDate = timestampToLocalDate(timestamp);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String formattedStringDate = localDate.format(formatter);
		return Integer.parseInt(formattedStringDate);
	}

	/**
	 * 返回两个时间戳的间隔天数
	 * @param timestamp1 小的时间戳
	 * @param timestamp2 大的时间戳
	 * @return 间隔天数
	 */
	public static long getDaysBetweenTwoTimestamps(long timestamp1, long timestamp2) {
		LocalDate date1 = timestampToLocalDate(timestamp1);
		LocalDate date2 = timestampToLocalDate(timestamp2);

		return date2.toEpochDay() - date1.toEpochDay();
	}

	/**
	 * 把字符串转换为LocalDate类型
	 * @param dateStr 格式必须为：“2016-05-31”
	 * @return LocalDate对象
	 */
	public static LocalDate dateStrToLocalDate(String dateStr){
		return LocalDate.parse(dateStr);
	}

	/**
	 * 将指定的日期字符串转换成LocalDateTime对象
	 * @param dateTimeStr 需要转换的日期/时间字符串，比如："2018/07/09 11:06"
	 * @param pattern 字符串格式，比如："yyyy/MM/dd HH:mm"
	 * @return LocalDateTime对象
	 */
	public static LocalDateTime dateTimeStrToLocalDate(String dateTimeStr, String pattern) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
		return LocalDateTime.parse(dateTimeStr, df);
	}

	/**
	 * 将当前时间转换成long时间戳
	 *
	 * @return long 时间戳，单位：秒
	 */
	public static long nowToTimestamp() {
		return Instant.now().getEpochSecond();
	}

	/**
	 * 将LocalDateTime转为timestamp
	 * @param time LocalDateTime
	 * @return long 时间戳，单位：秒
	 */
	public static long localDateTimeToTimestamp(LocalDateTime time){
		return time.atZone(DEFAULT_ZONE_ID).toEpochSecond();
	}

	/**
	 * 获取几天前00:00:00的时间戳
	 * @param d 距离现在d天前
	 * @return 时间戳，以秒为单位
	 */
	public static long getMinusDayTimestamp(int d) {
		LocalDate now = LocalDate.now();
		return now.minusDays(d).atStartOfDay().atZone(DEFAULT_ZONE_ID).toEpochSecond();
	}

	/**
	 * 获取距离现在m个月前00:00:00的时间戳（请使用midnightOfMinusMonthsToTimestamp）
	 * @param m 距离现在m个月前
	 * @return 时间戳，以秒为单位
	 */
/*	public static long getMinusMonthTimestamp(int m){
		LocalDate now = LocalDate.now();
		return now.minusMonths(m).atStartOfDay().atZone(DEFAULT_ZONE_ID).toEpochSecond();
	}*/

    /**
     * 将指定的日期字符串转换成timestamp，单位：秒
     * @param dateTimeStr 需要转换的日期字符串，比如："2018/07/09 11:06"
     * @param pattern 字符串格式，比如："yyyy/MM/dd HH:mm"
     * @return timestamp，单位：秒
     */
    public static long dateTimeStrToTimestamp(String dateTimeStr, String pattern) {
        LocalDateTime ldt = dateTimeStrToLocalDate(dateTimeStr, pattern);
        return ldt.atZone(DEFAULT_ZONE_ID).toEpochSecond();
    }

	/**
	 * 将指定的日期（格式为：yyyy-MM-dd）转换为当天凌晨00:00:00的时间戳
	 * @param dateStr 需要转换的日期，格式为：yyyy-MM-dd
	 * @return 时间戳，单位：秒
	 */
	public static long dateStrToStartOfDayTimestamp(String dateStr){
		return LocalDate.parse(dateStr).atStartOfDay().atZone(DEFAULT_ZONE_ID).toEpochSecond();
	}

	/**
	 * 将指定的日期（格式为：yyyy-MM-dd）转换为当天23:59:59.999999999的时间戳
	 * @param dateStr 需要转换的日期，格式为：yyyy-MM-dd
	 * @return 时间戳，单位：秒
	 */
	public static long dateStrToDayEndTimestamp(String dateStr){
		return LocalDate.parse(dateStr).atTime(LocalTime.MAX).atZone(DEFAULT_ZONE_ID).toEpochSecond();
	}

	/**
	 * 将date的凌晨0点转为timestamp
	 * @param date LocalDate
	 * @return 时间戳，单位：秒
	 */
	public static long startOfDayToTimestamp(LocalDate date){
		return date.atStartOfDay(DEFAULT_ZONE_ID).toEpochSecond();// .toInstant().toEpochMilli()/1000;
	}

	/**
	 * 将今天凌晨零点转换成long时间戳
	 * @return long 时间戳，单位：秒
	 */
	public static long startOfTodayToTimestamp() {
		return startOfDayToTimestamp(LocalDate.now());
	}

	/**
	 * 将date的最后一秒转为timestamp
	 * @param date LocalDate
	 * @return 时间戳，单位：秒
	 */
	public static long endOfDayToTimestamp(LocalDate date){
		return LocalDateTime.of(date, LocalTime.MAX).atZone(DEFAULT_ZONE_ID).toEpochSecond();
	}

	/**
	 * 获取若干天前00:00:00的时间戳
	 * @param days 距离现在d天前
	 * @return 时间戳，以秒为单位
	 */
	public static long daysBeforeToTimestamp(int days) {
		LocalDate now = LocalDate.now();
		return now.minusDays(days).atStartOfDay().atZone(DEFAULT_ZONE_ID).toEpochSecond();
	}

	/**
	 * 获取距离现在minutes分钟前的时间戳
	 */
	public static long minutesBeforeToTimestamp(int minutes){
		LocalDateTime theTime = LocalDateTime.now().minusMinutes(minutes);
		return theTime.atZone(DEFAULT_ZONE_ID).toEpochSecond();
	}

    /**
     * 获取当月第一天00:00:00的时间戳
     * @return 时间戳，以秒为单位
     */
    public static long midnightOfThisMonthFirstDayToTimestamp(){
        LocalDate now = LocalDate.now();
        return now.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay().atZone(DEFAULT_ZONE_ID).toEpochSecond();
    }

	/**
	 * 获取当前时间加month个月后的第dayOfMonth天00:00:00的时间戳
	 * @return 时间戳，以秒为单位
	 */
	public static long midnightOfPlusMonthAndDayOfMonthToTimestamp(int month, int dayOfMonth){
		LocalDate now = LocalDate.now();
		return now.plusMonths(month).withDayOfMonth(dayOfMonth).atStartOfDay().atZone(DEFAULT_ZONE_ID).toEpochSecond();
	}

	/**
	 * 获取距离现在m个月前00:00:00的时间戳
	 * @param months 距离现在m个月前
	 * @return 时间戳，以秒为单位
	 */
	public static long midnightOfMinusMonthsToTimestamp(int months){
		LocalDate now = LocalDate.now();
		return now.minusMonths(months).atStartOfDay().atZone(DEFAULT_ZONE_ID).toEpochSecond();
	}

//	/**
//	 * 获取距离现在days天前00:00:00的时间戳
//	 * @param days 距离现在days天前
//	 * @return 时间戳（秒）
//	 */
//	public static long midnightOfMinusDaysToTimestamp(int days){
//		LocalDate now = LocalDate.now();
//		return now.minusDays(days).atStartOfDay().atZone(DEFAULT_ZONE_ID).toEpochSecond();
//	}

    /**
     * 获取距离现在m个月前的月第一天00:00:00的时间戳
     * @param months 距离现在m个月前
     * @return 时间戳，以秒为单位
     */
    public static long midnightOfMinusMonthsFirstDayToTimestamp(int months){
        LocalDate now = LocalDate.now();
        return now.minusMonths(months).with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay().atZone(DEFAULT_ZONE_ID).toEpochSecond();
    }

	/**
	 * 获取某一天是星期几（中文）
	 * @param d 某一天
	 * @return “星期一”、“星期二”、etc.
	 */
	public static String getDayOfWeek(LocalDate d){
		DayOfWeek dayOfWeek = d.getDayOfWeek();
		return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.SIMPLIFIED_CHINESE);
	}

    /**
     * 秒数转换为中文
     * @param seconds 秒数
     * @return dd天hh小时mm分钟ss秒sss毫秒
     */
	public static String secondsToChnStr(long seconds){
	    return durationToChnStr(Duration.ofSeconds(seconds));
    }

	/**
	 * duration转换为中文
	 * @param duration 时长
	 * @return dd天hh小时mm分钟ss秒sss毫秒
	 */
	public static String durationToChnStr(Duration duration){
		StringBuilder sb = new StringBuilder();

		long secs = duration.getSeconds();
		long millis = duration.minusSeconds(secs).toMillis();

		int days = (int) secs / SECONDS_PER_DAY;
		int hours = (int) (secs % SECONDS_PER_DAY) / SECONDS_PER_HOUR;
		int minutes = (int) (secs % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE;
		int seconds = (int) secs % SECONDS_PER_MINUTE;

		if(days != 0){
			sb.append(days).append("天");
		}

		if(hours != 0){
			sb.append(hours).append("小时");
		}

		if(minutes != 0){
			sb.append(minutes).append("分钟");
		}

		if(seconds != 0){
			sb.append(seconds).append("秒");
		}

		if(millis != 0){
			sb.append(millis).append("毫秒");
		}

		return sb.toString();
	}

    /**
     * 毫秒数转换为中文的时长
     * @param milliseconds 毫秒
     * @return dd天hh小时mm分钟ss秒sss毫秒
     */
	public static String millisecondsToChnStr(long milliseconds){
		StringBuilder sb = new StringBuilder();
		long duration = milliseconds; //总毫秒数

        long milliSec = duration % 1000; //净毫秒数
        if(milliSec > 0){
            sb.insert(0, "毫秒").insert(0, milliSec);
        }

        if(duration > 1000) {
            duration = duration / 1000;
            long sec = duration % 60; //净秒数
            if (sec > 0) {
                sb.insert(0, "秒").insert(0, sec);
            }

            if(duration >= 60){ //大于一分钟
                duration = duration / 60; //总分钟数

                long min = duration % 60; //净分钟数
                if(min > 0){
                    sb.insert(0,"分钟").insert(0, min);
                }

                if(duration >= 60){ //大于一小时
                    duration = duration / 60; //总小时数

                    long hour = duration % 24; //净小时数
                    if(hour > 0){
                        sb.insert(0,"小时").insert(0, hour);
                    }

                    if(duration >= 24){ //大于一天
                        duration = duration / 24; //天数
                        sb.insert(0,"天").insert(0, duration);
                    }
                }
            }
        }

		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println("DEFAULT_ZONE_ID：" + DEFAULT_ZONE_ID);

		System.out.println(timestampToLocalDateTime(5614416000L));

		System.out.println(nowToStr(FMT_DATE_FILE));
		System.out.println(nowToStr(FMT_DATE_COMPACT));

		System.out.println("nowToIsoStr()：" + nowToIsoStr());
		System.out.println("nowToStr()：" + nowToStr("yyyy-MM-dd HH:mm:ss"));

		long nowTimestamp = nowToTimestamp();
		System.out.println("nowTimestamp: " + nowTimestamp);
		System.out.println("timestampToLocalDateTime(): " + timestampToLocalDateTime(nowTimestamp + 60));

		LocalDateTime nowLocalDateTime = LocalDateTime.now();
		System.out.println("localDateTimeToTimestamp(): " + localDateTimeToTimestamp(nowLocalDateTime));

		LocalDate nowLocalDate = LocalDate.now();
		System.out.println("startOfDayToTimestamp(): " + startOfDayToTimestamp(nowLocalDate));
		System.out.println("endOfDayToTimestamp(): " + endOfDayToTimestamp(nowLocalDate));

		System.out.println("daysBeforeToTimestamp(): " + daysBeforeToTimestamp(3));
		System.out.println("midnightOfMinusMonthsToTimestamp(): " + midnightOfMinusMonthsToTimestamp(1));

        System.out.println("getDaysBetweenTwoTimestamps(): " + getDaysBetweenTwoTimestamps(getMinusDayTimestamp(5), nowToTimestamp()));

        long t8 = midnightOfMinusMonthsFirstDayToTimestamp(8);
        System.out.println("midnightOfMinusMonthsFirstDayToTimestamp(): " + t8 + ", " + timestampToString(t8, FMT_DATE_TIME));

        long t9 = midnightOfThisMonthFirstDayToTimestamp();
        System.out.println("midnightOfThisMonthFirstDayToTimestamp(): " + t9 + ", " + timestampToString(t9, FMT_DATE_TIME));

        LocalDateTime ldt10 = dateTimeStrToLocalDate("2018/07/09 11:12", "yyyy/MM/dd HH:mm");
        System.out.println("dateTimeStrToLocalDate: " + ldt10.toString());

        System.out.println("millisecondsToChnStr: " + millisecondsToChnStr(12323656035L));

        System.out.println("startOfDay timestamp: " + dateStrToStartOfDayTimestamp("2018-12-19"));

		System.out.println(durationToChnStr(Duration.ofSeconds(60)));
		System.out.println(durationToChnStr(Duration.ofSeconds(61)));
		System.out.println(durationToChnStr(Duration.ofSeconds(60*60)));
		System.out.println(durationToChnStr(Duration.ofSeconds(60*60*10+2)));
		System.out.println(durationToChnStr(Duration.ofSeconds(60*60*24)));
		System.out.println(durationToChnStr(Duration.ofSeconds(60*60*24 + 60*60 + 60*3)));
		System.out.println(durationToChnStr(Duration.ofMillis((60*60*24 + 60*60 + 60*3)*1000 + 21)));

    }
}
