package com.handge.housingfund.common.service.util;

import com.handge.housingfund.common.service.loan.model.RepaymentApplyPrepaymentPost;
import com.handge.housingfund.common.service.loan.model.RepaymentApplyPrepaymentPostTQBFHKXX;
import com.handge.housingfund.common.service.loan.model.RepaymentApplyPrepaymentPostTQJQHKXX;
import com.handge.housingfund.common.service.loan.model.RepaymentApplyPrepaymentPostYQHKXX;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Funnyboy on 2017/9/6.
 */
public class ObjectAttributeCheck {

    public static void Check(RepaymentApplyPrepaymentPost body) {
        if (body == null) {
            throw new ErrorException("对象为空");
        }
        //对象验证
        Field[] filed = null;
        Object obj = null;
        if ("9".equals(body.getHKLX())) {
            filed = body.getYQHKXX().getClass().getDeclaredFields();
            obj = body.getYQHKXX();
        } else if ("8".equals(body.getHKLX())) {
            filed = body.getTQBFHKXX().getClass().getDeclaredFields();
            obj = body.getTQBFHKXX();
        } else {
            filed = body.getTQJQHKXX().getClass().getDeclaredFields();
            obj = body.getTQJQHKXX();
        }
        for (Field f : filed) {
            f.setAccessible(true);
            try {
                if ("9".equals(body.getHKLX())) {
                    if (f.get((RepaymentApplyPrepaymentPostYQHKXX) obj) == null) {
                        throw new ErrorException(f + "不能为空,一个都不能为空");
                    }
                    if (f.get((RepaymentApplyPrepaymentPostYQHKXX) obj).toString().equals("")) {
                        throw new ErrorException(f + "不能为空,不能有空字符串");
                    }
                } else if ("8".equals(body.getHKLX())) {
                    if (f.get((RepaymentApplyPrepaymentPostTQBFHKXX) obj) == null) {
                        throw new ErrorException(f + "不能为空,一个都不能为空");
                    }
                    if (f.get((RepaymentApplyPrepaymentPostTQBFHKXX) obj).toString().equals("")) {
                        throw new ErrorException(f + "不能为空,不能有空字符串");
                    }
                } else {
                    if (f.get((RepaymentApplyPrepaymentPostTQJQHKXX) obj) == null) {
                        throw new ErrorException(f + "不能为空,一个都不能为空");
                    }
                    if (f.get((RepaymentApplyPrepaymentPostTQJQHKXX) obj).toString().equals("")) {
                        throw new ErrorException(f + "不能为空,不能有空字符串");
                    }
                }
            } catch (Exception e) {
                throw new ErrorException(e);
            }
        }
    }

    public static <T> void checkObject(T obj) {
        for (Field f : obj.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {//其他基本类型有默认值
                if(f.getName().equals("FSRS")) continue;
                if(f.get(obj) == null || String.valueOf(f.get(obj)).trim().equals("")){ //判断字段是否为空，并且对象属性中的基本都会转为对象类型来判断
                    throw new Exception(f.getName() + "不能为空");
                }
                if (
                        !f.getGenericType().toString().matches(
                                "class java.lang.*") &&
                                !f.getGenericType().toString().matches(
                                        "class java.util.*") &&
                                !f.getGenericType().toString().equals(
                                        "long") &&
                                !f.getGenericType().toString().equals(
                                        "boolean") &&
                                !f.getGenericType().toString().equals(
                                        "int") &&
                                !f.getGenericType().toString().equals(
                                        "short")
                        ) { // 如果type是类类型，则前面包含"class "，后面跟类名

                    checkObject(f.get(obj));
                }
            } catch (Exception e) {
                throw new ErrorException(e);
            }
        }
    }

    public static <T> void checkList(ArrayList<T> body) {
        for (T bodys : body) {
            for (Field f : bodys.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                try {
                    if (f.get(bodys) == null || String.valueOf(f.get(bodys)).trim().equals("")) { //判断字段是否为空，并且对象属性中的基本都会转为对象类型来判断
                        throw new Exception(f.getName() + "不能为空,一个都不能为空");
                    }
                } catch (Exception e) {
                    throw new ErrorException(e);
                }
            }
        }
    }

    public static void checkObjects(HashMap<String,String> obj){
        for (Map.Entry<String, String> entry : obj.entrySet()) {
            try {
               if(!StringUtil.notEmpty(entry.getValue())) throw new ErrorException("空");
            }catch(Exception e){
                throw new ErrorException(entry.getKey()+":不能为空");
            }
        }
    }

    public static void checkDataType(HashMap<String,String> obj){
        for (Map.Entry<String, String> entry : obj.entrySet()) {
            try {
                Double.parseDouble(entry.getValue());
            }catch(Exception e){
                throw new ErrorException(entry.getKey()+"参数格式异常"+entry.getValue());
            }
        }

    }
}
