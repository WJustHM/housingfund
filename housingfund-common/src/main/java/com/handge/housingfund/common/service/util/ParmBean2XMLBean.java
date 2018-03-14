package com.handge.housingfund.common.service.util;

import com.handge.housingfund.common.service.bank.bean.center.FileList;
import com.handge.housingfund.common.service.bank.xmlbean.*;
import com.handge.housingfund.common.service.bank.xmlbean.Message;
import com.tienon.util.FileFieldConv;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * 将参数Bean转换成XMLbean,用于JAXB对象到xml的转化
 * Created by gxy on 17-6-20.
 */
public class ParmBean2XMLBean {
    private Message message = new Message();
    private Head head = new Head();
    private Body body = new Body();
    private FieldList fieldList = new FieldList();

    public Message transfer(Object model) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException {
        java.lang.reflect.Field[] field = model.getClass().getDeclaredFields();
        // 利用循环语句将对象中的属性值存到对应的key里面（key名必须与属性名相同）
        for (int i = 0; i < field.length; i++) {
            // 允许获取私有属性的值
            field[i].setAccessible(true);
            // 获取属性的名字
            String fieldName = field[i].getName();
            // 忽略序列化ID字段
            if ("serialVersionUID".equals(fieldName)) continue;
            // 将属性的首字符大写，方便构造get，set方法
            fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            // 获取属性的类型
            String type = field[i].getGenericType().toString();
            Method m = model.getClass().getMethod("get" + fieldName);
            // 获取头部信息
            if (type.contains("CenterHeadIn") || type.contains("SysHeadIn") || type.contains("CenterHeadOut")) {
                Object fieldValue = m.invoke(model);
                java.lang.reflect.Field[] chiField = fieldValue.getClass().getDeclaredFields();
                for (int j = 0; j < chiField.length; j++) {
                    // 允许获取私有属性的值
                    chiField[j].setAccessible(true);
                    String chiFieldName = chiField[j].getName();
                    // 忽略序列化ID字段
                    if ("serialVersionUID".equals(chiFieldName)) continue;
                    String chiFieldValue = (String)chiField[j].get(fieldValue);

                    Field headField = new Field();
                    headField.setName(chiFieldName);
                    headField.setvalue(chiFieldValue);
                    head.getField().add(headField);
//                    System.out.println(chiFieldName + " : " + chiFieldValue);
                }
            } else if (type.contains("FileList")) { //获取FileList信息
                FileList fieldValue = (FileList) m.invoke(model);
                java.lang.reflect.Field[] flField = fieldValue.getClass().getDeclaredFields();
                fieldList.setName("FILE_LIST");
                FieldList subFieldList1 = new FieldList();
                subFieldList1.setName("0");
                for (int j = 0; j < flField.length; j++) {
                    // 允许获取私有属性的值
                    flField[j].setAccessible(true);
                    String flFieldName = flField[j].getName();
                    // 忽略序列化ID字段
                    if ("serialVersionUID".equals(flFieldName)) continue;
                    String flFieldValue = (String)flField[j].get(fieldValue);

                    Field fileField = new Field();
                    fileField.setName(flFieldName);
                    //对文件进行压缩编码
                    if ("DATA".equals(flFieldName)) {
                        fileField.setvalue(FileFieldConv.fieldASCtoBCD(flFieldValue,"GBK"));
                    } else {
                        fileField.setvalue(flFieldValue);
                    }
                    subFieldList1.getFieldListOrFieldXxxxOrField().add(fileField);
//                    System.out.println(flFieldName + " : " + flFieldValue);
                }
                fieldList.getFieldListOrFieldXxxxOrField().add(subFieldList1);
                body.getFieldList().add(fieldList);
            } else {
                String fieldValue = "";
                if ("class java.lang.String".equals(type)) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    fieldValue = (String) m.invoke(model); // 调用getter方法获取属性值
                }
                if ("int".equals(type)) {
                    int value = (int) m.invoke(model);
                    fieldValue = String.valueOf(value);
                }
                if (type.contains("BigDecimal")) {
                    BigDecimal value = (BigDecimal) m.invoke(model);
                    fieldValue = null == value ? "" : value.toString();
                }

                Field bodyField = new Field();
                bodyField.setName(fieldName);
                bodyField.setvalue(fieldValue);
                body.getField().add(bodyField);
//                System.out.println(fieldName + " : " + fieldValue);
            }
        }
        message.setHead(head);
        message.setBody(body);

        return message;
    }
}
