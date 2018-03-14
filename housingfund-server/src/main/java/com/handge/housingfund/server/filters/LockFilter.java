package com.handge.housingfund.server.filters;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.configure.Configure;
import com.handge.housingfund.common.service.others.IActionService;
import com.handge.housingfund.common.service.util.RedisCache;
import org.apache.commons.configuration2.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Priority;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by tanyi on 2017/11/21.
 * 接口锁定解除
 */
@Provider
@PreMatching
@Service
@Priority(3000)
public class LockFilter implements ContainerRequestFilter, ContainerResponseFilter {

    RedisCache redisCache = RedisCache.getRedisCacheInstance();
    Configuration serverConf = Configure.getInstance().getConfiguration(Constant.SERVER_CONF);
    int api_rate = serverConf.getInt("api_rate", 5);//api调用频率，每秒次数

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) {
        boolean skip = false;
        if (containerRequestContext.getProperty("skip") != null) {
            skip = (boolean) containerRequestContext.getProperty("skip");
        }
        if (skip == true) {
            return;
        }
        if (containerRequestContext.getMethod().toUpperCase().equals("GET") || containerRequestContext.getMethod().toUpperCase().equals("OPTIONS")) {
            return;
        }
        try {
            Map<String, String> data = (Map<String, String>) containerRequestContext.getProperty("data");
            String md5 = data.get("md5");
            String token = data.get("token");

            //region 接口锁定解除
            JedisCluster redis = redisCache.getJedisCluster();
            String keyLock = "token_" + token + ":lock_" + md5;
            if (redis.exists(keyLock)) {
                redis.del(keyLock);
            }
            redis.close();
        } catch (Exception e) {
            containerRequestContext.abortWith(
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
        }
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        boolean skip = false;
        if (containerRequestContext.getProperty("skip") != null) {
            skip = (boolean) containerRequestContext.getProperty("skip");
        }
        if (skip == true) {
            return;
        }
        String path = containerRequestContext.getUriInfo().getPath();
        String method = containerRequestContext.getMethod().toUpperCase();
        if (method.equals("GET") || method.equals("OPTIONS")) {
            return;
        }
        //region 接口访问频率限制
        try {
            JedisCluster redis = redisCache.getJedisCluster();
            Map<String, String> data = (Map<String, String>) containerRequestContext.getProperty("data");
            String md5 = data.get("md5");
            String token = data.get("token");
            String keyRequestLimit = "token_" + token + ":request_" + md5;
            if (redis.exists(keyRequestLimit)) {
                String requesttimes = redis.get(keyRequestLimit);
                BigDecimal curtimes = new BigDecimal(requesttimes);
                if (curtimes.compareTo(new BigDecimal(api_rate)) >= 0) {
                    redis.close();
                    containerRequestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity("操作太频繁，稍后再试").build());
                    return;
                } else {
                    redis.setex(keyRequestLimit, 1, curtimes.add(BigDecimal.ONE).toString());
                }
            } else {
                redis.setex(keyRequestLimit, 1, "0");
            }

            //region 接口锁定
            String keyLock = "token_" + token + ":lock_" + md5;
            if (redis.exists(keyLock)) {
                redis.close();
                containerRequestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity("操作太频繁，稍后再试").build());
                return;
            }
            redis.setex(keyLock, 60, "1");
            redis.close();
            //endregion
        } catch (Exception e) {
            containerRequestContext.abortWith(
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getStackTrace()).build());
        }
    }
}
