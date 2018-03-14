package com.handge.housingfund.server.controllers;

import com.handge.housingfund.common.service.enumeration.ErrorEnumeration;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.util.CollectionUtils;
import com.handge.housingfund.common.service.util.ResponseWrapper;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import org.apache.logging.log4j.core.util.Throwables;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import javax.ws.rs.core.Response;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/*
 * Created by lian on 2017/7/12.贷款用
 */
@SuppressWarnings({"WeakerAccess", "unused", "SingleStatementInBlock", "ConstantConditions", "Duplicates", "unchecked"})
public abstract class ResUtils {

    public static Response wrapEntityIfNeeded(Response response) {

        if (!response.hasEntity()) {
            return response;
        }

        ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>(response.getEntity());

        try {

            Class currentclass = response.getEntity().getClass();

//            System.out.println("======================>" + response.getEntity());

            while (true) {

                boolean shouldBreak = false;

                for (Field field : currentclass.getDeclaredFields()) {


                    if (field.getName().contains("$")) {

                        currentclass = currentclass.getSuperclass();

                        break;

                    } else {


                        for (Field realField : currentclass.getDeclaredFields()) {

                            if (realField.getName().equals("Res") && realField.getType() == ArrayList.class) {

                                realField.setAccessible(true);

                                Object o = realField.get(response.getEntity());

                                wrapper = new ResponseWrapper<Object>(o);
                            }
                        }
                    }

                    shouldBreak = true;
                }

                if (currentclass.getDeclaredFields().length == 0) {

                    shouldBreak = true;
                }

                if (shouldBreak) {
                    break;
                }
            }


        } catch (Exception e) {

            e.printStackTrace();
        }

        Field responseField = null;

        try {

            Field[] fields = Class.forName(response.getClass().getName()).getDeclaredFields();

            for (Field field : fields) {

                if (field.getName().equals("entity")) {

                    responseField = field;
                }

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        if (responseField == null) {

            return response;
        }


        try {

            responseField.setAccessible(true);

            responseField.set(response, wrapper);

        } catch (IllegalAccessException e) {

            return response;
        }

        return response;
    }

    private static void copyProperties(Object source, Object target) throws BeansException {
        Class<?> editable = null;

        String[] ignoreProperties = null;

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
                        "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

        List<String> needsConvert = new ArrayList<>();

//        if(source instanceof Collection&&target instanceof Collection){
//
//            ((Collection) source).forEach(new Consumer() {
//                @Override
//                public void accept(Object o) {
//
//                    try {
//
//                        Class realClass = o.getClass();
//
//                        while (realClass.getName().contains("$")){
//                            realClass = realClass.getSuperclass();
//                        }
//                        Object element = realClass.newInstance();
//                        if(o==null||BeanUtils.isSimpleProperty(o.getClass())){
//                            element = o;
//                        }else {
//                            ResUtils.copyProperties(o,element);
//                        }
//                        ((Collection) target).add(element);
//                    } catch (InstantiationException e) {
//                        e.printStackTrace();
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    };
//                }
//            });
//        }
        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();

                    if (readMethod != null && ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {



                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);



                            if(value!=null&&(targetPd.getPropertyType().getName().contains(target.getClass().getName())||!BeanUtils.isSimpleProperty(targetPd.getPropertyType()))){


                                Object o = targetPd.getPropertyType().newInstance();

                                if(targetPd.getPropertyType().getName().equals(Object.class.getName())&&value!=null){

                                    Class realClass = value.getClass();

                                    while (realClass.getName().contains("$")){
                                        realClass = realClass.getSuperclass();
                                    }

                                    o = realClass.newInstance();
                                }

                                ResUtils.copyProperties(value,o);

                                writeMethod.invoke(target, o);

                                needsConvert.add(targetPd.getPropertyType().getName());

                                if(value instanceof Collection &&value!=null){//&&((Collection)value).size()!=0){

                                    Object finalO = o;
                                    ((Collection)value).forEach(new Consumer() {
                                        @Override
                                        public void accept(Object o1) {

                                            try {

                                                Class realClass = o1.getClass();

                                                while (realClass.getName().contains("$")){
                                                    realClass = realClass.getSuperclass();
                                                }
                                                Object element = realClass.newInstance();
                                                if(o1==null||BeanUtils.isSimpleProperty(o1.getClass())){
                                                    element = o1;
                                                }else {
                                                    ResUtils.copyProperties(o1,element);
                                                }
                                                ((Collection) finalO).add(element);
                                            } catch (InstantiationException e) {
                                                e.printStackTrace();
                                            } catch (IllegalAccessException e) {
                                                e.printStackTrace();
                                            };

                                        }
                                    });
                                }
                            }else if("null".equals(value)){

                                writeMethod.invoke(target, "");

                            }else if(targetPd.getPropertyType().getName().equals(String.class.getName())&&value == null){

                                writeMethod.invoke(target,"");
                            }
                        }
                        catch (Throwable ex) {
                            throw new FatalBeanException(

                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }

    public static <T> T noneAdductionValue(Class<T> tClass ,Object object){

        try {

            T obj = null;

            if(object instanceof List){

                obj = (T) CollectionUtils.flatmap((List<Object>) object, new CollectionUtils.Transformer<Object, Object>() {
                    @Override
                    public Object tansform(Object var1) {

                        return ResUtils.autoNoneAdductionValue(var1);
                    }
                });

            }else {


                T instance = tClass.newInstance();
                ResUtils.copyProperties(object,instance);
                obj = instance;
            }

            return obj;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object autoNoneAdductionValue(Object object){

        if(object == null){return  null;}

        Class realClass = object.getClass();

        while (realClass.getName().contains("$")){
            realClass = realClass.getSuperclass();
        }
        return ResUtils.noneAdductionValue(realClass,object);
    }

    public static Response buildParametersErrorResponse() {

        return buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "请检查参数");
    }

    public static Response buildParametersMissErrorResponse(){
        return buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "请检查参数");
    }

    public static Response buildParametersFormatErrorResponse() {

        return buildCommonErrorResponse(ReturnEnumeration.Parameter_NOT_MATCH, "请检查参数格式");

    }

    public static Response buildExceptionErrorResponse(Exception exception) {

        if (exception != null) {

            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        Throwable lastException = Throwables.getRootCause(exception);

        return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Program_UNKNOW_ERROR, lastException.getMessage());
//        return Response.status(200).entity(new Error(){{
//
//
//
//            this.setCode(lastException.hashCode() + "");
//
//            this.setMsg(lastException.getMessage());
//
//        }}).build();
    }

    public static Response buildExceptionErrorResponse(ArrayList<Exception> exceptions) {

        return exceptions.size() == 0 ? Response.status(200).entity(null).build() : ResUtils.buildExceptionErrorResponse(exceptions.get(0));
    }

    public static Response buildCommonErrorResponse(ErrorEnumeration errorEnumeration, String content, boolean rollback) {

        if (rollback) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return Response.status(200).entity(new Error() {{

            this.setCode(errorEnumeration.getCode() + "");

            this.setMsg(errorEnumeration.getMessage() + content);

        }}).build();

    }

    public static Response buildCommonErrorResponse(ReturnEnumeration errorEnumeration, boolean rollback) {

        if (rollback) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return Response.status(200).entity(new Error() {{

            this.setCode(errorEnumeration.getCode() + "");

            this.setMsg(errorEnumeration.getMessage() + "");

        }}).build();

    }

    public static Response buildCommonErrorResponse(ReturnEnumeration errorEnumeration, String content) {


        return Response.status(200).entity(new Error() {
            {

                this.setCode(errorEnumeration.getCode() + "");

                this.setMsg(errorEnumeration.getMessage() + content);

            }
        }).build();

    }

    public static Response buildCommonErrorResponse(ErrorEnumeration errorEnumeration) {

        //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        //TransactionAspectSupport.currentTransactionStatus().isNewTransaction()
        return Response.status(200).entity(new Error() {{

            this.setCode(errorEnumeration.getCode() + "");

            this.setMsg(errorEnumeration.getMessage() + "");

        }}).build();

    }

    public static Response buildCommonResponse(Exception exception, Object entity) {

        return exception == null ? Response.status(200).entity(entity).build() : ResUtils.buildExceptionErrorResponse(exception);
    }

    public static Response buildCommonResponse(ArrayList<Exception> exceptions, Object entity) {

        return exceptions.size() == 0 ? Response.status(200).entity(entity).build() : ResUtils.buildExceptionErrorResponse(exceptions.get(0));
    }

    /**
     * 判断对象的属性是否全不为空
     * @param obj
     * @return
     */
    public static Boolean isAllNotNull(Object obj){
        for (Field f : obj.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {
                if (f.get(obj) == null) { //判断字段是否为空，并且对象属性中的基本都会转为对象类型来判断
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
