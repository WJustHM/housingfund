package com.handge.housingfund.collection.utils;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.util.ErrorException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by 凡 on 2017/7/31.
 */
public class ComUtils {

    /**
     * 6位年月格式的日期验证:yyyy-MM
     */
    public static boolean dateYYYYMMmatch(String str){
        String format = "(19|20)[0-9]{2}\\-((0[1-9])|(1[0-2]))";
        return Pattern.matches(format,str);
    }

    /**
     * yyyy-MM-dd hh:mm 格式匹配
     * //TODO 未完成
     */
    public static boolean dateYYYYMMDDHHMMmatch(String str) {
        //String format = "(19|20)[0-9]{2}((0[1-9])|(1[0-2]))";
        //return Pattern.matches(format,str);
        return true;
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        int strLenth;
        if (null == str || (strLenth = str.length()) == 0){
            return true;
        }
        for(int i = 0; i < strLenth; i++){
            if(!Character.isWhitespace(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    /**
     * 判断对象是否为空
     */
    public static boolean isEmpty(Object obj){
        if(null == obj){
            return true;
        }
        if(obj instanceof String){
            return isEmpty((String)obj);
        }
        return false;
    }

    /**
     * 字符串转日期格式
     */
    public static Date parseToDate(String date, String format){
        if(date == null){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            throw new ErrorException("时间格式错误，时间格式应为为："+format);
        }
    }

    /**
     * yyyy-MM转换成yyyyMM格式的字符串
     * //TODO 异常处理
     */
    public static String parseToYYYYMM(String oldStr){
        Date date = parseToDate(oldStr, "yyyy-MM");
        return parseToString(date,"yyyyMM");
    }

    /**
     * yyyyMM转换成yyyy-MM格式的字符串
     */
    public static String parseToYYYYMM2(String oldStr){
        Date date = parseToDate(oldStr, "yyyyMM");
        return parseToString(date,"yyyy-MM");
    }

    /**
     *日期转字符串格式
     */
    public static String parseToString(Date date,String format){
        if(date == null){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * Number 转换为 BigDecimal
     */
    public static BigDecimal toBigDec(Number number){
        return new BigDecimal(number.toString());
    }

    /**
     * Number 转换为 BigDecimal
     */
    public static BigDecimal toBigDec(String number){
        return new BigDecimal(number.toString());
    }

    /**
     * 获取任意时间的下一个月,参数格式为"yyyy-MM"
     */
    public static String getNextMonth(String repeatDate) {
        String lastMonth = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM");
        int year = Integer.parseInt(repeatDate.substring(0, 4));
        String monthsString = repeatDate.substring(5, 7);
        int month;
        if ("0".equals(monthsString.substring(0, 1))) {
            month = Integer.parseInt(monthsString.substring(1, 2));
        } else {
            month = Integer.parseInt(monthsString.substring(0, 2));
        }
        cal.set(year,month,Calendar.DATE);
        lastMonth = dft.format(cal.getTime());
        return lastMonth;
    }

    /**
     * 获取任意时间的下一个月（1号）
     */
    public static Date getNextMonth(Date date) {
        String dateStr = parseToString(date,"yyyy-MM");
        String resultStr = getNextMonth(dateStr);
        return parseToDate(resultStr,"yyyy-MM");
    }

    public static Date getLastMonth(Date oldDate) {
        Date nextDate = getNextDate(oldDate, -10);
        return ComUtils.getfirstDayOfMonth(nextDate);
    }

    public static int getDayofCurrentMonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static String moneyFormat(BigDecimal number) {
        if(number == null){
            return "";
        }
        return number.divide(BigDecimal.ONE,2,BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 返回当前月份，格式:yyyyMM
     */
    public static String getDQYF() {
        Date date = getfirstDayOfMonth(new Date());
        return parseToString(date,"yyyyMM");
    }

    public static String filterNull(String str) {
        return str == null ? "" : str;
    }

    public static String getPreMonth(String str1, String format) {
        Date oldDate = ComUtils.parseToDate(str1, format);
        Date newDate = ComUtils.getLastMonth(oldDate);
        return ComUtils.parseToString(newDate,format);
    }

    /**
     * 用于验证后的返回的消息工具类
     */
    public static class FlagMessage {

        public boolean flag;
        public String message;
        FlagMessage(boolean flag, String message) {
            this.flag = flag;
            this.message = message;
        }

        FlagMessage(boolean flag) {
            this.flag = flag;
        }

    }
    public static FlagMessage checkMessage(boolean b, String s) {
        return new FlagMessage(b,s);
    }

    public static FlagMessage checkMessage(boolean b) {
        return new FlagMessage(b);
    }


    /**
     * 金额数字转汉字大写
     * 待转化的金额 851256211捌亿伍仟壹佰贰拾伍万陆仟贰佰壹拾壹元
     * 需重构
     * @return 返回金额的汉字大写
     * @throws Exception
     */
    public static String toMoneyUpper(String mny) {

        BigDecimal bgdec = new BigDecimal(mny);
        if(BigDecimal.ZERO.compareTo(bgdec) == 0){
            return "零元整";
        }
        mny = bgdec.toString();

        //1、查询字符串中是否有'.',看是否有小数
        int index = mny.indexOf('.');
        StringBuilder sb = null;
        if(index == -1){
            sb = IntPartTo(mny);
            sb.append("元整");
        }else{
            String[] split = mny.split("\\.");
            String intPart = split[0];
            String decimalPart = split[1];
            int i = Integer.parseInt(decimalPart);
            if(i==0){
                sb = IntPartTo(intPart);
                sb.append("元整");
            }else{
                sb = IntPartTo(intPart);
                sb.append("元");
                sb.append(decimalPartTo(decimalPart));
            }
        }
        return sb.toString();

    }

    private static String decimalPartTo(String decimalPart) {
        String[] g = {"零","壹","贰","叁","肆","伍","陆","柒","捌","玖"};
        int num = Integer.parseInt(decimalPart);
        if(num == 0){
            return "";
        }
        if(decimalPart.length() == 1){
            return g[num-1] + "角";
        }else{
            int shiWei = num / 10;
            int geWei = num % 10;
            if(geWei == 0){
                return g[num/10] + "角";
            }else{
                if(shiWei == 0){
                    return "零" + g[geWei] + "分";
                }
                return g[shiWei] + "角" + g[geWei] + "分";
            }
        }
    }

    private static StringBuilder IntPartTo(String mny){
        String[] s = {"","万","亿","万亿"};
        StringBuilder sb = new StringBuilder();

        int beginIndex =0,endIndex =0,i = 0;
        for(int length = mny.length();length>0;length-=4){
            beginIndex = length>4 ? length-4 : 0;
            endIndex = length;
            String substr = mny.substring(beginIndex, endIndex);
            String s1 = toMoneyUpperMini(substr);
            if(!"".equals(s1)){
                sb.insert(0,(s[i])).insert(0,s1);
            }
            i++;
            if(substr.startsWith("0") && beginIndex !=0 && !"".equals(s1)) {
                sb.insert(0, "零");
            }
        }
        return sb;
    }


    public static String toMoneyUpper(Number mny) {
        return toMoneyUpper(mny.toString());
    }


    /**
     * 4位及以下的数字转大写，0返回空串
     * 1234  ： 壹仟贰佰叁拾肆元 测试： 1,22,305,6007,6000,6050
     * @throws Exception
     */
    private static String toMoneyUpperMini(int mny) {
        if(mny>9999 && mny <0){
            throw new ErrorException("传入的金额不能大于9999，小于0");
        }
        String[] g = {"零","壹","贰","叁","肆","伍","陆","柒","捌","玖"};
        String[] w = {"","拾","佰","仟"};
        StringBuilder sb = new StringBuilder();
        for(int j=0;mny>0;j++,mny/=10){
            int i = mny % 10;
            if(i==0){
                if( sb.length()>0 && sb.indexOf("零", sb.length()-1) !=sb.length()-1 && mny >=10)
                    sb.append(g[i]);
            }else{
                sb.append(w[j]).append(g[i]);
            }
        }
        return sb.reverse().toString();
    }

    private static String toMoneyUpperMini(String mny){
        return toMoneyUpperMini(Integer.parseInt(mny));
    }

    /**
     * 判断指定年份是否是闰年
     * @return 如果是闰年则为true，否则返回false
     */
    public static boolean isLeapYear(int year) {
        return (year%4==0 && year%100!= 0) || (year %400==0);
    }


    public static int parstPageNo(String pageNo){
        int i;
        try{
            i = Integer.parseInt(pageNo);
        }catch (NumberFormatException e){
            i = 1;
        }
        return i;
    }

    public static int parstPageSize(String pageSIZE) {
        int i;
        try{
            i = Integer.parseInt(pageSIZE);
        }catch (NumberFormatException e){
            i = 10;
        }
        return i;
    }

    public static Date getfirstDayOfMonth(Date date) {
        String s = ComUtils.parseToString(date, "yyyy-MM");
        return ComUtils.parseToDate(s,"yyyy-MM");
    }

    /**
     * 传入一个日期，得到多少天后的日期
     */
    public static Date getNextDate(Date date,int amount){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,amount);
        return calendar.getTime();
    }

    public static String getNextDate(String oldDate,int amount){
        Date date = ComUtils.parseToDate(oldDate, "yyyy-MM-dd");
        Date nextDate = getNextDate(date, amount);
        return ComUtils.parseToString(nextDate,"yyyy-MM-dd");
    }

    /**
     * 第一个值加上第二个值 ：mny1+mny2
     */
    public static String addMoney(String mny1,String mny2){
        AssertUtils.isNumberFormat(mny1);
        AssertUtils.isNumberFormat(mny2);
        BigDecimal dec1 = new BigDecimal(mny1);
        BigDecimal dec2 = new BigDecimal(mny2);
        BigDecimal result = dec1.add(dec2);
        return result.toString();
    }

    /**
     * 第一个值减去第二个值 ：mny1-mny2
     */
    public static String subMoney(String mny1,String mny2){
        AssertUtils.isNumberFormat(mny1);
        AssertUtils.isNumberFormat(mny2);
        BigDecimal dec1 = new BigDecimal(mny1);
        BigDecimal dec2 = new BigDecimal(mny2);
        BigDecimal result = dec1.subtract(dec2);
        return result.toString();
    }

    /**
     * 第一个值乘以第二个值 ：mny1*mny2
     * 注意精度，默认精度相加
     */
    public static String mulMoney(String mny1,String mny2){
        AssertUtils.isNumberFormat(mny1);
        AssertUtils.isNumberFormat(mny2);
        BigDecimal dec1 = new BigDecimal(mny1);
        BigDecimal dec2 = new BigDecimal(mny2);
        BigDecimal result = dec1.multiply(dec2);
        return result.toString();
    }

    /**
     * 第一个值除以第二个值 ：mny1/mny2
     */
    public static String divMoney(String mny1,String mny2){
        AssertUtils.isNumberFormat(mny1);
        AssertUtils.isNumberFormat(mny2);
        BigDecimal dec1 = new BigDecimal(mny1);
        BigDecimal dec2 = new BigDecimal(mny2);
        BigDecimal result = dec1.divide(dec2);
        return moneyFormat(result);
    }

    public static  String roleListToString(TokenContext tokenContext) {
        List<String> roleList = tokenContext.getRoleList();
        StringBuilder sb = new StringBuilder();
        for(String role : roleList){
            sb.append(role).append("|");
        }
        return sb.toString();
    }

    public static List<String> stringSplitToList(String str){
        String[] splits = str.split("|");
        List<String> list =new ArrayList<>();
        for(String role : splits){
            if(!ComUtils.isEmpty(role)){
                list.add(role);
            }
        }
        return list;
    }

}
