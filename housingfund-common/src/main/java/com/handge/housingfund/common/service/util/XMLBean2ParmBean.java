package com.handge.housingfund.common.service.util;

import com.handge.housingfund.common.service.bank.bean.center.*;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;
import com.handge.housingfund.common.service.bank.bean.head.SysHeadOut;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeOut;
import com.handge.housingfund.common.service.bank.bean.sys.InterfaceCheckOut;
import com.handge.housingfund.common.service.bank.bean.transfer.*;
import com.handge.housingfund.common.service.bank.xmlbean.Field;
import com.handge.housingfund.common.service.bank.xmlbean.FieldList;
import com.handge.housingfund.common.service.bank.xmlbean.FieldXxxx;
import com.handge.housingfund.common.service.bank.xmlbean.Message;
import com.tienon.util.FileFieldConv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将XMLBean转换成参数Bean,用于返回给业务层
 * Created by gxy on 17-6-23.
 */
public class XMLBean2ParmBean {
    private static Logger logger = LogManager.getLogger(XMLBean2ParmBean.class);

    public Object transfer(Message msg, String returnType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Map<String, Object> msgMap = message2Map(msg);
        //存放FieldList
        List<Map<String, Object>> listMap;
        //创建HeadIn对象
        Object headIn = null;
        //创建HeadOut对象
        Object headOut;
        //默认创建CenterHeadOut
        headOut = new CenterHeadOut();
        //创建FieldList对象list
        List<Object> fieldList = new ArrayList<>();

        Object model = null;
        //根据不同的返回值类型创建不同的对象
        switch (returnType) {
            case "CenterInterfaceCheckOut":
                model = new CenterInterfaceCheckOut();
                break;
            case "LoginOut":
                model = new LoginOut();
                break;
            case "LogoutOut":
                model = new LogoutOut();
                break;
            case "SinglePaymentOut":
                model = new SinglePaymentOut();
                break;
            case "SingleCollectionOut":
                model = new SingleCollectionOut();
                break;
            case "BatchPaymentOut":
                model = new BatchPaymentOut();
                break;
            case "BatchCollectionOut":
                model = new BatchCollectionOut();
                break;
            case "LoanDeductionOut":
                model = new LoanDeductionOut();
                break;
            case "LoanCapIntDecOut":
                model = new LoanCapIntDecOut();
                break;
            case "BatchResultQueryOut":
                model = new BatchResultQueryOut();
                break;
            case "BatchResultDownloadOut":
                model = new BatchResultDownloadOut();
                fieldList.add(new FileList());
                listMap = (List<Map<String, Object>>)msgMap.get("FILE_LIST");
                createFieldListObj(fieldList, listMap);
                break;
            case "SingleTransferAccountOut":
                model = new SingleTransferAccountOut();
                break;
            case "TransactionResultQueryOut":
                model = new TransactionResultQueryOut();
                break;
            case "ChgNoQueryOut":
                model = new ChgNoQueryOut();
                listMap = (List<Map<String, Object>>)msgMap.get("ChgNoList");
                int listSize = listMap.size();
                for(int i = 0; i < listSize; i++){
                    ChgNoList chgNoList = new ChgNoList();
                    fieldList.add(chgNoList);
                }
                createFieldListObj(fieldList, listMap);
                break;
            case "AccTransDetailQueryOut":
                model = new AccTransDetailQueryOut();
                fieldList.add(new FileList());
                listMap = (List<Map<String, Object>>)msgMap.get("FILE_LIST");
                createFieldListObj(fieldList, listMap);
                break;
            case "Actived2FixedOut":
                model = new Actived2FixedOut();
                break;
            case "FixedDrawOut":
                model = new FixedDrawOut();
                break;
            case "FixedTransferModeSetOut":
                model = new FixedTransferModeSetOut();
                break;
            case "Actived2NoticeDepositOut":
                model = new Actived2NoticeDepositOut();
                break;
            case "NoticeDepositDrawOut":
                model = new NoticeDepositDrawOut();
                break;
            case "NoticeDepositDrawSetOut":
                model = new NoticeDepositDrawSetOut();
                break;
            case "NoticeDepositDrawCancelOut":
                model = new NoticeDepositDrawCancelOut();
                break;
            case "NoticeDepositDrawQueryOut":
                model = new NoticeDepositDrawQueryOut();
                listMap = (List<Map<String, Object>>)msgMap.get("SUMMARY");
                int bdc121SListSize = listMap.size();
                for(int i = 0; i < bdc121SListSize; i++){
                    BDC121Summary bdc121Summary = new BDC121Summary();
                    fieldList.add(bdc121Summary);
                }
                createFieldListObj(fieldList, listMap);
                break;
            case "FixedAccBalanceQueryOut":
                model = new FixedAccBalanceQueryOut();
                listMap = (List<Map<String, Object>>)msgMap.get("SUMMARY");
                int bdc122SListSize = listMap.size();
                for(int i = 0; i < bdc122SListSize; i++){
                    BDC122Summary bdc122Summary = new BDC122Summary();
                    fieldList.add(bdc122Summary);
                }
                createFieldListObj(fieldList, listMap);
                break;
            case "ActivedAccBalanceQueryOut":
                model = new ActivedAccBalanceQueryOut();
                break;
            case "InterfaceCheckOut":
                headOut = new SysHeadOut();
                model = new InterfaceCheckOut();
                break;
            case "AccChangeNoticeOut":
                headOut = new SysHeadOut();
                model = new AccChangeNoticeOut();
                fieldList.add(new FileList());
                listMap = (List<Map<String, Object>>)msgMap.get("FILE_LIST");
                createFieldListObj(fieldList, listMap);
                break;
            case "SingleTransInApplIn":
                headIn = new CenterHeadIn();
                model = new SingleTransInApplIn();
                break;
            case "SingleTransInApplOut":
                model = new SingleTransInApplOut();
                break;
            case "BatchTransInApplIn":
                headIn = new CenterHeadIn();
                model = new BatchTransInApplIn();
                break;
            case "BatchTransInApplOut":
                model = new BatchTransInApplOut();
                break;
            case "TransInApplCancelIn":
                headIn = new CenterHeadIn();
                model = new TransInApplCancelIn();
                break;
            case "TransInApplCancelOut":
                model = new TransInApplCancelOut();
                break;
            case "ApplScheduleQueryIn":
                headIn = new CenterHeadIn();
                model = new ApplScheduleQueryIn();
                break;
            case "ApplScheduleQueryOut":
                model = new ApplScheduleQueryOut();
                break;
            case "SingleTransOutInfoIn":
                headIn = new CenterHeadIn();
                model = new SingleTransOutInfoIn();
                break;
            case "SingleTransOutInfoOut":
                model = new SingleTransOutInfoOut();
                break;
            case "BatchTransOutInfoIn":
                headIn = new CenterHeadIn();
                model = new BatchTransOutInfoIn();
                break;
            case "BatchTransOutInfoOut":
                model = new BatchTransOutInfoOut();
                break;
            case "CRFCenterCodeQueryOut":
                model = new CRFCenterCodeQueryOut();
                fieldList.add(new FileList());
                listMap = (List<Map<String, Object>>)msgMap.get("FILE_LIST");
                createFieldListObj(fieldList, listMap);
                break;
        }

        if (headIn != null) {
            createHeadObj(headIn, msgMap);
            createObj(model, headIn, fieldList, msgMap);
        } else {
            createHeadObj(headOut, msgMap);
            createObj(model, headOut, fieldList, msgMap);
        }

        return model;
    }

    /**
     * 给Head对象赋值
     * @param model
     * @param msgMap
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    private void createHeadObj(Object model, Map<String, Object> msgMap) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        java.lang.reflect.Field[] field = model.getClass().getDeclaredFields();

        for (int i = 0; i < field.length; i++) {
            // 允许获取私有属性的值
            field[i].setAccessible(true);
            String fieldName = field[i].getName();
            // 忽略序列化ID字段
            if ("serialVersionUID".equals(fieldName)) continue;
            // 将属性的首字符大写，方便构造get，set方法
            fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            // 获取属性的类型
            String subType = field[i].getGenericType().toString();

            // 获取并调用setter方法设置属性值
            Method m;
            if ("class java.lang.String".equals(subType)) { // 如果type是类类型，则前面包含"class "，后面跟类名
                m = model.getClass().getMethod("set" + fieldName, String.class);
                m.invoke(model, msgMap.get(fieldName));
            }
            if ("int".equals(subType)) {
                m = model.getClass().getMethod("set" + fieldName, int.class);
                String value = (String)msgMap.get(fieldName);
                m.invoke(model, Integer.parseInt("".equals(value) ? "0" : value));
            }
            if (subType.contains("BigDecimal")) {
                m = model.getClass().getMethod("set" + fieldName, BigDecimal.class);
                String value = (String)msgMap.get(fieldName);
                m.invoke(model, new BigDecimal("".equals(value) ? "0" : value));
            }
        }
    }

    /**
     * 给field-list对象赋值
     * @param models
     * @param listMap
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    private void createFieldListObj(List<Object> models, List<Map<String, Object>> listMap) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        for (int j = 0; j < models.size(); j++){
            Object model = models.get(j);
            java.lang.reflect.Field[] field = model.getClass().getDeclaredFields();

            for (int i = 0; i < field.length; i++) {
                // 允许获取私有属性的值
                field[i].setAccessible(true);
                String fieldName = field[i].getName();
                // 忽略序列化ID字段
                if ("serialVersionUID".equals(fieldName)) continue;
                // 将属性的首字符大写，方便构造get，set方法
                fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                // 获取属性的类型
                String subType = field[i].getGenericType().toString();

                // 获取并调用setter方法设置属性值
                Method m;
                if ("class java.lang.String".equals(subType)) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    m = model.getClass().getMethod("set" + fieldName, String.class);
                    if ("DATA".equals(fieldName)){
                        m.invoke(model, listMap.get(j).get(fieldName));
                    } else {
                        m.invoke(model, listMap.get(j).get(fieldName));
                    }

                }
                if ("int".equals(subType)) {
                    m = model.getClass().getMethod("set" + fieldName, int.class);
                    String value = (String)listMap.get(j).get(fieldName);
                    m.invoke(model, Integer.parseInt("".equals(value) ? "0" : value));
                }
                if (subType.contains("BigDecimal")) {
                    m = model.getClass().getMethod("set" + fieldName, BigDecimal.class);
                    String value = (String)listMap.get(j).get(fieldName);
                    m.invoke(model, new BigDecimal("".equals(value) ? "0" : value));
                }
            }
        }
    }

    /**
     * 给返回给业务系统的对象赋值
     * @param model
     * @param head
     * @param fieldList
     * @param msgMap
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void createObj(Object model, Object head, List<Object> fieldList, Map<String, Object> msgMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
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

            // 获取并调用setter方法设置属性值
            Method m;
            if (type.contains("CenterHeadOut")) {//设置Head信息
                m = model.getClass().getMethod("set" + fieldName, CenterHeadOut.class);
                m.invoke(model, (CenterHeadOut)head);
            } else if (type.contains("SysHeadOut")) {
                m = model.getClass().getMethod("set" + fieldName, SysHeadOut.class);
                m.invoke(model, (SysHeadOut)head);
            } else if (type.contains("CenterHeadIn")) {
                m = model.getClass().getMethod("set" + fieldName, CenterHeadIn.class);
                m.invoke(model, (CenterHeadIn)head);
            } else if (type.contains("FileList")) { //设置FileList信息
                m = model.getClass().getMethod("set" + fieldName, FileList.class);
                m.invoke(model, (FileList)fieldList.get(0));
            } else if (type.contains("ChgNoList")) {
                m = model.getClass().getMethod("set" + fieldName, List.class);
                m.invoke(model, fieldList);
            } else if (type.contains("Summary")) {
                m = model.getClass().getMethod("set" + fieldName, List.class);
                m.invoke(model, fieldList);
            } else {
                if ("class java.lang.String".equals(type)) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    m = model.getClass().getMethod("set" + fieldName, String.class);
                    m.invoke(model, msgMap.get(fieldName));
                }
                if ("int".equals(type)) {
                    m = model.getClass().getMethod("set" + fieldName, int.class);
                    String value = (String)msgMap.get(fieldName);
                    m.invoke(model, Integer.parseInt("".equals(value) ? "0" : value));
                }
                if (type.contains("BigDecimal")) {
                    m = model.getClass().getMethod("set" + fieldName, BigDecimal.class);
                    String value = (String)msgMap.get(fieldName);
                    m.invoke(model, new BigDecimal("".equals(value) ? "0" : value));
                }
            }
        }
    }

    /**
     * 将Message对象中的值存入Map
     * @param msg
     * @return Map
     */
    private Map<String, Object> message2Map(Message msg) throws IOException {
        Map<String, Object> map = new HashMap<>();
        //获取head信息
        List<Field> headList = msg.getHead().getField();
        for (Field field : headList) {
            map.put(field.getName(), field.getvalue());
        }
        //获取body的field信息
        if (msg.getBody() != null) {
            List<Field> bodyList = msg.getBody().getField();
            for (Field field : bodyList) {
                map.put(field.getName(), field.getvalue());
            }
            //获取body的fieldlist的信息
            List<FieldList> fieldLists = msg.getBody().getFieldList();
            for (FieldList fieldList : fieldLists) {
                String fieldListName = fieldList.getName();
                List<Object> subFieldLists = fieldList.getFieldListOrFieldXxxxOrField();
                //子fieldList
                List<Map<String, Object>> list = new ArrayList<>();
                for (Object subFieldList : subFieldLists) {
                    boolean b = subFieldList instanceof FieldList;
                    if (b) {
                        List<Object> subFields = ((FieldList) subFieldList).getFieldListOrFieldXxxxOrField();
                        //子Field
                        Map<String, Object> subMap = new HashMap<>();
                        for (Object subField : subFields) {
                            String subFieldName = ((Field) subField).getName();
                            String subFieldValue = ((Field) subField).getvalue();
                            if ("DATA".equals(subFieldName))
                                subFieldValue = (subFieldValue != null && !"".equals(subFieldValue)) ? FileFieldConv.fieldBCDtoASC(subFieldValue, "GBK") : "";
                            subMap.put(subFieldName, subFieldValue);
                        }
                        list.add(subMap);
                    } else {
                        List<Field> subFields = ((FieldXxxx) subFieldList).getField();
                        //子Field
                        Map<String, Object> subMap = new HashMap<>();
                        for (Field subField : subFields) {
                            String subFieldName = subField.getName();
                            String subFieldValue = subField.getvalue();
                            if ("DATA".equals(subFieldName))
                                subFieldValue = (subFieldValue != null && !"".equals(subFieldValue)) ? FileFieldConv.fieldBCDtoASC(subFieldValue, "GBK") : "";
                            subMap.put(subFieldName, subFieldValue);
                        }
                        list.add(subMap);
                    }
                }
                map.put(fieldListName, list);
            }
        }

        logger.info("住建部报文转成Map结构: \n" + map);
        return map;
    }
}
