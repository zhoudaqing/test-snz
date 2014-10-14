package io.terminus.snz.user.tool;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Author:Guo Chaopeng
 * Created on 14-6-24.
 */
public class DateUtil {
    private static final DateTimeFormatter YEAR_FORMAT = DateTimeFormat.forPattern("yyyy");

    /**
     * 获取当年第一个月，如 2014-01
     */
    public static String getCurrentYearFirstMonth() {
        String year = YEAR_FORMAT.print(System.currentTimeMillis());
        return year + "-01";
    }

    /**
     * 获取当年最后一个月，如 2014-12
     */
    public static String getCurrentYearLastMonth() {
        String year = YEAR_FORMAT.print(System.currentTimeMillis());
        return year + "-12";
    }

    public static Date getYesterdayStart() {
        return new DateTime().minusDays(1).withTimeAtStartOfDay().toDate();
    }

}
