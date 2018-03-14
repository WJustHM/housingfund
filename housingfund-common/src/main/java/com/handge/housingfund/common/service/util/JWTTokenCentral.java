package com.handge.housingfund.common.service.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.configure.Configure;
import com.handge.housingfund.common.service.account.model.TokenData;
import org.apache.commons.configuration2.Configuration;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by xuefei_wang on 17-9-8.
 */
public class JWTTokenCentral {

    private static String USER_ID = "userId";

    private static String USER_TYPE = "userType";

    private static Configuration configuration = Configure.getInstance().getConfiguration(Constant.JWT_CONF);

    private static Algorithm getAlgorithm(String secret) throws UnsupportedEncodingException {

             Algorithm algorithm = Algorithm.HMAC256(configuration.getString("secret",secret));
             return algorithm;

    }
    private static Algorithm getAlgorithm() throws UnsupportedEncodingException {

        return getAlgorithm(configuration.getString("secret",Constant.JWT.secret_defualt));
    }

    /**
     *
     * @param userid
     * @param type
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String generateToken(String userid, int type) throws UnsupportedEncodingException {
        String token = JWT.create()
                .withIssuer(configuration.getString(Constant.JWT.issuer,Constant.JWT.secret_defualt))
                .withIssuedAt(new Date())
                .withClaim(USER_ID, userid)
                .withClaim(USER_TYPE, type)
                .withExpiresAt(new Date( new Date().getTime() + configuration.getLong(Constant.JWT.expires,Constant.JWT.expires_defualt)))
                .sign(getAlgorithm());
        return token;
    }

    /**
     *
     * @param userid
     * @param type
     * @param secret
     * @return
     * @throws UnsupportedEncodingException
     */
    @Deprecated
    public static String generateToken(String userid, int type ,String secret) throws UnsupportedEncodingException {
        String token = JWT.create()
                .withIssuer(configuration.getString(Constant.JWT.issuer,Constant.JWT.secret_defualt))
                .withIssuedAt(new Date())
                .withClaim(USER_ID, userid)
                .withClaim(USER_TYPE, type)
                .withExpiresAt(new Date( new Date().getTime() + configuration.getLong(Constant.JWT.expires,Constant.JWT.expires_defualt)))
                .sign(getAlgorithm(secret));
        return token;
    }

    /**
     *
     * @param token
     * @return
     */
    public static TokenData decodeToken(String token){
        TokenData tokenData = new TokenData();
        try {
            Algorithm algorithm = getAlgorithm();
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(configuration.getString(Constant.JWT.issuer,Constant.JWT.secret_defualt))
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            tokenData.setCode(1);
            tokenData.setExpiresAt(jwt.getExpiresAt());
            tokenData.setIssuedAt(jwt.getIssuedAt());
            tokenData.setIssuer(jwt.getIssuer());
            tokenData.setType(jwt.getClaim(USER_TYPE).asInt());
            tokenData.setUserid(jwt.getClaim(USER_ID).asString());
        } catch (Exception e) {
            tokenData.setCode(0);
            tokenData.setMsg(e.getMessage());
        }
        return tokenData;
    }

    /**
     *
     * @param token
     * @param secret
     * @return
     */
    @Deprecated
    public static TokenData decodeToken(String token,String secret){
        TokenData tokenData = new TokenData();
        try {
            Algorithm algorithm = getAlgorithm(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(configuration.getString(Constant.JWT.issuer,Constant.JWT.secret_defualt))
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            tokenData.setCode(1);
            tokenData.setExpiresAt(jwt.getExpiresAt());
            tokenData.setIssuedAt(jwt.getIssuedAt());
            tokenData.setIssuer(jwt.getIssuer());
            tokenData.setType(jwt.getClaim(USER_TYPE).asInt());
            tokenData.setUserid(jwt.getClaim(USER_ID).asString());
        } catch (Exception e) {
            tokenData.setCode(0);
            tokenData.setMsg(e.getMessage());
        }
        return tokenData;
    }

}
