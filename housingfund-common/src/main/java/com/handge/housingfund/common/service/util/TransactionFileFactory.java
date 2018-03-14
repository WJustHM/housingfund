package com.handge.housingfund.common.service.util;

import com.handge.housingfund.common.service.bank.bean.center.BatchResultFileDetail;
import com.handge.housingfund.common.service.bank.bean.center.BatchResultFileSummary;
import com.handge.housingfund.common.service.bank.bean.center.FileList;
import com.handge.housingfund.common.service.bank.bean.transfer.BatchTransInFile;
import com.handge.housingfund.common.service.bank.bean.transfer.BatchTransOutFile;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 完成交易文件内容和对象的相互转换
 * Created by gxy on 17-8-2.
 */
public class TransactionFileFactory {

    /**
     * 对象列表转字符串
     * @param list 对象列表
     * @return String
     */
    public  static <T> String getFileContent(List<T> list) {
        StringBuffer fileContent = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1)
                fileContent.append(list.get(i).toString());
            else
                fileContent.append(list.get(i).toString()).append("\n");
        }

        return fileContent.toString();
    }

    /**
     * 字符串转对象列表
     * @param summary 字符串
     * @param className 要转的对象的类名
     * @return 对象列表
     * @throws Exception
     */
    public static List<Object> getObjFromFile(String summary, String className) throws Exception {
        List<Object> objects = new ArrayList<>();
        String delimiter = "\\|";
        String endChar = "|";
        if (className.equals(BatchTransInFile.class.getName()) || className.equals(BatchTransOutFile.class.getName())) {
            delimiter = "@\\|\\$";
            endChar = "@|$";
        }

        String[] contents = summary.split("\n");
        if (className.equals(BatchResultFileSummary.class.getName()))
            contents = new String[]{contents[0]};
        else if(className.equals(BatchResultFileDetail.class.getName()))
            contents[0] = "";

        for (String content : contents) {
            int index = content.lastIndexOf(endChar);
            if (index == -1) continue;
            String sub = content.substring(0, index);
//            if (className.equals(BatchResultFileDetail.class.getName())) {
//                sub = content;
//            }
            String[] result = sub.split(delimiter,-1);

            Object o = Class.forName(className).newInstance();
            Field[] field = o.getClass().getDeclaredFields();
            Method m;
            // 利用循环语句将对象中的属性值存到对应的key里面（key名必须与属性名相同）
//            if (result.length == field.length - 1) {
                for (int i = 0, j = 0; i < field.length; i++, j++) {
                    // 允许获取私有属性的值
                    field[i].setAccessible(true);
                    // 获取属性的名字
                    String fieldName = field[i].getName();
                    if ("serialVersionUID".equals(fieldName)) {
                        j--;
                        continue;
                    }
                    // 将属性的首字符大写，方便构造get，set方法
                    fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    // 获取属性的类型
                    String type = field[i].getGenericType().toString();

                    try {
                        if ("class java.lang.String".equals(type)) {
                            m = o.getClass().getMethod("set" + fieldName, String.class);
                            m.invoke(o, result[j]);
                        }
                        if (type.contains("BigDecimal")) {
                            m = o.getClass().getMethod("set" + fieldName, BigDecimal.class);
                            m.invoke(o, new BigDecimal(result[j]));
                        }
                    } catch (Exception ignored) {
                    }
                }
                objects.add(o);
//            }
        }

        return objects;
    }

    public static <T> FileList getFileList(String sendSeqNo, List<T> list){
        String name = "BDC_BAT_" + "522400000000000" + sendSeqNo + ".DAT";
        String data = getFileContent(list);
        return new FileList(name, data);
    }
}
