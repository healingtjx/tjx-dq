package com.delayed.base.utils;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @作者: tjx
 * @描述: redis操作工具类
 * @创建时间: 创建于14:45 2020/7/29
 **/
public class RedisUtils {

    // 连接池
    private static JedisPool jedisPool = null;

    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_ACTIVE = 1012;
    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 200;
    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static long MAX_WAIT = 10000;
    private static int TIMEOUT = 10000;
    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;

    /**
     * 实例化
     * @param url 格式  host:port:auth
     */
    public static void initialize(String url){
        //请空
        jedisPool = null;
        if(StringUtil.isNull(url))
            return;
        //拆分字符串
        String[] split = url.split(":");
        int length = split.length;
        if(!(length == 3 || length == 2 ))
            return;
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(MAX_ACTIVE);
        config.setMaxIdle(MAX_IDLE);
        config.setMaxWaitMillis(MAX_WAIT);
        config.setTestOnBorrow(TEST_ON_BORROW);
        if(length == 2)
            jedisPool = new JedisPool(config, split[0], Integer.parseInt(split[1]), TIMEOUT); //, AUTH 设置密码后加上AUTH参数
        else
            jedisPool = new JedisPool(config, split[0], Integer.parseInt(split[1]), TIMEOUT,split[2]); //, AUTH 设置密码后加上AUTH参数
    }

    /**
     * 判断redis是否被实例化
     * @return
     */
    public static boolean checkStatus(){
        //校验参数
         try {
             jedisPool.getResource();
         }catch (Exception e){
             jedisPool = null;
             return false;
         }
        if(jedisPool != null)
            return true;
        return false;
    }



}
