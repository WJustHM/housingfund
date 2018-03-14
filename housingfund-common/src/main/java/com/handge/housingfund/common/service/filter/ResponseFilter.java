//package com.handge.housingfund.common.service.filter;
//
//
//import javax.ws.rs.container.ContainerRequestContext;
//import javax.ws.rs.container.ContainerResponseContext;
//import javax.ws.rs.container.ContainerResponseFilter;
//import javax.ws.rs.container.PreMatching;
//import javax.ws.rs.ext.Provider;
//import java.io.IOException;
//
///**
// * 自定义request过滤器
// * Created by gxy on 17-7-13.
// */
//@Provider
//@PreMatching
//public class ResponseFilter implements ContainerResponseFilter {
//    @Override
//    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
//
//    }
//
////    @Override
////    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
////        String path = requestContext.getUriInfo().getPath();
////        if (path.equals("/account/login") ||
////                path.equals("/account/loginByCa") ||
////                path.equals("/account/resetPwdSendEmail") ||
////                path.equals("/account/resetPwd"))
////            return;
////        if (responseContext.getStatus() == 200) {
////            String oldtoken = requestContext.getHeaderString("Token");
////            String token = oldtoken;
////            try {
////                TokenData data = JWTUtil.getData(oldtoken);
////                if (data.getExpiresAt().getTime() < (new Date().getTime() + 60 * 1000)) {
////                    token = JWTUtil.getToken(data.getUserid(), data.getType());
////                    RedisUtil.setex("Invalid_" + oldtoken, 11 * 60, "Invalid");
////                }
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////            MultivaluedMap<String, Object> headers = responseContext.getHeaders();
////            headers.add("Token", token);
////        }
////    }
//
//}
