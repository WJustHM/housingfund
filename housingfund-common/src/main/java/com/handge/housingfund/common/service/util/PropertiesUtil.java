//package com.handge.housingfund.common.service.util;
//
//import org.apache.commons.configuration.CompositeConfiguration;
//import org.apache.commons.configuration.ConfigurationException;
//import org.apache.commons.configuration.PropertiesConfiguration;
//import org.apache.commons.configuration.XMLConfiguration;
//import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
//
//import java.util.Iterator;
//
///**
// * 配置文件工具类
// * Created by gxy on 17-7-4.
// */
//
///**
// * @deprecated   changed by wangxuefei .
// * {@link com.handge.housingfund.common.configure.Configure}
// */
//@Deprecated
//public class PropertiesUtil {
//    public static CompositeConfiguration configuration = new CompositeConfiguration();
//    public static CompositeConfiguration getConfiguration(String fileName) {
//        try {
//            if (fileName.endsWith(".xml")) {
//                XMLConfiguration cfg = new XMLConfiguration();
//                cfg.setReloadingStrategy(new FileChangedReloadingStrategy());
//                cfg.setEncoding("UTF-8");
//                cfg.load(fileName);
//                configuration.addConfiguration(cfg);
//            } else if (fileName.endsWith(".properties")) {
//                PropertiesConfiguration cfg = new PropertiesConfiguration();
//                cfg.setReloadingStrategy(new FileChangedReloadingStrategy());
//                cfg.setEncoding("UTF-8");
//                cfg.load(fileName);
//                configuration.addConfiguration(cfg);
//            }
//        } catch (ConfigurationException e) {
//            e.printStackTrace();
//        }
//
//        return configuration;
//    }
//
//    public static void printConfiguration(String fileName) {
//        getConfiguration(fileName);
//        Iterator<String> it = configuration.getKeys();
//        while(it.hasNext()) {
//            String key = it.next();
//            System.out.println(key + " : " + configuration.getString(key));
//        }
//    }
//
////    public static final String fileName = "bank.properties";
////
////    public static PropertiesConfiguration cfg;
////
////    static {
////        try {
////            cfg = new PropertiesConfiguration("bank.properties");
////            cfg.setEncoding("UTF-8");
////            cfg.setReloadingStrategy(new FileChangedReloadingStrategy());
////        } catch (ConfigurationException e) {
////            e.printStackTrace();
////        }
////    }
////
////    // 读String
////    public static String getString(String key) {
////        return cfg.getString(key);
////    }
////
////    // 读int
////    public static int getInt(String key) {
////        return cfg.getInt(key);
////    }
////
////    // 读boolean
////    public static boolean getBoolean(String key) {
////        return cfg.getBoolean(key);
////    }
////
////    // 读List
////    public static List<?> getList(String key) {
////        return cfg.getList(key);
////    }
////
////    // 读数组
////    public static String[] getArray(String key) {
////        return cfg.getStringArray(key);
////    }
//}
