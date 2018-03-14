package com.handge.housingfund.server.filters;

import com.google.gson.Gson;
import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.configure.Configure;
import com.handge.housingfund.common.service.util.ActionUtil;
import com.handge.housingfund.common.service.util.RedisCache;
import org.apache.commons.configuration2.Configuration;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * Created by xuefei_wang on 17-9-4.
 */
@Service
@PreMatching
@Priority(1000)
public class AuthenticationToken implements ContainerRequestFilter {

    public AuthenticationToken() {
    }

    List<String> notoken_urls = Arrays.asList("/account/login", "/account/loginByCa", "/account/resetPwdSendEmail",
            "/account/resetPwd");

    Configuration serverConf = Configure.getInstance().getConfiguration(Constant.SERVER_CONF);

    RedisCache redisCache = RedisCache.getRedisCacheInstance();

    int expires = serverConf.getInt("token_expirestime", 1800);

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String path = requestContext.getUriInfo().getPath();
        String method = requestContext.getMethod().toUpperCase();
        String[] no_token_urls_str = serverConf.getString(Constant.Server.no_token_urls).split(",");
        List<String> no_token_urls = new ArrayList<String>();
        Map<String, String> data = null;
        if (no_token_urls_str.length == 0) {
            no_token_urls = notoken_urls;
        } else {
            no_token_urls = Arrays.asList(no_token_urls_str);
        }
        if (no_token_urls.contains(path)) {
            requestContext.setProperty("skip", true);
            return;
        }
        String token = requestContext.getHeaderString(Constant.Server.TOKEN_HEAD);
        if (null == token || token.isEmpty()) {
            token = requestContext.getUriInfo().getQueryParameters().getFirst(Constant.Server.TOKEN_HEAD);
        }
        if (null == token || token.isEmpty()) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).entity(Constant.Server.TOKEN_HEAD + " invalid").build());
            return;
        }
        try {

            String key = "token_" + token + ":data";
            JedisCluster redis = redisCache.getJedisCluster();
            if (redis.exists(key)) {
                data = redis.hgetAll(key);
                redis.expire(key, expires);
                redis.close();
            } else {
                redis.close();
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity(Constant.Server.TOKEN_HEAD + " invalid").build());
                return;
            }
            data.put("token", token);
            Gson gson = new Gson();
            HashSet<String> keywords = gson.fromJson(data.get("keywords"), HashSet.class);
            // 计算HTTP Method+Path的MD5
            String md5 = ActionUtil.getUrlMD5(method, path, keywords);
            data.put("md5", md5);
            HashSet<String> actionCodes = gson.fromJson(data.get("actionCodes"), HashSet.class);
            boolean access = actionCodes.contains(md5);
            if (!access) {
                requestContext.abortWith(
                        Response.status(Response.Status.FORBIDDEN).entity("无访问权限").build());
                return;
            }
            requestContext.setProperty("data", data);
        } catch (Exception e) {
            requestContext.abortWith(
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
        }
    }
}
