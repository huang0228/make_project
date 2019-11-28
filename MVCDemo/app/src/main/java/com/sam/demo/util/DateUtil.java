package com.sam.demo.util;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.sam.demo.MyApplication;
import com.sam.demo.entity.CalendarWeekMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import cn.addapp.pickers.util.ConvertUtils;

/**
 * 日期工具类
 *
 * @author huangziwei
 * @date 2016.1.13
 * modify by sam
 */
public class DateUtil {

    public final static int DAY = 86400000; //１天＝24*60*60*1000ms
    public final static int HOUR = 3600000;
    public final static int MIN = 60000;

    /**
     * 获取某个月份的天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDays(int year, int month) {
        if (month > 12) {
            month = 1;
            year += 1;
        } else if (month < 1) {
            month = 12;
            year -= 1;
        }
        int[] arr = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int days = 0;

        if (isLeapYear(year)) {
            arr[1] = 29; // 闰年2月29天
        }

        try {
            days = arr[month - 1];
        } catch (Exception e) {
            e.getStackTrace();
        }

        return days;
    }

    /**
     * 是否为闰年
     *
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }


    /**
     * 根据年份和月份获取日期数组，1、2、3...
     *
     * @param year
     * @param month
     * @return
     */
    public static List<String> getMonthDaysArray(int year, int month) {
        List<String> dayList = new ArrayList<String>();
        int days = DateUtil.getMonthDays(year, month);
        for (int i = 1; i <= days; i++) {
            dayList.add(i + "");
        }
        return dayList;
    }


    /**
     * 获取当前系统时间的年份
     *
     * @return
     */
    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 获取当前系统时间的月份
     *
     * @return
     */
    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前系统时间的月份的第几天
     *
     * @return
     */
    public static int getCurrentMonthDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前系统日期
     *
     * @return
     */
    public static String getCurrentDay() {
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        return year+"-"+month+"-"+day;
    }

    /**
     * 获取当前系统上一个月日期
     *
     * @return
     */
    public static String getPreviousDay() {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH,-1);
        return DateToStr(calendar.getTime(),new SimpleDateFormat("yyyy-MM-dd"));
    }

    /**
     * 获取三年前日期
     *
     * @return
     */
    public static String getThreeYearAgo() {
        Calendar calendar=Calendar.getInstance();
        /**sam.huang
         * date:2018.10.31
         *修复日期错误,1.3.5.7.8.10.12时日期问题
         */
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR,-3);
        return DateToStr(calendar.getTime(),new SimpleDateFormat("yyyy-MM-dd"));
        //---------------------------------------------------------
    }

    /**
     * 根据月份获取所在季度
     * @param month
     * @return
     */
    public static int getCurrentJiDU(int month){
        if (month==1||month==2||month==3){
            return 1;
        }else if (month==4||month==5||month==6){
            return 2;
        }else if (month==7||month==8||month==9){
            return 3;
        }else{
            return 4;
        }
    }

    /**
     * 获取当前系统时间的小时数
     *
     * @return
     */
    public static int getHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前系统时间的分钟数
     *
     * @return
     */
    public static int getMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    /**
     * 获取当前系统时间的秒数
     *
     * @return
     */
    public static int getSecond() {
        return Calendar.getInstance().get(Calendar.SECOND);
    }

    /**
     * 获取当前系统时间的毫秒数
     *
     * @return
     */
    public static int getMillSecond() {
        return Calendar.getInstance().get(Calendar.MILLISECOND);
    }


    /**
     * 根据系统默认时区，获取当前时间与time的天数差
     *
     * @param time 相差的天数
     * @return　等于０表示今天，大于０表示今天之前
     */
    public static long getDaySpan(long time) {
        return getTimeSpan(time, DAY);
    }

    public static long getHourSpan(long time) {
        return getTimeSpan(time, HOUR);
    }

    public static long getMinSpan(long time) {
        return getTimeSpan(time, MIN);
    }

    public static long getTimeSpan(long time, long span) {
        // 系统默认时区，ms
        int tiemzone = TimeZone.getDefault().getRawOffset();
        return (System.currentTimeMillis() + tiemzone) / span
                - (time + tiemzone) / span;
    }

    public static boolean isToday(long time) {
        return getDaySpan(time) == 0;
    }

    public static boolean isYestoday(long time) {
        return getDaySpan(time) == 1;
    }

    public static boolean isTomorrow(long time) {
        return getDaySpan(time) == -1;
    }

    /**
     * @return 返回当前时间，yyyy-MM-dd HH-mm-ss
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd HH-mm-ss");
    }

    public static String getDate(String format) {
        return getDate(new java.util.Date().getTime(), format);
    }

    public static String getDate(long time, String format) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(format);
        String date = sDateFormat.format(time);
        return date;
    }

    /**
     * * 日期转换成字符串
     * * @param date
     * * @return str
     */
    public static String DateToStr(Date date,SimpleDateFormat format) {
        String str = format.format(date);
        return str;
    }

    /**
     *  * 字符串转换成日期
     *  * @param str
     *  * @return date
     *
     */
    public static Date StrToDate(String str,SimpleDateFormat format) {
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取N天前的日期
     * @param date n天
     * @param dateFormat 日期格式
     * @return
     */
    public static String getHistoryDateByDay(String date,String dateFormat)
    {
        String value="";
        long totalMillSecond=0;
        long tempMillSecond=0;
        if(TextUtils.isEmpty(date))
            return value;
        tempMillSecond=24*60*60*1000*Long.parseLong(date);
        totalMillSecond=new Date().getTime()-tempMillSecond;
        value=new SimpleDateFormat(dateFormat).format(new Date(totalMillSecond));
        return value;
    }

    /**
     * 获取N天前的日期
     * @param day n天
     * @return
     */
    public static Long getHistoryDateByDay(String day)
    {
        String value="";
        long totalMillSecond=0;
        long tempMillSecond=0;
        if(TextUtils.isEmpty(day))
            return Long.valueOf(0);
        tempMillSecond=24*60*60*1000*Long.parseLong(day);
        totalMillSecond=new Date().getTime()-tempMillSecond;
        return totalMillSecond;
    }

    /**
     * 获取当前系统n月前日期
     * @param format
     * @param n
     * @return
     */
    public static String getPreviousMonth(String format,int n) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH,-n);
        return DateToStr(calendar.getTime(),new SimpleDateFormat(format));
    }

    /**
     * 获取当前月的第一天
     * @param format
     * @return
     */
    public static String getFirstDayOfMonth(String format){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MONTH,0);
        calendar.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        String value=simpleDateFormat.format(calendar.getTime());
        return value;
    }

    /**
     * 获取当前月最后一天
     * @param format
     * @return
     */
    public static String getLastDayOfMonth(String format)
    {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));//获取当前月最后一天
        String value=simpleDateFormat.format(calendar.getTime());
        return value;
    }

    /**
     * 根据周确定对应的日期
     * @param curYear
     * @param curWeek
     */
    public static String selectDateOfCalendarByWeek(int curYear,int curWeek)
    {
        String returnValue="";//返回日期
        String tempCurWeek="";//当前周临时变量
        String yearAndWeek="";//当前年的第几周
        if (curWeek<10)
        {
            tempCurWeek="0"+curWeek;
        }else
        {
            tempCurWeek=String.valueOf(curWeek);
        }
        yearAndWeek=curYear+tempCurWeek;

        ArrayList<CalendarWeekMode> data = new ArrayList<>();
        try {
            String json = ConvertUtils.toString(MyApplication.getInstance().getAssets().open("calendar.json"));
            data.addAll(JSON.parseArray(json, CalendarWeekMode.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!data.isEmpty()){
            for (int k=0;k<data.size();k++)
            {
                if (yearAndWeek.equals(data.get(k).getWEEK()))//取json数据中等于当前年中的选定周的json数据
                {
                    returnValue=data.get(k).getDATE();
                    break;
                }
            }
        }
        return returnValue;
    }
    /**
     * 根据周确定对应的日期
     * @param yearAndWeek 某年某周
     */
    public static String selectDateOfCalendarByWeek(String yearAndWeek)
    {
        String returnValue="";//返回日期
        ArrayList<CalendarWeekMode> data = new ArrayList<>();
        try {
            String json = ConvertUtils.toString(MyApplication.getInstance().getAssets().open("calendar.json"));
            data.addAll(JSON.parseArray(json, CalendarWeekMode.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!data.isEmpty()){
            for (int k=0;k<data.size();k++)
            {
                if (yearAndWeek.equals(data.get(k).getWEEK()))//取json数据中等于当前年中的选定周的json数据
                {
                    returnValue=data.get(k).getDATE();
                    break;
                }
            }
        }
        return returnValue;
    }
}