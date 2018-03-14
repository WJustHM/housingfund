package com.handge.housingfund.collection.utils;

import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.IdcardValidator;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * Created by 凡 on 2017/9/25.
 * 断言工具类，当与断言方法语义不匹配时，抛出异常ErrorException
 */
public final class AssertUtils {

    /**
     *断言：
     * 传入的对象为空
     */
    public static void isEmpty(Object obj,String msg){
        if(obj instanceof String)
            isEmpty((String)obj,msg);
        else
            Assert.isNull(obj, msg);
    }

    /**
     *断言：
     * 传入的字符串为空(空、空串、空白符)
     */
    public static void isEmpty(String str,String msg){
        if(!ComUtils.isEmpty(str)){
            throw new ErrorException(msg);
        }
    }

    /**
     * 断言：
     * 传入的对象非空
     */
    public static void notEmpty(Object obj,String msg){
        if(obj instanceof String)
            notEmpty((String)obj,msg);
        else
            Assert.notNull(obj, msg);
    }

    /**
     * 断言：
     * 传入的对象非空
     */
    public static void notEmpty(Collection obj, String msg){
        Assert.notEmpty(obj, msg);
    }

    /**
     * 断言：
     * 传入的字符串非空(空、空串、空白符)
     */
    public static void notEmpty(String str,String msg){
        Assert.hasText(str,msg);
    }


    /**
     * 断言：
     * 身份证件号码合法
     */
    public static void isIdentification(String zjhm) {
        if(!IdcardValidator.validate18Idcard(zjhm)){
            throw new ErrorException("身份证件号码格式错误！");
        }
    }

    /**
     * 断言：
     * 值为真
     */
    public static void isTrue(boolean equals, String msg) {
        if(!equals){
            throw new ErrorException(msg);
        }
    }

    /**
     * 断言：
     * dateStr的格式为format，
     * format格式包括：XXX..
     */
    public static void checkDateFormat(String dateStr,String format){
        ComUtils.parseToDate(dateStr,format);
    }

    public static void isNumberFormat(String mny1) {

    }

    /**
     * 断言：
     * 传入的数字为正数
     */
    public static void isPositiveNnumber(Number num, String msg) {
        BigDecimal numDec= new BigDecimal(num.toString());
        if(numDec.compareTo(BigDecimal.ZERO) < 0){
            throw new ErrorException(msg);
        }
    }
}
