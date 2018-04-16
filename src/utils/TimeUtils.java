package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 获取各种格式的时间工具类
 */
public class TimeUtils {
    /**
     * 获取当前时间
     *
     * @return 2018-03-17 10:47:50
     */
    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return format.format(new Date());
    }

    /**
     * 获取当前时分秒
     *
     * @return 10:47:50
     */
    public static String getTimeSupreme() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");//设置日期格式
        return format.format(new Date());
    }

    /**
     * 获取当前日期
     *
     * @return 2018-03-17
     */
    public static String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        return format.format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return 1521368590
     */
    public static long getCurrentTimeSecond() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取今天是星期几
     *
     * @return 1->周日 6->周五 7->周六
     */
    public static int getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 获取今天是星期几
     *
     * @return 星期日...
     */
    public static String getDayOfWeekString() {
        String result = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                result = "星期日";
                break;
            case 2:
                result = "星期一";
                break;
            case 3:
                result = "星期二";
                break;
            case 4:
                result = "星期三";
                break;
            case 5:
                result = "星期四";
                break;
            case 6:
                result = "星期五";
                break;
            case 7:
                result = "星期六";
                break;
        }
        return result;
    }

    public static int getDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前月份
     *
     * @return 1 2 3 4 ... 11 12
     */
    public static int getMonthOfYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前年份
     *
     * @return 2018
     */
    public static String getCurrentYear() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(new Date());
    }

    /**
     * 计算时间差
     *
     * @param courseTime  上课开始时间
     * @param timeSupreme 当前时分秒
     * @return 时间差
     */
    public static long calculateTimeLag(String courseTime, String timeSupreme) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//20:40:00
        String toDate = TimeUtils.getCurrentDate() + " " + courseTime;
        String fromDate = TimeUtils.getCurrentDate() + " " + timeSupreme;
        long result = 0;
        try {
            long from = simpleFormat.parse(fromDate).getTime();
            long to = simpleFormat.parse(toDate).getTime();
            result = to - from;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 计算时间差
     *
     * @param meetingTime 会议开始时间
     * @param timeSupreme 当前时分秒
     * @return 时间差
     */
    public static long calculateTimeLagMeet(String meetingTime, String timeSupreme) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//20:40:00
        String toDate = meetingTime + ":00";
        String fromDate = timeSupreme;
        long result = 0;
        try {
            long from = simpleFormat.parse(fromDate).getTime();
            long to = simpleFormat.parse(toDate).getTime();
            result = to - from;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 计算时间差
     *
     * @param time    目标时间
     * @param current 当前时间
     * @return 目标时间到当前时间的时间差
     */
    public static long calculateTimeLagAct(String time, String current) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//20:40:00
        String toDate = time + ":00";
        String fromDate = current;

        System.out.println(time + "--->" + current);

        long result = 0;
        try {
            long from = simpleFormat.parse(fromDate).getTime();
            long to = simpleFormat.parse(toDate).getTime();
            result = to - from;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
