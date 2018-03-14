package com.handge.housingfund.common.service.util;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.configure.Configure;
import com.handge.housingfund.common.configure.RedisConfKeyConst;
import org.apache.commons.configuration2.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by xuefei_wang on 17-9-5.
 */
public class RedisCache {

    static Configuration config = Configure.getInstance().getConfiguration(Constant.SERVER_CONF);

    private static volatile RedisCache redisCache = null;

    private static Set<HostAndPort> hostAndPortSet = new HashSet<>();

    private RedisCache() {
        this(config);
    }
    private RedisCache(Configuration config) {
        List<Object> clusters = config.getList(RedisConfKeyConst.REDIS_CLUSTER_HOST_PORT);
        for (Object cluster : clusters) {
            String[] host_port = cluster.toString().trim().split(":");
            hostAndPortSet.add(new HostAndPort(host_port[0], Integer.valueOf(host_port[1])));
        }
    }

    public static RedisCache getRedisCacheInstance() {
        if (redisCache == null) {
            synchronized (RedisCache.class) {
                if (redisCache == null) {
                    redisCache = new RedisCache();
                }
            }
        }
        return redisCache;
    }

    public static RedisCache getRedisCacheInstance(Configuration config) {
        if (redisCache == null) {
            synchronized (RedisCache.class) {
                if (redisCache == null) {
                    redisCache = new RedisCache(config);
                }
            }
        }
        return redisCache;
    }


    public JedisPool getRedisPool() throws Exception {
        if (hostAndPortSet.size() == 0) {
            throw new Exception("请初始化Ｒｅｄｉｓ服务地址：" + RedisConfKeyConst.REDIS_CLUSTER_HOST_PORT);
        } else if (hostAndPortSet.size() == 1) {
            HostAndPort hp = hostAndPortSet.iterator().next();
            JedisPool pool = new JedisPool(hp.getHost(), hp.getPort());
            return pool;
        }
        HostAndPort hp = hostAndPortSet.iterator().next();
        System.out.println("存在" + hostAndPortSet.size() + " 个ｒｅｄｉｓ服务器，选择：" + hp.getHost() + ":" + hp.getPort());
        JedisPool pool = new JedisPool(hp.getHost(), hp.getPort());
        return pool;


    }

    public Jedis getRedis() throws Exception {
        return getRedisPool().getResource();
    }

    public JedisCluster getJedisCluster() throws Exception {
        if (hostAndPortSet.size() == 0) {
            throw new Exception("请初始化Ｒｅｄｉｓ服务地址：" + RedisConfKeyConst.REDIS_CLUSTER_HOST_PORT);
        }
        JedisCluster cluster = new JedisCluster(hostAndPortSet);
        return cluster;
    }
}
