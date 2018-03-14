package com.handge.housingfund.common.service.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

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

            if(object instanceof ArrayList){

                T instance = tClass.newInstance();

                for(Object element : (ArrayList)object){

                     ((ArrayList)instance).add(ResUtils.autoNoneAdductionValue(element));
                }
                obj = instance;

            } else if(object instanceof List){

                T instance = (T) new ArrayList();

                for(Object element : (ArrayList)object){

                    ((ArrayList)instance).add(ResUtils.autoNoneAdductionValue(element));
                }
                obj = instance;
//                obj =  (T) CollectionUtils.flatmap((List<Object>) object, new CollectionUtils.Transformer<Object, Object>() {
//                    @Override
//                    public Object tansform(Object var1) { return ResUtils.autoNoneAdductionValue(var1); }
//                });


            }else {


                T instance = tClass.newInstance();
                ResUtils.copyProperties(object,instance);
                obj = instance;
            }

            return obj;
        } catch (InstantiationException | IllegalAccessException e) {
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
