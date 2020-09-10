package com.delayed.base.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Collections;
import java.util.Set;

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

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;

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

    public static void  del(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
             jedis.del(key);
        }catch (Exception e){
            return;
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }

    public static String get(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        }catch (Exception e){
            return null;
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }


    /**
     * spop 获取set元素最上面的
     * @param key
     * @return
     */
    public static String spop(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.spop(key);
        }catch (Exception e){
            return null;
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }

    /**
     * 自增
     * @param key
     * @return
     */
    public static long incr(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.incr(key);
        }catch (Exception e){
            return 0;
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }


    /**
     * KV 形式添加
     * @param key
     * @param value
     * @return
     */
    public static boolean setKV(String key,String value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            return true;
        }catch (Exception e){
            return false;
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }


    /**
     * set队列 插入
     * @param key
     * @param value
     * @return
     */
    public static boolean sadd(String key,String value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.sadd(key, value)>0?true:false;
        }catch (Exception e){
            return false;
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }

    /**
     * Zset 添加 一个元素
     * @param key
     * @param value
     * @return
     */
    public static boolean zaddOne(String key,String value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zadd(key, 1, value) >0?true:false;
        }catch (Exception e){
            return false;
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }


    public static boolean zrem (String key,String value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zrem(key, value) >0?true:false;
        }catch (Exception e){
            return false;
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }


    /**
     * 获取zset 内容分页
     * @param key
     * @param start  起始值
     * @param end    结束值(-1代表查所有)
     * @return
     */
    public static Set<String> zrange(String key,int start ,int end){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zrange(key, start, end);
        }catch (Exception e){
            return null;
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }





    /**
     * 尝试获取分布式锁
     * @param lockKey      锁
     * @param requestId    请求标识
     * @param expireTime   超期时间
     * @return  是否获取成功
     */
    public static boolean tryGetDistributedLock( String lockKey, String requestId, int expireTime){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);

            if (LOCK_SUCCESS.equals(result)) {
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }


    /**
     * 释放分布式锁
     * @param lockKey    锁
     * @param requestId  请求标识
     * @return
     */
    public static boolean releaseDistributedLock( String lockKey, String requestId){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
            if (RELEASE_SUCCESS.equals(result)) {
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }

    public static void main(String[] args) {
        RedisUtils.initialize("127.0.0.1:6379:");
        System.out.println(RedisUtils.zaddOne("test","tjx1"));
    }
}
