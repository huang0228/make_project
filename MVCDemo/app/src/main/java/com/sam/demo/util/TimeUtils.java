package com.sam.demo.util;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/***
 * 时间管理
 *
 * @author ztf
 */
public class TimeUtils {
    private static String mYear;
    private static String mMonth;
    private static String mDay;
    private static String mWay;

    /**
     * 将字符串类型的时间转换成long类型的
     *
     * @param date       字符串类型
     * @param formatType 格式化类型, 比如2014-12-16 09:49:00
     * @return
     */
    public static long getTime(String date, String formatType) {
        SimpleDateFormat format = new SimpleDateFormat(formatType);
        ParsePosition position = new ParsePosition(0);
        Date t = format.parse(date, position);
        long time = t.getTime();
        return time;
    }

    /**
     * 获取北京时间戳
     * @return
     */
    public static long getGMT8TimeTmpl(){
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        Date date = calendar.getTime();
        return date.getTime();
    }

    /**
     *获取一个月前的日期
     * @param dateTemp 传入的日期
     * @return
     */
    public static long getMonthAgo(Date dateTemp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTemp);
        calendar.add(Calendar.MONTH, -1);
        Date date=calendar.getTime();
        return date.getTime();
    }

    /**
     * 将格林治时间城转化为北京时间戳
     * @param oidTimeTmp
     * @return
     */
    public static long getGMT8TimeTmplByGMT0(Long oidTimeTmp) {

        return oidTimeTmp + TimeZone.getTimeZone("GMT+8").getRawOffset();
    }


    /**
     * 将long类型的时间转换成字符串类型的
     *
     * @param date       long类型 毫秒
     * @param formatType 格式化类型, 比如2014-12-16 09:49:00
     * @return
     */
    public static String getTime(long date, String formatType) {
        SimpleDateFormat format = new SimpleDateFormat(formatType);
        String time = format.format(date);
        return time;
    }

    /***
     * 获得当前日期（周一至周日） 同时获得星期对应的当前时间
     *
     * @return
     * @throws Exception
     */
    public static String getWeekTime() throws Exception {
        /*
         * SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		 * dateFormat.applyPattern("E"); dateFormat.applyLocalizedPattern("E");
		 * Date date=dateFormat.parse("2014-05-08");
		 * System.out.println("--一个星期中的天数"+dateFormat.format(date));
		 */
        GregorianCalendar d = new GregorianCalendar();
        int weekday = d.get(Calendar.WEEK_OF_MONTH);
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "日";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return mWay;
    }

    /***
     * 获得当前日期(2014-04-02)
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /***
     * 获得当前时间(12:12:12)
     *
     * @return
     */
    public static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /***
     * 获得当前日期(2014-04-02)
     *
     * @return
     */
    public static Date getCurrentDate() {
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return curDate;
    }

    /***
     * 获得当前日期(2014-04-02)
     *
     * @return
     */
    public static String getCurrentDateDetail() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sDateFormat.format(new Date());
        return date;
    }


    // 获得本周星期一的日期
    public static String getCurrentMonday() {
        weeks = -1;
        int plus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, plus);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    private static int weeks = 1;

    // 获得当前日期与本周一相差的天数
    private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天,星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    }

    // 获得相应周的（周一至周日）的日期 0
    public static String getSunday(int weeks, int weekday) {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + weekday);
        Date monday = currentDate.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        // DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }


    /**
     * 产生周序列,即得到当前时间所在的年度是第几周
     */
    public static String getSeqWeek() {
        Calendar c = Calendar.getInstance(Locale.CHINA);
        String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
        if (week.length() == 1) {
            week = "0" + week;
        }
        String year = Integer.toString(c.get(Calendar.YEAR));
        return year + week;
    }

    /**
     * 去除时分秒
     *
     * @param str
     * @return
     */
    public static String parseStr(String str) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(dateFormat.parse(str));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 去除年时分秒
     *
     * @param str
     * @return
     */
    public static String parseStrYearMD(String str) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        long time = getTime(str);
        return format.format(time);
    }

    /**
     * 时间字符串去除秒
     *
     * @param timeStr
     * @return
     */
    public static String timeParseStr(String timeStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long time = getTime(timeStr);
        return format.format(time);
    }

    public static String parseTimeStr(String str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd HH:mm:ss");
            return sdf1.format(sdf.parse(str));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串时间比较(传值是否大于当前时间)
     *
     * @param time 例: 2014-09-01
     * @return true 是当前时间大(过期), false 则相反
     */
    public static boolean timeCompare(String time) {
        long t = System.currentTimeMillis();
        long t1 = getTime(time);
        return t > t1;
    }

    // 将字符串类型的时间转换成long类型的
    public static long getTime(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition position = new ParsePosition(0);
        Date t = format.parse(date, position);
        long time = t.getTime();
        return time;
    }

    /**
     * 时间格式化, 去掉年份 显示07-03 HH:mm:ss
     *
     * @param time
     * @return
     */
    public static String timeFormat(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        long t = getTime(time);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        return format.format(t);
    }

    /**
     * 时间格式化, 去掉年月 显示HH:mm
     *
     * @param time
     * @return
     */
    public static String timeFormatHHmm(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        long t = getTime(time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(t);
    }

    /**
     * 时间格式化, 显示当天多少号
     *
     * @param time
     * @return
     */
    public static String gettimeddForlong(long time) {

        SimpleDateFormat format = new SimpleDateFormat("dd");
        return format.format(time);
    }

    /**
     * 字符串时间比较(参数1 跟参数2比较)
     * <p>
     * 例: 2014-09-01
     *
     * @return true 参数1时间大
     */
    public static boolean timeCompare(String time1, String time2) {
        long t1 = getTime(time1);
        long t2 = getTime(time2);
        return t1 > t2;

    }

    /**
     * 时间差，转换成分钟
     * <p>
     * 例: 2014-09-01
     *
     * @return
     */
    public static int timecha(String startTime, String endTime) {
        int cha = 0;
        long between = (getTime(endTime) - getTime(startTime)) / 1000;
        long day1 = between / (24 * 3600);
        long hour1 = between % (24 * 3600) / 3600;
        long minute1 = between % 3600 / 60;
        cha = (int) (day1 * 24 * 60 + hour1 * 60 + minute1);
        return cha;
    }

    /**
     * long 类型时间解析MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String getTimeFormt(long date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String time = format.format(date);
        return time;
    }

    /**
     * 获取当前季度
     *
     * @return
     */
    public static String getQuarter() {
        String quarter = "";
        int month = -1;
        int year = -1;
        GregorianCalendar d = new GregorianCalendar();
        int weekday = d.get(Calendar.WEEK_OF_MONTH);
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        if (month == 1 || month == 2 || month == 3) {
            quarter = "1季度";
        } else if (month == 4 || month == 5 || month == 6) {
            quarter = "2季度";
        } else if (month == 7 || month == 8 || month == 9) {
            quarter = "3季度";
        } else if (month == 10 || month == 11 || month == 12) {
            quarter = "4季度";
        }
        return year + "-" + quarter;
    }

    /**
     * 获取年月, 返回int[]
     *
     * @return 0year, 1month
     */
    public static int[] getYearMonthDay() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int[] ymd = new int[3];
        ymd[0] = year;
        ymd[1] = month;
        ymd[2] = day;
        return ymd;
    }

    /***
     * 比较两个日期之间的大小2014-04-24
     */
    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            if (null != DATE1 && null != DATE2) {
                Date dt1 = df.parse(DATE1);
                Date dt2 = df.parse(DATE2);
                if (dt1.getTime() > dt2.getTime()) {
                    return 1;
                } else if (dt1.getTime() < dt2.getTime()) {
                    return -1;
                } else {
                    return 0;// 当前日期
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /***
     * 比较两个日期之间的大小2014-04-24
     */
    public static int compare_date_YMD(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (null != DATE1 && null != DATE2) {
                Date dt1 = df.parse(DATE1);
                Date dt2 = df.parse(DATE2);
                if (dt1.getTime() > dt2.getTime()) {
                    return 1;
                } else if (dt1.getTime() < dt2.getTime()) {
                    return -1;
                } else {
                    return 0;// 当前日期
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /***
     * 比较两个日期之间的大小2014-04-24
     */
    public static int compare_dateHource(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        try {
            if (null != DATE1 && null != DATE2) {
                Date dt1 = df.parse(DATE1);
                Date dt2 = df.parse(DATE2);
                if (dt1.getTime() > dt2.getTime()) {
                    return 1;
                } else if (dt1.getTime() < dt2.getTime()) {
                    return -1;
                } else {
                    return 0;// 当前日期
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    /**
     * 返回文字描述的日期
     *
     * @param date
     * @return
     */
    public static String getTimeFormatText(long date) {

        long diff = getGMT8TimeTmpl() - date;
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    /**
     * 日程对开始时间和结束时间进行解析, 分别算出经过哪些时间 //{09-03到09-21,11-10到11-25,11-20到11-22}
     *
     * @param start
     * @param end
     * @return
     */
    public static List<String> getEndTimeSubStartTime(String start, String end) {
        long between = (getTime(end) - getTime(start)) / 1000;
        long day = between / (24 * 3600);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar();
        List<String> list = new ArrayList<String>();
        for (int i = 0; i <= day; i++) {
            calendar.setTimeInMillis(getTime(start));
            calendar.add(Calendar.DATE, i);
            list.add(sdf.format(calendar.getTime()));
        }
        return list;
    }

    /**
     * 传进来一个服务器毫秒 与 当前时间对比，出一个文字性的时间提示 一小时前，二小时前，一天以前，两天以前
     *
     * @param sl 服务器时间或是需要和当时前时间对比的时间
     * @return 历史时间字符串
     */
    public static String getHistoryHint(long sl) {
        String str = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        GregorianCalendar fiveMinuteAgo = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE) - 5, calendar.get(Calendar.SECOND));
        GregorianCalendar fifteenMinuteAgo = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE) - 15, calendar.get(Calendar.SECOND));
        GregorianCalendar thirtentyMinuteAgo = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE) - 30, calendar.get(Calendar.SECOND));
        GregorianCalendar oneHourAgo = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR) - 1, calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        GregorianCalendar threeHourAgo = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR) - 3, calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        GregorianCalendar sixHourAgo = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR) - 6, calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        GregorianCalendar oneDayAgo = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) - 1, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        GregorianCalendar twoDayAgo = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) - 2, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        GregorianCalendar threeDayAgo = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) - 3, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        GregorianCalendar servenDayAgo = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) - 7, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));

        long fiveMinuteAgoL = fiveMinuteAgo.getTimeInMillis();
        long fifteenMinuteAgoL = fifteenMinuteAgo.getTimeInMillis();
        long thirtentyMinuteAgoL = thirtentyMinuteAgo.getTimeInMillis();
        long oneHourAgoL = oneHourAgo.getTimeInMillis();
        long threeHourAgoL = threeHourAgo.getTimeInMillis();
        long sixHourAgoL = sixHourAgo.getTimeInMillis();
        long oneDayAgoL = oneDayAgo.getTimeInMillis();
        long twoDayAgoL = twoDayAgo.getTimeInMillis();
        long threeDayAgoL = threeDayAgo.getTimeInMillis();
        long servenDayAgoL = servenDayAgo.getTimeInMillis();

        if (sl <= fiveMinuteAgoL && sl >= fifteenMinuteAgoL) {
            str = "5分钟前";
        } else if (sl <= fifteenMinuteAgoL && sl >= thirtentyMinuteAgoL) {
            str = "15分钟前";
        } else if (sl <= thirtentyMinuteAgoL && sl >= oneHourAgoL) {
            str = "半小时前";
        } else if (sl <= oneHourAgoL && sl >= threeHourAgoL) {
            str = "1小时前";
        } else if (sl <= threeHourAgoL && sl >= sixHourAgoL) {
            str = "3小时前";
        } else if (sl <= sixHourAgoL && sl >= oneDayAgoL) {
            str = "6小时前";
        } else if (sl <= oneDayAgoL && sl >= twoDayAgoL) {
            str = "1天前";
        } else if (sl <= twoDayAgoL && sl >= threeDayAgoL) {
            str = "2天前";
        } else if (sl <= threeDayAgoL && sl >= servenDayAgoL) {
            str = "3天前";
        } else if (sl <= servenDayAgoL) {
            str = "1周前";
        } else {
            str = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(sl));
        }
        return str;
    }

    /**
     * 根据传入的参数计算出距离当前时间几秒前, 几分钟前
     *
     * @param gap 时间字符串 2014-12-16 08:30:00
     * @return
     */
    public static String computeFromNowStr(String gap) {
        return computeFromNowStr(getTime(gap));
    }

    /**
     * 根据传入的参数计算出距离当前时间几秒前, 几分钟前
     *
     * @param gap long类型毫秒
     * @return
     */
    public static String computeFromNowStr(long gap) {
        long time = gap;
        if (gap < 0) {
            gap = 0;
        }
        long a = (System.currentTimeMillis() / 1000) - (gap / 1000);
        //防止本地时间大于服务器时间
        if (a < 0) {
            return dataFormatComputeNow(time); // 时间工具类 返回的是字符串类型的时间
        }
        gap = Math.abs(a);
        final String[] danweis = new String[]{"秒前", "分钟前", "小时前", "天前"};
        String danwei = danweis[0];
        if (gap >= 60) {
            gap = gap / 60;// 以分钟为单位
            danwei = danweis[1];
            if (gap >= 60) {/*
                gap = gap / 60;// 以小时为单位
				danwei = danweis[2];
				if (gap >= 24) {
					gap = gap / 24;
					danwei = danweis[3];
					if (gap > 3) {
						return dataFormatComputeNow(time); // 时间工具类 返回的是字符串类型的时间
					}

				}
			 */
                return dataFormatComputeNow(time); // 时间工具类 返回的是字符串类型的时间

            }
        }
        return gap + danwei;
    }

    /**
     * 时间格式化
     *
     * @param time       String 字符串类型
     * @param formatType 时间类型的格式化 格式
     * @return
     */
    public static String dataFormatComputeNow(String time, String formatType) {
        return dataFormatComputeNow(getTime(time, formatType));
    }

    /**
     * 时间格式化
     *
     * @param time long类型毫秒
     * @return 今天 时间 , 昨天, 星期几, 日期
     */
    public static String dataFormatComputeNow(long time) {
        String returnStr = "";
        // 传参进来的时间 Calendar
        Calendar oldC = Calendar.getInstance();
        oldC.setTimeInMillis(time);
        // 当前系统时间 Calendar
        Calendar nowC = Calendar.getInstance();
        nowC.setTimeInMillis(System.currentTimeMillis());

        // 传参时间的 年-日
        int oldY = oldC.get(Calendar.YEAR);
        int oldM = oldC.get(Calendar.MONTH);
        int oldD = oldC.get(Calendar.DAY_OF_MONTH);
        // 当前星期几, 得到的值必须-1
        int oldWeek = oldC.get(Calendar.DAY_OF_WEEK) - 1;

        // 当前系统时间 年-日
        int nowY = nowC.get(Calendar.YEAR);
        int nowM = nowC.get(Calendar.MONTH);
        int nowD = nowC.get(Calendar.DAY_OF_MONTH);
        if (oldY == nowY) {
            if (nowM - oldM == 0 && nowD - oldD < 7 && nowD - oldD > -1) {
                // 说明在一个星期内 显示昨天或者星期几
                if (nowD - oldD == 0) {
                    // 当天
                    returnStr = "今天 " + getTime(time, "HH:mm");
                } else if (nowD - oldD == 1) {
                    returnStr = "昨天 " + getTime(time, "HH:mm");
                } else {
                    returnStr = dayOfWeek(oldWeek) + " " + getTime(time, "HH:mm");
                }
            } else {
                // 同年 显示日期即可
                returnStr = getTime(time, "MM-dd");
            }

        } else {
            returnStr = getTime(time, "dd-MM-yyyy");
        }

        return returnStr;
    }


    /**
     * 聊天详情调用
     *
     * @param time
     * @return
     */
    public static String dataFormatChat(long time) {
        String returnStr = "";
        // 传参进来的时间 Calendar
        Calendar oldC = Calendar.getInstance();
        oldC.setTimeInMillis(time);
        // 当前系统时间 Calendar
        Calendar nowC = Calendar.getInstance();
        nowC.setTimeInMillis(System.currentTimeMillis());

        // 传参时间的 年-日
        int oldY = oldC.get(Calendar.YEAR);
        int oldD = oldC.get(Calendar.DAY_OF_MONTH);

        // 当前系统时间 年-日
        int nowY = nowC.get(Calendar.YEAR);
        int nowD = nowC.get(Calendar.DAY_OF_MONTH);

        if (oldY == nowY) {
            // 说明在当年, 同年 显示日期即可
            if (nowD - oldD == 0) {
                // 当天
                returnStr = getTime(time, "HH:mm:ss");
            } else {
                returnStr = getTime(time, "MM-dd HH:mm:ss");
            }

        } else {
            // 判断不在当年显示全部日期
            returnStr = getTime(time, "yyyy-MM-dd HH:mm:ss");
        }
        return returnStr;
    }

    /**
     * 计算星期几
     *
     * @param week
     * @return
     */
    private static String dayOfWeek(int week) {
        String weekStr = "";
        switch (week) {
            case 0:
                weekStr = "星期日";
                break;
            case 1:
                weekStr = "星期一";
                break;
            case 2:
                weekStr = "星期二";
                break;
            case 3:
                weekStr = "星期三";
                break;
            case 4:
                weekStr = "星期四";
                break;
            case 5:
                weekStr = "星期五";
                break;
            case 6:
                weekStr = "星期六";
                break;
        }
        return weekStr;
    }

    public static String getNewTimeLong() {
        long currentTimeMillis = System.currentTimeMillis();
        return String.valueOf(currentTimeMillis);
    }

    public static String getServerTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    public static int getTimeMiss(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (null != DATE1 && null != DATE2) {
                Date dt1 = df.parse(DATE1);
                Date dt2 = df.parse(DATE2);
                return (int) (dt2.getTime() - dt1.getTime()) / 1000;
            } else {
                return 0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getServerTime1() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    public static String getTimeAdd(Long time, long mouth) {
        time += mouth * 60 * 1000;
        Date da = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(da);
    }

}
