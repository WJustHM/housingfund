package com.handge.housingfund.common;

import com.handge.housingfund.common.service.util.RedisCache;
import redis.clients.jedis.JedisCluster;

public class Publisher {
    public static void main(String[] args) throws Exception {

        RedisCache cache = RedisCache.getRedisCacheInstance();

        JedisCluster cluster = cache.getJedisCluster();
        int i = 0;
        while (true) {

            cluster.set("notify", "message"+i);
            cluster.expire(String.valueOf(i), 1);
            cluster.publish("notify", "message"+i);

            System.out.println("publisher :  message" +i );
            i++;
            Thread.sleep(1000);


        }
    }
}
