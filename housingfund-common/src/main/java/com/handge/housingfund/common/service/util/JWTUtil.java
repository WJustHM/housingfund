//package com.handge.housingfund.common.service.util;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.JWTCreationException;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import com.handge.housingfund.common.service.account.model.TokenData;
//import org.apache.commons.configuration.CompositeConfiguration;
//
//import java.io.UnsupportedEncodingException;
//import java.util.Date;
//
///**
// * Created by tanyi on 2017/7/11.
// */
//
///**
// * @deprecated   changed by wangxuefei .
// * {@link com.handge.housingfund.common.service.util.JWTTokenCentral}
// */
//@Deprecated
//public class JWTUtil {
//
//    static CompositeConfiguration config = PropertiesUtil.getConfiguration("jwt.properties");
//    static String key = config.getString("key");
//    static String issuer = config.getString("issuer");
//    static long expires = config.getLong("expirestime", 10);
//
//    /**
//     * token生成
//     *
//     * @param userid 用户ID
//     * @param type   用户类型(101:邮箱认证)
//     * @return
//     */
//    public static String getToken(String userid, int type) {
////        Assert.notNull(key, "key不能为空，jwt.properties配置文件");
////        Assert.notNull(issuer, "issuer不能为空，jwt.properties配置文件");
//        try {
//            Algorithm algorithm = Algorithm.HMAC256(key);
//            String token = JWT.create()
//                    .withIssuer(issuer)
//                    .withIssuedAt(new Date())
//                    .withClaim("userid", userid)
//                    .withClaim("type", type)
//                    .withExpiresAt(new Date(new Date().getTime() + expires * 60 * 1000))
//                    .sign(algorithm);
//            return token;
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            return null;
//        } catch (JWTCreationException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    /**
//     * 解析token
//     *
//     * @param token string
//     * @return code=1为验证通过，0为验证失败
//     */
//    public static TokenData getData(String token) {
//        TokenData tokenData = new TokenData();
//        try {
//            Algorithm algorithm = Algorithm.HMAC256(key);
//            JWTVerifier verifier = JWT.require(algorithm)
//                    .withIssuer(issuer)
//                    .build();
//            DecodedJWT jwt = verifier.verify(token);
//            tokenData.setCode(1);
//            tokenData.setExpiresAt(jwt.getExpiresAt());
//            tokenData.setIssuedAt(jwt.getIssuedAt());
//            tokenData.setIssuer(jwt.getIssuer());
//            tokenData.setType(jwt.getClaim("type").asInt());
//            tokenData.setUserid(jwt.getClaim("userid").asString());
//        } catch (UnsupportedEncodingException e) {
//            tokenData.setCode(0);
//            tokenData.setMsg(e.getMessage());
//        } catch (JWTVerificationException e) {
//            tokenData.setCode(0);
//            tokenData.setMsg(e.getMessage());
//        }
//        return tokenData;
//    }
//
//}
