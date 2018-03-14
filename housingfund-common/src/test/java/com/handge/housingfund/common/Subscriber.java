package com.handge.housingfund.common;

import com.handge.housingfund.common.service.util.RedisCache;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPubSub;

public class Subscriber extends JedisPubSub {

    public void onMessage(String channel, String message) {

        System.out.println(" onMessage  --  "+channel + "   " + message);

    }

    public void onPMessage(String pattern, String channel, String message) {
        System.out.println(" onPMessage  --  " +pattern +"     "+channel + "   " + message);


    }

    public void onSubscribe(String channel, int subscribedChannels) {

        System.out.println(" onSubscribe  --  "+channel + "   " + subscribedChannels);
    }

    public void onUnsubscribe(String channel, int subscribedChannels) {


        System.out.println(" onUnsubscribe  --  "+channel + "   " + subscribedChannels);
    }

    public void onPUnsubscribe(String pattern, int subscribedChannels) {

        System.out.println(" onPUnsubscribe  --  "+ pattern +"    " + subscribedChannels);

    }

    public void onPSubscribe(String pattern, int subscribedChannels) {

        System.out.println(" onPSubscribe  --  "+ pattern +"    " + subscribedChannels);
    }

    public void onPong(String pattern) {

        System.out.println(" onPong  --  "+ pattern );

    }

    public static void main(String[] args) throws Exception {
        RedisCache cache = RedisCache.getRedisCacheInstance();
        JedisCluster cluster = cache.getJedisCluster();
        cluster.subscribe(new Subscriber(),"__keyevent@0__:expired");
    }
}
