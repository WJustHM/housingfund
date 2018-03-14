package com.handge.housingfund.common.service.util;

/**
 * <p>Title: 时间和日期的工具类</p>
 * <p>Description: DateUtil类包含了标准的时间和日期格式，以及这些格式在字符串及日期之间转换的方法</p>
 * <p>Copyright: Copyright (c) 2007 advance,Inc. All Rights Reserved</p>
 * <p>Company: advance,Inc.</p>
 *
 * @author advance
 * @version 1.0
 */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

    public static String dateStrTransform(String str) {
        if (str == null) return "";
        String returnStr = "";
        switch (str.length()) {
            case 6:
                returnStr = str.substring(0, 4) + "-" + str.substring(4);
                break;
            case 8:
                returnStr = str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6);
                break;
        }
        return returnStr;
    }


    public static String dbformatYear = "yyyy";

    public static String dbformatMonth = "MM";

    //public static String dbformatDay="dd";

    public static String dbformatYear_Month = "yyyyMM";

    public static String dbformatYear_Month_Day = "yyyyMMdd";

    private static SimpleDateFormat simpleM = new SimpleDateFormat("yyyy-MM");
    private static SimpleDateFormat simpleData = new SimpleDateFormat("yyyyMM");
    private static SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");


    /**
     * 获取年
     *
     * @param iDate eg:2002-01-01
     * @return
     */
    public static int getYear(String iDate) {
        int iYear = Integer.parseInt(iDate.substring(0, 4));
        return iYear;
    }

    /**
     * 获取月
     *
     * @param iDate eg:2002-01-01
     * @return
     */
    public static int getMonth(String iDate) {
        int iMonth = Integer.parseInt(iDate.substring(5, 7));
        return iMonth;
    }

    /**
     * 获取天
     *
     * @param iDate eg:2002-01-01
     * @return
     */
    public static int getDay(String iDate) {
        int iDay = Integer.parseInt(iDate.substring(8, 10));
        return iDay;
    }

    /**
     * 获取日期
     *
     * @param date eg:2002-01-01 12:12:33
     * @return
     */
    public static String subDate(String date) {
        return date.substring(0, 10);
    }

    /**
     * 计算是否是季度末
     *
     * @param date eg:2002-01-01
     * @return
     */
    public static boolean isSeason(String date) {
        int getMonth = Integer.parseInt(date.substring(5, 7));
        boolean sign = false;
        if (getMonth == 3)
            sign = true;
        if (getMonth == 6)
            sign = true;
        if (getMonth == 9)
            sign = true;
        if (getMonth == 12)
            sign = true;
        return sign;
    }

    /**
     * djj  时间转换
     */
    public static Date dateStringtoStringDate(Date date) {

        Date new_date = null;
        try {
            new_date = sim.parse(sim.format(date));
        } catch (ParseException e) {
            throw new ErrorException(e);
        }
        return new_date;
    }

    /**
     * djj  时间转换
     */
    public static Date dateStringtoStringDate(Date date,String format) {
        SimpleDateFormat formats=new SimpleDateFormat(format);
        Date new_date = null;
        try {
            new_date = formats.parse(formats.format(date));
        } catch (ParseException e) {
            throw new ErrorException(e);
        }
        return new_date;
    }

    /**
     * djj 时间转换
     */
    public static String convertToFormat(String date) {
        String new_date = null;
        try {
            new_date = sim.format(sim.parse(date));
        } catch (ParseException e) {
            throw new ErrorException(e);
        }
        return new_date;
    }

    /**
     * 计算从现在开始几天后的时间
     *
     * @param afterDay eg:1
     * @return
     */
    public static String getDateFromNow(int afterDay) {
        GregorianCalendar calendar = new GregorianCalendar();
        Date date = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + afterDay);
        date = calendar.getTime();

        return df.format(date);
    }

    /**
     * 计算从现在开始几天后的时间带格式
     *
     * @param afterDay      eg:1
     * @param format_string eg:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getDateFromNow(int afterDay, String format_string) {
        Calendar calendar = Calendar.getInstance();
        Date date = null;

        DateFormat df = new SimpleDateFormat(format_string);

        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + afterDay);
        date = calendar.getTime();

        return df.format(date);
    }

    /**
     * 得到当前时间，用于文件名，没有特殊字符，使用yyyyMMddHHmmss格式
     *
     * @param afterDay eg:30
     * @return by time
     */
    public static String getNowForFileName(int afterDay) {
        GregorianCalendar calendar = new GregorianCalendar();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + afterDay);
        Date date = calendar.getTime();
        return df.format(date);
    }

    /**
     * 与现在的日期对比  得到天数
     *
     * @param limitDate eg:2017-06-06
     * @return
     */
    public static long getLongCompare(String limitDate) {

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(getYear(limitDate), getMonth(limitDate) - 1, getDay(limitDate));
        Date date = calendar.getTime();
        long datePP = date.getTime();
        Date nowDate = new Date();
        long dateNow = nowDate.getTime();
        return ((dateNow - datePP) / (24 * 60 * 60 * 1000));

    }

    /**
     * method diffdate 计算两个日期间相隔的日子
     *
     * @param beforDate 格式:2005-06-20
     * @param afterDate 格式:2005-06-21
     * @return
     */
    public static int diffDate(String beforDate, String afterDate) {
        String[] tt = beforDate.split("-");
        Date firstDate = new Date(Integer.parseInt(tt[0]), Integer.parseInt(tt[1]) - 1, Integer.parseInt(tt[2]));

        tt = afterDate.split("-");
        Date nextDate = new Date(Integer.parseInt(tt[0]), Integer.parseInt(tt[1]) - 1, Integer.parseInt(tt[2]));
        return (int) (nextDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取今天的日期的字符串
     *
     * @return
     */
    public static String getToday() {
        Calendar cld = Calendar.getInstance();
        Date date = new Date();
        cld.setTime(date);
        int intMon = cld.get(Calendar.MONTH) + 1;
        int intDay = cld.get(Calendar.DAY_OF_MONTH);
        String mons = String.valueOf(intMon);
        String days = String.valueOf(intDay);
        if (intMon < 10)
            mons = "0" + String.valueOf(intMon);
        if (intDay < 10)
            days = "0" + String.valueOf(intDay);
        return String.valueOf(cld.get(Calendar.YEAR)) + "-" + mons + "-" + days;
    }

    /**
     * 获取当前月份
     *
     * @return 返回字符串 格式：两位数
     */
    public static String getCurrentMonth() {
        String strmonth = null;
        Calendar cld = Calendar.getInstance();
        Date date = new Date();
        cld.setTime(date);
        int intMon = cld.get(Calendar.MONTH) + 1;
        if (intMon < 10)
            strmonth = "0" + String.valueOf(intMon);
        else
            strmonth = String.valueOf(intMon);
        date = null;
        return strmonth;
    }

    /**
     * 获取昨天的日期的字符串
     */
    public static String getYestoday() {
        Calendar cld = Calendar.getInstance();
        Date date = new Date();
        cld.setTime(date);
        cld.add(Calendar.DATE, -1);
        int intMon = cld.get(Calendar.MONTH) + 1;
        int intDay = cld.get(Calendar.DAY_OF_MONTH);
        String mons = String.valueOf(intMon);
        String days = String.valueOf(intDay);
        if (intMon < 10)
            mons = "0" + String.valueOf(intMon);
        if (intDay < 10)
            days = "0" + String.valueOf(intDay);
        return String.valueOf(cld.get(Calendar.YEAR)) + "-" + mons + "-" + days;
    }

    /**
     * 格式化时间
     *
     * @param aDate  Date
     * @param format eg:"yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String date2Str(Date aDate, String format) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(format);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    public static String date2Str1(Date aDate, String format) {
        SimpleDateFormat df = null;
        String returnValue = null;

        if (aDate != null) {
            df = new SimpleDateFormat(format);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    public static String DBdate2Str(String dbformat, String dbDate, String format) {

        return DateUtil.date2Str(DateUtil.safeStr2Date(dbformat, dbDate), format);
    }

    public static String DBdate2Str(String dbformat, Date dbDate, String format) {

        return DateUtil.date2Str(dbDate, format);
    }

    /**
     * 格式化时间
     *
     * @param aDate Date
     * @return
     */
    public static String date2Str(Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }


    /**
     * 字符串转时间
     *
     * @param format  eg:"yyyy-MM-dd HH:mm:ss"
     * @param strDate eg:"2017-02-11 12:12:12"
     * @return
     * @throws ParseException
     */
    public static Date str2Date(String format, String strDate) throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        if (StringUtil.notEmpty(strDate)) {
            df = new SimpleDateFormat(format);
            try {
                date = df.parse(strDate);
            } catch (ParseException pe) {
                throw pe;
                //return null;
            }
        }
        return (date);
    }


    /**
     * 字符串转时间
     *
     * @param strDate eg:"2017-02-11 12:12:12"
     * @return
     * @throws ParseException
     */
    public static Date str2Date(String strDate) throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {

            throw pe;

        }
        return (date);
    }


    /**
     * 字符串是否符合时间格式
     *
     * @param format  eg:"yyyy-MM-dd HH:mm:ss"
     * @param strDate eg:"2017-02-11 12:12:12"
     * @return
     */
    public static boolean isFollowFormat(String strDate, String format) {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(format);
        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    /**
     * 字符串是否符合时间格式
     *
     * @param format  eg:"yyyy-MM-dd HH:mm:ss"
     * @param strDate eg:"2017-02-11 12:12:12"
     * @return
     */
    public static boolean isFollowFormat(String strDate, String format, boolean allowNull) {

        if (!StringUtil.notEmpty(strDate)) {
            return allowNull;
        }

        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(format);
        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    /**
     * 字符串转时间
     *
     * @param format  eg:"yyyy-MM-dd HH:mm:ss"
     * @param strDate eg:"2017-02-11 12:12:12"
     * @return
     * @throws ParseException
     */
    public static Date safeStr2Date(String format, String strDate) {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(format);
        try {
            date = df.parse(strDate);
        } catch (Exception e) {
            return null;
        }
        return (date);
    }

    public static String safeStr2DBDate(String format, String strDate, String formatDb) {

        return DateUtil.date2Str(DateUtil.safeStr2Date(format, strDate), formatDb);
    }

    /**
     * 得到格式化后的系统当前日期
     *
     * @param format 格式模式字符串
     * @return 格式化后的系统当前时间，如果有异常产生，返回空串""
     */
    public static String getNowDateTime(String format) {
        String strReturn = null;
        Date now = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            strReturn = sdf.format(now);
        } catch (Exception e) {
            strReturn = "";
        }
        return strReturn;
    }

    /**
     * 获取当前格式化时间
     *
     * @return
     */
    public static String getNowDateTime() {
        String strReturn = null;
        Date now = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            strReturn = sdf.format(now);
        } catch (Exception e) {
            strReturn = "";
        }
        return strReturn;
    }

    /**
     * 将字符串数组使用指定的分隔符合并成一个字符串。
     *
     * @param array 字符串数组
     * @param delim 分隔符，为null的时候使用""作为分隔符（即没有分隔符）
     * @return 合并后的字符串
     * @since 0.4
     */
    public static String combineStringArray(String[] array, String delim) {
        int length = array.length - 1;
        if (delim == null) {
            delim = "";
        }
        StringBuffer result = new StringBuffer(length * 8);
        for (int i = 0; i < length; i++) {
            result.append(array[i]);
            result.append(delim);
        }
        result.append(array[length]);
        return result.toString();
    }

    /**
     * 获取周对应数字
     *
     * @param strWeek
     * @return
     */
    public static int getWeekNum(String strWeek) {
        int returnValue = 0;
        if (strWeek.equals("Mon")) {
            returnValue = 1;
        } else if (strWeek.equals("Tue")) {
            returnValue = 2;
        } else if (strWeek.equals("Wed")) {
            returnValue = 3;
        } else if (strWeek.equals("Thu")) {
            returnValue = 4;
        } else if (strWeek.equals("Fri")) {
            returnValue = 5;
        } else if (strWeek.equals("Sat")) {
            returnValue = 6;
        } else if (strWeek.equals("Sun")) {
            returnValue = 0;
        } else if (strWeek == null) {
            returnValue = 0;
        }

        return returnValue;
    }

    /**
     * 获取日期
     *
     * @param timeType 时间类型，譬如：Calendar.DAY_OF_YEAR
     * @param timenum  时间数字，譬如：-1 昨天，0 今天，1 明天
     * @return 日期
     */
    public static final Date getDateFromNow(int timeType, int timenum) {
        Calendar cld = Calendar.getInstance();
        cld.set(timeType, cld.get(timeType) + timenum);
        return cld.getTime();
    }

    /**
     * 获取日期
     *
     * @param timeType      时间类型，譬如：Calendar.DAY_OF_YEAR
     * @param timenum       时间数字，譬如：-1 昨天，0 今天，1 明天
     * @param format_string 时间格式，譬如："yyyy-MM-dd HH:mm:ss"
     * @return 字符串
     */
    public static final String getDateFromNow(int timeType, int timenum, String format_string) {
        if ((format_string == null) || (format_string.equals("")))
            format_string = "yyyy-MM-dd HH:mm:ss";
        Calendar cld = Calendar.getInstance();
        Date date = null;
        DateFormat df = new SimpleDateFormat(format_string);
        cld.set(timeType, cld.get(timeType) + timenum);
        date = cld.getTime();
        return df.format(date);
    }

    /**
     * 获取当前日期的字符串
     *
     * @param format_string 时间格式，譬如："yyyy-MM-dd HH:mm:ss"
     * @return 字符串
     */
    public static final String getDateNow(String format_string) {
        if ((format_string == null) || (format_string.equals("")))
            format_string = "yyyy-MM-dd HH:mm:ss";
        Calendar cld = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat(format_string);
        return df.format(cld.getTime());
    }

    /**
     * 获取下一个月
     *
     * @param date 时间格式，譬如："201707"
     * @return 字符串 譬如："201708"
     */
    public static String getNextMonth(String date) {
        if (date.equals("") || date == null) return "";
        if (date.length() != 6) return "";
        int n = Integer.parseInt(date.substring(0, 4));
        String y = date.substring(4, 6);
        String newNY = "";
        if (y.equals("12")) {
            newNY = String.valueOf(n + 1) + "01";
        } else if (y.equals("10") || y.equals("11")) {
            newNY = String.valueOf(n) + ((Integer.parseInt(y) + 1));
        } else if (y.equals("09")) {
            newNY = String.valueOf(n) + ((Integer.parseInt(y.substring(1, 2)) + 1));
        } else {
            newNY = String.valueOf(n) + "0" + ((Integer.parseInt(y.substring(1, 2)) + 1));
        }
        return newNY;
    }
    /**
     * 获取下一个月
     */
    public static String getNextMonthStr(String date){
        Calendar calendar =Calendar.getInstance();
        Date date1 = null;
        try {
            calendar.setTime(str2Date("yyyyMM",date));
            calendar.add(Calendar.MONTH,1);
            date1 = calendar.getTime();
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.User_Defined,"日期格式不正确");
        }
        return date2Str(date1,"yyyyMM");
    }

    /**
     * 获取上一个月
     *
     * @return 字符串 譬如："201708"
     */
    public static String getPreviousMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        return String.format("%s%s", year, month);
    }


    /**
     * 计算当前日期几个月后的日期
     *
     * @param date       当前日期
     * @param f
     * @param afterMouth 月份数
     * @return
     */
    public static String getNextDate(Date date, String f, int afterMouth) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, afterMouth);
        c.add(Calendar.DAY_OF_MONTH, 1);
        DateFormat df = new SimpleDateFormat(f);
        return df.format(c.getTime());
    }

    /**
     * 获取当前时间的日期,时间数组, 譬如：Array{"20170707","050505"}
     *
     * @return
     */
    public static String[] getDatetime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HHmmss");
        String datetime = format.format(new Date());
        String[] datetimes = datetime.split("\\s+");
        return datetimes;
    }

    public static Date getCurrentDateTime() {
        return new Date();
    }

    public static int getAge(Date birthDay) {
        Calendar cal = Calendar.getInstance();

        if (birthDay == null || cal.before(birthDay)) {
            return 0;
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            } else {
                age--;
            }
        }
        return age;
    }

    /**
     * 1.去掉前端传回数据的“-”，int参数填任意负数；
     * 2.填充“-”到前端，int参数填6或8，如199306就填6，会变成1993-06；8位长度的同理；
     * 3.8位或更长的格式，也可以传6，那么就只会获得前6位的数据；同理超过8位的也可以传8。
     *
     * @param date
     * @param flag
     * @return
     */
    public static String str2str(String date, int flag) {

        if (StringUtil.isEmpty(date))
            return null;

        try {
            if (flag < 0) {
                date = date.replace("-", "");
            } else {
                if (flag == 6 || date.length() == 6) {
                    date = date.substring(0, 4) + "-" + date.substring(4, 6);
                } else if (flag == 8 || date.length() == 8) {
                    date = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
                } else {
                    throw new ErrorException("参数错误");
                }
            }
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    public static String datetostr(String date) {
        String str;
        try {
            str = simpleM.format(simpleData.parse(date));

        } catch (Exception e) {
            throw new ErrorException(e);
        }
        return str;
    }

    /**
     * 比较两个日期的前后
     */
    public static boolean compare_date(String date1, String date2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = df.parse(date1);
            dt2 = df.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }
}
