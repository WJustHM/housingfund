package com.handge.housingfund.finance.service;

import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.database.dao.IBaseDAO;
import com.handge.housingfund.database.entities.Common;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/8/30.
 */
@Component
public class UpdateSFSYService<K extends Common, T extends IBaseDAO<K>> implements IUpdateSFSYService<K, T> {

    @Override
    public void updateSFSY(K entity, T DAOClasz, String id) {

        entity = DAOBuilder.instance(DAOClasz).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
            }
        });

        if (entity == null) throw new ErrorException(ReturnEnumeration.Data_MISS, "没有相关信息，请联系管理员");

        Field[] fields = entity.getClass().getDeclaredFields();
        Method m;

        for (Field field : fields) {
            field.setAccessible(true);
            // 获取属性的名字
            String fieldName = field.getName();
            if ("sfsy".equals(fieldName) || "sfysy".equals(fieldName)) {
                // 将属性的首字符大写，方便构造get，set方法
                fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                // 获取属性的类型
                String type = field.getGenericType().toString();

                try {
                    if ("class java.lang.Boolean".equals(type)) {
                        m = entity.getClass().getMethod("set" + fieldName, Boolean.class);
                        m.invoke(entity, Boolean.TRUE);
                    }
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
                break;
            }

            if (fieldName.contains("Extension")) {
//                fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                try {
                    Method mget = entity.getClass().getMethod("get" + fieldName);
                    Object o = mget.invoke(entity);
                    Field[] extensionFields = o.getClass().getDeclaredFields();
                    Method extensionM;

                    for (Field extensionField : extensionFields) {
                        field.setAccessible(true);
                        // 获取属性的名字
                        String extensionFieldName = extensionField.getName();
                        if ("sfysy".equals(extensionFieldName) || "sfsy".equals(extensionFieldName)) {
                            // 将属性的首字符大写，方便构造get，set方法
                            extensionFieldName = extensionFieldName.substring(0, 1).toUpperCase() + extensionFieldName.substring(1);
                            // 获取属性的类型
                            String type = extensionField.getGenericType().toString();
                            try {
                                if ("class java.lang.Boolean".equals(type)) {
                                    extensionM = o.getClass().getMethod("set" + extensionFieldName, Boolean.class);
                                    extensionM.invoke(o, Boolean.TRUE);
                                }
                            } catch (Exception ignored) {
                            }
                            break;
                        }
                    }
                }catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }

        DAOBuilder.instance(DAOClasz).entity(entity).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "更新数据库失败，请联系管理员");
            }
        });
    }
}
