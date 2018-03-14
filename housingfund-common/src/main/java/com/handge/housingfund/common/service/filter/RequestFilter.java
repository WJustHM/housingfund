//package com.handge.housingfund.common.service.filter;
//
//
//import com.handge.housingfund.common.service.account.model.TokenData;
//import com.handge.housingfund.common.service.util.JWTUtil;
//import com.handge.housingfund.common.service.util.PropertiesUtil;
//import com.handge.housingfund.common.service.util.RedisUtil;
//import org.apache.commons.configuration.CompositeConfiguration;
//
//import javax.ws.rs.container.ContainerRequestContext;
//import javax.ws.rs.container.ContainerRequestFilter;
//import javax.ws.rs.container.PreMatching;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.ext.Provider;
//import java.io.IOException;
//import java.util.Set;
//
///**
// * 自定义request过滤器
// * Created by gxy on 17-7-11.
// */
//@Provider
//@PreMatching
//public abstract class RequestFilter implements ContainerRequestFilter {
//
//    static CompositeConfiguration config = PropertiesUtil.getConfiguration("token.properties");
//    static int expires = config.getInt("expirestime", 15);
//
//    @Override
//    public void filter(ContainerRequestContext requestContext) throws IOException {
//
//
//        Response.ResponseBuilder responseBuilder = Response.accepted();
//        String path = requestContext.getUriInfo().getPath();
//        String method = requestContext.getRequest().getMethod();
//        System.out.println("method: " + method);
//        System.out.println("path: " + path);
//        if (path.equals("/account/login") ||
//                path.equals("/account/loginByCa") ||
//                path.equals("/account/resetPwdSendEmail") ||
//                path.equals("/account/resetPwd"))
//            return;
//        String token = requestContext.getHeaderString("Token");
//        //判断是否有Token
//        if (token == null) {
//            requestContext.abortWith(responseBuilder.status(Response.Status.FORBIDDEN)
//                    .entity("No Token, Plase login")
//                    .build());
//        } else {
//            try {
//                if (!RedisUtil.exists("Valid_" + token)) {
//                    requestContext.abortWith(responseBuilder.status(402)
//                            .entity("Token Expires, Plase re-login")
//                            .build());
//                    return;
//                }
//                RedisUtil.expire("Valid_" + token, expires * 60);
//                TokenData tokenData = JWTUtil.getData(token);
//                String userId = tokenData.getUserid();
//                int type = tokenData.getType();
//                //判断Token是否有效
//                if (tokenData.getCode() == 1) {
//                    if (tokenData.getType() == 0) {
//                        return;
//                    }
//                    //判断redis中是否有该用户的信息,没有则从数据库中取
//                    if (!RedisUtil.exists("Permisssion_" + userId + "_type_" + type)) {
//                        // 从数据库取用户-权限信息,并同时存入redis,方便下次读取
//                        boolean flag = getPermissionByUserId(userId, type, path, method);
//
//                        //判断是否有权限
//                        if (!flag) {
//                            requestContext.abortWith(responseBuilder.status(403)
//                                    .entity("Access denied for this resource")
//                                    .build());
//                            return;
//                        }
//                        System.out.println("from DB: through");
//                    } else {
//                        //判断是否有权限
//                        boolean isPermission = false;
//                        Set<String> roles = RedisUtil.smembers("Permisssion_" + userId + "_type_" + type);
//                        for (String role : roles) {
//                            if (RedisUtil.sismember("Permisssion_" + role, method + ":" + path)) {
//                                isPermission = true;
//                                break;
//                            }
//                        }
//                        if (!isPermission) {
//                            requestContext.abortWith(responseBuilder.status(403)
//                                    .entity("Access denied for this resource")
//                                    .build());
////                            return;
//                        }
//                        System.out.println("from redis: through");
//                    }
//                } else {
//                    requestContext.abortWith(responseBuilder.status(402)
//                            .entity("Invalid Token, Plase re-login")
//                            .build());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                requestContext.abortWith(responseBuilder.status(500)
//                        .entity("INTERNAL SERVER ERROR")
//                        .build());
//            }
//
//        }
//    }
//
//    public abstract boolean getPermissionByUserId(String userId, int type, String path, String method) throws Exception;
//
//}
