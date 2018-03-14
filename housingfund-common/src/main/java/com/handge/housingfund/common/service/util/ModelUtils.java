package com.handge.housingfund.common.service.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.github.benas.randombeans.api.Randomizer;
import io.github.benas.randombeans.randomizers.registry.CustomRandomizerRegistry;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings({"unused", "unchecked", "Duplicates", "Convert2Lambda"})
public class ModelUtils {

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

                                ModelUtils.copyProperties(value,o);

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
                                                    ModelUtils.copyProperties(o1,element);
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
            T instance = tClass.newInstance();

            ModelUtils.copyProperties(object,instance);

            return instance;
        
        } catch (InstantiationException | IllegalAccessException e) {
            
            e.printStackTrace();
        }

        return null;
    }

    public static <T> Class<T> realClass(Object object){
        if(object == null){return  null;}

        Class realClass = object.getClass();

        while (realClass.getName().contains("$")){
            realClass = realClass.getSuperclass();
        }

        return realClass;
    }

    public static Object autoNoneAdductionValue(Object object){

        if(object == null){return  null;}

        Class realClass = object.getClass();

        while (realClass.getName().contains("$")){
            realClass = realClass.getSuperclass();
        }
        return ModelUtils.noneAdductionValue(realClass,object);
    }
    
    public static boolean hasNullValue(Object object){

        return false;
    }

    public interface ChechValidationHandler{

        public boolean isValidate(String propertyName,Object value);
    }

    public static boolean checkValidation(Object target,ChechValidationHandler handler){

        if(target == null){

            return handler.isValidate(null,null);
        }

        Class<?> editable = null;

        String[] ignoreProperties = null;

        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();

        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);

        for (PropertyDescriptor targetPd : targetPds) {


            Method readMethod = targetPd.getReadMethod();

            Object value = null;

            try {

                value = readMethod.invoke(target);

            } catch (IllegalAccessException | InvocationTargetException e) {


                e.printStackTrace();

                continue;
            }

            if(!BeanUtils.isSimpleProperty(targetPd.getPropertyType())){

                if(value==null){

                   if(!handler.isValidate(targetPd.getName(),null)){ return false;}

                }else {


                    if(!checkValidation(value,handler)){return false;}
                }

            }else {

                if(!handler.isValidate(targetPd.getName(),value)){ return  false;}
            }
        }

        return true;
    }

    public static void checkValidation(Object source,List<String>checkValues,ChechValidationHandler handler){


        ModelUtils.checkValidation(source, new ChechValidationHandler() {

            @Override
            public boolean isValidate(String propertyName, Object value) {

                return !CollectionUtils.flatmap(checkValues, new CollectionUtils.Transformer<String, String>() {

                    @Override
                    public String tansform(String var1) {

                        return var1.toUpperCase();
                    }

                }).contains(propertyName.toUpperCase()) || handler.isValidate(propertyName, value);

            }
        });
    }

    public static <T> T randomModel(Class<T> type){

        return  randomModel(type, new HashMap<>());
    }

    public static <T> T randomModel(Class<T> type, HashMap<String,Object>map){

        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().collectionSizeRange(1,30).registerRandomizerRegistry(new CustomRandomizerRegistry(){

            @Override
            public Randomizer<?> getRandomizer(Field field) {

                Class type = field.getType();

                String name = field.getName();

                if(!type.equals(String.class)){return null;}

                EnhancedRandom enhancedRandom1 = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();

                if(name.endsWith("E")||name.endsWith("JS")){ return new Randomizer<Object>() {
                    @Override
                    public Object getRandomValue() { return enhancedRandom1.nextFloat()+""; }
                }; }

                if(name.endsWith("DM")||name.endsWith("HM")||name.endsWith("NY")||name.endsWith("Count")||name.endsWith("Size")||name.endsWith("No")||name.endsWith("Page")){ return new Randomizer<Object>() {
                    @Override
                    public Object getRandomValue() { return Math.abs(enhancedRandom1.nextInt())+"";}
                };}

                if(name.endsWith("NY")){ return new Randomizer<Object>() {

                    @Override
                    public Object getRandomValue() { return new SimpleDateFormat("yyyy-MM").format(enhancedRandom1.nextObject(Date.class)); }

                };}

                if(name.endsWith("RQ")){ return new Randomizer<Object>() {

                    @Override
                    public Object getRandomValue() { return new SimpleDateFormat("yyyy-MM-dd").format(enhancedRandom1.nextObject(Date.class)); }

                };}

                if(map == null){return null;}

                for (Map.Entry<String, Object> entry : map.entrySet()) {

                    if(name.equals(entry.getKey())){

                        return new Randomizer<Object>() {

                            @Override
                            public Object getRandomValue() { return entry.getValue(); }
                        };
                    }
                }

                return null;

            }
        }).build();

        return  enhancedRandom.nextObject(type);
    }

    public static <T> void randomJson(Class<T> type){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(ModelUtils.randomModel(type), System.out);
        System.out.println("");

    }

    public interface FieldsEnum{

        public void onEnum(Field field, Object Value);
    }

    public static void getAllField(Object object,FieldsEnum fieldsEnum) {

        if (object == null) { return; }

        Class currentClass = object.getClass();

        for(Field field : currentClass.getDeclaredFields()) {
           Object value =null;
            try {
                field.setAccessible(true);
                value = field.get(object);
            } catch (IllegalAccessException e) { e.printStackTrace(); }

            fieldsEnum.onEnum(field,value);

            if(!BeanUtils.isSimpleProperty(field.getDeclaringClass())){

                getAllField(value,fieldsEnum);
            }
        }
    }

}
