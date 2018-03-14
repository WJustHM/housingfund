//package com.handge.housingfund.common.service.filter;
//
//import org.jboss.resteasy.plugins.interceptors.CorsFilter;
//
//import javax.ws.rs.core.Feature;
//import javax.ws.rs.core.FeatureContext;
//
///**
// * Created by tanyi on 2017/7/13.
// */
//public class CORSFilter implements Feature {
//    public boolean configure(FeatureContext featureContext) {
//        CorsFilter filter = new CorsFilter();
//        filter.getAllowedOrigins().add("*");
//        filter.setAllowedHeaders("Content-Type, x-token, Token," +
//                " Access-Control-Request-Headers," +
//                " Access-Control-Request-Method");
//        filter.setAllowedMethods("GET, POST, DELETE, PUT, PATCH, OPTIONS");
//        filter.setAllowCredentials(true);
//        featureContext.register(filter);
//        return true;
//    }
//}
