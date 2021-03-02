package com.cn.image.common;

import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * @Desc 
 * @author Z
 * @date 2020年9月14日 下午3:55:23
 */
public class DateUtil {

    public final static String MMDDHHMMSS = "MMddhhmmss";

    public final static String YYYYMMDDHH = "yyyyMMddHH";

    public final static String YYYYMMDDHHMM = "yyyyMMddHHmm";

    public final static String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";

    public final static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public final static String YYYYMMDDHHMMSSSSSWITHSYMBOL = "yyyy-MM-dd HH:mm:ss";

    public final static String YYYYMMDD = "yyyy-MM-dd";

    public final static String YYYYMMDDHHMMWITHSYMBOL = "yyyy-MM-dd HH:mm";

    public final static String MMDDHHMMSSWITHSYMBOL = "MM-dd HH:mm:ss";

    public final static String YYYYMMDDWITHOUTMINUS = "yyyyMMdd";

    public final static String MMDDWITHDOT = "MM.dd";

    public final static String HHMMWITHCOLON = "HH:mm";

    /**
     * <pre>
     * 指定Pattern 格式化时间
     * 字符串转date类型
     * date = parseToDate("2000-01-01",FORMAT_DATE_TIME)
     * </pre>
     * 
     * @param strTime
     *            pattern对应的时间
     * @param pattern
     *            strTime的格式
     * @see #FORMAT_DATE
     * @see #FORMAT_TIME
     * @see #FORMAT_DATE_TIME
     * @see #FORMAT_MONTH_DAY_TIME
     * @return 格式化结果
     * @throws UnsupportedOperationException
     *             if parsing is not supported
     * @throws IllegalArgumentException
     *             if the text to parse is invalid
     * 
     */
    public static Date s2d(String strTime, String pattern) {
        if (StringUtils.isEmpty(strTime)) {
            return null;
        }
        DateTime d = DateTimeFormat.forPattern(pattern).parseDateTime(strTime);
        return d.toDate();
    }

    /**
     * date类型转字符串
     * @param date
     * @param pattern
     * @return
     */
    public static String d2s(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        DateTime time = new DateTime(date);
        return time.toString(pattern);
    }

}
