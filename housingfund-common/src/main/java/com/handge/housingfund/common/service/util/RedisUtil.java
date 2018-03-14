//package com.handge.housingfund.common.service.util;
//
//import org.apache.commons.configuration.CompositeConfiguration;
//import redis.clients.jedis.HostAndPort;
//import redis.clients.jedis.JedisCluster;
//
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * Created by tanyi on 2017/7/10.
// */
//
//
///**
// * @deprecated   changed by wangxuefei .
// *   {@link com.handge.housingfund.common.service.util.RedisCache}
// */
//@Deprecated
//public class RedisUtil {
//    //    private static JedisPool jedisPool = null;
//    private static CompositeConfiguration conf = PropertiesUtil.getConfiguration("redis.properties");
//    private static String host = conf.getString("redis.host");
//    private static int port = conf.getInt("redis.port");
//    private static String password = conf.getString("redis.password");
//    private static int timeout = conf.getInt("redis.timeout");
//    private static int database = conf.getInt("redis.database");
//
//    //redis cluster mode
//    private static JedisCluster jedisCluster = null;
//    private static Set<HostAndPort> jedisClusterNodes = new HashSet<>();
//
//    private static String header = "housingfund_";
//
//    /**
//     * 初始化Redis连接池
//     */
//    static {
////        Assert.notNull(host, "host不能为空，请检查redis.properties配置文件");
////        Assert.notNull(port, "port不能为空，请检查redis.properties配置文件");
//        password = password.equals("null") ? null : password;
////        Assert.notNull(timeout, "timeout不能为空，请检查redis.properties配置文件");
////        Assert.notNull(database, "database不能为空，请检查redis.properties配置文件");
////        try {
////            JedisPoolConfig config = new JedisPoolConfig();
////            config.setMaxTotal(-1);
////            config.setMaxIdle(200);
////            config.setMaxWaitMillis(10 * 1000);
////            config.setTestOnBorrow(true);
////            jedisPool = new JedisPool(config, host, port, timeout, password, database);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//    }
//
//    /**
//     * 获取Jedis实例
//     *
//     * @return
//     */
//    public synchronized static JedisCluster getJedis() throws Exception {
//        if (jedisCluster == null) {
//            jedisClusterNodes.add(new HostAndPort(host, port));
//            jedisCluster = new JedisCluster(jedisClusterNodes);
//        }
//
//        return jedisCluster;
//    }
////
////    /**
////     * 释放jedis资源
////     *
////     * @param jedis
////     */
////    public static void returnResource(final Jedis jedis) {
////        if (jedis != null) {
////            jedisPool.returnResource(jedis);
////        }
////    }
//
//    public static String set(String key, String value) throws Exception {
//        String res = getJedis().set(header + key, value);
//        return res;
//    }
//
//    public static String setex(String key, int seconds, String data) throws Exception {
//        String res = getJedis().setex(header + key, seconds, data);
//        return res;
//    }
//
//
//    public static boolean exists(String key) throws Exception {
//        Boolean res = getJedis().exists(header + key);
//        return res;
//    }
//
//    public static long sadd(String key, String value) throws Exception {
//        long res = getJedis().sadd(header + key, value);
//        return res;
//    }
//
//    public static long expire(String key, int seconds) throws Exception {
//        long res = getJedis().expire(header + key, seconds);
//        return res;
//    }
//
//    public static boolean sismember(String key, String value) throws Exception {
//        boolean res = getJedis().sismember(header + key, value);
//        return res;
//    }
//
//    public static Set<String> smembers(String key) throws Exception {
//        Set<String> res = getJedis().smembers(header + key);
//        return res;
//    }
//
//    public static long del(String key) throws Exception {
//        return getJedis().del(header + key);
//    }
//}
