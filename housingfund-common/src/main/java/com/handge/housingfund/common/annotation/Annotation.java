package com.handge.housingfund.common.annotation;

import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ModelUtils;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public  class Annotation {

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Custom{

        public String name();

        public String regex() default "";
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Mobile{

        public String name();

        public String regex() default "^[1][0-9]{10}$";
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Number{

        public String name();

        public String regex() default "[0-9]*";
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Ratio{

        public String name();

        public String regex() default "[1-9][0-9]*";
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Phone{

        public String name();

        public String regex() default "(^[0][1-9]{2,3}-[0-9]{5,10}$)|(^[1-9]{1}[0-9]{5,8}$)";
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Email{

        public String name();

        public String regex() default "^[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface BankCard{

        public String name();

        public String regex() default "^[0-9]{0,30}$";

    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface GYPhone{

        public String name();

        public String regex() default "^\\d{3}-\\d{8}|\\d{11}$";

    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Postalcode{

        public String name();

        public String regex() default "^[1-9]\\d{5}$";
    }


    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Money{

        public String name();

        public String regex() default "(^[1-9]([0-9]+)?(\\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\\.[0-9]([0-9])?$)";
    }


    private static void checkValid(java.lang.annotation.Annotation annotation, Object value){
        String regex = null;
        String name = null;
        if(annotation.annotationType().getName().equals(Custom.class.getName())){

             regex = ((Custom)annotation).regex();
             name = ((Custom)annotation).name();
        }

        if(annotation.annotationType().getName().equals(Mobile.class.getName())){
            regex = ((Mobile)annotation).regex();
            name = ((Mobile)annotation).name();
        }
        if(annotation.annotationType().getName().equals(Phone.class.getName())){
            regex = ((Phone)annotation).regex();
            name = ((Phone)annotation).name();
        }
        if(annotation.annotationType().getName().equals(Email.class.getName())){
            regex = ((Email)annotation).regex();
            name = ((Email)annotation).name();
        }
        if(annotation.annotationType().getName().equals(BankCard.class.getName())){
            regex = ((BankCard)annotation).regex();
            name = ((BankCard)annotation).name();
        }
        if(annotation.annotationType().getName().equals(Postalcode.class.getName())){
            regex = ((Postalcode)annotation).regex();
            name = ((Postalcode)annotation).name();
        }

        if(annotation.annotationType().getName().equals(Money.class.getName())){
            regex = ((Money)annotation).regex();
            name = ((Money)annotation).name();
        }

        if(annotation.annotationType().getName().equals(GYPhone.class.getName())){
            regex = ((Money)annotation).regex();
            name = ((Money)annotation).name();
        }

        if(StringUtil.notEmpty(regex)&&value instanceof String&&StringUtil.notEmpty((String) value)){

            Pattern pattern = Pattern.compile(regex);

            Matcher matcher = pattern.matcher((String) value);

            if (!matcher.matches()){ throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,name);}
        }
    }

    public static void checkValue(Object[] values){

        for(Object o : values){
            
            ModelUtils.getAllField(o, new ModelUtils.FieldsEnum() {
                @Override
                public void onEnum(Field field, Object Value) {

                    for(java.lang.annotation.Annotation annotation : field.getDeclaredAnnotations()){

                       checkValid(annotation,Value);
                    }
                }
            });
        }
    }
}
