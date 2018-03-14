package com.handge.housingfund.common.service.util;

import com.handge.housingfund.common.service.collection.model.individual.ImportExcelErrorListRes;
import com.handge.housingfund.common.service.collection.model.individual.ImportExcelRes;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sjw on 2017/11/9.
 */
public class ExcelUtil {

    public static ImportExcelRes ReturnNotice(String Name, String Mes){
        ArrayList arrayList= new ArrayList();
        ImportExcelRes importExcelRes = new ImportExcelRes();
        ImportExcelErrorListRes excelErrorListRes = new ImportExcelErrorListRes();
        excelErrorListRes =  ReturnExcelErrorlist(Name,Mes);
        arrayList.add(excelErrorListRes);
        importExcelRes.setImportExcelErrorListRes(arrayList);
        importExcelRes.setSuccess_num("0");
        importExcelRes.setFail_num("1");
        return importExcelRes;
    }

    public static ImportExcelErrorListRes ReturnExcelErrorlist(String Name ,String Mes){
        ImportExcelErrorListRes excelErrorListRes = new ImportExcelErrorListRes();
        excelErrorListRes.setName(Name);
        excelErrorListRes.setMes(Mes);
        if(Mes.equals("正在导入")){
            excelErrorListRes.setStatus("doing");
        }else{
            excelErrorListRes.setStatus("fail");
        }
        return excelErrorListRes;
    }
    /**
     * 根据身份证的号码算出当前身份证持有者的性别和出生年月 18位身份证
     *
     * @return
     * @throws Exception
     */
    public static Map<String, String> getCarInfo18W(String CardCode) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            String year = CardCode.substring(6).substring(0, 4);// 得到年份
            String yue = CardCode.substring(10).substring(0, 2);// 得到月份
            String sexcode;
            String CSNY;
            if (Integer.parseInt(CardCode.substring(16).substring(0, 1)) % 2 == 0) {// 判断性别
                sexcode = "2";//女
            } else {
                sexcode = "1";//男
            }
            CSNY = year + "-" + yue;
            map.put("sexcode", sexcode);
            map.put("CSNY", CSNY);
            return map;
        }catch (Exception e){
            throw new ErrorException(e);
        }
    }

    /**
     * 15位身份证的验证
     *
     * @param
     * @throws Exception
     */
    public static Map<String, String> getCarInfo15W(String card) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            String uyear = "19" + card.substring(6, 8);// 年份
            String uyue = card.substring(8, 10);// 月份
            String usex = card.substring(14, 15);// 用户的性别
            String sexcode;
            String CSNY;
            if (Integer.parseInt(usex) % 2 == 0) {
                sexcode = "2";//女
            } else {
                sexcode = "1";//男
            }
            CSNY = uyear+"-"+uyue;
            map.put("sexcode", sexcode);
            map.put("CSNY", CSNY);
            return map;
        }catch (Exception e){
            throw new ErrorException(e);
        }
    }

    public static Map<String, String> getCarInfo(String card){
        Map<String, String> map = new HashMap<String, String>();
        if(card.length()==15){
            map =  getCarInfo15W(card);
        }
        if(card.length()==18){
            map =  getCarInfo18W(card);
        }
        return map;
    }
    /***
     * 将汉字转成拼音(取首字母或全拼)
     * @param hanzi
     * @param full 是否全拼
     * @return
     */
    public static String convertHanzi2Pinyin(String hanzi,boolean full)
    {
        /***
         * ^[\u2E80-\u9FFF]+$ 匹配所有东亚区的语言
         * ^[\u4E00-\u9FFF]+$ 匹配简体和繁体
         * ^[\u4E00-\u9FA5]+$ 匹配简体
         */
        String regExp="^[\u4E00-\u9FFF]+$";
        StringBuffer sb=new StringBuffer();
        if(hanzi==null||"".equals(hanzi.trim()))
        {
            return "";
        }
        String pinyin="";
        for(int i=0;i<hanzi.length();i++)
        {
            char unit=hanzi.charAt(i);
            if(match(String.valueOf(unit),regExp))//是汉字，则转拼音
            {
                pinyin=convertSingleHanzi2Pinyin(unit);
                if(full)
                {
                    sb.append(pinyin);
                }
                else
                {
                    sb.append(pinyin.charAt(0));
                }
            }
            else
            {
                sb.append(unit);
            }
        }
        return sb.toString();
    }

    /***
     * 将单个汉字转成拼音
     * @param hanzi
     * @return
     */
    private static String convertSingleHanzi2Pinyin(char hanzi)
    {
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        String[] res;
        StringBuffer sb=new StringBuffer();
        try {
            res = PinyinHelper.toHanyuPinyinStringArray(hanzi,outputFormat);
            sb.append(res[0]);//对于多音字，只用第一个拼音
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return sb.toString();
    }

    /***
     * @param str 源字符串
     * @param regex 正则表达式
     * @return 是否匹配
     */
    public static boolean match(String str,String regex)
    {
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(str);
        return matcher.find();
    }
}
