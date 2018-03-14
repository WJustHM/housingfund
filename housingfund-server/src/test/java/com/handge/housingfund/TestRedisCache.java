package com.handge.housingfund;

import com.handge.housingfund.common.service.util.RedisCache;
import redis.clients.jedis.JedisCluster;

import java.util.Set;

/**
 * Created by xuefei_wang on 17-9-5.
 */
public class TestRedisCache {
    public static void main(String[] args) throws Exception {
//        RedisCache.getRedisCacheInstance().getJedisCluster().flushDB();
        RedisCache redisCache = RedisCache.getRedisCacheInstance();
        JedisCluster cluster = redisCache.getJedisCluster();

        cluster.sadd("k","tt");
        cluster.sadd("k","tt1");
        cluster.sadd("k","tt2");
        cluster.sadd("k","tt3");
        cluster.sadd("k","tt4");
        Set<String> vs  = cluster.smembers("k");


        for (String v : vs){
            System.out.println(v);
}

    }

}
