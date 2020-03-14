package com.yfkk.cardbag.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期工具类
 * <p>
 * Created by litao on 2018/1/25.
 */

public class DateUtils {

    /**
     * 联系人列表需要的时间格式
     *
     * @param
     * @return
     */
    public static String dateToConversation(long time) {
        if (time < 2000000000) {
            time = time * 1000;
        }

        Date mDate = new Date(time);
        int isYeaterday = isYeaterday(mDate, null);
        switch (isYeaterday) {
            case -1: // 今天
                return dateToStr(time, "HH:mm");
            case 0: // 昨天
                return "昨天";
            case 1: // 至少是前天
                return getWeekStr(time);
        }
        return dateToStr(time);
    }

    /**
     * 返回今天，昨天，2019-09-02这类的格式
     *
     * @param
     * @return
     */
    public static String dateSimple(long time) {
        if (time < 2000000000) {
            time = time * 1000;
        }

        Date mDate = new Date(time);
        int isYeaterday = isYeaterday(mDate, null);
        switch (isYeaterday) {
            case -1: // 今天
                return dateToStr(time, "HH:mm");
            case 0: // 昨天
                return "昨天";
        }
        return dateToStr(time);
    }

    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     *
     * @param
     * @return
     */
    public static String dateToStr(long time) {
        return dateToStr(time, "yyyy-MM-dd");
    }

    public static String dateToStr(long time, String format) {
        if (time < 2000000000) {
            time = time * 1000;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(new Date(time));
        return dateString;
    }

    public static String getDateString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(new Date());
    }

    /**
     * @param oldTime 较小的时间
     * @param newTime 较大的时间 (如果为空 默认当前时间 ,表示和当前时间相比)
     * @return -1 ：同一天. 0：昨天 . 1 ：至少是前天.
     * @throws ParseException 转换异常
     * @author LuoB.
     */
    public static int isYeaterday(Date oldTime, Date newTime) {
        if (newTime == null) {
            newTime = new Date();
        }
        try {
            // 将下面的 理解成 yyyy-MM-dd 00：00：00 更好理解点
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String todayStr = format.format(newTime);
            Date today = format.parse(todayStr);
            // 昨天 86400000=24*60*60*1000 一天
            if ((today.getTime() - oldTime.getTime()) > 0 && (today.getTime() - oldTime.getTime()) <= 86400000) {
                return 0;
            } else if ((today.getTime() - oldTime.getTime()) <= 0
                    && (today.getTime() - oldTime.getTime()) > -86400000) { // 至少是今天
                return -1;
            } else { // 至少是前天
                return 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 返回日期位于周几
     * (周日为1，周一为2)
     */
    public static int getWeekInt(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 返回日期位于周几
     * (周日为1，周一为2)
     */
    public static String getWeekStr(long time) {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String week = "";
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                week = "周日";
                break;
            case 2:
                week = "周一";
                break;
            case 3:
                week = "周二";
                break;
            case 4:
                week = "周三";
                break;
            case 5:
                week = "周四";
                break;
            case 6:
                week = "周五";
                break;
            case 7:
                week = "周六";
                break;
        }
        return week;
    }

    public static Date stringToDate(String strDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            ParsePosition pos = new ParsePosition(0);
            Date strtodate = formatter.parse(strDate, pos);
            return strtodate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static Date stringToDate(String strDate, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            ParsePosition pos = new ParsePosition(0);
            Date strtodate = formatter.parse(strDate, pos);
            return strtodate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Date();
    }


    // 24小时制改12小时,如：14:24 返回 下午 2:24
    public static String timeTo12(String strDate) {
        if (StringUtils.isEmpty(strDate)) {
            return strDate;
        }
        if (strDate.split(" ").length == 2) {
            return strDate;
        }
        try {
            int hour = Integer.parseInt(strDate.split(":")[0]);
            int min = Integer.parseInt(strDate.split(":")[1]);
            String timeStr = "";
            if (hour < 12) {
                timeStr = "上午 ";
                if (hour == 0) {
                    timeStr += "12";
                } else {
                    timeStr += hour < 10 ? "0" + hour : hour;
                }
            } else {
                timeStr = "下午 ";
                if (hour == 12) {
                    timeStr += hour;
                } else {
                    timeStr += (hour - 12 < 10) ? ("0" + (hour - 12)) : (hour - 12);
                }
            }
            timeStr += ":" + (min < 10 ? "0" + min : min);
            return timeStr;
        } catch (Exception e) {

        }
        return strDate;
    }
}
