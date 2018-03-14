package com.handge.housingfund.common;

import com.handge.housingfund.common.configure.Configure;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * Created by xuefei_wang on 17-9-8.
 */
public class TestJWTTokenCentral {


    public class A {

        public String str;

        public class B {
            private int i;
        }
    }

    public class C {

        public String str;

        public class D {
            private int i;
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        TestJWTTokenCentral testJWTTokenCentral = new TestJWTTokenCentral();
//        final Class[]  classes = TestJWTTokenCentral.class.getClasses();
//        for (Class c : classes){
//            System.out.println(c);
//        }

//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MONTH,-2);
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        // 月末
//        Date end = new Date(year, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//        // 月首
//        Date start = new Date(year, month, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
//
//        System.out.println(end);
//        System.out.println(start);
//
//
//        List<String> notoken_urls = Arrays.asList("/account/login", "/account/loginByCa",
//                "/account/resetPwdSendEmail", "/account/resetPwd");

        System.out.println(String.format("%s%02d",2017,2));
//        System.out.println(Thread.currentThread().getContextClassLoader().getResource("/").getPath());

        System.getProperty("java.class.path");
        System.out.println(System.getProperty("java.class.path"));

        System.out.println(  new File("").getAbsolutePath());
        Configure.getInstance();



    }
}
